/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.business.service.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.ManagedException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.RestTemplateUtils;
import it.csi.cosmo.cosmobe.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmobe.business.service.DiscoveryDeclarativeFallbackService;
import it.csi.cosmo.cosmobe.config.Constants;
import it.csi.cosmo.cosmobe.config.ParametriApplicativo;
import it.csi.cosmo.cosmobe.util.logger.LogCategory;
import it.csi.cosmo.cosmobe.util.logger.LoggerFactory;


@Service
public class DiscoveryDeclarativeFallbackServiceImpl
implements DiscoveryDeclarativeFallbackService, InitializingBean, DisposableBean {

  private static final String URL_SEPARATOR = "/";

  private static final CosmoLogger LOGGER =
      LoggerFactory.getLogger(LogCategory.DISCOVERY_LOG_CATEGORY,
          "DiscoveryDeclarativeFallbackServiceImpl");

  private static final String CONTEXT_PREFIX = Constants.PRODUCT;

  private static final long MAX_CACHE_LIFE = 1 * 60 * 1000L;

  @Autowired
  private ConfigurazioneService configurazioneService;

  private RestTemplate restTemplate;

  private Map<String, Object> semaphores;

  private Map<String, AlternativeLocation> discoveryCache;

  @Override
  public boolean isEnabled() {
    return configurazioneService.requireConfig(ParametriApplicativo.DISCOVERY_FETCH_FALLBACK_ENABLE)
        .asBool();
  }

  public DiscoveryDeclarativeFallbackServiceImpl() {
    semaphores = new HashMap<>();
    discoveryCache = new HashMap<>();

    //@formatter:off
    restTemplate = RestTemplateUtils.builder()
        .withAllowConnectionReuse(false)
        .withReadTimeout(30000)
        .build();
    //@formatter:on
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (isEnabled()) {
      LOGGER.info("afterPropertiesSet", "initializing discovery fetch fallback service");
    }
  }

  @Override
  public void destroy() throws Exception {
    LOGGER.info("destroy", "destroying discovery fetch fallback service");
  }

  @Override
  public URI getLocationWithFallbackDiscovery(String urlSegment) {
    String method = "getLocationWithFallbackDiscovery";
    LOGGER.debug(method, "getting fallback location for url segment " + urlSegment);

    String lockKey = urlSegment.trim().toUpperCase();
    semaphores.putIfAbsent(lockKey, new Object());

    synchronized (semaphores.get(lockKey)) {
      // check if cached
      AlternativeLocation cached = getFromCacheIfValid(lockKey);
      if (cached != null) {
        LOGGER.debug(method, "returning cached fallback location for url segment " + urlSegment);
        return cached.baseLocation;
      }

      try {
        cached = attemptDiscovery(urlSegment);
        LOGGER.info(method, "computed fallback location for url segment " + urlSegment + " at "
            + cached.baseLocation.toString() + ", saving to cache");
        discoveryCache.put(lockKey, cached);
        return cached.baseLocation;

      } catch (Exception e) {
        LOGGER.error(method, "error computing fallback location for url segment " + urlSegment, e);
        return null;
      }
    }
  }

  private AlternativeLocation getFromCacheIfValid(String lockKey) {
    String method = "getFromCacheIfValid";
    AlternativeLocation inCache = discoveryCache.getOrDefault(lockKey, null);

    if (inCache != null) {
      if (inCache.discoveryTime.until(OffsetDateTime.now(), ChronoUnit.MILLIS) > MAX_CACHE_LIFE) {
        LOGGER.info(method, "cached discovery result expired for key " + lockKey);
        discoveryCache.remove(lockKey);
        return null;
      } else {
        LOGGER.debug(method, "cached discovery result for key " + lockKey + " is still valid");
      }
    } else {
      LOGGER.debug(method, "no cached discovery result for key " + lockKey);
    }

    return inCache;
  }

  public AlternativeLocation attemptDiscovery(String urlSegment) {
    String method = "attemptDiscovery";

    LOGGER.info(method, "attempting fallback discovery for url segment " + urlSegment);

    List<AlternativeLocation> alternatives = buildAlternatives(urlSegment);
    if (alternatives.isEmpty()) {
      LOGGER.warn(method, "no alternatives computed to attempt discovery on");
      return null;
    }

    LOGGER.debug(method, "will attempt discovery for url segment " + urlSegment + " on "
        + alternatives.size() + " possible targets");

    List<AlternativeLocation> responding = findRespondingAlternatives(alternatives);

    if (responding.isEmpty()) {
      LOGGER.warn(method, "none of the alternatives did respond, no valid target");
      return null;
    }

    // return instance with top-score
    AlternativeLocation result = getTopScoring(responding).orElseThrow();

    LOGGER.info(method, "returning top-scoring responding instance at location "
        + result.baseLocation.toString() + " discovered from endpoint " + result.testEndpoint.toString());

    return result;
  }

  private Optional<AlternativeLocation> getTopScoring(List<AlternativeLocation> responding) {

    // return instance with top-score
    return responding.stream()
        .sorted((i1, i2) -> i2.score - i1.score)
        .findFirst();
  }

  private List<AlternativeLocation> findRespondingAlternatives(
      List<AlternativeLocation> alternatives) {

    String method = "findRespondingAlternatives";

    if (LOGGER.isDebugEnabled() && !alternatives.isEmpty()) {
      LOGGER.debug(method, "will attempt fetching of following locations: " + alternatives.stream()
      .map(o -> o.testEndpoint.toString()).collect(Collectors.joining(", ")));
    }

    List<AlternativeLocation> output = Collections.synchronizedList(new ArrayList<>());

    int numThreads = Math.max(alternatives.size(), 4);

    ExecutorService parallelExecutor = Executors.newFixedThreadPool(numThreads);
    List<Callable<Void>> tasks = new ArrayList<>();

    for (final AlternativeLocation alternative : alternatives) {
      tasks.add(() -> {
        attemptFetch(alternative, output);
        return null;
      });
    }

    LOGGER.debug(method, "launching parallel fetch now");

    try {
      parallelExecutor.invokeAll(tasks, 5000, TimeUnit.MILLISECONDS);
      parallelExecutor.shutdown();

      LOGGER.debug(method, "parallel fetch completed");

    } catch (InterruptedException e) { // NOSONAR
      LOGGER.error(method, "parallel fetch interrupted", e);
      throw new InternalServerException("Fallback discovery was interrupted");
    }

    LOGGER.debug(method, "parallel fetch resulted in " + output.size() + " responding instances");

    return output;
  }

  private void attemptFetch(AlternativeLocation alternative, List<AlternativeLocation> output) {
    String method = "attemptFetch";
    String urlString = alternative.testEndpoint.toString();

    LOGGER.info(method, "fetching from URL " + urlString);

    try {
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Type", "application/json");
      headers.add("Accept", "application/json");
      HttpEntity<?> entity = new HttpEntity<>(null);

      ParameterizedTypeReference<HashMap<String, Object>> responseType =
          new ParameterizedTypeReference<HashMap<String, Object>>() {};

          ResponseEntity<HashMap<String, Object>> getOutput = this.restTemplate
              .exchange(alternative.testEndpoint,
                  HttpMethod.GET, entity, responseType);

          if (getOutput != null && getOutput.getStatusCode().is2xxSuccessful()) {
            LOGGER.info(method, "got valid output with status " + getOutput.getStatusCodeValue() +
                " from URL " + urlString + ", adding " + alternative.baseLocation.toString() + " as a valid target");
            output.add(alternative);

          } else if (getOutput != null) {
            LOGGER.warn(method, "got invalid output with status " + getOutput.getStatusCodeValue() +
                " from URL " + urlString + ", not counting as a valid target");
            throw ManagedException.withMessage(HttpStatus.NO_CONTENT, "invalid response");

          } else {
            LOGGER.warn(method, "got EMPTY output from URL " + urlString + ", not counting as a valid target");
            throw ManagedException.withMessage(HttpStatus.NO_CONTENT, "empty response");
          }

    } catch (Exception e) {
      LOGGER.info(method, "fetching failed from URL " + urlString + ": "
          + e.getClass().getSimpleName() + " - " + e.getMessage());

      throw e;
    }
  }

  private List<AlternativeLocation> buildAlternatives(String urlSegment) {

    List<String> baseLocations = configurazioneService.requireConfig(ParametriApplicativo.DISCOVERY_FETCH_FALLBACK_LOCATIONS).asStringList(",");
    List<AlternativeLocation> output = new ArrayList<>();

    int locationScore = 10;
    for (String baseLocation: baseLocations) {
      output.add(new AlternativeLocation(baseLocation + URL_SEPARATOR + CONTEXT_PREFIX + urlSegment,
          "/api/ping", 10 * locationScore + 4));
      output.add(new AlternativeLocation(baseLocation + URL_SEPARATOR + CONTEXT_PREFIX + urlSegment,
          "/api/status", 10 * locationScore + 3));
      output.add(new AlternativeLocation(baseLocation + URL_SEPARATOR + urlSegment, "/api/ping",
          10 * locationScore + 2));
      output.add(new AlternativeLocation(baseLocation + URL_SEPARATOR + urlSegment, "/api/status",
          10 * locationScore + 1));

      if (locationScore > 1) {
        locationScore--;
      }
    }

    return output;
  }

  private static class AlternativeLocation {

    private URI baseLocation;
    private URI testEndpoint;
    private int score;
    private OffsetDateTime discoveryTime;

    public AlternativeLocation(String baseLocation, String testEndpoint, int score) {
      super();
      this.score = score;
      this.discoveryTime = OffsetDateTime.now();
      try {
        this.baseLocation = new URI(baseLocation).normalize();
        this.testEndpoint = new URI(baseLocation + URL_SEPARATOR + testEndpoint).normalize();
      } catch (URISyntaxException e) {
        throw new InternalServerException("Fallback discovery could not be attempted");
      }
    }

  }
}

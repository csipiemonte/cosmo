/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.business.service.impl;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.discovery.fetch.DiscoveryAnalyzer;
import it.csi.cosmo.common.discovery.model.DiscoveredService;
import it.csi.cosmo.common.discovery.model.DiscoveryRegistry;
import it.csi.cosmo.common.dto.common.ServiceStatusDTO;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.monitoring.Monitorable;
import it.csi.cosmo.cosmobe.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmobe.business.service.DiscoveryFetchService;
import it.csi.cosmo.cosmobe.config.ParametriApplicativo;
import it.csi.cosmo.cosmobe.dto.discovery.CachedDiscoveryServices;
import it.csi.cosmo.cosmobe.integration.rest.CosmoDiscoveryFeignClient;
import it.csi.cosmo.cosmobe.util.logger.LogCategory;
import it.csi.cosmo.cosmobe.util.logger.LoggerConstants;
import it.csi.cosmo.cosmobe.util.logger.LoggerFactory;


@Service
public class DiscoveryFetchServiceImpl
implements DiscoveryFetchService, InitializingBean, DisposableBean, Monitorable {

  private static final CosmoLogger LOGGER =
      LoggerFactory.getLogger(LogCategory.DISCOVERY_LOG_CATEGORY, "DiscoveryFetchServiceImpl");

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoDiscoveryFeignClient cosmoDiscoveryFeignClient;

  private ScheduledExecutorService heartbeatExecutor;

  private CachedDiscoveryServices cache;

  private DiscoveryAnalyzer analyzer = new DiscoveryAnalyzer(LoggerConstants.ROOT_LOG_CATEGORY);

  private boolean isEnabled() {
    return configurazioneService.requireConfig(ParametriApplicativo.DISCOVERY_FETCH_ENABLE)
        .asBool();
  }

  public DiscoveryFetchServiceImpl() {
    cache = new CachedDiscoveryServices();
    heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
  }

  @Override
  public ServiceStatusDTO checkStatus() {
    if (this.cache == null) {
      return ServiceStatusDTO.warning().withMessage("No cached registry data available").build();
    } else if (this.cache.hasData()) {
      return ServiceStatusDTO.up()
          .withDetail("knownServices", this.cache.getRegistry().getServices().size()).build();
    } else {
      return ServiceStatusDTO.warning().withMessage("No registry data available").build();
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (isEnabled()) {
      long interval = configurazioneService
          .requireConfig(ParametriApplicativo.DISCOVERY_FETCH_INTERVAL).asLong();
      heartbeatExecutor.scheduleAtFixedRate(this::registryFetchTick, 1000,
          interval, TimeUnit.MILLISECONDS);
    }
  }

  @Override
  public void destroy() throws Exception {
    LOGGER.info("destroy", "destroying discovery fetch service");
    if (heartbeatExecutor != null) {
      heartbeatExecutor.shutdownNow();
    }
  }

  @Override
  public Set<DiscoveredService> getServices() {
    final var methodName = "getServices";

    if (!cache.hasData()) {
      LOGGER.warn(methodName, "no computed service status available");
      return Collections.emptySet();
    }

    LOGGER.info(methodName, "returning computed service status from registry");
    var results = cache.getAnalyzedResults();

    return results != null ? results.getServices() : Collections.emptySet();
  }

  private void registryFetchTick() {
    String method = "registryFetch";
    try {
      this.registryFetch();
    } catch (Throwable t) { // NOSONAR

      String message = "error in registryFetch task";
      if (LOGGER.isDebugEnabled()) {
        LOGGER.error(method, message, t);
      } else {
        LOGGER.error(method,
            message + ": " + t.getClass().getSimpleName() + " - " + t.getMessage());
      }
    }
  }

  private void registryFetch() {
    String method = "registryFetch";
    LOGGER.debug(method, "fetching registry from discovery server");
    OffsetDateTime now = OffsetDateTime.now();

    try {
      DiscoveryRegistry fetched = cosmoDiscoveryFeignClient.getRegistry();
      cache.setLastFetchTime(now);

      if (LOGGER.isTraceEnabled()) {
        LOGGER.trace(method,
            "fetched registry from discovery server with id " + fetched.getFullId());
      }

      if (!cache.hasData() || !cache.getRegistry().getFullId().equals(fetched.getFullId())) {


        processRegistryChanged(fetched);
      }

    } catch (Exception e) {
      String message = "error fetching registry from discovery server";
      if (LOGGER.isDebugEnabled()) {
        LOGGER.error(method, message, e);
      } else {
        LOGGER.error(method,
            message + ": " + e.getClass().getSimpleName() + " - " + e.getMessage());
      }
    }
  }

  private void processRegistryChanged(DiscoveryRegistry fetched) {
    String method = "processRegistryChanged";
    OffsetDateTime now = OffsetDateTime.now();

    LOGGER.debug(method,
        "fetched registry changed from version "
            + (cache.hasData() ? cache.getRegistry().getFullId() : "<null>") + " to version "
            + fetched.getFullId());

    cache.setRegistry(fetched);
    cache.setLastUpdateTime(now);
    cache.setAnalyzedResults(analyzer.analyze(fetched));

    LOGGER.debug(method, "cached registry updated");
  }

}

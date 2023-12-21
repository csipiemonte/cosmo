/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.discovery.client;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.discovery.model.DiscoveryHeartBeatRequest;
import it.csi.cosmo.common.discovery.model.DiscoveryHeartBeatResponse;
import it.csi.cosmo.common.discovery.model.DiscoveryInstanceConfiguration;
import it.csi.cosmo.common.discovery.model.DiscoveryInstanceReportedStatus;
import it.csi.cosmo.common.exception.BadConfigurationException;
import it.csi.cosmo.common.feignclient.FeignClientProvider;
import it.csi.cosmo.common.security.handler.CosmoAuthenticationConfig;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.RequestUtils;

/**
 *
 */

public class DiscoveryClientHeartbeatHandler implements Serializable, AutoCloseable {

  private static final String URI_SEPARATOR = "/";

  /**
   *
   */
  private static final long serialVersionUID = 9090726473741200184L;

  private transient Logger logger;

  private static final int TICK_INTERVAL = 1000;

  private DiscoveryClientConfiguration configuration;

  private boolean enableHeartbeat = false;

  private int tickCounter = 0;

  private boolean heartbeatInProgress = false;

  private boolean heartbeatAttempted = false;

  private boolean lastHeartbeatOk = false;

  private boolean heartbeatScheduled = false;

  private transient ScheduledExecutorService heartbeatExecutor = null;

  private transient List<Consumer<Exception>> errorCallbacks;

  public DiscoveryClientHeartbeatHandler(DiscoveryClientConfiguration configuration,
      String loggingPrefix) {
    logger = Logger.getLogger((loggingPrefix != null ? loggingPrefix : Constants.PRODUCT)
        + ".discovery.DiscoveryClientHeartbeatHandler");

    this.configuration = configuration;
    this.heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
    errorCallbacks = new ArrayList<>();
  }

  public DiscoveryClientHeartbeatHandler withErrorCallback(Consumer<Exception> callback) {
    errorCallbacks.add(callback);
    return this;
  }

  public boolean getHeartbeatStatus() {
    if (!heartbeatAttempted) {
      return false;
    }
    return lastHeartbeatOk;
  }

  @Override
  public void close() throws Exception {
    if (this.enableHeartbeat) {
      this.disableHeartbeat();
    }

    if (this.heartbeatExecutor != null) {
      this.heartbeatExecutor.shutdownNow();
    }
  }

  public synchronized void disableHeartbeat() {
    this.enableHeartbeat = false;
    attemptImmediateHeartbeat(DiscoveryInstanceReportedStatus.SHUTDOWN);
  }

  public synchronized void enableHeartbeat(
      Supplier<DiscoveryInstanceReportedStatus> statusProvider) {

    this.enableHeartbeat = true;

    attemptImmediateHeartbeat(DiscoveryInstanceReportedStatus.STARTING);

    if (!heartbeatScheduled) {
      this.heartbeatScheduled = true;

      heartbeatExecutor.scheduleWithFixedDelay(() -> this.everySecondTick(statusProvider),
          TICK_INTERVAL, TICK_INTERVAL, TimeUnit.MILLISECONDS);
    }
  }

  private synchronized void everySecondTick(
      Supplier<DiscoveryInstanceReportedStatus> statusProvider) {
    try {
      doEverySecond(statusProvider);
    } catch (Throwable e) { // NOSONAR

      String message = "[" + this.configuration.getCombinedId() + "] error in tick handler";
      if (logger.isDebugEnabled()) {
        logger.error(message, e);
      } else {
        logger.error(message + ": " + e.getClass().getSimpleName() + " - " + e.getMessage());
      }
    }
  }

  private void doEverySecond(Supplier<DiscoveryInstanceReportedStatus> statusProvider) {
    if (!this.enableHeartbeat) {
      return;
    }
    this.tickCounter += TICK_INTERVAL;

    if (this.tickCounter < getTickTreshold()) {
      return;
    }

    if (this.heartbeatInProgress) {
      logger.warn("skipping heartbeat because another is still in progress");
    }

    String prefix = "[" + this.configuration.getCombinedId() + "] ";

    logger.debug(prefix + "sending automatic heartbeat");

    this.tickCounter = 0;
    heartbeatAttempted = true;
    heartbeatInProgress = true;
    try {
      this.doSendHeartbeatWithErrorLogging(statusProvider.get());
      lastHeartbeatOk = true;
    } catch (Throwable t) { // NOSONAR
      lastHeartbeatOk = false;
    } finally {
      heartbeatInProgress = false;
    }
  }

  public ResponseEntity<DiscoveryHeartBeatResponse> attemptImmediateHeartbeat(
      DiscoveryInstanceReportedStatus status) {

    try {
      return this.doSendHeartbeatWithErrorLogging(status);
    } catch (Throwable e) { // NOSONAR

      String message = "generic error in heartbeat send task";
      if (logger.isDebugEnabled()) {
        logger.error(message, e);
      } else {
        logger.error(message + ": " + e.getClass().getSimpleName() + " - " + e.getMessage());
      }

      return null;
    }
  }

  public Future<ResponseEntity<DiscoveryHeartBeatResponse>> scheduleHeartbeatWithShortDelay(
      DiscoveryInstanceReportedStatus status) {

    return this.heartbeatExecutor.schedule(() -> {

      try {
        return this.doSendHeartbeatWithErrorLogging(status);
      } catch (Throwable e) { // NOSONAR

        String message = "generic error in heartbeat send task";
        if (logger.isDebugEnabled()) {
          logger.error(message, e);
        } else {
          logger.error(message + ": " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        return null;
      }

    }, 10, TimeUnit.MILLISECONDS);
  }

  private Integer getTickTreshold() {
    return lastHeartbeatOk ? getTickTresholdForTriggerWhenHealthy()
        : getTickTresholdForTriggerWhenFailed();
  }

  private Integer getTickTresholdForTriggerWhenHealthy() {
    if (this.configuration == null || this.configuration.getInstanceConfiguration() == null
        || this.configuration.getInstanceConfiguration().getHeartBeatInterval() == null) {

      return null;
    }

    return this.configuration.getInstanceConfiguration().getHeartBeatInterval();
  }

  private Integer getTickTresholdForTriggerWhenFailed() {
    if (this.configuration == null || this.configuration.getInstanceConfiguration() == null
        || this.configuration.getInstanceConfiguration().getHeartBeatInterval() == null) {

      return null;
    }

    Integer val = this.configuration.getInstanceConfiguration().getHeartBeatInterval();
    if (val < DiscoveryInstanceConfiguration.MINIMUM_HEARTBEAT_INTERVAL) {
      val = DiscoveryInstanceConfiguration.MINIMUM_HEARTBEAT_INTERVAL;
    }
    return val;
  }

  private RestTemplate getRestTemplate() {
    return FeignClientProvider.getBasicFeignClientRestTemplate();
  }

  private ResponseEntity<DiscoveryHeartBeatResponse> doSendHeartbeatWithErrorLogging(
      DiscoveryInstanceReportedStatus status) {

    String prefix = "[" + this.configuration.getCombinedId() + "] ";

    try {
      logger.debug(prefix + "sending heartbeat");

      ResponseEntity<DiscoveryHeartBeatResponse> res = this.doSendHeartbeat(status);
      logger.debug(prefix + "heartbeat sent succesfully");
      return res;

    } catch (HttpServerErrorException e) {

      handleServerErrorException(prefix, e);
      throw e;

    } catch (HttpClientErrorException e) {

      handleClientErrorException(prefix, e);
      throw e;

    } catch (Exception e) {
      handleGenericException(prefix, e);
      throw e;
    }
  }

  private void handleServerErrorException(String prefix, HttpServerErrorException e) {
    String message = prefix + "HTTP server error sending heartbeat: " + e.getStatusCode().value()
        + " " + e.getStatusText() + " - " + e.getResponseBodyAsString();
    if (logger.isDebugEnabled()) {
      logger.error(message, e);
    } else {
      logger.error(message + ": " + e.getClass().getSimpleName() + " - " + e.getMessage());
    }

    if (!this.errorCallbacks.isEmpty()) {
      this.errorCallbacks.forEach(callback -> callback.accept(e));
    }
  }

  private void handleClientErrorException(String prefix, HttpClientErrorException e) {
    String message = prefix + "HTTP client error sending heartbeat: " + e.getStatusCode().value()
        + " " + e.getStatusText() + " - " + e.getResponseBodyAsString();
    if (logger.isDebugEnabled()) {
      logger.error(message, e);
    } else {
      logger.error(message + ": " + e.getClass().getSimpleName() + " - " + e.getMessage());
    }

    if (!this.errorCallbacks.isEmpty()) {
      this.errorCallbacks.forEach(callback -> callback.accept(e));
    }
  }

  private void handleGenericException(String prefix, Exception e) {
    String message = prefix + "error sending heartbeat";
    if (logger.isDebugEnabled()) {
      logger.error(message, e);
    } else {
      logger.error(message + ": " + e.getClass().getSimpleName() + " - " + e.getMessage());
    }

    if (!this.errorCallbacks.isEmpty()) {
      this.errorCallbacks.forEach(callback -> callback.accept(e));
    }
  }

  private ResponseEntity<DiscoveryHeartBeatResponse> doSendHeartbeat(
      DiscoveryInstanceReportedStatus status) {

    DiscoveryHeartBeatRequest requestBody =
        DiscoveryHeartBeatRequest.builder().withService(configuration.getServiceConfiguration())
        .withInstance(configuration.getInstanceConfiguration()).withStatus(status).build();

    if (logger.isTraceEnabled()) {
      logger.trace("preparing request body for heartbeat: " + ObjectUtils.represent(requestBody));
    }

    URI targetURI;
    try {
      targetURI = new URI(configuration.getDiscoveryServerEndpoint().toString() + URI_SEPARATOR
          + configuration.getHeartbeatEndpoint()).normalize();
    } catch (URISyntaxException e) {
      throw new BadConfigurationException(
          "invalid DiscoveryServerEndpoint/HeartbeatEndpoint configuration", e);
    }

    logger.debug("sending heartbeat to " + targetURI.toString());

    String authHeader = "Basic " + Base64.getEncoder()
    .encodeToString((configuration.getUsername() + ":" + configuration.getPassword())
        .getBytes(CosmoAuthenticationConfig.ENCODING));

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", authHeader);
    headers.add(it.csi.cosmo.common.config.Constants.HEADERS_PREFIX + "Sender-Name",
        configuration.getServiceConfiguration().getName());
    headers.add(it.csi.cosmo.common.config.Constants.HEADERS_PREFIX + "Sender-Instance",
        configuration.getInstanceConfiguration().getInstanceId());
    headers.add("Date", RequestUtils.formatDateHeader(OffsetDateTime.now()));

    if (logger.isTraceEnabled()) {
      logger.trace("adding authorization header for heartbeat with username [ "
          + configuration.getUsername() + " ]");
    }

    HttpEntity<DiscoveryHeartBeatRequest> entity = new HttpEntity<>(requestBody, headers);

    return getRestTemplate().postForEntity(targetURI, entity, DiscoveryHeartBeatResponse.class);
  }

}

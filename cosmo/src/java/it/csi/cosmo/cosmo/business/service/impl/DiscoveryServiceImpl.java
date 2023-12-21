/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.business.service.impl;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.discovery.fetch.DiscoveryAnalyzer;
import it.csi.cosmo.common.discovery.model.DiscoveredInstanceStatus;
import it.csi.cosmo.common.discovery.model.DiscoveredServices;
import it.csi.cosmo.common.discovery.model.DiscoveryHeartBeatRequest;
import it.csi.cosmo.common.discovery.model.DiscoveryHeartBeatResponse;
import it.csi.cosmo.common.discovery.model.DiscoveryInstanceConfiguration;
import it.csi.cosmo.common.discovery.model.DiscoveryInstanceReportedStatus;
import it.csi.cosmo.common.discovery.model.DiscoveryRegistry;
import it.csi.cosmo.common.discovery.model.DiscoveryRegistryInstanceEntry;
import it.csi.cosmo.common.discovery.model.DiscoveryRegistryServiceEntry;
import it.csi.cosmo.common.discovery.model.DiscoveryServiceConfiguration;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmo.discovery.DiscoveryHeartBeatHelper;
import it.csi.cosmo.cosmo.discovery.DiscoveryRegistryProvider;
import it.csi.cosmo.cosmo.dto.exception.DiscoveryConflictException;
import it.csi.cosmo.cosmo.util.logger.LogCategory;
import it.csi.cosmo.cosmo.util.logger.LoggerConstants;
import it.csi.cosmo.cosmo.util.logger.LoggerFactory;

@Service
public class DiscoveryServiceImpl implements it.csi.cosmo.cosmo.business.service.DiscoveryService,
InitializingBean, DisposableBean {

  private static final CosmoLogger LOGGER =
      LoggerFactory.getLogger(LogCategory.DISCOVERY_LOG_CATEGORY, "DiscoveryServiceImpl");

  private static final int CLUSTER_LOCK_SHORT_OPERATION = 500;

  private static final int CLUSTER_LOCK_ACQ_TIMEOUT = 5000;

  private static final int HEARTBEAT_DEQUEUE_INTERVAL = 3000;

  private static final int HEARTBEAT_DEQUEUE_MAX_PER_TICK = 100;

  private static final double INTERVAL_EVICTION_FACTOR_FOR_SHUTDOWN = 10.0;

  private static final double INTERVAL_EVICTION_FACTOR_FOR_FAILURE = 20.0;

  private static final int EVICTION_TRESHOLD_FOR_INSTANCES_NUMBER = 50;

  @Autowired
  private DiscoveryRegistryProvider discoveryRegistryProvider;

  private ConcurrentLinkedQueue<DiscoveryHeartBeatRequest> heartbeatQueue;

  private ScheduledExecutorService heartbeatExecutor;

  private DiscoveryAnalyzer discoveryAnalyzer =
      new DiscoveryAnalyzer(LoggerConstants.ROOT_LOG_CATEGORY);

  // properties for registry caching
  private Object cachedDiscoveredServicesLock = new Object();
  private DiscoveredServices cachedDiscoveredServices;
  private LocalDateTime cachedDiscoveredServicesExpires;
  private static final int LOCAL_REGISTRY_CACHE_TTL = 15;

  public DiscoveryServiceImpl() {
    LOGGER.info("constructor", "instantiating discovery service");
    heartbeatQueue = new ConcurrentLinkedQueue<>();
    heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    LOGGER.info("afterPropertiesSet", "initializing discovery service");
    heartbeatExecutor.scheduleAtFixedRate(this::heartbeatDequeueTick, HEARTBEAT_DEQUEUE_INTERVAL,
        HEARTBEAT_DEQUEUE_INTERVAL, TimeUnit.MILLISECONDS);
  }

  @Override
  public void destroy() throws Exception {
    LOGGER.info("destroy", "destroying discovery service");
    if (heartbeatExecutor != null) {
      heartbeatExecutor.shutdownNow();
    }
  }

  @Override
  public DiscoveryRegistry getRegistry() {
    return discoveryRegistryProvider.get();
  }

  @Override
  public DiscoveredServices getDiscoveredServices() {
    synchronized (cachedDiscoveredServicesLock) {
      LocalDateTime now = LocalDateTime.now();

      if (cachedDiscoveredServices != null && cachedDiscoveredServicesExpires != null
          && now.isBefore(cachedDiscoveredServicesExpires)) {
        return cachedDiscoveredServices;
      }

      DiscoveryRegistry registry = discoveryRegistryProvider.get();
      if (registry == null) {
        return null;
      }

      return forceLocalCacheUpdate(registry);
    }
  }

  private DiscoveredServices forceLocalCacheUpdate(DiscoveryRegistry registry) {
    var result = discoveryAnalyzer.analyze(registry);
    cachedDiscoveredServices = result;
    cachedDiscoveredServicesExpires = LocalDateTime.now().plusSeconds(LOCAL_REGISTRY_CACHE_TTL);
    return result;
  }

  @Override
  public DiscoveryHeartBeatResponse processHeartBeat(DiscoveryHeartBeatRequest request) {
    String method = "processHeartBeat";
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(method, "adding heartbeat request to queue");
    }

    heartbeatQueue.add(request);

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(method, "there are " + heartbeatQueue.size() + " pending beats in the queue");
    }

    return DiscoveryHeartBeatResponse.builder().withStatus(request.getStatus()).build();
  }

  private void heartbeatDequeueTick() {
    try {
      this.heartbeatDequeue();
    } catch (Throwable t) { // NOSONAR
      LOGGER.error("heartbeatDequeue", "error in heartbeatDequeue task", t);
    }
  }

  private void heartbeatDequeue() {
    String method = "heartbeatDequeueTick";
    try {
      if (heartbeatQueue.isEmpty()) {
        LOGGER.debug(method, "no heartbeat to dequeue");
        return;
      }

      List<DiscoveryHeartBeatRequest> toProcess = new LinkedList<>();
      int counter = 0;
      DiscoveryHeartBeatRequest toDequeue;
      while (((toDequeue = heartbeatQueue.poll()) != null)) {
        toProcess.add(toDequeue);
        if (++counter >= HEARTBEAT_DEQUEUE_MAX_PER_TICK) {
          LOGGER.warn(method, "reached max number of heartbeat processed per tick, limiting to "
              + HEARTBEAT_DEQUEUE_MAX_PER_TICK);
          break;
        }
      }

      if (toProcess.isEmpty()) {
        LOGGER.debug(method, "no heartbeat to actually dequeue");
        return;
      }

      discoveryRegistryProvider.executeWithDoubleLock(() -> {
        LOGGER.debug(method, "processing " + toProcess.size() + " dequeued heartbeats now");

        DiscoveryRegistry registry = discoveryRegistryProvider.get();

        toProcess.forEach(hb -> processQueuedHeartBeat(registry, hb));

        LOGGER.debug(method, "done processing heartbeats, saving registry to cache");
        discoveryRegistryProvider.save();

        LOGGER.debug(method, "saved registry to cache");

        forceLocalCacheUpdate(registry);

        LOGGER.debug(method, "releasing cluster lock");

      }, CLUSTER_LOCK_SHORT_OPERATION, CLUSTER_LOCK_ACQ_TIMEOUT);

      LOGGER.debug(method, "finished dequeueing heartbeats");

    } catch (Exception e) {
      LOGGER.error(method, "error in dequeueing heartbeat requests", e);
    }
  }

  private void processQueuedHeartBeat(DiscoveryRegistry registry,
      DiscoveryHeartBeatRequest request) {
    String method = "processHeartBeat";
    try {
      DiscoveryHeartBeatHelper.validateHeartBeatPayload(request);

      LOGGER.debug(method, "processing heartbeat from " + request.getCombinedId());

      processHeartbeatInLock(registry, request);

      LOGGER.debug(method, "finished processing heartbeat from " + request.getCombinedId());
    } catch (Exception e) {
      LOGGER.error(method, "error processing single heartbeat", e);
    }
  }

  private void processHeartbeatInLock(DiscoveryRegistry registry,
      DiscoveryHeartBeatRequest request) {
    // check if service is already known
    Optional<DiscoveryRegistryServiceEntry> knownService =
        registry.findService(request.getService().getName());

    if (knownService.isEmpty()) {
      // new service, add to registry
      processHeartbeatForNewServiceInLock(registry, request);

    } else {
      // service already known
      processHeartbeatForKnownServiceInLock(registry, knownService.get(), request);
    }
  }

  private DiscoveryRegistryServiceEntry processHeartbeatForNewServiceInLock(
      DiscoveryRegistry registry, DiscoveryHeartBeatRequest request) {
    String method = "processHeartbeatForNewServiceInLock";
    LOGGER.info(method,
        "discovered new service [ {} ] with instance [ {} ] in status [ {} ], registering",
        request.getService().getName(), request.getInstance().getInstanceId(), request.getStatus());

    DiscoveryRegistryServiceEntry service = buildNewService(request);

    registry.getServices().add(service);

    registry.markUpdated();

    LOGGER.info(method,
        "registered new service [ {} ] with declared instance [ {} ] in status [ {} ]",
        request.getService().getName(), request.getInstance().getInstanceId(), request.getStatus());

    return service;
  }

  private void processHeartbeatForKnownServiceInLock(DiscoveryRegistry registry,
      DiscoveryRegistryServiceEntry service, DiscoveryHeartBeatRequest request) {

    service.setLastHeartBeat(request);
    service.setLastHeartBeatTime(ZonedDateTime.now());

    // check if configuration
    checkServiceConfigurationChanged(service, request);

    // check if instance is already known
    Optional<DiscoveryRegistryInstanceEntry> knownInstance =
        service.findInstance(request.getInstance().getInstanceId());

    if (knownInstance.isEmpty()) {
      // since i'm adding an instance, i will remove any shutdown or failed instances to make space
      evictObsoleteInstances(service);

      // new instance, add to registry
      processHeartbeatForNewInstanceInLock(registry, service, request);

    } else {
      // instance already known
      processHeartbeatForKnownInstanceInLock(registry, knownInstance.get(), request);
    }
  }

  private void evictObsoleteInstances(DiscoveryRegistryServiceEntry service) {

    String method = "evictObsoleteInstances";

    // evict conditions (ORed):
    // - instance is SHUTDOWN and time passed >= <?> * heartbeat interval
    // - instance is in FAILURE and time passed >= <?> * heartbeat interval
    // - instance is in SHUTDOWN or FAILURE and service has more than <?> instances

    List<DiscoveryRegistryInstanceEntry> toRemove =
        service.getInstances().stream().filter(instance -> {
          DiscoveredInstanceStatus status =
              discoveryAnalyzer.computeStatusForDiscoveredInstance(service, instance);
          if (status != DiscoveredInstanceStatus.SHUTDOWN
              && status != DiscoveredInstanceStatus.FAILURE) {
            return false;
          }

          long timeSinceLastHB =
              discoveryAnalyzer.getMillisecondsSinceLastHeartbeat(service, instance);

          String instanceFullId = service.getConfiguration().getName() + ":"
              + instance.getConfiguration().getInstanceId();

          if (status == DiscoveredInstanceStatus.SHUTDOWN
              && timeSinceLastHB >= instance.getConfiguration().getHeartBeatInterval()
              * INTERVAL_EVICTION_FACTOR_FOR_SHUTDOWN) {

            LOGGER.warn(method, "EVICTED instance {} because of declared {} state", instanceFullId,
                status.name());
            return true;

          } else if (status == DiscoveredInstanceStatus.FAILURE
              && timeSinceLastHB >= instance.getConfiguration().getHeartBeatInterval()
              * INTERVAL_EVICTION_FACTOR_FOR_FAILURE) {

            LOGGER.warn(method, "EVICTED instance {} because of persistence in {} state",
                instanceFullId, status.name());
            return true;

          } else if (service.getInstances().size() >= EVICTION_TRESHOLD_FOR_INSTANCES_NUMBER) {

            LOGGER.warn(method,
                "EVICTED instance {} because it is in status {} and there are too many instances knwown in service",
                instanceFullId, status.name());
            return true;
          }

          return false;

        }).collect(Collectors.toList());

    service.getInstances().removeAll(toRemove);
  }

  private void checkServiceConfigurationChanged(DiscoveryRegistryServiceEntry service,
      DiscoveryHeartBeatRequest request) {

    // check if location changed
    if (request.getService().getRoute() != null
        && !request.getService().getRoute().equals(service.getConfiguration().getRoute())) {
      logChanged(request.getCombinedId(), "route", service.getConfiguration().getRoute(),
          request.getService().getRoute() + " ]");

      throw new DiscoveryConflictException(
          "Conflict with route reported from previous service discovery");
    }
  }

  private void processHeartbeatForNewInstanceInLock(DiscoveryRegistry registry,
      DiscoveryRegistryServiceEntry service, DiscoveryHeartBeatRequest request) {
    String method = "processHeartbeatForNewInstanceInLock";

    LOGGER.info(method,
        "discovered new instance [ {} ] in status [ {} ] for service [ {} ], registering",
        request.getInstance().getInstanceId(), request.getStatus().name(),
        service.getConfiguration().getName());

    DiscoveryRegistryInstanceEntry instance = buildNewInstance(request);

    service.getInstances().add(instance);

    registry.markUpdated();

    LOGGER.info(method, "registered new instance [ {} ] in status [ {} ] for service [ {} ]",
        request.getInstance().getInstanceId(), request.getStatus().name(),
        service.getConfiguration().getName());
  }

  private void processHeartbeatForKnownInstanceInLock(DiscoveryRegistry registry,
      DiscoveryRegistryInstanceEntry instance, DiscoveryHeartBeatRequest request) {

    String method = "processHeartbeatForKnownInstanceInLock";

    instance.setLastHeartBeat(request);
    instance.setLastHeartBeatTime(ZonedDateTime.now());

    DiscoveryInstanceReportedStatus newStatus = request.getStatus();

    // check if status changed
    if (newStatus != instance.getStatus()) {
      LOGGER.info(method,
          "instance [ {} ] reported status change from [ {} ] to [ {} ]", request.getCombinedId(),
          instance.getStatus().name(), newStatus.name());

      instance.setStatus(newStatus);
      registry.markUpdated();
    } else {
      LOGGER.debug(method, "instance [ {} ] is still in status [ {} ]", request.getCombinedId(),
          newStatus.name());
    }

    // check if configuration changed
    checkInstanceConfigurationChanged(registry, instance, request);

    registry.markUpdated();
  }

  private void checkInstanceConfigurationChanged(DiscoveryRegistry registry,
      DiscoveryRegistryInstanceEntry instance, DiscoveryHeartBeatRequest request) {

    // check if heartbeat interval changed
    if (request.getInstance().getHeartBeatInterval() != null && !request.getInstance()
        .getHeartBeatInterval().equals(instance.getConfiguration().getHeartBeatInterval())) {
      logChanged(request.getCombinedId(), "heatbeat interval",
          instance.getConfiguration().getHeartBeatInterval(),
          request.getInstance().getHeartBeatInterval());

      instance.getConfiguration()
      .setHeartBeatInterval(request.getInstance().getHeartBeatInterval());
      registry.markUpdated();
    }

    // check if location changed
    if (request.getInstance().getLocation() != null
        && !request.getInstance().getLocation().equals(instance.getConfiguration().getLocation())) {

      logChanged(request.getCombinedId(), "location", instance.getConfiguration().getLocation(),
          request.getInstance().getLocation());

      instance.getConfiguration().setLocation(request.getInstance().getLocation());
      registry.markUpdated();
    }

  }

  private DiscoveryRegistryServiceEntry buildNewService(DiscoveryHeartBeatRequest request) {
    ZonedDateTime now = ZonedDateTime.now();

    DiscoveryRegistryServiceEntry service = new DiscoveryRegistryServiceEntry();
    service.setLastHeartBeat(request);
    service.setLastHeartBeatTime(now);
    service.setFirstDiscoveryTime(now);

    DiscoveryServiceConfiguration configuration = new DiscoveryServiceConfiguration();
    configuration.setName(request.getService().getName());
    configuration.setDescription(request.getService().getDescription());
    configuration.setRoute(request.getService().getRoute());
    service.setConfiguration(configuration);

    DiscoveryRegistryInstanceEntry instance = buildNewInstance(request);
    service.getInstances().add(instance);

    return service;
  }

  private DiscoveryRegistryInstanceEntry buildNewInstance(DiscoveryHeartBeatRequest request) {

    ZonedDateTime now = ZonedDateTime.now();

    DiscoveryRegistryInstanceEntry instance = new DiscoveryRegistryInstanceEntry();
    instance.setLastHeartBeat(request);
    instance.setLastHeartBeatTime(now);
    instance.setFirstDiscoveryTime(now);
    instance.setStatus(request.getStatus());

    DiscoveryInstanceConfiguration instanceConfiguration = new DiscoveryInstanceConfiguration();
    instanceConfiguration.setHeartBeatInterval(request.getInstance().getHeartBeatInterval());
    instanceConfiguration.setInstanceId(request.getInstance().getInstanceId());
    instanceConfiguration.setLocation(request.getInstance().getLocation());
    instance.setConfiguration(instanceConfiguration);

    return instance;
  }

  private void logChanged(String id, String name, Object previousValue, Object newValue) {
    String method = "logChanged";

    LOGGER.info(method, "instance [ {} ] reported {} change from [ {} ] to [ {} ]", id, name,
        previousValue, newValue);
  }

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.discovery.fetch;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.discovery.model.DiscoveredInstance;
import it.csi.cosmo.common.discovery.model.DiscoveredInstanceStatus;
import it.csi.cosmo.common.discovery.model.DiscoveredService;
import it.csi.cosmo.common.discovery.model.DiscoveredServiceStatus;
import it.csi.cosmo.common.discovery.model.DiscoveredServices;
import it.csi.cosmo.common.discovery.model.DiscoveryInstanceReportedStatus;
import it.csi.cosmo.common.discovery.model.DiscoveryRegistry;
import it.csi.cosmo.common.discovery.model.DiscoveryRegistryInstanceEntry;
import it.csi.cosmo.common.discovery.model.DiscoveryRegistryServiceEntry;

/**
 *
 */

public class DiscoveryAnalyzer {

  private Logger logger;

  private static final double HEARTBEAT_INTERVAL_LATE_FACTOR = 2.0;

  private static final double HEARTBEAT_INTERVAL_FAIL_FACTOR = 4.0;

  public DiscoveryAnalyzer(String loggingPrefix) {
    // nop

    logger = Logger.getLogger((loggingPrefix != null ? loggingPrefix : Constants.PRODUCT)
        + ".discovery.DiscoveryAnalyzer");
  }

  public DiscoveredServices analyze(DiscoveryRegistry registry) {

    return computeDiscoveredServicesFromRegistry(registry);
  }

  private DiscoveredServices computeDiscoveredServicesFromRegistry(DiscoveryRegistry registry) {
    logger.debug("re-computing discovered status from registry " + registry.getFullId());

    DiscoveredServices output = new DiscoveredServices();

    registry.getServices()
    .forEach(service -> output.getServices().add(computeDiscoveredService(service)));

    return output;
  }

  private DiscoveredService computeDiscoveredService(DiscoveryRegistryServiceEntry service) {
    DiscoveredService discoveredService = new DiscoveredService();
    discoveredService.setRegistryEntry(service);

    service.getInstances().forEach(instance -> discoveredService.getInstances()
        .add(computeDiscoveredInstance(service, instance)));

    discoveredService
    .setStatus(computeStatusForDiscoveredService(service, discoveredService.getInstances()));

    logger.debug("compute aggregated status [ " + discoveredService.getStatus().name()
        + " ] for service " + service.getConfiguration().getName());

    return discoveredService;
  }

  private DiscoveredInstance computeDiscoveredInstance(DiscoveryRegistryServiceEntry service,
      DiscoveryRegistryInstanceEntry instance) {

    DiscoveredInstance discoveredInstance = new DiscoveredInstance();
    discoveredInstance.setRegistryEntry(instance);
    discoveredInstance.setStatus(computeStatusForDiscoveredInstance(service, instance));

    logger.debug("compute status [ " + discoveredInstance.getStatus().name() + " ] for instance "
        + service.getConfiguration().getName() + ":" + instance.getConfiguration().getInstanceId());

    return discoveredInstance;
  }

  public long getMillisecondsSinceLastHeartbeat(DiscoveryRegistryServiceEntry service,
      DiscoveryRegistryInstanceEntry instance) {

    long msSinceLastHeartbeat =
        instance.getLastHeartBeatTime().until(ZonedDateTime.now(), ChronoUnit.MILLIS);

    if (msSinceLastHeartbeat < 0) {
      logger.warn("instance " + service.getConfiguration().getName() + ":"
          + instance.getConfiguration().getInstanceId()
          + " last heartbeat time is future: are system clocks desynchronized between cluster instances?");
      return 0;
    } else {
      return msSinceLastHeartbeat;
    }
  }

  public DiscoveredInstanceStatus computeStatusForDiscoveredInstance(
      DiscoveryRegistryServiceEntry service, DiscoveryRegistryInstanceEntry instance) {
    if (instance == null) {
      return null;
    }

    String instanceFullId =
        service.getConfiguration().getName() + ":" + instance.getConfiguration().getInstanceId();

    if (instance.getStatus() == DiscoveryInstanceReportedStatus.SHUTDOWN) {
      return DiscoveredInstanceStatus.SHUTDOWN;
    }

    long msSinceLastHeartbeat = getMillisecondsSinceLastHeartbeat(service, instance);

    if (msSinceLastHeartbeat < instance.getConfiguration().getHeartBeatInterval()
        * HEARTBEAT_INTERVAL_LATE_FACTOR) {
      // heartbeat recente, ritorno stato dichiarato
      logger.debug(String.format("instance %s last heartbeat time is %s ms ago, valid",
          instanceFullId, msSinceLastHeartbeat));
      return DiscoveredInstanceStatus.fromReported(instance.getStatus());

    } else if (msSinceLastHeartbeat < (instance.getConfiguration().getHeartBeatInterval()
        * HEARTBEAT_INTERVAL_FAIL_FACTOR)) {
      // heartbeat in ritardo ma nel grace period
      logger.warn(
          String.format("instance %s last heartbeat time is %s ms ago, late but in grace period.",
              instanceFullId, msSinceLastHeartbeat));
      if (instance.getStatus() == DiscoveryInstanceReportedStatus.UP) {
        return DiscoveredInstanceStatus.NOT_REPORTING;
      } else {
        return DiscoveredInstanceStatus.fromReported(instance.getStatus());
      }

    } else {
      // heartbeat oltre grace period
      logger.warn(String.format(
          "instance %s last heartbeat time is %s ms ago, late over grace period. reporting FAILURE state",
          instanceFullId, msSinceLastHeartbeat));
      return DiscoveredInstanceStatus.FAILURE;
    }
  }

  private DiscoveredServiceStatus computeStatusForDiscoveredService(
      DiscoveryRegistryServiceEntry service, Collection<DiscoveredInstance> discoveredInstances) {
    if (service == null) {
      return null;
    }

    logger.debug(String.format("aggregating instances status to service %s status from %s",
        service.getConfiguration().getName(), discoveredInstances.stream()
        .map(o -> o.getStatus().name()).collect(Collectors.joining(", "))));

    if (discoveredInstances.isEmpty()) {
      // no instances available
      return DiscoveredServiceStatus.UNAVAILABLE;

    } else if (discoveredInstances.size() == 1) {
      // single instance available
      return DiscoveredServiceStatus
          .fromInstanceStatus(discoveredInstances.stream().findAny().orElseThrow().getStatus());

    } else if (discoveredInstances.stream()
        .anyMatch(i -> i.getStatus() == DiscoveredInstanceStatus.UP)) {
      // at least one instance UP
      return DiscoveredServiceStatus.UP;

    } else if (discoveredInstances.stream()
        .anyMatch(i -> i.getStatus() == DiscoveredInstanceStatus.TROUBLE)) {
      // at least one instance TROUBLE
      return DiscoveredServiceStatus.TROUBLE;

    } else if (discoveredInstances.stream()
        .anyMatch(i -> i.getStatus() == DiscoveredInstanceStatus.NOT_REPORTING
        && i.getRegistryEntry().getStatus() == DiscoveryInstanceReportedStatus.UP)) {
      // at least one instance late with reporting but latest reported was UP
      return DiscoveredServiceStatus.NOT_REPORTING;

    } else if (discoveredInstances.stream().map(DiscoveredInstance::getStatus).distinct()
        .count() <= 1) {
      // multiple instances with the same status, return any
      return DiscoveredServiceStatus
          .fromInstanceStatus(discoveredInstances.stream().findAny().orElseThrow().getStatus());

    } else {
      // at least 2 instances, of different status, none is UP or TROUBLE
      if (discoveredInstances.stream()
          .anyMatch(i -> i.getStatus() == DiscoveredInstanceStatus.STARTING)) {
        // at least one instance STARTING
        return DiscoveredServiceStatus.STARTING;
      } else if (discoveredInstances.stream()
          .anyMatch(i -> i.getStatus() == DiscoveredInstanceStatus.DOWN)) {
        // at least one instance DOWN
        return DiscoveredServiceStatus.DOWN;
      } else if (discoveredInstances.stream()
          .anyMatch(i -> i.getStatus() == DiscoveredInstanceStatus.FAILURE)) {
        // at least one instance FAILURE
        return DiscoveredServiceStatus.FAILURE;
      } else if (discoveredInstances.stream()
          .anyMatch(i -> i.getStatus() == DiscoveredInstanceStatus.SHUTDOWN)) {
        // at least one instance SHUTDOWN
        return DiscoveredServiceStatus.SHUTDOWN;
      } else if (discoveredInstances.stream()
          .anyMatch(i -> i.getStatus() == DiscoveredInstanceStatus.NOT_REPORTING)) {
        // at least one instance NOT_REPORTING but was not UP
        return DiscoveredServiceStatus.FAILURE;
      }
    }

    return DiscoveredServiceStatus.FAILURE;
  }

}

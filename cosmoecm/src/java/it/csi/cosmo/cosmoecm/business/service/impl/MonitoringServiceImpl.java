/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.dto.common.ServiceStatusDTO;
import it.csi.cosmo.common.dto.common.ServiceStatusEnum;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.monitoring.Monitorable;
import it.csi.cosmo.cosmoecm.business.service.MonitoringService;
import it.csi.cosmo.cosmoecm.config.Constants;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;


@Service
public class MonitoringServiceImpl implements MonitoringService, ApplicationContextAware {

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "MonitoringServiceImpl");

  private static final long REFRESH_INTERVAL_MS = 60000;
  private static final long MAX_EXECUTION_TIME_MS = 15000;
  private static final long MAX_EXECUTION_TIME_SINGLE_CHECK_MS = 5000;

  private ApplicationContext applicationContext;

  private ServiceStatusDTO latestStatus;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Scheduled(fixedDelay = REFRESH_INTERVAL_MS)
  public void refreshServiceStatus() {
    final var method = "refreshServiceStatus";

    logger.debug(method, "re-computing service status");
    latestStatus = computeServiceStatus();
  }

  @Override
  public ServiceStatusDTO getServiceStatus() {
    String method = "getServiceStatus";
    if (latestStatus != null) {
      logger.debug(method, "cache hit for service status");
      return latestStatus;
    }
    return computeServiceStatus();
  }

  private synchronized ServiceStatusDTO computeServiceStatus() {
    String method = "computeServiceStatus";
    logger.debug(method, "checking service status");

    Map<String, Monitorable> beans = applicationContext.getBeansOfType(Monitorable.class);
    final Map<Monitorable, ServiceStatusDTO> beansStatuses = new ConcurrentHashMap<>();

    Instant before = Instant.now();

    if (!beans.isEmpty()) {
      logger.debug(method, "checking service status for " + beans.size() + " services");

      ExecutorService executor = Executors.newFixedThreadPool(beans.size());
      ExecutorService taskExecutor = Executors.newFixedThreadPool(beans.size());

      for (Monitorable bean : beans.values()) {
        executor.submit(() -> getServiceStatusForBean(bean, taskExecutor, beansStatuses));
      }

      executor.shutdown();
      try {
        if (!executor.awaitTermination(MAX_EXECUTION_TIME_MS, TimeUnit.MILLISECONDS)) {
          executor.shutdownNow();
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        logger.warn(method,
            "executor did not complete in MAX_EXECUTION_TIME_MS, following executions might be delayed");
        return ServiceStatusDTO.down().withMessage("Status check timed out").build();
      }
      taskExecutor.shutdown();
    }

    ServiceStatusDTO result =
        aggregate(beansStatuses, ChronoUnit.MILLIS.between(before, Instant.now()));

    if (logger.isDebugEnabled()) {
      logger.debug(method, "computed overall service status is: " + result.getStatus());
    }

    return result;
  }

  private ServiceStatusDTO getServiceStatusForBean(Monitorable bean, ExecutorService taskExecutor,
      Map<Monitorable, ServiceStatusDTO> beansStatuses) {
    String method = "getServiceStatusForBean";
    ServiceStatusDTO result;
    Instant before = Instant.now();
    String desc = extractVisualDescription(bean, null);

    if (logger.isDebugEnabled()) {
      logger.debug(method, "checking status for service " + desc);
    }

    Future<ServiceStatusDTO> submitted = taskExecutor.submit(bean::checkStatus);

    try {
      result = submitted.get(MAX_EXECUTION_TIME_SINGLE_CHECK_MS, TimeUnit.MILLISECONDS);

      if (logger.isDebugEnabled()) {
        logger.error(method,
            "status for service " + desc + " is " + (result != null ? result.getStatus() : null));
      }
    } catch (CancellationException e) {
      // if the computation was cancelled
      result = computationFailed(desc, "service check was cancelled", e, ServiceStatusEnum.UNKNOWN);
    } catch (ExecutionException e) {
      // if the computation threw an exception
      result = computationFailed(desc, "service check failed", e, ServiceStatusEnum.DOWN);
    } catch (InterruptedException e) {
      // if the current thread was interrupted while waiting
      Thread.currentThread().interrupt();
      result =
          computationFailed(desc, "service check was interrupted", e, ServiceStatusEnum.UNKNOWN);
    } catch (TimeoutException e) {
      // if the computation timed out
      result = computationFailed(desc, "service check timed out", e, ServiceStatusEnum.WARNING);
    } catch (Exception e) {
      result = computationFailed(desc, "service check failed unexpectedly", e,
          ServiceStatusEnum.UNKNOWN);
    }

    Instant after = Instant.now();

    desc = extractVisualDescription(bean, result);
    ServiceStatusDTO beanResult;

    if (result == null) {
      //@formatter:off
      beanResult = ServiceStatusDTO.builder()
          .withMessage(desc + " service check is unresponsive")
          .withName(desc)
          .withStatus(ServiceStatusEnum.UNKNOWN)
          .withResponseTime(ChronoUnit.MILLIS.between(before, after))
          .build();
      //@formatter:on
    } else {
      //@formatter:off
      beanResult = ServiceStatusDTO.builder()
          .withDetails(result.getDetails())
          .withMessage(result.getMessage())
          .withName(desc)
          .withStatus(result.getStatus())
          .withResponseTime(ChronoUnit.MILLIS.between(before, after))
          .build();
      //@formatter:on
    }

    beansStatuses.put(bean, beanResult);
    return beanResult;
  }

  private ServiceStatusDTO computationFailed(String beanName, String message, Exception e,
      ServiceStatusEnum status) {
    if (logger.isDebugEnabled()) {
      logger.error("computationFailed",
          "error checking status for service " + beanName + ": " + message, e);
    }
    return ServiceStatusDTO.builder().withStatus(status).withMessage(beanName + message)
        .withDetail("error", e.getClass() + ": " + e.getMessage()
            + (e.getCause() != null ? "; " + e.getCause().getMessage() : ""))
        .build();
  }

  private String extractVisualDescription(Monitorable bean, ServiceStatusDTO result) {
    String desc = result != null ? result.getName() : null;
    if (desc == null) {
      desc = extractKey(bean);
    }
    return desc;
  }

  private String extractKey(Monitorable bean) {
    String desc = bean.getClass().getSimpleName();

    if (desc.contains("$$Enhancer")) {
      desc = desc.split("\\$\\$Enhancer")[0];
    }

    if (desc.endsWith("ServiceImpl")) {
      desc = desc.substring(0, desc.length() - 11);
    }

    return desc;
  }

  private ServiceStatusDTO aggregate(Map<Monitorable, ServiceStatusDTO> statuses, long time) {
    ServiceStatusEnum resultStatus = ServiceStatusEnum.UP;
    int numUp = 0;
    int numUnknown = 0;
    int numDown = 0;
    int numWarn = 0;

    Map<String, Object> details = new HashMap<>();

    for (Entry<Monitorable, ServiceStatusDTO> statusEntry : statuses.entrySet()) {

      details.put(extractKey(statusEntry.getKey()), statusEntry.getValue());

      if (statusEntry.getValue().getStatus() == ServiceStatusEnum.UP) {
        numUp++;
      } else if (statusEntry.getValue().getStatus() == ServiceStatusEnum.WARNING) {
        numWarn++;
        resultStatus = ServiceStatusEnum.WARNING;
      } else if (statusEntry.getValue().getStatus() == ServiceStatusEnum.DOWN) {
        numDown++;
        resultStatus = ServiceStatusEnum.DOWN;
      } else {
        numUnknown++;
        resultStatus = ServiceStatusEnum.UNKNOWN;
      }
    }

    String message = "Service is " + resultStatus.name() + ": ";
    if (numUp > 0) {
      message += numUp + " services UP, ";
    }
    if (numUnknown > 0) {
      message += numUnknown + " services UNKNOWN, ";
    }
    if (numWarn > 0) {
      message += numWarn + " services in WARNING, ";
    }
    if (numDown > 0) {
      message += numDown + " services DOWN, ";
    }

    if (message.endsWith(", ")) {
      message = message.substring(0, message.length() - 2);
    }

    //@formatter:off
    return ServiceStatusDTO.builder()
        .withName(Constants.COMPONENT_DESCRIPTION + " Service Status")
        .withStatus(resultStatus)
        .withMessage(message)
        .withDetails(details)
        .withResponseTime(time)
        .build();
    //@formatter:on
  }
}

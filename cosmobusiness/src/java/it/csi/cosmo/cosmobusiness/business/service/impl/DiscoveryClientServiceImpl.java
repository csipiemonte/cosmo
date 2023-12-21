/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service.impl;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.discovery.client.DiscoveryClient;
import it.csi.cosmo.common.discovery.model.DiscoveryInstanceConfiguration;
import it.csi.cosmo.common.discovery.model.DiscoveryInstanceReportedStatus;
import it.csi.cosmo.common.discovery.model.DiscoveryServiceConfiguration;
import it.csi.cosmo.common.dto.common.ServiceStatusDTO;
import it.csi.cosmo.common.util.AsyncUtils;
import it.csi.cosmo.cosmobusiness.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmobusiness.business.service.DiscoveryClientService;
import it.csi.cosmo.cosmobusiness.business.service.MonitoringService;
import it.csi.cosmo.cosmobusiness.config.Constants;
import it.csi.cosmo.cosmobusiness.config.ParametriApplicativo;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerConstants;


@Service
public class DiscoveryClientServiceImpl
implements DiscoveryClientService, InitializingBean, DisposableBean {

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private MonitoringService monitoringService;

  private DiscoveryClient client = null;

  private boolean isEnabled() {
    return configurazioneService.requireConfig(ParametriApplicativo.DISCOVERY_CLIENT_ENABLE)
        .asBool();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    AsyncUtils.when("INIT_DISCOVERY_SERVICE", LoggerConstants.ROOT_LOG_CATEGORY,
        configurazioneService::isReady, () -> {
          if (isEnabled()) {
            getClient().enableHeartbeat(this::getCurrentStatusToReport);
          }
        }, 5000l, null);
  }

  @Override
  public void destroy() throws Exception {
    if (this.client != null) {
      this.client.close();
    }
  }

  private DiscoveryInstanceReportedStatus getCurrentStatusToReport() {
    if (monitoringService == null) {
      return DiscoveryInstanceReportedStatus.STARTING;
    } else {

      ServiceStatusDTO status = monitoringService.getServiceStatus();
      switch (status.getStatus()) {
        case WARNING:
        case UNKNOWN:
          return DiscoveryInstanceReportedStatus.TROUBLE;
        case DOWN:
          return DiscoveryInstanceReportedStatus.DOWN;
        default:
          return DiscoveryInstanceReportedStatus.UP;
      }
    }
  }

  private synchronized DiscoveryClient getClient() {
    // Double-checked locking should not be used (java:S2168)
    if (client == null) {
      //@formatter:off
      client = DiscoveryClient.builder()
          .withLoggingPrefix(LoggerConstants.ROOT_LOG_CATEGORY)
          .withDiscoveryServerEndpoint(configurazioneService.requireConfig(ParametriApplicativo.DISCOVERY_SERVER_LOCATION).asURI())
          .withServiceConfiguration(DiscoveryServiceConfiguration.builder()
              .withName(Constants.DISCOVERY.SERVICE_NAME)
              .withDescription(Constants.COMPONENT_DESCRIPTION)
              .withRoute(Constants.DISCOVERY.SERVICE_ROUTE)
              .build())
          .withInstanceConfiguration(DiscoveryInstanceConfiguration.builder()
              .withHeartBeatInterval(configurazioneService.requireConfig(ParametriApplicativo.DISCOVERY_CLIENT_HEARTBEAT_INTERVAL).asInteger())
              .withLocation(configurazioneService.requireConfig(ParametriApplicativo.DISCOVERY_CLIENT_LOCATION).asURI())
              .build())
          .withUsername(configurazioneService.requireConfig(ParametriApplicativo.DISCOVERY_CLIENT_USERNAME).asString())
          .withPassword(configurazioneService.requireConfig(ParametriApplicativo.DISCOVERY_CLIENT_PASSWORD).asString())
          .build();
      //@formatter:on
    }
    return client;
  }

}

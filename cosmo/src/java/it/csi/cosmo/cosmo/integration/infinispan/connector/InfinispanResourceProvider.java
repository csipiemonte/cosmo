/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.integration.infinispan.connector;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import it.csi.cosmo.common.exception.BadConfigurationException;
import it.csi.cosmo.common.infinispan.connector.InfinispanResourceConnector;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmo.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmo.config.ParametriApplicativo;
import it.csi.cosmo.cosmo.util.logger.LogCategory;
import it.csi.cosmo.cosmo.util.logger.LoggerFactory;


@Component
public class InfinispanResourceProvider implements InfinispanResourceConnector {

  private CosmoLogger logger = LoggerFactory.getLogger(LogCategory.INFINISPAN_LOG_CATEGORY,
      "InfinispanResourceConnectorImpl");

  private Map<String, Object> fallbackLocalCache = new LinkedHashMap<>();

  @Resource(lookup = "@@@discovery.store.infinispan.jndi.name@@@")
  private Map<String, Object> cache;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Override
  public Map<String, Object> getCache() {
    if (configurazioneService.requireConfig(ParametriApplicativo.DISCOVERY_STORE_JNDI_ENABLE)
        .asBool()) {

      if (cache != null) {
        return cache;
      }

      logger.error("getCache",
          "REQUIRED SHARED CACHE IS NOT AVAILABLE. ARE YOU RUNNING NOT IN A CLUSTER? IS THE CACHE CONFIGURED?");
      throw new BadConfigurationException("Required shared cache is not available.");
    }

    return fallbackLocalCache;
  }

  @Override
  public void close() throws Exception {
    // NOP
  }

}

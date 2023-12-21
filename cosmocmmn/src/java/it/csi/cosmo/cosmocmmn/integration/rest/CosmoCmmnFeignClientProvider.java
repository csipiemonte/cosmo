/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.integration.rest;

import org.springframework.stereotype.Component;
import it.csi.cosmo.common.feignclient.FeignClientProvider;
import it.csi.cosmo.cosmocmmn.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmocmmn.config.Constants;
import it.csi.cosmo.cosmocmmn.util.listener.SpringApplicationContextHelper;
import it.csi.cosmo.cosmocmmn.util.logger.LoggerConstants;

/**
 *
 */

@Component
public class CosmoCmmnFeignClientProvider extends FeignClientProvider {

  public CosmoCmmnFeignClientProvider() {
    super(LoggerConstants.ROOT_LOG_CATEGORY);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getBean(Class<T> requiredBean) {
    return (T) SpringApplicationContextHelper.getBean(requiredBean);
  }

  @Override
  public String resolveConfiguration(String key) {
    return ConfigurazioneService.getInstance().getConfig(key).asString();
  }

  @Override
  public String getNamespaceToScan() {
    return "it.csi." + Constants.PRODUCT + "." + Constants.COMPONENT_NAME;
  }

}

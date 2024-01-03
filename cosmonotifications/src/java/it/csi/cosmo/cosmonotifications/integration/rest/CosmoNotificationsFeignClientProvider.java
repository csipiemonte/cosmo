/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.integration.rest;

import org.springframework.stereotype.Component;
import it.csi.cosmo.common.feignclient.FeignClientProvider;
import it.csi.cosmo.cosmonotifications.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmonotifications.config.Constants;
import it.csi.cosmo.cosmonotifications.util.listener.SpringApplicationContextHelper;
import it.csi.cosmo.cosmonotifications.util.logger.LoggerConstants;

/**
 *
 */

@Component
public class CosmoNotificationsFeignClientProvider extends FeignClientProvider {

  public CosmoNotificationsFeignClientProvider() {
    super(LoggerConstants.COSMONOTIFICATIONS_ROOT_LOG_CATEGORY);
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

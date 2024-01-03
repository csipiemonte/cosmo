/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmopratiche.testbed.providers;

import org.springframework.stereotype.Component;
import it.csi.cosmo.common.feignclient.FeignClientProvider;
import it.csi.cosmo.cosmopratiche.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmopratiche.config.Constants;
import it.csi.cosmo.cosmopratiche.util.listener.SpringApplicationContextHelper;
import it.csi.cosmo.cosmopratiche.util.logger.LoggerConstants;

/**
 *
 */

@Component
public class CosmoPraticheTestFeignClientProvider extends FeignClientProvider {

  public CosmoPraticheTestFeignClientProvider() {
    super(LoggerConstants.COSMOPRATICHE_ROOT_LOG_CATEGORY);
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
    return "it.csi.test." + Constants.PRODUCT + "." + Constants.COMPONENT_NAME;
  }

}

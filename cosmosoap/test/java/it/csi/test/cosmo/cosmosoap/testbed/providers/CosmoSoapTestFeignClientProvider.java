/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmosoap.testbed.providers;

import org.springframework.stereotype.Component;
import it.csi.cosmo.common.feignclient.FeignClientProvider;
import it.csi.cosmo.cosmosoap.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmosoap.util.listener.SpringApplicationContextHelper;
import it.csi.cosmo.cosmosoap.util.logger.LoggerConstants;

/**
 *
 */

@Component
public class CosmoSoapTestFeignClientProvider extends FeignClientProvider {

  public CosmoSoapTestFeignClientProvider() {
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
    return "it.csi.cosmo.cosmosoap";
  }

}

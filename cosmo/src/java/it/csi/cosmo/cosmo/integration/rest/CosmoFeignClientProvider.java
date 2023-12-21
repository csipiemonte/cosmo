/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.integration.rest;

import java.util.Optional;
import org.springframework.stereotype.Component;
import it.csi.cosmo.common.discovery.model.DiscoveredInstance;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.feignclient.FeignClientProvider;
import it.csi.cosmo.cosmo.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmo.business.service.ProxyService;
import it.csi.cosmo.cosmo.util.listener.SpringApplicationContextHelper;
import it.csi.cosmo.cosmo.util.logger.LoggerConstants;

/**
 *
 */

@Component
public class CosmoFeignClientProvider extends FeignClientProvider {

  private static final String SERVICE_PREFIX = "service:";

  public CosmoFeignClientProvider() {
    super(LoggerConstants.ROOT_LOG_CATEGORY);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getBean(Class<T> requiredBean) {
    return (T) SpringApplicationContextHelper.getBean(requiredBean);
  }

  @Override
  public String resolveConfiguration(String key) {
    if (key.startsWith(SERVICE_PREFIX)) {
      return resolveInstanceLocation(key.substring(SERVICE_PREFIX.length()));
    }
    return ConfigurazioneService.getInstance().getConfig(key).asString();
  }

  private String resolveInstanceLocation(String serviceName) {
    Optional<DiscoveredInstance> instance =
        ProxyService.getInstance().pickInstanceForCall(serviceName);
    if (instance.isEmpty()) {
      throw new NotFoundException(
          "no available instances for service " + serviceName);
    } else {
      return instance.get().getRegistryEntry().getConfiguration().getLocation().toString();
    }
  }

  @Override
  public String getNamespaceToScan() {
    return "it.csi.cosmo.cosmo";
  }

}

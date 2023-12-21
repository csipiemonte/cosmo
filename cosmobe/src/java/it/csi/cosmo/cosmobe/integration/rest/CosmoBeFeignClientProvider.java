/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.integration.rest;

import java.net.URI;
import java.util.Optional;
import org.springframework.stereotype.Component;
import it.csi.cosmo.common.discovery.model.DiscoveredInstance;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.feignclient.FeignClientProvider;
import it.csi.cosmo.cosmobe.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmobe.business.service.ProxyService;
import it.csi.cosmo.cosmobe.business.service.impl.DiscoveryDeclarativeFallbackServiceImpl;
import it.csi.cosmo.cosmobe.config.Constants;
import it.csi.cosmo.cosmobe.util.listener.SpringApplicationContextHelper;
import it.csi.cosmo.cosmobe.util.logger.LoggerConstants;

/**
 *
 */

@Component
public class CosmoBeFeignClientProvider extends FeignClientProvider {

  private static final String SERVICE_PREFIX = "service:";

  public CosmoBeFeignClientProvider() {
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

      var discoveryFallbackService = getBean(DiscoveryDeclarativeFallbackServiceImpl.class);
      if (!discoveryFallbackService.isEnabled()) {
        throw new NotFoundException("no available instances for service " + serviceName);
      }

      URI discovered = discoveryFallbackService.getLocationWithFallbackDiscovery(serviceName);
      if (discovered == null) {
        throw new NotFoundException(
            "no available instances for service " + serviceName + " AND fallback discovery failed");
      } else {
        return discovered.toString();
      }

    } else {
      return instance.get().getRegistryEntry().getConfiguration().getLocation().toString();
    }
  }

  @Override
  public String getNamespaceToScan() {
    return "it.csi." + Constants.PRODUCT + "." + Constants.COMPONENT_NAME;
  }

}

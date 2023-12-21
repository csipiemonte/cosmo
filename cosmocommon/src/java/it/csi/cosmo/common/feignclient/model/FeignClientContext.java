/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.feignclient.model;

import org.springframework.web.client.RestTemplate;
import it.csi.cosmo.common.feignclient.FeignClientProvider;


/**
 *
 */

public class FeignClientContext {

  private FeignClientProvider provider;

  private RestTemplate restTemplate;

  private boolean configured = false;

  public boolean isConfigured() {
    return configured;
  }

  public void setConfigured(boolean configured) {
    this.configured = configured;
  }

  public FeignClientProvider getProvider() {
    return provider;
  }

  public void setProvider(FeignClientProvider provider) {
    this.provider = provider;
  }

  public RestTemplate getRestTemplate() {
    return restTemplate;
  }

  public void setRestTemplate(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

}

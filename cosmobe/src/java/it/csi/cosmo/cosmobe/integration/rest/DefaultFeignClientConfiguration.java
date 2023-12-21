/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.integration.rest;

import org.springframework.stereotype.Component;
import it.csi.cosmo.common.feignclient.model.FeignClientContext;
import it.csi.cosmo.common.feignclient.model.FeignClientContextConfigurator;
import it.csi.cosmo.common.util.RestTemplateUtils;

/**
 *
 */

@Component
public class DefaultFeignClientConfiguration implements FeignClientContextConfigurator {

  @Override
  public void configure(FeignClientContext context) {

    context.setRestTemplate(RestTemplateUtils.builder()
        .withAllowConnectionReuse(false)
        .withConnectionRequestTimeout(10000)
        .withConnectTimeout(10000)
        .withReadTimeout(30000)
        .build());
  }
}

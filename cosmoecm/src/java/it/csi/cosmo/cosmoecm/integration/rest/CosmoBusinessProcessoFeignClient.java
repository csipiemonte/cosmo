/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmoecm.integration.rest;

import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmobusiness.client.ProcessoApi;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo;

/**
 *
 */

@FeignClient(
    value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY
        + "}/api/proxy/business/processo",
    interceptors = CosmoEcmM2MFeignClientConfiguration.class,
    configurator = DefaultFeignClientConfiguration.class)
public interface CosmoBusinessProcessoFeignClient extends ProcessoApi {
}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmobe.integration.rest;

import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmo.client.DiscoveryApi;
import it.csi.cosmo.cosmobe.config.ParametriApplicativo;


/**
 *
 */

@FeignClient(value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY + "}/api/discovery",
interceptors = CosmoBeFeignClientConfiguration.class)
public interface CosmoDiscoveryFeignClient extends DiscoveryApi {
}

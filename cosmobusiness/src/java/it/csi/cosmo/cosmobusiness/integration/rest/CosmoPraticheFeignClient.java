/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmobusiness.integration.rest;

import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmobusiness.config.ParametriApplicativo;
import it.csi.cosmo.cosmopratiche.client.PraticheApi;


/**
 *
 */

@FeignClient(
    value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY
    + "}/api/proxy/pratiche/pratiche",
    interceptors = CosmoBusinessFeignClientConfiguration.class,
    configurator = DefaultFeignClientConfiguration.class)
public interface CosmoPraticheFeignClient extends PraticheApi {

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.integration.rest;

import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmoauthorization.client.UtentiApi;
import it.csi.cosmo.cosmocmmn.config.ParametriApplicativo;

/**
 *
 */


@FeignClient(
    value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY
        + "}/api/proxy/authorization/utenti",
    configurator = CosmoCmmnFeignClientConfiguration.class,
    interceptors = CosmoCmmnFeignClientM2MAuthenticator.class)
public interface CosmoAuthorizationUtentiFeignClient extends UtentiApi {

}

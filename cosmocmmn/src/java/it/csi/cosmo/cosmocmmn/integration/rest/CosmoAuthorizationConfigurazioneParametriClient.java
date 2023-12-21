/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.integration.rest;

import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmoauthorization.client.ConfigurazioneApi;
import it.csi.cosmo.cosmocmmn.config.ParametriApplicativo;


/**
 *
 */

@FeignClient(
    value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY
        + "}/api/proxy/authorization/configurazione",
    interceptors = CosmoCmmnFeignClientM2MAuthenticator.class,
    configurator = CosmoCmmnFeignClientConfiguration.class)
public interface CosmoAuthorizationConfigurazioneParametriClient extends ConfigurazioneApi {
}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.integration.rest;

import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmocmmn.config.ParametriApplicativo;
import it.csi.cosmo.cosmoecm.client.SigilloElettronicoApi;

/**
 *
 */


@FeignClient(value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY
    + "}/api/proxy/ecm/sigillo-elettronico",
    configurator = CosmoCmmnFeignClientConfiguration.class,
    interceptors = CosmoCmmnFeignClientAuthenticator.class)
public interface CosmoEcmSigilloElettronicoFeignClient extends SigilloElettronicoApi {

}

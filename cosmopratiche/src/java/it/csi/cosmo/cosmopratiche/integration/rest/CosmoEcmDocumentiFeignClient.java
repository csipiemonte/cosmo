/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.rest;

import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmoecm.client.DocumentiApi;
import it.csi.cosmo.cosmopratiche.config.ParametriApplicativo;

/**
 *
 */
@FeignClient(
    value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY
    + "}/api/proxy/ecm/documenti",
    interceptors = CosmoPraticheFeignClientConfiguration.class,
    configurator = DefaultFeignClientConfiguration.class)
public interface CosmoEcmDocumentiFeignClient extends DocumentiApi {

}

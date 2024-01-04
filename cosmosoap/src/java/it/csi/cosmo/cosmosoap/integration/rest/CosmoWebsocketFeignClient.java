/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.rest;

import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmo.client.WebSocketProxyApi;
import it.csi.cosmo.cosmosoap.config.ParametriApplicativo;

/**
 *
 */
@FeignClient(value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY + "}/api/websocket",
    interceptors = CosmoSoapM2MFeignClientConfiguration.class,
    configurator = DefaultFeignClientConfiguration.class)
public interface CosmoWebsocketFeignClient extends WebSocketProxyApi {
}

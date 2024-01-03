/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmonotifications.integration.rest;

import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmo.client.WebSocketProxyApi;
import it.csi.cosmo.cosmonotifications.config.ParametriApplicativo;


/**
 *
 */

@FeignClient(value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY + "}/api/websocket",
    interceptors = CosmoNotificationsFeignClientConfiguration.class,
    configurator = DefaultFeignClientConfiguration.class)
public interface CosmoWebsocketFeignClient extends WebSocketProxyApi {
}

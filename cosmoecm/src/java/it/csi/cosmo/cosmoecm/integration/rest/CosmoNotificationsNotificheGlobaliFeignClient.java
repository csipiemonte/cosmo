/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.rest;

import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo;
import it.csi.cosmo.cosmonotifications.client.NotificheGlobaliApi;

@FeignClient(
    value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY
    + "}/api/proxy/notifications/notifiche-globali",
    interceptors = CosmoEcmM2MFeignClientConfiguration.class,
    configurator = DefaultFeignClientConfiguration.class)
public interface CosmoNotificationsNotificheGlobaliFeignClient extends NotificheGlobaliApi {
}


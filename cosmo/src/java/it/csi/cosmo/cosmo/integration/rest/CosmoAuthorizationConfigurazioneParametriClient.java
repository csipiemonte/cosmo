/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.integration.rest;

import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmoauthorization.client.ConfigurazioneApi;


/**
 *
 */

@FeignClient(value = "${service:cosmoauthorization}/api/configurazione",
interceptors = FeignClientJWTConfigurator.class,
configurator = DefaultFeignClientConfiguration.class)
public interface CosmoAuthorizationConfigurazioneParametriClient extends ConfigurazioneApi {

}

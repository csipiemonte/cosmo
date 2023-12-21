/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.integration.rest;

import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmoauthorization.client.FruitoriApi;


/**
 *
 */

@FeignClient(
    value = "${service:cosmoauthorization}/api/fruitori",
    interceptors = FeignClientM2MConfigurator.class)
public interface CosmoAuthorizationFruitoriFeignClient extends FruitoriApi {
}

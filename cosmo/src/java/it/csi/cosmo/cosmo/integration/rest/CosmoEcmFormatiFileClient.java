/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.integration.rest;

import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmoecm.client.FormatiFileApi;


/**
 *
 */

@FeignClient(value = "${service:cosmoecm}/api/formati-file",
interceptors = FeignClientJWTConfigurator.class,
configurator = DefaultFeignClientConfiguration.class)
public interface CosmoEcmFormatiFileClient extends FormatiFileApi {

}

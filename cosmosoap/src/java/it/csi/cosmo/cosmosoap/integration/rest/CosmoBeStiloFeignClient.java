/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
/*
 * Copyright 2001-2019 CSI Piemonte. All Rights Reserved.
 *
 * This software is proprietary information of CSI Piemonte. Use is subject to license terms.
 *
 */

package it.csi.cosmo.cosmosoap.integration.rest;

import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmobe.client.StiloApi;
import it.csi.cosmo.cosmosoap.config.ParametriApplicativo;


/**
 *
 */
@FeignClient(
    value = "${" + ParametriApplicativo.STILO_COSMOBEPROXY
    + "}",
    interceptors = CosmoSoapFeignClientConfiguration.class,
    configurator = DefaultFeignClientConfiguration.class)

public interface CosmoBeStiloFeignClient extends StiloApi {
}

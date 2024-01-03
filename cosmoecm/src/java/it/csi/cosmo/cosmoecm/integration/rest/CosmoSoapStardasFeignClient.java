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

package it.csi.cosmo.cosmoecm.integration.rest;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo;
import it.csi.cosmo.cosmosoap.client.StardasApi;
import it.csi.cosmo.cosmosoap.dto.rest.GetStatoRichiestaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.GetStatoRichiestaResponse;
import it.csi.cosmo.cosmosoap.dto.rest.SmistaDocumentoRequest;
import it.csi.cosmo.cosmosoap.dto.rest.SmistaDocumentoResponse;

/**
 *
 */

@FeignClient(
    value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY
    + "}/api/proxy/soap/stardas",
    interceptors = CosmoEcmM2MFeignClientConfiguration.class,
    configurator = CosmoSoapStardasFeignClientConfiguration.class)
public interface CosmoSoapStardasFeignClient extends StardasApi {

  @Override
  @POST
  @Path("/smistamento")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public SmistaDocumentoResponse smistamentoDocumento(@Valid SmistaDocumentoRequest body);

  @Override
  @GET
  @Path("/smistamento")
  @Produces({"application/json"})
  public GetStatoRichiestaResponse statoRichiesta(@Valid GetStatoRichiestaRequest body);
}

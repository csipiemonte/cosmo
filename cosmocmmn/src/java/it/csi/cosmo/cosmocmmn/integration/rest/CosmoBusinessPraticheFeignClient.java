/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.integration.rest;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmobusiness.client.PraticheApi;
import it.csi.cosmo.cosmobusiness.dto.rest.GetElaborazionePraticaRequest;
import it.csi.cosmo.cosmocmmn.config.ParametriApplicativo;

/**
 *
 */


@FeignClient(value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY
    + "}/api/proxy/business/pratiche", configurator = CosmoCmmnFeignClientConfiguration.class,
    interceptors = CosmoCmmnFeignClientAuthenticator.class)
public interface CosmoBusinessPraticheFeignClient extends PraticheApi {

  @POST
  @Path("/{id}/elaborazione")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public JsonNode postPraticheIdElaborazioneJsonNode(@PathParam("id") Long id,
      @Valid GetElaborazionePraticaRequest body);

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.integration.rest;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmocmmn.config.ParametriApplicativo;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticaInRelazione;
import it.csi.cosmo.cosmopratiche.dto.rest.StoricoPraticaRequest;

/**
 *
 */

@FeignClient(value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY
+ "}/api/proxy/pratiche/pratiche", configurator = CosmoCmmnFeignClientConfiguration.class,
interceptors = CosmoCmmnFeignClientAuthenticator.class)
public interface CosmoPratichePraticheFeignClient {

  @GET
  @Path("/{idPratica}/in-relazione")
  @Produces({"application/json"})
  public PraticaInRelazione[] getPraticheIdPraticaRelazioni(
      @PathParam("idPratica") Integer idPratica);


  @GET
  @Path("/{idPratica}")
  @Produces({"application/json"})
  public Pratica getPraticheIdPratica(@PathParam("idPratica") String idPratica,
      @QueryParam("annullata") Boolean annullata);

  @POST
  @Path("/{idPratica}/storico")
  @Consumes({"application/json"})
  public void postPraticheIdPraticaStorico(@PathParam("idPratica") Integer idPratica,
      @Valid StoricoPraticaRequest body);


}

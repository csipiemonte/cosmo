/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmoecm.integration.rest;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmobusiness.client.PraticheApi;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo;
import it.csi.cosmo.cosmoecm.dto.Task;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;

/**
 *
 */

@FeignClient(
    value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY
    + "}/api/proxy/business/pratiche",
    interceptors = CosmoEcmM2MFeignClientConfiguration.class,
    configurator = DefaultFeignClientConfiguration.class)
public interface CosmoBusinessPraticheFeignClient extends PraticheApi {

  @POST
  @Path("/{idPratica}/attivita/{idAttivita}/conferma")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public RiferimentoOperazioneAsincrona postPraticaAttivitaConfermaConLock(
      @PathParam("idPratica") Long idPratica, @PathParam("idAttivita") Long idAttivita,
      @Valid Task body, @HeaderParam("X-Resource-Lock") String codiceOwnerLock);

  @POST
  @Path("/{idPratica}/attivita/{idAttivita}/salva")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public RiferimentoOperazioneAsincrona postPraticaAttivitaSalvaConLock(
      @PathParam("idPratica") Long idPratica, @PathParam("idAttivita") Long idAttivita,
      @Valid Task body, @HeaderParam("X-Resource-Lock") String codiceOwnerLock);

  @Override
  @GET
  @Path("/{idPratica}/elaborazione/contesto")
  @Produces({"application/json"})
  public Object getPraticheIdElaborazioneContesto(@PathParam("idPratica") Long idPratica);
}

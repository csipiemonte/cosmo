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
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo;
import it.csi.cosmo.cosmosoap.client.ActaApi;
import it.csi.cosmo.cosmosoap.dto.rest.Classificazioni;
import it.csi.cosmo.cosmosoap.dto.rest.ContenutoDocumentoFisico;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiFisiciMap;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiFisiciResponse;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiSemplici;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiSempliciMap;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiSempliciResponse;
import it.csi.cosmo.cosmosoap.dto.rest.IdentitaActaResponse;
import it.csi.cosmo.cosmosoap.dto.rest.ImportaDocumentiRequest;
import it.csi.cosmo.cosmosoap.dto.rest.Protocollo;
import it.csi.cosmo.cosmosoap.dto.rest.RegistrazioniClassificazioni;
import it.csi.cosmo.cosmosoap.dto.rest.Titolario;
import it.csi.cosmo.cosmosoap.dto.rest.VociTitolario;

/**
 *
 */

@FeignClient(
    value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY
    + "}/api/proxy/soap/acta",
    interceptors = CosmoEcmM2MFeignClientConfiguration.class,
    configurator = CosmoSoapFeignClientConfiguration.class)
public interface CosmoSoapActaFeignClient extends ActaApi {
  @Override
  @GET
  @Path("/classificazioni/{idDocumentoSemplice}")
  @Produces({"application/json"})
  Classificazioni getClassificazioniIdDocumentoSemplice(
      @PathParam("idDocumentoSemplice") String var1,
      @NotNull @QueryParam("idIdentita") String var2);

  @Override
  @GET
  @Path("/contenuto-primario/{id}")
  @Produces({"application/json"})
  ContenutoDocumentoFisico getContenutoPrimarioId(@PathParam("id") String var1,
      @NotNull @QueryParam("idIdentita") String var2);

  @Override
  @GET
  @Path("/documenti-fisici/{idDocumentoSemplice}")
  @Produces({"application/json"})
  DocumentiFisiciResponse getDocumentiFisiciByidDocumentoSemplice(
      @PathParam("idDocumentoSemplice") String var1,
      @NotNull @QueryParam("idIdentita") String var2);

  @Override
  @PUT
  @Path("/documenti-fisici-map")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  DocumentiFisiciMap getDocumentiFisiciMap(@NotNull @QueryParam("idIdentita") String var1,
      @Valid ImportaDocumentiRequest var3);

  @Override
  @GET
  @Path("/documenti-semplici")
  @Produces({"application/json"})
  DocumentiSempliciResponse getDocumentiSemplici(@NotNull @QueryParam("idIdentita") String var1,
      @QueryParam("parolaChiave") String var2, @QueryParam("idClassificazione") String var3);

  @Override
  @PUT
  @Path("/documenti-semplici-map")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  DocumentiSempliciMap getDocumentiSempliciMap(@NotNull @QueryParam("idIdentita") String var1,
      @Valid ImportaDocumentiRequest var2);

  @Override
  @GET
  @Path("/documenti-semplici-pageable")
  @Produces({"application/json"})
  DocumentiSemplici getDocumentiSempliciPageable(@NotNull @QueryParam("filter") String var1,
      @NotNull @QueryParam("idIdentita") String var2,
      @NotNull @QueryParam("perProtocollo") Boolean var3);

  @Override
  @GET
  @Path("/identita-disponibili")
  @Produces({"application/json"})
  IdentitaActaResponse getIdentitaDisponibili();

  @Override
  @GET
  @Path("/protocollo/{id}")
  @Produces({"application/json"})
  Protocollo getProtocolloId(@PathParam("id") String var1,
      @NotNull @QueryParam("idIdentita") String var2);

  @Override
  @GET
  @Path("/registrazioni")
  @Produces({"application/json"})
  RegistrazioniClassificazioni getRegistrazioni(@NotNull @QueryParam("idIdentita") String var1,
      @QueryParam("filter") String var2);

  @Override
  @GET
  @Path("/ricerca-per-indice-classificazione-esteso")
  @Produces({"application/json"})
  String getRicercaPerIndiceClassificazioneEstesa(
      @QueryParam("identita") String identita,
      @QueryParam("indiceClassificazioneEsteso") String indiceClassificazioneEsteso);

  @Override
  @GET @Path("/ricerca-titolario")  @Produces({ "application/json" })
  public Titolario getRicercaTitolario(@NotNull @QueryParam("codice") String codice,
      @NotNull @QueryParam("idIdentita") String idIdentita);

  @Override
  @GET @Path("/ricerca-alberatura-voci-pageable")  @Produces({ "application/json" })
  public VociTitolario ricercaAlberaturaVociPageable(
      @NotNull @QueryParam("idIdentita") String idIdentita,
      @NotNull @QueryParam("chiaveTitolario") String chiaveTitolario,
      @NotNull @QueryParam("filter") String filter, @QueryParam("chiavePadre") String chiavePadre);

}

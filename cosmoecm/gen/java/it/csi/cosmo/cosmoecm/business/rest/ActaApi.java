/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest;

import it.csi.cosmo.cosmoecm.dto.rest.*;


import it.csi.cosmo.cosmoecm.dto.rest.DocumentiFisiciActaResponse;
import it.csi.cosmo.cosmoecm.dto.rest.Esito;
import it.csi.cosmo.cosmoecm.dto.rest.IdentitaUtenteResponse;
import it.csi.cosmo.cosmoecm.dto.rest.ImportaDocumentiActaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmoecm.dto.rest.TitolarioActa;
import it.csi.cosmo.cosmoecm.dto.rest.VociTitolarioActa;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.validation.constraints.*;

@Path("/acta")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface ActaApi  {
   
    @GET
    @Path("/documenti/ricerca-con-indice-classificazione-esteso-aggregazione")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getActaDocumentiRicercaConIndiceClassificazioneEstesoAggregazione( @NotNull @QueryParam("identita") String identita, @NotNull @QueryParam("indiceClassificazioneEsteso") String indiceClassificazioneEsteso,@Context SecurityContext securityContext);
    @GET
    @Path("/documenti")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getDocumentiActa( @NotNull @QueryParam("identita") String identita, @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    @Path("/documenti/{id}/documenti-fisici")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getDocumentiFisiciActa( @PathParam("id") String id, @NotNull @QueryParam("identita") String identita,@Context SecurityContext securityContext);
    @GET
    @Path("/identita-utente")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getIdentitaUtente(@Context SecurityContext securityContext);
    @GET
    @Path("/ricerca-alberatura-voci-pageable")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getRicercaAlberaturaVociPageable( @NotNull @QueryParam("idIdentita") String idIdentita, @NotNull @QueryParam("chiaveTitolario") String chiaveTitolario, @NotNull @QueryParam("filter") String filter, @QueryParam("chiavePadre") String chiavePadre,@Context SecurityContext securityContext);
    @GET
    @Path("/ricerca-titolario")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getRicercaTitolario( @NotNull @QueryParam("codice") String codice, @NotNull @QueryParam("idIdentita") String idIdentita,@Context SecurityContext securityContext);
    @POST
    @Path("/documenti/importa")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response importaDocumentiActa( @NotNull @QueryParam("identita") String identita, ImportaDocumentiActaRequest body,@Context SecurityContext securityContext);
}

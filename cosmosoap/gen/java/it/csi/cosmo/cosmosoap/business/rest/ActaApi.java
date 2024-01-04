/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.business.rest;

import it.csi.cosmo.cosmosoap.dto.rest.*;


import it.csi.cosmo.cosmosoap.dto.rest.Classificazioni;
import it.csi.cosmo.cosmosoap.dto.rest.ContenutoDocumentoFisico;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiFisiciMap;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiFisiciResponse;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiSemplici;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiSempliciMap;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiSempliciResponse;
import it.csi.cosmo.cosmosoap.dto.rest.Esito;
import it.csi.cosmo.cosmosoap.dto.rest.IdentitaActaResponse;
import it.csi.cosmo.cosmosoap.dto.rest.ImportaDocumentiRequest;
import it.csi.cosmo.cosmosoap.dto.rest.Protocollo;
import it.csi.cosmo.cosmosoap.dto.rest.RegistrazioniClassificazioni;
import it.csi.cosmo.cosmosoap.dto.rest.Titolario;
import it.csi.cosmo.cosmosoap.dto.rest.VociTitolario;

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
    @Path("/classificazioni/{idDocumentoSemplice}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getClassificazioniIdDocumentoSemplice( @PathParam("idDocumentoSemplice") String idDocumentoSemplice, @NotNull @QueryParam("idIdentita") String idIdentita,@Context SecurityContext securityContext);
    @GET
    @Path("/contenuto-primario/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getContenutoPrimarioId( @PathParam("id") String id, @NotNull @QueryParam("idIdentita") String idIdentita,@Context SecurityContext securityContext);
    @GET
    @Path("/documenti-fisici/{idDocumentoSemplice}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getDocumentiFisiciByidDocumentoSemplice( @PathParam("idDocumentoSemplice") String idDocumentoSemplice, @NotNull @QueryParam("idIdentita") String idIdentita,@Context SecurityContext securityContext);
    @PUT
    @Path("/documenti-fisici-map")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getDocumentiFisiciMap( @NotNull @QueryParam("idIdentita") String idIdentita, ImportaDocumentiRequest body,@Context SecurityContext securityContext);
    @GET
    @Path("/documenti-semplici")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getDocumentiSemplici( @NotNull @QueryParam("idIdentita") String idIdentita, @QueryParam("parolaChiave") String parolaChiave, @QueryParam("idClassificazione") String idClassificazione,@Context SecurityContext securityContext);
    @PUT
    @Path("/documenti-semplici-map")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getDocumentiSempliciMap( @NotNull @QueryParam("idIdentita") String idIdentita, ImportaDocumentiRequest body,@Context SecurityContext securityContext);
    @GET
    @Path("/documenti-semplici-pageable")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getDocumentiSempliciPageable( @NotNull @QueryParam("filter") String filter, @NotNull @QueryParam("idIdentita") String idIdentita, @NotNull @QueryParam("perProtocollo") Boolean perProtocollo,@Context SecurityContext securityContext);
    @GET
    @Path("/identita-disponibili")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getIdentitaDisponibili(@Context SecurityContext securityContext);
    @GET
    @Path("/protocollo/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getProtocolloId( @PathParam("id") String id, @NotNull @QueryParam("idIdentita") String idIdentita,@Context SecurityContext securityContext);
    @GET
    @Path("/registrazioni")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getRegistrazioni( @NotNull @QueryParam("idIdentita") String idIdentita, @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    @Path("/ricerca-per-indice-classificazione-esteso")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getRicercaPerIndiceClassificazioneEstesa( @QueryParam("identita") String identita, @QueryParam("indiceClassificazioneEsteso") String indiceClassificazioneEsteso,@Context SecurityContext securityContext);
    @GET
    @Path("/ricerca-titolario")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getRicercaTitolario( @NotNull @QueryParam("codice") String codice, @NotNull @QueryParam("idIdentita") String idIdentita,@Context SecurityContext securityContext);
    @GET
    @Path("/ricerca-alberatura-voci-pageable")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response ricercaAlberaturaVociPageable( @NotNull @QueryParam("idIdentita") String idIdentita, @NotNull @QueryParam("chiaveTitolario") String chiaveTitolario, @NotNull @QueryParam("filter") String filter, @QueryParam("chiavePadre") String chiavePadre,@Context SecurityContext securityContext);
}

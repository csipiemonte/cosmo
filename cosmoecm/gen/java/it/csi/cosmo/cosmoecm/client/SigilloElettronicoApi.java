/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.client;

import it.csi.cosmo.cosmoecm.dto.rest.*;

import it.csi.cosmo.cosmoecm.dto.rest.CreaCredenzialiSigilloElettronicoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CredenzialiSigilloElettronico;
import it.csi.cosmo.cosmoecm.dto.rest.CredenzialiSigilloElettronicoResponse;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/sigillo-elettronico")  
public interface SigilloElettronicoApi  {
   
    @DELETE @Path("/{id}")  @Produces({ "application/json" })
    public void deleteSigilloElettronicoId( @PathParam("id") Integer id);

    @GET   @Produces({ "application/json" })
    public CredenzialiSigilloElettronicoResponse getSigilloElettronico(  @QueryParam("filter") String filter);

    @GET @Path("/{id}")  @Produces({ "application/json" })
    public CredenzialiSigilloElettronico getSigilloElettronicoId( @PathParam("id") Integer id);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public CredenzialiSigilloElettronico postSigilloElettronico( @Valid CreaCredenzialiSigilloElettronicoRequest body);

    @POST @Path("/{idPratica}/aggiornaSigilliInErrore")  
    public void postSigilloElettronicoIdPraticaAggiornaSigilliInErrore( @PathParam("idPratica") Long idPratica,  @NotNull  @QueryParam("identificativoEvento") String identificativoEvento,  @NotNull  @QueryParam("identificativoAlias") String identificativoAlias);

    @PUT @Path("/{id}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public CredenzialiSigilloElettronico putSigilloElettronicoId( @PathParam("id") Integer id,  @Valid CreaCredenzialiSigilloElettronicoRequest body);

}

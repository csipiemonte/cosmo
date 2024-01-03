/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest;

import it.csi.cosmo.cosmoecm.dto.rest.*;


import it.csi.cosmo.cosmoecm.dto.rest.CreaCredenzialiSigilloElettronicoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CredenzialiSigilloElettronico;
import it.csi.cosmo.cosmoecm.dto.rest.CredenzialiSigilloElettronicoResponse;

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

@Path("/sigillo-elettronico")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface SigilloElettronicoApi  {
   
    @DELETE
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteSigilloElettronicoId( @PathParam("id") Integer id,@Context SecurityContext securityContext);
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getSigilloElettronico( @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getSigilloElettronicoId( @PathParam("id") Integer id,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postSigilloElettronico( CreaCredenzialiSigilloElettronicoRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/{idPratica}/aggiornaSigilliInErrore")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postSigilloElettronicoIdPraticaAggiornaSigilliInErrore( @PathParam("idPratica") Long idPratica, @NotNull @QueryParam("identificativoEvento") String identificativoEvento, @NotNull @QueryParam("identificativoAlias") String identificativoAlias,@Context SecurityContext securityContext);
    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putSigilloElettronicoId( @PathParam("id") Integer id, CreaCredenzialiSigilloElettronicoRequest body,@Context SecurityContext securityContext);
}

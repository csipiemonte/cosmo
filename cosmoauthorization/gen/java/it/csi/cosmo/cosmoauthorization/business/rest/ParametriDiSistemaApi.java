/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.rest;

import it.csi.cosmo.cosmoauthorization.dto.rest.*;


import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaParametroDiSistemaRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaParametroDiSistemaRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.ParametroDiSistema;
import it.csi.cosmo.cosmoauthorization.dto.rest.ParametroDiSistemaResponse;

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

@Path("/parametri-di-sistema")

@Produces({ "application/json" })


public interface ParametriDiSistemaApi  {
   
    @DELETE
    @Path("/{chiave}")
    
    @Produces({ "application/json" })

    public Response deleteParametroDiSistemaByChiave( @PathParam("chiave") String chiave,@Context SecurityContext securityContext);
    @GET
    
    
    @Produces({ "application/json" })

    public Response getParametriDiSistema( @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    @Path("/{chiave}")
    
    @Produces({ "application/json" })

    public Response getParametroDiSistemaByChiave( @PathParam("chiave") String chiave,@Context SecurityContext securityContext);
    @POST
    
    
    @Produces({ "application/json" })

    public Response postParametroDiSistema( CreaParametroDiSistemaRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{chiave}")
    
    @Produces({ "application/json" })

    public Response putParametroDiSistemaByChiave( @PathParam("chiave") String chiave, AggiornaParametroDiSistemaRequest body,@Context SecurityContext securityContext);
}

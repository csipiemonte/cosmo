/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest;

import it.csi.cosmo.cosmopratiche.dto.rest.*;


import it.csi.cosmo.cosmopratiche.dto.rest.CustomForm;
import it.csi.cosmo.cosmopratiche.dto.rest.CustomFormResponse;

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

@Path("/custom-forms")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface CustomFormsApi  {
   
    @DELETE
    @Path("/{codice}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteCustomForm( @PathParam("codice") String codice,@Context SecurityContext securityContext);
    @GET
    @Path("/{codice}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getCustomForm( @PathParam("codice") String codice,@Context SecurityContext securityContext);
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getCustomForms( @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    @Path("/tipo-pratica/{codiceTipoPratica}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getCustomFormsCodiceTipoPratica( @PathParam("codiceTipoPratica") String codiceTipoPratica,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postCustomForm( CustomForm body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{codice}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putCustomForm( @PathParam("codice") String codice, CustomForm body,@Context SecurityContext securityContext);
}

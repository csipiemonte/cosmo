/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.client;

import it.csi.cosmo.cosmopratiche.dto.rest.*;

import it.csi.cosmo.cosmopratiche.dto.rest.CustomForm;
import it.csi.cosmo.cosmopratiche.dto.rest.CustomFormResponse;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/custom-forms")  
public interface CustomFormsApi  {
   
    @DELETE @Path("/{codice}")  
    public void deleteCustomForm( @PathParam("codice") String codice);

    @GET @Path("/{codice}")  @Produces({ "application/json" })
    public CustomForm getCustomForm( @PathParam("codice") String codice);

    @GET   @Produces({ "application/json" })
    public CustomFormResponse getCustomForms(  @QueryParam("filter") String filter);

    @GET @Path("/tipo-pratica/{codiceTipoPratica}")  @Produces({ "application/json" })
    public CustomForm getCustomFormsCodiceTipoPratica( @PathParam("codiceTipoPratica") String codiceTipoPratica);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public CustomForm postCustomForm( @Valid CustomForm body);

    @PUT @Path("/{codice}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public CustomForm putCustomForm( @PathParam("codice") String codice,  @Valid CustomForm body);

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.client;

import it.csi.cosmo.cosmoecm.dto.rest.*;

import it.csi.cosmo.cosmoecm.dto.rest.CreaTemplateFeaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateFea;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateFeaResponse;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/template-fea")  
public interface TemplateFeaApi  {
   
    @DELETE @Path("/{id}")  @Produces({ "application/json" })
    public void deleteTemplateFeaId( @PathParam("id") Long id);

    @GET   @Produces({ "application/json" })
    public TemplateFeaResponse getTemplateFea(  @QueryParam("filter") String filter,   @QueryParam("tutti") Boolean tutti);

    @GET @Path("/{id}")  @Produces({ "application/json" })
    public TemplateFea getTemplateFeaId( @PathParam("id") Long id);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public TemplateFea postTemplateFea( @Valid CreaTemplateFeaRequest body);

    @PUT @Path("/{id}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public TemplateFea putTemplateFeaId( @PathParam("id") Long id,  @Valid CreaTemplateFeaRequest body);

}

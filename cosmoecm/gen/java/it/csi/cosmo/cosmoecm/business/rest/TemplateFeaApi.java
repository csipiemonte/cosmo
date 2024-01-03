/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest;

import it.csi.cosmo.cosmoecm.dto.rest.*;


import it.csi.cosmo.cosmoecm.dto.rest.CreaTemplateFeaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateFea;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateFeaResponse;

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

@Path("/template-fea")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface TemplateFeaApi  {
   
    @DELETE
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteTemplateFeaId( @PathParam("id") Long id,@Context SecurityContext securityContext);
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getTemplateFea( @QueryParam("filter") String filter, @QueryParam("tutti") Boolean tutti,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getTemplateFeaId( @PathParam("id") Long id,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postTemplateFea( CreaTemplateFeaRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putTemplateFeaId( @PathParam("id") Long id, CreaTemplateFeaRequest body,@Context SecurityContext securityContext);
}

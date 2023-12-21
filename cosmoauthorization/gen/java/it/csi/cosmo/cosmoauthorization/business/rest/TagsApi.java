/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.rest;

import it.csi.cosmo.cosmoauthorization.dto.rest.*;


import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaTagRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaTagRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.TagResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.TagsResponse;

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

@Path("/tags")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface TagsApi  {
   
    @DELETE
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteTagsId( @PathParam("id") Long id,@Context SecurityContext securityContext);
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getTags( @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getTagsId( @PathParam("id") Long id,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postTags( CreaTagRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putTagsId( @PathParam("id") Long id, AggiornaTagRequest body,@Context SecurityContext securityContext);
}

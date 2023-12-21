/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.client;

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
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/tags")  
public interface TagsApi  {
   
    @DELETE @Path("/{id}")  @Produces({ "application/json" })
    public void deleteTagsId( @PathParam("id") Long id);

    @GET   @Produces({ "application/json" })
    public TagsResponse getTags(  @QueryParam("filter") String filter);

    @GET @Path("/{id}")  @Produces({ "application/json" })
    public TagResponse getTagsId( @PathParam("id") Long id);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public TagResponse postTags( @Valid CreaTagRequest body);

    @PUT @Path("/{id}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public TagResponse putTagsId( @PathParam("id") Long id,  @Valid AggiornaTagRequest body);

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.client;

import it.csi.cosmo.cosmobusiness.dto.rest.*;

import it.csi.cosmo.cosmobusiness.dto.rest.Commento;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaCommenti;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaTask;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/task")  
public interface TaskApi  {
   
    @GET   @Produces({ "application/json" })
    public PaginaTask getTask(  @QueryParam("dueAfter") String dueAfter,   @QueryParam("sort") String sort,   @QueryParam("size") String size,   @QueryParam("start") String start,   @QueryParam("dueBefore") String dueBefore);

    @GET @Path("/{id}")  @Produces({ "application/json" })
    public Task getTaskId( @PathParam("id") String id);

    @GET @Path("/{idtask}/commenti")  @Produces({ "application/json" })
    public PaginaCommenti getTaskIdtaskComments( @PathParam("idtask") String idtask);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Task postTask( @Valid Task body);

    @POST @Path("/{id}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Task postTaskId( @PathParam("id") String id,  @Valid Task body);

    @POST @Path("/{idtask}/commenti") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Commento postTaskIdtaskComments( @PathParam("idtask") String idtask,   @QueryParam("reply") Boolean reply,  @Valid Commento body);

    @PUT @Path("/{id}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Task putTaskId( @PathParam("id") String id,  @Valid Task body);

}

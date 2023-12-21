/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest;

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
import javax.ws.rs.core.HttpHeaders;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.validation.constraints.*;

@Path("/task")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface TaskApi  {
   
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getTask( @QueryParam("dueAfter") String dueAfter, @QueryParam("sort") String sort, @QueryParam("size") String size, @QueryParam("start") String start, @QueryParam("dueBefore") String dueBefore,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getTaskId( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    @Path("/{idtask}/commenti")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getTaskIdtaskComments( @PathParam("idtask") String idtask,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postTask( Task body,@Context SecurityContext securityContext);
    @POST
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postTaskId( @PathParam("id") String id, Task body,@Context SecurityContext securityContext);
    @POST
    @Path("/{idtask}/commenti")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postTaskIdtaskComments( @PathParam("idtask") String idtask, Commento body, @QueryParam("reply") Boolean reply,@Context SecurityContext securityContext);
    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putTaskId( @PathParam("id") String id, Task body,@Context SecurityContext securityContext);
}

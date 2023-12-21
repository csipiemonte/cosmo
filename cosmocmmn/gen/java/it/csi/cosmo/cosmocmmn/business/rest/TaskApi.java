/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.rest;

import it.csi.cosmo.cosmocmmn.dto.rest.*;


import it.csi.cosmo.cosmocmmn.dto.rest.AssegnaTaskRequest;
import it.csi.cosmo.cosmocmmn.dto.rest.AssegnaTaskResponse;

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
   
    @POST
    @Path("/{idTask}/assegna")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postTaskAssegna( @PathParam("idTask") String idTask, AssegnaTaskRequest body,@Context SecurityContext securityContext);
}

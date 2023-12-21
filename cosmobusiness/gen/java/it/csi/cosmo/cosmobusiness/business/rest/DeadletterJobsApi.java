/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest;

import it.csi.cosmo.cosmobusiness.dto.rest.*;


import it.csi.cosmo.cosmobusiness.dto.rest.DeadLetterJobAction;
import it.csi.cosmo.cosmobusiness.dto.rest.DeadLetterJobsResponse;

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

@Path("/deadletter-jobs")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface DeadletterJobsApi  {
   
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getDeadletterJobs(@Context SecurityContext securityContext);
    @POST
    @Path("/{jobId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postDeadletterJobsJobId( @PathParam("jobId") String jobId, DeadLetterJobAction body,@Context SecurityContext securityContext);
}

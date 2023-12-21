/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.client;

import it.csi.cosmo.cosmobusiness.dto.rest.*;

import it.csi.cosmo.cosmobusiness.dto.rest.DeadLetterJobAction;
import it.csi.cosmo.cosmobusiness.dto.rest.DeadLetterJobsResponse;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/deadletter-jobs")  
public interface DeadletterJobsApi  {
   
    @GET   @Produces({ "application/json" })
    public DeadLetterJobsResponse getDeadletterJobs();

    @POST @Path("/{jobId}") @Consumes({ "application/json" }) 
    public void postDeadletterJobsJobId( @PathParam("jobId") String jobId,  @Valid DeadLetterJobAction body);

}

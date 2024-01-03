/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest;

import it.csi.cosmo.cosmoecm.dto.rest.*;


import it.csi.cosmo.cosmoecm.dto.rest.FormatoFileResponse;
import it.csi.cosmo.cosmoecm.dto.rest.RaggruppamentoFormatiFileResponse;

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

@Path("/formati-file")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface FormatiFileApi  {
   
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getFormatiFile( @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    @Path("/grouped")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getFormatiFileGrouped( @QueryParam("filter") String filter,@Context SecurityContext securityContext);
}

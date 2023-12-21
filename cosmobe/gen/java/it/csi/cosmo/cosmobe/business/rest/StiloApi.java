/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.business.rest;

import it.csi.cosmo.cosmobe.dto.rest.*;


import it.csi.cosmo.cosmobe.dto.rest.AddUdRequest;
import it.csi.cosmo.cosmobe.dto.rest.AddUdResponse;
import it.csi.cosmo.cosmobe.dto.rest.UpUdRequest;
import it.csi.cosmo.cosmobe.dto.rest.UpUdResponse;

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

@Path("/stilo")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface StiloApi  {
   
    @POST
    @Path("/inserimentoCosmo")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postStiloInserimentoCosmo( AddUdRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/aggiornamentoCosmo")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putStiloAggiornamentoCosmo( UpUdRequest body,@Context SecurityContext securityContext);
}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.business.rest;

import it.csi.cosmo.cosmo.dto.rest.*;


import it.csi.cosmo.cosmo.dto.rest.LoginRequest;
import it.csi.cosmo.cosmo.dto.rest.LogoutResponse;
import it.csi.cosmo.cosmo.dto.rest.UserInfo;

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

@Path("/sessione")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface SessioneApi  {
   
    @DELETE
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteSessione( @QueryParam("ambienteLogout") String ambienteLogout,@Context SecurityContext securityContext);
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getSessione(@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postSessione( LoginRequest body,@Context SecurityContext securityContext);
}

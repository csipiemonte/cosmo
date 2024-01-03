/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.business.rest;

import it.csi.cosmo.cosmonotifications.dto.rest.*;


import it.csi.cosmo.cosmonotifications.dto.rest.NotificheGlobaliRequest;

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

@Path("/notifiche-globali")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface NotificheGlobaliApi  {
   
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postNotificheGlobali( NotificheGlobaliRequest body,@Context SecurityContext securityContext);
}

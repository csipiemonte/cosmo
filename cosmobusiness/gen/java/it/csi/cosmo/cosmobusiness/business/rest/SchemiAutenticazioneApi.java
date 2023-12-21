/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest;

import it.csi.cosmo.cosmobusiness.dto.rest.*;



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

@Path("/schemi-autenticazione")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface SchemiAutenticazioneApi  {
   
    @POST
    @Path("/{idSchemaAutenticazione}/test")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response testSchemaAutenticazione( @PathParam("idSchemaAutenticazione") Long idSchemaAutenticazione,@Context SecurityContext securityContext);
}

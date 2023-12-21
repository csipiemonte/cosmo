/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest;

import it.csi.cosmo.cosmobusiness.dto.rest.*;


import it.csi.cosmo.cosmobusiness.dto.rest.AcquisizioneLockRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.Esito;
import it.csi.cosmo.cosmobusiness.dto.rest.Lock;
import it.csi.cosmo.cosmobusiness.dto.rest.RilascioLockRequest;

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

@Path("/lock")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface LockApi  {
   
    @DELETE
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteLock( RilascioLockRequest body,@Context SecurityContext securityContext);
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getLock( @NotNull @Size(min=1,max=255) @QueryParam("codiceRisorsa") String codiceRisorsa,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postLock( AcquisizioneLockRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/release")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postLockRelease( RilascioLockRequest body,@Context SecurityContext securityContext);
}

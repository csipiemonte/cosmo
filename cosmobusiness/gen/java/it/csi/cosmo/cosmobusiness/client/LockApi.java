/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.client;

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
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/lock")  
public interface LockApi  {
   
    @DELETE  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public void deleteLock( @Valid RilascioLockRequest body);

    @GET   @Produces({ "application/json" })
    public Lock getLock( @NotNull @Size(min=1,max=255)  @QueryParam("codiceRisorsa") String codiceRisorsa);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Lock postLock( @Valid AcquisizioneLockRequest body);

    @POST @Path("/release") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public void postLockRelease( @Valid RilascioLockRequest body);

}

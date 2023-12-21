/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.client;

import it.csi.cosmo.cosmobusiness.dto.rest.*;

import it.csi.cosmo.cosmobusiness.dto.rest.CustomCallbackResponse;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/custom-callback")  
public interface CustomCallbackApi  {
   
    @GET   @Produces({ "application/json" })
    public CustomCallbackResponse getCustomCallback( @NotNull  @QueryParam("apiManagerId") String apiManagerId,  @NotNull  @QueryParam("codiceDescrittivo") String codiceDescrittivo,   @QueryParam("processInstanceId") String processInstanceId);

}

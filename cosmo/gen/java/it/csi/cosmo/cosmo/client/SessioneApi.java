/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.client;

import it.csi.cosmo.cosmo.dto.rest.*;

import it.csi.cosmo.cosmo.dto.rest.LoginRequest;
import it.csi.cosmo.cosmo.dto.rest.LogoutResponse;
import it.csi.cosmo.cosmo.dto.rest.UserInfo;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/sessione")  
public interface SessioneApi  {
   
    @DELETE   @Produces({ "application/json" })
    public LogoutResponse deleteSessione(  @QueryParam("ambienteLogout") String ambienteLogout);

    @GET   @Produces({ "application/json" })
    public UserInfo getSessione();

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public UserInfo postSessione( @Valid LoginRequest body);

}

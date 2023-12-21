/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.client;

import it.csi.cosmo.cosmobe.dto.rest.*;

import it.csi.cosmo.cosmobe.dto.rest.WhoAmI;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/whoami")  
public interface WhoamiApi  {
   
    @GET   @Produces({ "application/json" })
    public WhoAmI getWhoami();

}

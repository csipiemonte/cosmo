/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.client;

import it.csi.cosmo.cosmoauthorization.dto.rest.*;

import it.csi.cosmo.cosmoauthorization.dto.rest.AutorizzazioniFruitoreResponse;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/autorizzazioni-fruitore")  
public interface AutorizzazioniFruitoreApi  {
   
    @GET   @Produces({ "application/json" })
    public AutorizzazioniFruitoreResponse getAuthFruitore(  @QueryParam("filter") String filter);

}

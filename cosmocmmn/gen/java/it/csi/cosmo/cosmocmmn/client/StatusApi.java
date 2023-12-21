/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.client;

import it.csi.cosmo.cosmocmmn.dto.rest.*;

import it.csi.cosmo.cosmocmmn.dto.rest.Esito;
import it.csi.cosmo.cosmocmmn.dto.rest.ServiceStatus;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/status")  
public interface StatusApi  {
   
    @GET   @Produces({ "application/json" })
    public ServiceStatus getStatus();

}

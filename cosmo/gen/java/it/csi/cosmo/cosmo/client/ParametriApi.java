/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.client;

import it.csi.cosmo.cosmo.dto.rest.*;

import it.csi.cosmo.cosmo.dto.rest.Parametro;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/parametri")  
public interface ParametriApi  {
   
    @GET   @Produces({ "application/json" })
    public List<Parametro> getParametri();

}

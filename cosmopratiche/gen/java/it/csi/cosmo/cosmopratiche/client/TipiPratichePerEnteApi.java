/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.client;

import it.csi.cosmo.cosmopratiche.dto.rest.*;

import it.csi.cosmo.cosmopratiche.dto.rest.TipoPratica;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/tipi-pratiche-per-ente")  
public interface TipiPratichePerEnteApi  {
   
    @GET   @Produces({ "application/json" })
    public List<TipoPratica> getTipiPratichePerEnte(  @QueryParam("idEnte") Integer idEnte,   @QueryParam("creazionePratica") Boolean creazionePratica,   @QueryParam("conEnte") Boolean conEnte);

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest;

import it.csi.cosmo.cosmopratiche.dto.rest.*;


import it.csi.cosmo.cosmopratiche.dto.rest.TipoPratica;

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

@Path("/tipi-pratiche-per-ente")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface TipiPratichePerEnteApi  {
   
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getTipiPratichePerEnte( @QueryParam("idEnte") Integer idEnte, @QueryParam("creazionePratica") Boolean creazionePratica, @QueryParam("conEnte") Boolean conEnte,@Context SecurityContext securityContext);
}

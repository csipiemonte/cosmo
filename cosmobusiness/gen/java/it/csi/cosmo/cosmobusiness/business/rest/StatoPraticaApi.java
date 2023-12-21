/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest;

import it.csi.cosmo.cosmobusiness.dto.rest.*;


import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaStatoPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.Esito;

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

@Path("/stato-pratica")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface StatoPraticaApi  {
   
    @PUT
    @Path("/{idPratica}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response callbackPutStatoPratica( @PathParam("idPratica") String idPratica, AggiornaStatoPraticaRequest body,@Context SecurityContext securityContext);
}

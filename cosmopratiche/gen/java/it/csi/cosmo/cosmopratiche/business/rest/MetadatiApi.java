/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest;

import it.csi.cosmo.cosmopratiche.dto.rest.*;


import it.csi.cosmo.cosmopratiche.dto.rest.Esito;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;

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

@Path("/metadati")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface MetadatiApi  {
   
    @PUT
    @Path("/{idPratica}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putMetadatiIdPratica( @PathParam("idPratica") String idPratica,@Context SecurityContext securityContext);
    @PUT
    @Path("/{idPratica}/variabili-processo")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putMetadatiIdPraticaVariabiliProcesso( @PathParam("idPratica") String idPratica,@Context SecurityContext securityContext);
}

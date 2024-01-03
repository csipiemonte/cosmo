/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest;

import it.csi.cosmo.cosmopratiche.dto.rest.*;


import it.csi.cosmo.cosmopratiche.dto.rest.AttivitaEseguibileMassivamente;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoAttivita;

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

@Path("/esecuzione-multipla")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface EsecuzioneMultiplaApi  {
   
    @GET
    @Path("/task-disponibili")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getEsecuzioneMultiplaTaskDisponibili( @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    @Path("/task")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getEsecuzioneMultiplaTasks(@Context SecurityContext securityContext);
}

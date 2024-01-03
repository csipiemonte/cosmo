/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.client;

import it.csi.cosmo.cosmopratiche.dto.rest.*;

import it.csi.cosmo.cosmopratiche.dto.rest.Attivita;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/pratiche-attivita")  
public interface PraticheAttivitaApi  {
   
    @GET @Path("/{idAttivita}")  @Produces({ "application/json" })
    public Attivita getPraticheAttivitaIdAttivita( @PathParam("idAttivita") String idAttivita);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Pratica postPraticheAttivita( @Valid Pratica body);

    @PUT  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Pratica putPraticheAttivita( @Valid Pratica body);

    @PUT @Path("/{idAttivita}")  @Produces({ "application/json" })
    public Boolean putPraticheAttivitaIdAttivita( @PathParam("idAttivita") String idAttivita);

}

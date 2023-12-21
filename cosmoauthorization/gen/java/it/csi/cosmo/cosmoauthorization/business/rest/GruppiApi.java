/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.rest;

import it.csi.cosmo.cosmoauthorization.dto.rest.*;


import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaGruppoRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaGruppoRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.Esito;
import it.csi.cosmo.cosmoauthorization.dto.rest.GruppiResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.GruppoResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.RiferimentoGruppo;

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

@Path("/gruppi")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface GruppiApi  {
   
    @DELETE
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteGruppi( @PathParam("id") Long id,@Context SecurityContext securityContext);
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getGruppi( @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getGruppiId( @PathParam("id") Long id,@Context SecurityContext securityContext);
    @GET
    @Path("/utente-corrente")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getGruppiUtenteCorrente(@Context SecurityContext securityContext);
    @GET
    @Path("/codice/{codice}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getGruppoCodice( @PathParam("codice") String codice,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postGruppi( CreaGruppoRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putGruppi( @PathParam("id") Long id, AggiornaGruppoRequest body,@Context SecurityContext securityContext);
}

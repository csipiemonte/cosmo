/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.client;

import it.csi.cosmo.cosmoauthorization.dto.rest.*;

import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaEndpointFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaSchemaAutenticazioneFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaEndpointFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaSchemaAutenticazioneFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CredenzialiAutenticazioneFruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.EndpointFruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.Esito;
import it.csi.cosmo.cosmoauthorization.dto.rest.Fruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.FruitoriResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.SchemaAutenticazioneFruitore;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/fruitori")  
public interface FruitoriApi  {
   
    @DELETE @Path("/{idFruitore}/endpoints/{idEndpoint}")  
    public void deleteEndpointFruitore( @PathParam("idFruitore") Long idFruitore,  @PathParam("idEndpoint") Long idEndpoint);

    @DELETE @Path("/{id}")  @Produces({ "application/json" })
    public void deleteFruitoriId( @PathParam("id") Long id);

    @DELETE @Path("/{idFruitore}/schemi-autenticazione/{idSchemaAutenticazione}")  
    public void deleteSchemaAuthFruitore( @PathParam("idFruitore") Long idFruitore,  @PathParam("idSchemaAutenticazione") Long idSchemaAutenticazione);

    @GET   @Produces({ "application/json" })
    public FruitoriResponse getFruitori(  @QueryParam("filter") String filter);

    @GET @Path("/ami/{apiManagerId}")  @Produces({ "application/json" })
    public Fruitore getFruitoriAmiApiManagerId( @PathParam("apiManagerId") String apiManagerId);

    @GET @Path("/{id}")  @Produces({ "application/json" })
    public Fruitore getFruitoriId( @PathParam("id") Long id);

    @POST @Path("/{id}/endpoints") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public EndpointFruitore postEndpointsFruitori( @PathParam("id") Long id,  @Valid CreaEndpointFruitoreRequest body);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Fruitore postFruitori( @Valid CreaFruitoreRequest body);

    @POST @Path("/autentica") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Fruitore postFruitoriAutentica( @Valid CredenzialiAutenticazioneFruitore body);

    @POST @Path("/{id}/schemi-autenticazione") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public SchemaAutenticazioneFruitore postSchemiAuthFruitori( @PathParam("id") Long id,  @Valid CreaSchemaAutenticazioneFruitoreRequest body);

    @PUT @Path("/{idFruitore}/endpoints/{idEndpoint}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public SchemaAutenticazioneFruitore putEndpointFruitore( @PathParam("idFruitore") Long idFruitore,  @PathParam("idEndpoint") Long idEndpoint,  @Valid AggiornaEndpointFruitoreRequest body);

    @PUT @Path("/{id}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Fruitore putFruitori( @PathParam("id") Long id,  @Valid AggiornaFruitoreRequest body);

    @PUT @Path("/{idFruitore}/schemi-autenticazione/{idSchemaAutenticazione}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public SchemaAutenticazioneFruitore putSchemaAuthFruitore( @PathParam("idFruitore") Long idFruitore,  @PathParam("idSchemaAutenticazione") Long idSchemaAutenticazione,  @Valid AggiornaSchemaAutenticazioneFruitoreRequest body);

}

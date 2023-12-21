/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.rest;

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
import javax.ws.rs.core.HttpHeaders;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.validation.constraints.*;

@Path("/fruitori")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface FruitoriApi  {
   
    @DELETE
    @Path("/{idFruitore}/endpoints/{idEndpoint}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteEndpointFruitore( @PathParam("idFruitore") Long idFruitore, @PathParam("idEndpoint") Long idEndpoint,@Context SecurityContext securityContext);
    @DELETE
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteFruitoriId( @PathParam("id") Long id,@Context SecurityContext securityContext);
    @DELETE
    @Path("/{idFruitore}/schemi-autenticazione/{idSchemaAutenticazione}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteSchemaAuthFruitore( @PathParam("idFruitore") Long idFruitore, @PathParam("idSchemaAutenticazione") Long idSchemaAutenticazione,@Context SecurityContext securityContext);
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getFruitori( @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    @Path("/ami/{apiManagerId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getFruitoriAmiApiManagerId( @PathParam("apiManagerId") String apiManagerId,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getFruitoriId( @PathParam("id") Long id,@Context SecurityContext securityContext);
    @POST
    @Path("/{id}/endpoints")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postEndpointsFruitori( @PathParam("id") Long id, CreaEndpointFruitoreRequest body,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postFruitori( CreaFruitoreRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/autentica")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postFruitoriAutentica( CredenzialiAutenticazioneFruitore body,@Context SecurityContext securityContext);
    @POST
    @Path("/{id}/schemi-autenticazione")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postSchemiAuthFruitori( @PathParam("id") Long id, CreaSchemaAutenticazioneFruitoreRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{idFruitore}/endpoints/{idEndpoint}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putEndpointFruitore( @PathParam("idFruitore") Long idFruitore, @PathParam("idEndpoint") Long idEndpoint, AggiornaEndpointFruitoreRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putFruitori( @PathParam("id") Long id, AggiornaFruitoreRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{idFruitore}/schemi-autenticazione/{idSchemaAutenticazione}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putSchemaAuthFruitore( @PathParam("idFruitore") Long idFruitore, @PathParam("idSchemaAutenticazione") Long idSchemaAutenticazione, AggiornaSchemaAutenticazioneFruitoreRequest body,@Context SecurityContext securityContext);
}

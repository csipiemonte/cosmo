/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.rest;

import it.csi.cosmo.cosmoauthorization.dto.rest.*;


import it.csi.cosmo.cosmoauthorization.dto.rest.ApplicazioneEsterna;
import it.csi.cosmo.cosmoauthorization.dto.rest.ApplicazioneEsternaConValidita;
import it.csi.cosmo.cosmoauthorization.dto.rest.Esito;
import it.csi.cosmo.cosmoauthorization.dto.rest.FunzionalitaApplicazioneEsternaConValidita;
import java.util.List;

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

@Path("/applicazione-esterna")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface ApplicazioneEsternaApi  {
   
    @DELETE
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteApplicazioneEsternaId( @PathParam("id") String id,@Context SecurityContext securityContext);
    @DELETE
    @Path("/{idApplicazione}/funzionalita/{idFunzionalita}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteApplicazioneEsternaIdApplicazioneFunzionalitaIdFunzionalita( @PathParam("idApplicazione") String idApplicazione, @PathParam("idFunzionalita") String idFunzionalita,@Context SecurityContext securityContext);
    @DELETE
    @Path("/{id}/associazione-ente")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteApplicazioneEsternaIdAssociazioneEnte( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getApplicazioneEsterna( @NotNull @QueryParam("configurata") Boolean configurata,@Context SecurityContext securityContext);
    @GET
    @Path("/all")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getApplicazioneEsternaAll(@Context SecurityContext securityContext);
    @GET
    @Path("/associazione-ente")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getApplicazioneEsternaAssociazioneEnte( @NotNull @QueryParam("enteAssociato") Boolean enteAssociato,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getApplicazioneEsternaId( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    @Path("/{idApplicazione}/funzionalita")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getApplicazioneEsternaIdApplicazioneFunzionalita( @PathParam("idApplicazione") String idApplicazione,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}/associazione-ente")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getApplicazioneEsternaIdAssociazioneEnte( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    @Path("/{idApplicazione}/funzionalita/{idFunzionalita}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getApplicazioneEsternaIdFunzionalitaId( @PathParam("idApplicazione") String idApplicazione, @PathParam("idFunzionalita") String idFunzionalita,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postApplicazioneEsterna( ApplicazioneEsterna body,@Context SecurityContext securityContext);
    @POST
    @Path("/{idApplicazione}/funzionalita")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postApplicazioneEsternaIdFunzionalita( @PathParam("idApplicazione") String idApplicazione, FunzionalitaApplicazioneEsternaConValidita body,@Context SecurityContext securityContext);
    @PUT
    @Path("/associazione-utente")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putApplicazioneEsternaAssociazioneUtente( List<ApplicazioneEsterna> body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putApplicazioneEsternaId( @PathParam("id") String id, ApplicazioneEsterna body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{id}/associazione-ente")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putApplicazioneEsternaIdAssociazioneEnte( @PathParam("id") String id, ApplicazioneEsternaConValidita body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{idApplicazione}/funzionalita/{idFunzionalita}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putApplicazioneEsternaIdFunzionalitaId( @PathParam("idApplicazione") String idApplicazione, @PathParam("idFunzionalita") String idFunzionalita, FunzionalitaApplicazioneEsternaConValidita body,@Context SecurityContext securityContext);
}

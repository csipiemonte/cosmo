/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest;

import it.csi.cosmo.cosmobusiness.dto.rest.*;


import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaFormLogicoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaIstanzaFunzionalitaFormLogicoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaFormLogicoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaIstanzaFunzionalitaFormLogicoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.Esito;
import it.csi.cosmo.cosmobusiness.dto.rest.FormLogiciResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.FormLogico;
import it.csi.cosmo.cosmobusiness.dto.rest.IstanzaFunzionalitaFormLogico;
import it.csi.cosmo.cosmobusiness.dto.rest.IstanzeFormLogiciResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.TipologiaFunzionalitaFormLogico;
import it.csi.cosmo.cosmobusiness.dto.rest.TipologieParametroFormLogicoResponse;

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

@Path("/form-logici")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface FormLogiciApi  {
   
    @DELETE
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteFormLogici( @PathParam("id") Long id,@Context SecurityContext securityContext);
    @DELETE
    @Path("/istanze-funzionalita/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteFormLogiciIstanzeId( @PathParam("id") Long id,@Context SecurityContext securityContext);
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getFormLogici( @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    @Path("/istanze-funzionalita")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getFormLogiciIstanze( @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    @Path("/istanze-funzionalita/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getFormLogiciIstanzeId( @PathParam("id") Long id,@Context SecurityContext securityContext);
    @GET
    @Path("/tipologie-istanze-funzionalita")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getFormLogiciTipologieIstanzeFunzionalita(@Context SecurityContext securityContext);
    @GET
    @Path("/tipologie-istanze-funzionalita/{id}/parametri")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getFormLogiciTipologieIstanzeFunzionalitaParametri( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getFormLogico( @PathParam("id") Long id,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postFormLogici( CreaFormLogicoRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/istanze-funzionalita")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postFormLogiciIstanze( CreaIstanzaFunzionalitaFormLogicoRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putFormLogici( @PathParam("id") Long id, AggiornaFormLogicoRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/istanze-funzionalita/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putFormLogiciIstanzeId( @PathParam("id") Long id, AggiornaIstanzaFunzionalitaFormLogicoRequest body,@Context SecurityContext securityContext);
}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.client;

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
import it.csi.cosmo.cosmobusiness.dto.rest.TipologiaParametroFormLogico;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/form-logici")  
public interface FormLogiciApi  {
   
    @DELETE @Path("/{id}")  @Produces({ "application/json" })
    public FormLogico deleteFormLogici( @PathParam("id") Long id);

    @DELETE @Path("/istanze-funzionalita/{id}")  @Produces({ "application/json" })
    public void deleteFormLogiciIstanzeId( @PathParam("id") Long id);

    @GET   @Produces({ "application/json" })
    public FormLogiciResponse getFormLogici(  @QueryParam("filter") String filter);

    @GET @Path("/istanze-funzionalita")  @Produces({ "application/json" })
    public IstanzeFormLogiciResponse getFormLogiciIstanze(  @QueryParam("filter") String filter);

    @GET @Path("/istanze-funzionalita/{id}")  @Produces({ "application/json" })
    public IstanzaFunzionalitaFormLogico getFormLogiciIstanzeId( @PathParam("id") Long id);

    @GET @Path("/tipologie-istanze-funzionalita")  @Produces({ "application/json" })
    public List<TipologiaFunzionalitaFormLogico> getFormLogiciTipologieIstanzeFunzionalita();

    @GET @Path("/tipologie-istanze-funzionalita/{id}/parametri")  @Produces({ "application/json" })
    public List<List<TipologiaParametroFormLogico>> getFormLogiciTipologieIstanzeFunzionalitaParametri( @PathParam("id") String id);

    @GET @Path("/{id}")  @Produces({ "application/json" })
    public FormLogico getFormLogico( @PathParam("id") Long id);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public FormLogico postFormLogici( @Valid CreaFormLogicoRequest body);

    @POST @Path("/istanze-funzionalita") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public IstanzaFunzionalitaFormLogico postFormLogiciIstanze( @Valid CreaIstanzaFunzionalitaFormLogicoRequest body);

    @PUT @Path("/{id}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public FormLogico putFormLogici( @PathParam("id") Long id,  @Valid AggiornaFormLogicoRequest body);

    @PUT @Path("/istanze-funzionalita/{id}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public IstanzaFunzionalitaFormLogico putFormLogiciIstanzeId( @PathParam("id") Long id,  @Valid AggiornaIstanzaFunzionalitaFormLogicoRequest body);

}

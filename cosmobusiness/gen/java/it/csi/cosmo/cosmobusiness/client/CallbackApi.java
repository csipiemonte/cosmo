/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.client;

import it.csi.cosmo.cosmobusiness.dto.rest.*;

import it.csi.cosmo.cosmobusiness.dto.rest.Esito;
import it.csi.cosmo.cosmobusiness.dto.rest.EsitoSmistaDocumentoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.EsitoSmistaDocumentoResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaCallbackAggiornamentoDeliberaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaCallbackCreazioneDeliberaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaCallbackStatoPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InvioCallbackResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.SchedulaCallbackStatoPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.SchedulazioneCallbackResponse;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/callback")  
public interface CallbackApi  {
   
    @POST @Path("/aggiornamento-delibera/invia") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public InvioCallbackResponse postCallbackAggiornamentoDeliberaInvia( @Valid InviaCallbackAggiornamentoDeliberaRequest body);

    @POST @Path("/aggiornamento-delibera/schedula") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public SchedulazioneCallbackResponse postCallbackAggiornamentoDeliberaSchedula( @Valid InviaCallbackAggiornamentoDeliberaRequest body);

    @POST @Path("/creazione-delibera/invia") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public InvioCallbackResponse postCallbackCreazioneDeliberaInvia( @Valid InviaCallbackCreazioneDeliberaRequest body);

    @POST @Path("/creazione-delibera/schedula") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public SchedulazioneCallbackResponse postCallbackCreazioneDeliberaSchedula( @Valid InviaCallbackCreazioneDeliberaRequest body);

    @POST @Path("/esitoSmistaDocumento") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public EsitoSmistaDocumentoResponse postCallbackSmistaDocumento( @Valid EsitoSmistaDocumentoRequest body);

    @POST @Path("/stato-pratica/invia") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public InvioCallbackResponse postCallbackStatoPraticaInvia( @Valid InviaCallbackStatoPraticaRequest body);

    @POST @Path("/stato-pratica/schedula") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public SchedulazioneCallbackResponse postCallbackStatoPraticaSchedula( @Valid SchedulaCallbackStatoPraticaRequest body);

    @PUT @Path("/esitoSmistaDocumento") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public EsitoSmistaDocumentoResponse putCallbackSmistaDocumento( @Valid EsitoSmistaDocumentoRequest body);

}

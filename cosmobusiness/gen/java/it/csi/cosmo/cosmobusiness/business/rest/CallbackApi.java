/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest;

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
import javax.ws.rs.core.HttpHeaders;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.validation.constraints.*;

@Path("/callback")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface CallbackApi  {
   
    @POST
    @Path("/aggiornamento-delibera/invia")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postCallbackAggiornamentoDeliberaInvia( InviaCallbackAggiornamentoDeliberaRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/aggiornamento-delibera/schedula")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postCallbackAggiornamentoDeliberaSchedula( InviaCallbackAggiornamentoDeliberaRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/creazione-delibera/invia")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postCallbackCreazioneDeliberaInvia( InviaCallbackCreazioneDeliberaRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/creazione-delibera/schedula")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postCallbackCreazioneDeliberaSchedula( InviaCallbackCreazioneDeliberaRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/esitoSmistaDocumento")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postCallbackSmistaDocumento( EsitoSmistaDocumentoRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/stato-pratica/invia")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postCallbackStatoPraticaInvia( InviaCallbackStatoPraticaRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/stato-pratica/schedula")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postCallbackStatoPraticaSchedula( SchedulaCallbackStatoPraticaRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/esitoSmistaDocumento")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putCallbackSmistaDocumento( EsitoSmistaDocumentoRequest body,@Context SecurityContext securityContext);
}

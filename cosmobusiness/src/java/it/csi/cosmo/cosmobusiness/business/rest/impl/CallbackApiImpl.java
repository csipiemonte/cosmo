/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmobusiness.business.rest.CallbackApi;
import it.csi.cosmo.cosmobusiness.business.service.CallbackDelibereService;
import it.csi.cosmo.cosmobusiness.business.service.CallbackStatoPraticaService;
import it.csi.cosmo.cosmobusiness.business.service.StardasCallbackService;
import it.csi.cosmo.cosmobusiness.dto.rest.EsitoSmistaDocumentoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaCallbackAggiornamentoDeliberaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaCallbackCreazioneDeliberaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaCallbackStatoPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.SchedulaCallbackStatoPraticaRequest;

/**
 *
 */

public class CallbackApiImpl extends ParentApiImpl implements CallbackApi {

  @Autowired
  private StardasCallbackService stardasCallbackService;

  @Autowired
  private CallbackStatoPraticaService statoPraticaCallbackService;

  @Autowired
  private CallbackDelibereService callbackDelibereService;

  @Override
  public Response postCallbackSmistaDocumento(EsitoSmistaDocumentoRequest body,
      SecurityContext securityContext) {
    return Response.ok(stardasCallbackService.inserisciEsitoSmistaDocumento(body)).build();
  }

  @Override
  public Response putCallbackSmistaDocumento(EsitoSmistaDocumentoRequest body,
      SecurityContext securityContext) {
    return Response.ok(stardasCallbackService.aggiornaEsitoSmistaDocumento(body)).build();
  }

  @Override
  public Response postCallbackStatoPraticaInvia(InviaCallbackStatoPraticaRequest body,
      SecurityContext securityContext) {
    return Response.status(200)
        .entity(statoPraticaCallbackService.postCallbackStatoPraticaInvia(body)).build();
  }

  @Override
  public Response postCallbackStatoPraticaSchedula(SchedulaCallbackStatoPraticaRequest body,
      SecurityContext securityContext) {
    return Response.status(201)
        .entity(statoPraticaCallbackService.postCallbackStatoPraticaSchedula(body)).build();
  }

  @Override
  public Response postCallbackAggiornamentoDeliberaInvia(
      InviaCallbackAggiornamentoDeliberaRequest body, SecurityContext securityContext) {

    return Response.status(200)
        .entity(callbackDelibereService.postCallbackAggiornamentoDeliberaInvia(body)).build();
  }

  @Override
  public Response postCallbackAggiornamentoDeliberaSchedula(
      InviaCallbackAggiornamentoDeliberaRequest body, SecurityContext securityContext) {

    return Response.status(201)
        .entity(callbackDelibereService.postCallbackAggiornamentoDeliberaSchedula(body)).build();
  }

  @Override
  public Response postCallbackCreazioneDeliberaInvia(InviaCallbackCreazioneDeliberaRequest body,
      SecurityContext securityContext) {

    return Response.status(200)
        .entity(callbackDelibereService.postCallbackCreazioneDeliberaInvia(body)).build();
  }

  @Override
  public Response postCallbackCreazioneDeliberaSchedula(InviaCallbackCreazioneDeliberaRequest body,
      SecurityContext securityContext) {

    return Response.status(201)
        .entity(callbackDelibereService.postCallbackCreazioneDeliberaSchedula(body)).build();
  }

}

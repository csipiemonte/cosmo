/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import it.csi.cosmo.cosmoauthorization.business.rest.UtentiApi;
import it.csi.cosmo.cosmoauthorization.business.service.UtentiService;
import it.csi.cosmo.cosmoauthorization.dto.rest.UtenteCampiTecnici;
import it.csi.cosmo.cosmoauthorization.dto.rest.UtenteResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.UtentiResponse;

/**
 *
 */

public class UtentiApiImpl extends ParentApiImpl implements UtentiApi {

  @Autowired
  private UtentiService utentiService;

  @Override
  public Response deleteUtentiId(String id, SecurityContext securityContext) {
    UtenteResponse utenteResponse = new UtenteResponse();
    utenteResponse.setUtente(utentiService.deleteUtente(id));
    if (utenteResponse.getUtente() != null) {
      return Response.ok(utenteResponse).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Override
  public Response getUtenti(String filter, SecurityContext securityContext) {

    UtentiResponse utentiResponse = utentiService.getUtenti(filter);
    return Response.ok(utentiResponse).build();
  }

  @Override
  public Response getUtentiId(String id, SecurityContext securityContext) {
    UtenteResponse utenteResponse = new UtenteResponse();
    utenteResponse.setUtente(utentiService.getUtenteDaId(id));
    if (utenteResponse.getUtente() != null) {
      return Response.ok(utenteResponse).build();
    } else {
      return Response.noContent().build();
    }
  }


  @Override
  public Response getUtentiCodiceFiscale(String codiceFiscale, SecurityContext securityContext) {
    UtenteResponse utenteResponse = new UtenteResponse();
    utenteResponse.setUtente(utentiService.getUtenteDaCodiceFiscale(codiceFiscale));
    if (utenteResponse.getUtente() != null) {
      return Response.ok(utenteResponse).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Override
  public Response getUtentiUtenteCorrente(SecurityContext securityContext) {
    UtenteResponse utenteResponse = new UtenteResponse();
    utenteResponse.setUtente(utentiService.getUtenteCorrente());
    if (utenteResponse.getUtente() != null) {
      return Response.ok(utenteResponse).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Override
  public Response postUtenti(UtenteCampiTecnici utente, SecurityContext securityContext) {
    UtenteResponse utenteResponse = new UtenteResponse();
    utenteResponse.setUtente(utentiService.createUtente(utente));
    if (utenteResponse.getUtente() != null) {
      return Response.ok(utenteResponse).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Override
  public Response putUtenti(UtenteCampiTecnici utente, SecurityContext securityContext) {
    UtenteResponse utenteResponse = new UtenteResponse();
    utenteResponse.setUtente(utentiService.updateUtente(utente));
    if (utenteResponse.getUtente() != null) {
      return Response.ok(utenteResponse).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Override
  public Response getUtentiEnte(String filter, SecurityContext securityContext) {
    UtentiResponse utentiResponse = utentiService.getUtentiEnte(filter);
    if (CollectionUtils.isEmpty(utentiResponse.getUtenti())) {
      return Response.noContent().build();
    } else {
      return Response.ok(utentiResponse).build();
    }
  }


  @Override
  public Response getUtentiEntiValiditaId(String idUtente, String idEnte,
      SecurityContext securityContext) {
    UtenteCampiTecnici utente = utentiService.getUtenteConValidita(idUtente, idEnte);
    if (utente == null) {
      return Response.noContent().build();
    } else {
      return Response.ok(utente).build();
    }
  }


}

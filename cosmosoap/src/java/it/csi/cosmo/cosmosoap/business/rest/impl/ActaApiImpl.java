/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmosoap.business.rest.ActaApi;
import it.csi.cosmo.cosmosoap.business.service.ActaService;
import it.csi.cosmo.cosmosoap.dto.rest.Classificazioni;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiFisiciMap;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiFisiciResponse;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiSempliciMap;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiSempliciResponse;
import it.csi.cosmo.cosmosoap.dto.rest.IdentitaActaResponse;
import it.csi.cosmo.cosmosoap.dto.rest.ImportaDocumentiRequest;

/**
 *
 */

public class ActaApiImpl extends ParentApiImpl implements ActaApi {

  @Autowired
  private ActaService actaService;

  @Override
  public Response getClassificazioniIdDocumentoSemplice(String idDocumentoSemplice,
      String idIdentita, SecurityContext securityContext) {

    Classificazioni classificazioni = new Classificazioni();
    classificazioni.setClassificazioni(
        actaService.findClassificazioniByIdDocumentoSemplice(idIdentita, idDocumentoSemplice));
    return Response.ok(classificazioni).build();
  }

  @Override
  public Response getDocumentiFisiciByidDocumentoSemplice(String idDocumentoSemplice,
      String idIdentita, SecurityContext securityContext) {
    DocumentiFisiciResponse response = new DocumentiFisiciResponse();
    response.setDocumentiFisici(
        actaService.findDocumentiFisiciByIdDocumentoSemplice(idIdentita, idDocumentoSemplice));
    return Response.ok(response).build();
  }

  @Override
  public Response getDocumentiFisiciMap(String idIdentita, ImportaDocumentiRequest body,
      SecurityContext securityContext) {
    DocumentiFisiciMap response = new DocumentiFisiciMap();
    response.setDocumentiFisiciMap(actaService.findDocumentiFisici(idIdentita, body));
    return Response.ok(response).build();
  }

  @Override
  public Response getDocumentiSemplici(String idIdentita, String parolaChiave,
      String idClassificazione, SecurityContext securityContext) {
    if (StringUtils.isNotBlank(parolaChiave)) {
      DocumentiSempliciResponse response = new DocumentiSempliciResponse();
      response.setDocumentiSemplici(
          actaService.findDocumentiSempliciByParolaChiave(idIdentita, parolaChiave));
      return Response.ok(response).build();

    } else if (StringUtils.isNotBlank(idClassificazione)) {
      DocumentiSempliciResponse response = new DocumentiSempliciResponse();
      response.setDocumentiSemplici(
          actaService.findDocumentiSempliciByIdClassificazione(idIdentita, idClassificazione));
      return Response.ok(response).build();
    }

    return Response.ok().build();
  }


  @Override
  public Response getDocumentiSempliciMap(String idIdentita, ImportaDocumentiRequest body,
      SecurityContext securityContext) {
    DocumentiSempliciMap response = new DocumentiSempliciMap();
    response.setDocumentiSempliciMap(actaService.findDocumentiSemplici(idIdentita, body));
    return Response.ok(response).build();
  }


  @Override
  public Response getDocumentiSempliciPageable(String filter, String idIdentita,
      Boolean perProtocollo, SecurityContext securityContext) {

    if (Boolean.TRUE.equals(perProtocollo)) {
      return Response.ok(actaService.findDocumentiSempliciPerProtocollo(idIdentita, filter))
          .build();
    } else {
      return Response.ok(actaService.findDocumentiSemplici(idIdentita, filter)).build();
    }

  }

  @Override
  public Response getProtocolloId(String id, String idIdentita, SecurityContext securityContext) {
    return Response.ok(actaService.findProtocolloById(idIdentita, id)).build();
  }

  @Override
  public Response getRegistrazioni(String idIdentita, String filter,
      SecurityContext securityContext) {
    return Response.ok(actaService.findRegistrazioni(idIdentita, filter)).build();
  }

  @Override
  public Response getContenutoPrimarioId(String id, String idIdentita,
      SecurityContext securityContext) {
    return Response.ok(actaService.findContenutoPrimario(idIdentita, id)).build();
  }

  @Override
  public Response getIdentitaDisponibili(SecurityContext securityContext) {
    IdentitaActaResponse response = new IdentitaActaResponse();
    response.setIdentitaActa(actaService.findIdentitaDisponibili());
    return Response.ok(response).build();
  }

  @Override
  public Response getRicercaPerIndiceClassificazioneEstesa(String identita,
      String indiceClassificazioneEsteso, SecurityContext securityContext) {
    return Response.ok(actaService.findObjectIdStrutturaAggregativa(identita, indiceClassificazioneEsteso)).build();
  }

  @Override
  public Response getRicercaTitolario(String codice,
      String idIdentita,
      SecurityContext securityContext) {
    return Response.ok(actaService.getTitolario(idIdentita, codice)).build();
  }

  @Override
  public Response ricercaAlberaturaVociPageable(String idIdentita, String chiaveTitolario,
      String filter, String chiavePadre, SecurityContext securityContext) {
    return Response.ok(actaService.ricercaAlberaturaVociTitolario(idIdentita, chiaveTitolario, chiavePadre, filter)).build();
  }

}

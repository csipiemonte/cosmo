/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.rest.impl;

import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.cosmoecm.business.rest.ActaApi;
import it.csi.cosmo.cosmoecm.business.service.ActaService;
import it.csi.cosmo.cosmoecm.dto.rest.DocumentiFisiciActaResponse;
import it.csi.cosmo.cosmoecm.dto.rest.DocumentoFisicoActa;
import it.csi.cosmo.cosmoecm.dto.rest.ImportaDocumentiActaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmoecm.security.UseCase;

public class ActaApiImpl extends ParentApiImpl implements ActaApi {

  @Autowired
  private ActaService actaService;

  @Override
  public Response getIdentitaUtente(SecurityContext securityContext) {

    var result = actaService.findIdentitaDisponibili();
    return Response.ok(result).build();
  }

  @Secured(UseCase.UC_RICERCA_ACTA)
  @Override
  public Response getDocumentiActa(String identita, String filter,
      SecurityContext securityContext) {
    RiferimentoOperazioneAsincrona response =
        actaService.findDocumentiSemplici(identita, filter);

    return Response.ok(response).build();
  }

  @Secured(UseCase.UC_RICERCA_ACTA)
  @Override
  public Response getDocumentiFisiciActa(String identita, String id,
      SecurityContext securityContext) {

    try {

      List<DocumentoFisicoActa> mappedDocFisici;
      mappedDocFisici = actaService.findDocumentiFisiciByIdDocumentoSemplice(identita, id);

      var response = new DocumentiFisiciActaResponse();
      mappedDocFisici.forEach(response::add);

      return Response.ok(response).build();
    } catch (Exception e) {
      throw ExceptionUtils.toChecked(e);
    }

  }

  @Secured(UseCase.UC_RICERCA_ACTA)
  @Override
  public Response importaDocumentiActa(String identita, ImportaDocumentiActaRequest body,
      SecurityContext securityContext) {

    RiferimentoOperazioneAsincrona response;

    try {
      response = actaService.importaDocumentiActa(identita, body);
    } catch (Exception e) {
      throw ExceptionUtils.toChecked(e);
    }

    return Response.status(201).entity(response).build();
  }

  @Secured(UseCase.UC_RICERCA_ACTA)
  @Override
  public Response getActaDocumentiRicercaConIndiceClassificazioneEstesoAggregazione(String identita,
      String indiceClassificazioneEsteso, SecurityContext securityContext) {
    String objectId = actaService.findIdByIndiceClassificazioneEstesaAggregazione(identita, indiceClassificazioneEsteso);
    return Response.ok(objectId).build();
  }

  @Secured(UseCase.UC_RICERCA_ACTA)
  @Override
  public Response getRicercaAlberaturaVociPageable(String idIdentita, String chiaveTitolario,
      String filter, String chiavePadre, SecurityContext securityContext) {
    return Response.ok(actaService.ricercaAlberaturaVociTitolario(idIdentita, chiaveTitolario, chiavePadre, filter)).build();
  }

  @Secured(UseCase.UC_RICERCA_ACTA)
  @Override
  public Response getRicercaTitolario(String codice, String idIdentita,
      SecurityContext securityContext) {
    return Response.ok(actaService.getTitolarioActa(idIdentita, codice)).build();
  }

}

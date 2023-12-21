/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.testbed.rest;

import java.util.List;
import org.springframework.stereotype.Component;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoEcmDocumentiFeignClient;
import it.csi.cosmo.cosmoecm.dto.rest.AggiornaDocumentoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.ContenutiDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentiRequest;
import it.csi.cosmo.cosmoecm.dto.rest.Documenti;
import it.csi.cosmo.cosmoecm.dto.rest.DocumentiResponse;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoInvioStiloRequest;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoSmistaDocumentoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoSmistamentoResponse;
import it.csi.cosmo.cosmoecm.dto.rest.PreparaEsposizioneDocumentiRequest;
import it.csi.cosmo.cosmoecm.dto.rest.PreparaEsposizioneDocumentiResponse;
import it.csi.cosmo.cosmoecm.dto.rest.RelazioniDocumentoDuplicato;
import it.csi.cosmo.cosmoecm.dto.rest.RichiediSmistamentoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmoecm.dto.rest.VerificaTipologiaDocumentiSalvati;

/**
 *
 */


@Component
public class CosmoEcmDocumentiTestClient extends ParentTestClient
implements CosmoEcmDocumentiFeignClient {

  @Override
  public void deleteDocumentoId(Integer arg0) {
    notMocked();

  }

  @Override
  public ContenutiDocumento getContenuti(Integer arg0) {
    notMocked();
    return null;
  }

  @Override
  public void getContenutoId(Integer arg0, Integer arg1, Boolean arg2) {
    notMocked();

  }

  @Override
  public Documento getDocumentoId(Integer arg0) { // NOSONAR
    notMocked();
    return null;
  }

  @Override
  public EsitoSmistamentoResponse postDocumentiIdDocumentoSmistamento(String arg0,
      EsitoSmistaDocumentoRequest arg1) {
    notMocked();
    return null;
  }

  @Override
  public void postDocumentiIdPraticaRichiediSmistamento(String arg0,
      RichiediSmistamentoRequest arg1) {
    notMocked();

  }

  @Override
  public Documenti postDocumento(Long arg0, CreaDocumentiRequest arg1) {
    notMocked();
    return null;
  }

  @Override
  public EsitoSmistamentoResponse putDocumentiIdDocumentoSmistamento(String arg0, // NOSONAR
      EsitoSmistaDocumentoRequest arg1) {
    notMocked();
    return null;
  }

  @Override
  public Documento putDocumentoId(Integer arg0, AggiornaDocumentoRequest arg1) {
    notMocked();
    return null;
  }

  @Override
  public void deleteContenutoDocumentoId(Integer arg0, Integer arg1) {
    notMocked();

  }

  @Override
  public void getContenutoIdSigned(String arg0, String arg1, String arg2) {
    // nothing to do

  }

  @Override
  public RiferimentoOperazioneAsincrona duplicaDocumenti(Integer arg0, Integer arg1) {
    notMocked();
    return null;
  }

  @Override
  public RelazioniDocumentoDuplicato duplicaDocumento(Integer arg0, Integer arg1,
      RelazioniDocumentoDuplicato arg2) {
    notMocked();
    return null;
  }

  @Override
  public Documenti preparaDuplicazione(Integer arg0, Integer arg1, Boolean arg2) {
    notMocked();
    return null;
  }

  @Override
  public List<Documento> getDocumentiInvioStilo(String arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DocumentiResponse getDocumento(String arg0, Boolean arg1) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void getIndexContent(Integer arg0, Integer arg1) {
    // TODO Auto-generated method stub

  }

  @Override
  public List<VerificaTipologiaDocumentiSalvati> getTipologieDocumentiSalvati(String arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void postDocumentiIdDocumentoEsitoInvioStilo(String arg0, EsitoInvioStiloRequest arg1) {
    // TODO Auto-generated method stub

  }

  @Override
  public void postDocumentiIdDocumentoInvioDocumentoStilo(String arg0, Long arg1) {
    // TODO Auto-generated method stub

  }

  @Override
  public PreparaEsposizioneDocumentiResponse postDocumentiIdPraticaEsposizione(Long arg0,
      PreparaEsposizioneDocumentiRequest arg1) {
    // TODO Auto-generated method stub
    return null;
  }

}

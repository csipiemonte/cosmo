/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import java.util.List;
import java.util.Set;
import it.csi.cosmo.common.entities.CosmoRSigilloDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.cosmoecm.dto.EsitoRichiestaSigilloElettronicoDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.CreaCredenzialiSigilloElettronicoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CredenzialiSigilloElettronico;
import it.csi.cosmo.cosmoecm.dto.rest.CredenzialiSigilloElettronicoResponse;
import it.csi.cosmo.cosmoecm.dto.rest.RichiediApposizioneSigilloRequest;


public interface SigilloElettronicoService {

  void deleteSigilloElettronico(Integer id);

  CredenzialiSigilloElettronico getSigilloElettronicoId(Integer id);

  CredenzialiSigilloElettronico updateSigilloElettronico(Integer id,
      CreaCredenzialiSigilloElettronicoRequest body);

  CredenzialiSigilloElettronico creaSigilloElettronico(
      CreaCredenzialiSigilloElettronicoRequest body);

  CredenzialiSigilloElettronicoResponse getSigilloElettronico(String filter);

  /**
   * Impostazione dello stato di smistamento work in progress  sui documenti afferenti a una pratica. Verranno
   * elaborati dal batch ApposizioneSigilloBatch alla prima esecuzione
   *
   * @param idPratica l'id della pratica sulla quale sono stati caricati i documenti da sigillare
   * @param body informazioni relative alla richiesta di apposizione sigillo
   */
  void richiediApposizioneSigillo(String idPratica, RichiediApposizioneSigilloRequest body);

  public List<CosmoTDocumento> recuperaDocumentiDaSigillare();

  /**
   * Salvataggio dell'esito di richiesta apposizione sigillo elettronico
   *
   * @param esitoRichiestaApposizioneSigillo l'esito di richiesta apposizione sigillo
   * @param documento il documento che e' stato mandato a DOSIGN
   */
  void salvaEsitoRichiestaApposizioneSigillo(EsitoRichiestaSigilloElettronicoDocumento esitoRichiestaSigillo,
      CosmoTDocumento documento, Long idSigillo);

  Long recuperaIdEnteDaDocumento(CosmoTDocumento documento);

  Set<Long> recuperaIdPraticaDaDocumento(CosmoTDocumento documento);

  /**
   * Inserimento di un nuovo esito sigillo documenti
   *
   * @param request
   * @return un oggetto contenente codice - messaggio, risultato dell'inserimento
   */
  //EsitoSmistaDocumentoResponse inserisciEsitoSigilloElettronico(EsitoSmistaDocumentoRequest request);

  boolean checkApposizioneSigilloDocumenti(List<CosmoTDocumento> documenti, Long idSigillo);

  void aggiornaStatoSigilloWip(CosmoRSigilloDocumento sigilloDocumento);

  void aggiornaSigilliInErrore(Long idPratica, String identificativoEvento, String identificativoAlias);

}

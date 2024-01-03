/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import java.util.List;
import java.util.Set;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoSmistaDocumentoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoSmistamentoResponse;
import it.csi.cosmo.cosmoecm.dto.rest.RichiediSmistamentoRequest;
import it.csi.cosmo.cosmoecm.dto.stardas.EsitoRichiestaSmistamentoDocumento;

/**
 *
 */

public interface StardasService {

  /**
   * Invocazione della operation SOAP di smistamento
   *
   * @param documento il documento da smistare
   * @return
   */
  public EsitoRichiestaSmistamentoDocumento smistaDocumento(CosmoTDocumento documento);

  public List<CosmoTDocumento> recuperaDocumentiDaSmistare(Long idPratica, int principaliOAllegati);

  /**
   * Impostazione dello stato di smistamento sui documenti afferenti a una pratica. Verranno
   * elaborati dal batch ProtocollazioneStardasBatch alla prima esecuzione
   *
   * @param idPratica l'id della pratica sulla quale sono stati caricati i documenti da smistare
   * @param body informazioni relative alla richiesta di smistamento
   */
  void richiediSmistamento(String idPratica, RichiediSmistamentoRequest body);

  /**
   * Impostazione dell'esito di uno smistamento
   *
   * @param idDocumento id del documento smistato
   * @param request informazioni relative all'esito di smistamento
   * @param primoSmistamento indica se e' il primo smistamento relativo al documento in input o meno
   */
  void impostaEsitoSmistamento(Long idDocumento, EsitoSmistaDocumentoRequest request,
      boolean primoSmistamento);

  /**
   *
   * @param id del documento
   * @return un oggetto che, oltre all'esito dell'operazione, contiene informazioni relative ai
   *         documenti smistati correttamente / smistati con errore / e da smistare (totali) per
   *         l'evento di smistamento in oggetto
   */
  EsitoSmistamentoResponse creaEsitoSmistamento(Long idDocumento,
      String messageUUID);

  /**
   * Salvataggio dell'esito di richiesta smistamento
   *
   * @param esitoRichiestaSmistamento l'esito di richiesta smistamento restituita dall'operation
   *        soap di STARDAS
   * @param documento il documento che e' stato mandato a STARDAS
   */
  void salvaEsitoRichiestaSmistamento(EsitoRichiestaSmistamentoDocumento esitoRichiestaSmistamento,
      CosmoTDocumento documento);

  Long recuperaIdEnteDaDocumento(CosmoTDocumento documento);

  Set<Long> recuperaIdPraticaDaDocumento(CosmoTDocumento documento);

  void aggiornaDaSmistare(CosmoTDocumento documento);

  boolean checkEsitoSmistamentoDocPadre(CosmoTDocumento documento);
}

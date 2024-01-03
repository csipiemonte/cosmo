/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import it.csi.cosmo.common.entities.CosmoTEndpointFruitore;
import it.csi.cosmo.common.entities.enums.OperazioneFruitore;
import it.csi.cosmo.cosmoecm.dto.ContenutoDocumentoResult;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentiFruitoreRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentiLinkFruitoreRequest;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoCreazioneDocumentiFruitore;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoCreazioneDocumentiLinkFruitore;

/**
 *
 */

public interface FruitoriService {

  /**
   * Metodo che inserisce una lista di documenti sul database
   *
   * @param idPratica e'
   * @param documento e' una lista di documenti contenenti le informazioni dei documenti da salvare
   * @return i documenti salvati
   */
  EsitoCreazioneDocumentiFruitore inserisciDocumenti(CreaDocumentiFruitoreRequest documento);

  /**
   * Metodo che restituisce il contenuto di un documento al fruitore
   *
   * @param idDocumentoExt e' l'identificativo del documento dal quale estrarne il contenuto
   * @return ContenutoDocumentoResult: contiene un link per il download diretto se previsto, il
   *         contenuto fisico altrimenti
   */
  ContenutoDocumentoResult getContenutoFruitore(String idDocumentoExt);

  /**
   * Simile a getContenutoFruitore ma autentica utilizzando il digest fornito
   */
  ContenutoDocumentoResult getContenutoFruitoreSigned(String idDocumentoExt,
      String idChiavePubblica, String digest);

  /**
   * Metodo che inserisce una lista di documenti sul database, a partire da una lista di link (url)
   *
   * @param idPratica e'
   * @param documenti e' una lista di documenti contenenti le informazioni dei documenti da salvare
   * @return i documenti salvati
   */
  EsitoCreazioneDocumentiLinkFruitore inserisciDocumentiLink(CreaDocumentiLinkFruitoreRequest documenti);

  CosmoTEndpointFruitore getEndpoint(OperazioneFruitore operazione, Long idFruitore,
      String codiceDescrittivo);
}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import java.io.InputStream;
import java.util.List;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.cosmoecm.dto.rest.AggiornaDocumentoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentiRequest;
import it.csi.cosmo.cosmoecm.dto.rest.Documenti;
import it.csi.cosmo.cosmoecm.dto.rest.DocumentiResponse;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;
import it.csi.cosmo.cosmoecm.dto.rest.PreparaEsposizioneDocumentiRequest;
import it.csi.cosmo.cosmoecm.dto.rest.PreparaEsposizioneDocumentiResponse;
import it.csi.cosmo.cosmoecm.dto.rest.VerificaTipologiaDocumentiSalvati;

/**
 *
 */

public interface DocumentoService {

  /**
   * Metodo che restituisce il documento di cui si passa l'id
   *
   * @param id del documento di cui si vuole informazioni
   * @return il documento con l'id in input
   */
  Documento getDocumento(Integer id);

  /**
   * Metodo che restituisce i documenti paginati
   *
   * @param filter sono i fitri che selezionano i documenti
   * @return una lista di documenti paginati
   */
  DocumentiResponse getDocumenti(String filter, Boolean export);

  /**
   * Metodo che restituisce i documenti da firmare dall'utente
   *
   * @param filter contiene i filtri con i quali cercare i documenti che devono essere firmati
   * @param count indica se bisogna restituire solo il numero o anche la lista dei doc da firmare
   * @return una lista di documenti da firmare
   */
  /**
   *
   * @param filter contiene i filtri con i quali cercare i documenti da firmare
   * @param filter contiene i filtri obbligatori che devono essere usati per cercare i documenti
   *        firmati
   * @param tutti se i documenti devono essere restituiti tutti o paginati
   * @return
   */
  DocumentiResponse getDocumentiDaFirmare(String filter, String filterDaFirmare, Boolean export);

  /**
   * Metodo che modifica un documento su database
   *
   * @param body e' un oggetto con le informazioni del documento
   * @return il documento aggiornato
   */
  Documento modificaDocumento(AggiornaDocumentoRequest body, Integer id);

  /**
   * Metodo che cancella fisicamente il documento dal filesystem o da index, a seconda di dove e'
   * collocato, e successivamente fa la cancellazione logica sul database, settando data e utente di
   * cancellazione del documento
   *
   * @param id del documento da identificare come cancellato
   * @return l'oggetto cancellato
   */
  Documento cancellaDocumento(Integer id);

  /**
   * Metodo che cancella il documento da Index tramite l'uuid
   *
   * @param uuid del documento in Index
   */
  void cancellaDocumentoIndex(String uuid);

  /**
   * Metodo per ottenere un file presente sul file system
   *
   * @param fileUUID e' l'uuid che identifica il file sul file system
   * @return RetrivedFile: oggetto contenente info relative al file ricercato sul file system
   */
  RetrievedContent get(String fileUUID);

  /**
   * Metodo per cancellare un file presente sul file system
   *
   * @param file e' un oggetto contenente le info relative al file che si vuole eliminare dal file
   *        system
   * @return
   */

  void delete(RetrievedContent file);

  /**
   * Metodo che inserisce una lista di documenti sul database
   *
   * @param idPratica e'
   * @param documento e' una lista di documenti contenenti le informazioni dei documenti da salvare
   * @return i documenti salvati
   */
  Documenti inserisciDocumenti(Long idPratica, CreaDocumentiRequest documento);

  /**
   * @param idPratica
   * @param titolo
   * @param filename
   * @param mimeType
   * @param content
   * @return
   */
  CosmoTDocumento creaDocumentoProgrammaticamente(Long idPratica, String codiceTipo, String titolo,
      String autore, String filename, String mimeType, InputStream content);

  /**
   * @param id
   * @return
   */
  CosmoTDocumento cancellaDocumentoLogicamente(Integer id);

  /**
   * @param idPratica
   * @param body
   * @return
   */
  PreparaEsposizioneDocumentiResponse preparaEsposizioneDocumenti(Long idPratica,
      PreparaEsposizioneDocumentiRequest body);

  /**
   * Restituisce l'elenco delle tipologie dei documenti passate in input, indicando se sono presenti
   * o meno tra i documenti salvati
   *
   * @param body contiene le tipologie di cui si vuole sapere se presenti e il numero di pratica da
   *        cui trarre questa informazione
   * @return una lista con le tipologie passate in input e un booleano true o false che indica se
   *         sono presenti documenti con quella tipologia o meno
   */
  List<VerificaTipologiaDocumentiSalvati> getTipologieDocumentiSalvati(String body);

}

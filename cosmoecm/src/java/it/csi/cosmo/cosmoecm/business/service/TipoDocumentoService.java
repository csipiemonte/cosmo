/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import java.util.List;
import it.csi.cosmo.cosmoecm.dto.rest.TipoDocumento;

/**
 *
 */

public interface TipoDocumentoService {

  /**
   * Metodo che restituisce tutte le possibili tipologie di documento collegate ad una pratica
   *
   * @param idPratica e' la pratica di cui si vogliono le tipologie di documento
   * @param codiceTipoDocPadre se presente contiene il codice tipo documento del documento padre:
   *        usato per filtrare i tipi documento ammissibili per gli allegati
   * @return una lista di tipologie di documenti
   */
  List<TipoDocumento> getTipiDocumenti(String idPratica, String codiceTipoDocPadre);

  /**
   * Metodo che restituisce tutte le possibili tipologie di documento collegate ad una pratica
   *
   * @param codici e' una lista di codici tipi documento
   *
   * @return una lista di tipologie di documenti
   */
  List<TipoDocumento> getTipiDocumento(List<String> codici);

  /**
   * Metodo che restituisce tutte le possibili tipologie di documento collegate ad una pratica
   *
   * @param codici e' una lista di codici tipi documento
   * @param addFormatoFile se aggiungere o meno il formato dei file
   * @param codicePadre e' il codice del padre, nel caso fossero allegati
   *
   * @return una lista di tipologie di documenti
   */
  List<TipoDocumento> getTipiDocumento(List<String> codici, Boolean addFormatoFile,
      String codicePadre, String codiceTipoPratica);

  /**
   * Metodo che restituisce tutte le possibili tipologie di documento a partire dal tipo pratica
   *
   * @param tipoPratica: il tipo di pratica dal quale ottenere tutti i tipi di documento ad esso
   *        associati
   * @return una lista di tipologie di documenti
   */
  List<TipoDocumento> getTipiDocumentiAll(String tipoPratica);
}

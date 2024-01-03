/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import java.util.List;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoInvioStiloRequest;

/**
 *
 */

public interface StiloService {
  /**
   * Restituisce l'elenco dei documenti da inviare a stilo
   *
   * @param filter
   * @return una lista di documenti
   */
  List<Documento> getDocumentiInvioStilo(String filter);

  /**
   * Metodo che imposta lo stato invio Stilo pari a INVIATO o ERR_INVIO su database, il codice esito
   * invio e il messaggio esito invio. Nel caso in cui l'elemento indicato da idDocumento e
   * body.idUd non esiste sul database, viene creato un nuovo record.
   *
   * @param idDocumento e' l'id del documento
   * @param body contiene le informazioni da salvare
   * @return
   */
  void impostaEsitoInvioStilo(String idDocumento, EsitoInvioStiloRequest body);

  /**
   * Metodo che imposta lo stato invio Stilo pari a IN_FASE_DI_INVIO su database. Nel caso in cui
   * l'elemento indicato da idDocumento e idUd non esiste sul database, viene creato un nuovo
   * record.
   *
   *
   * @param idDocumento e' l'id del documento
   * @param idUd è l'id riferito a Stilo
   * @return
   */
  void impostaInvioDocumentoStilo(String idDocumento, Long idUd);

}






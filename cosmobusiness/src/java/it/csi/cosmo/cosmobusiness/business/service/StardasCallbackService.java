/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service;

import it.csi.cosmo.cosmobusiness.dto.rest.EsitoSmistaDocumentoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.EsitoSmistaDocumentoResponse;

/**
 *
 */

public interface StardasCallbackService {

  /**
   * Inserimento di un nuovo esito di smistamento documento
   * 
   * @param request i dati della callback
   * @return un oggetto contenente codice - messaggio, risultato dell'inserimento
   */
  EsitoSmistaDocumentoResponse inserisciEsitoSmistaDocumento(EsitoSmistaDocumentoRequest request);

  /**
   * Aggiornamento di un esito di smistamento documento
   * 
   * @param request i dati della callback
   * @return un oggetto contenente codice - messaggio, risultato dell'aggiornamento
   */
  EsitoSmistaDocumentoResponse aggiornaEsitoSmistaDocumento(EsitoSmistaDocumentoRequest request);

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service;

import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;

/**
 *
 */

public interface MetadatiService {

  /**
   * Metodo per aggiornare la colonna metadati della pratica di cui si passa l'id, in base ai valori
   * salvati nella tabella configurazione_metadati, a seconda del tipo di pratica, e alle variabili
   * di processo restituite da flowable per questa data pratica
   *
   * @param idPratica e' l'id della pratica da aggiornare
   * @return la pratica con la colonna metadati aggiornata
   */
  Pratica aggiornaMetadatiPratica(String idPratica);

  /**
   * Se la pratica di cui viene passato l'id, ha la colonna metadati valorizzata, si chiama il
   * servizio di aggiornamento variabili di cosmobusiness fornendo il nuovo payload ricevuto in
   * input cancellando e riscrivendo tutte le variabili presenti all'interno della tabella
   * cosmo_c_configurazione_metadati
   *
   * @param idPratica
   * @return
   */
  Pratica aggiornaVariabiliProcesso(String idPratica);

}

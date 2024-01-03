/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.service;

import it.csi.cosmo.common.dto.common.ConfigurazioneDTO;

/**
 * Servizio per la gestione della configurazione
 */
public interface ConfigurazioneEnteService {

  /**
   * Restituisce la configurazione del parametro passato in ingresso
   *
   * @param chiave e' il parametro di cui si vuole la configurazione
   * @return e' un dto con le informazioni con la configurazione del parametro di ingresso trovato
   *         nel database
   */
  ConfigurazioneDTO getConfigEnte(String chiave);

}

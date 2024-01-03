/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.service;

import it.csi.cosmo.cosmopratiche.dto.rest.CondivisionePratica;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaCondivisionePraticaRequest;

public interface CondivisioniPraticheService {

  /**
   * @param idPratica
   * @param body
   * @return
   */
  CondivisionePratica creaCondivisione(Long idPratica, CreaCondivisionePraticaRequest body);

  /**
   * @param idPratica
   * @param idCondivisione
   */
  void rimuoviCondivisione(Long idPratica, Long idCondivisione);

}

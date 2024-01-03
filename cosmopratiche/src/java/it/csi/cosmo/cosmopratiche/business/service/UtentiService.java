/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.service;

import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;

public interface UtentiService {

  // ATTIVO
  public Void deleteUtentiPratichePreferiteIdPratica(String idPratica);

  // ATTIVO
  public Pratica putUtentiPratichePreferiteIdPratica(String idPratica);
}

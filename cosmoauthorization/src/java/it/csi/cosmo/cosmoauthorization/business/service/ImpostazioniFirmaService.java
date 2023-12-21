/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service;

import it.csi.cosmo.cosmoauthorization.dto.rest.ImpostazioniFirma;

/**
 * Servizio per la gestione delle impostazioni di firma
 */
public interface ImpostazioniFirmaService {

  /**
   * Restituisce le impostazioni di firma
   *
   * @return le impostazioni di firma
   */
  ImpostazioniFirma getImpostazioniFirma();

}

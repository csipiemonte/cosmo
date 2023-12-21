/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface MotoreJsonDinamicoService {

  /**
   * @param specificaMappatura
   * @param sorgente
   * @return
   */
  JsonNode eseguiMappatura(String specificaMappatura, Object sorgente);

}

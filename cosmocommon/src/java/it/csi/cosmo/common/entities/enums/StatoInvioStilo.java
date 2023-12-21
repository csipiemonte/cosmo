/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities.enums;

import java.security.InvalidParameterException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 */

public enum StatoInvioStilo {

  DA_INVIARE("DA_INVIARE"), 
  IN_FASE_DI_INVIO("IN_FASE_DI_INVIO"), 
  INVIATO("INVIATO"), 
  ERR_INVIO("ERR_INVIO"), 
  NON_INVIATO("NON_INVIATO");

  private String codice;

  private StatoInvioStilo(String codice) {
    this.codice = codice;
  }

  public String getCodice() {
    return codice;
  }

  public static StatoInvioStilo fromCode(String code) {
    if (StringUtils.isBlank(code)) {
      return null;
    }
    for (StatoInvioStilo candidate : StatoInvioStilo.values()) {
      if (candidate.codice.equals(code)) {
        return candidate;
      }
    }
    throw new InvalidParameterException(
        "Unknown " + StatoInvioStilo.class.getSimpleName() + " [" + code + "]");
  }
}

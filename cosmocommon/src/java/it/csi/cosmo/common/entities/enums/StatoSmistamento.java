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

public enum StatoSmistamento {

  SMISTATO("SMISTATO");

  private String codice;

  private StatoSmistamento(String codice) {
    this.codice = codice;
  }

  public String getCodice() {
    return codice;
  }

  public static StatoSmistamento fromCode(String code) {
    if (StringUtils.isBlank(code)) {
      return null;
    }
    for (StatoSmistamento candidate : StatoSmistamento.values()) {
      if (candidate.codice.equals(code)) {
        return candidate;
      }
    }
    throw new InvalidParameterException(
        "Unknown " + StatoSmistamento.class.getSimpleName() + " [" + code + "]");
  }
}

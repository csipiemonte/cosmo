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

public enum FaseTrasformazioneDatiPratica {
  
  BEFORE_PROCESS_START("beforeProcessStart"), 
  AFTER_PROCESS_START("afterProcessStart");

  private String codice;

  private FaseTrasformazioneDatiPratica(String codice) {
    this.codice = codice;
  }

  public String getCodice() {
    return codice;
  }

  public static FaseTrasformazioneDatiPratica fromCode(String code) {
    if (StringUtils.isBlank(code)) {
      return null;
    }
    for (FaseTrasformazioneDatiPratica candidate : FaseTrasformazioneDatiPratica.values()) {
      if (candidate.codice.equals(code)) {
        return candidate;
      }
    }
    throw new InvalidParameterException(
        "Unknown " + FaseTrasformazioneDatiPratica.class.getSimpleName() + " [" + code + "]");
  }
}

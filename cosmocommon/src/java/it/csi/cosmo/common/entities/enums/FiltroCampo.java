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

public enum FiltroCampo {

  //@formatter:off
  RANGE("range", "Range (Da / a)"),
  SINGOLO("singolo","Singolo Campo");

  //@formatter:on

  private String codice;

  private String descrizione;

  private FiltroCampo(String codice, String descrizione) {
    this.codice = codice;
    this.descrizione = descrizione;
  }

  public String getCodice() {
    return codice;
  }

  public String getdescrizione() {
    return descrizione;
  }

  public static FiltroCampo fromCodice(String codice) {
    if (StringUtils.isBlank(codice)) {
      return null;
    }
    for (FiltroCampo candidate : FiltroCampo.values()) {
      if (candidate.codice.equals(codice)) {
        return candidate;
      }
    }
    throw new InvalidParameterException(
        "Unknown " + FiltroCampo.class.getSimpleName() + " [" + codice + "]");
  }

  public static FiltroCampo fromdescrizione(String descrizione) {
    if (StringUtils.isBlank(descrizione)) {
      return null;
    }
    for (FiltroCampo candidate : FiltroCampo.values()) {
      if (candidate.descrizione.equals(descrizione)) {
        return candidate;
      }
    }
    throw new InvalidParameterException(
        "Unknown " + FiltroCampo.class.getSimpleName() + " [" + descrizione + "]");
  }


}

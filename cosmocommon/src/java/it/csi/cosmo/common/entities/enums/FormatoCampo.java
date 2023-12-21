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

public enum FormatoCampo {

  //@formatter:off
  BOOLEANO("booleano", "Booleano"),
  DATA("data","Data"),
  STRINGA("stringa","Stringa"),
  NUMERICO("numerico","Numerico");

  //@formatter:on

  private String codice;

  private String descrizione;

  private FormatoCampo(String codice, String descrizione) {
    this.codice = codice;
    this.descrizione = descrizione;
  }

  public String getCodice() {
    return codice;
  }

  public String getdescrizione() {
    return descrizione;
  }

  public static FormatoCampo fromCodice(String codice) {
    if (StringUtils.isBlank(codice)) {
      return null;
    }
    for (FormatoCampo candidate : FormatoCampo.values()) {
      if (candidate.codice.equals(codice)) {
        return candidate;
      }
    }
    throw new InvalidParameterException(
        "Unknown " + FormatoCampo.class.getSimpleName() + " [" + codice + "]");
  }

  public static FormatoCampo fromdescrizione(String descrizione) {
    if (StringUtils.isBlank(descrizione)) {
      return null;
    }
    for (FormatoCampo candidate : FormatoCampo.values()) {
      if (candidate.descrizione.equals(descrizione)) {
        return candidate;
      }
    }
    throw new InvalidParameterException(
        "Unknown " + FormatoCampo.class.getSimpleName() + " [" + descrizione + "]");
  }


}

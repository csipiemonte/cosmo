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

public enum TipoRelazionePraticaPratica {
  DUPLICA("DUPLICA"), DIPENDE_DA("DIPENDE_DA"), DIPENDENTE_DA("DIPENDENTE_DA");

  private String codice;

  private TipoRelazionePraticaPratica(String codice) {
    this.codice = codice;
  }

  public String getCodice() {
    return codice;
  }


  public static TipoRelazionePraticaPratica fromCodice(String codice) {
    if (StringUtils.isBlank(codice)) {
      return null;
    }
    for (TipoRelazionePraticaPratica candidate : TipoRelazionePraticaPratica.values()) {
      if (candidate.codice.equals(codice)) {
        return candidate;
      }
    }
    throw new InvalidParameterException(
        "Unknown " + TipoRelazionePraticaPratica.class.getSimpleName() + " [" + codice + "]");
  }

}

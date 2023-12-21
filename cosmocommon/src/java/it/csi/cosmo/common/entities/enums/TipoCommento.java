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

public enum TipoCommento {
  //@formatter:off
  COMMENTO("comment", "Commento"),
  EVENTO("event","Evento");

  //@formatter:on

  private String codice;

  private String descrizione;

  private TipoCommento(String codice, String descrizione) {
    this.codice = codice;
    this.descrizione = descrizione;
  }

  public String getCodice() {
    return codice;
  }

  public String getdescrizione() {
    return descrizione;
  }

  public static TipoCommento fromCodice(String codice) {
    if (StringUtils.isBlank(codice)) {
      return null;
    }
    for (TipoCommento candidate : TipoCommento.values()) {
      if (candidate.codice.equals(codice)) {
        return candidate;
      }
    }
    throw new InvalidParameterException(
        "Unknown " + TipoCommento.class.getSimpleName() + " [" + codice + "]");
  }

  public static TipoCommento fromdescrizione(String descrizione) {
    if (StringUtils.isBlank(descrizione)) {
      return null;
    }
    for (TipoCommento candidate : TipoCommento.values()) {
      if (candidate.descrizione.equals(descrizione)) {
        return candidate;
      }
    }
    throw new InvalidParameterException(
        "Unknown " + TipoCommento.class.getSimpleName() + " [" + descrizione + "]");
  }
}

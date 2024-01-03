/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.dto.rest;


/**
 *
 */

public enum GroupsCriteria {

  IN_CORSO("inCorso"),
  CONCLUSE("concluse"),
  ANNULLATE("annullate");

  private String codice;

  public String getCodice() {
    return codice;
  }

  private GroupsCriteria(String codice) {
    this.codice = codice;
  }

  public static GroupsCriteria fromCodice(String codice) {
    for (GroupsCriteria candidate : GroupsCriteria.values()) {
      if (candidate.getCodice().equals(codice)) {
        return candidate;
      }
    }
    return null;
  }
}

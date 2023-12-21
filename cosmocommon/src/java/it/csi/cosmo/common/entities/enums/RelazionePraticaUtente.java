/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities.enums;

/**
 *
 */

public enum RelazionePraticaUtente {
  CONDIVISA("condivisa"), PREFERITA("preferita");

  private String codice;

  private RelazionePraticaUtente(String codice) {
    this.codice = codice;
  }

  public String getCodice() {
    return codice;
  }

  public static RelazionePraticaUtente fromCodice(String codice) {
    for (RelazionePraticaUtente candidate : RelazionePraticaUtente.values()) {
      if (candidate.getCodice().equalsIgnoreCase(codice)) {
        return candidate;
      }
    }
    return null;
  }
}

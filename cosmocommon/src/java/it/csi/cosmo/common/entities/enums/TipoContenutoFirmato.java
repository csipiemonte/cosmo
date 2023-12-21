/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities.enums;

/**
 *
 */

public enum TipoContenutoFirmato {
  FIRMA_DIGITALE("firma_digitale"),
  FIRMA_ELETTRONICA_AVANZATA("firma_elettronica_avanzata"),
  SIGILLO_ELETTRONICO("sigillo_elettronico");

  private String codice;

  private TipoContenutoFirmato(String codice) {
    this.codice = codice;
  }

  public String getCodice() {
    return codice;
  }

}

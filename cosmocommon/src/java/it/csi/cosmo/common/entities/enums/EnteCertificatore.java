/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities.enums;

/**
 *
 */

public enum EnteCertificatore {
  ARUBA("Aruba"), INFOCERT("InfoCert"), UANATACA("Uanataca");

  private String codice;

  EnteCertificatore(String codice) {
    this.codice = codice;
  }

  public String getCodice() {
    return codice;
  }

}

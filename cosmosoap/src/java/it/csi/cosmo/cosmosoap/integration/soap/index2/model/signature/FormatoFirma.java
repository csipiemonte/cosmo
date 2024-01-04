/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature;

/**
 *
 */

public enum FormatoFirma {
  //@formatter:off
  FIRMA_DIGITALE_ENVELOPED(1),
  FIRMA_DIGITALE_DETACHED(2),
  MARCA_TEMPORALE_INFOCERT(3),
  MARCA_TEMPORALE_DETACHED(4),
  TIME_STAMPED_DATA(5),
  FIRMA_DIGITALE_PADES(6),
  FIRMA_DIGITALE_XADES(7);
  //@formatter:on

  public static FormatoFirma byValue(int value) {
    for (FormatoFirma candidate : FormatoFirma.values()) {
      if (candidate.getValue() == value) {
        return candidate;
      }
    }
    return null;
  }

  private int value;

  private FormatoFirma(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

}

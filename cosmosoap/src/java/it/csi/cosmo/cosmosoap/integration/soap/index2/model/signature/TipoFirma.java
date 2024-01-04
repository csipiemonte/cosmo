/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature;

/**
 *
 */

public enum TipoFirma {
  //@formatter:off
  MARCA_TEMPORALE(0),
  SEMPLICE(1),
  MULTIPLA_PARALLELA(2),
  MULTIPLA_CONTROFIRMA(3),
  MULTIPLA_CATENA(4);
  //@formatter:on

  public static TipoFirma byValue(int value) {
    for (TipoFirma candidate : TipoFirma.values()) {
      if (candidate.getValue() == value) {
        return candidate;
      }
    }
    return null;
  }

  private int value;

  private TipoFirma(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

}

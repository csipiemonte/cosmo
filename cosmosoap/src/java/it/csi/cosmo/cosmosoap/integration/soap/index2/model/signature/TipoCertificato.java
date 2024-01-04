/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature;

/**
 *
 */

public enum TipoCertificato {
  //@formatter:off
  DIGITAL_SIGNATURE (1),
  TIMESTAMP_SIGNATURE (2);
  //@formatter:on

  public static TipoCertificato byValue(int value) {
    for (TipoCertificato candidate : TipoCertificato.values()) {
      if (candidate.getValue() == value) {
        return candidate;
      }
    }
    return null;
  }

  private int value;

  private TipoCertificato(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

}

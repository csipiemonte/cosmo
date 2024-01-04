/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature;

/**
 *
 */

public enum ConformitaParametriInput {
  //@formatter:off
  NON_OK(0),
  OK(1);
  //@formatter:on

  public static ConformitaParametriInput byValue(int value) {
    for (ConformitaParametriInput candidate : ConformitaParametriInput.values()) {
      if (candidate.getValue() == value) {
        return candidate;
      }
    }
    return null;
  }

  private int value;

  private ConformitaParametriInput(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

}

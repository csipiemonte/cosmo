/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.stardas.model;

/**
 *
 */

public enum StatoSmistamento {

  DA_SMISTARE("DA_SMISTARE"),
  IN_SMISTAMENTO("IN_SMISTAMENTO"),
  SMISTATO_PARZIALMENTE(
      "SMISTATO_PARZIALMENTE"),
  SMISTATO("SMISTATO"),
  ERR_SMISTAMENTO("ERR_SMISTAMENTO"),
  ERR_CALLBACK("ERR_CALLBACK");

  private String codice;

  private StatoSmistamento(String codice) {
    this.codice = codice;
  }

  public String getCodice() {
    return codice;
  }
}

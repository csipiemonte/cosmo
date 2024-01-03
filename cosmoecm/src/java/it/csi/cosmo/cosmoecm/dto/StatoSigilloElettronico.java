/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.dto;

/**
 *
 */

public enum StatoSigilloElettronico {

  DA_SIGILLARE("DA_SIGILLARE"),
  SIGILLO_WIP("SIGILLO_WIP"),
  SIGILLATO("SIGILLATO"),
  ERR_SIGILLO("ERR_SIGILLO");

  private String codice;

  private StatoSigilloElettronico(String codice) {
    this.codice = codice;
  }

  public String getCodice() {
    return codice;
  }
}

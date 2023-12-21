/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmobusiness.security;

/**
 * Enumerazione contenente tutti gli Scopes gestiti dall'applicativo
 */
public enum Scopes {
  MONITORING("Monitoraggio dello stato applicativo"), PROCESS_EVENTS(
      "Invio di eventi relativi al process management"), DEFAULT_CLIENT("Default client");

  private String descrizione;

  Scopes(String descrizione) {
    this.descrizione = descrizione;
  }

  public String getDescrizione() {
    return descrizione;
  }

}

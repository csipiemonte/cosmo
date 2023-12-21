/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmobe.security;

/**
 * Enumerazione contenente tutti gli Scopes gestiti dall'applicativo
 */
public enum Scopes {

  //@formatter:off
  API_MANAGER("API Manager"),
  AP_PRAT("Apertura pratica"),
  BE_ORCHESTRATOR("Orchestrazione per i fruitori esterni"),
  MONITORING("Monitoraggio dello stato applicativo"),
  STARDAS_CALLBACK("Invio di callback da STARDAS");
  //@formatter:on

  private String descrizione;

  Scopes ( String descrizione ) {
    this.descrizione = descrizione;
  }

  public String getDescrizione () {
    return descrizione;
  }

}

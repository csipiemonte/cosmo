/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmoecm.security;

/**
 * Enumerazione contenente tutti gli Scopes gestiti dall'applicativo
 */
public enum Scopes {
  MONITORING ( "Monitoraggio dello stato applicativo" ),
  DEFAULT_CLIENT ( "Default client" );

  private String descrizione;

  Scopes ( String descrizione ) {
    this.descrizione = descrizione;
  }

  public String getDescrizione () {
    return descrizione;
  }

}

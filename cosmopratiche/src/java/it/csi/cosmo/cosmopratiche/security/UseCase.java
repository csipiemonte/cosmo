/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmopratiche.security;

/**
 * Enumerazione contenente tutte le Authorities censite e gestite dall'applicativo.
 */
public enum UseCase {

  LOGGED_IN("Login effettuato"),

  ADMIN_COSMO("Amministratore di sistema");

  public static final String ADMIN_PRAT_CODE = "ADMIN_PRAT";

  private String descrizione;

  UseCase(String descrizione) {
    this.descrizione = descrizione;
  }

  public String getDescrizione() {
    return descrizione;
  }

}

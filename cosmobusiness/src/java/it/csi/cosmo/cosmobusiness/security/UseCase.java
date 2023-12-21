/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmobusiness.security;

/**
 * Enumerazione contenente tutte le Authorities censite e gestite dall'applicativo.
 */
public enum UseCase {

  OPERATE("Diritti per tutti gli utenti"), ADMIN_COSMO("Amministrazione del backoffice"), LOGGED_IN(
      "Login effettuato");

  private String descrizione;

  UseCase(String descrizione) {
    this.descrizione = descrizione;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public static class Constants {
    private Constants() { /* private */ }

    public static final String ADMIN_COSMO = "ADMIN_COSMO";
    public static final String CONF = "CONF";
  }
}

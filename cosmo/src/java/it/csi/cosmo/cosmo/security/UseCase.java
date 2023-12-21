/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.security;

/**
 * Enumerazione contenente tutte le Authorities censite e gestite dall'applicativo.
 */
public enum UseCase {

  OPERATE("Diritti per tutti gli utenti"), ADMIN("Diritti di amministrazione"), LOGGED_IN(
      "Login effettuato"), CONF("Configurazione processi");

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

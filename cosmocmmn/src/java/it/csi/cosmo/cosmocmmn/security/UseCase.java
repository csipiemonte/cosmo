/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmocmmn.security;

/**
 * Enumerazione contenente tutte le Authorities censite e gestite dall'applicativo.
 */
public enum UseCase {

  OPERATE("Diritti per tutti gli utenti"), ADMIN("Diritti di amministrazione"), LOGGED_IN(
      "Login effettuato");

  private String descrizione;

  UseCase(String descrizione) {
    this.descrizione = descrizione;
  }

  public String getDescrizione() {
    return descrizione;
  }

}

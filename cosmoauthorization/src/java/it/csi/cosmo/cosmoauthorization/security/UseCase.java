/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.security;

/**
 * Enumerazione contenente tutte le Authorities censite e gestite dall'applicativo.
 */
public enum UseCase {
  //@formatter:off
  ADMIN_PRAT("Amministrazione pratica"),
  OPERATE("Diritti per tutti gli utenti"), 
  ADMIN("Diritti di amministrazione"), 
  LOGGED_IN("Login effettuato");
  //@formatter:on

  private String descrizione;

  UseCase(String descrizione) {
    this.descrizione = descrizione;
  }

  public String getDescrizione() {
    return descrizione;
  }

}

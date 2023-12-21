/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.security.model;

/**
 * Da documentazione CSI F301-IDEN-SHB2-VARV08-Manuale Integrazione:
 * Puo' assumere i seguenti valori:
    1 = username e password di utenti auto-registrati;
    2 = username e password di utenti con identita' verificati;
    4 = username, password e PIN di utenti con identita' verificati;
    8 = certificati X.509 di CA non qualificate;
    16= certificati X.509 di CA qualificate
 */
public enum LivelloAutenticazione {

  NON_RICONOSCIUTO(0),
  USERNAME_PASSWORD_AUTOREGISTRATI(1),
  USERNAME_PASSWORD_IDENTITA_VERIFICATA(2),
  USERNAME_PASSWORD_PIN_IDENTITA_VERIFICATA(4),
  X509_CA_NON_QUALIFICATA(8),
  X509_CA_QUALIFICATA(16);

  private int valore;

  private LivelloAutenticazione(int valore) {
    this.valore = valore;
  }

  public int getValore() {
    return valore;
  }

  public static LivelloAutenticazione fromValore(int valore) {
    for (LivelloAutenticazione candidate: LivelloAutenticazione.values()) {
      if (candidate.getValore() == valore) {
        return candidate;
      }
    }
    return LivelloAutenticazione.NON_RICONOSCIUTO;
  }

}

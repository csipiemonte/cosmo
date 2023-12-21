/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities.enums;

import java.security.InvalidParameterException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 */

public enum TipoNotifica {
  //@formatter:off
  CREA_TASK("CREA_TASK", "creaTask"),
  ASSEGNA_TASK("ASSEGNA_TASK", "assegnaTask"),
  ANNULLA_TASK("ANNULLA_TASK", "annullaTask"),
  COMMENTO("COMMENTO", "commento"),
  CONDIVISIONE_PRATICA("CONDIVISIONE_PRATICA", "condividiPratica"),
  RIPRISTINO_PRATICA("RIPRISTINO_PRATICA", "ripristinoPratica"),
  INFO("INFO", "info"),
  SMISTAMENTO_DOCUMENTI("SMISTAMENTO_DOCUMENTI","smistamentoDocumenti"),
  ELABORAZIONE_DOCUMENTI("ELABORAZIONE_DOCUMENTI","elaborazioneDocumenti"),
  APP_SIGILLO_DOCUMENTI("APP_SIGILLO_DOCUMENTI", "sigilloDocumenti");

  //@formatter:on

  private String codice;

  private String azione;

  private TipoNotifica(String codice, String azione) {
    this.codice = codice;
    this.azione = azione;
  }

  public String getCodice() {
    return codice;
  }

  public String getAzione() {
    return azione;
  }

  public static TipoNotifica fromCodice(String codice) {
    if (StringUtils.isBlank(codice)) {
      return null;
    }
    for (TipoNotifica candidate : TipoNotifica.values()) {
      if (candidate.codice.equals(codice)) {
        return candidate;
      }
    }
    throw new InvalidParameterException(
        "Unknown " + TipoNotifica.class.getSimpleName() + " [" + codice + "]");
  }

  public static TipoNotifica fromAzione(String azione) {
    if (StringUtils.isBlank(azione)) {
      return null;
    }
    for (TipoNotifica candidate : TipoNotifica.values()) {
      if (candidate.azione.equals(azione)) {
        return candidate;
      }
    }
    throw new InvalidParameterException(
        "Unknown " + TipoNotifica.class.getSimpleName() + " [" + azione + "]");
  }
}

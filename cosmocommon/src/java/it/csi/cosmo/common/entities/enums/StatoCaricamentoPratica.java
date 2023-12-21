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

public enum StatoCaricamentoPratica {

  //@formatter:off
  CARICAMENTO_IN_BOZZA("CARICAMENTO_IN_BOZZA"),
  IN_ATTESA_DI_ELABORAZIONE("IN_ATTESA_DI_ELABORAZIONE"),
  ELABORAZIONE_INIZIATA("ELABORAZIONE_INIZIATA"),
  PRATICA_CREATA("PRATICA_CREATA"),
  PRATICA_IN_ERRORE("PRATICA_IN_ERRORE"),
  PRATICA_CREATA_CON_ERRORE("PRATICA_CREATA_CON_ERRORE"),
  CARICAMENTO_DOCUMENTI("CARICAMENTO_DOCUMENTI"),
  CARICAMENTO_DOCUMENTI_IN_ERRORE("CARICAMENTO_DOCUMENTI_IN_ERRORE"),
  PROCESSO_AVVIATO("PROCESSO_AVVIATO"),
  PROCESSO_IN_ERRORE("PROCESSO_IN_ERRORE"),
  ELABORAZIONE_COMPLETATA("ELABORAZIONE_COMPLETATA"),
  ELABORAZIONE_COMPLETATA_CON_ERRORE("ELABORAZIONE_COMPLETATA_CON_ERRORE"),
  ERRORE_ELABORAZIONE("ERRORE_ELABORAZIONE");
  //@formatter:on

  private String codice;

  private StatoCaricamentoPratica(String codice) {
    this.codice = codice;
  }

  public String getCodice() {
    return codice;
  }

  public static StatoCaricamentoPratica fromCode(String code) {
    if (StringUtils.isBlank(code)) {
      return null;
    }
    for (StatoCaricamentoPratica candidate : StatoCaricamentoPratica.values()) {
      if (candidate.codice.equals(code)) {
        return candidate;
      }
    }
    throw new InvalidParameterException(
        "Unknown " + StatoCaricamentoPratica.class.getSimpleName() + " [" + code + "]");
  }

}

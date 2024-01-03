/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.dto.rest;

import it.csi.cosmo.common.dto.search.StringFilter;

/**
 *
 */

public class FiltroRicercaCaricamentoPraticheDTO {

  private StringFilter codiceStatoCaricamentoPratica;

  private StringFilter nomeFile;


  public StringFilter getNomeFile() {
    return nomeFile;
  }

  public void setNomeFile(StringFilter nomeFile) {
    this.nomeFile = nomeFile;
  }

  public StringFilter getCodiceStatoCaricamentoPratica() {
    return codiceStatoCaricamentoPratica;
  }

  public void setCodiceStatoCaricamentoPratica(StringFilter codiceStatoCaricamentoPratica) {
    this.codiceStatoCaricamentoPratica = codiceStatoCaricamentoPratica;
  }

}

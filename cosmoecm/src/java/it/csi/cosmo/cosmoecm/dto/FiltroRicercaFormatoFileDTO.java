/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.dto;

import it.csi.cosmo.common.dto.search.BooleanFilter;
import it.csi.cosmo.common.dto.search.StringFilter;

/**
 *
 */

public class FiltroRicercaFormatoFileDTO {

  private StringFilter codice;
  private StringFilter descrizione;
  private StringFilter icona;
  private StringFilter mimeType;
  private BooleanFilter supportaAnteprima;
  private StringFilter fullText;

  public StringFilter getCodice() {
    return codice;
  }

  public void setCodice(StringFilter codice) {
    this.codice = codice;
  }

  public StringFilter getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(StringFilter descrizione) {
    this.descrizione = descrizione;
  }

  public StringFilter getIcona() {
    return icona;
  }

  public void setIcona(StringFilter icona) {
    this.icona = icona;
  }

  public StringFilter getMimeType() {
    return mimeType;
  }

  public void setMimeType(StringFilter mimeType) {
    this.mimeType = mimeType;
  }

  public BooleanFilter getSupportaAnteprima() {
    return supportaAnteprima;
  }

  public void setSupportaAnteprima(BooleanFilter supportaAnteprima) {
    this.supportaAnteprima = supportaAnteprima;
  }

  public StringFilter getFullText() {
    return fullText;
  }

  public void setFullText(StringFilter fullText) {
    this.fullText = fullText;
  }

}

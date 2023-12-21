/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.dto.rest;

import java.io.Serializable;
import java.time.OffsetDateTime;

public class ProfiloFEQDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private String codice = null;
  private String descrizione = null;
  private Boolean nonValidoInApposizione = null;
  private OffsetDateTime dtInizioVal = null;
  private OffsetDateTime dtFineVal = null;

  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }


  public Boolean getNonValidoInApposizione() {
    return nonValidoInApposizione;
  }

  public void setNonValidoInApposizione(Boolean nonValidoInApposizione) {
    this.nonValidoInApposizione = nonValidoInApposizione;
  }

  public OffsetDateTime getDtInizioVal() {
    return dtInizioVal;
  }

  public void setDtInizioVal(OffsetDateTime dtInizioVal) {
    this.dtInizioVal = dtInizioVal;
  }

  public OffsetDateTime getDtFineVal() {
    return dtFineVal;
  }

  public void setDtFineVal(OffsetDateTime dtFineVal) {
    this.dtFineVal = dtFineVal;
  }

  @Override
  public String toString() {
    return "ProfiloFEQDTO [codice=" + codice + ", descrizione=" + descrizione
        + ", nonValidoInApposizione=" + nonValidoInApposizione + ", dtInizioVal=" + dtInizioVal
        + ", dtFineVal=" + dtFineVal + "]";
  }



}


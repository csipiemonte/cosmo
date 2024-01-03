/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.dto.rest;


import it.csi.cosmo.common.dto.search.LongFilter;
import it.csi.cosmo.common.dto.search.RecursiveFilterSpecification;
import it.csi.cosmo.common.dto.search.StringFilter;



public class FiltroRicercaTipiPraticheDTO
implements RecursiveFilterSpecification<FiltroRicercaTipiPraticheDTO> {

  private StringFilter codice;
  private StringFilter descrizione;
  private LongFilter idEnte;
  private StringFilter fullText;
  private FiltroRicercaTipiPraticheDTO[] and;
  private FiltroRicercaTipiPraticheDTO[] or;

  @Override
  public FiltroRicercaTipiPraticheDTO[] getAnd() {
    return and;
  }

  public void setAnd(FiltroRicercaTipiPraticheDTO[] and) {
    this.and = and;
  }

  @Override
  public FiltroRicercaTipiPraticheDTO[] getOr() {
    return or;
  }

  public void setOr(FiltroRicercaTipiPraticheDTO[] or) {
    this.or = or;
  }

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


  public LongFilter getIdEnte() {
    return idEnte;
  }

  public void setIdEnte(LongFilter idEnte) {
    this.idEnte = idEnte;
  }

  public StringFilter getFullText() {
    return fullText;
  }

  public void setFullText(StringFilter fullText) {
    this.fullText = fullText;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codice == null) ? 0 : codice.hashCode());
    result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
    result = prime * result + ((fullText == null) ? 0 : fullText.hashCode());
    result = prime * result + ((idEnte == null) ? 0 : idEnte.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FiltroRicercaTipiPraticheDTO other = (FiltroRicercaTipiPraticheDTO) obj;
    if (codice == null) {
      if (other.codice != null)
        return false;
    } else if (!codice.equals(other.codice))
      return false;
    if (descrizione == null) {
      if (other.descrizione != null)
        return false;
    } else if (!descrizione.equals(other.descrizione))
      return false;
    if (fullText == null) {
      if (other.fullText != null)
        return false;
    } else if (!fullText.equals(other.fullText))
      return false;
    if (idEnte == null) {
      if (other.idEnte != null)
        return false;
    } else if (!idEnte.equals(other.idEnte))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "FiltroRicercaCustomFormDTO [codice=" + codice + ", descrizione="
        + descrizione + ", fullText=" + fullText +", idEnte=" + idEnte + "]";
  }

}



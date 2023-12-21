/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.dto.rest;

import it.csi.cosmo.common.dto.search.LongFilter;
import it.csi.cosmo.common.dto.search.RecursiveFilterSpecification;
import it.csi.cosmo.common.dto.search.StringFilter;

/**
 *
 */

public class FiltroRicercaFormLogiciDTO
implements RecursiveFilterSpecification<FiltroRicercaFormLogiciDTO> {

  private LongFilter id;
  private StringFilter codice;
  private StringFilter descrizione;
  private StringFilter fullText;
  private LongFilter idEnte;

  private FiltroRicercaFormLogiciDTO[] and;
  private FiltroRicercaFormLogiciDTO[] or;

  @Override
  public FiltroRicercaFormLogiciDTO[] getAnd() {
    return and;
  }

  public void setAnd(FiltroRicercaFormLogiciDTO[] and) {
    this.and = and;
  }

  @Override
  public FiltroRicercaFormLogiciDTO[] getOr() {
    return or;
  }

  public void setOr(FiltroRicercaFormLogiciDTO[] or) {
    this.or = or;
  }

  public LongFilter getId() {
    return id;
  }

  public void setId(LongFilter id) {
    this.id = id;
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

  public StringFilter getFullText() {
    return fullText;
  }

  public void setFullText(StringFilter fullText) {
    this.fullText = fullText;
  }

  public LongFilter getIdEnte() {
    return idEnte;
  }

  public void setIdEnte(LongFilter idEnte) {
    this.idEnte = idEnte;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codice == null) ? 0 : codice.hashCode());
    result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
    result = prime * result + ((fullText == null) ? 0 : fullText.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    FiltroRicercaFormLogiciDTO other = (FiltroRicercaFormLogiciDTO) obj;
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
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
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
    return "FiltroRicercaFormLogiciDTO [id=" + id + ", codice=" + codice + ", descrizione="
        + descrizione + ", fullText=" + fullText + ", idEnte=" + idEnte + "]";
  }

}

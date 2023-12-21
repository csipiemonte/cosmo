/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.dto.rest;

import it.csi.cosmo.common.dto.search.LongFilter;
import it.csi.cosmo.common.dto.search.StringFilter;

/**
 *
 */

public class FiltroRicercaEntiDTO {

  private LongFilter id;
  private StringFilter nome;
  private StringFilter codiceIpa;
  private StringFilter codiceFiscale;
  private StringFilter fullText;

  public LongFilter getId() {
    return id;
  }

  public void setId(LongFilter id) {
    this.id = id;
  }

  public StringFilter getNome() {
    return nome;
  }

  public void setNome(StringFilter nome) {
    this.nome = nome;
  }

  public StringFilter getCodiceIpa() {
    return codiceIpa;
  }

  public void setCodiceIpa(StringFilter codiceIpa) {
    this.codiceIpa = codiceIpa;
  }

  public StringFilter getCodiceFiscale() {
    return codiceFiscale;
  }

  public void setCodiceFiscale(StringFilter codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
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
    result = prime * result + ((codiceFiscale == null) ? 0 : codiceFiscale.hashCode());
    result = prime * result + ((codiceIpa == null) ? 0 : codiceIpa.hashCode());
    result = prime * result + ((fullText == null) ? 0 : fullText.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
    FiltroRicercaEntiDTO other = (FiltroRicercaEntiDTO) obj;
    if (codiceFiscale == null) {
      if (other.codiceFiscale != null)
        return false;
    } else if (!codiceFiscale.equals(other.codiceFiscale))
      return false;
    if (codiceIpa == null) {
      if (other.codiceIpa != null)
        return false;
    } else if (!codiceIpa.equals(other.codiceIpa))
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
    if (nome == null) {
      if (other.nome != null)
        return false;
    } else if (!nome.equals(other.nome))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "FiltroRicercaEntiDTO [id=" + id + ", nome=" + nome + ", codiceIpa=" + codiceIpa
        + ", codiceFiscale=" + codiceFiscale + ", fullText=" + fullText + "]";
  }

}

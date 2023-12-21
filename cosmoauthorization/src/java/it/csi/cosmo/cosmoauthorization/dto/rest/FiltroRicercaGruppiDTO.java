/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.dto.rest;

import it.csi.cosmo.common.dto.search.BooleanFilter;
import it.csi.cosmo.common.dto.search.LongFilter;
import it.csi.cosmo.common.dto.search.StringFilter;

/**
 *
 */

public class FiltroRicercaGruppiDTO {

  private LongFilter id;
  private LongFilter idEnte;
  private StringFilter nome;
  private StringFilter codice;
  private StringFilter descrizione;
  private StringFilter fullText;
  private StringFilter codiceTipoPratica;
  private BooleanFilter soloConAssociazioneTipoPratica;

  public StringFilter getFullText() {
    return fullText;
  }

  public void setFullText(StringFilter fullText) {
    this.fullText = fullText;
  }

  public LongFilter getId() {
    return id;
  }

  public void setId(LongFilter id) {
    this.id = id;
  }

  public LongFilter getIdEnte() {
    return idEnte;
  }

  public void setIdEnte(LongFilter idEnte) {
    this.idEnte = idEnte;
  }

  public StringFilter getNome() {
    return nome;
  }

  public void setNome(StringFilter nome) {
    this.nome = nome;
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

  public StringFilter getCodiceTipoPratica() {
    return codiceTipoPratica;
  }

  public void setCodiceTipoPratica(StringFilter codiceTipoPratica) {
    this.codiceTipoPratica = codiceTipoPratica;
  }

  public BooleanFilter getSoloConAssociazioneTipoPratica() {
    return soloConAssociazioneTipoPratica;
  }

  public void setSoloConAssociazioneTipoPratica(BooleanFilter soloConAssociazioneTipoPratica) {
    this.soloConAssociazioneTipoPratica = soloConAssociazioneTipoPratica;
  }

  @Override
  public String toString() {
    return "FiltroRicercaGruppiDTO [id=" + id + ", idEnte=" + idEnte + ", nome=" + nome
        + ", codice=" + codice + ", descrizione=" + descrizione + ", fullText=" + fullText
        + ", codiceTipoPratica=" + codiceTipoPratica + ", soloConAssociazioneTipoPratica="
        + soloConAssociazioneTipoPratica + "]";
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
    result = prime * result + ((nome == null) ? 0 : nome.hashCode());
    result = prime * result + ((codiceTipoPratica == null) ? 0 : codiceTipoPratica.hashCode());
    result = prime * result
        + ((soloConAssociazioneTipoPratica == null) ? 0
            : soloConAssociazioneTipoPratica.hashCode());
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
    FiltroRicercaGruppiDTO other = (FiltroRicercaGruppiDTO) obj;
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
    if (nome == null) {
      if (other.nome != null)
        return false;
    } else if (!nome.equals(other.nome))
      return false;
    if (codiceTipoPratica == null) {
      if (other.codiceTipoPratica != null)
        return false;
    } else if (!codiceTipoPratica.equals(other.codiceTipoPratica))
      return false;
    if (soloConAssociazioneTipoPratica == null) {
      if (other.soloConAssociazioneTipoPratica != null)
        return false;
    } else if (!soloConAssociazioneTipoPratica.equals(other.soloConAssociazioneTipoPratica))
      return false;
    return true;
  }

}

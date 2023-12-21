/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.dto.rest;

import it.csi.cosmo.common.dto.search.LongFilter;
import it.csi.cosmo.common.dto.search.RecursiveFilterSpecification;
import it.csi.cosmo.common.dto.search.StringFilter;

/**
 *
 */

public class FiltroRicercaTagsDTO implements RecursiveFilterSpecification<FiltroRicercaTagsDTO> {

  private LongFilter id;
  private StringFilter codice;
  private StringFilter descrizione;
  private StringFilter codiceTipoTag;
  private LongFilter idEnte;
  private LongFilter idUtente;
  private LongFilter idGruppo;
  private LongFilter idPratica;
  private StringFilter fullText;
  private FiltroRicercaTagsDTO[] and;
  private FiltroRicercaTagsDTO[] or;

  public StringFilter getFullText() {
    return fullText;
  }

  public void setFullText(StringFilter fullText) {
    this.fullText = fullText;
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

  public StringFilter getCodiceTipoTag() {
    return codiceTipoTag;
  }

  public void setCodiceTipoTag(StringFilter codiceTipoTag) {
    this.codiceTipoTag = codiceTipoTag;
  }

  @Override
  public FiltroRicercaTagsDTO[] getAnd() {
    return and;
  }

  public void setAnd(FiltroRicercaTagsDTO[] and) {
    this.and = and;
  }

  @Override
  public FiltroRicercaTagsDTO[] getOr() {
    return or;
  }

  public void setOr(FiltroRicercaTagsDTO[] or) {
    this.or = or;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codice == null) ? 0 : codice.hashCode());
    result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
    result = prime * result + ((codiceTipoTag == null) ? 0 : codiceTipoTag.hashCode());
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
    FiltroRicercaTagsDTO other = (FiltroRicercaTagsDTO) obj;
    if (codice == null) {
      if (other.codice != null)
        return false;
    } else if (!descrizione.equals(other.descrizione))
      return false;
    if (codiceTipoTag == null) {
      if (other.codiceTipoTag != null)
        return false;
    } else if (!idEnte.equals(other.idEnte))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "FiltroRicercaEntiDTO [id=" + id + ", codice=" + codice + ", descrizione=" + descrizione
        + ", codiceTipoTag=" + codiceTipoTag + ", codiceIpaEnte=" + idEnte + "]";
  }

  public LongFilter getIdUtente() {
    return idUtente;
  }

  public void setIdUtente(LongFilter idUtente) {
    this.idUtente = idUtente;
  }

  public LongFilter getIdGruppo() {
    return idGruppo;
  }

  public void setIdGruppo(LongFilter idGruppo) {
    this.idGruppo = idGruppo;
  }

  public LongFilter getIdPratica() {
    return idPratica;
  }

  public void setIdPratica(LongFilter idPratica) {
    this.idPratica = idPratica;
  }

  public LongFilter getIdEnte() {
    return idEnte;
  }

  public void setIdEnte(LongFilter idEnte) {
    this.idEnte = idEnte;
  }



}


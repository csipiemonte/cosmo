/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.dto;

import it.csi.cosmo.common.dto.search.DateFilter;
import it.csi.cosmo.common.dto.search.StringFilter;

/**
 *
 */

public class FiltroRicercaDocumentiActaDTO {

  private StringFilter oggetto;
  private StringFilter paroleChiave;
  private StringFilter numeroRepertorio;
  private StringFilter numeroProtocollo;
  private StringFilter aooProtocollante;
  private DateFilter dataCronica;
  private DateFilter dataRegistrazioneProtocollo;
  private Boolean ricercaPerProtocollo;

  public Boolean getRicercaPerProtocollo() {
    return ricercaPerProtocollo;
  }

  public void setRicercaPerProtocollo(Boolean ricercaPerProtocollo) {
    this.ricercaPerProtocollo = ricercaPerProtocollo;
  }

  public StringFilter getOggetto() {
    return oggetto;
  }

  public void setOggetto(StringFilter oggetto) {
    this.oggetto = oggetto;
  }

  public StringFilter getParoleChiave() {
    return paroleChiave;
  }

  public void setParoleChiave(StringFilter paroleChiave) {
    this.paroleChiave = paroleChiave;
  }

  public StringFilter getNumeroRepertorio() {
    return numeroRepertorio;
  }

  public void setNumeroRepertorio(StringFilter numeroRepertorio) {
    this.numeroRepertorio = numeroRepertorio;
  }

  public StringFilter getNumeroProtocollo() {
    return numeroProtocollo;
  }

  public void setNumeroProtocollo(StringFilter numeroProtocollo) {
    this.numeroProtocollo = numeroProtocollo;
  }

  public StringFilter getAooProtocollante() {
    return aooProtocollante;
  }

  public void setAooProtocollante(StringFilter aooProtocollante) {
    this.aooProtocollante = aooProtocollante;
  }

  public DateFilter getDataCronica() {
    return dataCronica;
  }

  public void setDataCronica(DateFilter dataCronica) {
    this.dataCronica = dataCronica;
  }

  public DateFilter getDataRegistrazioneProtocollo() {
    return dataRegistrazioneProtocollo;
  }

  public void setDataRegistrazioneProtocollo(DateFilter dataRegistrazioneProtocollo) {
    this.dataRegistrazioneProtocollo = dataRegistrazioneProtocollo;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((aooProtocollante == null) ? 0 : aooProtocollante.hashCode());
    result = prime * result + ((dataCronica == null) ? 0 : dataCronica.hashCode());
    result = prime * result
        + ((dataRegistrazioneProtocollo == null) ? 0 : dataRegistrazioneProtocollo.hashCode());
    result = prime * result + ((numeroProtocollo == null) ? 0 : numeroProtocollo.hashCode());
    result = prime * result + ((numeroRepertorio == null) ? 0 : numeroRepertorio.hashCode());
    result = prime * result + ((oggetto == null) ? 0 : oggetto.hashCode());
    result = prime * result + ((paroleChiave == null) ? 0 : paroleChiave.hashCode());
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
    FiltroRicercaDocumentiActaDTO other = (FiltroRicercaDocumentiActaDTO) obj;
    if (aooProtocollante == null) {
      if (other.aooProtocollante != null)
        return false;
    } else if (!aooProtocollante.equals(other.aooProtocollante))
      return false;
    if (dataCronica == null) {
      if (other.dataCronica != null)
        return false;
    } else if (!dataCronica.equals(other.dataCronica))
      return false;
    if (dataRegistrazioneProtocollo == null) {
      if (other.dataRegistrazioneProtocollo != null)
        return false;
    } else if (!dataRegistrazioneProtocollo.equals(other.dataRegistrazioneProtocollo))
      return false;
    if (numeroProtocollo == null) {
      if (other.numeroProtocollo != null)
        return false;
    } else if (!numeroProtocollo.equals(other.numeroProtocollo))
      return false;
    if (numeroRepertorio == null) {
      if (other.numeroRepertorio != null)
        return false;
    } else if (!numeroRepertorio.equals(other.numeroRepertorio))
      return false;
    if (oggetto == null) {
      if (other.oggetto != null)
        return false;
    } else if (!oggetto.equals(other.oggetto))
      return false;
    if (paroleChiave == null) {
      if (other.paroleChiave != null)
        return false;
    } else if (!paroleChiave.equals(other.paroleChiave))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "FiltroRicercaDocumentiActaDTO [oggetto=" + oggetto + ", paroleChiave=" + paroleChiave
        + ", numeroRepertorio=" + numeroRepertorio + ", numeroProtocollo=" + numeroProtocollo
        + ", aooProtocollante=" + aooProtocollante + ", dataCronica=" + dataCronica
        + ", dataRegistrazioneProtocollo=" + dataRegistrazioneProtocollo + "]";
  }

}

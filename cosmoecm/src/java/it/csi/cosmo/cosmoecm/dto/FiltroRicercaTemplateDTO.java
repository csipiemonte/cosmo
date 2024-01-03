/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.dto;

import java.util.Map;

/**
 *
 */

public class FiltroRicercaTemplateDTO {

  private Map<String, String> idEnte;
  private Map<String, Object> codiceTipoPratica;
  private Map<String, Object> codice;

  public Map<String, String> getIdEnte() {
    return idEnte;
  }

  public void setIdEnte(Map<String, String> idEnte) {
    this.idEnte = idEnte;
  }

  public Map<String, Object> getCodiceTipoPratica() {
    return codiceTipoPratica;
  }

  public void setCodiceTipoPratica(Map<String, Object> codiceTipoPratica) {
    this.codiceTipoPratica = codiceTipoPratica;
  }


  public Map<String, Object> getCodice() {
    return codice;
  }

  public void setCodice(Map<String, Object> codice) {
    this.codice = codice;
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((idEnte == null) ? 0 : idEnte.hashCode());
    result = prime * result + ((codiceTipoPratica == null) ? 0 : codiceTipoPratica.hashCode());
    result = prime * result + ((codice == null) ? 0 : codice.hashCode());
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
    FiltroRicercaTemplateDTO other = (FiltroRicercaTemplateDTO) obj;
    if (idEnte == null) {
      if (other.idEnte != null)
        return false;
    } else if (!idEnte.equals(other.idEnte))
      return false;
    if (codiceTipoPratica == null) {
      if (other.codiceTipoPratica != null)
        return false;
    } else if (!codiceTipoPratica.equals(other.codiceTipoPratica))
      return false;
    if (codice == null) {
      if (other.codice != null)
        return false;
    } else if (!codice.equals(other.codice))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "FiltroRicercaDocumentiDTO [ente=" + idEnte + ", tipo=" + codiceTipoPratica + ", codice="
        + codice
        + "]";
  }

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Map;

/**
 *
 */

public class FiltroRicercaDocumentiDTO {

  private Map<String, Object> tipo;
  private Map<String, String> idPratica;
  private Map<String, String> idParent;
  private Map<String, Object> titoloNomeFile;
  private Map<String, Object> nomeFile;
  private Map<String, Object> formato;
  private Map<String, Object> codiceTipoFirma;

  public Map<String, Object> getTipo() {
    return tipo;
  }

  public void setTipo(Map<String, Object> tipo) {
    this.tipo = tipo;
  }

  public Map<String, String> getIdPratica() {
    return idPratica;
  }

  public void setIdPratica(Map<String, String> idPratica) {
    this.idPratica = idPratica;
  }

  public Map<String, String> getIdParent() {
    return idParent;
  }

  public void setIdParent(Map<String, String> idParent) {
    this.idParent = idParent;
  }

  public Map<String, Object> getTitoloNomeFile() {
    return titoloNomeFile;
  }

  public void setTitoloNomeFile(Map<String, Object> titoloNomeFile) {
    this.titoloNomeFile = titoloNomeFile;
  }

  public Map<String, Object> getFormato() {
    return formato;
  }

  public void setFormato(Map<String, Object> formato) {
    this.formato = formato;
  }

  public Map<String, Object> getNomeFile() {
    return nomeFile;
  }

  public void setNomeFile(Map<String, Object> nomeFile) {
    this.nomeFile = nomeFile;
  }

  public Map<String, Object> getCodiceTipoFirma() {
    return codiceTipoFirma;
  }

  public void setCodiceTipoFirma(Map<String, Object> codiceTipoFirma) {
    this.codiceTipoFirma = codiceTipoFirma;
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((formato == null) ? 0 : formato.hashCode());
    result = prime * result + ((idParent == null) ? 0 : idParent.hashCode());
    result = prime * result + ((idPratica == null) ? 0 : idPratica.hashCode());
    result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
    result = prime * result + ((titoloNomeFile == null) ? 0 : titoloNomeFile.hashCode());
    result = prime * result + ((nomeFile == null) ? 0 : nomeFile.hashCode());
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
    FiltroRicercaDocumentiDTO other = (FiltroRicercaDocumentiDTO) obj;
    if (formato == null) {
      if (other.formato != null)
        return false;
    } else if (!formato.equals(other.formato))
      return false;
    if (idParent == null) {
      if (other.idParent != null)
        return false;
    } else if (!idParent.equals(other.idParent))
      return false;
    if (idPratica == null) {
      if (other.idPratica != null)
        return false;
    } else if (!idPratica.equals(other.idPratica))
      return false;
    if (tipo == null) {
      if (other.tipo != null)
        return false;
    } else if (!tipo.equals(other.tipo))
      return false;
    if (titoloNomeFile == null) {
      if (other.titoloNomeFile != null)
        return false;
    } else if (!titoloNomeFile.equals(other.titoloNomeFile))
      return false;
    if (nomeFile == null) {
      if (other.nomeFile != null)
        return false;
    } else if (!nomeFile.equals(other.nomeFile))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "FiltroRicercaDocumentiDTO [tipo=" + tipo + ", idPratica=" + idPratica + ", idParent="
        + idParent + ", titoloNomeFile=" + titoloNomeFile + ", formato=" + formato + ", nomeFile="
        + nomeFile + " ]";
  }




}

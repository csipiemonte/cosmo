/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.dto.rest.ws;

import java.io.Serializable;

/**
 *
 */

public class WebSocketTargetSelector implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -1319001904395511384L;

  private String codiceFiscale;
  private Long idEnte;
  private Long idProfilo;

  public WebSocketTargetSelector() {
    // NOP
  }

  public String getCodiceFiscale() {
    return codiceFiscale;
  }

  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }

  public Long getIdEnte() {
    return idEnte;
  }

  public void setIdEnte(Long idEnte) {
    this.idEnte = idEnte;
  }

  public Long getIdProfilo() {
    return idProfilo;
  }

  public void setIdProfilo(Long idProfilo) {
    this.idProfilo = idProfilo;
  }

  @Override
  public String toString() {
    return "WebSocketTargetSelector [codiceFiscale=" + codiceFiscale + ", idEnte=" + idEnte
        + ", idProfilo=" + idProfilo + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codiceFiscale == null) ? 0 : codiceFiscale.hashCode());
    result = prime * result + ((idEnte == null) ? 0 : idEnte.hashCode());
    result = prime * result + ((idProfilo == null) ? 0 : idProfilo.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    WebSocketTargetSelector other = (WebSocketTargetSelector) obj;
    if (codiceFiscale == null) {
      if (other.codiceFiscale != null) {
        return false;
      }
    } else if (!codiceFiscale.equals(other.codiceFiscale)) {
      return false;
    }
    if (idEnte == null) {
      if (other.idEnte != null) {
        return false;
      }
    } else if (!idEnte.equals(other.idEnte)) {
      return false;
    }
    if (idProfilo == null) {
      if (other.idProfilo != null) {
        return false;
      }
    } else if (!idProfilo.equals(other.idProfilo)) {
      return false;
    }
    return true;
  }

}

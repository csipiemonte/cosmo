/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.dto.jasper;

public class ContestoCreazioneDocumento {

  private String codiceTemplate;

  private Long idEnte;

  private String codiceTipoPratica;

  public ContestoCreazioneDocumento() {
    // Auto-generated constructor stub
  }

  public String getCodiceTemplate() {
    return codiceTemplate;
  }

  public void setCodiceTemplate(String codiceTemplate) {
    this.codiceTemplate = codiceTemplate;
  }

  public Long getIdEnte() {
    return idEnte;
  }

  public void setIdEnte(Long idEnte) {
    this.idEnte = idEnte;
  }

  public String getCodiceTipoPratica() {
    return codiceTipoPratica;
  }

  public void setCodiceTipoPratica(String codiceTipoPratica) {
    this.codiceTipoPratica = codiceTipoPratica;
  }

  @Override
  public String toString() {
    return "ContestoCreazioneDocumento [codiceTemplate=" + codiceTemplate + ", idEnte=" + idEnte
        + ", codiceTipoPratica=" + codiceTipoPratica + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codiceTemplate == null) ? 0 : codiceTemplate.hashCode());
    result = prime * result + ((codiceTipoPratica == null) ? 0 : codiceTipoPratica.hashCode());
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
    ContestoCreazioneDocumento other = (ContestoCreazioneDocumento) obj;
    if (codiceTemplate == null) {
      if (other.codiceTemplate != null)
        return false;
    } else if (!codiceTemplate.equals(other.codiceTemplate))
      return false;
    if (codiceTipoPratica == null) {
      if (other.codiceTipoPratica != null)
        return false;
    } else if (!codiceTipoPratica.equals(other.codiceTipoPratica))
      return false;
    if (idEnte == null) {
      if (other.idEnte != null)
        return false;
    } else if (!idEnte.equals(other.idEnte))
      return false;
    return true;
  }

}

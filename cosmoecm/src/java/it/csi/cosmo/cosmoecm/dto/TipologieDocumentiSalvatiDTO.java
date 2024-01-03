/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import it.csi.cosmo.cosmoecm.dto.rest.VerificaTipologiaDocumentiSalvati;

/**
 *
 */

public class TipologieDocumentiSalvatiDTO implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private Long idPratica;
  private Boolean daFirmare;
  private LocalDateTime creationTime;
  private Boolean verificaDataDocObbligatori;
  private List<VerificaTipologiaDocumentiSalvati> tipologieDocumenti;

  public Long getIdPratica() {
    return idPratica;
  }

  public void setIdPratica(Long idPratica) {
    this.idPratica = idPratica;
  }

  public Boolean getDaFirmare() {
    return daFirmare;
  }

  public void setDaFirmare(Boolean daFirmare) {
    this.daFirmare = daFirmare;
  }

  public LocalDateTime getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(LocalDateTime creationTime) {
    this.creationTime = creationTime;
  }

  public List<VerificaTipologiaDocumentiSalvati> getTipologieDocumenti() {
    return tipologieDocumenti;
  }

  public void setTipologieDocumenti(List<VerificaTipologiaDocumentiSalvati> tipologieDocumenti) {
    this.tipologieDocumenti = tipologieDocumenti;
  }

  public Boolean getVerificaDataDocObbligatori() {
    return verificaDataDocObbligatori;
  }

  public void setVerificaDataDocObbligatori(Boolean verificaDataDocObbligatori) {
    this.verificaDataDocObbligatori = verificaDataDocObbligatori;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((creationTime == null) ? 0 : creationTime.hashCode());
    result = prime * result + ((daFirmare == null) ? 0 : daFirmare.hashCode());
    result = prime * result + ((idPratica == null) ? 0 : idPratica.hashCode());
    result = prime * result + ((tipologieDocumenti == null) ? 0 : tipologieDocumenti.hashCode());
    result =
        prime * result
        + ((verificaDataDocObbligatori == null) ? 0 : verificaDataDocObbligatori.hashCode());
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
    TipologieDocumentiSalvatiDTO other = (TipologieDocumentiSalvatiDTO) obj;
    if (creationTime == null) {
      if (other.creationTime != null)
        return false;
    } else if (!creationTime.equals(other.creationTime))
      return false;
    if (daFirmare == null) {
      if (other.daFirmare != null)
        return false;
    } else if (!daFirmare.equals(other.daFirmare))
      return false;
    if (idPratica == null) {
      if (other.idPratica != null)
        return false;
    } else if (!idPratica.equals(other.idPratica))
      return false;
    if (tipologieDocumenti == null) {
      if (other.tipologieDocumenti != null)
        return false;
    } else if (!tipologieDocumenti.equals(other.tipologieDocumenti))
      return false;
    if (verificaDataDocObbligatori == null) {
      if (other.verificaDataDocObbligatori != null)
        return false;
    } else if (!verificaDataDocObbligatori.equals(other.verificaDataDocObbligatori))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "TipologieDocumentiSalvatiDTO [idPratica=" + idPratica + ", daFirmare=" + daFirmare
        + ", creationTime=" + creationTime + ", verificaDataDocObbligatori="
        + verificaDataDocObbligatori
        + " tipologieDocumenti=" + tipologieDocumenti + "]";
  }

}

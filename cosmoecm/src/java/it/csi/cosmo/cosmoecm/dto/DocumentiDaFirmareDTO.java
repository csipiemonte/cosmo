/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 */

public class DocumentiDaFirmareDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Boolean tutti;

  private LocalDateTime creationTime;

  private List<String> tipologieDocumenti;

  public Boolean getTutti() {
    return tutti;
  }

  public void setTutti(Boolean tutti) {
    this.tutti = tutti;
  }

  public LocalDateTime getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(LocalDateTime creationTime) {
    this.creationTime = creationTime;
  }

  public List<String> getTipologieDocumenti() {
    return tipologieDocumenti;
  }

  public void setTipologieDocumenti(List<String> tipologieDocumenti) {
    this.tipologieDocumenti = tipologieDocumenti;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((creationTime == null) ? 0 : creationTime.hashCode());
    result = prime * result + ((tipologieDocumenti == null) ? 0 : tipologieDocumenti.hashCode());
    result = prime * result + ((tutti == null) ? 0 : tutti.hashCode());
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
    DocumentiDaFirmareDTO other = (DocumentiDaFirmareDTO) obj;
    if (creationTime == null) {
      if (other.creationTime != null)
        return false;
    } else if (!creationTime.equals(other.creationTime))
      return false;
    if (tipologieDocumenti == null) {
      if (other.tipologieDocumenti != null)
        return false;
    } else if (!tipologieDocumenti.equals(other.tipologieDocumenti))
      return false;
    if (tutti == null) {
      if (other.tutti != null)
        return false;
    } else if (!tutti.equals(other.tutti))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "DocumentiDaFirmareDTO [tutti=" + tutti + ", creationTime=" + creationTime
        + ", tipologieDocumenti=" + tipologieDocumenti + "]";
  }

}

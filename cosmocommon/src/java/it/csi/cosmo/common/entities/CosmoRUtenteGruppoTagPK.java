/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the cosmo_r_utente_gruppo_tag database table.
 */
@Embeddable
public class CosmoRUtenteGruppoTagPK implements Serializable {

  // default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "id_utente_gruppo", insertable = false, updatable = false, nullable = false)
  private Long idUtenteGruppo;

  @Column(name = "id_tag", insertable = false, updatable = false, nullable = false)
  private Long idTag;

  public CosmoRUtenteGruppoTagPK() {
    // Empty constructor
  }

  public Long getIdUtenteGruppo() {
    return this.getIdUtenteGruppo();
  }

  public void setIdUtenteGruppo(Long idUtenteGruppo) {
    this.idUtenteGruppo = idUtenteGruppo;
  }

  public Long getIdTag() {
    return this.getIdTag();
  }

  public void setIdTag(Long idTag) {
    this.idTag = idTag;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof CosmoRUtenteGruppoTagPK)) {
      return false;
    }
    CosmoRUtenteGruppoTagPK castOther = (CosmoRUtenteGruppoTagPK) other;
    return this.idUtenteGruppo.equals(castOther.idUtenteGruppo)
        && this.idTag.equals(castOther.idTag);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.idUtenteGruppo.hashCode();
    hash = hash * prime + this.idTag.hashCode();

    return hash;
  }
  
  @Override
  public String toString() {
    return "CosmoRUtenteEntePK [" + (idUtenteGruppo != null ? "idUtenteGruppo=" + idUtenteGruppo + ", " : "")
        + (idTag != null ? "idTag=" + idTag : "") + "]";
  }


}

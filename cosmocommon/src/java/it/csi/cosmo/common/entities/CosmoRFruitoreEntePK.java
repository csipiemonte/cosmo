/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the cosmo_r_fruitore_ente database table.
 *
 */
@Embeddable
public class CosmoRFruitoreEntePK implements Serializable {
  // default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "id_ente", insertable = false, updatable = false)
  private Long idEnte;

  @Column(name = "id_fruitore", insertable = false, updatable = false)
  private Long idFruitore;

  public CosmoRFruitoreEntePK() {
    // empty constructor
  }

  public Long getIdEnte() {
    return this.idEnte;
  }

  public void setIdEnte(Long idEnte) {
    this.idEnte = idEnte;
  }

  public Long getIdFruitore() {
    return this.idFruitore;
  }

  public void setIdFruitore(Long idFruitore) {
    this.idFruitore = idFruitore;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof CosmoRFruitoreEntePK)) {
      return false;
    }
    CosmoRFruitoreEntePK castOther = (CosmoRFruitoreEntePK) other;
    return this.idEnte.equals(castOther.idEnte) && this.idFruitore.equals(castOther.idFruitore);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.idEnte.hashCode();
    hash = hash * prime + this.idFruitore.hashCode();

    return hash;
  }

  @Override
  public String toString() {
    return "CosmoRFruitoreEntePK [" + (idEnte != null ? "idEnte=" + idEnte + ", " : "")
        + (idFruitore != null ? "idFruitore=" + idFruitore : "") + "]";
  }

}

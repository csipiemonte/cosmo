/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoREntity;


/**
 * The persistent class for the cosmo_r_fruitore_ente database table.
 *
 */
@Entity
@Table(name = "cosmo_r_fruitore_ente")
@NamedQuery(name = "CosmoRFruitoreEnte.findAll", query = "SELECT c FROM CosmoRFruitoreEnte c")
public class CosmoRFruitoreEnte extends CosmoREntity {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private CosmoRFruitoreEntePK id;

  // bi-directional many-to-one association to CosmoTEnte
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_ente", nullable = false, insertable = false, updatable = false)
  private CosmoTEnte cosmoTEnte;

  // bi-directional many-to-one association to CosmoTFruitore
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_fruitore", nullable = false, insertable = false, updatable = false)
  private CosmoTFruitore cosmoTFruitore;

  public CosmoRFruitoreEnte() {
    // empty constructor
  }

  public CosmoRFruitoreEntePK getId() {
    return this.id;
  }

  public void setId(CosmoRFruitoreEntePK id) {
    this.id = id;
  }

  public CosmoTEnte getCosmoTEnte() {
    return this.cosmoTEnte;
  }

  public void setCosmoTEnte(CosmoTEnte cosmoTEnte) {
    this.cosmoTEnte = cosmoTEnte;
  }

  public CosmoTFruitore getCosmoTFruitore() {
    return this.cosmoTFruitore;
  }

  public void setCosmoTFruitore(CosmoTFruitore cosmoTFruitore) {
    this.cosmoTFruitore = cosmoTFruitore;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    CosmoRFruitoreEnte other = (CosmoRFruitoreEnte) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CosmoRFruitoreEnte [" + (id != null ? "id=" + id + ", " : "") + "]";
  }

}

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
 * The persistent class for the cosmo_r_gruppo_tipo_pratica database table.
 *
 */
@Entity
@Table(name = "cosmo_r_gruppo_tipo_pratica")
@NamedQuery(name = "CosmoRGruppoTipoPratica.findAll",
query = "SELECT c FROM CosmoRGruppoTipoPratica c")
public class CosmoRGruppoTipoPratica extends CosmoREntity {
  private static final long serialVersionUID = 1L;


  @EmbeddedId
  private CosmoRGruppoTipoPraticaPK id;

  // bi-directional many-to-one association to CosmoDTipoPratica
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_tipo_pratica", nullable = false, insertable = false, updatable = false)
  private CosmoDTipoPratica cosmoDTipoPratica;

  // bi-directional many-to-one association to CosmoTGruppo
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_gruppo", nullable = false, insertable = false, updatable = false)
  private CosmoTGruppo cosmoTGruppo;

  private Boolean creatore;

  private Boolean supervisore;

  public CosmoRGruppoTipoPratica() {
    // empty constructor
  }

  public CosmoRGruppoTipoPraticaPK getId() {
    return this.id;
  }

  public void setId(CosmoRGruppoTipoPraticaPK id) {
    this.id = id;
  }

  public Boolean getCreatore() {
    return creatore;
  }

  public void setCreatore(Boolean creatore) {
    this.creatore = creatore;
  }

  public Boolean getSupervisore() {
    return supervisore;
  }

  public void setSupervisore(Boolean supervisore) {
    this.supervisore = supervisore;
  }

  public CosmoDTipoPratica getCosmoDTipoPratica() {
    return this.cosmoDTipoPratica;
  }

  public void setCosmoDTipoPratica(CosmoDTipoPratica cosmoDTipoPratica) {
    this.cosmoDTipoPratica = cosmoDTipoPratica;
  }

  public CosmoTGruppo getCosmoTGruppo() {
    return this.cosmoTGruppo;
  }

  public void setCosmoTGruppo(CosmoTGruppo cosmoTGruppo) {
    this.cosmoTGruppo = cosmoTGruppo;
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
    CosmoRGruppoTipoPratica other = (CosmoRGruppoTipoPratica) obj;
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
    return "CosmoRGruppoTipoPratica [" + (id != null ? "id=" + id + ", " : "") + "]";
  }
}

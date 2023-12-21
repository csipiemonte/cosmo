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
 * The persistent class for the cosmo_r_tipodoc_tipopratica database table.
 *
 */
@Entity
@Table(name = "cosmo_r_tipodoc_tipopratica")
@NamedQuery(name = "CosmoRTipodocTipopratica.findAll",
query = "SELECT c FROM CosmoRTipodocTipopratica c")
public class CosmoRTipodocTipopratica extends CosmoREntity {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private CosmoRTipodocTipopraticaPK id;

  // bi-directional many-to-one association to CosmoDTipoPratica
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_tipo_pratica", nullable = false, insertable = false, updatable = false)
  private CosmoDTipoPratica cosmoDTipoPratica;

  // bi-directional many-to-one association to CosmoDTipoDocumento
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_tipo_documento", nullable = false, insertable = false,
  updatable = false)
  private CosmoDTipoDocumento cosmoDTipoDocumento;

  public CosmoRTipodocTipopratica() {
    // empty constructor
  }

  public CosmoRTipodocTipopraticaPK getId() {
    return this.id;
  }

  public void setId(CosmoRTipodocTipopraticaPK id) {
    this.id = id;
  }

  public CosmoDTipoPratica getCosmoDTipoPratica() {
    return this.cosmoDTipoPratica;
  }

  public void setCosmoDTipoPratica(CosmoDTipoPratica cosmoDTipoPratica) {
    this.cosmoDTipoPratica = cosmoDTipoPratica;
  }

  public CosmoDTipoDocumento getCosmoDTipoDocumento() {
    return this.cosmoDTipoDocumento;
  }

  public void setCosmoDTipoDocumento(CosmoDTipoDocumento cosmoDTipoDocumento) {
    this.cosmoDTipoDocumento = cosmoDTipoDocumento;
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
    CosmoRTipodocTipopratica other = (CosmoRTipodocTipopratica) obj;
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
    return "CosmoRTipodocTipopratica [" + (id != null ? "id=" + id + ", " : "") + "]";
  }

}

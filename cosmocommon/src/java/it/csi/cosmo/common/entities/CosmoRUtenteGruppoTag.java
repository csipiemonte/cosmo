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
 * The persistent class for the cosmo_r_utente_gruppo_tag database table.
 *
 */
@Entity
@Table(name = "cosmo_r_utente_gruppo_tag")
@NamedQuery(name = "CosmoRUtenteGruppoTag.findAll", query = "SELECT c FROM CosmoRUtenteGruppoTag c")
public class CosmoRUtenteGruppoTag extends CosmoREntity {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private CosmoRUtenteGruppoTagPK id;

  // bi-directional many-to-one association to CosmoTTag
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_tag", nullable = false, insertable = false, updatable = false)
  private CosmoTTag cosmoTTag;

  // bi-directional many-to-one association to CosmoTUtenteGruppo
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_utente_gruppo", nullable = false, insertable = false, updatable = false)
  private CosmoTUtenteGruppo cosmoTUtenteGruppo;

  public CosmoRUtenteGruppoTagPK getId() {
    return this.id;
  }

  public void setId(CosmoRUtenteGruppoTagPK id) {
    this.id = id;
  }

  public CosmoRUtenteGruppoTag() {
    // Empty constructor
  }

  public CosmoTTag getCosmoTTag() {
    return this.cosmoTTag;
  }

  public void setCosmoTTag(CosmoTTag cosmoTTag) {
    this.cosmoTTag = cosmoTTag;
  }

  public CosmoTUtenteGruppo getCosmoTUtenteGruppo() {
    return this.cosmoTUtenteGruppo;
  }

  public void setCosmoTUtenteGruppo(CosmoTUtenteGruppo cosmoTUtenteGruppo) {
    this.cosmoTUtenteGruppo = cosmoTUtenteGruppo;
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
    CosmoRUtenteGruppoTag other = (CosmoRUtenteGruppoTag) obj;
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
    return "CosmoRUtenteGruppoTag [" + (id != null ? "id=" + id + ", " : "") + "]";
  }

}

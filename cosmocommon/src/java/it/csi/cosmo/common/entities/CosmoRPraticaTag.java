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
 * The persistent class for the cosmo_r_pratica_tag database table.
 *
 */
@Entity
@Table(name = "cosmo_r_pratica_tag")
@NamedQuery(name = "CosmoRPraticaTag.findAll", query = "SELECT c FROM CosmoRPraticaTag c")
public class CosmoRPraticaTag extends CosmoREntity {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private CosmoRPraticaTagPK id;

  // bi-directional many-to-one association to CosmoTPratica
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_pratica", nullable = false, insertable = false, updatable = false)
  private CosmoTPratica cosmoTPratica;

  // bi-directional many-to-one association to CosmoTTag
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_tag", nullable = false, insertable = false, updatable = false)
  private CosmoTTag cosmoTTag;

  public CosmoRPraticaTag() {
    // empty constructor
  }

  public CosmoRPraticaTagPK getId() {
    return this.id;
  }

  public void setId(CosmoRPraticaTagPK id) {
    this.id = id;
  }
  public CosmoTPratica getCosmoTPratica() {
    return this.cosmoTPratica;
  }

  public void setCosmoTPratica(CosmoTPratica cosmoTPratica) {
    this.cosmoTPratica = cosmoTPratica;
  }

  public CosmoTTag getCosmoTTag() {
    return this.cosmoTTag;
  }

  public void setCosmoTTag(CosmoTTag cosmoTTag) {
    this.cosmoTTag = cosmoTTag;
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
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CosmoRPraticaTag other = (CosmoRPraticaTag) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoRPraticaTag [id=" + id + "]";
  }

}

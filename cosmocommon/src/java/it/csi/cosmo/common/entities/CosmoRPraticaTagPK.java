/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the cosmo_r_pratica_tag database table.
 *
 */
@Embeddable
public class CosmoRPraticaTagPK implements Serializable {

  // default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "id_pratica", insertable = false, updatable = false, nullable = false)
  private Long idPratica;

  @Column(name = "id_tag", insertable = false, updatable = false, nullable = false)
  private Long idTag;

  public CosmoRPraticaTagPK() {
    // Empty constructor
  }

  public Long getIdPratica() {
    return this.getIdPratica();
  }

  public void setIdPratica(Long idPratica) {
    this.idPratica = idPratica;
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
    if (!(other instanceof CosmoRPraticaTagPK)) {
      return false;
    }
    CosmoRPraticaTagPK castOther = (CosmoRPraticaTagPK) other;
    return
    this.idPratica.equals(castOther.idPratica) && this.idTag.equals(castOther.idTag);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.idPratica.hashCode();
    hash = hash * prime + this.idTag.hashCode();

    return hash;
  }


}

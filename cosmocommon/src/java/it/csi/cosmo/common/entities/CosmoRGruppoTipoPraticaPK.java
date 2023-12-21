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
public class CosmoRGruppoTipoPraticaPK implements Serializable {
  // default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  public String getPrimaryKeyRepresentation() {
    return String.valueOf(
        "{\"id_gruppo\"=" + idGruppo + ", \"codice_tipo_pratica\"=" + codiceTipoPratica + "\"}");
  }
  @Column(name = "id_gruppo", insertable = false, updatable = false)
  private Long idGruppo;

  @Column(name = "codice_tipo_pratica", insertable = false, updatable = false, length = 255)
  private String codiceTipoPratica;

  public CosmoRGruppoTipoPraticaPK() {
    // empty constructor
  }

  public Long getIdGruppo() {
    return idGruppo;
  }

  public void setIdGruppo(Long idGruppo) {
    this.idGruppo = idGruppo;
  }


  public String getCodiceTipoPratica() {
    return codiceTipoPratica;
  }

  public void setCodiceTipoPratica(String codiceTipoPratica) {
    this.codiceTipoPratica = codiceTipoPratica;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof CosmoRGruppoTipoPraticaPK)) {
      return false;
    }
    CosmoRGruppoTipoPraticaPK castOther = (CosmoRGruppoTipoPraticaPK) other;
    return this.idGruppo.equals(castOther.idGruppo)
        && this.codiceTipoPratica.equals(castOther.codiceTipoPratica);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.idGruppo.hashCode();
    hash = hash * prime + this.codiceTipoPratica.hashCode();

    return hash;
  }

  @Override
  public String toString() {
    return "CosmoRGruppoPraticaPK [" + (idGruppo != null ? "idGruppo=" + idGruppo + ", " : "")
        + (codiceTipoPratica != null ? "codiceTipoPratica=" + codiceTipoPratica : "") + "]";
  }

}

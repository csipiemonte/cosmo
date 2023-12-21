/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.sql.Timestamp;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoREntity;


/**
 * The persistent class for the cosmo_r_pratica_pratica database table.
 *
 */
@Entity
@Table(name="cosmo_r_pratica_pratica")
@NamedQuery(name="CosmoRPraticaPratica.findAll", query="SELECT c FROM CosmoRPraticaPratica c")
public class CosmoRPraticaPratica extends CosmoREntity {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private CosmoRPraticaPraticaPK id;

  //bi-directional many-to-one association to CosmoDTipoRelazionePratica
  @ManyToOne
  @JoinColumn(name = "codice_tipo_relazione", nullable = false, insertable = false,
  updatable = false)
  private CosmoDTipoRelazionePratica cosmoDTipoRelazionePratica;

  //bi-directional many-to-one association to CosmoTPratica
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_pratica_a", nullable = false, insertable = false, updatable = false)
  private CosmoTPratica cosmoTPraticaA;

  //bi-directional many-to-one association to CosmoTPratica
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_pratica_da", nullable = false, insertable = false, updatable = false)
  private CosmoTPratica cosmoTPraticaDa;

  public CosmoRPraticaPratica() {
    // empty constructor
  }

  public CosmoRPraticaPraticaPK getId() {
    return this.id;
  }

  public void setId(CosmoRPraticaPraticaPK id) {
    this.id = id;
  }

  @Override
  public Timestamp getDtFineVal() {
    return this.dtFineVal;
  }

  @Override
  public void setDtFineVal(Timestamp dtFineVal) {
    this.dtFineVal = dtFineVal;
  }

  @Override
  public Timestamp getDtInizioVal() {
    return this.dtInizioVal;
  }

  @Override
  public void setDtInizioVal(Timestamp dtInizioVal) {
    this.dtInizioVal = dtInizioVal;
  }

  public CosmoDTipoRelazionePratica getCosmoDTipoRelazionePratica() {
    return this.cosmoDTipoRelazionePratica;
  }

  public void setCosmoDTipoRelazionePratica(CosmoDTipoRelazionePratica cosmoDTipoRelazionePratica) {
    this.cosmoDTipoRelazionePratica = cosmoDTipoRelazionePratica;
  }

  public CosmoTPratica getCosmoTPraticaA() {
    return this.cosmoTPraticaA;
  }

  public void setCosmoTPraticaA(CosmoTPratica cosmoTPraticaA) {
    this.cosmoTPraticaA = cosmoTPraticaA;
  }

  public CosmoTPratica getCosmoTPraticaDa() {
    return this.cosmoTPraticaDa;
  }

  public void setCosmoTPraticaDa(CosmoTPratica cosmoTPraticaDa) {
    this.cosmoTPraticaDa = cosmoTPraticaDa;
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
    CosmoRPraticaPratica other = (CosmoRPraticaPratica) obj;
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
    return "CosmoRPraticaPratica [" + (id != null ? "id=" + id + ", " : "") + "]";
  }
}

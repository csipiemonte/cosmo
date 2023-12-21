/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.sql.Timestamp;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoREntity;


/**
 * The persistent class for the cosmo_r_ente_funzionalita_applicazione_esterna database table.
 *
 */
@Entity
@Table(name="cosmo_r_ente_funzionalita_applicazione_esterna")
@NamedQuery(name="CosmoREnteFunzionalitaApplicazioneEsterna.findAll", query="SELECT c FROM CosmoREnteFunzionalitaApplicazioneEsterna c")
public class CosmoREnteFunzionalitaApplicazioneEsterna extends CosmoREntity {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private CosmoREnteFunzionalitaApplicazioneEsternaPK id;

  //bi-directional many-to-one association to CosmoTEnte
  @ManyToOne
  @JoinColumn(name = "id_ente", nullable = false, insertable = false, updatable = false)
  private CosmoTEnte cosmoTEnte;

  //bi-directional many-to-one association to CosmoTFunzionalitaApplicazioneEsterna
  @ManyToOne
  @JoinColumn(name = "id_funzionalita_applicazione_esterna", nullable = false, insertable = false,
  updatable = false)
  private CosmoTFunzionalitaApplicazioneEsterna cosmoTFunzionalitaApplicazioneEsterna;

  public CosmoREnteFunzionalitaApplicazioneEsterna() {
    // NOP
  }

  public CosmoREnteFunzionalitaApplicazioneEsternaPK getId() {
    return this.id;
  }

  public void setId(CosmoREnteFunzionalitaApplicazioneEsternaPK id) {
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

  public CosmoTEnte getCosmoTEnte() {
    return this.cosmoTEnte;
  }

  public void setCosmoTEnte(CosmoTEnte cosmoTEnte) {
    this.cosmoTEnte = cosmoTEnte;
  }

  public CosmoTFunzionalitaApplicazioneEsterna getCosmoTFunzionalitaApplicazioneEsterna() {
    return this.cosmoTFunzionalitaApplicazioneEsterna;
  }

  public void setCosmoTFunzionalitaApplicazioneEsterna(CosmoTFunzionalitaApplicazioneEsterna cosmoTFunzionalitaApplicazioneEsterna) {
    this.cosmoTFunzionalitaApplicazioneEsterna = cosmoTFunzionalitaApplicazioneEsterna;
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
    CosmoREnteFunzionalitaApplicazioneEsterna other =
        (CosmoREnteFunzionalitaApplicazioneEsterna) obj;
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
    return "CosmoREnteFunzionalitaApplicazioneEsterna [" + (id != null ? "id=" + id : "") + "]";
  }


}

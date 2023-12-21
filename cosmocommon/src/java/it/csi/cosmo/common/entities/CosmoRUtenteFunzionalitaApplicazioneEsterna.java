/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoREntity;


/**
 * The persistent class for the cosmo_r_utente_funzionalita_applicazione_esterna database table.
 *
 */
@Entity
@Table(name="cosmo_r_utente_funzionalita_applicazione_esterna")
@NamedQuery(name="CosmoRUtenteFunzionalitaApplicazioneEsterna.findAll", query="SELECT c FROM CosmoRUtenteFunzionalitaApplicazioneEsterna c")
public class CosmoRUtenteFunzionalitaApplicazioneEsterna extends CosmoREntity {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private CosmoRUtenteFunzionalitaApplicazioneEsternaPK id;

  //bi-directional many-to-one association to CosmoTFunzionalitaApplicazioneEsterna
  @ManyToOne
  @JoinColumn(name = "id_funzionalita_applicazione_esterna", nullable = false, insertable = false,
  updatable = false)
  private CosmoTFunzionalitaApplicazioneEsterna cosmoTFunzionalitaApplicazioneEsterna;

  //bi-directional many-to-one association to CosmoTUtente
  @ManyToOne
  @JoinColumn(name = "id_utente", nullable = false, insertable = false, updatable = false)
  private CosmoTUtente cosmoTUtente;

  private Integer posizione;

  public CosmoRUtenteFunzionalitaApplicazioneEsterna() {
    // NOP
  }

  public CosmoRUtenteFunzionalitaApplicazioneEsternaPK getId() {
    return this.id;
  }

  public void setId(CosmoRUtenteFunzionalitaApplicazioneEsternaPK id) {
    this.id = id;
  }

  public CosmoTFunzionalitaApplicazioneEsterna getCosmoTFunzionalitaApplicazioneEsterna() {
    return this.cosmoTFunzionalitaApplicazioneEsterna;
  }

  public void setCosmoTFunzionalitaApplicazioneEsterna(CosmoTFunzionalitaApplicazioneEsterna cosmoTFunzionalitaApplicazioneEsterna) {
    this.cosmoTFunzionalitaApplicazioneEsterna = cosmoTFunzionalitaApplicazioneEsterna;
  }

  public CosmoTUtente getCosmoTUtente() {
    return this.cosmoTUtente;
  }

  public void setCosmoTUtente(CosmoTUtente cosmoTUtente) {
    this.cosmoTUtente = cosmoTUtente;
  }

  public Integer getPosizione() {
    return this.posizione;
  }

  public void setPosizione(Integer posizione) {
    this.posizione = posizione;
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
    CosmoRUtenteFunzionalitaApplicazioneEsterna other =
        (CosmoRUtenteFunzionalitaApplicazioneEsterna) obj;
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
    return "CosmoRUtenteFunzionalitaApplicazioneEsterna [" + (id != null ? "id=" + id : "") + "]";
  }


}

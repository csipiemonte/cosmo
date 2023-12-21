/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the cosmo_r_ente_funzionalita_applicazione_esterna database table.
 *
 */
@Embeddable
public class CosmoREnteFunzionalitaApplicazioneEsternaPK implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name="id_ente", insertable=false, updatable=false)
  private Long idEnte;

  @Column(name="id_funzionalita_applicazione_esterna", insertable=false, updatable=false)
  private Long idFunzionalitaApplicazioneEsterna;

  public CosmoREnteFunzionalitaApplicazioneEsternaPK() {
      // NOP
  }
  public Long getIdEnte() {
    return this.idEnte;
  }
  public void setIdEnte(Long idEnte) {
    this.idEnte = idEnte;
  }
  public Long getIdFunzionalitaApplicazioneEsterna() {
    return this.idFunzionalitaApplicazioneEsterna;
  }
  public void setIdFunzionalitaApplicazioneEsterna(Long idFunzionalitaApplicazioneEsterna) {
    this.idFunzionalitaApplicazioneEsterna = idFunzionalitaApplicazioneEsterna;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof CosmoREnteFunzionalitaApplicazioneEsternaPK)) {
      return false;
    }
    CosmoREnteFunzionalitaApplicazioneEsternaPK castOther = (CosmoREnteFunzionalitaApplicazioneEsternaPK)other;
    return
        this.idEnte.equals(castOther.idEnte)
        && this.idFunzionalitaApplicazioneEsterna.equals(castOther.idFunzionalitaApplicazioneEsterna);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.idEnte.hashCode();
    hash = hash * prime + this.idFunzionalitaApplicazioneEsterna.hashCode();

    return hash;
  }

    @Override
    public String toString() {
      return "CosmoREnteFunzionalitaApplicazioneEsternaPK ["
          + (idEnte != null ? "idEnte=" + idEnte + ", " : "")
          + (idFunzionalitaApplicazioneEsterna != null
              ? "idFunzionalitaApplicazioneEsterna=" + idFunzionalitaApplicazioneEsterna
              : "")
          + "]";
    }
}

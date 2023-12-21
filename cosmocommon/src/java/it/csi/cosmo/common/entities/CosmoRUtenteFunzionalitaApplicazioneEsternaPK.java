/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the cosmo_r_utente_funzionalita_applicazione_esterna database table.
 *
 */
@Embeddable
public class CosmoRUtenteFunzionalitaApplicazioneEsternaPK implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name="id_utente", insertable=false, updatable=false)
  private Long idUtente;

  @Column(name="id_funzionalita_applicazione_esterna", insertable=false, updatable=false)
  private Long idFunzionalitaApplicazioneEsterna;

  public CosmoRUtenteFunzionalitaApplicazioneEsternaPK() {
    // NOP
  }
  public Long getIdUtente() {
    return this.idUtente;
  }
  public void setIdUtente(Long idUtente) {
    this.idUtente = idUtente;
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
    if (!(other instanceof CosmoRUtenteFunzionalitaApplicazioneEsternaPK)) {
      return false;
    }
    CosmoRUtenteFunzionalitaApplicazioneEsternaPK castOther = (CosmoRUtenteFunzionalitaApplicazioneEsternaPK)other;
    return
        this.idUtente.equals(castOther.idUtente)
        && this.idFunzionalitaApplicazioneEsterna.equals(castOther.idFunzionalitaApplicazioneEsterna);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.idUtente.hashCode();
    hash = hash * prime + this.idFunzionalitaApplicazioneEsterna.hashCode();

    return hash;
  }

  @Override
  public String toString() {
    return "CosmoRUtenteFunzionalitaApplicazioneEsternaPK ["
        + (idUtente != null ? "idUtente=" + idUtente + ", " : "")
        + (idFunzionalitaApplicazioneEsterna != null
            ? "idFunzionalitaApplicazioneEsterna=" + idFunzionalitaApplicazioneEsterna
            : "")
        + "]";
  }

}

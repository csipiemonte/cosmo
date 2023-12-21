/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the cosmo_r_notifica_utente_ente database table.
 *
 */
@Embeddable
public class CosmoRNotificaUtenteEntePK implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name="id_utente", insertable=false, updatable=false)
  private Long idUtente;

  @Column(name="id_notifica", insertable=false, updatable=false)
  private Long idNotifica;

  @Column(name = "id_ente", insertable = false, updatable = false)
  private Long idEnte;

  public CosmoRNotificaUtenteEntePK() {
    // empty constructor
  }
  public Long getIdUtente() {
    return this.idUtente;
  }
  public void setIdUtente(Long idUtente) {
    this.idUtente = idUtente;
  }
  public Long getIdNotifica() {
    return this.idNotifica;
  }
  public void setIdNotifica(Long idNotifica) {
    this.idNotifica = idNotifica;
  }

  public Long getIdEnte() {
    return this.idEnte;
  }

  public void setIdEnte(Long idEnte) {
    this.idEnte = idEnte;
  }
  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof CosmoRNotificaUtenteEntePK)) {
      return false;
    }
    CosmoRNotificaUtenteEntePK castOther = (CosmoRNotificaUtenteEntePK)other;
    return
        this.idUtente.equals(castOther.idUtente)
            && this.idNotifica.equals(castOther.idNotifica) && this.idEnte.equals(castOther.idEnte);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.idUtente.hashCode();
    hash = hash * prime + this.idNotifica.hashCode();
    hash = hash * prime + this.idEnte.hashCode();

    return hash;
  }

  @Override
  public String toString() {
    return "CosmoRNotificaUtenteEntePK [" + (idUtente != null ? "idUtente=" + idUtente + ", " : "")
        + (idNotifica != null ? "idNotifica=" + idNotifica : "") + "]";
  }
}

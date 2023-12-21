/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the cosmo_r_utente_ente database table.
 *
 */
@Embeddable
public class CosmoRUtenteEntePK implements Serializable {
  // default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "id_ente", insertable = false, updatable = false, unique = true, nullable = false)
  private Long idEnte;

  @Column(name = "id_utente", insertable = false, updatable = false, unique = true,
      nullable = false)
  private Long idUtente;

  public CosmoRUtenteEntePK() {
    // empty constructor
  }

  public Long getIdEnte() {
    return this.idEnte;
  }

  public void setIdEnte(Long idEnte) {
    this.idEnte = idEnte;
  }

  public Long getIdUtente() {
    return this.idUtente;
  }

  public void setIdUtente(Long idUtente) {
    this.idUtente = idUtente;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof CosmoRUtenteEntePK)) {
      return false;
    }
    CosmoRUtenteEntePK castOther = (CosmoRUtenteEntePK) other;
    return this.idEnte.equals(castOther.idEnte) && this.idUtente.equals(castOther.idUtente);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.idEnte.hashCode();
    hash = hash * prime + this.idUtente.hashCode();

    return hash;
  }

  @Override
  public String toString() {
    return "CosmoRUtenteEntePK [" + (idEnte != null ? "idEnte=" + idEnte + ", " : "")
        + (idUtente != null ? "idUtente=" + idUtente : "") + "]";
  }
}

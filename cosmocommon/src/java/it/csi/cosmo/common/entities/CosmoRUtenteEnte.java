/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoREntity;


/**
 * The persistent class for the cosmo_r_utente_ente database table.
 *
 */
@Entity
@Table(name = "cosmo_r_utente_ente")
@NamedQuery(name = "CosmoRUtenteEnte.findAll", query = "SELECT c FROM CosmoRUtenteEnte c")
public class CosmoRUtenteEnte extends CosmoREntity {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private CosmoRUtenteEntePK id;

  @Column(length = 255)
  private String email;

  @Column(length = 100)
  private String telefono;

  // bi-directional many-to-one association to CosmoTEnte
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_ente", nullable = false, insertable = false, updatable = false)
  private CosmoTEnte cosmoTEnte;

  // bi-directional many-to-one association to CosmoTUtente
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_utente", nullable = false, insertable = false, updatable = false)
  private CosmoTUtente cosmoTUtente;

  public CosmoRUtenteEnte() {
    // empty constructor
  }

  public CosmoRUtenteEntePK getId() {
    return this.id;
  }

  public void setId(CosmoRUtenteEntePK id) {
    this.id = id;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelefono() {
    return this.telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public CosmoTEnte getCosmoTEnte() {
    return this.cosmoTEnte;
  }

  public void setCosmoTEnte(CosmoTEnte cosmoTEnte) {
    this.cosmoTEnte = cosmoTEnte;
  }

  public CosmoTUtente getCosmoTUtente() {
    return this.cosmoTUtente;
  }

  public void setCosmoTUtente(CosmoTUtente cosmoTUtente) {
    this.cosmoTUtente = cosmoTUtente;
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
    CosmoRUtenteEnte other = (CosmoRUtenteEnte) obj;
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
    return "CosmoRUtenteEnte [" + (id != null ? "id=" + id + ", " : "")
        + (email != null ? "email=" + email + ", " : "")
        + (telefono != null ? "telefono=" + telefono : "") + "]";
  }

}

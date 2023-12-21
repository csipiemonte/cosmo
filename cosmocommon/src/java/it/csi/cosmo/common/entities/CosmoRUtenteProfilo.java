/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoREntity;


/**
 * The persistent class for the cosmo_r_utente_profilo database table.
 *
 */
@Entity
@Table(name = "cosmo_r_utente_profilo")
@NamedQuery(name = "CosmoRUtenteProfilo.findAll", query = "SELECT c FROM CosmoRUtenteProfilo c")
public class CosmoRUtenteProfilo extends CosmoREntity {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "COSMO_R_UTENTE_PROFILO_ID_GENERATOR",
      sequenceName = "COSMO_R_UTENTE_PROFILO_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "COSMO_R_UTENTE_PROFILO_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  // bi-directional many-to-one association to CosmoTEnte
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_ente", nullable = true)
  private CosmoTEnte cosmoTEnte;

  // bi-directional many-to-one association to CosmoTUtente
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_utente", nullable = false)
  private CosmoTUtente cosmoTUtente;

  // bi-directional many-to-one association to CosmoTProfilo
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_profilo", nullable = false)
  private CosmoTProfilo cosmoTProfilo;

  public CosmoRUtenteProfilo() {
    // empty constructor
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public CosmoTProfilo getCosmoTProfilo() {
    return cosmoTProfilo;
  }

  public void setCosmoTProfilo(CosmoTProfilo cosmoTProfilo) {
    this.cosmoTProfilo = cosmoTProfilo;
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
    CosmoRUtenteProfilo other = (CosmoRUtenteProfilo) obj;
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
    return "CosmoRUtenteProfilo [" + (id != null ? "id=" + id : "") + "]";
  }

}

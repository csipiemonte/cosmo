/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_approvazione database table.
 *
 */
@Entity
@Table(name="cosmo_t_approvazione")
@NamedQuery(name="CosmoTApprovazione.findAll", query="SELECT c FROM CosmoTApprovazione c")
public class CosmoTApprovazione extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "COSMO_T_APPROVAZIONE_ID_GENERATOR",
      sequenceName = "COSMO_T_APPROVAZIONE_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COSMO_T_APPROVAZIONE_ID_GENERATOR")
  private Long id;

  @Column(name="dt_approvazione")
  private Timestamp dtApprovazione;


  //bi-directional many-to-one association to CosmoTAttivita
  @ManyToOne
  @JoinColumn(name="id_attivita")
  private CosmoTAttivita cosmoTAttivita;

  //bi-directional many-to-one association to CosmoTDocumento
  @ManyToOne
  @JoinColumn(name="id_documento")
  private CosmoTDocumento cosmoTDocumento;

  //bi-directional many-to-one association to CosmoTUtente
  @ManyToOne
  @JoinColumn(name="id_utente")
  private CosmoTUtente cosmoTUtente;

  public CosmoTApprovazione() {
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Timestamp getDtApprovazione() {
    return this.dtApprovazione;
  }

  public void setDtApprovazione(Timestamp dtApprovazione) {
    this.dtApprovazione = dtApprovazione;
  }

  public CosmoTAttivita getCosmoTAttivita() {
    return this.cosmoTAttivita;
  }

  public void setCosmoTAttivita(CosmoTAttivita cosmoTAttivita) {
    this.cosmoTAttivita = cosmoTAttivita;
  }

  public CosmoTDocumento getCosmoTDocumento() {
    return this.cosmoTDocumento;
  }

  public void setCosmoTDocumento(CosmoTDocumento cosmoTDocumento) {
    this.cosmoTDocumento = cosmoTDocumento;
  }

  public CosmoTUtente getCosmoTUtente() {
    return this.cosmoTUtente;
  }

  public void setCosmoTUtente(CosmoTUtente cosmoTUtente) {
    this.cosmoTUtente = cosmoTUtente;
  }

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((cosmoTAttivita == null) ? 0 : cosmoTAttivita.hashCode());
    result = prime * result + ((cosmoTDocumento == null) ? 0 : cosmoTDocumento.hashCode());
    result = prime * result + ((cosmoTUtente == null) ? 0 : cosmoTUtente.hashCode());
    result = prime * result + ((dtApprovazione == null) ? 0 : dtApprovazione.hashCode());
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
    CosmoTApprovazione other = (CosmoTApprovazione) obj;
    if (cosmoTAttivita == null) {
      if (other.cosmoTAttivita != null) {
        return false;
      }
    } else if (!cosmoTAttivita.equals(other.cosmoTAttivita)) {
      return false;
    }
    if (cosmoTDocumento == null) {
      if (other.cosmoTDocumento != null) {
        return false;
      }
    } else if (!cosmoTDocumento.equals(other.cosmoTDocumento)) {
      return false;
    }
    if (cosmoTUtente == null) {
      if (other.cosmoTUtente != null) {
        return false;
      }
    } else if (!cosmoTUtente.equals(other.cosmoTUtente)) {
      return false;
    }
    if (dtApprovazione == null) {
      if (other.dtApprovazione != null) {
        return false;
      }
    } else if (!dtApprovazione.equals(other.dtApprovazione)) {
      return false;
    }
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
    return "CosmoTApprovazione [" + (id != null ? "id=" + id + ", " : "")
        + (dtApprovazione != null ? "dtApprovazione=" + dtApprovazione + ", " : "")
        + (cosmoTAttivita != null ? "cosmoTAttivita=" + cosmoTAttivita + ", " : "")
        + (cosmoTDocumento != null ? "cosmoTDocumento=" + cosmoTDocumento + ", " : "")
        + (cosmoTUtente != null ? "cosmoTUtente=" + cosmoTUtente : "") + "]";
  }



}

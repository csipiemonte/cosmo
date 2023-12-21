/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.sql.Timestamp;
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
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_otp database table.
 *
 */
@Entity
@Table(name="cosmo_t_otp")
@NamedQuery(name="CosmoTOtp.findAll", query="SELECT c FROM CosmoTOtp c")
public class CosmoTOtp extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_OTP_ID_GENERATOR",
  sequenceName = "COSMO_T_OTP_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COSMO_T_OTP_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name = "dt_scadenza", nullable = false)
  private Timestamp dtScadenza;

  // bi-directional many-to-one association to CosmoTEnte
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_utente", nullable = false)
  private CosmoTUtente utente;

  // bi-directional many-to-one association to CosmoTEnte
  @ManyToOne
  @JoinColumn(name = "id_ente", nullable = false)
  private CosmoTEnte cosmoTEnte;

  @Column(name = "valore", nullable = false)
  private String valore;

  @Column(nullable = false)
  private Boolean utilizzato;

  public CosmoTOtp() {
    // empty constructor
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Timestamp getDtScadenza() {
    return this.dtScadenza;
  }

  public void setDtScadenza(Timestamp dtScadenza) {
    this.dtScadenza = dtScadenza;
  }

  public CosmoTUtente getIdUtente() {
    return utente;
  }

  public void setIdUtente(CosmoTUtente idUtente) {
    this.utente = idUtente;
  }

  public CosmoTEnte getCosmoTEnte() {
    return this.cosmoTEnte;
  }

  public void setCosmoTEnte(CosmoTEnte cosmoTEnte) {
    this.cosmoTEnte = cosmoTEnte;
  }

  public String getValore() {
    return this.valore;
  }

  public void setValore(String valore) {
    this.valore = valore;
  }

  public Boolean getUtilizzato() {
    return this.utilizzato;
  }

  public void setUtilizzato(Boolean utilizzato) {
    this.utilizzato = utilizzato;
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
    CosmoTOtp other = (CosmoTOtp) obj;
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
    return "CosmoTOtp [" + (id != null ? "id=" + id + ", " : "")
        + (valore != null ? "valore=" + valore + ", " : "")
        + (dtScadenza != null ? "dtScadenza=" + dtScadenza + ", " : "")
        + (utente != null ? "utente=" + utente.getId() + ", " : "")
        + (cosmoTEnte != null ? "ente=" + cosmoTEnte.getId() + ", " : "")
        + (utilizzato != null ? "utilizzato=" + utilizzato + " " : "") + "]";
  }

}

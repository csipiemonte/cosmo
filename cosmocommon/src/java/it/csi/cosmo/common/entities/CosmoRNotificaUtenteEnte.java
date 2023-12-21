/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.sql.Timestamp;
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
 * The persistent class for the cosmo_r_notifica_utente_ente database table.
 *
 */
@Entity
@Table(name="cosmo_r_notifica_utente_ente")
@NamedQuery(name="CosmoRNotificaUtenteEnte.findAll", query="SELECT c FROM CosmoRNotificaUtenteEnte c")
public class CosmoRNotificaUtenteEnte extends CosmoREntity {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private CosmoRNotificaUtenteEntePK id;

  @Column(name="data_lettura")
  private Timestamp dataLettura;

  // bi-directional many-to-one association to CosmoTEnte
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_ente", nullable = false, insertable = false, updatable = false)
  private CosmoTEnte cosmoTEnte;

  //bi-directional many-to-one association to CosmoTNotifica

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_notifica", nullable = false, insertable = false, updatable = false)
  private CosmoTNotifica cosmoTNotifica;

  //bi-directional many-to-one association to CosmoTUtente
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_utente", nullable = false, insertable = false, updatable = false)
  private CosmoTUtente cosmoTUtente;

  @Column(name = "invio_mail")
  private Boolean invioMail;

  @Column(name = "stato_invio_mail")
  private String statoInvioMail;

  public CosmoRNotificaUtenteEnte() {
    // empty constructor
  }

  public CosmoRNotificaUtenteEntePK getId() {
    return this.id;
  }

  public void setId(CosmoRNotificaUtenteEntePK id) {
    this.id = id;
  }

  public Timestamp getDataLettura() {
    return this.dataLettura;
  }

  public void setDataLettura(Timestamp dataLettura) {
    this.dataLettura = dataLettura;
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
    return cosmoTEnte;
  }

  public void setCosmoTEnte(CosmoTEnte cosmoTEnte) {
    this.cosmoTEnte = cosmoTEnte;
  }

  public CosmoTNotifica getCosmoTNotifica() {
    return this.cosmoTNotifica;
  }

  public void setCosmoTNotifica(CosmoTNotifica cosmoTNotifica) {
    this.cosmoTNotifica = cosmoTNotifica;
  }

  public CosmoTUtente getCosmoTUtente() {
    return this.cosmoTUtente;
  }

  public void setCosmoTUtente(CosmoTUtente cosmoTUtente) {
    this.cosmoTUtente = cosmoTUtente;
  }

  public Boolean getInvioMail() {
    return this.invioMail;
  }

  public void setInvioMail(Boolean invioMail) {
    this.invioMail = invioMail;
  }

  public String getStatoInvioMail() {
    return this.statoInvioMail;
  }

  public void setStatoInvioMail(String statoInvioMail) {
    this.statoInvioMail = statoInvioMail;
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
    CosmoRNotificaUtenteEnte other = (CosmoRNotificaUtenteEnte) obj;
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
    return "CosmoRNotificaUtenteEnte [" + (id != null ? "id=" + id + ", " : "")
        + (dataLettura != null ? "dataLettura=" + dataLettura : "")
        + (invioMail != null ? "invioMail=" + invioMail : "")
        + (statoInvioMail != null ? "statoInvioMail=" + statoInvioMail : "") + "]";
  }

}

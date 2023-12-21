/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the csi_log_audit database table.
 *
 */
@Entity
@Table(name = "csi_log_audit")
@NamedQuery(name = "CsiLogAudit.findAll", query = "SELECT c FROM CsiLogAudit c")
public class CsiLogAudit implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "CSI_LOG_AUDIT_ID_GENERATOR", sequenceName = "CSI_LOG_AUDIT_ID_SEQ",
  allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CSI_LOG_AUDIT_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name = "data_ora", nullable = false)
  private Timestamp dataOra;

  @Column(name = "id_app", nullable = false, length = 100)
  private String idApp;

  @Column(name = "ip_address", length = 40)
  private String ipAddress;

  @Column(name = "key_oper", length = 500)
  private String keyOper;

  @Column(name = "ogg_oper", length = 150)
  private String oggOper;

  @Column(nullable = false, length = 10)
  private String operazione;

  @Column(nullable = false, length = 100)
  private String utente;

  public CsiLogAudit() {
    // empty constructor
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Timestamp getDataOra() {
    return this.dataOra;
  }

  public void setDataOra(Timestamp dataOra) {
    this.dataOra = dataOra;
  }

  public String getIdApp() {
    return this.idApp;
  }

  public void setIdApp(String idApp) {
    this.idApp = idApp;
  }

  public String getIpAddress() {
    return this.ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public String getKeyOper() {
    return this.keyOper;
  }

  public void setKeyOper(String keyOper) {
    this.keyOper = keyOper;
  }

  public String getOggOper() {
    return this.oggOper;
  }

  public void setOggOper(String oggOper) {
    this.oggOper = oggOper;
  }

  public String getOperazione() {
    return this.operazione;
  }

  public void setOperazione(String operazione) {
    this.operazione = operazione;
  }

  public String getUtente() {
    return this.utente;
  }

  public void setUtente(String utente) {
    this.utente = utente;
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
    CsiLogAudit other = (CsiLogAudit) obj;
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
    return "CsiLogAudit [" + (id != null ? "id=" + id + ", " : "")
        + (dataOra != null ? "dataOra=" + dataOra + ", " : "")
        + (idApp != null ? "idApp=" + idApp + ", " : "")
        + (ipAddress != null ? "ipAddress=" + ipAddress + ", " : "")
        + (keyOper != null ? "keyOper=" + keyOper + ", " : "")
        + (oggOper != null ? "oggOper=" + oggOper + ", " : "")
        + (operazione != null ? "operazione=" + operazione + ", " : "")
        + (utente != null ? "utente=" + utente : "") + "]";
  }

}

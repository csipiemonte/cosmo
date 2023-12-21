/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoCEntity;


/**
 * The persistent class for the cosmo_c_configurazione database table.
 *
 */
@Entity
@Table(name = "cosmo_c_configurazione")
@NamedQuery(name = "CosmoCConfigurazione.findAll", query = "SELECT c FROM CosmoCConfigurazione c")
public class CosmoCConfigurazione
extends CosmoCEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(unique = true, nullable = false, length = 20)
  private String chiave;

  @Column(length = 500)
  private String descrizione;

  @Column(nullable = false, length = 255)
  private String valore;

  public CosmoCConfigurazione() {
    // empty constructor
  }

  public String getChiave() {
    return this.chiave;
  }

  public void setChiave(String chiave) {
    this.chiave = chiave;
  }

  public String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public String getValore() {
    return this.valore;
  }

  public void setValore(String valore) {
    this.valore = valore;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((chiave == null) ? 0 : chiave.hashCode());
    result = prime * result + ((dtInizioVal == null) ? 0 : dtInizioVal.hashCode());
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
    CosmoCConfigurazione other = (CosmoCConfigurazione) obj;
    if (chiave == null) {
      if (other.chiave != null) {
        return false;
      }
    } else if (!chiave.equals(other.chiave)) {
      return false;
    }
    if (dtInizioVal == null) {
      if (other.dtInizioVal != null) {
        return false;
      }
    } else if (!dtInizioVal.equals(other.dtInizioVal)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CosmoCEntity [" + (chiave != null ? "chiave=" + chiave + ", " : "")
        + (descrizione != null ? "descrizione=" + descrizione + ", " : "")
        + (dtFineVal != null ? "dtFineVal=" + dtFineVal + ", " : "")
        + (dtInizioVal != null ? "dtInizioVal=" + dtInizioVal + ", " : "")
        + (valore != null ? "valore=" + valore : "") + "]";
  }

}

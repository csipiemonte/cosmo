/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoCEntity;


/**
 * The persistent class for the cosmo_c_configurazione_metadati database table.
 *
 */
@Entity
@Table(name = "cosmo_c_configurazione_metadati")
@NamedQuery(name = "CosmoCConfigurazioneMetadati.findAll", query = "SELECT c FROM CosmoCConfigurazioneMetadati c")
public class CosmoCConfigurazioneMetadati extends CosmoCEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(unique = true, nullable = false, length = 100)
  private String chiave;

  @Column(length = 255)
  private String descrizione;

  @Column(nullable = false, length = 255)
  private String valore;

  // bi-directional many-to-one association to CosmoDTipoPratica
  @ManyToOne
  @JoinColumn(name = "codice_tipo_pratica")
  private CosmoDTipoPratica cosmoDTipoPratica;

  public CosmoCConfigurazioneMetadati() {
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

  public String getValore() {
    return this.valore;
  }

  public void setValore(String valore) {
    this.valore = valore;
  }

  public CosmoDTipoPratica getCosmoDTipoPratica() {
    return this.cosmoDTipoPratica;
  }

  public void setCosmoDTipoPratica(CosmoDTipoPratica cosmoDTipoPratica) {
    this.cosmoDTipoPratica = cosmoDTipoPratica;
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
    CosmoCConfigurazioneMetadati other = (CosmoCConfigurazioneMetadati) obj;
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
    return "CosmoCConfigurazioneMetadati [" 
        + (chiave != null ? "chiave=" + chiave + ", " : "")
        + (descrizione != null ? "descrizione=" + descrizione + ", " : "")
        + (dtFineVal != null ? "dtFineVal=" + dtFineVal + ", " : "")
        + (dtInizioVal != null ? "dtInizioVal=" + dtInizioVal + ", " : "")
        + (valore != null ? "valore=" + valore : "") + "]";
  }
}

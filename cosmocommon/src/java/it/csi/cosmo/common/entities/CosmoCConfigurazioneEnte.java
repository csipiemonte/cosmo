/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoCEntity;


/**
 * The persistent class for the cosmo_c_configurazione_ente database table.
 *
 */
@Entity
@Table(name="cosmo_c_configurazione_ente")
@NamedQuery(name="CosmoCConfigurazioneEnte.findAll", query="SELECT c FROM CosmoCConfigurazioneEnte c")
public class CosmoCConfigurazioneEnte extends CosmoCEntity {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private CosmoCConfigurazioneEntePK id;

  private String descrizione;

  private String valore;


  // bi-directional many-to-one association to CosmoTEnte
  @ManyToOne
  @JoinColumn(name = "id_ente", insertable = false, updatable = false)
  private CosmoTEnte cosmoTEnte;


  public CosmoCConfigurazioneEnte() {}

  public CosmoCConfigurazioneEntePK getId() {
    return this.id;
  }

  public void setId(CosmoCConfigurazioneEntePK id) {
    this.id = id;
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

  public CosmoTEnte getCosmoTEnte() {
    return this.cosmoTEnte;
  }

  public void setCosmoTEnte(CosmoTEnte cosmoTEnte) {
    this.cosmoTEnte = cosmoTEnte;
  }

  @Override
  public int hashCode() {
    return Objects.hash(descrizione, dtFineVal, dtInizioVal, id, valore);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CosmoCConfigurazioneEnte other = (CosmoCConfigurazioneEnte) obj;
    return Objects.equals(descrizione, other.descrizione)
        && Objects.equals(dtFineVal, other.dtFineVal)
        && Objects.equals(dtInizioVal, other.dtInizioVal) && Objects.equals(id, other.id)
        && Objects.equals(valore, other.valore);
  }

  @Override
  public String toString() {
    return "CosmoCConfigurazioneEnte [id=" + id + ", descrizione=" + descrizione + ", dtFineVal="
        + dtFineVal + ", dtInizioVal=" + dtInizioVal + ", valore=" + valore + "]";
  }



}

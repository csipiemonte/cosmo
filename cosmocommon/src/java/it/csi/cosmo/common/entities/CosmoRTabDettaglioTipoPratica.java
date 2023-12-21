/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoREntity;


/**
 * The persistent class for the cosmo_r_tab_dettaglio_tipo_pratica database table.
 *
 */
@Entity
@Table(name="cosmo_r_tab_dettaglio_tipo_pratica")
@NamedQuery(name="CosmoRTabDettaglioTipoPratica.findAll", query="SELECT c FROM CosmoRTabDettaglioTipoPratica c")
public class CosmoRTabDettaglioTipoPratica extends CosmoREntity {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private CosmoRTabDettaglioTipoPraticaPK id;

  private Integer ordine;

  //bi-directional many-to-one association to CosmoDTabDettaglio
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "codice_tab_dettaglio", nullable = false, insertable = false,
  updatable = false)
  private CosmoDTabDettaglio cosmoDTabDettaglio;

  //bi-directional many-to-one association to CosmoDTipoPratica
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "codice_tipo_pratica", nullable = false, insertable = false,
  updatable = false)
  private CosmoDTipoPratica cosmoDTipoPratica;

  public CosmoRTabDettaglioTipoPratica() {
    // empty constructor
  }

  public CosmoRTabDettaglioTipoPraticaPK getId() {
    return this.id;
  }

  public void setId(CosmoRTabDettaglioTipoPraticaPK id) {
    this.id = id;
  }

  public Integer getOrdine() {
    return this.ordine;
  }

  public void setOrdine(Integer ordine) {
    this.ordine = ordine;
  }

  public CosmoDTabDettaglio getCosmoDTabDettaglio() {
    return this.cosmoDTabDettaglio;
  }

  public void setCosmoDTabDettaglio(CosmoDTabDettaglio cosmoDTabDettaglio) {
    this.cosmoDTabDettaglio = cosmoDTabDettaglio;
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
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CosmoRTabDettaglioTipoPratica other = (CosmoRTabDettaglioTipoPratica) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoRTabDettaglioTipoPratica [id=" + id + ", ordine=" + ordine + "]";
  }

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;


/**
 * The persistent class for the cosmo_d_helper_modale database table.
 *
 */
@Entity
@Table(name="cosmo_d_helper_modale")
@NamedQuery(name="CosmoDHelperModale.findAll", query="SELECT c FROM CosmoDHelperModale c")
public class CosmoDHelperModale extends CosmoDEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(nullable = false, length = 100)
  private String codice;

  @Column(length = 255)
  private String descrizione;

  // bi-directional many-to-one association to CosmoTHelper
  @OneToMany(mappedBy = "helperModale", fetch = FetchType.LAZY)
  private List<CosmoTHelper> cosmoTHelpers;

  // bi-directional many-to-one association to CosmoDHelperTab
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_pagina")
  private CosmoDHelperPagina helperPagina;

  // bi-directional many-to-one association to CosmoDHelperTab
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_tab")
  private CosmoDHelperTab helperTab;

  public String getCodice() {
    return this.codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public CosmoDHelperPagina getCosmoDHelperPagina() {
    return this.helperPagina;
  }

  public void setCosmoDHelperPagina(CosmoDHelperPagina cosmoDHelperPagina) {
    this.helperPagina = cosmoDHelperPagina;
  }

  public CosmoDHelperTab getCosmoDHelperTab() {
    return this.helperTab;
  }

  public void setCosmoDHelperTab(CosmoDHelperTab cosmoDHelperTab) {
    this.helperTab = cosmoDHelperTab;
  }

  public List<CosmoTHelper> getCosmoTHelpers() {
    return this.cosmoTHelpers;
  }

  public void setCosmoTHelpers(List<CosmoTHelper> cosmoTHelpers) {
    this.cosmoTHelpers = cosmoTHelpers;
  }

  public CosmoTHelper addCosmoTHelper(CosmoTHelper cosmoTHelper) {
    getCosmoTHelpers().add(cosmoTHelper);
    cosmoTHelper.setCosmoDHelperModale(this);

    return cosmoTHelper;
  }

  public CosmoTHelper removeCosmoTHelper(CosmoTHelper cosmoTHelper) {
    getCosmoTHelpers().remove(cosmoTHelper);
    cosmoTHelper.setCosmoDHelperModale(null);

    return cosmoTHelper;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codice == null) ? 0 : codice.hashCode());
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
    CosmoDHelperModale other = (CosmoDHelperModale) obj;
    if (codice == null) {
      if (other.codice != null) {
        return false;
      }
    } else if (!codice.equals(other.codice)) {
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
    return "CosmoDHelperModale [" + (codice != null ? "codice=" + codice + ", " : "")
        + (descrizione != null ? "descrizione=" + descrizione + ", " : "") + "]";
  }
}

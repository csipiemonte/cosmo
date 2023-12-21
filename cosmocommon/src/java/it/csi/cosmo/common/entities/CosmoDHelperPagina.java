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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;


/**
 * The persistent class for the cosmo_d_helper_pagina database table.
 *
 */
@Entity
@Table(name = "cosmo_d_helper_pagina")
@NamedQuery(name = "CosmoDHelperPagina.findAll", query = "SELECT c FROM CosmoDHelperPagina c")
public class CosmoDHelperPagina extends CosmoDEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(nullable = false, length = 100)
  private String codice;

  @Column(length = 255)
  private String descrizione;

  // bi-directional many-to-one association to CosmoDHelperTab
  @OneToMany(mappedBy = "helperPagina", fetch = FetchType.LAZY)
  private List<CosmoTHelper> cosmoTHelpers;

  // bi-directional many-to-one association to CosmoDHelperTab
  @OneToMany(mappedBy = "cosmoDHelperPagina")
  private List<CosmoDHelperTab> cosmoDHelperTabs;

  // bi-directional many-to-one association to CosmoDHelperModale
  @OneToMany(mappedBy = "helperPagina")
  private List<CosmoDHelperModale> cosmoDHelperModales;

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

  public List<CosmoTHelper> getCosmoTHelpers() {
    return this.cosmoTHelpers;
  }

  public void setCosmoTHelpers(List<CosmoTHelper> cosmoTHelpers) {
    this.cosmoTHelpers = cosmoTHelpers;
  }

  public CosmoTHelper addCosmoTHelper(CosmoTHelper cosmoTHelper) {
    getCosmoTHelpers().add(cosmoTHelper);
    cosmoTHelper.setCosmoDHelperPagina(this);

    return cosmoTHelper;
  }

  public CosmoTHelper removeCosmoTHelper(CosmoTHelper cosmoTHelper) {
    getCosmoTHelpers().remove(cosmoTHelper);
    cosmoTHelper.setCosmoDHelperPagina(null);

    return cosmoTHelper;
  }

  public List<CosmoDHelperModale> getCosmoDHelperModales() {
    return this.cosmoDHelperModales;
  }

  public void setCosmoDHelperModales(List<CosmoDHelperModale> cosmoDHelperModales) {
    this.cosmoDHelperModales = cosmoDHelperModales;
  }

  public CosmoDHelperModale addCosmoDHelperModale(CosmoDHelperModale cosmoDHelperModale) {
    getCosmoDHelperModales().add(cosmoDHelperModale);
    cosmoDHelperModale.setCosmoDHelperPagina(this);

    return cosmoDHelperModale;
  }

  public CosmoDHelperModale removeCosmoDHelperModale(CosmoDHelperModale cosmoDHelperModale) {
    getCosmoDHelperModales().remove(cosmoDHelperModale);
    cosmoDHelperModale.setCosmoDHelperPagina(null);

    return cosmoDHelperModale;
  }

  public List<CosmoDHelperTab> getCosmoDHelperTabs() {
    return this.cosmoDHelperTabs;
  }

  public void setCosmoDHelperTabs(List<CosmoDHelperTab> cosmoDHelperTabs) {
    this.cosmoDHelperTabs = cosmoDHelperTabs;
  }

  public CosmoDHelperTab addCosmoDHelperTab(CosmoDHelperTab cosmoDHelperTab) {
    getCosmoDHelperTabs().add(cosmoDHelperTab);
    cosmoDHelperTab.setCosmoDHelperPagina(this);

    return cosmoDHelperTab;
  }

  public CosmoDHelperTab removeCosmoDHelperTab(CosmoDHelperTab cosmoDHelperTab) {
    getCosmoDHelperTabs().remove(cosmoDHelperTab);
    cosmoDHelperTab.setCosmoDHelperPagina(null);

    return cosmoDHelperTab;
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
    CosmoDHelperPagina other = (CosmoDHelperPagina) obj;
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
    return "CosmoDHelperPagina [" + (codice != null ? "codice=" + codice + ", " : "")
        + (descrizione != null ? "descrizione=" + descrizione + ", " : "") + "]";
  }

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

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
 * The persistent class for the cosmo_t_helper database table.
 *
 */
@Entity
@Table(name="cosmo_t_helper")
@NamedQuery(name="CosmoTHelper.findAll", query="SELECT c FROM CosmoTHelper c")
public class CosmoTHelper extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "COSMO_T_HELPER_ID_GENERATOR", sequenceName = "COSMO_T_HELPER_ID_SEQ",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COSMO_T_HELPER_ID_GENERATOR")

  @Column(unique = true, nullable = false)
  private Long id;

  @Column(length = 5000)
  private String html;

  // bi-directional many-to-one association to CosmoDCodicePagina
  @ManyToOne
  @JoinColumn(name = "codice_pagina", nullable = false)
  private CosmoDHelperPagina helperPagina;

  // bi-directional many-to-one association to CosmoDHelperTab
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_tab")
  private CosmoDHelperTab helperTab;

  // bi-directional many-to-one association to CosmoDHelperTab
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_form")
  private CosmoDCustomFormFormio helperForm;

  // bi-directional many-to-one association to CosmoDHelperTab
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_modale")
  private CosmoDHelperModale helperModale;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getHtml() {
    return this.html;
  }

  public void setHtml(String html) {
    this.html = html;
  }

  public CosmoDHelperPagina getCosmoDHelperPagina() {
    return this.helperPagina;
  }

  public void setCosmoDHelperPagina(CosmoDHelperPagina helperPagina) {
    this.helperPagina = helperPagina;
  }

  public CosmoDHelperTab getCosmoDHelperTab() {
    return this.helperTab;
  }

  public void setCosmoDHelperTab(CosmoDHelperTab codiceTab) {
    this.helperTab = codiceTab;
  }

  public CosmoDCustomFormFormio getCosmoDCustomFormFormio() {
    return this.helperForm;
  }

  public void setCosmoDCustomFormFormio(CosmoDCustomFormFormio codiceForm) {
    this.helperForm = codiceForm;
  }

  public CosmoDHelperModale getCosmoDHelperModale() {
    return this.helperModale;
  }

  public void setCosmoDHelperModale(CosmoDHelperModale helperModale) {
    this.helperModale = helperModale;
  }

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
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
    CosmoTHelper other = (CosmoTHelper) obj;
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
    return "CosmoTHelper [" + (id != null ? "id=" + id + ", " : "")
        + (html != null ? "html=" + html + ", " : "")
        + (helperPagina != null ? "codicePagina =" + helperPagina.getCodice() + ", "
            : "")
        + (helperTab != null ? "codiceTab =" + helperTab.getCodice() + ", " : "")
        + (helperForm != null ? "codiceForm =" + helperForm.getCodice() + ", " : "")
        + (helperModale != null ? "codiceModale =" + helperModale.getCodice() + ", " : "");
  }

}

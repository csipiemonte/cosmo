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
 * The persistent class for the cosmo_t_custom_form_formio database table.
 *
 */
@Entity
@Table(name = "cosmo_d_custom_form_formio")
@NamedQuery(name = "CosmoDCustomFormFormio.findAll",
query = "SELECT c FROM CosmoDCustomFormFormio c")
public class CosmoDCustomFormFormio extends CosmoDEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(nullable = false, length = 30)
  private String codice;

  @Column(name = "custom_form", nullable = false)
  private String customForm;

  private String descrizione;

  // bi-directional many-to-one association to CosmoDTipoPratica
  @ManyToOne
  @JoinColumn(name = "codice_tipo_pratica")
  private CosmoDTipoPratica cosmoDTipoPratica;

  // bi-directional many-to-one association to CosmoDHelperTab
  @OneToMany(mappedBy = "helperPagina", fetch = FetchType.LAZY)
  private List<CosmoTHelper> cosmoTHelpers;

  public CosmoDCustomFormFormio() {
    // empty constructor
  }

  public String getCodice() {
    return this.codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public String getCustomForm() {
    return this.customForm;
  }

  public void setCustomForm(String customForm) {
    this.customForm = customForm;
  }

  public String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public CosmoDTipoPratica getCosmoDTipoPratica() {
    return this.cosmoDTipoPratica;
  }

  public void setCosmoDTipoPratica(CosmoDTipoPratica cosmoDTipoPratica) {
    this.cosmoDTipoPratica = cosmoDTipoPratica;
  }

  public List<CosmoTHelper> getCosmoTHelpers() {
    return this.cosmoTHelpers;
  }

  public void setCosmoTHelpers(List<CosmoTHelper> cosmoTHelpers) {
    this.cosmoTHelpers = cosmoTHelpers;
  }

  public CosmoTHelper addCosmoTHelper(CosmoTHelper cosmoTHelper) {
    getCosmoTHelpers().add(cosmoTHelper);
    cosmoTHelper.setCosmoDCustomFormFormio(this);

    return cosmoTHelper;
  }

  public CosmoTHelper removeCosmoTHelper(CosmoTHelper cosmoTHelper) {
    getCosmoTHelpers().remove(cosmoTHelper);
    cosmoTHelper.setCosmoDCustomFormFormio(null);

    return cosmoTHelper;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codice == null) ? 0 : codice.hashCode());
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
    CosmoDCustomFormFormio other = (CosmoDCustomFormFormio) obj;
    if (codice == null) {
      if (other.codice != null)
        return false;
    } else if (!codice.equals(other.codice))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoDCustomFormFormio [descrizione=" + descrizione + ", codice=" + codice + "]";
  }

}

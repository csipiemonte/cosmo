/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;

/**
 * The persistent class for the cosmo_d_tipo_tag database table.
 *
 */
@Entity
@Table(name = "cosmo_d_tipo_tag")
@NamedQuery(name = "CosmoDTipoTag.findAll", query = "SELECT c FROM CosmoDTipoTag c")
public class CosmoDTipoTag extends CosmoDEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(unique = true, nullable = false, length = 30)
  private String codice;

  @Column(length = 100)
  private String descrizione;

  @Column(length = 100)
  private String label;

  // bi-directional many-to-one association to CosmoTTag
  @OneToMany(mappedBy = "cosmoDTipoTag")
  private List<CosmoTTag> cosmoTTags;

  public CosmoDTipoTag() {
    // empty constructor
  }

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

  public String getLabel() {
    return this.label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public List<CosmoTTag> getCosmoTTags() {
    return this.cosmoTTags;
  }

  public void setCosmoTTags(List<CosmoTTag> cosmoTTags) {
    this.cosmoTTags = cosmoTTags;
  }

  public CosmoTTag addCosmoTTag(CosmoTTag cosmoTTag) {
    getCosmoTTags().add(cosmoTTag);
    cosmoTTag.setCosmoDTipoTag(this);

    return cosmoTTag;
  }

  public CosmoTTag removeCosmoTTag(CosmoTTag cosmoTTag) {
    getCosmoTTags().remove(cosmoTTag);
    cosmoTTag.setCosmoDTipoTag(null);

    return cosmoTTag;
  }

}

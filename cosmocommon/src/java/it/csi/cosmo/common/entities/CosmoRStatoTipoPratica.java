/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoREntity;


/**
 * The persistent class for the cosmo_r_stato_tipo_pratica database table.
 *
 */
@Entity
@Table(name = "cosmo_r_stato_tipo_pratica")
@NamedQuery(name = "CosmoRStatoTipoPratica.findAll",
    query = "SELECT c FROM CosmoRStatoTipoPratica c")
public class CosmoRStatoTipoPratica extends CosmoREntity {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "COSMO_R_STATO_TIPO_PRATICA_ID_GENERATOR",
      sequenceName = "COSMO_R_STATO_TIPO_PRATICA_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "COSMO_R_STATO_TIPO_PRATICA_ID_GENERATOR")
  private Long id;

  // bi-directional many-to-one association to CosmoDTipoPratica
  @ManyToOne
  @JoinColumn(name = "codice_tipo_pratica")
  private CosmoDTipoPratica cosmoDTipoPratica;

  // bi-directional many-to-one association to CosmoDStatoPratica
  @ManyToOne
  @JoinColumn(name = "codice_stato_pratica")
  private CosmoDStatoPratica cosmoDStatoPratica;

  public CosmoRStatoTipoPratica() {
    // NOP
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CosmoDTipoPratica getCosmoDTipoPratica() {
    return this.cosmoDTipoPratica;
  }

  public void setCosmoDTipoPratica(CosmoDTipoPratica cosmoDTipoPratica) {
    this.cosmoDTipoPratica = cosmoDTipoPratica;
  }

  public CosmoDStatoPratica getCosmoDStatoPratica() {
    return this.cosmoDStatoPratica;
  }

  public void setCosmoDStatoPratica(CosmoDStatoPratica cosmoDStatoPratica) {
    this.cosmoDStatoPratica = cosmoDStatoPratica;
  }

}

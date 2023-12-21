/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoREntity;


/**
 * The persistent class for the cosmo_r_tipo_documento_tipo_documento database table.
 *
 */
@Entity
@Table(name="cosmo_r_tipo_documento_tipo_documento")
@NamedQuery(name="CosmoRTipoDocumentoTipoDocumento.findAll", query="SELECT c FROM CosmoRTipoDocumentoTipoDocumento c")
public class CosmoRTipoDocumentoTipoDocumento extends CosmoREntity {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private CosmoRTipoDocumentoTipoDocumentoPK id;

  @Column(name="codice_stardas_allegato", length=100)
  private String codiceStardasAllegato;

  //bi-directional many-to-one association to CosmoDTipoDocumento
  @ManyToOne
  @JoinColumn(name="codice_allegato", nullable=false, insertable=false, updatable=false)
  private CosmoDTipoDocumento cosmoDTipoDocumentoAllegato;

  //bi-directional many-to-one association to CosmoDTipoDocumento
  @ManyToOne
  @JoinColumn(name="codice_padre", nullable=false, insertable=false, updatable=false)
  private CosmoDTipoDocumento cosmoDTipoDocumentoPadre;

  // bi-directional many-to-one association to CosmoDTipoPratica
  @ManyToOne
  @JoinColumn(name = "codice_tipo_pratica", nullable = false, insertable = false, updatable = false)
  private CosmoDTipoPratica cosmoDTipoPratica;


  public CosmoRTipoDocumentoTipoDocumento() {
    // empty constructor
  }

  public CosmoRTipoDocumentoTipoDocumentoPK getId() {
    return this.id;
  }

  public void setId(CosmoRTipoDocumentoTipoDocumentoPK id) {
    this.id = id;
  }

  public String getCodiceStardasAllegato() {
    return this.codiceStardasAllegato;
  }

  public void setCodiceStardasAllegato(String codiceStardasAllegato) {
    this.codiceStardasAllegato = codiceStardasAllegato;
  }

  public CosmoDTipoDocumento getCosmoDTipoDocumentoPadre() {
    return this.cosmoDTipoDocumentoPadre;
  }

  public void setCosmoDTipoDocumentoPadre(CosmoDTipoDocumento cosmoDTipoDocumentoPadre) {
    this.cosmoDTipoDocumentoPadre = cosmoDTipoDocumentoPadre;
  }

  public CosmoDTipoDocumento getCosmoDTipoDocumentoAllegato() {
    return this.cosmoDTipoDocumentoAllegato;
  }

  public void setCosmoDTipoDocumentoAllegato(CosmoDTipoDocumento cosmoDTipoDocumentoAllegato) {
    this.cosmoDTipoDocumentoAllegato = cosmoDTipoDocumentoAllegato;
  }

  public CosmoDTipoPratica getCosmoDTipoPratica() {
    return this.cosmoDTipoPratica;
  }

  public void setCosmoDTipoPratica(CosmoDTipoPratica cosmoDTipoPratica) {
    this.cosmoDTipoPratica = cosmoDTipoPratica;
  }

}

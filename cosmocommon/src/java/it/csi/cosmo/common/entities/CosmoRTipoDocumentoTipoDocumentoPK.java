/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the cosmo_r_tipo_documento_tipo_documento database table.
 *
 */
@Embeddable
public class CosmoRTipoDocumentoTipoDocumentoPK implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name="codice_padre", insertable=false, updatable=false, unique=true, nullable=false, length=100)
  private String codicePadre;

  @Column(name="codice_allegato", insertable=false, updatable=false, unique=true, nullable=false, length=100)
  private String codiceAllegato;

  @Column(name = "codice_tipo_pratica", insertable = false, updatable = false, unique = true,
      nullable = false, length = 255)
  private String codiceTipoPratica;

  public CosmoRTipoDocumentoTipoDocumentoPK() {
    // empty constructor
  }

  public String getCodicePadre() {
    return this.codicePadre;
  }

  public void setCodicePadre(String codicePadre) {
    this.codicePadre = codicePadre;
  }

  public String getCodiceAllegato() {
    return this.codiceAllegato;
  }

  public void setCodiceAllegato(String codiceAllegato) {
    this.codiceAllegato = codiceAllegato;
  }

  public String getCodiceTipoPratica() {
    return this.codiceTipoPratica;
  }

  public void setCodiceTipoPratica(String codiceTipoPratica) {
    this.codiceTipoPratica = codiceTipoPratica;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof CosmoRTipoDocumentoTipoDocumentoPK)) {
      return false;
    }
    CosmoRTipoDocumentoTipoDocumentoPK castOther = (CosmoRTipoDocumentoTipoDocumentoPK)other;
    return
        this.codicePadre.equals(castOther.codicePadre)
        && this.codiceAllegato.equals(castOther.codiceAllegato)
        && this.codiceTipoPratica.equals(castOther.codiceTipoPratica);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.codicePadre.hashCode();
    hash = hash * prime + this.codiceAllegato.hashCode();
    hash = hash * prime + this.codiceTipoPratica.hashCode();

    return hash;
  }
}

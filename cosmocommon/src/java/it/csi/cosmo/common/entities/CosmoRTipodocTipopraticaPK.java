/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the cosmo_r_tipodoc_tipopratica database table.
 *
 */
@Embeddable
public class CosmoRTipodocTipopraticaPK implements Serializable {
  // default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  public String getPrimaryKeyRepresentation() {
    return String.valueOf("{\"codice_tipo_documento\"=" + codiceTipoDocumento
        + ", \"codice_tipo_pratica\"=" + codiceTipoPratica + "\"}");
  }

  @Column(name = "codice_tipo_documento", insertable = false, updatable = false, unique = true,
      nullable = false, length = 100)
  private String codiceTipoDocumento;

  @Column(name = "codice_tipo_pratica", insertable = false, updatable = false, unique = true,
      nullable = false)
  private String codiceTipoPratica;

  public CosmoRTipodocTipopraticaPK() {
    // empty constructor
  }

  public String getCodiceTipoDocumento() {
    return this.codiceTipoDocumento;
  }

  public void setCodiceTipoDocumento(String codiceTipoDocumento) {
    this.codiceTipoDocumento = codiceTipoDocumento;
  }

  /**
   * @return the codiceTipoPratica
   */
  public String getCodiceTipoPratica() {
    return codiceTipoPratica;
  }

  /**
   * @param codiceTipoPratica the codiceTipoPratica to set
   */
  public void setCodiceTipoPratica(String codiceTipoPratica) {
    this.codiceTipoPratica = codiceTipoPratica;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codiceTipoDocumento == null) ? 0 : codiceTipoDocumento.hashCode());
    result = prime * result + ((codiceTipoPratica == null) ? 0 : codiceTipoPratica.hashCode());
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
    CosmoRTipodocTipopraticaPK other = (CosmoRTipodocTipopraticaPK) obj;
    if (codiceTipoDocumento == null) {
      if (other.codiceTipoDocumento != null)
        return false;
    } else if (!codiceTipoDocumento.equals(other.codiceTipoDocumento))
      return false;
    if (codiceTipoPratica == null) {
      if (other.codiceTipoPratica != null)
        return false;
    } else if (!codiceTipoPratica.equals(other.codiceTipoPratica))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoRTipodocTipopraticaPK [codiceTipoDocumento=" + codiceTipoDocumento
        + ", codiceTipoPratica=" + codiceTipoPratica + "]";
  }

}

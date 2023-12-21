/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the cosmo_r_tab_dettaglio_tipo_pratica database table.
 *
 */
@Embeddable
public class CosmoRTabDettaglioTipoPraticaPK implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name="codice_tipo_pratica", insertable=false, updatable=false)
  private String codiceTipoPratica;

  @Column(name="codice_tab_dettaglio", insertable=false, updatable=false)
  private String codiceTabDettaglio;

  public CosmoRTabDettaglioTipoPraticaPK() {
      // empty constructor
  }
  public String getCodiceTipoPratica() {
    return this.codiceTipoPratica;
  }
  public void setCodiceTipoPratica(String codiceTipoPratica) {
    this.codiceTipoPratica = codiceTipoPratica;
  }
  public String getCodiceTabDettaglio() {
    return this.codiceTabDettaglio;
  }
  public void setCodiceTabDettaglio(String codiceTabDettaglio) {
    this.codiceTabDettaglio = codiceTabDettaglio;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof CosmoRTabDettaglioTipoPraticaPK)) {
      return false;
    }
    CosmoRTabDettaglioTipoPraticaPK castOther = (CosmoRTabDettaglioTipoPraticaPK)other;
    return
        this.codiceTipoPratica.equals(castOther.codiceTipoPratica)
        && this.codiceTabDettaglio.equals(castOther.codiceTabDettaglio);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.codiceTipoPratica.hashCode();
    hash = hash * prime + this.codiceTabDettaglio.hashCode();

    return hash;
  }

    @Override
    public String toString() {
      return "CosmoRTabDettaglioTipoPraticaPK [codiceTipoPratica=" + codiceTipoPratica
          + ", codiceTabDettaglio=" + codiceTabDettaglio + "]";
    }


}

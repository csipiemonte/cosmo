/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;


/**
 * The persistent class for the cosmo_d_esito_verifica_firma database table.
 *
 */
@Entity
@Table(name = "cosmo_d_esito_verifica_firma")
@NamedQuery(name = "CosmoDEsitoVerificaFirma.findAll",
query = "SELECT c FROM CosmoDEsitoVerificaFirma c")
public class CosmoDEsitoVerificaFirma extends CosmoDEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private String codice;

  private String descrizione;

  public CosmoDEsitoVerificaFirma() {
    // NOP
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codice == null) ? 0 : codice.hashCode());
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
    CosmoDEsitoVerificaFirma other = (CosmoDEsitoVerificaFirma) obj;
    if (codice == null) {
      if (other.codice != null) {
        return false;
      }
    } else if (!codice.equals(other.codice)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CosmoDEsitoVerificaFirma [" + (codice != null ? "codice=" + codice + ", " : "")
        + (descrizione != null ? "descrizione=" + descrizione : "") + "]";
  }

}

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
 * The persistent class for the cosmo_d_tipo_firma database table.
 *
 */
@Entity
@Table(name = "cosmo_d_tipo_firma")
@NamedQuery(name = "CosmoDTipoFirma.findAll", query = "SELECT c FROM CosmoDTipoFirma c")
public class CosmoDTipoFirma extends CosmoDEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(unique = true, nullable = false, length = 100)
  private String codice;

  @Column(length = 255)
  private String descrizione;

  @Column(nullable = false)
  private Boolean estraibile;

  @Column(name = "mime_type")
  private String mimeType;

  // bi-directional many-to-one association to CosmoRFormatoFileProfiloFeqTipoFirma
  @OneToMany(mappedBy = "cosmoDTipoFirma")
  private List<CosmoRFormatoFileProfiloFeqTipoFirma> cosmoRFormatoFileProfiloFeqTipoFirmas;

  public CosmoDTipoFirma() {
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

  public Boolean getEstraibile() {
    return this.estraibile;
  }

  public void setEstraibile(Boolean estraibile) {
    this.estraibile = estraibile;
  }

  public String getMimeType() {
    return this.mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public List<CosmoRFormatoFileProfiloFeqTipoFirma> getCosmoRFormatoFileProfiloFeqTipoFirmas() {
    return this.cosmoRFormatoFileProfiloFeqTipoFirmas;
  }

  public void setCosmoRFormatoFileProfiloFeqTipoFirmas(
      List<CosmoRFormatoFileProfiloFeqTipoFirma> cosmoRFormatoFileProfiloFeqTipoFirmas) {
    this.cosmoRFormatoFileProfiloFeqTipoFirmas = cosmoRFormatoFileProfiloFeqTipoFirmas;
  }

  public CosmoRFormatoFileProfiloFeqTipoFirma addCosmoRFormatoFileProfiloFeqTipoFirma(
      CosmoRFormatoFileProfiloFeqTipoFirma cosmoRFormatoFileProfiloFeqTipoFirma) {
    getCosmoRFormatoFileProfiloFeqTipoFirmas().add(cosmoRFormatoFileProfiloFeqTipoFirma);
    cosmoRFormatoFileProfiloFeqTipoFirma.setCosmoDTipoFirma(this);

    return cosmoRFormatoFileProfiloFeqTipoFirma;
  }

  public CosmoRFormatoFileProfiloFeqTipoFirma removeCosmoRFormatoFileProfiloFeqTipoFirma(
      CosmoRFormatoFileProfiloFeqTipoFirma cosmoRFormatoFileProfiloFeqTipoFirma) {
    getCosmoRFormatoFileProfiloFeqTipoFirmas().remove(cosmoRFormatoFileProfiloFeqTipoFirma);
    cosmoRFormatoFileProfiloFeqTipoFirma.setCosmoDTipoFirma(null);

    return cosmoRFormatoFileProfiloFeqTipoFirma;
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
    CosmoDTipoFirma other = (CosmoDTipoFirma) obj;
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
    return "CosmoDTipoFirma [" + (codice != null ? "codice=" + codice + ", " : "")
        + (descrizione != null ? "descrizione=" + descrizione + ", " : "")
        + (dtFineVal != null ? "dtFineVal=" + dtFineVal + ", " : "")
        + (dtInizioVal != null ? "dtInizioVal=" + dtInizioVal : "")
        + (estraibile != null ? "estraibile=" + estraibile : "")
        + (mimeType != null ? "mimeType=" + mimeType : "") + "]";
  }

}

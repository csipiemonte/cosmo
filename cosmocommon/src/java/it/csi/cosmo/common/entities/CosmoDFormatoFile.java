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
 * The persistent class for the cosmo_d_formato_file database table.
 *
 */
@Entity
@Table(name = "cosmo_d_formato_file")
@NamedQuery(name = "CosmoDFormatoFile.findAll", query = "SELECT c FROM CosmoDFormatoFile c")
public class CosmoDFormatoFile extends CosmoDEntity {
  private static final long serialVersionUID = 1L;

  @Id
  private String codice;

  private String descrizione;

  @Column(name = "estensione_default")
  private String estensioneDefault;

  private String icona;

  @Column(name = "mime_type")
  private String mimeType;

  @Column(name = "supporta_anteprima")
  private Boolean supportaAnteprima;

  @Column(name = "supporta_sbustamento")
  private Boolean supportaSbustamento;

  @Column(name = "upload_consentito")
  private Boolean uploadConsentito;

  // bi-directional many-to-one association to CosmoRFormatoFileProfiloFeqTipoFirma
  @OneToMany(mappedBy = "cosmoDFormatoFile")
  private List<CosmoRFormatoFileProfiloFeqTipoFirma> cosmoRFormatoFileProfiloFeqTipoFirmas;

  public CosmoDFormatoFile() {
    // empty
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

  public String getEstensioneDefault() {
    return this.estensioneDefault;
  }

  public void setEstensioneDefault(String estensioneDefault) {
    this.estensioneDefault = estensioneDefault;
  }

  public String getIcona() {
    return this.icona;
  }

  public void setIcona(String icona) {
    this.icona = icona;
  }

  public String getMimeType() {
    return this.mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public Boolean getSupportaAnteprima() {
    return this.supportaAnteprima;
  }

  public void setSupportaAnteprima(Boolean supportaAnteprima) {
    this.supportaAnteprima = supportaAnteprima;
  }

  public Boolean getSupportaSbustamento() {
    return this.supportaSbustamento;
  }

  public void setSupportaSbustamento(Boolean supportaSbustamento) {
    this.supportaSbustamento = supportaSbustamento;
  }

  public Boolean getUploadConsentito() {
    return this.uploadConsentito;
  }

  public void setUploadConsentito(Boolean uploadConsentito) {
    this.uploadConsentito = uploadConsentito;
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
    cosmoRFormatoFileProfiloFeqTipoFirma.setCosmoDFormatoFile(this);

    return cosmoRFormatoFileProfiloFeqTipoFirma;
  }

  public CosmoRFormatoFileProfiloFeqTipoFirma removeCosmoRFormatoFileProfiloFeqTipoFirma(
      CosmoRFormatoFileProfiloFeqTipoFirma cosmoRFormatoFileProfiloFeqTipoFirma) {
    getCosmoRFormatoFileProfiloFeqTipoFirmas().remove(cosmoRFormatoFileProfiloFeqTipoFirma);
    cosmoRFormatoFileProfiloFeqTipoFirma.setCosmoDFormatoFile(null);

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
    CosmoDFormatoFile other = (CosmoDFormatoFile) obj;
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
    return "CosmoDFormatoFile [" + (codice != null ? "codice=" + codice + ", " : "")
        + (icona != null ? "icona=" + icona + ", " : "")
        + (mimeType != null ? "mimeType=" + mimeType : "") + "]";
  }

}

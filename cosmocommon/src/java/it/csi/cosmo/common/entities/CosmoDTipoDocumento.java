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
 * The persistent class for the cosmo_d_tipo_documento database table.
 *
 */
@Entity
@Table(name = "cosmo_d_tipo_documento")
@NamedQuery(name = "CosmoDTipoDocumento.findAll", query = "SELECT c FROM CosmoDTipoDocumento c")
public class CosmoDTipoDocumento extends CosmoDEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(unique = true, nullable = false, length = 100)
  private String codice;

  @Column(length = 100)
  private String descrizione;

  private Boolean firmabile;

  @Column(name = "codice_stardas")
  private String codiceStardas;

  @Column(name = "dimensione_massima")
  private Long dimensioneMassima;

  // bi-directional many-to-one association to CosmoRTipodocTipopratica
  @OneToMany(mappedBy = "cosmoDTipoDocumento")
  private List<CosmoRTipodocTipopratica> cosmoRTipodocTipopraticas;

  // bi-directional many-to-one association to CosmoTDocumento
  @OneToMany(mappedBy = "tipo")
  private List<CosmoTDocumento> cosmoTDocumentos;

  // bi-directional many-to-one association to CosmoRFormatoFileTipoDocumento
  @OneToMany(mappedBy = "cosmoDTipoDocumento")
  private List<CosmoRFormatoFileTipoDocumento> cosmoRFormatoFileTipoDocumentos;

  // bi-directional many-to-one association to CosmoRTipoDocumentoTipoDocumento
  @OneToMany(mappedBy = "cosmoDTipoDocumentoAllegato")
  private List<CosmoRTipoDocumentoTipoDocumento> cosmoRTipoDocumentoTipoDocumentosAllegato;

  // bi-directional many-to-one association to CosmoRTipoDocumentoTipoDocumento
  @OneToMany(mappedBy = "cosmoDTipoDocumentoPadre")
  private List<CosmoRTipoDocumentoTipoDocumento> cosmoRTipoDocumentoTipoDocumentosPadre;

  // bi-directional many-to-one association to CosmoTTemplateFea
  @OneToMany(mappedBy = "tipologiaDocumento")
  private List<CosmoTTemplateFea> cosmoTTemplateFeas;

  public CosmoDTipoDocumento() {
    // empty constructor
  }

  public String getCodice() {
    return this.codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public String getCodiceStardas() {
    return codiceStardas;
  }

  public void setCodiceStardas(String codiceStardas) {
    this.codiceStardas = codiceStardas;
  }

  public String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public Boolean getFirmabile() {
    return this.firmabile;
  }

  public void setFirmabile(Boolean firmabile) {
    this.firmabile = firmabile;
  }

  public Long getDimensioneMassima() {
    return dimensioneMassima;
  }

  public void setDimensioneMassima(Long dimensioneMassima) {
    this.dimensioneMassima = dimensioneMassima;
  }

  public List<CosmoRTipodocTipopratica> getCosmoRTipodocTipopraticas() {
    return this.cosmoRTipodocTipopraticas;
  }

  public void setCosmoRTipodocTipopraticas(
      List<CosmoRTipodocTipopratica> cosmoRTipodocTipopraticas) {
    this.cosmoRTipodocTipopraticas = cosmoRTipodocTipopraticas;
  }

  public CosmoRTipodocTipopratica addCosmoRTipodocTipopratica(
      CosmoRTipodocTipopratica cosmoRTipodocTipopratica) {
    getCosmoRTipodocTipopraticas().add(cosmoRTipodocTipopratica);
    cosmoRTipodocTipopratica.setCosmoDTipoDocumento(this);

    return cosmoRTipodocTipopratica;
  }

  public CosmoRTipodocTipopratica removeCosmoRTipodocTipopratica(
      CosmoRTipodocTipopratica cosmoRTipodocTipopratica) {
    getCosmoRTipodocTipopraticas().remove(cosmoRTipodocTipopratica);
    cosmoRTipodocTipopratica.setCosmoDTipoDocumento(null);

    return cosmoRTipodocTipopratica;
  }

  public List<CosmoTDocumento> getCosmoTDocumentos() {
    return this.cosmoTDocumentos;
  }

  public void setCosmoTDocumentos(List<CosmoTDocumento> cosmoTDocumentos) {
    this.cosmoTDocumentos = cosmoTDocumentos;
  }

  public CosmoTDocumento addCosmoTDocumento(CosmoTDocumento cosmoTDocumento) {
    getCosmoTDocumentos().add(cosmoTDocumento);
    cosmoTDocumento.setTipo(this);

    return cosmoTDocumento;
  }

  public CosmoTDocumento removeCosmoTDocumento(CosmoTDocumento cosmoTDocumento) {
    getCosmoTDocumentos().remove(cosmoTDocumento);
    cosmoTDocumento.setTipo(null);

    return cosmoTDocumento;
  }

  public List<CosmoRFormatoFileTipoDocumento> getCosmoRFormatoFileTipoDocumentos() {
    return cosmoRFormatoFileTipoDocumentos;
  }

  public void setCosmoRFormatoFileTipoDocumentos(
      List<CosmoRFormatoFileTipoDocumento> cosmoRFormatoFileTipoDocumentos) {
    this.cosmoRFormatoFileTipoDocumentos = cosmoRFormatoFileTipoDocumentos;
  }


  public List<CosmoRTipoDocumentoTipoDocumento> getCosmoRTipoDocumentoTipoDocumentosAllegato() {
    return cosmoRTipoDocumentoTipoDocumentosAllegato;
  }

  public void setCosmoRTipoDocumentoTipoDocumentosAllegato(
      List<CosmoRTipoDocumentoTipoDocumento> cosmoRTipoDocumentoTipoDocumentosAllegato) {
    this.cosmoRTipoDocumentoTipoDocumentosAllegato = cosmoRTipoDocumentoTipoDocumentosAllegato;
  }

  public List<CosmoRTipoDocumentoTipoDocumento> getCosmoRTipoDocumentoTipoDocumentosPadre() {
    return cosmoRTipoDocumentoTipoDocumentosPadre;
  }

  public void setCosmoRTipoDocumentoTipoDocumentosPadre(
      List<CosmoRTipoDocumentoTipoDocumento> cosmoRTipoDocumentoTipoDocumentosPadre) {
    this.cosmoRTipoDocumentoTipoDocumentosPadre = cosmoRTipoDocumentoTipoDocumentosPadre;
  }

  public List<CosmoTTemplateFea> getCosmoTTemplateFeas() {
    return this.cosmoTTemplateFeas;
  }

  public void setCosmoTTemplateFeas(List<CosmoTTemplateFea> cosmoTTemplateFeas) {
    this.cosmoTTemplateFeas = cosmoTTemplateFeas;
  }

  public CosmoTTemplateFea addCosmoTTemplateFea(CosmoTTemplateFea cosmoTTemplateFea) {
    getCosmoTTemplateFeas().add(cosmoTTemplateFea);
    cosmoTTemplateFea.setTipologiaDocumento(this);

    return cosmoTTemplateFea;
  }

  public CosmoTTemplateFea removeCosmoTTemplateFea(CosmoTTemplateFea cosmoTTemplateFea) {
    getCosmoTTemplateFeas().remove(cosmoTTemplateFea);
    cosmoTTemplateFea.setTipologiaDocumento(null);

    return cosmoTTemplateFea;
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
    CosmoDTipoDocumento other = (CosmoDTipoDocumento) obj;
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
    return "CosmoDTipoDocumento [codice=" + codice + ", descrizione=" + descrizione + ", firmabile="
        + firmabile + ", codiceStardas=" + codiceStardas + ", dimensioneMassima="
        + dimensioneMassima + "]";
  }
}

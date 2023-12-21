/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.sql.Timestamp;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_contenuto_documento database table.
 *
 */
@Entity
@Table(name = "cosmo_t_contenuto_documento")
@NamedQuery(name = "CosmoTContenutoDocumento.findAll",
query = "SELECT c FROM CosmoTContenutoDocumento c")
public class CosmoTContenutoDocumento extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_CONTENUTO_DOCUMENTO_ID_GENERATOR",
  sequenceName = "COSMO_T_CONTENUTO_DOCUMENTO_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
  generator = "COSMO_T_CONTENUTO_DOCUMENTO_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  private Long dimensione;

  @Column(name = "nome_file")
  private String nomeFile;

  @Column(name = "uuid_nodo")
  private String uuidNodo;

  @Column(name = "url_download")
  private String urlDownload;

  // bi-directional many-to-one association to CosmoDTipoFirma
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_tipo_firma")
  private CosmoDTipoFirma tipoFirma;

  // bi-directional many-to-one association to CosmoDFormatoFile
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_formato_file")
  private CosmoDFormatoFile formatoFile;

  // bi-directional many-to-one association to CosmoDTipoContenutoDocumento
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_tipo_contenuto")
  private CosmoDTipoContenutoDocumento tipo;

  // bi-directional many-to-one association to CosmoTContenutoDocumento
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_contenuto_sorgente")
  private CosmoTContenutoDocumento contenutoSorgente;

  // bi-directional many-to-one association to CosmoTDocumento
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_documento")
  private CosmoTDocumento documentoPadre;

  // bi-directional many-to-one association to CosmoDEsitoVerificaFirma
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_esito_verifica_firma")
  private CosmoDEsitoVerificaFirma esitoVerificaFirma;

  @Column(name = "dt_verifica_firma")
  private Timestamp dataVerificaFirma;

  // bi-directional many-to-one association to CosmoTContenutoDocumento
  @OneToMany(mappedBy = "contenutoDocumentoPadre")
  private List<CosmoTInfoVerificaFirma> infoVerificaFirme;

  // bi-directional many-to-one association to CosmoTContenutoDocumento
  @JsonIgnoreProperties("contenuto")
  @OneToMany(mappedBy = "contenuto")
  private List<CosmoTAnteprimaContenutoDocumento> anteprime;

  @Column(name = "sha_file")
  private String shaFile;

  // bi-directional many-to-one association to CosmoDTipoContenutoFirmato
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tipo_contenuto_firmato")
  private CosmoDTipoContenutoFirmato tipoContenutoFirmato;

  public CosmoTContenutoDocumento() {
    // empty
  }

  public String getUrlDownload() {
    return urlDownload;
  }

  public void setUrlDownload(String urlDownload) {
    this.urlDownload = urlDownload;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<CosmoTInfoVerificaFirma> getInfoVerificaFirme() {
    return infoVerificaFirme;
  }

  public void setInfoVerificaFirme(List<CosmoTInfoVerificaFirma> infoVerificaFirme) {
    this.infoVerificaFirme = infoVerificaFirme;
  }

  public Timestamp getDataVerificaFirma() {
    return dataVerificaFirma;
  }

  public void setDataVerificaFirma(Timestamp dataVerificaFirma) {
    this.dataVerificaFirma = dataVerificaFirma;
  }

  public CosmoDEsitoVerificaFirma getEsitoVerificaFirma() {
    return esitoVerificaFirma;
  }

  public void setEsitoVerificaFirma(CosmoDEsitoVerificaFirma esitoVerificaFirma) {
    this.esitoVerificaFirma = esitoVerificaFirma;
  }

  public CosmoDTipoFirma getTipoFirma() {
    return tipoFirma;
  }

  public void setTipoFirma(CosmoDTipoFirma tipoFirma) {
    this.tipoFirma = tipoFirma;
  }

  public Long getDimensione() {
    return this.dimensione;
  }

  public void setDimensione(Long dimensione) {
    this.dimensione = dimensione;
  }

  public String getNomeFile() {
    return this.nomeFile;
  }

  public void setNomeFile(String nomeFile) {
    this.nomeFile = nomeFile;
  }

  public String getUuidNodo() {
    return this.uuidNodo;
  }

  public void setUuidNodo(String uuidNodo) {
    this.uuidNodo = uuidNodo;
  }

  public CosmoTDocumento getDocumentoPadre() {
    return documentoPadre;
  }

  public void setDocumentoPadre(CosmoTDocumento documentoPadre) {
    this.documentoPadre = documentoPadre;
  }

  public CosmoDFormatoFile getFormatoFile() {
    return formatoFile;
  }

  public void setFormatoFile(CosmoDFormatoFile formatoFile) {
    this.formatoFile = formatoFile;
  }

  public CosmoDTipoContenutoDocumento getTipo() {
    return tipo;
  }

  public void setTipo(CosmoDTipoContenutoDocumento tipo) {
    this.tipo = tipo;
  }

  public CosmoTContenutoDocumento getContenutoSorgente() {
    return contenutoSorgente;
  }

  public void setContenutoSorgente(CosmoTContenutoDocumento contenutoSorgente) {
    this.contenutoSorgente = contenutoSorgente;
  }

  public List<CosmoTAnteprimaContenutoDocumento> getAnteprime() {
    return anteprime;
  }

  public void setAnteprime(List<CosmoTAnteprimaContenutoDocumento> anteprime) {
    this.anteprime = anteprime;
  }

  public String getShaFile() {
    return this.shaFile;
  }

  public void setShaFile(String shaFile) {
    this.shaFile = shaFile;
  }

  public CosmoDTipoContenutoFirmato getTipoContenutoFirmato() {
    return tipoContenutoFirmato;
  }

  public void setTipoContenutoFirmato(CosmoDTipoContenutoFirmato tipoContenutoFirmato) {
    this.tipoContenutoFirmato = tipoContenutoFirmato;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    CosmoTContenutoDocumento other = (CosmoTContenutoDocumento) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CosmoTContenutoDocumento [" + (id != null ? "id=" + id + ", " : "")
        + (dimensione != null ? "dimensione=" + dimensione + ", " : "")
        + (nomeFile != null ? "nomeFile=" + nomeFile + ", " : "")
        + (shaFile != null ? "shaFile=" + shaFile + ", " : "")
        + (uuidNodo != null ? "uuidNodo=" + uuidNodo : "") + "]";
  }

}

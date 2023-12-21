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
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_info_verifica_firma database table.
 *
 */
@Entity
@Table(name = "cosmo_t_info_verifica_firma")
@NamedQuery(name = "CosmoTInfoVerificaFirma.findAll",
    query = "SELECT c FROM CosmoTInfoVerificaFirma c")
public class CosmoTInfoVerificaFirma extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_INFO_VERIFICA_FIRMA_ID_GENERATOR",
      sequenceName = "COSMO_T_INFO_VERIFICA_FIRMA_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "COSMO_T_INFO_VERIFICA_FIRMA_ID_GENERATOR")
  private Long id;

  @Column(name = "cf_firmatario")
  private String cfFirmatario;

  @Column(name = "dt_verifica_firma")
  private Timestamp dtVerificaFirma;

  @Column(name = "dt_apposizione")
  private Timestamp dtApposizione;

  @Column(name = "dt_apposizione_marcatura_temporale")
  private Timestamp dtApposizioneMarcaturaTemporale;

  private String firmatario;

  private String organizzazione;

  @Column(name = "codice_errore")
  private String codiceErrore;

  // bi-directional many-to-one association to CosmoTContenutoDocumento
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_contenuto_documento")
  private CosmoTContenutoDocumento contenutoDocumentoPadre;

  // bi-directional many-to-one association to CosmoDEsitoVerificaFirma
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_esito")
  private CosmoDEsitoVerificaFirma esito;

  // bi-directional many-to-one association to CosmoTInfoVerificaFirma
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_padre")
  private CosmoTInfoVerificaFirma infoVerificaFirmaPadre;

  // bi-directional many-to-one association to CosmoTInfoVerificaFirma
  @OneToMany(mappedBy = "infoVerificaFirmaPadre")
  private List<CosmoTInfoVerificaFirma> infoVerificaFirmeFiglie;

  public CosmoTInfoVerificaFirma() {
    // NOP
  }

  public Timestamp getDtVerificaFirma() {
    return dtVerificaFirma;
  }

  public void setDtVerificaFirma(Timestamp dtVerificaFirma) {
    this.dtVerificaFirma = dtVerificaFirma;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCfFirmatario() {
    return this.cfFirmatario;
  }

  public void setCfFirmatario(String cfFirmatario) {
    this.cfFirmatario = cfFirmatario;
  }

  public Timestamp getDtApposizione() {
    return this.dtApposizione;
  }

  public void setDtApposizione(Timestamp dtApposizione) {
    this.dtApposizione = dtApposizione;
  }

  public Timestamp getDtApposizioneMarcaturaTemporale() {
    return this.dtApposizioneMarcaturaTemporale;
  }

  public void setDtApposizioneMarcaturaTemporale(Timestamp dtApposizioneMarcaturaTemporale) {
    this.dtApposizioneMarcaturaTemporale = dtApposizioneMarcaturaTemporale;
  }

  public String getFirmatario() {
    return this.firmatario;
  }

  public void setFirmatario(String firmatario) {
    this.firmatario = firmatario;
  }

  public String getOrganizzazione() {
    return this.organizzazione;
  }

  public void setOrganizzazione(String organizzazione) {
    this.organizzazione = organizzazione;
  }

  public CosmoDEsitoVerificaFirma getEsito() {
    return esito;
  }

  public void setEsito(CosmoDEsitoVerificaFirma esito) {
    this.esito = esito;
  }

  public CosmoTInfoVerificaFirma getInfoVerificaFirmaPadre() {
    return infoVerificaFirmaPadre;
  }

  public void setInfoVerificaFirmaPadre(CosmoTInfoVerificaFirma infoVerificaFirmaPadre) {
    this.infoVerificaFirmaPadre = infoVerificaFirmaPadre;
  }

  public List<CosmoTInfoVerificaFirma> getInfoVerificaFirmeFiglie() {
    return infoVerificaFirmeFiglie;
  }

  public void setInfoVerificaFirmeFiglie(List<CosmoTInfoVerificaFirma> infoVerificaFirmeFiglie) {
    this.infoVerificaFirmeFiglie = infoVerificaFirmeFiglie;
  }

  public CosmoTContenutoDocumento getContenutoDocumentoPadre() {
    return contenutoDocumentoPadre;
  }

  public void setContenutoDocumentoPadre(CosmoTContenutoDocumento contenutoDocumentoPadre) {
    this.contenutoDocumentoPadre = contenutoDocumentoPadre;
  }

  /**
   * @return the codiceErrore
   */
  public String getCodiceErrore() {
    return codiceErrore;
  }

  /**
   * @param codiceErrore the codiceErrore to set
   */
  public void setCodiceErrore(String codiceErrore) {
    this.codiceErrore = codiceErrore;
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
    CosmoTInfoVerificaFirma other = (CosmoTInfoVerificaFirma) obj;
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
    return "CosmoTInfoVerificaFirma [" + (id != null ? "id=" + id + ", " : "")
        + (cfFirmatario != null ? "cfFirmatario=" + cfFirmatario + ", " : "")
        + (esito != null ? "esito=" + esito : "")
        + (codiceErrore != null ? "codiceErrore=" + codiceErrore : "") + "]";
  }

}

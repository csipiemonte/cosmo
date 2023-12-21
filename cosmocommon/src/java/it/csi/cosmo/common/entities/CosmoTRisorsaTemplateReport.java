/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_risorsa_template_report database table.
 * 
 */
@Entity
@Table(name = "cosmo_t_risorsa_template_report")
@NamedQuery(name = "CosmoTRisorsaTemplateReport.findAll",
    query = "SELECT c FROM CosmoTRisorsaTemplateReport c")
public class CosmoTRisorsaTemplateReport extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_RISORSA_TEMPLATE_REPORT_ID_GENERATOR",
      sequenceName = "COSMO_T_RISORSA_TEMPLATE_REPORT_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "COSMO_T_RISORSA_TEMPLATE_REPORT_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name = "codice_tipo_pratica", insertable = false, updatable = false)
  private String codiceTipoPratica;

  @Column(name = "id_ente", insertable = false, updatable = false)
  private Long idEnte;

  @Column(name = "codice_template", nullable = false)
  private String codiceTemplate;

  @Column(name = "codice", nullable = false)
  private String codice;

  @Column(name = "contenuto_risorsa")
  private byte[] contenutoRisorsa;

  @Column(name = "nodo_risorsa")
  private String nodoRisorsa;

  // bi-directional many-to-one association to CosmoDTipoPratica
  @ManyToOne
  @JoinColumn(name = "codice_tipo_pratica")
  private CosmoDTipoPratica tipoPratica;

  // bi-directional many-to-one association to CosmoTEnte
  @ManyToOne
  @JoinColumn(name = "id_ente")
  private CosmoTEnte ente;

  public CosmoTRisorsaTemplateReport() {
    // NOP
  }

  public String getCodiceTemplate() {
    return codiceTemplate;
  }

  public void setCodiceTemplate(String codiceTemplate) {
    this.codiceTemplate = codiceTemplate;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCodice() {
    return this.codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public byte[] getContenutoRisorsa() {
    return this.contenutoRisorsa;
  }

  public void setContenutoRisorsa(byte[] contenutoRisorsa) {
    this.contenutoRisorsa = contenutoRisorsa;
  }

  public String getNodoRisorsa() {
    return this.nodoRisorsa;
  }

  public void setNodoRisorsa(String nodoRisorsa) {
    this.nodoRisorsa = nodoRisorsa;
  }

  public String getCodiceTipoPratica() {
    return codiceTipoPratica;
  }

  public void setCodiceTipoPratica(String codiceTipoPratica) {
    this.codiceTipoPratica = codiceTipoPratica;
  }

  public Long getIdEnte() {
    return idEnte;
  }

  public void setIdEnte(Long idEnte) {
    this.idEnte = idEnte;
  }

  public CosmoDTipoPratica getTipoPratica() {
    return tipoPratica;
  }

  public void setTipoPratica(CosmoDTipoPratica tipoPratica) {
    this.tipoPratica = tipoPratica;
  }

  public CosmoTEnte getEnte() {
    return ente;
  }

  public void setEnte(CosmoTEnte ente) {
    this.ente = ente;
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
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CosmoTRisorsaTemplateReport other = (CosmoTRisorsaTemplateReport) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoTRisorsaTemplateReport [id=" + id + ", codiceTipoPratica=" + codiceTipoPratica
        + ", idEnte=" + idEnte + ", codiceTemplate=" + codiceTemplate + ", codice=" + codice
        + ", nodoRisorsa=" + nodoRisorsa + "]";
  }

}

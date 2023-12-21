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
 * The persistent class for the cosmo_t_template_report database table.
 *
 */
@Entity
@Table(name = "cosmo_t_template_report")
@NamedQuery(name = "CosmoTTemplateReport.findAll", query = "SELECT c FROM CosmoTTemplateReport c")
public class CosmoTTemplateReport extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_TEMPLATE_REPORT_ID_GENERATOR",
      sequenceName = "COSMO_T_TEMPLATE_REPORT_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "COSMO_T_TEMPLATE_REPORT_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  private String codice;

  @Column(name = "codice_template_padre")
  private String codiceTemplatePadre;

  @Column(name = "codice_tipo_pratica", insertable = false, updatable = false)
  private String codiceTipoPratica;

  @Column(name = "id_ente", insertable = false, updatable = false)
  private Long idEnte;

  @Column(name = "sorgente_template")
  private byte[] sorgenteTemplate;

  @Column(name = "template_compilato")
  private byte[] templateCompilato;

  // bi-directional many-to-one association to CosmoDTipoPratica
  @ManyToOne
  @JoinColumn(name = "codice_tipo_pratica")
  private CosmoDTipoPratica tipoPratica;

  // bi-directional many-to-one association to CosmoTEnte
  @ManyToOne
  @JoinColumn(name = "id_ente")
  private CosmoTEnte ente;

  public CosmoTTemplateReport() {
    // NOP
  }

  public String getCodiceTemplatePadre() {
    return codiceTemplatePadre;
  }

  public void setCodiceTemplatePadre(String codiceTemplatePadre) {
    this.codiceTemplatePadre = codiceTemplatePadre;
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

  public byte[] getSorgenteTemplate() {
    return this.sorgenteTemplate;
  }

  public void setSorgenteTemplate(byte[] sorgenteTemplate) {
    this.sorgenteTemplate = sorgenteTemplate;
  }

  public byte[] getTemplateCompilato() {
    return this.templateCompilato;
  }

  public void setTemplateCompilato(byte[] templateCompilato) {
    this.templateCompilato = templateCompilato;
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
    CosmoTTemplateReport other = (CosmoTTemplateReport) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoTTemplateReport [id=" + id + ", codice=" + codice + ", codiceTipoPratica="
        + codiceTipoPratica + ", idEnte=" + idEnte + "]";
  }

}

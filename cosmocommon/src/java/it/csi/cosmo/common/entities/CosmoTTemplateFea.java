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
 * The persistent class for the cosmo_t_template_fea database table.
 *
 */
@Entity
@Table(name="cosmo_t_template_fea")
@NamedQuery(name="CosmoTTemplateFea.findAll", query="SELECT c FROM CosmoTTemplateFea c")
public class CosmoTTemplateFea extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_TEMPLATE_FEA_ID_GENERATOR",
  sequenceName = "COSMO_T_TEMPLATE_FEA_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COSMO_T_TEMPLATE_FEA_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name = "coordinata_x", nullable = false)
  private Double coordinataX;

  @Column(name = "coordinata_y", nullable = false)
  private Double coordinataY;

  @Column(name = "descrizione", nullable = false)
  private String descrizione;

  // bi-directional many-to-one association to CosmoTEnte
  @ManyToOne
  @JoinColumn(name = "id_ente", nullable = false)
  private CosmoTEnte ente;

  @Column(name = "pagina", nullable = false)
  private Long pagina;

  @Column(name = "caricato_da_template")
  private Boolean caricatoDaTemplate;

  // bi-directional many-to-one association to CosmoDTipoPratica
  @ManyToOne
  @JoinColumn(name = "tipologia_documento", nullable = false)
  private CosmoDTipoDocumento tipologiaDocumento;

  // bi-directional many-to-one association to CosmoDTipoPratica
  @ManyToOne
  @JoinColumn(name = "tipologia_pratica", nullable = false)
  private CosmoDTipoPratica tipologiaPratica;

  // bi-directional many-to-one association to CosmoTPratica
  @ManyToOne
  @JoinColumn(name = "id_pratica")
  private CosmoTPratica cosmoTPratica;

  public CosmoTTemplateFea() {
    // empty constructor
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public double getCoordinataX() {
    return this.coordinataX;
  }

  public void setCoordinataX(double coordinataX) {
    this.coordinataX = coordinataX;
  }

  public double getCoordinataY() {
    return this.coordinataY;
  }

  public void setCoordinataY(double coordinataY) {
    this.coordinataY = coordinataY;
  }

  public String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public CosmoTEnte getEnte() {
    return ente;
  }

  public void setEnte(CosmoTEnte ente) {
    this.ente = ente;
  }

  public Long getPagina() {
    return this.pagina;
  }

  public void setPagina(Long pagina) {
    this.pagina = pagina;
  }

  public Boolean getCaricatoDaTemplate() {
    return this.caricatoDaTemplate;
  }

  public void setCaricatoDaTemplate(Boolean caricatoDaTemplate) {
    this.caricatoDaTemplate = caricatoDaTemplate;
  }


  public CosmoDTipoDocumento getTipologiaDocumento() {
    return this.tipologiaDocumento;
  }

  public void setTipologiaDocumento(CosmoDTipoDocumento tipologiaDocumento) {
    this.tipologiaDocumento = tipologiaDocumento;
  }

  public CosmoDTipoPratica getTipologiaPratica() {
    return this.tipologiaPratica;
  }

  public void setTipologiaPratica(CosmoDTipoPratica tipologiaPratica) {
    this.tipologiaPratica = tipologiaPratica;
  }

  public CosmoTPratica getCosmoTPratica() {
    return this.cosmoTPratica;
  }

  public void setCosmoTPratica(CosmoTPratica cosmoTPratica) {
    this.cosmoTPratica = cosmoTPratica;
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
    CosmoTTemplateFea other = (CosmoTTemplateFea) obj;
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
    return "CosmoTTemplateFea [" + (id != null ? "id=" + id + ", " : "")
        + (descrizione != null ? "descrizione=" + descrizione + ", " : "")
        + (coordinataX != null ? "coordinataX=" + coordinataX + ", ": "")
        + (coordinataY != null ? "coordinataY=" + coordinataY + ", " : "")
        + (pagina != null ? "pagina=" + pagina + ", " : "")
        + (caricatoDaTemplate != null ? "caricatoDaTemplate=" + caricatoDaTemplate + ", " : "")
        + (ente != null ? "idEnte=" + ente.getId() + ", " : "")
        + (tipologiaPratica != null ? "codiceTipologiaPratica=" + tipologiaPratica.getCodice() + ", " : "")
        + (tipologiaDocumento != null ? "codiceTipologiaDocumento=" + tipologiaDocumento.getCodice() + ", " : "") + "]";
  }
}

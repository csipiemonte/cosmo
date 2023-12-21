/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * The persistent class for the cosmo_t_variabile_processo database table.
 *
 */
@Entity
@Table(name="cosmo_t_variabile_processo")
@NamedQuery(name="CosmoTVariabileProcesso.findAll", query="SELECT c FROM CosmoTVariabileProcesso c")
public class CosmoTVariabileProcesso extends CosmoTEntity implements CsiLogAuditedEntity {

  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }



  @Id
  @SequenceGenerator(name = "COSMO_T_VARIABILE_PROCESSO_ID_GENERATOR",
      sequenceName = "COSMO_T_VARIABILE_PROCESSO_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
  generator = "COSMO_T_VARIABILE_PROCESSO_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  // bi-directional many-to-one association to CosmoDTipoPratica
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_tipo_pratica")
  private CosmoDTipoPratica tipoPratica;

  @Column(name = "nome_variabile", nullable = false)
  private String nomeVariabile;

  @Column(name = "nome_variabile_flowable", nullable = false)
  private String nomeVariabileFlowable;

  @Column(name = "visualizzare_in_tabella")
  private Boolean visualizzareInTabella;

  // bi-directional many-to-one association to CosmoDFiltroCampo
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_filtro_campo")
  private CosmoDFiltroCampo filtroCampo;

  // bi-directional many-to-one association to CosmoDFormatoCampo
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_formato_campo")
  private CosmoDFormatoCampo formatoCampo;


  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }



  public String getNomeVariabile() {
    return this.nomeVariabile;
  }

  public void setNomeVariabile(String nomeVariabile) {
    this.nomeVariabile = nomeVariabile;
  }


  public String getNomeVariabileFlowable() {
    return this.nomeVariabileFlowable;
  }

  public void setNomeVariabileFlowable(String nomeVariabileFlowable) {
    this.nomeVariabileFlowable = nomeVariabileFlowable;
  }


  public Boolean getVisualizzareInTabella() {
    return this.visualizzareInTabella;
  }

  public void setVisualizzareInTabella(Boolean visualizzareInTabella) {
    this.visualizzareInTabella = visualizzareInTabella;
  }

  public CosmoDFiltroCampo getFiltroCampo() {
    return filtroCampo;
  }

  public void setFiltroCampo(CosmoDFiltroCampo filtroCampo) {
    this.filtroCampo = filtroCampo;
  }

  public CosmoDFormatoCampo getFormatoCampo() {
    return formatoCampo;
  }

  public void setFormatoCampo(CosmoDFormatoCampo formatoCampo) {
    this.formatoCampo = formatoCampo;
  }

  public CosmoDTipoPratica getTipoPratica() {
    return tipoPratica;
  }

  public void setTipoPratica(CosmoDTipoPratica tipoPratica) {
    this.tipoPratica = tipoPratica;
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
    CosmoTVariabileProcesso other = (CosmoTVariabileProcesso) obj;
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
    return "CosmoTVariabileProcesso [" + (id != null ? "id=" + id + ", " : "")
        + (nomeVariabile != null ? "nomeVariabile=" + nomeVariabile + ", " : "")
        + (nomeVariabileFlowable != null ? "nomeVariabileFlowable=" + nomeVariabileFlowable + ", "
            : "")
        + (tipoPratica != null ? "codice_tipo_pratica=" + tipoPratica.getCodice() + ", " : "");
  }



}

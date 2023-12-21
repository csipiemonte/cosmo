/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

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
 * The persistent class for the cosmo_t_smistamento database table.
 *
 */
@Entity
@Table(name="cosmo_t_smistamento")
@NamedQuery(name="CosmoTSmistamento.findAll", query="SELECT c FROM CosmoTSmistamento c")
public class CosmoTSmistamento extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "COSMO_T_SMISTAMENTO_ID_GENERATOR",
  sequenceName = "COSMO_T_SMISTAMENTO_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
  generator = "COSMO_T_SMISTAMENTO_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  // bi-directional many-to-one association to CosmoTPratica
  @ManyToOne
  @JoinColumn(name = "id_pratica")
  private CosmoTPratica cosmoTPratica;

  @Column(name="identificativo_evento")
  private String identificativoEvento;

  private Boolean utilizzato;

  //bi-directional many-to-one association to CosmoRSmistamentoDocumento
  @OneToMany(mappedBy = "cosmoTSmistamento", fetch = FetchType.LAZY)
  private List<CosmoRSmistamentoDocumento> cosmoRSmistamentoDocumentos;

  public CosmoTSmistamento() {
    // empty constructor
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIdentificativoEvento() {
    return this.identificativoEvento;
  }

  public void setIdentificativoEvento(String identificativoEvento) {
    this.identificativoEvento = identificativoEvento;
  }

  public Boolean getUtilizzato() {
    return this.utilizzato;
  }

  public void setUtilizzato(Boolean utilizzato) {
    this.utilizzato = utilizzato;
  }


  public List<CosmoRSmistamentoDocumento> getCosmoRSmistamentoDocumentos() {
    return this.cosmoRSmistamentoDocumentos;
  }

  public void setCosmoRSmistamentoDocumentos(List<CosmoRSmistamentoDocumento> cosmoRSmistamentoDocumentos) {
    this.cosmoRSmistamentoDocumentos = cosmoRSmistamentoDocumentos;
  }

  public CosmoRSmistamentoDocumento addCosmoRSmistamentoDocumento(CosmoRSmistamentoDocumento cosmoRSmistamentoDocumento) {
    getCosmoRSmistamentoDocumentos().add(cosmoRSmistamentoDocumento);
    cosmoRSmistamentoDocumento.setCosmoTSmistamento(this);

    return cosmoRSmistamentoDocumento;
  }

  public CosmoRSmistamentoDocumento removeCosmoRSmistamentoDocumento(CosmoRSmistamentoDocumento cosmoRSmistamentoDocumento) {
    getCosmoRSmistamentoDocumentos().remove(cosmoRSmistamentoDocumento);
    cosmoRSmistamentoDocumento.setCosmoTSmistamento(null);

    return cosmoRSmistamentoDocumento;
  }

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
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
    CosmoTSmistamento other = (CosmoTSmistamento) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoTSmistamento [id=" + id + ", identificativoEvento=" + identificativoEvento
        + "]";
  }

  public CosmoTPratica getCosmoTPratica() {
    return cosmoTPratica;
  }

  public void setCosmoTPratica(CosmoTPratica cosmoTPratica) {
    this.cosmoTPratica = cosmoTPratica;
  }

}

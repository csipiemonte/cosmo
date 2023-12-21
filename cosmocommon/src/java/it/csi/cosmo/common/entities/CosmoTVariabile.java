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
 * The persistent class for the cosmo_t_variabile database table.
 *
 */
@Entity
@Table(name="cosmo_t_variabile")
@NamedQuery(name="CosmoTVariabile.findAll", query="SELECT c FROM CosmoTVariabile c")
public class CosmoTVariabile extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_VARIABILE_ID_GENERATOR",
      sequenceName = "COSMO_T_VARIABILE_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COSMO_T_VARIABILE_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name = "bytearray_value")
  private byte[] bytearrayValue;

  @Column(name = "double_value")
  private Double doubleValue;

  // bi-directional many-to-one association to CosmoTPratica
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_pratica")
  private CosmoTPratica cosmoTPratica;

  @Column(name = "long_value")
  private Long longValue;

  @Column(length = 255, nullable = false)
  private String nome;

  @Column(name = "text_value", length = 4000)
  private String textValue;

  @Column(name = "type_name", length = 255, nullable = false)
  private String typeName;

  @Column(name = "json_value")
  private String jsonValue;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public byte[] getBytearrayValue() {
    return bytearrayValue;
  }

  public void setBytearrayValue(byte[] bytearrayValue) {
    this.bytearrayValue = bytearrayValue;
  }

  public Double getDoubleValue() {
    return doubleValue;
  }

  public void setDoubleValue(Double doubleValue) {
    this.doubleValue = doubleValue;
  }

  public Long getLongValue() {
    return longValue;
  }

  public void setLongValue(Long longValue) {
    this.longValue = longValue;
  }

  public CosmoTPratica getCosmoTPratica() {
    return cosmoTPratica;
  }

  public void setCosmoTPratica(CosmoTPratica cosmoTPratica) {
    this.cosmoTPratica = cosmoTPratica;
  }

  public String getNome() {
    return this.nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getTextValue() {
    return textValue;
  }

  public void setTextValue(String textValue) {
    this.textValue = textValue;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public String getJsonValue() {
    return jsonValue;
  }

  public void setJsonValue(String jsonValue) {
    this.jsonValue = jsonValue;
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
    CosmoTVariabile other = (CosmoTVariabile) obj;
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
    return "CosmoTVariabile [" + (id != null ? "id=" + id + ", " : "")
        + (doubleValue != null ? "doubleValue=" + doubleValue + ", " : "")
        + (cosmoTPratica != null ? "idPratica=" + cosmoTPratica.getId() + ", " : "")
        + (longValue != null ? "longValue=" + longValue + ", " : "")
        + (nome != null ? "nome=" + nome + ", " : "")
        + (textValue != null ? "textValue" + textValue + ", " : "")
        + (jsonValue != null ? "jsonValue" + jsonValue + ", " : "")
        + (typeName != null ? "typeName=" + typeName + ", " : "") + "]";
  }
}

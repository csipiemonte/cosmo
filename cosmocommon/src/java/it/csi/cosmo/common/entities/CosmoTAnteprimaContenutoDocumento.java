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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_contenuto_documento database table.
 *
 */
@Entity
@Table(name = "cosmo_t_anteprima_contenuto_documento")
@NamedQuery(name = "CosmoTAnteprimaContenutoDocumento.findAll",
    query = "SELECT c FROM CosmoTAnteprimaContenutoDocumento c")
public class CosmoTAnteprimaContenutoDocumento extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_ANTEPRIMA_CONTENUTO_DOCUMENTO_ID_GENERATOR",
      sequenceName = "COSMO_T_ANTEPRIMA_CONTENUTO_DOCUMENTO_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "COSMO_T_ANTEPRIMA_CONTENUTO_DOCUMENTO_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name = "dimensione", nullable = true)
  private Long dimensione;

  @Column(name = "nome_file", nullable = false, length = 255)
  private String nomeFile;

  @Column(name = "uuid_nodo", nullable = false, length = 255)
  private String uuidNodo;

  @Column(name = "share_url", nullable = true, length = 2000)
  private String shareUrl;

  // bi-directional many-to-one association to CosmoDFormatoFile
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_formato_file")
  private CosmoDFormatoFile formatoFile;

  // bi-directional many-to-one association to CosmoTContenutoDocumento
  @JsonIgnoreProperties("anteprime")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_contenuto")
  private CosmoTContenutoDocumento contenuto;

  public CosmoTAnteprimaContenutoDocumento() {
    // empty
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getDimensione() {
    return dimensione;
  }

  public void setDimensione(Long dimensione) {
    this.dimensione = dimensione;
  }

  public String getNomeFile() {
    return nomeFile;
  }

  public void setNomeFile(String nomeFile) {
    this.nomeFile = nomeFile;
  }

  public String getUuidNodo() {
    return uuidNodo;
  }

  public void setUuidNodo(String uuidNodo) {
    this.uuidNodo = uuidNodo;
  }

  public String getShareUrl() {
    return shareUrl;
  }

  public void setShareUrl(String shareUrl) {
    this.shareUrl = shareUrl;
  }

  public CosmoDFormatoFile getFormatoFile() {
    return formatoFile;
  }

  public void setFormatoFile(CosmoDFormatoFile formatoFile) {
    this.formatoFile = formatoFile;
  }

  public CosmoTContenutoDocumento getContenuto() {
    return contenuto;
  }

  public void setContenuto(CosmoTContenutoDocumento contenuto) {
    this.contenuto = contenuto;
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
    CosmoTAnteprimaContenutoDocumento other = (CosmoTAnteprimaContenutoDocumento) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoTAnteprimaContenutoDocumento [id=" + id + ", dimensione=" + dimensione
        + ", nomeFile=" + nomeFile + ", uuidNodo=" + uuidNodo + ", shareUrl=" + shareUrl
        + ", formatoFile=" + formatoFile + "]";
  }

}

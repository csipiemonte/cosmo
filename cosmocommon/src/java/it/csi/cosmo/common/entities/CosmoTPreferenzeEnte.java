/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import javax.persistence.Column;
import javax.persistence.Convert;
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
import it.csi.cosmo.common.entities.converter.PreferenzeEnteConverter;
import it.csi.cosmo.common.entities.dto.PreferenzeEnteEntity;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;

/**
 * The persistent class for the cosmo_t_preferenze_ente database table.
 *
 */
@Entity
@Table(name="cosmo_t_preferenze_ente")
@NamedQuery(name="CosmoTPreferenzeEnte.findAll", query="SELECT c FROM CosmoTPreferenzeEnte c")
public class CosmoTPreferenzeEnte extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_PREFERENZE_ENTE_ID_GENERATOR",
      sequenceName = "COSMO_T_PREFERENZE_ENTE_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "COSMO_T_PREFERENZE_ENTE_ID_GENERATOR")
  @Column(unique=true, nullable=false)
  private Long id;

  @Column(nullable = false, columnDefinition = "json")
  @Convert(converter = PreferenzeEnteConverter.class)
  private PreferenzeEnteEntity valore;

  @Column(nullable=false, length=12)
  private String versione;

  //bi-directional many-to-one association to CosmoTEnte
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="id_ente", nullable=false)
  private CosmoTEnte cosmoTEnte;

  public CosmoTPreferenzeEnte() {
    // empty constructor
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PreferenzeEnteEntity getValore() {
    return this.valore;
  }

  public void setValore(PreferenzeEnteEntity valore) {
    this.valore = valore;
  }

  public String getVersione() {
    return this.versione;
  }

  public void setVersione(String versione) {
    this.versione = versione;
  }

  public CosmoTEnte getCosmoTEnte() {
    return this.cosmoTEnte;
  }

  public void setCosmoTEnte(CosmoTEnte cosmoTEnte) {
    this.cosmoTEnte = cosmoTEnte;
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
    CosmoTPreferenzeEnte other = (CosmoTPreferenzeEnte) obj;
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
    return "CosmoTPreferenzeEnte [" + (id != null ? "id=" + id + ", " : "")
        + (valore != null ? "valore=" + valore + ", " : "")
        + (versione != null ? "versione=" + versione : "") + "]";
  }

}

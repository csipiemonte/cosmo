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
import it.csi.cosmo.common.entities.converter.PreferenzeUtenteConverter;
import it.csi.cosmo.common.entities.dto.PreferenzeUtenteEntity;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_preferenze_utente database table.
 *
 */
@Entity
@Table(name = "cosmo_t_preferenze_utente")
@NamedQuery(name = "CosmoTPreferenzeUtente.findAll",
query = "SELECT c FROM CosmoTPreferenzeUtente c")
public class CosmoTPreferenzeUtente extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_PREFERENZE_UTENTE_ID_GENERATOR",
      sequenceName = "COSMO_T_PREFERENZE_UTENTE_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "COSMO_T_PREFERENZE_UTENTE_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(nullable = false, columnDefinition = "json")
  @Convert(converter = PreferenzeUtenteConverter.class)
  private PreferenzeUtenteEntity valore;

  @Column(nullable = false, length = 12)
  private String versione;

  // bi-directional many-to-one association to CosmoTUtente
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_utente", nullable = false)
  private CosmoTUtente cosmoTUtente;

  public CosmoTPreferenzeUtente() {
    // empty constructor
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PreferenzeUtenteEntity getValore() {
    return this.valore;
  }

  public void setValore(PreferenzeUtenteEntity valore) {
    this.valore = valore;
  }

  public String getVersione() {
    return this.versione;
  }

  public void setVersione(String versione) {
    this.versione = versione;
  }

  public CosmoTUtente getCosmoTUtente() {
    return this.cosmoTUtente;
  }

  public void setCosmoTUtente(CosmoTUtente cosmoTUtente) {
    this.cosmoTUtente = cosmoTUtente;
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
    CosmoTPreferenzeUtente other = (CosmoTPreferenzeUtente) obj;
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
    return "CosmoTPreferenzeUtente [" + (id != null ? "id=" + id + ", " : "")
        + (valore != null ? "valore=" + valore + ", " : "")
        + (versione != null ? "versione=" + versione : "") + "]";
  }

}

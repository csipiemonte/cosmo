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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;

/**
 *
 */

/**
 * The persistent class for the cosmo_t_utente_gruppo database table.
 *
 */
@Entity
@Table(name = "cosmo_t_utente_gruppo")
@NamedQuery(name = "CosmoTUtenteGruppo.findAll", query = "SELECT c FROM CosmoTUtenteGruppo c")
public class CosmoTUtenteGruppo extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "COSMO_T_UTENTE_GRUPPO_ID_GENERATOR",
      sequenceName = "COSMO_T_UTENTE_GRUPPO_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "COSMO_T_UTENTE_GRUPPO_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name = "id_gruppo", nullable = false)
  private Long idGruppo;

  @Column(name = "id_utente", nullable = false)
  private Long idUtente;

  // bi-directional many-to-one association to CosmoTGruppo
  @JsonIgnoreProperties("associazioniUtenti")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_gruppo", nullable = false, insertable = false, updatable = false)
  private CosmoTGruppo gruppo;

  // bi-directional many-to-one association to CosmoTGruppo
  @JsonIgnoreProperties("cosmoTGruppos")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_utente", nullable = false, insertable = false, updatable = false)
  private CosmoTUtente utente;

  // bi-directional many-to-one association to CosmoRUtenteGruppoTag
  @OneToMany(mappedBy = "cosmoTUtenteGruppo")
  private List<CosmoRUtenteGruppoTag> cosmoRUtenteGruppoTags;

  public CosmoTUtenteGruppo() {
    // Empty constructor
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getIdGruppo() {
    return this.idGruppo;
  }

  public void setIdGruppo(Long idGruppo) {
    this.idGruppo = idGruppo;
  }

  public Long getIdUtente() {
    return this.idUtente;
  }

  public void setIdUtente(Long idUtente) {
    this.idUtente = idUtente;
  }

  public List<CosmoRUtenteGruppoTag> getCosmoRUtenteGruppoTags() {
    return this.cosmoRUtenteGruppoTags;
  }

  public void setCosmoRUtenteGruppoTags(List<CosmoRUtenteGruppoTag> cosmoRUtenteGruppoTags) {
    this.cosmoRUtenteGruppoTags = cosmoRUtenteGruppoTags;
  }

  public CosmoRUtenteGruppoTag addCosmoRUtenteGruppoTag(
      CosmoRUtenteGruppoTag cosmoRUtenteGruppoTag) {
    getCosmoRUtenteGruppoTags().add(cosmoRUtenteGruppoTag);
    cosmoRUtenteGruppoTag.setCosmoTUtenteGruppo(this);

    return cosmoRUtenteGruppoTag;
  }

  public CosmoRUtenteGruppoTag removeCosmoRUtenteGruppoTag(
      CosmoRUtenteGruppoTag cosmoRUtenteGruppoTag) {
    getCosmoRUtenteGruppoTags().remove(cosmoRUtenteGruppoTag);
    cosmoRUtenteGruppoTag.setCosmoTUtenteGruppo(null);

    return cosmoRUtenteGruppoTag;
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
    CosmoTUtenteGruppo other = (CosmoTUtenteGruppo) obj;
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
    return "CosmoTUtenteGruppo [" + (id != null ? "id=" + id : "") + "]";
  }

  public CosmoTGruppo getGruppo() {
    return gruppo;
  }

  public void setGruppo(CosmoTGruppo gruppo) {
    this.gruppo = gruppo;
  }

  public CosmoTUtente getUtente() {
    return utente;
  }

  public void setUtente(CosmoTUtente utente) {
    this.utente = utente;
  }

  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

}

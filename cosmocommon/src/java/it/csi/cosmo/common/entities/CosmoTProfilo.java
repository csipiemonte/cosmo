/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_profilo database table.
 *
 */
@Entity
@Table(name = "cosmo_t_profilo")
@NamedQuery(name = "CosmoTProfilo.findAll", query = "SELECT c FROM CosmoTProfilo c")
public class CosmoTProfilo extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_PROFILO_ID_GENERATOR", sequenceName = "COSMO_T_PROFILO_ID_SEQ",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COSMO_T_PROFILO_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name = "codice", nullable = false, length = 100)
  private String codice;

  @Column(name = "descrizione", length = 255)
  private String descrizione;

  @Column(name = "assegnabile", nullable = false)
  private Boolean assegnabile;

  // bi-directional many-to-one association to CosmoRUtenteProfilo
  @OneToMany(mappedBy = "cosmoTProfilo")
  private List<CosmoRUtenteProfilo> cosmoRUtenteProfilos;

  // bi-directional many-to-many association to CosmoDUseCase
  @ManyToMany
  @JoinTable(name = "cosmo_r_profilo_use_case",
      joinColumns = {@JoinColumn(name = "id_profilo", nullable = false)},
      inverseJoinColumns = {@JoinColumn(name = "codice_use_case", nullable = false)})
  private List<CosmoDUseCase> cosmoDUseCases;

  public CosmoTProfilo() {
    // empty constructor
  }

  public Boolean getAssegnabile() {
    return assegnabile;
  }

  public void setAssegnabile(Boolean assegnabile) {
    this.assegnabile = assegnabile;
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

  public String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public List<CosmoRUtenteProfilo> getCosmoRUtenteProfilos() {
    return this.cosmoRUtenteProfilos;
  }

  public void setCosmoRUtenteProfilos(List<CosmoRUtenteProfilo> cosmoRUtenteProfilos) {
    this.cosmoRUtenteProfilos = cosmoRUtenteProfilos;
  }

  public CosmoRUtenteProfilo addCosmoRUtenteProfilo(CosmoRUtenteProfilo cosmoRUtenteProfilo) {
    getCosmoRUtenteProfilos().add(cosmoRUtenteProfilo);
    cosmoRUtenteProfilo.setCosmoTProfilo(this);

    return cosmoRUtenteProfilo;
  }

  public CosmoRUtenteProfilo removeCosmoRUtenteProfilo(CosmoRUtenteProfilo cosmoRUtenteProfilo) {
    getCosmoRUtenteProfilos().remove(cosmoRUtenteProfilo);
    cosmoRUtenteProfilo.setCosmoTProfilo(null);

    return cosmoRUtenteProfilo;
  }

  public List<CosmoDUseCase> getCosmoDUseCases() {
    return this.cosmoDUseCases;
  }

  public void setCosmoDUseCases(List<CosmoDUseCase> cosmoDUseCases) {
    this.cosmoDUseCases = cosmoDUseCases;
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
    CosmoTProfilo other = (CosmoTProfilo) obj;
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
    return "CosmoTProfilo [" + (id != null ? "id=" + id + ", " : "")
        + (codice != null ? "codice=" + codice + ", " : "")
        + (descrizione != null ? "descrizione=" + descrizione + ", " : "") + "]";
  }

}

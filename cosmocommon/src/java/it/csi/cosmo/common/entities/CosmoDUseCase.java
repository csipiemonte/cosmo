/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;


/**
 * The persistent class for the cosmo_d_use_case database table.
 *
 */
@Entity
@Table(name = "cosmo_d_use_case")
@NamedQuery(name = "CosmoDUseCase.findAll", query = "SELECT c FROM CosmoDUseCase c")
public class CosmoDUseCase extends CosmoDEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(unique = true, nullable = false, length = 100)
  private String codice;

  @Column(nullable = false, length = 255)
  private String descrizione;

  // bi-directional many-to-one association to CosmoDCategoriaUseCase
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_categoria", nullable = false)
  private CosmoDCategoriaUseCase cosmoDCategoriaUseCase;

  // bi-directional many-to-many association to CosmoTProfilo
  @ManyToMany(mappedBy = "cosmoDUseCases")
  private List<CosmoTProfilo> cosmoTProfilos;

  public CosmoDUseCase() {
    // empty constructor
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

  public CosmoDCategoriaUseCase getCosmoDCategoriaUseCase() {
    return this.cosmoDCategoriaUseCase;
  }

  public void setCosmoDCategoriaUseCase(CosmoDCategoriaUseCase cosmoDCategoriaUseCase) {
    this.cosmoDCategoriaUseCase = cosmoDCategoriaUseCase;
  }

  public List<CosmoTProfilo> getCosmoTProfilos() {
    return this.cosmoTProfilos;
  }

  public void setCosmoTProfilos(List<CosmoTProfilo> cosmoTProfilos) {
    this.cosmoTProfilos = cosmoTProfilos;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codice == null) ? 0 : codice.hashCode());
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
    CosmoDUseCase other = (CosmoDUseCase) obj;
    if (codice == null) {
      if (other.codice != null) {
        return false;
      }
    } else if (!codice.equals(other.codice)) {
      return false;
    }
    if (dtInizioVal == null) {
      if (other.dtInizioVal != null) {
        return false;
      }
    } else if (!dtInizioVal.equals(other.dtInizioVal)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CosmoDUseCase [" + (codice != null ? "codice=" + codice + ", " : "")
        + (descrizione != null ? "descrizione=" + descrizione + ", " : "")
        + (dtFineVal != null ? "dtFineVal=" + dtFineVal + ", " : "")
        + (dtInizioVal != null ? "dtInizioVal=" + dtInizioVal : "") + "]";
  }

}

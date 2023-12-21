/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;


/**
 * The persistent class for the cosmo_d_categoria_use_case database table.
 *
 */
@Entity
@Table(name = "cosmo_d_categoria_use_case")
@NamedQuery(name = "CosmoDCategoriaUseCase.findAll",
query = "SELECT c FROM CosmoDCategoriaUseCase c")
public class CosmoDCategoriaUseCase extends CosmoDEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(unique = true, nullable = false, length = 100)
  private String codice;

  @Column(nullable = false, length = 255)
  private String descrizione;

  // bi-directional many-to-one association to CosmoDUseCase
  @OneToMany(mappedBy = "cosmoDCategoriaUseCase")
  private List<CosmoDUseCase> cosmoDUseCases;

  public CosmoDCategoriaUseCase() {
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

  public List<CosmoDUseCase> getCosmoDUseCases() {
    return this.cosmoDUseCases;
  }

  public void setCosmoDUseCases(List<CosmoDUseCase> cosmoDUseCases) {
    this.cosmoDUseCases = cosmoDUseCases;
  }

  public CosmoDUseCase addCosmoDUseCas(CosmoDUseCase cosmoDUseCas) {
    getCosmoDUseCases().add(cosmoDUseCas);
    cosmoDUseCas.setCosmoDCategoriaUseCase(this);

    return cosmoDUseCas;
  }

  public CosmoDUseCase removeCosmoDUseCas(CosmoDUseCase cosmoDUseCas) {
    getCosmoDUseCases().remove(cosmoDUseCas);
    cosmoDUseCas.setCosmoDCategoriaUseCase(null);

    return cosmoDUseCas;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codice == null) ? 0 : codice.hashCode());
    result = prime * result + ((dtInizioVal == null) ? 0 : dtInizioVal.hashCode());
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
    CosmoDCategoriaUseCase other = (CosmoDCategoriaUseCase) obj;
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
    return "CosmoDCategoriaUseCase [" + (codice != null ? "codice=" + codice + ", " : "")
        + (descrizione != null ? "descrizione=" + descrizione + ", " : "")
        + (dtFineVal != null ? "dtFineVal=" + dtFineVal + ", " : "")
        + (dtInizioVal != null ? "dtInizioVal=" + dtInizioVal : "") + "]";
  }

}

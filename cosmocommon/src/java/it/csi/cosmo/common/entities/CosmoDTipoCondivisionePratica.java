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
 * The persistent class for the cosmo_d_tipo_condivisione_pratica database table.
 *
 */
@Entity
@Table(name = "cosmo_d_tipo_condivisione_pratica")
@NamedQuery(name = "CosmoDTipoCondivisionePratica.findAll",
query = "SELECT c FROM CosmoDTipoCondivisionePratica c")
public class CosmoDTipoCondivisionePratica extends CosmoDEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(unique = true, nullable = false, length = 10)
  private String codice;

  @Column(length = 100)
  private String descrizione;

  // bi-directional many-to-one association to CosmoRPraticaUtenteGruppo
  @OneToMany(mappedBy = "cosmoDTipoCondivisionePratica")
  private List<CosmoRPraticaUtenteGruppo> cosmoRPraticaUtenteGruppos;

  public CosmoDTipoCondivisionePratica() {
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

  public List<CosmoRPraticaUtenteGruppo> getCosmoRPraticaUtenteGruppos() {
    return this.cosmoRPraticaUtenteGruppos;
  }

  public void setCosmoRPraticaUtenteGruppos(
      List<CosmoRPraticaUtenteGruppo> cosmoRPraticaUtenteGruppos) {
    this.cosmoRPraticaUtenteGruppos = cosmoRPraticaUtenteGruppos;
  }

  public CosmoRPraticaUtenteGruppo addCosmoRPraticaUtente(
      CosmoRPraticaUtenteGruppo cosmoRPraticaUtenteGruppos) {
    getCosmoRPraticaUtenteGruppos().add(cosmoRPraticaUtenteGruppos);
    cosmoRPraticaUtenteGruppos.setCosmoDTipoCondivisionePratica(this);

    return cosmoRPraticaUtenteGruppos;
  }

  public CosmoRPraticaUtenteGruppo removeCosmoRPraticaUtente(
      CosmoRPraticaUtenteGruppo cosmoRPraticaUtenteGruppos) {
    getCosmoRPraticaUtenteGruppos().remove(cosmoRPraticaUtenteGruppos);
    cosmoRPraticaUtenteGruppos.setCosmoDTipoCondivisionePratica(null);

    return cosmoRPraticaUtenteGruppos;
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
    CosmoDTipoCondivisionePratica other = (CosmoDTipoCondivisionePratica) obj;
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
    return "CosmoDTipoCondivisionePratica [" + (codice != null ? "codice=" + codice + ", " : "")
        + (descrizione != null ? "descrizione=" + descrizione + ", " : "")
        + (dtFineVal != null ? "dtFineVal=" + dtFineVal + ", " : "")
        + (dtInizioVal != null ? "dtInizioVal=" + dtInizioVal : "") + "]";
  }

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

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
import it.csi.cosmo.common.entities.proto.CosmoREntity;


/**
 * The persistent class for the cosmo_r_pratica_utente database table.
 *
 */
@Entity
@Table(name = "cosmo_r_pratica_utente_gruppo")
@NamedQuery(name = "CosmoRPraticaUtenteGruppo.findAll",
query = "SELECT c FROM CosmoRPraticaUtenteGruppo c")
public class CosmoRPraticaUtenteGruppo extends CosmoREntity {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "COSMO_R_PRATICA_UTENTE_GRUPPO_ID_GENERATOR",
  sequenceName = "COSMO_R_PRATICA_UTENTE_GRUPPO_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
  generator = "COSMO_R_PRATICA_UTENTE_GRUPPO_ID_GENERATOR")
  private Long id;

  // bi-directional many-to-one association to CosmoDTipoCondivisionePratica
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_tipo_condivisione", nullable = false)
  private CosmoDTipoCondivisionePratica cosmoDTipoCondivisionePratica;

  // bi-directional many-to-one association to CosmoTGruppo
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_gruppo", nullable = false)
  private CosmoTGruppo cosmoTGruppo;

  // bi-directional many-to-one association to CosmoTPratica
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_pratica", nullable = false)
  private CosmoTPratica cosmoTPratica;

  // bi-directional many-to-one association to CosmoTUtente
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_utente", nullable = false)
  private CosmoTUtente cosmoTUtente;

  // bi-directional many-to-one association to CosmoTUtente
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_utente_cond", nullable = false)
  private CosmoTUtente cosmoTUtenteCondivisore;

  public CosmoRPraticaUtenteGruppo() {
    // empty constructor
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CosmoDTipoCondivisionePratica getCosmoDTipoCondivisionePratica() {
    return this.cosmoDTipoCondivisionePratica;
  }

  public void setCosmoDTipoCondivisionePratica(CosmoDTipoCondivisionePratica cosmoDTipoCondivisionePratica) {
    this.cosmoDTipoCondivisionePratica = cosmoDTipoCondivisionePratica;
  }

  public CosmoTGruppo getCosmoTGruppo() {
    return this.cosmoTGruppo;
  }

  public void setCosmoTGruppo(CosmoTGruppo cosmoTGruppo) {
    this.cosmoTGruppo = cosmoTGruppo;
  }

  public CosmoTPratica getCosmoTPratica() {
    return this.cosmoTPratica;
  }

  public void setCosmoTPratica(CosmoTPratica cosmoTPratica) {
    this.cosmoTPratica = cosmoTPratica;
  }

  public CosmoTUtente getCosmoTUtente() {
    return this.cosmoTUtente;
  }

  public void setCosmoTUtente(CosmoTUtente cosmoTUtente) {
    this.cosmoTUtente = cosmoTUtente;
  }

  public CosmoTUtente getCosmoTUtenteCondivisore() {
    return cosmoTUtenteCondivisore;
  }

  public void setCosmoTUtenteCondivisore(CosmoTUtente cosmoTUtenteCondivisore) {
    this.cosmoTUtenteCondivisore = cosmoTUtenteCondivisore;
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
    CosmoRPraticaUtenteGruppo other = (CosmoRPraticaUtenteGruppo) obj;
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
    return "CosmoRPraticaUtenteGruppo [" + (id != null ? "id=" + id + ", " : "") + "]";
  }

}

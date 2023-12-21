/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;


/**
 * The persistent class for the cosmo_d_operazione_fruitore database table.
 *
 */
@Entity
@Table(name = "cosmo_d_operazione_fruitore")
@NamedQuery(name = "CosmoDOperazioneFruitore.findAll",
query = "SELECT c FROM CosmoDOperazioneFruitore c")
public class CosmoDOperazioneFruitore extends CosmoDEntity {
  private static final long serialVersionUID = 1L;

  @Id
  private String codice;

  private String descrizione;

  private Boolean personalizzabile;

  public CosmoDOperazioneFruitore() {
    // NOP
  }

  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public Boolean getPersonalizzabile() {
    return personalizzabile;
  }

  public void setPersonalizzabile(Boolean personalizzabile) {
    this.personalizzabile = personalizzabile;
  }

  @Override
  public String toString() {
    return "CosmoDOperazioneFruitore [codice=" + codice + ", descrizione=" + descrizione
        + ", personalizzabile=" + personalizzabile + "]";
  }

}

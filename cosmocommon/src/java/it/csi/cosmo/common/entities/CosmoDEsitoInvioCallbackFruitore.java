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
 * The persistent class for the cosmo_d_esito_invio_callback_fruitore database table.
 * 
 */
@Entity
@Table(name = "cosmo_d_esito_invio_callback_fruitore")
@NamedQuery(name = "CosmoDEsitoInvioCallbackFruitore.findAll",
    query = "SELECT c FROM CosmoDEsitoInvioCallbackFruitore c")
public class CosmoDEsitoInvioCallbackFruitore extends CosmoDEntity {
  private static final long serialVersionUID = 1L;

  @Id
  private String codice;

  private String descrizione;

  public CosmoDEsitoInvioCallbackFruitore() {
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

  @Override
  public String toString() {
    return "CosmoDEsitoInvioCallbackFruitore [codice=" + codice + ", descrizione=" + descrizione
        + "]";
  }
}

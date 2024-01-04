/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes;

import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaProperty;

/**
 *
 */

public abstract class AggregazioneICEViewActa extends EntitaActa {

  @ActaProperty(propertyName = "idTipoAggregazione")
  protected Long idTipoAggregazione;

  @ActaProperty(propertyName = "idStato")
  protected Long idStato;

  @ActaProperty(propertyName = "ecmUuidNodo")
  protected String ecmUuidNodo;

  public Long getIdTipoAggregazione() {
    return idTipoAggregazione;
  }

  public void setIdTipoAggregazione(Long idTipoAggregazione) {
    this.idTipoAggregazione = idTipoAggregazione;
  }

  public Long getIdStato() {
    return idStato;
  }

  public void setIdStato(Long idStato) {
    this.idStato = idStato;
  }

  public String getEcmUuidNodo() {
    return ecmUuidNodo;
  }

  public void setEcmUuidNodo(String ecmUuidNodo) {
    this.ecmUuidNodo = ecmUuidNodo;
  }


}

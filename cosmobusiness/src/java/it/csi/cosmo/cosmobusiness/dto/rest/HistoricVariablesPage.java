/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.dto.rest;

import org.flowable.rest.service.api.history.HistoricVariableInstanceResponse;

/**
 *
 */

/**
 *
 *
 */
public class HistoricVariablesPage {

  private HistoricVariableInstanceResponse[] data;

  public HistoricVariablesPage() {
    // Auto-generated constructor stub
  }

  public HistoricVariableInstanceResponse[] getData() {
    return data;
  }

  public void setData(HistoricVariableInstanceResponse[] data) {
    this.data = data;
  }

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.tokenrequest;

import it.csi.cosmo.common.entities.CosmoTCredenzialiAutenticazioneFruitore;

/**
 *
 */

public class CredenzialiAutenticazioneFruitoreSandboxedNode {

  private CosmoTCredenzialiAutenticazioneFruitore entity;

  public CredenzialiAutenticazioneFruitoreSandboxedNode(
      CosmoTCredenzialiAutenticazioneFruitore entity) {
    this.entity = entity;
  }

  public String getClientId() {
    return entity.getClientId();
  }

  public String getClientSecret() {
    return entity.getClientSecret();
  }

}

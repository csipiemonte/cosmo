/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.tokenrequest;

import it.csi.cosmo.common.entities.CosmoTCredenzialiAutenticazioneFruitore;

/**
 *
 */

public class RootTokenRequestNode {

  private CosmoTCredenzialiAutenticazioneFruitore credenziali;

  private CredenzialiAutenticazioneFruitoreSandboxedNode cachedCredenziali;

  public RootTokenRequestNode(CosmoTCredenzialiAutenticazioneFruitore credenziali) {
    this.credenziali = credenziali;
  }

  public CredenzialiAutenticazioneFruitoreSandboxedNode getCredenziali() {
    if (cachedCredenziali == null) {
      cachedCredenziali = new CredenzialiAutenticazioneFruitoreSandboxedNode(credenziali);
    }

    return cachedCredenziali;
  }

}

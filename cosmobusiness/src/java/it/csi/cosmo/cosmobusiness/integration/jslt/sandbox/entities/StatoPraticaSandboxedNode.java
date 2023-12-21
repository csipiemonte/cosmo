/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.entities;

import it.csi.cosmo.common.entities.CosmoDStatoPratica;
import it.csi.cosmo.cosmobusiness.integration.jslt.JsltProviderContext;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.AbstractJsltSandboxedNode;

/**
 *
 */

public class StatoPraticaSandboxedNode extends AbstractJsltSandboxedNode {

  private CosmoDStatoPratica entity;

  public StatoPraticaSandboxedNode(JsltProviderContext ctx, CosmoDStatoPratica entity) {
    super(ctx);
    this.entity = entity;
  }

  public String getCodice() {
    return entity.getCodice();
  }

  public String getDescrizione() {
    return entity.getDescrizione();
  }

  public String getClasse() {
    return entity.getClasse();
  }
}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.entities;

import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.cosmobusiness.integration.jslt.JsltProviderContext;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.AbstractJsltSandboxedNode;

/**
 *
 */

public class TipoPraticaSandboxedNode extends AbstractJsltSandboxedNode {

  private CosmoDTipoPratica entity;

  public TipoPraticaSandboxedNode(JsltProviderContext ctx, CosmoDTipoPratica entity) {
    super(ctx);
    this.entity = entity;
  }

  public String getCodice() {
    return entity.getCodice();
  }

  public String getDescrizione() {
    return entity.getDescrizione();
  }

  public String getProcessDefinitionKey() {
    return entity.getProcessDefinitionKey();
  }

  public String getCaseDefinitionKey() {
    return entity.getCaseDefinitionKey();
  }

}

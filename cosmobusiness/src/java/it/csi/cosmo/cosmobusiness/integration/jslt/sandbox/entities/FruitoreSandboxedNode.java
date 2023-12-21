/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.entities;

import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.cosmobusiness.integration.jslt.JsltProviderContext;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.AbstractJsltSandboxedNode;

/**
 *
 */

public class FruitoreSandboxedNode extends AbstractJsltSandboxedNode {

  private CosmoTFruitore entity;

  public FruitoreSandboxedNode(JsltProviderContext ctx, CosmoTFruitore entity) {
    super(ctx);
    this.entity = entity;
  }

  public Long getId() {
    return entity.getId();
  }

  public String getNomeApp() {
    return entity.getNomeApp();
  }

  public String getUrl() {
    return entity.getUrl();
  }

}

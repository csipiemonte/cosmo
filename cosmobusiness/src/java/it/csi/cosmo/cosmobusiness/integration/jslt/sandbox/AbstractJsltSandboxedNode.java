/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.sandbox;

import it.csi.cosmo.cosmobusiness.integration.jslt.JsltProviderContext;

/**
 *
 */

public abstract class AbstractJsltSandboxedNode {

  protected JsltProviderContext context;

  public AbstractJsltSandboxedNode(JsltProviderContext ctx) {
    this.context = ctx;
  }

}

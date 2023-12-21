/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.entities;

import it.csi.cosmo.cosmobusiness.integration.jslt.JsltProviderContext;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.AbstractJsltSandboxedNode;

/**
 *
 */

public class RootSandboxedNode extends AbstractJsltSandboxedNode {

  public RootSandboxedNode(JsltProviderContext ctx) {
    super(ctx);
  }

  public PraticaSandboxedNode getPratica() {
    var loaded = this.context.getPratica();
    if (loaded == null) {
      return null;
    }
    return new PraticaSandboxedNode(this.context, loaded);
  }
  
  public ProcessoSandboxedNode getProcesso() {
    var loaded = this.context.getPratica();
    if (loaded == null) {
      return null;
    }
    return new ProcessoSandboxedNode(this.context, loaded);
  }

}

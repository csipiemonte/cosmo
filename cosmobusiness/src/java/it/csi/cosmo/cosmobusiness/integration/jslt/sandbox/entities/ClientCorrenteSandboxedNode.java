/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.entities;

import java.util.List;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.ScopeDTO;
import it.csi.cosmo.cosmobusiness.integration.jslt.JsltProviderContext;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.AbstractJsltSandboxedNode;

/**
 *
 */

public class ClientCorrenteSandboxedNode extends AbstractJsltSandboxedNode {

  private ClientInfoDTO entity;

  public ClientCorrenteSandboxedNode(JsltProviderContext ctx, ClientInfoDTO entity) {
    super(ctx);
    this.entity = entity;
  }

  public String getCodice() {
    return entity.getCodice();
  }

  public Boolean getAnonimo() {
    return entity.getAnonimo();
  }

  public String getNome() {
    return entity.getNome();
  }

  public List<ScopeDTO> getScopes() {
    return entity.getScopes();
  }

}

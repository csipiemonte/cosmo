/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.entities;

import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.cosmobusiness.integration.jslt.JsltProviderContext;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.AbstractJsltSandboxedNode;

/**
 *
 */

public class EnteSandboxedNode extends AbstractJsltSandboxedNode {

  private CosmoTEnte entity;

  public EnteSandboxedNode(JsltProviderContext ctx, CosmoTEnte entity) {
    super(ctx);
    this.entity = entity;
  }

  public Long getId() {
    return entity.getId();
  }

  public byte[] getLogo() {
    return entity.getLogo();
  }

  public String getNome() {
    return entity.getNome();
  }

  public String getCodiceIpa() {
    return entity.getCodiceIpa();
  }

  public String getCodiceFiscale() {
    return entity.getCodiceFiscale();
  }

}

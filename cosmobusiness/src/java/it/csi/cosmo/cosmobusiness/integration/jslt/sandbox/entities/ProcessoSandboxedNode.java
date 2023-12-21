/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.entities;

import java.util.HashMap;
import java.util.Map;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.cosmobusiness.integration.jslt.JsltProviderContext;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.AbstractJsltSandboxedNode;

/**
 *
 */

public class ProcessoSandboxedNode extends AbstractJsltSandboxedNode {

  private Map<String, Object> cachedVariabili;

  private CosmoTPratica entity;

  private String processInstanceId;

  public ProcessoSandboxedNode(JsltProviderContext ctx, CosmoTPratica entity) {
    super(ctx);
    this.entity = entity;
  }

  public ProcessoSandboxedNode(JsltProviderContext ctx, CosmoTPratica entity,
      String processInstanceId) {
    super(ctx);
    this.entity = entity;
    this.processInstanceId = processInstanceId;
  }

  public String getId() {
    return processInstanceId != null ? processInstanceId : entity.getProcessInstanceId();
  }

  public String getProcessInstanceId() {
    return processInstanceId != null ? processInstanceId : entity.getProcessInstanceId();
  }

  public Map<String, Object> getVariabili() {
    if (cachedVariabili == null) {
      if (this.processInstanceId != null) {
        cachedVariabili = this.context.getStatoPraticaProvider()
            .getVariabiliProcessoByProcessInstanceId(processInstanceId);
      } else {
        var loaded = this.context.getPratica();
        if (loaded == null || loaded.getProcessInstanceId() == null) {
          return new HashMap<>();
        }

        cachedVariabili =
            this.context.getStatoPraticaProvider().getVariabiliProcesso(this.context.getPratica());
      }
    }
    return cachedVariabili;
  }
}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.statopratica;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import it.csi.cosmo.cosmobusiness.integration.jslt.JsltProviderContext;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.AbstractJsltSandboxedNode;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.entities.ClientCorrenteSandboxedNode;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.entities.EnteSandboxedNode;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.entities.ProcessoSandboxedNode;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.entities.UtenteCorrenteSandboxedNode;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmobusiness.security.SecurityUtils;
import it.csi.cosmo.cosmobusiness.util.listener.SpringApplicationContextHelper;

/**
 *
 */

public class RootSandboxedNode extends AbstractJsltSandboxedNode {

  private String processInstanceId;

  public RootSandboxedNode(JsltProviderContext ctx) {
    super(ctx);
  }

  public PraticaSandboxedNode getPratica() {
    return new PraticaSandboxedNode(this.context);
  }

  public ProcessoSandboxedNode getProcesso() {
    var loaded = this.context.getPratica();
    if (this.processInstanceId == null
        && (loaded == null || loaded.getProcessInstanceId() == null)) {
      return null;
    }
    return new ProcessoSandboxedNode(this.context, loaded, this.processInstanceId);
  }

  public UtenteCorrenteSandboxedNode getUtenteCorrente() {
    return new UtenteCorrenteSandboxedNode(this.context, SecurityUtils.getUtenteCorrente());
  }

  public ClientCorrenteSandboxedNode getClientCorrente() {
    return new ClientCorrenteSandboxedNode(this.context, SecurityUtils.getClientCorrente());
  }

  public EnteSandboxedNode getEnteCorrente() {
    var entity = SecurityUtils.getUtenteCorrente();
    if (entity.getEnte() == null) {
      return null;
    }
    CosmoTEnteRepository repo = SpringApplicationContextHelper.getBean(CosmoTEnteRepository.class);
    return new EnteSandboxedNode(this.context, repo.findOne(entity.getEnte().getId()));
  }

  @JsonIgnore
  @Deprecated
  public Map<String, Object> getVariabili() {
    var p = this.getProcesso();
    if (p == null) {
      return null;
    }
    return p.getVariabili();
  }

  public void forceProcessInstanceId(String processInstanceId) {
    this.processInstanceId = processInstanceId;
  }
}

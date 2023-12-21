/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.entities;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.cosmobusiness.integration.jslt.JsltProviderContext;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.AbstractJsltSandboxedNode;

/**
 *
 */

public class AttivitaSandboxedNode extends AbstractJsltSandboxedNode {

  private CosmoTAttivita entity;

  public AttivitaSandboxedNode(JsltProviderContext ctx, CosmoTAttivita entity) {
    super(ctx);
    this.entity = entity;
  }

  public String getTaskId() {
    return entity.getTaskId();
  }

  public Long getId() {
    return entity.getId();
  }

  public String getDescrizione() {
    return entity.getDescrizione();
  }

  public Timestamp getDtInserimento() {
    return entity.getDtInserimento();
  }

  public Timestamp getDtUltimaModifica() {
    return entity.getDtUltimaModifica();
  }

  public String getLinkAttivita() {
    return entity.getLinkAttivita();
  }

  public String getNome() {
    return entity.getNome();
  }

  public AttivitaSandboxedNode getParent() {
    var loaded = entity.getParent();
    if (loaded == null) {
      return null;
    }
    return new AttivitaSandboxedNode(this.context, loaded);
  }

  public List<AttivitaSandboxedNode> getSubtasks() {
    var loaded = entity.getSubtasks();
    if (loaded == null) {
      return Collections.emptyList();
    }
    return loaded.stream().map(e -> new AttivitaSandboxedNode(this.context, e))
        .collect(Collectors.toList());
  }

}

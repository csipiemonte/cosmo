/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.entities;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.cosmobusiness.integration.jslt.JsltProviderContext;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.AbstractJsltSandboxedNode;

/**
 *
 */

public class PraticaSandboxedNode extends AbstractJsltSandboxedNode {

  private CosmoTPratica entity;

  public PraticaSandboxedNode(JsltProviderContext ctx, CosmoTPratica entity) {
    super(ctx);
    this.entity = entity;
  }

  public Long getId() {
    return entity.getId();
  }

  public String getProcessInstanceId() {
    return entity.getProcessInstanceId();
  }

  public Timestamp getDataCambioStato() {
    return entity.getDataCambioStato();
  }

  public Timestamp getDataCreazionePratica() {
    return entity.getDataCreazionePratica();
  }

  public Timestamp getDataFinePratica() {
    return entity.getDataFinePratica();
  }

  public String getIdPraticaExt() {
    return entity.getIdPraticaExt();
  }

  public String getLinkPratica() {
    return entity.getLinkPratica();
  }

  public String getMetadati() {
    return entity.getMetadati();
  }

  public String getOggetto() {
    return entity.getOggetto();
  }

  public String getRiassunto() {
    return entity.getRiassunto();
  }

  public String getUtenteCreazionePratica() {
    return entity.getUtenteCreazionePratica();
  }

  public String getUuidNodo() {
    return entity.getUuidNodo();
  }

  public StatoPraticaSandboxedNode getStato() {
    var loaded = entity.getStato();
    if (loaded == null) {
      return null;
    }
    return new StatoPraticaSandboxedNode(this.context, loaded);
  }

  public TipoPraticaSandboxedNode getTipo() {
    var loaded = entity.getTipo();
    if (loaded == null) {
      return null;
    }
    return new TipoPraticaSandboxedNode(this.context, loaded);
  }

  public EnteSandboxedNode getEnte() {
    var loaded = entity.getEnte();
    if (loaded == null) {
      return null;
    }
    return new EnteSandboxedNode(this.context, loaded);
  }

  public FruitoreSandboxedNode getFruitore() {
    var loaded = entity.getFruitore();
    if (loaded == null) {
      return null;
    }
    return new FruitoreSandboxedNode(this.context, loaded);
  }

  public List<AttivitaSandboxedNode> getAttivita() {
    var loaded = entity.getAttivita();
    if (loaded == null) {
      return Collections.emptyList();
    }
    return loaded.stream().map(e -> new AttivitaSandboxedNode(this.context, e))
        .collect(Collectors.toList());
  }

}

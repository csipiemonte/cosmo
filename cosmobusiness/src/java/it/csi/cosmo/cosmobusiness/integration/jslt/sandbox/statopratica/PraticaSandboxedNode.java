/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.statopratica;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaStatoPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AttivitaFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.DocumentoFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.MessaggioFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.StatoPraticaFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.TipoPraticaFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.UtenteFruitore;
import it.csi.cosmo.cosmobusiness.integration.jslt.JsltProviderContext;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.AbstractJsltSandboxedNode;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.entities.EnteSandboxedNode;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.entities.FruitoreSandboxedNode;

/**
 *
 */

public class PraticaSandboxedNode extends AbstractJsltSandboxedNode {

  private AggiornaStatoPraticaRequest entity;

  private List<AttivitaFruitore> attivitaCached = null;

  private List<MessaggioFruitore> commentiCached = null;

  private List<DocumentoFruitore> documentiCached = null;

  private Object metadatiCached = null;

  public PraticaSandboxedNode(JsltProviderContext ctx) {
    super(ctx);
    this.entity = ctx.getStatoPraticaProvider().getPratica(ctx.getPratica());
  }

  public EnteSandboxedNode getEnte() {
    var loaded = this.context.getPratica().getEnte();
    if (loaded == null) {
      return null;
    }
    return new EnteSandboxedNode(this.context, loaded);
  }

  public FruitoreSandboxedNode getFruitore() {
    var loaded = this.context.getPratica().getFruitore();
    if (loaded == null) {
      return null;
    }
    return new FruitoreSandboxedNode(this.context, loaded);
  }

  public String getId() {
    return entity.getId();
  }

  public String getCodiceIpaEnte() {
    return entity.getCodiceIpaEnte();
  }

  public TipoPraticaFruitore getTipo() {
    return entity.getTipo();
  }

  public String getOggetto() {
    return entity.getOggetto();
  }

  public String getRiassunto() {
    return entity.getRiassunto();
  }

  public UtenteFruitore getUtenteCreazione() {
    return entity.getUtenteCreazione();
  }

  public OffsetDateTime getDataCreazione() {
    return entity.getDataCreazione();
  }

  public OffsetDateTime getDataFine() {
    return entity.getDataFine();
  }

  public OffsetDateTime getDataCambioStato() {
    return entity.getDataCambioStato();
  }

  public OffsetDateTime getDataAggiornamento() {
    return entity.getDataAggiornamento();
  }

  public synchronized Object getMetadati() {
    if (this.metadatiCached == null) {
      var metadati = entity.getMetadati();
      if (metadati == null) {
        this.metadatiCached = null;
      } else {
        this.metadatiCached = entity.getMetadati();

        // parsing da json se e' una stringa serializzata
        if (metadati instanceof String && ((String) metadati).length() > 0
            && ((String) metadati).trim().startsWith("{")
            && ((String) metadati).trim().endsWith("}")) {

          // probabilmente un JSON MA NON SICURAMENTE
          try {
            this.metadatiCached = ObjectUtils.fromJson((String) metadati, HashMap.class);
          } catch (Exception e) {
            // fail silently
          }
        }
      }
    }
    return this.metadatiCached;
  }

  public StatoPraticaFruitore getStato() {
    return entity.getStato();
  }

  public List<AttivitaFruitore> getAttivita() {
    if (attivitaCached == null) {
      attivitaCached =
          this.context.getStatoPraticaProvider().getAttivita(this.context.getPratica());
    }
    return attivitaCached;
  }

  public List<MessaggioFruitore> getCommenti() {
    if (commentiCached == null) {
      commentiCached =
          this.context.getStatoPraticaProvider().getMessaggi(this.context.getPratica());
    }
    return commentiCached;
  }

  public List<DocumentoFruitore> getDocumenti() {
    if (documentiCached == null) {
      documentiCached =
          this.context.getStatoPraticaProvider().getDocumenti(this.context.getPratica());
    }
    return documentiCached;
  }
}

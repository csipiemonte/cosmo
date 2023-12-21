/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.dto;

import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.entities.enums.EsitoInvioCallbackFruitore;
import it.csi.cosmo.common.entities.enums.StatoCallbackFruitore;

/**
 *
 */

/**
 *
 *
 */
public class EsitoTentativoInvioCallback {

  private StatoCallbackFruitore stato;

  private EsitoInvioCallbackFruitore esito;

  private Throwable errore;

  private JsonNode risposta;

  private EsitoTentativoInvioCallback(Builder builder) {
    this.stato = builder.stato;
    this.esito = builder.esito;
    this.errore = builder.errore;
    this.risposta = builder.risposta;
  }

  public EsitoTentativoInvioCallback() {
    // Auto-generated constructor stub
  }

  public JsonNode getRisposta() {
    return risposta;
  }

  public EsitoInvioCallbackFruitore getEsito() {
    return esito;
  }

  public Throwable getErrore() {
    return errore;
  }

  public StatoCallbackFruitore getStato() {
    return stato;
  }

  /**
   * Creates builder to build {@link EsitoTentativoInvioCallback}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link EsitoTentativoInvioCallback}.
   */
  public static final class Builder {
    private StatoCallbackFruitore stato;
    private EsitoInvioCallbackFruitore esito;
    private Throwable errore;
    private JsonNode risposta;

    private Builder() {}

    public Builder withStato(StatoCallbackFruitore stato) {
      this.stato = stato;
      return this;
    }

    public Builder withEsito(EsitoInvioCallbackFruitore esito) {
      this.esito = esito;
      return this;
    }

    public Builder withErrore(Throwable errore) {
      this.errore = errore;
      return this;
    }

    public Builder withRisposta(JsonNode risposta) {
      this.risposta = risposta;
      return this;
    }

    public EsitoTentativoInvioCallback build() {
      return new EsitoTentativoInvioCallback(this);
    }
  }

}

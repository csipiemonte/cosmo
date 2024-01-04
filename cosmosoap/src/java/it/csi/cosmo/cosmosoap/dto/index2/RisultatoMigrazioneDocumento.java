/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.dto.index2;

import it.csi.cosmo.common.entities.CosmoTDocumento;

public class RisultatoMigrazioneDocumento {

  private CosmoTDocumento documento;

  private boolean successo;

  private Throwable errore;

  private RisultatoMigrazioneDocumento(Builder builder) {
    this.documento = builder.documento;
    this.successo = builder.successo;
    this.errore = builder.errore;
  }

  public RisultatoMigrazioneDocumento() {}

  public CosmoTDocumento getDocumento() {
    return documento;
  }

  public boolean isSuccesso() {
    return successo;
  }

  public Throwable getErrore() {
    return errore;
  }

  /**
   * Creates builder to build {@link RisultatoMigrazioneDocumento}.
   * 
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link RisultatoMigrazioneDocumento}.
   */
  public static final class Builder {
    private CosmoTDocumento documento;
    private boolean successo;
    private Throwable errore;

    private Builder() {}

    public Builder withDocumento(CosmoTDocumento documento) {
      this.documento = documento;
      return this;
    }

    public Builder withSuccesso(boolean successo) {
      this.successo = successo;
      return this;
    }

    public Builder withErrore(Throwable errore) {
      this.errore = errore;
      return this;
    }

    public RisultatoMigrazioneDocumento build() {
      return new RisultatoMigrazioneDocumento(this);
    }
  }


}

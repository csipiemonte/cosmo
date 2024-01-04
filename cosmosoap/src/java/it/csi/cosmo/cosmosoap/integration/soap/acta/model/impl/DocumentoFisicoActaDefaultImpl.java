/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl;

import java.time.LocalDateTime;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.DocumentoFisicoActa;


/**
 *
 */
public class DocumentoFisicoActaDefaultImpl extends DocumentoFisicoActa {

  private DocumentoFisicoActaDefaultImpl(Builder builder) {
    this.id = builder.id;
    this.dbKey = builder.dbKey;
    this.changeToken = builder.changeToken;
    this.descrizione = builder.descrizione;
    this.progressivo = builder.progressivo;
    this.dataMemorizzazione = builder.dataMemorizzazione;
  }

  public DocumentoFisicoActaDefaultImpl() {}

  /**
   * Creates builder to build {@link DocumentoFisicoActaDefaultImpl}.
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link DocumentoFisicoActaDefaultImpl}.
   */
  public static final class Builder {
    private String id;
    private String dbKey;
    private LocalDateTime changeToken;
    private String descrizione;
    private Integer progressivo;
    private LocalDateTime dataMemorizzazione;

    private Builder() {}

    public Builder withId(String id) {
      this.id = id;
      return this;
    }

    public Builder withDbKey(String dbKey) {
      this.dbKey = dbKey;
      return this;
    }

    public Builder withChangeToken(LocalDateTime changeToken) {
      this.changeToken = changeToken;
      return this;
    }

    public Builder withDescrizione(String descrizione) {
      this.descrizione = descrizione;
      return this;
    }

    public Builder withProgressivo(Integer progressivo) {
      this.progressivo = progressivo;
      return this;
    }

    public Builder withDataMemorizzazione(LocalDateTime dataMemorizzazione) {
      this.dataMemorizzazione = dataMemorizzazione;
      return this;
    }

    public DocumentoFisicoActaDefaultImpl build() {
      return new DocumentoFisicoActaDefaultImpl(this);
    }
  }

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl;

import javax.activation.DataHandler;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.ContenutoDocumentoFisicoActa;


/**
 *
 */
public class ContenutoDocumentoFisicoActaDefaultImpl extends ContenutoDocumentoFisicoActa {

  private ContenutoDocumentoFisicoActaDefaultImpl(Builder builder) {
    this.stream = builder.stream;
    this.fileName = builder.fileName;
    this.mimeType = builder.mimeType;
  }

  public ContenutoDocumentoFisicoActaDefaultImpl() {}

  /**
   * Creates builder to build {@link ContenutoDocumentoFisicoActaDefaultImpl}.
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link ContenutoDocumentoFisicoActaDefaultImpl}.
   */
  public static final class Builder {
    private DataHandler stream;
    private String fileName;
    private String mimeType;

    private Builder() {}

    public Builder withStream(DataHandler stream) {
      this.stream = stream;
      return this;
    }

    public Builder withFileName(String fileName) {
      this.fileName = fileName;
      return this;
    }

    public Builder withMimeType(String mimeType) {
      this.mimeType = mimeType;
      return this;
    }

    public ContenutoDocumentoFisicoActaDefaultImpl build() {
      return new ContenutoDocumentoFisicoActaDefaultImpl(this);
    }
  }

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes;


import javax.activation.DataHandler;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.ContenutoDocumentoFisicoActaDefaultImpl;


/**
 *
 */
public abstract class ContenutoDocumentoFisicoActa {

  public static ContenutoDocumentoFisicoActaDefaultImpl.Builder builder() {
    return ContenutoDocumentoFisicoActaDefaultImpl.builder();
  }

  protected DataHandler stream;

  protected String fileName;

  protected String mimeType;

  public DataHandler getStream() {
    return stream;
  }

  public void setStream(DataHandler stream) {
    this.stream = stream;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }


}

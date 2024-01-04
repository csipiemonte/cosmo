/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.mtom;

import java.io.InputStream;
import javax.activation.DataHandler;

/**
 *
 */

public class Index2DataHandler extends DataHandler {

  private String contentType;

  public Index2DataHandler(InputStream inputStream, String contentType) {
    super(new Index2MTOMDataSource(inputStream, contentType));
    this.contentType = contentType;
  }

  @Override
  public String getContentType() {
    return contentType;
  }

  @Override
  public String toString() {
    return "Index2DataHandler [contentType=" + contentType + "]";
  }

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.mtom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;

/**
 *
 */

public class Index2MTOMDataSource implements DataSource {

  private InputStream inputStream;

  private String contentType;

  public Index2MTOMDataSource(InputStream inputStream, String contentType) {
    this.inputStream = inputStream;
    this.contentType = contentType;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return inputStream;
  }

  @Override
  public OutputStream getOutputStream() throws IOException {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public String getContentType() {
    return contentType;
  }

  @Override
  public String getName() {
    return "InputStreamDataSource";
  }
}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing;

import java.net.URI;
import java.time.OffsetDateTime;

/**
 *
 */

public class CreatedSharedLink {

  private String filename;
  private IndexContentDisposition contentDisposition;
  private URI downloadUri;
  private IndexShareScope source;
  private OffsetDateTime fromDate;
  private OffsetDateTime toDate;

  public CreatedSharedLink() {
    // NOP
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public IndexContentDisposition getContentDisposition() {
    return contentDisposition;
  }

  public void setContentDisposition(IndexContentDisposition contentDisposition) {
    this.contentDisposition = contentDisposition;
  }

  public URI getDownloadUri() {
    return downloadUri;
  }

  public void setDownloadUri(URI downloadUri) {
    this.downloadUri = downloadUri;
  }

  public IndexShareScope getSource() {
    return source;
  }

  public void setSource(IndexShareScope source) {
    this.source = source;
  }

  public OffsetDateTime getFromDate() {
    return fromDate;
  }

  public void setFromDate(OffsetDateTime fromDate) {
    this.fromDate = fromDate;
  }

  public OffsetDateTime getToDate() {
    return toDate;
  }

  public void setToDate(OffsetDateTime toDate) {
    this.toDate = toDate;
  }

  @Override
  public String toString() {
    return "CreatedSharedLink [" + (source != null ? "source=" + source + ", " : "")
        + (fromDate != null ? "fromDate=" + fromDate + ", " : "")
        + (toDate != null ? "toDate=" + toDate : "") + "]";
  }
}

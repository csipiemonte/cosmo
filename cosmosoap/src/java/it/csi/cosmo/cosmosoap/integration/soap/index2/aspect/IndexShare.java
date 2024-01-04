/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.aspect;

import java.time.OffsetDateTime;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing.IndexShareScope;

/**
 *
 */

public class IndexShare {

  private String entry;
  private String contentHash;
  private IndexShareScope source;
  private String contentPropertyPrefixedName;
  private String resultPropertyPrefixedName;
  private String contentDisposition;
  private OffsetDateTime fromDate;
  private OffsetDateTime toDate;

  public IndexShare() {
    // NOP
  }

  public String getContentDisposition() {
    return contentDisposition;
  }

  public void setContentDisposition(String contentDisposition) {
    this.contentDisposition = contentDisposition;
  }

  public String getEntry() {
    return entry;
  }

  public void setEntry(String entry) {
    this.entry = entry;
  }

  public String getContentHash() {
    return contentHash;
  }

  public void setContentHash(String contentHash) {
    this.contentHash = contentHash;
  }

  public IndexShareScope getSource() {
    return source;
  }

  public void setSource(IndexShareScope source) {
    this.source = source;
  }

  public String getContentPropertyPrefixedName() {
    return contentPropertyPrefixedName;
  }

  public void setContentPropertyPrefixedName(String contentPropertyPrefixedName) {
    this.contentPropertyPrefixedName = contentPropertyPrefixedName;
  }

  public String getResultPropertyPrefixedName() {
    return resultPropertyPrefixedName;
  }

  public void setResultPropertyPrefixedName(String resultPropertyPrefixedName) {
    this.resultPropertyPrefixedName = resultPropertyPrefixedName;
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
    return "CreatedSharedLink [" + (contentHash != null ? "contentHash=" + contentHash + ", " : "")
        + (source != null ? "source=" + source + ", " : "")
        + (contentPropertyPrefixedName != null
        ? "contentPropertyPrefixedName=" + contentPropertyPrefixedName + ", "
            : "")
        + (resultPropertyPrefixedName != null
        ? "resultPropertyPrefixedName=" + resultPropertyPrefixedName + ", "
            : "")
        + (fromDate != null ? "fromDate=" + fromDate + ", " : "")
        + (toDate != null ? "toDate=" + toDate : "") + "]";
  }

}

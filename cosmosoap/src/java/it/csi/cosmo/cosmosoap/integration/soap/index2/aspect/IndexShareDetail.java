/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.aspect;

import java.net.URI;
import java.time.OffsetDateTime;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing.IndexShareScope;

/**
 *
 */

public class IndexShareDetail {

  private URI downloadUri;
  private IndexShareScope source;
  private String contentPropertyPrefixedName;
  private String resultPropertyPrefixedName;
  private String contentDisposition;
  private OffsetDateTime fromDate;
  private OffsetDateTime toDate;

  public IndexShareDetail() {
    // NOP
  }

  /**
   * @return the downloadUri
   */
  public URI getDownloadUri() {
    return downloadUri;
  }

  /**
   * @param downloadUri the downloadUri to set
   */
  public void setDownloadUri(URI downloadUri) {
    this.downloadUri = downloadUri;
  }

  /**
   * @return the source
   */
  public IndexShareScope getSource() {
    return source;
  }

  /**
   * @param source the source to set
   */
  public void setSource(IndexShareScope source) {
    this.source = source;
  }

  /**
   * @return the contentPropertyPrefixedName
   */
  public String getContentPropertyPrefixedName() {
    return contentPropertyPrefixedName;
  }

  /**
   * @param contentPropertyPrefixedName the contentPropertyPrefixedName to set
   */
  public void setContentPropertyPrefixedName(String contentPropertyPrefixedName) {
    this.contentPropertyPrefixedName = contentPropertyPrefixedName;
  }

  /**
   * @return the resultPropertyPrefixedName
   */
  public String getResultPropertyPrefixedName() {
    return resultPropertyPrefixedName;
  }

  /**
   * @param resultPropertyPrefixedName the resultPropertyPrefixedName to set
   */
  public void setResultPropertyPrefixedName(String resultPropertyPrefixedName) {
    this.resultPropertyPrefixedName = resultPropertyPrefixedName;
  }

  /**
   * @return the contentDisposition
   */
  public String getContentDisposition() {
    return contentDisposition;
  }

  /**
   * @param contentDisposition the contentDisposition to set
   */
  public void setContentDisposition(String contentDisposition) {
    this.contentDisposition = contentDisposition;
  }

  /**
   * @return the fromDate
   */
  public OffsetDateTime getFromDate() {
    return fromDate;
  }

  /**
   * @param fromDate the fromDate to set
   */
  public void setFromDate(OffsetDateTime fromDate) {
    this.fromDate = fromDate;
  }

  /**
   * @return the toDate
   */
  public OffsetDateTime getToDate() {
    return toDate;
  }

  /**
   * @param toDate the toDate to set
   */
  public void setToDate(OffsetDateTime toDate) {
    this.toDate = toDate;
  }

  @Override
  public String toString() {
    return "IndexShareDetail [downloadUri=" + downloadUri + ", source=" + source
        + ", contentPropertyPrefixedName=" + contentPropertyPrefixedName
        + ", resultPropertyPrefixedName=" + resultPropertyPrefixedName + ", contentDisposition="
        + contentDisposition + ", fromDate=" + fromDate + ", toDate=" + toDate + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((downloadUri == null) ? 0 : downloadUri.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    IndexShareDetail other = (IndexShareDetail) obj;
    if (downloadUri == null) {
      if (other.downloadUri != null)
        return false;
    } else if (!downloadUri.equals(other.downloadUri))
      return false;
    return true;
  }

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.mail.model;

import java.io.Serializable;

/**
 *
 */
public class CosmoMailAttachment implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -626925741958383776L;

  private String fileName;

  private String url;

  private Boolean asLink;

  private CosmoMailAttachment(Builder builder) {
    this.fileName = builder.fileName;
    this.url = builder.url;
    this.asLink = builder.asLink;
  }

  public CosmoMailAttachment() {}

  public String getFileName() {
    return fileName;
  }

  public String getUrl() {
    return url;
  }

  public Boolean getAsLink() {
    return asLink;
  }

  /**
   * Creates builder to build {@link CosmoMailAttachment}.
   * 
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link CosmoMailAttachment}.
   */
  public static final class Builder {
    private String fileName;
    private String url;
    private Boolean asLink;

    private Builder() {}

    public Builder withFileName(String fileName) {
      this.fileName = fileName;
      return this;
    }

    public Builder withUrl(String url) {
      this.url = url;
      return this;
    }

    public Builder withAsLink(Boolean asLink) {
      this.asLink = asLink;
      return this;
    }

    public CosmoMailAttachment build() {
      return new CosmoMailAttachment(this);
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((asLink == null) ? 0 : asLink.hashCode());
    result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
    result = prime * result + ((url == null) ? 0 : url.hashCode());
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
    CosmoMailAttachment other = (CosmoMailAttachment) obj;
    if (asLink == null) {
      if (other.asLink != null)
        return false;
    } else if (!asLink.equals(other.asLink))
      return false;
    if (fileName == null) {
      if (other.fileName != null)
        return false;
    } else if (!fileName.equals(other.fileName))
      return false;
    if (url == null) {
      if (other.url != null)
        return false;
    } else if (!url.equals(other.url))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoMailAttachment [fileName=" + fileName + ", url=" + url + ", asLink=" + asLink
        + "]";
  }

}

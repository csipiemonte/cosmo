/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.fileshare.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 *
 */

public class UploadSessionMetadata implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 9044464019122458189L;

  private String sessionUUID;

  private String fileName;

  private String mimeType;

  private Long size;

  private ZonedDateTime uploadedAt;

  private UploadSessionMetadata(Builder builder) {
    this.sessionUUID = builder.sessionUUID;
    this.fileName = builder.fileName;
    this.mimeType = builder.mimeType;
    this.size = builder.size;
    this.uploadedAt = builder.uploadedAt;
  }

  public UploadSessionMetadata() {}

  public ZonedDateTime getUploadedAt() {
    return uploadedAt;
  }

  public void setUploadedAt(ZonedDateTime uploadedAt) {
    this.uploadedAt = uploadedAt;
  }

  public String getSessionUUID() {
    return sessionUUID;
  }

  public void setSessionUUID(String sessionUUID) {
    this.sessionUUID = sessionUUID;
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

  public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size = size;
  }

  /**
   * Creates builder to build {@link UploadSessionMetadata}.
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link UploadSessionMetadata}.
   */
  public static final class Builder {
    private String sessionUUID;
    private String fileName;
    private String mimeType;
    private Long size;
    private ZonedDateTime uploadedAt;

    private Builder() {}

    public Builder withSessionUUID(String sessionUUID) {
      this.sessionUUID = sessionUUID;
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

    public Builder withSize(Long size) {
      this.size = size;
      return this;
    }

    public Builder withUploadedAt(ZonedDateTime uploadedAt) {
      this.uploadedAt = uploadedAt;
      return this;
    }

    public UploadSessionMetadata build() {
      return new UploadSessionMetadata(this);
    }
  }


}

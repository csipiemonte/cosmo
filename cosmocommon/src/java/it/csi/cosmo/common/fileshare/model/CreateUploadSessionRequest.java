/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.fileshare.model;

/**
 *
 */

public class CreateUploadSessionRequest {
  private String fileName;
  private String mimeType;
  private Long size;

  private CreateUploadSessionRequest(Builder builder) {
    this.fileName = builder.fileName;
    this.mimeType = builder.mimeType;
    this.size = builder.size;
  }

  public CreateUploadSessionRequest() {}

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
   * Creates builder to build {@link CreateUploadSessionRequest}.
   * 
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link CreateUploadSessionRequest}.
   */
  public static final class Builder {
    private String fileName;
    private String mimeType;
    private Long size;

    private Builder() {}

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

    public CreateUploadSessionRequest build() {
      return new CreateUploadSessionRequest(this);
    }
  }

}


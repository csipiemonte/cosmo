/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.fileshare.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class UploadSessionPartMetadata implements Serializable {

  private static final long serialVersionUID = 9044464019122458189L;

  private String fileUUID;

  private Long partNumber;

  private ZonedDateTime uploadedAt;

  private UploadSessionPartMetadata(Builder builder) {
    this.fileUUID = builder.fileUUID;
    this.partNumber = builder.partNumber;
    this.uploadedAt = builder.uploadedAt;
  }

  public UploadSessionPartMetadata() {}

  public ZonedDateTime getUploadedAt() {
    return uploadedAt;
  }

  public void setUploadedAt(ZonedDateTime uploadedAt) {
    this.uploadedAt = uploadedAt;
  }

  public String getFileUUID() {
    return fileUUID;
  }

  public void setFileUUID(String fileUUID) {
    this.fileUUID = fileUUID;
  }

  public Long getPartNumber() {
    return partNumber;
  }

  public void setPartNumber(Long partNumber) {
    this.partNumber = partNumber;
  }

  /**
   * Creates builder to build {@link UploadSessionPartMetadata}.
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link UploadSessionPartMetadata}.
   */
  public static final class Builder {
    private String fileUUID;
    private Long partNumber;
    private ZonedDateTime uploadedAt;

    private Builder() {}

    public Builder withFileUUID(String fileUUID) {
      this.fileUUID = fileUUID;
      return this;
    }

    public Builder withPartNumber(Long partNumber) {
      this.partNumber = partNumber;
      return this;
    }

    public Builder withUploadedAt(ZonedDateTime uploadedAt) {
      this.uploadedAt = uploadedAt;
      return this;
    }

    public UploadSessionPartMetadata build() {
      return new UploadSessionPartMetadata(this);
    }
  }

}

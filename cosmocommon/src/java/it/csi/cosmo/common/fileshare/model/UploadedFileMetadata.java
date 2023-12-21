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

public class UploadedFileMetadata implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 9044464019122458189L;

  private String filename;

  private String fileUUID;

  private String contentUUID;

  private String contentHash;

  private String contentType;

  private String uploadHash;

  private String uploadedBy;

  private ZonedDateTime uploadedAt;

  private Long contentSize;

  public UploadedFileMetadata() {
    // NOP
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public ZonedDateTime getUploadedAt() {
    return uploadedAt;
  }

  public void setUploadedAt(ZonedDateTime uploadedAt) {
    this.uploadedAt = uploadedAt;
  }

  public Long getContentSize() {
    return contentSize;
  }

  public void setContentSize(Long contentSize) {
    this.contentSize = contentSize;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getFileUUID() {
    return fileUUID;
  }

  public void setFileUUID(String fileUUID) {
    this.fileUUID = fileUUID;
  }

  public String getContentUUID() {
    return contentUUID;
  }

  public void setContentUUID(String contentUUID) {
    this.contentUUID = contentUUID;
  }

  public String getContentHash() {
    return contentHash;
  }

  public void setContentHash(String contentHash) {
    this.contentHash = contentHash;
  }

  public String getUploadHash() {
    return uploadHash;
  }

  public void setUploadHash(String uploadHash) {
    this.uploadHash = uploadHash;
  }

  public String getUploadedBy() {
    return uploadedBy;
  }

  public void setUploadedBy(String uploadedBy) {
    this.uploadedBy = uploadedBy;
  }

  @Override
  public String toString() {
    return "UploadedFileMetadata [filename=" + filename + ", fileUUID=" + fileUUID
        + ", contentUUID=" + contentUUID + ", contentHash=" + contentHash + ", uploadHash="
        + uploadHash + ", uploadedBy=" + uploadedBy + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fileUUID == null) ? 0 : fileUUID.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    UploadedFileMetadata other = (UploadedFileMetadata) obj;
    if (fileUUID == null) {
      if (other.fileUUID != null) {
        return false;
      }
    } else if (!fileUUID.equals(other.fileUUID)) {
      return false;
    }
    return true;
  }

}

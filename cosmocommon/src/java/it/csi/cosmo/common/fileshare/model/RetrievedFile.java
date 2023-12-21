/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.fileshare.model;

import java.io.InputStream;
import java.nio.file.Path;
import java.time.ZonedDateTime;

/**
 *
 */

public class RetrievedFile implements RetrievedContent {

  private Path workingFolder;

  private InputStream contentStream;

  private UploadedFileMetadata metadata;

  private RetrievedFile(Builder builder) {
    this.workingFolder = builder.workingFolder;
    this.contentStream = builder.contentStream;
    this.metadata = builder.metadata;
  }

  public RetrievedFile() {}

  @Override
  public String getUploadUUID() {
    if (metadata == null) {
      return null;
    }
    return metadata.getFileUUID();
  }

  @Override
  public ZonedDateTime getUploadedAt() {
    if (metadata == null) {
      return null;
    }
    return metadata.getUploadedAt();
  }

  @Override
  public String getFilename() {
    if (metadata == null) {
      return null;
    }
    return metadata.getFilename();
  }

  @Override
  public String getContentType() {
    if (metadata == null) {
      return null;
    }
    return metadata.getContentType();
  }

  @Override
  public Long getContentSize() {
    if (metadata == null) {
      return null;
    }
    return metadata.getContentSize();
  }

  @Override
  public Path getWorkingFolder() {
    return workingFolder;
  }

  @Override
  public InputStream getContentStream() {
    return contentStream;
  }

  public UploadedFileMetadata getMetadata() {
    return metadata;
  }

  @Override
  public String toString() {
    return "RetrievedFile [metadata=" + metadata + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((metadata == null) ? 0 : metadata.hashCode());
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
    RetrievedFile other = (RetrievedFile) obj;
    if (metadata == null) {
      if (other.metadata != null) {
        return false;
      }
    } else if (!metadata.equals(other.metadata)) {
      return false;
    }
    return true;
  }

  /**
   * Creates builder to build {@link RetrievedFile}.
   * 
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link RetrievedFile}.
   */
  public static final class Builder {
    private Path workingFolder;
    private InputStream contentStream;
    private UploadedFileMetadata metadata;

    private Builder() {}

    public Builder withWorkingFolder(Path workingFolder) {
      this.workingFolder = workingFolder;
      return this;
    }

    public Builder withContentStream(InputStream contentStream) {
      this.contentStream = contentStream;
      return this;
    }

    public Builder withMetadata(UploadedFileMetadata metadata) {
      this.metadata = metadata;
      return this;
    }

    public RetrievedFile build() {
      return new RetrievedFile(this);
    }
  }

}

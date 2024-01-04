/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.mtom;

import java.io.InputStream;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 */

public class Index2ContentAttachment {

  private final String fileName;

  private final String contentType;

  private final Long fileSize;

  @JsonIgnore
  private final InputStream contentStream;

  private Index2ContentAttachment(Builder builder) {
    this.fileName = builder.fileName;
    this.contentType = builder.contentType;
    this.fileSize = builder.fileSize;
    this.contentStream = builder.contentStream;
  }

  public Index2ContentAttachment() {
    fileName = null;
    contentType = null;
    fileSize = null;
    contentStream = null;
  }

  public String getFileName() {
    return fileName;
  }

  public String getContentType() {
    return contentType;
  }

  public Long getFileSize() {
    return fileSize;
  }

  @JsonIgnore
  public InputStream getContentStream() {
    return contentStream;
  }

  /**
   * Creates builder to build {@link Index2ContentAttachment}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link Index2ContentAttachment}.
   */
  public static final class Builder {
    private String fileName;
    private String contentType;
    private Long fileSize;
    private InputStream contentStream;

    private Builder() {}

    public Builder withFileName(String fileName) {
      this.fileName = fileName;
      return this;
    }

    public Builder withContentType(String contentType) {
      this.contentType = contentType;
      return this;
    }

    public Builder withFileSize(Long fileSize) {
      this.fileSize = fileSize;
      return this;
    }

    public Builder withContentStream(InputStream contentStream) {
      this.contentStream = contentStream;
      return this;
    }

    public Index2ContentAttachment build() {
      return new Index2ContentAttachment(this);
    }
  }

}

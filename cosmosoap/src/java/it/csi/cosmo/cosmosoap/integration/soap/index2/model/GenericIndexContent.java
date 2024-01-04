/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmosoap.integration.soap.index2.model;

/**
 *
 */
@IndexModel(prefix = "cm", type = "content")
public class GenericIndexContent extends IndexEntity {

  public GenericIndexContent() {
    // NOP
  }

  private GenericIndexContent(Builder builder) {
    filename = builder.filename;
    mimeType = builder.mimeType;
    encoding = builder.encoding;
    content = builder.content;
  }

  /**
   * Creates builder to build {@link GenericIndexContent}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link GenericIndexContent}.
   */
  public static final class Builder {

    private String filename;

    private String mimeType;

    private String encoding;

    private byte[] content;

    private Builder() {}

    public Builder withFilename(String filename) {
      this.filename = filename;
      return this;
    }

    public Builder withMimeType(String mimeType) {
      this.mimeType = mimeType;
      return this;
    }

    public Builder withEncoding(String encoding) {
      this.encoding = encoding;
      return this;
    }

    public Builder withContent(byte[] content) {
      this.content = content;
      return this;
    }

    public GenericIndexContent build() {
      return new GenericIndexContent(this);
    }
  }

  @Override
  public String toString() {
    return "GenericIndexContent [filename=" + filename + "]";
  }

}

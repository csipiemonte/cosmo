/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.model;

/**
 *
 */

public class IndexFileFormatInfo {

  private String description;

  private String formatVersion;

  private String[] mimeType;

  private String puid;

  private String typeExtension;

  private boolean signed;

  private String signatureType;

  private IndexFileFormatInfo(Builder builder) {
    this.description = builder.description;
    this.formatVersion = builder.formatVersion;
    this.mimeType = builder.mimeType;
    this.puid = builder.puid;
    this.typeExtension = builder.typeExtension;
    this.signed = builder.signed;
    this.signatureType = builder.signatureType;
  }

  public IndexFileFormatInfo() {
    // NOP
  }

  public boolean isSigned() {
    return signed;
  }

  public String getSignatureType() {
    return signatureType;
  }

  public String getDescription() {
    return description;
  }

  public String getFormatVersion() {
    return formatVersion;
  }

  public String[] getMimeType() {
    return mimeType;
  }

  public String getPuid() {
    return puid;
  }

  public String getTypeExtension() {
    return typeExtension;
  }

  /**
   * Creates builder to build {@link IndexFileFormatInfo}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link IndexFileFormatInfo}.
   */
  public static final class Builder {
    private String description;
    private String formatVersion;
    private String[] mimeType;
    private String puid;
    private String typeExtension;
    private boolean signed;
    private String signatureType;

    private Builder() {}

    public Builder withDescription(String description) {
      this.description = description;
      return this;
    }

    public Builder withFormatVersion(String formatVersion) {
      this.formatVersion = formatVersion;
      return this;
    }

    public Builder withMimeType(String[] mimeType) {
      this.mimeType = mimeType;
      return this;
    }

    public Builder withPuid(String puid) {
      this.puid = puid;
      return this;
    }

    public Builder withTypeExtension(String typeExtension) {
      this.typeExtension = typeExtension;
      return this;
    }

    public Builder withSigned(boolean signed) {
      this.signed = signed;
      return this;
    }

    public Builder withSignatureType(String signatureType) {
      this.signatureType = signatureType;
      return this;
    }

    public IndexFileFormatInfo build() {
      return new IndexFileFormatInfo(this);
    }
  }

}

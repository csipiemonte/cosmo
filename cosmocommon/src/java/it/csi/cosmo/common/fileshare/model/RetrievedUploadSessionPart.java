/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.fileshare.model;

/**
 *
 */

public class RetrievedUploadSessionPart {

  private UploadSessionPartMetadata part;

  private RetrievedFile partFile;

  private RetrievedUploadSessionPart(Builder builder) {
    this.part = builder.part;
    this.partFile = builder.partFile;
  }

  public RetrievedUploadSessionPart() {}

  public UploadSessionPartMetadata getPart() {
    return part;
  }

  public void setPart(UploadSessionPartMetadata part) {
    this.part = part;
  }

  public RetrievedFile getPartFile() {
    return partFile;
  }

  public void setPartFile(RetrievedFile partFile) {
    this.partFile = partFile;
  }

  /**
   * Creates builder to build {@link RetrievedUploadSessionPart}.
   * 
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link RetrievedUploadSessionPart}.
   */
  public static final class Builder {
    private UploadSessionPartMetadata part;
    private RetrievedFile partFile;

    private Builder() {}

    public Builder withPart(UploadSessionPartMetadata part) {
      this.part = part;
      return this;
    }

    public Builder withPartFile(RetrievedFile partFile) {
      this.partFile = partFile;
      return this;
    }

    public RetrievedUploadSessionPart build() {
      return new RetrievedUploadSessionPart(this);
    }
  }


}

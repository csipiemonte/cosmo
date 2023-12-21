/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.fileshare.model;

import java.net.URI;

/**
 *
 */

public class CompleteUploadSessionResponse {

  private URI location;

  private UploadSessionMetadata metadata;

  private CompleteUploadSessionResponse(Builder builder) {
    this.location = builder.location;
    this.metadata = builder.metadata;
  }

  public CompleteUploadSessionResponse() {}

  public URI getLocation() {
    return location;
  }

  public void setLocation(URI location) {
    this.location = location;
  }

  public UploadSessionMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(UploadSessionMetadata metadata) {
    this.metadata = metadata;
  }

  /**
   * Creates builder to build {@link CompleteUploadSessionResponse}.
   * 
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link CompleteUploadSessionResponse}.
   */
  public static final class Builder {
    private URI location;
    private UploadSessionMetadata metadata;

    private Builder() {}

    public Builder withLocation(URI location) {
      this.location = location;
      return this;
    }

    public Builder withMetadata(UploadSessionMetadata metadata) {
      this.metadata = metadata;
      return this;
    }

    public CompleteUploadSessionResponse build() {
      return new CompleteUploadSessionResponse(this);
    }
  }

}

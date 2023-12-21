/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.fileshare.model;

/**
 *
 */

public class CompleteUploadSessionRequest {

  private String sessionUUID;
  private String hashes;

  private CompleteUploadSessionRequest(Builder builder) {
    this.sessionUUID = builder.sessionUUID;
    this.hashes = builder.hashes;
  }

  public CompleteUploadSessionRequest() {}

  public String getSessionUUID() {
    return sessionUUID;
  }

  public void setSessionUUID(String sessionUUID) {
    this.sessionUUID = sessionUUID;
  }

  public String getHashes() {
    return hashes;
  }

  public void setHashes(String hashes) {
    this.hashes = hashes;
  }

  /**
   * Creates builder to build {@link CompleteUploadSessionRequest}.
   * 
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link CompleteUploadSessionRequest}.
   */
  public static final class Builder {
    private String sessionUUID;
    private String hashes;

    private Builder() {}

    public Builder withSessionUUID(String sessionUUID) {
      this.sessionUUID = sessionUUID;
      return this;
    }

    public Builder withHashes(String hashes) {
      this.hashes = hashes;
      return this;
    }

    public CompleteUploadSessionRequest build() {
      return new CompleteUploadSessionRequest(this);
    }
  }

}


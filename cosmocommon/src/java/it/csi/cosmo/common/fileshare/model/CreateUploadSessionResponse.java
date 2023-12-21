/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.fileshare.model;

/**
 *
 */

public class CreateUploadSessionResponse {
  private String sessionUUID;

  private CreateUploadSessionResponse(Builder builder) {
    this.sessionUUID = builder.sessionUUID;
  }

  public CreateUploadSessionResponse() {}

  public String getSessionUUID() {
    return sessionUUID;
  }

  public void setSessionUUID(String sessionUUID) {
    this.sessionUUID = sessionUUID;
  }

  /**
   * Creates builder to build {@link CreateUploadSessionResponse}.
   * 
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link CreateUploadSessionResponse}.
   */
  public static final class Builder {
    private String sessionUUID;

    private Builder() {}

    public Builder withSessionUUID(String sessionUUID) {
      this.sessionUUID = sessionUUID;
      return this;
    }

    public CreateUploadSessionResponse build() {
      return new CreateUploadSessionResponse(this);
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((sessionUUID == null) ? 0 : sessionUUID.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CreateUploadSessionResponse other = (CreateUploadSessionResponse) obj;
    if (sessionUUID == null) {
      if (other.sessionUUID != null)
        return false;
    } else if (!sessionUUID.equals(other.sessionUUID))
      return false;
    return true;
  }



}


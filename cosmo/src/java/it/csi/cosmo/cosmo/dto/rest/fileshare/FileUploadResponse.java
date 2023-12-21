/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.dto.rest.fileshare;

import java.net.URI;
import it.csi.cosmo.common.fileshare.model.UploadedFileMetadata;

/**
 *
 */

public class FileUploadResponse {

  private URI location;

  private UploadedFileMetadata metadata;

  public FileUploadResponse() {
    // NOP
  }

  public URI getLocation() {
    return location;
  }

  public void setLocation(URI location) {
    this.location = location;
  }

  public UploadedFileMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(UploadedFileMetadata metadata) {
    this.metadata = metadata;
  }

  @Override
  public String toString() {
    return "FileUploadResult [location=" + location + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((location == null) ? 0 : location.hashCode());
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
    FileUploadResponse other = (FileUploadResponse) obj;
    if (location == null) {
      if (other.location != null) {
        return false;
      }
    } else if (!location.equals(other.location)) {
      return false;
    }
    return true;
  }

}

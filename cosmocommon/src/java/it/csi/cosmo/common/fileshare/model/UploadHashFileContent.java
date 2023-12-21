/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.fileshare.model;

import java.io.Serializable;

/**
 *
 */

public class UploadHashFileContent implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -2749037274492804549L;

  private String contentUUID;

  public UploadHashFileContent() {
    // NOP
  }

  public String getContentUUID() {
    return contentUUID;
  }

  public void setContentUUID(String contentUUID) {
    this.contentUUID = contentUUID;
  }

  @Override
  public String toString() {
    return "UploadHashFileContent [contentUUID=" + contentUUID + "]";
  }

}

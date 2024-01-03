/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto;

import java.io.Serializable;
import java.net.URI;

public class ContenutoDocumentoResult extends FileContent implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case]
  private static final long serialVersionUID = 1L;

  private URI linkDownloadDiretto = null;

  public URI getLinkDownloadDiretto() {
    return linkDownloadDiretto;
  }

  public void setLinkDownloadDiretto(URI linkDownloadDiretto) {
    this.linkDownloadDiretto = linkDownloadDiretto;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((linkDownloadDiretto == null) ? 0 : linkDownloadDiretto.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ContenutoDocumentoResult other = (ContenutoDocumentoResult) obj;

    if (linkDownloadDiretto == null) {
      if (other.linkDownloadDiretto != null) {
        return false;
      }
    } else if (!linkDownloadDiretto.equals(other.linkDownloadDiretto)) {
      return false;
    }
    return true;
  }

}


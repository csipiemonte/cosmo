/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmosoap.integration.soap.index2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.csi.cosmo.cosmosoap.integration.soap.index2.Index2WrapperFacadeImpl.IndexFolderBindingProxy;

/**
 *
 */
public class IndexFolder extends IndexObject {

  @IndexProperty ( value = "cm:name", required = false, readOnly = false )
  protected String foldername;

  private IndexFolderBindingProxy indexBindingProxy;

  @JsonIgnore
  public String getEffectivePath() {
    if (indexBindingProxy != null) {
      return indexBindingProxy.resolvePath(this);
    } else {
      return null;
    }
  }

  public String getFoldername () {
    return foldername;
  }

  public void setFoldername ( String foldername ) {
    this.foldername = foldername;
  }

  @Override
  public String toString() {
    return "IndexFolder [foldername=" + foldername + ", uid=" + uid + ", remoteName=" + remoteName
        + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((uid == null) ? 0 : uid.hashCode());
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
    IndexFolder other = (IndexFolder) obj;
    if (uid == null) {
      if (other.uid != null) {
        return false;
      }
    } else if (!uid.equals(other.uid)) {
      return false;
    }
    return true;
  }
}

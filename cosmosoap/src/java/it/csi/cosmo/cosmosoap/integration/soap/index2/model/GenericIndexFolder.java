/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmosoap.integration.soap.index2.model;

/**
 *
 */
@IndexModel(prefix = "cm", type = "folder")
public class GenericIndexFolder extends IndexFolder {

  public GenericIndexFolder() {
    // NOP
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((getUid() == null) ? 0 : getUid().hashCode());
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
    GenericIndexFolder other = (GenericIndexFolder) obj;
    if (getUid() == null) {
      if (other.getUid() != null) {
        return false;
      }
    } else if (!getUid().equals(other.getUid())) {
      return false;
    }
    return true;
  }


}

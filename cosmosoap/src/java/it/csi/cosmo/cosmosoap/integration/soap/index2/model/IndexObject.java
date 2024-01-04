/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.model;

import it.csi.cosmo.cosmosoap.integration.soap.index2.aspect.AuditableAspect;
import it.csi.cosmo.cosmosoap.integration.soap.index2.aspect.IndexAspect;
import it.csi.cosmo.cosmosoap.integration.soap.index2.aspect.SharedAspect;

/**
 *
 */

public class IndexObject {

  protected String uid;

  // @IndexProperty(value = "cm:name", required = false, readOnly = true)
  protected String remoteName;

  @IndexProperty(value = "cm:icon", required = false, readOnly = true)
  protected String icon;

  @IndexProperty(value = "sys:node-dbid", required = false, readOnly = true)
  protected String nodeDatabaseId;

  @IndexProperty(value = "sys:node-uuid", required = false, readOnly = true)
  protected String nodeUUID;

  @IndexProperty(value = "sys:store-protocol", required = false, readOnly = true)
  protected String storeProtocol;

  @IndexProperty(value = "sys:store-identifier", required = false, readOnly = true)
  protected String storeIdentifier;

  @IndexAspect(value = "cm:auditable")
  private AuditableAspect auditable;

  @IndexAspect(value = "ecm-sys:shared")
  private SharedAspect shared;

  public IndexObject() {
    // NOP
  }

  public String getUid() {
    return uid;
  }

  public String getNodeDatabaseId() {
    return nodeDatabaseId;
  }

  public String getNodeUUID() {
    return nodeUUID;
  }

  public String getStoreProtocol() {
    return storeProtocol;
  }

  public String getStoreIdentifier() {
    return storeIdentifier;
  }

  public String getRemoteName() {
    return remoteName;
  }

  public String getIcon() {
    return icon;
  }

  public AuditableAspect getAuditable() {
    return auditable;
  }

  public SharedAspect getShared() {
    return shared;
  }

  @Override
  public String toString() {
    return "IndexObject [uid=" + uid + ", remoteName=" + remoteName + "]";
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
    IndexObject other = (IndexObject) obj;
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

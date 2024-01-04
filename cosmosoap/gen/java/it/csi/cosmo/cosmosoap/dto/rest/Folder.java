/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.Audit;
import it.csi.cosmo.cosmosoap.dto.rest.Shared;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Folder  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String uid = null;
  private String remoteName = null;
  private String icon = null;
  private String nodeDatabaseId = null;
  private String nodeUUID = null;
  private String storeProtocol = null;
  private String storeIdentifier = null;
  private Audit auditable = null;
  private Shared shared = null;
  private String foldername = null;
  private String effectivePath = null;

  /**
   **/
  


  // nome originario nello yaml: uid 
  public String getUid() {
    return uid;
  }
  public void setUid(String uid) {
    this.uid = uid;
  }

  /**
   **/
  


  // nome originario nello yaml: remoteName 
  public String getRemoteName() {
    return remoteName;
  }
  public void setRemoteName(String remoteName) {
    this.remoteName = remoteName;
  }

  /**
   **/
  


  // nome originario nello yaml: icon 
  public String getIcon() {
    return icon;
  }
  public void setIcon(String icon) {
    this.icon = icon;
  }

  /**
   **/
  


  // nome originario nello yaml: nodeDatabaseId 
  public String getNodeDatabaseId() {
    return nodeDatabaseId;
  }
  public void setNodeDatabaseId(String nodeDatabaseId) {
    this.nodeDatabaseId = nodeDatabaseId;
  }

  /**
   **/
  


  // nome originario nello yaml: nodeUUID 
  public String getNodeUUID() {
    return nodeUUID;
  }
  public void setNodeUUID(String nodeUUID) {
    this.nodeUUID = nodeUUID;
  }

  /**
   **/
  


  // nome originario nello yaml: storeProtocol 
  public String getStoreProtocol() {
    return storeProtocol;
  }
  public void setStoreProtocol(String storeProtocol) {
    this.storeProtocol = storeProtocol;
  }

  /**
   **/
  


  // nome originario nello yaml: storeIdentifier 
  public String getStoreIdentifier() {
    return storeIdentifier;
  }
  public void setStoreIdentifier(String storeIdentifier) {
    this.storeIdentifier = storeIdentifier;
  }

  /**
   **/
  


  // nome originario nello yaml: auditable 
  public Audit getAuditable() {
    return auditable;
  }
  public void setAuditable(Audit auditable) {
    this.auditable = auditable;
  }

  /**
   **/
  


  // nome originario nello yaml: shared 
  public Shared getShared() {
    return shared;
  }
  public void setShared(Shared shared) {
    this.shared = shared;
  }

  /**
   **/
  


  // nome originario nello yaml: foldername 
  public String getFoldername() {
    return foldername;
  }
  public void setFoldername(String foldername) {
    this.foldername = foldername;
  }

  /**
   **/
  


  // nome originario nello yaml: effectivePath 
  public String getEffectivePath() {
    return effectivePath;
  }
  public void setEffectivePath(String effectivePath) {
    this.effectivePath = effectivePath;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Folder folder = (Folder) o;
    return Objects.equals(uid, folder.uid) &&
        Objects.equals(remoteName, folder.remoteName) &&
        Objects.equals(icon, folder.icon) &&
        Objects.equals(nodeDatabaseId, folder.nodeDatabaseId) &&
        Objects.equals(nodeUUID, folder.nodeUUID) &&
        Objects.equals(storeProtocol, folder.storeProtocol) &&
        Objects.equals(storeIdentifier, folder.storeIdentifier) &&
        Objects.equals(auditable, folder.auditable) &&
        Objects.equals(shared, folder.shared) &&
        Objects.equals(foldername, folder.foldername) &&
        Objects.equals(effectivePath, folder.effectivePath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uid, remoteName, icon, nodeDatabaseId, nodeUUID, storeProtocol, storeIdentifier, auditable, shared, foldername, effectivePath);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Folder {\n");
    
    sb.append("    uid: ").append(toIndentedString(uid)).append("\n");
    sb.append("    remoteName: ").append(toIndentedString(remoteName)).append("\n");
    sb.append("    icon: ").append(toIndentedString(icon)).append("\n");
    sb.append("    nodeDatabaseId: ").append(toIndentedString(nodeDatabaseId)).append("\n");
    sb.append("    nodeUUID: ").append(toIndentedString(nodeUUID)).append("\n");
    sb.append("    storeProtocol: ").append(toIndentedString(storeProtocol)).append("\n");
    sb.append("    storeIdentifier: ").append(toIndentedString(storeIdentifier)).append("\n");
    sb.append("    auditable: ").append(toIndentedString(auditable)).append("\n");
    sb.append("    shared: ").append(toIndentedString(shared)).append("\n");
    sb.append("    foldername: ").append(toIndentedString(foldername)).append("\n");
    sb.append("    effectivePath: ").append(toIndentedString(effectivePath)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.Audit;
import it.csi.cosmo.cosmosoap.dto.rest.Lock;
import it.csi.cosmo.cosmosoap.dto.rest.Shared;
import it.csi.cosmo.cosmosoap.dto.rest.Version;
import it.csi.cosmo.cosmosoap.dto.rest.WorkingCopy;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Entity  implements Serializable {
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
  private String filename = null;
  private String mimeType = "UTF-8";
  private String encoding = null;
  private byte[] content = null;
  private OffsetDateTime dataModificaEcmSys = null;
  private String contentUrl = null;
  private Boolean contentChanged = false;
  private String descrizione = null;
  private String tipoDocumento = null;
  private Long idDocumento = null;
  private Long idDocumentoPadre = null;
  private Lock lockable = null;
  private Version versionable = null;
  private WorkingCopy workingCopy = null;

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
  


  // nome originario nello yaml: filename 
  public String getFilename() {
    return filename;
  }
  public void setFilename(String filename) {
    this.filename = filename;
  }

  /**
   **/
  


  // nome originario nello yaml: mimeType 
  public String getMimeType() {
    return mimeType;
  }
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  /**
   **/
  


  // nome originario nello yaml: encoding 
  public String getEncoding() {
    return encoding;
  }
  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  /**
   **/
  


  // nome originario nello yaml: content 
  @Pattern(regexp="^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$")
  public byte[] getContent() {
    return content;
  }
  public void setContent(byte[] content) {
    this.content = content;
  }

  /**
   **/
  


  // nome originario nello yaml: dataModificaEcmSys 
  public OffsetDateTime getDataModificaEcmSys() {
    return dataModificaEcmSys;
  }
  public void setDataModificaEcmSys(OffsetDateTime dataModificaEcmSys) {
    this.dataModificaEcmSys = dataModificaEcmSys;
  }

  /**
   **/
  


  // nome originario nello yaml: contentUrl 
  public String getContentUrl() {
    return contentUrl;
  }
  public void setContentUrl(String contentUrl) {
    this.contentUrl = contentUrl;
  }

  /**
   **/
  


  // nome originario nello yaml: contentChanged 
  public Boolean isContentChanged() {
    return contentChanged;
  }
  public void setContentChanged(Boolean contentChanged) {
    this.contentChanged = contentChanged;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizione 
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoDocumento 
  public String getTipoDocumento() {
    return tipoDocumento;
  }
  public void setTipoDocumento(String tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
  }

  /**
   **/
  


  // nome originario nello yaml: idDocumento 
  public Long getIdDocumento() {
    return idDocumento;
  }
  public void setIdDocumento(Long idDocumento) {
    this.idDocumento = idDocumento;
  }

  /**
   **/
  


  // nome originario nello yaml: idDocumentoPadre 
  public Long getIdDocumentoPadre() {
    return idDocumentoPadre;
  }
  public void setIdDocumentoPadre(Long idDocumentoPadre) {
    this.idDocumentoPadre = idDocumentoPadre;
  }

  /**
   **/
  


  // nome originario nello yaml: lockable 
  public Lock getLockable() {
    return lockable;
  }
  public void setLockable(Lock lockable) {
    this.lockable = lockable;
  }

  /**
   **/
  


  // nome originario nello yaml: versionable 
  public Version getVersionable() {
    return versionable;
  }
  public void setVersionable(Version versionable) {
    this.versionable = versionable;
  }

  /**
   **/
  


  // nome originario nello yaml: workingCopy 
  public WorkingCopy getWorkingCopy() {
    return workingCopy;
  }
  public void setWorkingCopy(WorkingCopy workingCopy) {
    this.workingCopy = workingCopy;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Entity entity = (Entity) o;
    return Objects.equals(uid, entity.uid) &&
        Objects.equals(remoteName, entity.remoteName) &&
        Objects.equals(icon, entity.icon) &&
        Objects.equals(nodeDatabaseId, entity.nodeDatabaseId) &&
        Objects.equals(nodeUUID, entity.nodeUUID) &&
        Objects.equals(storeProtocol, entity.storeProtocol) &&
        Objects.equals(storeIdentifier, entity.storeIdentifier) &&
        Objects.equals(auditable, entity.auditable) &&
        Objects.equals(shared, entity.shared) &&
        Objects.equals(filename, entity.filename) &&
        Objects.equals(mimeType, entity.mimeType) &&
        Objects.equals(encoding, entity.encoding) &&
        Objects.equals(content, entity.content) &&
        Objects.equals(dataModificaEcmSys, entity.dataModificaEcmSys) &&
        Objects.equals(contentUrl, entity.contentUrl) &&
        Objects.equals(contentChanged, entity.contentChanged) &&
        Objects.equals(descrizione, entity.descrizione) &&
        Objects.equals(tipoDocumento, entity.tipoDocumento) &&
        Objects.equals(idDocumento, entity.idDocumento) &&
        Objects.equals(idDocumentoPadre, entity.idDocumentoPadre) &&
        Objects.equals(lockable, entity.lockable) &&
        Objects.equals(versionable, entity.versionable) &&
        Objects.equals(workingCopy, entity.workingCopy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uid, remoteName, icon, nodeDatabaseId, nodeUUID, storeProtocol, storeIdentifier, auditable, shared, filename, mimeType, encoding, content, dataModificaEcmSys, contentUrl, contentChanged, descrizione, tipoDocumento, idDocumento, idDocumentoPadre, lockable, versionable, workingCopy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Entity {\n");
    
    sb.append("    uid: ").append(toIndentedString(uid)).append("\n");
    sb.append("    remoteName: ").append(toIndentedString(remoteName)).append("\n");
    sb.append("    icon: ").append(toIndentedString(icon)).append("\n");
    sb.append("    nodeDatabaseId: ").append(toIndentedString(nodeDatabaseId)).append("\n");
    sb.append("    nodeUUID: ").append(toIndentedString(nodeUUID)).append("\n");
    sb.append("    storeProtocol: ").append(toIndentedString(storeProtocol)).append("\n");
    sb.append("    storeIdentifier: ").append(toIndentedString(storeIdentifier)).append("\n");
    sb.append("    auditable: ").append(toIndentedString(auditable)).append("\n");
    sb.append("    shared: ").append(toIndentedString(shared)).append("\n");
    sb.append("    filename: ").append(toIndentedString(filename)).append("\n");
    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
    sb.append("    encoding: ").append(toIndentedString(encoding)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    dataModificaEcmSys: ").append(toIndentedString(dataModificaEcmSys)).append("\n");
    sb.append("    contentUrl: ").append(toIndentedString(contentUrl)).append("\n");
    sb.append("    contentChanged: ").append(toIndentedString(contentChanged)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    tipoDocumento: ").append(toIndentedString(tipoDocumento)).append("\n");
    sb.append("    idDocumento: ").append(toIndentedString(idDocumento)).append("\n");
    sb.append("    idDocumentoPadre: ").append(toIndentedString(idDocumentoPadre)).append("\n");
    sb.append("    lockable: ").append(toIndentedString(lockable)).append("\n");
    sb.append("    versionable: ").append(toIndentedString(versionable)).append("\n");
    sb.append("    workingCopy: ").append(toIndentedString(workingCopy)).append("\n");
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


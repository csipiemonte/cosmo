/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmosoap.integration.soap.index2.model;

import java.time.ZonedDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import it.csi.cosmo.cosmosoap.integration.soap.index2.Index2WrapperFacadeImpl.IndexBindingProxy;
import it.csi.cosmo.cosmosoap.integration.soap.index2.exceptions.Index2Exception;
import it.csi.cosmo.cosmosoap.integration.soap.index2.mtom.Index2ContentAttachment;


/**
 *
 */
public class IndexEntity extends IndexObject {

  @IndexProperty(value = "cm:name", required = false, readOnly = false)
  protected String filename;

  protected String mimeType = "UTF-8";

  protected String encoding;

  @JsonIgnore
  protected byte[] content;

  @IndexProperty(value = "ecm-sys:dataModifica", required = false, readOnly = true)
  protected ZonedDateTime dataModificaEcmSys;

  @IndexProperty(value = "cm:content", required = false, readOnly = true)
  protected String contentUrl;

  private IndexBindingProxy indexBindingProxy;

  private Boolean contentChanged = false;

  public ZonedDateTime getDataModificaEcmSys() {
    return dataModificaEcmSys;
  }

  public String getContentUrl() {
    return contentUrl;
  }

  public String getEncoding() {
    return encoding;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  @JsonIgnore
  public String getEffectivePath() {
    if (indexBindingProxy != null) {
      return indexBindingProxy.resolvePath(this);
    } else {
      return null;
    }
  }

  @JsonIgnore
  public Index2ContentAttachment getContentData() {
    return indexBindingProxy.resolveContentAttachment(this);
  }

  @JsonIgnore
  public byte[] getContent() {
    if (content == null && indexBindingProxy != null) {
      content = indexBindingProxy.resolveContent(this);
      contentChanged = false;
    }
    return content;
  }

  public byte[] reloadContent() {
    if (indexBindingProxy != null) {
      content = indexBindingProxy.resolveContent(this);
      contentChanged = false;
    } else {
      throw new Index2Exception("IndexBindingProxy not available");
    }
    return content;
  }

  @JsonIgnore
  public void setContent(byte[] content) {
    this.content = content;
    contentChanged = true;
  }

  @JsonIgnore
  public boolean isContentChanged() {
    return contentChanged != null && contentChanged;
  }

  @Override
  public String toString() {
    return "IndexEntity [filename=" + filename + ", mimeType=" + mimeType + ", uid=" + uid
        + ", remoteName=" + remoteName + "]";
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
    IndexEntity other = (IndexEntity) obj;
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

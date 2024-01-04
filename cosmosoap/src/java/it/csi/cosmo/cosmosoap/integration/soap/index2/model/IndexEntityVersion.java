/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.model;

import java.security.InvalidParameterException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import it.csi.cosmo.cosmosoap.integration.soap.index2.Index2WrapperFacadeImpl.IndexBindingProxyForVersion;

/**
 *
 */

public class IndexEntityVersion<T extends IndexEntity> {

  private String versionLabel;

  private String uid;

  private String creator;

  private ZonedDateTime createdDate;

  private String description;

  private List<IndexRawProperty> properties = new ArrayList<>();

  private T entity = null;

  private IndexBindingProxyForVersion<T> indexBindingProxy;

  public IndexEntityVersion() {
    // NOP
  }

  @JsonIgnore
  public synchronized T getEntity() {
    if (entity == null) {
      if (StringUtils.isEmpty(this.uid)) {
        throw new InvalidParameterException("No UID to retrieve entity from");
      }
      if (indexBindingProxy == null) {
        throw new InvalidParameterException("No indexBindingProxy to retrieve entity from");
      }
      entity = indexBindingProxy.resolveVersionEntity(this.uid);
    }
    return entity;
  }

  public String getVersionLabel() {
    return versionLabel;
  }

  public void setVersionLabel(String versionLabel) {
    this.versionLabel = versionLabel;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public ZonedDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(ZonedDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<IndexRawProperty> getProperties() {
    return properties;
  }

  public void setProperties(List<IndexRawProperty> properties) {
    this.properties = properties;
  }

  @Override
  public String toString() {
    return "IndexEntityVersion [versionLabel=" + versionLabel + ", uid=" + uid + ", creator="
        + creator + ", createdDate=" + createdDate + ", description=" + description
        + ", properties=" + properties + "]";
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
    IndexEntityVersion<?> other = (IndexEntityVersion<?>) obj;
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

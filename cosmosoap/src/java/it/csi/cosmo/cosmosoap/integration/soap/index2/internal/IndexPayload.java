/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.soap.index2.internal;

import java.io.Serializable;
import java.util.List;
import it.doqui.index.ecmengine.client.webservices.dto.engine.management.Aspect;
import it.doqui.index.ecmengine.client.webservices.dto.engine.management.Property;


public class IndexPayload implements Serializable {

  private static final long serialVersionUID = 1L;

  private byte[] content;

  private String typePrefixedName;

  private String prefixedName;

  private String enconding;

  private String mimeType;

  private String contentPropertyPrefixedName;

  private String modelPrefixedName;

  private String parentAssocTypePrefixedName;

  private List<Property> indexProperties;

  private List<Aspect> indexAspects;

  public List<Aspect> getIndexAspects() {
    return indexAspects;
  }

  public void setIndexAspects(List<Aspect> indexAspects) {
    this.indexAspects = indexAspects;
  }

  public byte[] getContent() {
    return content;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

  public String getTypePrefixedName() {
    return typePrefixedName;
  }

  public void setTypePrefixedName(String typePrefixedName) {
    this.typePrefixedName = typePrefixedName;
  }

  public String getPrefixedName() {
    return prefixedName;
  }

  public void setPrefixedName(String prefixedName) {
    this.prefixedName = prefixedName;
  }

  public String getEnconding() {
    return enconding;
  }

  public void setEnconding(String enconding) {
    this.enconding = enconding;
  }

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public List<Property> getIndexProperties() {
    return indexProperties;
  }

  public void setIndexProperties(List<Property> indexProperties) {
    this.indexProperties = indexProperties;
  }

  public String getContentPropertyPrefixedName() {
    return contentPropertyPrefixedName;
  }

  public void setContentPropertyPrefixedName(String contentPropertyPrefixedName) {
    this.contentPropertyPrefixedName = contentPropertyPrefixedName;
  }

  public String getModelPrefixedName() {
    return modelPrefixedName;
  }

  public void setModelPrefixedName(String modelPrefixedName) {
    this.modelPrefixedName = modelPrefixedName;
  }

  public String getParentAssocTypePrefixedName() {
    return parentAssocTypePrefixedName;
  }

  public void setParentAssocTypePrefixedName(String parentAssocTypePrefixedName) {
    this.parentAssocTypePrefixedName = parentAssocTypePrefixedName;
  }
}

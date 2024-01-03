/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmopratiche.dto.rest.SimpleFormField;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class SimpleForm  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private String name = null;
  private String key = null;
  private Integer version = null;
  private String url = null;
  private List<SimpleFormField> fields = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: id 
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: name 
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  


  // nome originario nello yaml: key 
  public String getKey() {
    return key;
  }
  public void setKey(String key) {
    this.key = key;
  }

  /**
   **/
  


  // nome originario nello yaml: version 
  public Integer getVersion() {
    return version;
  }
  public void setVersion(Integer version) {
    this.version = version;
  }

  /**
   **/
  


  // nome originario nello yaml: url 
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   **/
  


  // nome originario nello yaml: fields 
  public List<SimpleFormField> getFields() {
    return fields;
  }
  public void setFields(List<SimpleFormField> fields) {
    this.fields = fields;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SimpleForm simpleForm = (SimpleForm) o;
    return Objects.equals(id, simpleForm.id) &&
        Objects.equals(name, simpleForm.name) &&
        Objects.equals(key, simpleForm.key) &&
        Objects.equals(version, simpleForm.version) &&
        Objects.equals(url, simpleForm.url) &&
        Objects.equals(fields, simpleForm.fields);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, key, version, url, fields);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SimpleForm {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    key: ").append(toIndentedString(key)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    fields: ").append(toIndentedString(fields)).append("\n");
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


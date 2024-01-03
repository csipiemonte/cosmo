/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmopratiche.dto.rest.SimpleFormFieldOption;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class SimpleFormField  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String fieldType = null;
  private String id = null;
  private String name = null;
  private String type = null;
  private String value = null;
  private Boolean required = null;
  private Boolean readOnly = null;
  private String placeholder = null;
  private Boolean hasEmptyValue = null;
  private List<SimpleFormFieldOption> options = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: fieldType 
  public String getFieldType() {
    return fieldType;
  }
  public void setFieldType(String fieldType) {
    this.fieldType = fieldType;
  }

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
  


  // nome originario nello yaml: type 
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  /**
   **/
  


  // nome originario nello yaml: value 
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }

  /**
   **/
  


  // nome originario nello yaml: required 
  public Boolean isRequired() {
    return required;
  }
  public void setRequired(Boolean required) {
    this.required = required;
  }

  /**
   **/
  


  // nome originario nello yaml: readOnly 
  public Boolean isReadOnly() {
    return readOnly;
  }
  public void setReadOnly(Boolean readOnly) {
    this.readOnly = readOnly;
  }

  /**
   **/
  


  // nome originario nello yaml: placeholder 
  public String getPlaceholder() {
    return placeholder;
  }
  public void setPlaceholder(String placeholder) {
    this.placeholder = placeholder;
  }

  /**
   **/
  


  // nome originario nello yaml: hasEmptyValue 
  public Boolean isHasEmptyValue() {
    return hasEmptyValue;
  }
  public void setHasEmptyValue(Boolean hasEmptyValue) {
    this.hasEmptyValue = hasEmptyValue;
  }

  /**
   **/
  


  // nome originario nello yaml: options 
  public List<SimpleFormFieldOption> getOptions() {
    return options;
  }
  public void setOptions(List<SimpleFormFieldOption> options) {
    this.options = options;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SimpleFormField simpleFormField = (SimpleFormField) o;
    return Objects.equals(fieldType, simpleFormField.fieldType) &&
        Objects.equals(id, simpleFormField.id) &&
        Objects.equals(name, simpleFormField.name) &&
        Objects.equals(type, simpleFormField.type) &&
        Objects.equals(value, simpleFormField.value) &&
        Objects.equals(required, simpleFormField.required) &&
        Objects.equals(readOnly, simpleFormField.readOnly) &&
        Objects.equals(placeholder, simpleFormField.placeholder) &&
        Objects.equals(hasEmptyValue, simpleFormField.hasEmptyValue) &&
        Objects.equals(options, simpleFormField.options);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fieldType, id, name, type, value, required, readOnly, placeholder, hasEmptyValue, options);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SimpleFormField {\n");
    
    sb.append("    fieldType: ").append(toIndentedString(fieldType)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    required: ").append(toIndentedString(required)).append("\n");
    sb.append("    readOnly: ").append(toIndentedString(readOnly)).append("\n");
    sb.append("    placeholder: ").append(toIndentedString(placeholder)).append("\n");
    sb.append("    hasEmptyValue: ").append(toIndentedString(hasEmptyValue)).append("\n");
    sb.append("    options: ").append(toIndentedString(options)).append("\n");
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


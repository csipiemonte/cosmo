/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Audit  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private OffsetDateTime creationTimestamp = null;
  private String creatorUser = null;
  private OffsetDateTime modifiedTimestamp = null;
  private String modifierUser = null;
  private OffsetDateTime accessedTimestamp = null;

  /**
   **/
  


  // nome originario nello yaml: creationTimestamp 
  public OffsetDateTime getCreationTimestamp() {
    return creationTimestamp;
  }
  public void setCreationTimestamp(OffsetDateTime creationTimestamp) {
    this.creationTimestamp = creationTimestamp;
  }

  /**
   **/
  


  // nome originario nello yaml: creatorUser 
  public String getCreatorUser() {
    return creatorUser;
  }
  public void setCreatorUser(String creatorUser) {
    this.creatorUser = creatorUser;
  }

  /**
   **/
  


  // nome originario nello yaml: modifiedTimestamp 
  public OffsetDateTime getModifiedTimestamp() {
    return modifiedTimestamp;
  }
  public void setModifiedTimestamp(OffsetDateTime modifiedTimestamp) {
    this.modifiedTimestamp = modifiedTimestamp;
  }

  /**
   **/
  


  // nome originario nello yaml: modifierUser 
  public String getModifierUser() {
    return modifierUser;
  }
  public void setModifierUser(String modifierUser) {
    this.modifierUser = modifierUser;
  }

  /**
   **/
  


  // nome originario nello yaml: accessedTimestamp 
  public OffsetDateTime getAccessedTimestamp() {
    return accessedTimestamp;
  }
  public void setAccessedTimestamp(OffsetDateTime accessedTimestamp) {
    this.accessedTimestamp = accessedTimestamp;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Audit audit = (Audit) o;
    return Objects.equals(creationTimestamp, audit.creationTimestamp) &&
        Objects.equals(creatorUser, audit.creatorUser) &&
        Objects.equals(modifiedTimestamp, audit.modifiedTimestamp) &&
        Objects.equals(modifierUser, audit.modifierUser) &&
        Objects.equals(accessedTimestamp, audit.accessedTimestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(creationTimestamp, creatorUser, modifiedTimestamp, modifierUser, accessedTimestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Audit {\n");
    
    sb.append("    creationTimestamp: ").append(toIndentedString(creationTimestamp)).append("\n");
    sb.append("    creatorUser: ").append(toIndentedString(creatorUser)).append("\n");
    sb.append("    modifiedTimestamp: ").append(toIndentedString(modifiedTimestamp)).append("\n");
    sb.append("    modifierUser: ").append(toIndentedString(modifierUser)).append("\n");
    sb.append("    accessedTimestamp: ").append(toIndentedString(accessedTimestamp)).append("\n");
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


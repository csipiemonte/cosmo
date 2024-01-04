/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.cosmo.cosmosoap.dto.rest.ShareOptions;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CondivisioniRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String sourceIdentifier = null;
  private Entity entity = null;
  private ShareOptions options = null;

  /**
   **/
  


  // nome originario nello yaml: sourceIdentifier 
  public String getSourceIdentifier() {
    return sourceIdentifier;
  }
  public void setSourceIdentifier(String sourceIdentifier) {
    this.sourceIdentifier = sourceIdentifier;
  }

  /**
   **/
  


  // nome originario nello yaml: entity 
  public Entity getEntity() {
    return entity;
  }
  public void setEntity(Entity entity) {
    this.entity = entity;
  }

  /**
   **/
  


  // nome originario nello yaml: options 
  public ShareOptions getOptions() {
    return options;
  }
  public void setOptions(ShareOptions options) {
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
    CondivisioniRequest condivisioniRequest = (CondivisioniRequest) o;
    return Objects.equals(sourceIdentifier, condivisioniRequest.sourceIdentifier) &&
        Objects.equals(entity, condivisioniRequest.entity) &&
        Objects.equals(options, condivisioniRequest.options);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sourceIdentifier, entity, options);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CondivisioniRequest {\n");
    
    sb.append("    sourceIdentifier: ").append(toIndentedString(sourceIdentifier)).append("\n");
    sb.append("    entity: ").append(toIndentedString(entity)).append("\n");
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


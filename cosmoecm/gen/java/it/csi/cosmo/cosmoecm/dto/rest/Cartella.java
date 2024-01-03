/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Cartella  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String path = null;
  private String uuid = null;

  /**
   **/
  


  // nome originario nello yaml: path 
  @NotNull
  public String getPath() {
    return path;
  }
  public void setPath(String path) {
    this.path = path;
  }

  /**
   **/
  


  // nome originario nello yaml: uuid 
  public String getUuid() {
    return uuid;
  }
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Cartella cartella = (Cartella) o;
    return Objects.equals(path, cartella.path) &&
        Objects.equals(uuid, cartella.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(path, uuid);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Cartella {\n");
    
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
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


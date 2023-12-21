/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Preferenza  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String valore = null;
  private String versione = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: valore 
  public String getValore() {
    return valore;
  }
  public void setValore(String valore) {
    this.valore = valore;
  }

  /**
   **/
  


  // nome originario nello yaml: versione 
  public String getVersione() {
    return versione;
  }
  public void setVersione(String versione) {
    this.versione = versione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Preferenza preferenza = (Preferenza) o;
    return Objects.equals(id, preferenza.id) &&
        Objects.equals(valore, preferenza.valore) &&
        Objects.equals(versione, preferenza.versione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, valore, versione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Preferenza {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    valore: ").append(toIndentedString(valore)).append("\n");
    sb.append("    versione: ").append(toIndentedString(versione)).append("\n");
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


/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.ContenutoDocumento;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class ContenutiDocumento  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<ContenutoDocumento> contenuti = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: contenuti 
  public List<ContenutoDocumento> getContenuti() {
    return contenuti;
  }
  public void setContenuti(List<ContenutoDocumento> contenuti) {
    this.contenuti = contenuti;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContenutiDocumento contenutiDocumento = (ContenutiDocumento) o;
    return Objects.equals(contenuti, contenutiDocumento.contenuti);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contenuti);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContenutiDocumento {\n");
    
    sb.append("    contenuti: ").append(toIndentedString(contenuti)).append("\n");
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


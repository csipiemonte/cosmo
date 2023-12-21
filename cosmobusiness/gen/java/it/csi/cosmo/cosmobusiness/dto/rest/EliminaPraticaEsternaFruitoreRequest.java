/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class EliminaPraticaEsternaFruitoreRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceIpaEnte = null;

  /**
   **/
  


  // nome originario nello yaml: codiceIpaEnte 
  @NotNull
  public String getCodiceIpaEnte() {
    return codiceIpaEnte;
  }
  public void setCodiceIpaEnte(String codiceIpaEnte) {
    this.codiceIpaEnte = codiceIpaEnte;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EliminaPraticaEsternaFruitoreRequest eliminaPraticaEsternaFruitoreRequest = (EliminaPraticaEsternaFruitoreRequest) o;
    return Objects.equals(codiceIpaEnte, eliminaPraticaEsternaFruitoreRequest.codiceIpaEnte);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceIpaEnte);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EliminaPraticaEsternaFruitoreRequest {\n");
    
    sb.append("    codiceIpaEnte: ").append(toIndentedString(codiceIpaEnte)).append("\n");
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


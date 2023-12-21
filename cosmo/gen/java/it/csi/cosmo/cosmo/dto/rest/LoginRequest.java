/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class LoginRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long idEnte = null;
  private Long idProfilo = null;

  /**
   **/
  


  // nome originario nello yaml: idEnte 
  @NotNull
  public Long getIdEnte() {
    return idEnte;
  }
  public void setIdEnte(Long idEnte) {
    this.idEnte = idEnte;
  }

  /**
   **/
  


  // nome originario nello yaml: idProfilo 
  @NotNull
  public Long getIdProfilo() {
    return idProfilo;
  }
  public void setIdProfilo(Long idProfilo) {
    this.idProfilo = idProfilo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginRequest loginRequest = (LoginRequest) o;
    return Objects.equals(idEnte, loginRequest.idEnte) &&
        Objects.equals(idProfilo, loginRequest.idProfilo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idEnte, idProfilo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginRequest {\n");
    
    sb.append("    idEnte: ").append(toIndentedString(idEnte)).append("\n");
    sb.append("    idProfilo: ").append(toIndentedString(idProfilo)).append("\n");
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


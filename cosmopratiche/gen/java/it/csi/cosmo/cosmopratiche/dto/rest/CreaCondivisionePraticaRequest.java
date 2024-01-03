/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaCondivisionePraticaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long idUtente = null;
  private Long idGruppo = null;

  /**
   **/
  


  // nome originario nello yaml: idUtente 
  public Long getIdUtente() {
    return idUtente;
  }
  public void setIdUtente(Long idUtente) {
    this.idUtente = idUtente;
  }

  /**
   **/
  


  // nome originario nello yaml: idGruppo 
  public Long getIdGruppo() {
    return idGruppo;
  }
  public void setIdGruppo(Long idGruppo) {
    this.idGruppo = idGruppo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaCondivisionePraticaRequest creaCondivisionePraticaRequest = (CreaCondivisionePraticaRequest) o;
    return Objects.equals(idUtente, creaCondivisionePraticaRequest.idUtente) &&
        Objects.equals(idGruppo, creaCondivisionePraticaRequest.idGruppo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idUtente, idGruppo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaCondivisionePraticaRequest {\n");
    
    sb.append("    idUtente: ").append(toIndentedString(idUtente)).append("\n");
    sb.append("    idGruppo: ").append(toIndentedString(idGruppo)).append("\n");
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


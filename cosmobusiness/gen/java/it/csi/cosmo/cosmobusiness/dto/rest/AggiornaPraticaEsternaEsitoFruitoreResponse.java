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

public class AggiornaPraticaEsternaEsitoFruitoreResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String code = null;
  private Integer status = null;
  private String idPratica = null;

  /**
   **/
  


  // nome originario nello yaml: code 
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }

  /**
   **/
  


  // nome originario nello yaml: status 
  public Integer getStatus() {
    return status;
  }
  public void setStatus(Integer status) {
    this.status = status;
  }

  /**
   **/
  


  // nome originario nello yaml: id_pratica 
  public String getIdPratica() {
    return idPratica;
  }
  public void setIdPratica(String idPratica) {
    this.idPratica = idPratica;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AggiornaPraticaEsternaEsitoFruitoreResponse aggiornaPraticaEsternaEsitoFruitoreResponse = (AggiornaPraticaEsternaEsitoFruitoreResponse) o;
    return Objects.equals(code, aggiornaPraticaEsternaEsitoFruitoreResponse.code) &&
        Objects.equals(status, aggiornaPraticaEsternaEsitoFruitoreResponse.status) &&
        Objects.equals(idPratica, aggiornaPraticaEsternaEsitoFruitoreResponse.idPratica);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, status, idPratica);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AggiornaPraticaEsternaEsitoFruitoreResponse {\n");
    
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    idPratica: ").append(toIndentedString(idPratica)).append("\n");
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


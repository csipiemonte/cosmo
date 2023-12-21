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

public class AvviaProcessoFruitoreResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String idProcesso = null;
  private Integer idPraticaCosmo = null;
  private String idPratica = null;
  private String codiceIpaEnte = null;

  /**
   **/
  


  // nome originario nello yaml: idProcesso 
  @NotNull
  public String getIdProcesso() {
    return idProcesso;
  }
  public void setIdProcesso(String idProcesso) {
    this.idProcesso = idProcesso;
  }

  /**
   **/
  


  // nome originario nello yaml: idPraticaCosmo 
  @NotNull
  public Integer getIdPraticaCosmo() {
    return idPraticaCosmo;
  }
  public void setIdPraticaCosmo(Integer idPraticaCosmo) {
    this.idPraticaCosmo = idPraticaCosmo;
  }

  /**
   **/
  


  // nome originario nello yaml: idPratica 
  @NotNull
  @Size(max=255)
  public String getIdPratica() {
    return idPratica;
  }
  public void setIdPratica(String idPratica) {
    this.idPratica = idPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceIpaEnte 
  @NotNull
  @Size(max=255)
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
    AvviaProcessoFruitoreResponse avviaProcessoFruitoreResponse = (AvviaProcessoFruitoreResponse) o;
    return Objects.equals(idProcesso, avviaProcessoFruitoreResponse.idProcesso) &&
        Objects.equals(idPraticaCosmo, avviaProcessoFruitoreResponse.idPraticaCosmo) &&
        Objects.equals(idPratica, avviaProcessoFruitoreResponse.idPratica) &&
        Objects.equals(codiceIpaEnte, avviaProcessoFruitoreResponse.codiceIpaEnte);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idProcesso, idPraticaCosmo, idPratica, codiceIpaEnte);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AvviaProcessoFruitoreResponse {\n");
    
    sb.append("    idProcesso: ").append(toIndentedString(idProcesso)).append("\n");
    sb.append("    idPraticaCosmo: ").append(toIndentedString(idPraticaCosmo)).append("\n");
    sb.append("    idPratica: ").append(toIndentedString(idPratica)).append("\n");
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


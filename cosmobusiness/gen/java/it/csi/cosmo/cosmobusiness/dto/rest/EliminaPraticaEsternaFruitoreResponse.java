/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmobusiness.dto.rest.EliminaPraticaEsternaEsitoFruitoreResponse;
import java.io.Serializable;
import javax.validation.constraints.*;

public class EliminaPraticaEsternaFruitoreResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private EliminaPraticaEsternaEsitoFruitoreResponse esito = null;

  /**
   **/
  


  // nome originario nello yaml: esito 
  public EliminaPraticaEsternaEsitoFruitoreResponse getEsito() {
    return esito;
  }
  public void setEsito(EliminaPraticaEsternaEsitoFruitoreResponse esito) {
    this.esito = esito;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EliminaPraticaEsternaFruitoreResponse eliminaPraticaEsternaFruitoreResponse = (EliminaPraticaEsternaFruitoreResponse) o;
    return Objects.equals(esito, eliminaPraticaEsternaFruitoreResponse.esito);
  }

  @Override
  public int hashCode() {
    return Objects.hash(esito);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EliminaPraticaEsternaFruitoreResponse {\n");
    
    sb.append("    esito: ").append(toIndentedString(esito)).append("\n");
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


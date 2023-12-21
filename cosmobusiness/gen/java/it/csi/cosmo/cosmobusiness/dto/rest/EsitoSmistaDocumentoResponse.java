/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmobusiness.dto.rest.ResultType;
import java.io.Serializable;
import javax.validation.constraints.*;

public class EsitoSmistaDocumentoResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private ResultType esito = null;

  /**
   **/
  


  // nome originario nello yaml: esito 
  public ResultType getEsito() {
    return esito;
  }
  public void setEsito(ResultType esito) {
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
    EsitoSmistaDocumentoResponse esitoSmistaDocumentoResponse = (EsitoSmistaDocumentoResponse) o;
    return Objects.equals(esito, esitoSmistaDocumentoResponse.esito);
  }

  @Override
  public int hashCode() {
    return Objects.hash(esito);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EsitoSmistaDocumentoResponse {\n");
    
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


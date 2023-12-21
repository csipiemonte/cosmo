/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmobusiness.dto.rest.EsitoSmistaDocumento;
import java.io.Serializable;
import javax.validation.constraints.*;

public class EsitoSmistaDocumentoRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private EsitoSmistaDocumento esitoSmistaDocumento = null;

  /**
   **/
  


  // nome originario nello yaml: esitoSmistaDocumento 
  public EsitoSmistaDocumento getEsitoSmistaDocumento() {
    return esitoSmistaDocumento;
  }
  public void setEsitoSmistaDocumento(EsitoSmistaDocumento esitoSmistaDocumento) {
    this.esitoSmistaDocumento = esitoSmistaDocumento;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsitoSmistaDocumentoRequest esitoSmistaDocumentoRequest = (EsitoSmistaDocumentoRequest) o;
    return Objects.equals(esitoSmistaDocumento, esitoSmistaDocumentoRequest.esitoSmistaDocumento);
  }

  @Override
  public int hashCode() {
    return Objects.hash(esitoSmistaDocumento);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EsitoSmistaDocumentoRequest {\n");
    
    sb.append("    esitoSmistaDocumento: ").append(toIndentedString(esitoSmistaDocumento)).append("\n");
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


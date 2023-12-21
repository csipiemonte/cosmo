/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmobe.dto.rest.StiloAllegatoResponse;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AddUdResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String serviceResponse = null;
  private StiloAllegatoResponse allegato = null;

  /**
   **/
  


  // nome originario nello yaml: serviceResponse 
  public String getServiceResponse() {
    return serviceResponse;
  }
  public void setServiceResponse(String serviceResponse) {
    this.serviceResponse = serviceResponse;
  }

  /**
   **/
  


  // nome originario nello yaml: allegato 
  public StiloAllegatoResponse getAllegato() {
    return allegato;
  }
  public void setAllegato(StiloAllegatoResponse allegato) {
    this.allegato = allegato;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AddUdResponse addUdResponse = (AddUdResponse) o;
    return Objects.equals(serviceResponse, addUdResponse.serviceResponse) &&
        Objects.equals(allegato, addUdResponse.allegato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serviceResponse, allegato);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AddUdResponse {\n");
    
    sb.append("    serviceResponse: ").append(toIndentedString(serviceResponse)).append("\n");
    sb.append("    allegato: ").append(toIndentedString(allegato)).append("\n");
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


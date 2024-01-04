/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiPayload;
import it.csi.cosmo.cosmosoap.dto.rest.DosignEsito;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class FirmaMassivaResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<DocumentiPayload> payloadList = new ArrayList<>();
  private DosignEsito esito = null;

  /**
   **/
  


  // nome originario nello yaml: payloadList 
  public List<DocumentiPayload> getPayloadList() {
    return payloadList;
  }
  public void setPayloadList(List<DocumentiPayload> payloadList) {
    this.payloadList = payloadList;
  }

  /**
   **/
  


  // nome originario nello yaml: esito 
  public DosignEsito getEsito() {
    return esito;
  }
  public void setEsito(DosignEsito esito) {
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
    FirmaMassivaResponse firmaMassivaResponse = (FirmaMassivaResponse) o;
    return Objects.equals(payloadList, firmaMassivaResponse.payloadList) &&
        Objects.equals(esito, firmaMassivaResponse.esito);
  }

  @Override
  public int hashCode() {
    return Objects.hash(payloadList, esito);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FirmaMassivaResponse {\n");
    
    sb.append("    payloadList: ").append(toIndentedString(payloadList)).append("\n");
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


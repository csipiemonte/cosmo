/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.StartTransaction;
import java.io.Serializable;
import javax.validation.constraints.*;

public class StartSessionRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private StartTransaction startTransactionDto = null;
  private String idTransazione = null;

  /**
   **/
  


  // nome originario nello yaml: startTransactionDto 
  @NotNull
  public StartTransaction getStartTransactionDto() {
    return startTransactionDto;
  }
  public void setStartTransactionDto(StartTransaction startTransactionDto) {
    this.startTransactionDto = startTransactionDto;
  }

  /**
   **/
  


  // nome originario nello yaml: idTransazione 
  @NotNull
  public String getIdTransazione() {
    return idTransazione;
  }
  public void setIdTransazione(String idTransazione) {
    this.idTransazione = idTransazione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StartSessionRequest startSessionRequest = (StartSessionRequest) o;
    return Objects.equals(startTransactionDto, startSessionRequest.startTransactionDto) &&
        Objects.equals(idTransazione, startSessionRequest.idTransazione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startTransactionDto, idTransazione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StartSessionRequest {\n");
    
    sb.append("    startTransactionDto: ").append(toIndentedString(startTransactionDto)).append("\n");
    sb.append("    idTransazione: ").append(toIndentedString(idTransazione)).append("\n");
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


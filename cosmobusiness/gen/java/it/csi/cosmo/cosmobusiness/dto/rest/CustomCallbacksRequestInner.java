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

public class CustomCallbacksRequestInner  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String apiManagerId = null;
  private String codiceDescrittivo = null;
  private String nomeVariabile = null;

  /**
   **/
  


  // nome originario nello yaml: apiManagerId 
  @NotNull
  public String getApiManagerId() {
    return apiManagerId;
  }
  public void setApiManagerId(String apiManagerId) {
    this.apiManagerId = apiManagerId;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceDescrittivo 
  @NotNull
  public String getCodiceDescrittivo() {
    return codiceDescrittivo;
  }
  public void setCodiceDescrittivo(String codiceDescrittivo) {
    this.codiceDescrittivo = codiceDescrittivo;
  }

  /**
   **/
  


  // nome originario nello yaml: nomeVariabile 
  public String getNomeVariabile() {
    return nomeVariabile;
  }
  public void setNomeVariabile(String nomeVariabile) {
    this.nomeVariabile = nomeVariabile;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CustomCallbacksRequestInner customCallbacksRequestInner = (CustomCallbacksRequestInner) o;
    return Objects.equals(apiManagerId, customCallbacksRequestInner.apiManagerId) &&
        Objects.equals(codiceDescrittivo, customCallbacksRequestInner.codiceDescrittivo) &&
        Objects.equals(nomeVariabile, customCallbacksRequestInner.nomeVariabile);
  }

  @Override
  public int hashCode() {
    return Objects.hash(apiManagerId, codiceDescrittivo, nomeVariabile);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomCallbacksRequestInner {\n");
    
    sb.append("    apiManagerId: ").append(toIndentedString(apiManagerId)).append("\n");
    sb.append("    codiceDescrittivo: ").append(toIndentedString(codiceDescrittivo)).append("\n");
    sb.append("    nomeVariabile: ").append(toIndentedString(nomeVariabile)).append("\n");
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


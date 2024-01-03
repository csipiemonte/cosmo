/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class RiferimentoFruitore  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String apiManagerId = null;
  private String nomeApp = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

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
  


  // nome originario nello yaml: nomeApp 
  @NotNull
  public String getNomeApp() {
    return nomeApp;
  }
  public void setNomeApp(String nomeApp) {
    this.nomeApp = nomeApp;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiferimentoFruitore riferimentoFruitore = (RiferimentoFruitore) o;
    return Objects.equals(id, riferimentoFruitore.id) &&
        Objects.equals(apiManagerId, riferimentoFruitore.apiManagerId) &&
        Objects.equals(nomeApp, riferimentoFruitore.nomeApp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, apiManagerId, nomeApp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiferimentoFruitore {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    apiManagerId: ").append(toIndentedString(apiManagerId)).append("\n");
    sb.append("    nomeApp: ").append(toIndentedString(nomeApp)).append("\n");
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


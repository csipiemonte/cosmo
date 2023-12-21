/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaFruitoreRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String nomeApp = null;
  private String apiManagerId = null;
  private String url = null;
  private List<Long> idEnti = new ArrayList<>();
  private List<String> autorizzazioni = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: nomeApp 
  @NotNull
  @Size(max=50)
  public String getNomeApp() {
    return nomeApp;
  }
  public void setNomeApp(String nomeApp) {
    this.nomeApp = nomeApp;
  }

  /**
   **/
  


  // nome originario nello yaml: apiManagerId 
  @NotNull
  @Size(max=50)
  public String getApiManagerId() {
    return apiManagerId;
  }
  public void setApiManagerId(String apiManagerId) {
    this.apiManagerId = apiManagerId;
  }

  /**
   **/
  


  // nome originario nello yaml: url 
  @Size(max=2000)
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   **/
  


  // nome originario nello yaml: idEnti 
  public List<Long> getIdEnti() {
    return idEnti;
  }
  public void setIdEnti(List<Long> idEnti) {
    this.idEnti = idEnti;
  }

  /**
   **/
  


  // nome originario nello yaml: autorizzazioni 
  public List<String> getAutorizzazioni() {
    return autorizzazioni;
  }
  public void setAutorizzazioni(List<String> autorizzazioni) {
    this.autorizzazioni = autorizzazioni;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaFruitoreRequest creaFruitoreRequest = (CreaFruitoreRequest) o;
    return Objects.equals(nomeApp, creaFruitoreRequest.nomeApp) &&
        Objects.equals(apiManagerId, creaFruitoreRequest.apiManagerId) &&
        Objects.equals(url, creaFruitoreRequest.url) &&
        Objects.equals(idEnti, creaFruitoreRequest.idEnti) &&
        Objects.equals(autorizzazioni, creaFruitoreRequest.autorizzazioni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nomeApp, apiManagerId, url, idEnti, autorizzazioni);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaFruitoreRequest {\n");
    
    sb.append("    nomeApp: ").append(toIndentedString(nomeApp)).append("\n");
    sb.append("    apiManagerId: ").append(toIndentedString(apiManagerId)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    idEnti: ").append(toIndentedString(idEnti)).append("\n");
    sb.append("    autorizzazioni: ").append(toIndentedString(autorizzazioni)).append("\n");
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


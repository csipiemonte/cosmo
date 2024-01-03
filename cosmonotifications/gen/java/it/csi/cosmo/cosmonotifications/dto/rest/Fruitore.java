/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmonotifications.dto.rest.AssociazioneFruitoreEnte;
import it.csi.cosmo.cosmonotifications.dto.rest.AutorizzazioneFruitore;
import it.csi.cosmo.cosmonotifications.dto.rest.EndpointFruitore;
import it.csi.cosmo.cosmonotifications.dto.rest.SchemaAutenticazioneFruitore;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Fruitore  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String apiManagerId = null;
  private String url = null;
  private String nomeApp = null;
  private List<AssociazioneFruitoreEnte> enti = new ArrayList<>();
  private List<AutorizzazioneFruitore> autorizzazioni = new ArrayList<>();
  private List<EndpointFruitore> endpoints = new ArrayList<>();
  private List<SchemaAutenticazioneFruitore> schemiAutenticazione = new ArrayList<>();

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
  


  // nome originario nello yaml: url 
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   **/
  


  // nome originario nello yaml: nomeApp 
  public String getNomeApp() {
    return nomeApp;
  }
  public void setNomeApp(String nomeApp) {
    this.nomeApp = nomeApp;
  }

  /**
   **/
  


  // nome originario nello yaml: enti 
  public List<AssociazioneFruitoreEnte> getEnti() {
    return enti;
  }
  public void setEnti(List<AssociazioneFruitoreEnte> enti) {
    this.enti = enti;
  }

  /**
   **/
  


  // nome originario nello yaml: autorizzazioni 
  public List<AutorizzazioneFruitore> getAutorizzazioni() {
    return autorizzazioni;
  }
  public void setAutorizzazioni(List<AutorizzazioneFruitore> autorizzazioni) {
    this.autorizzazioni = autorizzazioni;
  }

  /**
   **/
  


  // nome originario nello yaml: endpoints 
  public List<EndpointFruitore> getEndpoints() {
    return endpoints;
  }
  public void setEndpoints(List<EndpointFruitore> endpoints) {
    this.endpoints = endpoints;
  }

  /**
   **/
  


  // nome originario nello yaml: schemiAutenticazione 
  public List<SchemaAutenticazioneFruitore> getSchemiAutenticazione() {
    return schemiAutenticazione;
  }
  public void setSchemiAutenticazione(List<SchemaAutenticazioneFruitore> schemiAutenticazione) {
    this.schemiAutenticazione = schemiAutenticazione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Fruitore fruitore = (Fruitore) o;
    return Objects.equals(id, fruitore.id) &&
        Objects.equals(apiManagerId, fruitore.apiManagerId) &&
        Objects.equals(url, fruitore.url) &&
        Objects.equals(nomeApp, fruitore.nomeApp) &&
        Objects.equals(enti, fruitore.enti) &&
        Objects.equals(autorizzazioni, fruitore.autorizzazioni) &&
        Objects.equals(endpoints, fruitore.endpoints) &&
        Objects.equals(schemiAutenticazione, fruitore.schemiAutenticazione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, apiManagerId, url, nomeApp, enti, autorizzazioni, endpoints, schemiAutenticazione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Fruitore {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    apiManagerId: ").append(toIndentedString(apiManagerId)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    nomeApp: ").append(toIndentedString(nomeApp)).append("\n");
    sb.append("    enti: ").append(toIndentedString(enti)).append("\n");
    sb.append("    autorizzazioni: ").append(toIndentedString(autorizzazioni)).append("\n");
    sb.append("    endpoints: ").append(toIndentedString(endpoints)).append("\n");
    sb.append("    schemiAutenticazione: ").append(toIndentedString(schemiAutenticazione)).append("\n");
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


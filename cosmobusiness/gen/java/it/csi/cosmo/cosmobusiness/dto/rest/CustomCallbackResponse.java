/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmobusiness.dto.rest.SchemaAutenticazioneFruitore;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CustomCallbackResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private SchemaAutenticazioneFruitore schemaAutenticazione = null;
  private String codiceTipoEndpoint = null;
  private String url = null;
  private String metodoHttp = null;
  private String codiceDescrittivo = null;

  /**
   **/
  


  // nome originario nello yaml: schemaAutenticazione 
  public SchemaAutenticazioneFruitore getSchemaAutenticazione() {
    return schemaAutenticazione;
  }
  public void setSchemaAutenticazione(SchemaAutenticazioneFruitore schemaAutenticazione) {
    this.schemaAutenticazione = schemaAutenticazione;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceTipoEndpoint 
  @NotNull
  public String getCodiceTipoEndpoint() {
    return codiceTipoEndpoint;
  }
  public void setCodiceTipoEndpoint(String codiceTipoEndpoint) {
    this.codiceTipoEndpoint = codiceTipoEndpoint;
  }

  /**
   **/
  


  // nome originario nello yaml: url 
  @NotNull
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   **/
  


  // nome originario nello yaml: metodoHttp 
  public String getMetodoHttp() {
    return metodoHttp;
  }
  public void setMetodoHttp(String metodoHttp) {
    this.metodoHttp = metodoHttp;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceDescrittivo 
  public String getCodiceDescrittivo() {
    return codiceDescrittivo;
  }
  public void setCodiceDescrittivo(String codiceDescrittivo) {
    this.codiceDescrittivo = codiceDescrittivo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CustomCallbackResponse customCallbackResponse = (CustomCallbackResponse) o;
    return Objects.equals(schemaAutenticazione, customCallbackResponse.schemaAutenticazione) &&
        Objects.equals(codiceTipoEndpoint, customCallbackResponse.codiceTipoEndpoint) &&
        Objects.equals(url, customCallbackResponse.url) &&
        Objects.equals(metodoHttp, customCallbackResponse.metodoHttp) &&
        Objects.equals(codiceDescrittivo, customCallbackResponse.codiceDescrittivo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(schemaAutenticazione, codiceTipoEndpoint, url, metodoHttp, codiceDescrittivo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomCallbackResponse {\n");
    
    sb.append("    schemaAutenticazione: ").append(toIndentedString(schemaAutenticazione)).append("\n");
    sb.append("    codiceTipoEndpoint: ").append(toIndentedString(codiceTipoEndpoint)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    metodoHttp: ").append(toIndentedString(metodoHttp)).append("\n");
    sb.append("    codiceDescrittivo: ").append(toIndentedString(codiceDescrittivo)).append("\n");
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


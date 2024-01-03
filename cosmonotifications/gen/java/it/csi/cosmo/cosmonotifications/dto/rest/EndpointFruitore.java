/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmonotifications.dto.rest.OperazioneFruitore;
import it.csi.cosmo.cosmonotifications.dto.rest.SchemaAutenticazioneFruitore;
import java.io.Serializable;
import javax.validation.constraints.*;

public class EndpointFruitore  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private SchemaAutenticazioneFruitore schemaAutenticazione = null;
  private OperazioneFruitore operazione = null;
  private String codiceTipoEndpoint = null;
  private String endpoint = null;
  private String metodoHttp = null;
  private String codiceDescrittivo = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  @NotNull
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

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
  


  // nome originario nello yaml: operazione 
  @NotNull
  public OperazioneFruitore getOperazione() {
    return operazione;
  }
  public void setOperazione(OperazioneFruitore operazione) {
    this.operazione = operazione;
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
  


  // nome originario nello yaml: endpoint 
  @NotNull
  public String getEndpoint() {
    return endpoint;
  }
  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
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
    EndpointFruitore endpointFruitore = (EndpointFruitore) o;
    return Objects.equals(id, endpointFruitore.id) &&
        Objects.equals(schemaAutenticazione, endpointFruitore.schemaAutenticazione) &&
        Objects.equals(operazione, endpointFruitore.operazione) &&
        Objects.equals(codiceTipoEndpoint, endpointFruitore.codiceTipoEndpoint) &&
        Objects.equals(endpoint, endpointFruitore.endpoint) &&
        Objects.equals(metodoHttp, endpointFruitore.metodoHttp) &&
        Objects.equals(codiceDescrittivo, endpointFruitore.codiceDescrittivo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, schemaAutenticazione, operazione, codiceTipoEndpoint, endpoint, metodoHttp, codiceDescrittivo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EndpointFruitore {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    schemaAutenticazione: ").append(toIndentedString(schemaAutenticazione)).append("\n");
    sb.append("    operazione: ").append(toIndentedString(operazione)).append("\n");
    sb.append("    codiceTipoEndpoint: ").append(toIndentedString(codiceTipoEndpoint)).append("\n");
    sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
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


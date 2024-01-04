/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.Result;
import it.csi.cosmo.cosmosoap.dto.rest.StatoRichiesta;
import java.io.Serializable;
import javax.validation.constraints.*;

public class GetStatoRichiestaResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Result result = null;
  private StatoRichiesta statoRichiesta = null;
  private String codiceEsitoRichiesta = null;
  private String dettaglioEsitoRichiesta = null;

  /**
   **/
  


  // nome originario nello yaml: result 
  public Result getResult() {
    return result;
  }
  public void setResult(Result result) {
    this.result = result;
  }

  /**
   **/
  


  // nome originario nello yaml: statoRichiesta 
  public StatoRichiesta getStatoRichiesta() {
    return statoRichiesta;
  }
  public void setStatoRichiesta(StatoRichiesta statoRichiesta) {
    this.statoRichiesta = statoRichiesta;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceEsitoRichiesta 
  @Pattern(regexp="[012][0-9]{2}")
  public String getCodiceEsitoRichiesta() {
    return codiceEsitoRichiesta;
  }
  public void setCodiceEsitoRichiesta(String codiceEsitoRichiesta) {
    this.codiceEsitoRichiesta = codiceEsitoRichiesta;
  }

  /**
   **/
  


  // nome originario nello yaml: dettaglioEsitoRichiesta 
  @Size(max=400)
  public String getDettaglioEsitoRichiesta() {
    return dettaglioEsitoRichiesta;
  }
  public void setDettaglioEsitoRichiesta(String dettaglioEsitoRichiesta) {
    this.dettaglioEsitoRichiesta = dettaglioEsitoRichiesta;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetStatoRichiestaResponse getStatoRichiestaResponse = (GetStatoRichiestaResponse) o;
    return Objects.equals(result, getStatoRichiestaResponse.result) &&
        Objects.equals(statoRichiesta, getStatoRichiestaResponse.statoRichiesta) &&
        Objects.equals(codiceEsitoRichiesta, getStatoRichiestaResponse.codiceEsitoRichiesta) &&
        Objects.equals(dettaglioEsitoRichiesta, getStatoRichiestaResponse.dettaglioEsitoRichiesta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(result, statoRichiesta, codiceEsitoRichiesta, dettaglioEsitoRichiesta);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetStatoRichiestaResponse {\n");
    
    sb.append("    result: ").append(toIndentedString(result)).append("\n");
    sb.append("    statoRichiesta: ").append(toIndentedString(statoRichiesta)).append("\n");
    sb.append("    codiceEsitoRichiesta: ").append(toIndentedString(codiceEsitoRichiesta)).append("\n");
    sb.append("    dettaglioEsitoRichiesta: ").append(toIndentedString(dettaglioEsitoRichiesta)).append("\n");
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


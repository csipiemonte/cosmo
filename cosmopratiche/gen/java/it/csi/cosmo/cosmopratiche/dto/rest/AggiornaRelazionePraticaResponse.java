/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaRelazionePraticaResponseRelazioniPratica;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AggiornaRelazionePraticaResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String idPraticaExtDa = null;
  private List<AggiornaRelazionePraticaResponseRelazioniPratica> relazioniPratica = new ArrayList<>();

  /**
   * id esterno della pratica da cui partono le relazioni
   **/
  


  // nome originario nello yaml: idPraticaExtDa 
  @NotNull
  public String getIdPraticaExtDa() {
    return idPraticaExtDa;
  }
  public void setIdPraticaExtDa(String idPraticaExtDa) {
    this.idPraticaExtDa = idPraticaExtDa;
  }

  /**
   * lista contenente i dati delle relazioni di cui è stato richiesto l&#39;inserimento o l&#39;aggiornamento
   **/
  


  // nome originario nello yaml: relazioniPratica 
  public List<AggiornaRelazionePraticaResponseRelazioniPratica> getRelazioniPratica() {
    return relazioniPratica;
  }
  public void setRelazioniPratica(List<AggiornaRelazionePraticaResponseRelazioniPratica> relazioniPratica) {
    this.relazioniPratica = relazioniPratica;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AggiornaRelazionePraticaResponse aggiornaRelazionePraticaResponse = (AggiornaRelazionePraticaResponse) o;
    return Objects.equals(idPraticaExtDa, aggiornaRelazionePraticaResponse.idPraticaExtDa) &&
        Objects.equals(relazioniPratica, aggiornaRelazionePraticaResponse.relazioniPratica);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idPraticaExtDa, relazioniPratica);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AggiornaRelazionePraticaResponse {\n");
    
    sb.append("    idPraticaExtDa: ").append(toIndentedString(idPraticaExtDa)).append("\n");
    sb.append("    relazioniPratica: ").append(toIndentedString(relazioniPratica)).append("\n");
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


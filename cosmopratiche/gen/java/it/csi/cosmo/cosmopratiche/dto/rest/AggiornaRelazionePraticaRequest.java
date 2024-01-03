/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmopratiche.dto.rest.RelazionePratica;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AggiornaRelazionePraticaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceIpaEnte = null;
  private List<RelazionePratica> relazioniPratica = new ArrayList<>();

  /**
   * codice Ipa dell&#39;ente
   **/
  


  // nome originario nello yaml: codiceIpaEnte 
  @NotNull
  public String getCodiceIpaEnte() {
    return codiceIpaEnte;
  }
  public void setCodiceIpaEnte(String codiceIpaEnte) {
    this.codiceIpaEnte = codiceIpaEnte;
  }

  /**
   * lista delle relazioni tra pratiche che si vuole inserire o aggiornare
   **/
  


  // nome originario nello yaml: relazioniPratica 
  public List<RelazionePratica> getRelazioniPratica() {
    return relazioniPratica;
  }
  public void setRelazioniPratica(List<RelazionePratica> relazioniPratica) {
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
    AggiornaRelazionePraticaRequest aggiornaRelazionePraticaRequest = (AggiornaRelazionePraticaRequest) o;
    return Objects.equals(codiceIpaEnte, aggiornaRelazionePraticaRequest.codiceIpaEnte) &&
        Objects.equals(relazioniPratica, aggiornaRelazionePraticaRequest.relazioniPratica);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceIpaEnte, relazioniPratica);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AggiornaRelazionePraticaRequest {\n");
    
    sb.append("    codiceIpaEnte: ").append(toIndentedString(codiceIpaEnte)).append("\n");
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


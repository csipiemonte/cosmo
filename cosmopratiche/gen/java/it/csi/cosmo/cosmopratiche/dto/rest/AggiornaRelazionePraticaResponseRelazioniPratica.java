/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmopratiche.dto.rest.Esito;
import it.csi.cosmo.cosmopratiche.dto.rest.RelazionePratica;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AggiornaRelazionePraticaResponseRelazioniPratica  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private RelazionePratica relazionePratica = null;
  private Esito esito = null;

  /**
   **/
  


  // nome originario nello yaml: relazionePratica 
  @NotNull
  public RelazionePratica getRelazionePratica() {
    return relazionePratica;
  }
  public void setRelazionePratica(RelazionePratica relazionePratica) {
    this.relazionePratica = relazionePratica;
  }

  /**
   **/
  


  // nome originario nello yaml: esito 
  @NotNull
  public Esito getEsito() {
    return esito;
  }
  public void setEsito(Esito esito) {
    this.esito = esito;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AggiornaRelazionePraticaResponseRelazioniPratica aggiornaRelazionePraticaResponseRelazioniPratica = (AggiornaRelazionePraticaResponseRelazioniPratica) o;
    return Objects.equals(relazionePratica, aggiornaRelazionePraticaResponseRelazioniPratica.relazionePratica) &&
        Objects.equals(esito, aggiornaRelazionePraticaResponseRelazioniPratica.esito);
  }

  @Override
  public int hashCode() {
    return Objects.hash(relazionePratica, esito);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AggiornaRelazionePraticaResponseRelazioniPratica {\n");
    
    sb.append("    relazionePratica: ").append(toIndentedString(relazionePratica)).append("\n");
    sb.append("    esito: ").append(toIndentedString(esito)).append("\n");
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


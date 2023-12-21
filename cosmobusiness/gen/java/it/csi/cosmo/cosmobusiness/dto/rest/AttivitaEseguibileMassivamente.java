/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmobusiness.dto.rest.FunzionalitaEseguibileMassivamente;
import it.csi.cosmo.cosmobusiness.dto.rest.Pratica;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoAttivita;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AttivitaEseguibileMassivamente  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Pratica pratica = null;
  private RiferimentoAttivita attivita = null;
  private FunzionalitaEseguibileMassivamente funzionalita = null;

  /**
   **/
  


  // nome originario nello yaml: pratica 
  public Pratica getPratica() {
    return pratica;
  }
  public void setPratica(Pratica pratica) {
    this.pratica = pratica;
  }

  /**
   **/
  


  // nome originario nello yaml: attivita 
  public RiferimentoAttivita getAttivita() {
    return attivita;
  }
  public void setAttivita(RiferimentoAttivita attivita) {
    this.attivita = attivita;
  }

  /**
   **/
  


  // nome originario nello yaml: funzionalita 
  public FunzionalitaEseguibileMassivamente getFunzionalita() {
    return funzionalita;
  }
  public void setFunzionalita(FunzionalitaEseguibileMassivamente funzionalita) {
    this.funzionalita = funzionalita;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttivitaEseguibileMassivamente attivitaEseguibileMassivamente = (AttivitaEseguibileMassivamente) o;
    return Objects.equals(pratica, attivitaEseguibileMassivamente.pratica) &&
        Objects.equals(attivita, attivitaEseguibileMassivamente.attivita) &&
        Objects.equals(funzionalita, attivitaEseguibileMassivamente.funzionalita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pratica, attivita, funzionalita);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttivitaEseguibileMassivamente {\n");
    
    sb.append("    pratica: ").append(toIndentedString(pratica)).append("\n");
    sb.append("    attivita: ").append(toIndentedString(attivita)).append("\n");
    sb.append("    funzionalita: ").append(toIndentedString(funzionalita)).append("\n");
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


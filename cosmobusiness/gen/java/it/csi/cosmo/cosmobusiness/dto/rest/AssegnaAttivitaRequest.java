/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmobusiness.dto.rest.AssegnaAttivitaRequestAssegnazione;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AssegnaAttivitaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<AssegnaAttivitaRequestAssegnazione> assegnazioni = new ArrayList<>();
  private Boolean esclusivo = null;
  private Boolean mantieniAssegnazione = false;

  /**
   **/
  


  // nome originario nello yaml: assegnazioni 
  public List<AssegnaAttivitaRequestAssegnazione> getAssegnazioni() {
    return assegnazioni;
  }
  public void setAssegnazioni(List<AssegnaAttivitaRequestAssegnazione> assegnazioni) {
    this.assegnazioni = assegnazioni;
  }

  /**
   **/
  


  // nome originario nello yaml: esclusivo 
  public Boolean isEsclusivo() {
    return esclusivo;
  }
  public void setEsclusivo(Boolean esclusivo) {
    this.esclusivo = esclusivo;
  }

  /**
   **/
  


  // nome originario nello yaml: mantieniAssegnazione 
  public Boolean isMantieniAssegnazione() {
    return mantieniAssegnazione;
  }
  public void setMantieniAssegnazione(Boolean mantieniAssegnazione) {
    this.mantieniAssegnazione = mantieniAssegnazione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AssegnaAttivitaRequest assegnaAttivitaRequest = (AssegnaAttivitaRequest) o;
    return Objects.equals(assegnazioni, assegnaAttivitaRequest.assegnazioni) &&
        Objects.equals(esclusivo, assegnaAttivitaRequest.esclusivo) &&
        Objects.equals(mantieniAssegnazione, assegnaAttivitaRequest.mantieniAssegnazione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(assegnazioni, esclusivo, mantieniAssegnazione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssegnaAttivitaRequest {\n");
    
    sb.append("    assegnazioni: ").append(toIndentedString(assegnazioni)).append("\n");
    sb.append("    esclusivo: ").append(toIndentedString(esclusivo)).append("\n");
    sb.append("    mantieniAssegnazione: ").append(toIndentedString(mantieniAssegnazione)).append("\n");
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


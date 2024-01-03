/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmopratiche.dto.rest.AssegnazioneStoricoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.AttivitaStoricoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.EventoStoricoPratica;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class StoricoPratica  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<EventoStoricoPratica> eventi = new ArrayList<>();
  private List<AttivitaStoricoPratica> attivita = new ArrayList<>();
  private List<AssegnazioneStoricoPratica> assegnazioni = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: eventi 
  @NotNull
  public List<EventoStoricoPratica> getEventi() {
    return eventi;
  }
  public void setEventi(List<EventoStoricoPratica> eventi) {
    this.eventi = eventi;
  }

  /**
   **/
  


  // nome originario nello yaml: attivita 
  public List<AttivitaStoricoPratica> getAttivita() {
    return attivita;
  }
  public void setAttivita(List<AttivitaStoricoPratica> attivita) {
    this.attivita = attivita;
  }

  /**
   **/
  


  // nome originario nello yaml: assegnazioni 
  public List<AssegnazioneStoricoPratica> getAssegnazioni() {
    return assegnazioni;
  }
  public void setAssegnazioni(List<AssegnazioneStoricoPratica> assegnazioni) {
    this.assegnazioni = assegnazioni;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StoricoPratica storicoPratica = (StoricoPratica) o;
    return Objects.equals(eventi, storicoPratica.eventi) &&
        Objects.equals(attivita, storicoPratica.attivita) &&
        Objects.equals(assegnazioni, storicoPratica.assegnazioni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventi, attivita, assegnazioni);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StoricoPratica {\n");
    
    sb.append("    eventi: ").append(toIndentedString(eventi)).append("\n");
    sb.append("    attivita: ").append(toIndentedString(attivita)).append("\n");
    sb.append("    assegnazioni: ").append(toIndentedString(assegnazioni)).append("\n");
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


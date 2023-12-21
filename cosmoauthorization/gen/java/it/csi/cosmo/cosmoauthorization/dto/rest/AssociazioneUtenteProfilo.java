/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoauthorization.dto.rest.Ente;
import it.csi.cosmo.cosmoauthorization.dto.rest.Profilo;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AssociazioneUtenteProfilo  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Ente ente = null;
  private Profilo profilo = null;

  /**
   **/
  


  // nome originario nello yaml: ente 
  public Ente getEnte() {
    return ente;
  }
  public void setEnte(Ente ente) {
    this.ente = ente;
  }

  /**
   **/
  


  // nome originario nello yaml: profilo 
  @NotNull
  public Profilo getProfilo() {
    return profilo;
  }
  public void setProfilo(Profilo profilo) {
    this.profilo = profilo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AssociazioneUtenteProfilo associazioneUtenteProfilo = (AssociazioneUtenteProfilo) o;
    return Objects.equals(ente, associazioneUtenteProfilo.ente) &&
        Objects.equals(profilo, associazioneUtenteProfilo.profilo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ente, profilo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssociazioneUtenteProfilo {\n");
    
    sb.append("    ente: ").append(toIndentedString(ente)).append("\n");
    sb.append("    profilo: ").append(toIndentedString(profilo)).append("\n");
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


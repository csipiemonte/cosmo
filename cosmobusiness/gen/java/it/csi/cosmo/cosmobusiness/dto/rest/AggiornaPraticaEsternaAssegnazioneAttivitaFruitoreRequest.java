/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String utente = null;
  private String gruppo = null;

  /**
   **/
  


  // nome originario nello yaml: utente 
  public String getUtente() {
    return utente;
  }
  public void setUtente(String utente) {
    this.utente = utente;
  }

  /**
   **/
  


  // nome originario nello yaml: gruppo 
  public String getGruppo() {
    return gruppo;
  }
  public void setGruppo(String gruppo) {
    this.gruppo = gruppo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest aggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest = (AggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest) o;
    return Objects.equals(utente, aggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest.utente) &&
        Objects.equals(gruppo, aggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest.gruppo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(utente, gruppo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest {\n");
    
    sb.append("    utente: ").append(toIndentedString(utente)).append("\n");
    sb.append("    gruppo: ").append(toIndentedString(gruppo)).append("\n");
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


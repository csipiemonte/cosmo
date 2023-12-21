/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmoauthorization.dto.rest.Utente;
import java.io.Serializable;
import javax.validation.constraints.*;

public class UtenteResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Utente utente = null;

  /**
   **/
  


  // nome originario nello yaml: utente 
  public Utente getUtente() {
    return utente;
  }
  public void setUtente(Utente utente) {
    this.utente = utente;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UtenteResponse utenteResponse = (UtenteResponse) o;
    return Objects.equals(utente, utenteResponse.utente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(utente);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UtenteResponse {\n");
    
    sb.append("    utente: ").append(toIndentedString(utente)).append("\n");
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


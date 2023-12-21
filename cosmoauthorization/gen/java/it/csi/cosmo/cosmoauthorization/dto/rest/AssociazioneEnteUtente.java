/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoauthorization.dto.rest.Ente;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AssociazioneEnteUtente  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Ente ente = null;
  private String telefono = null;
  private String email = null;

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
  


  // nome originario nello yaml: telefono 
  public String getTelefono() {
    return telefono;
  }
  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  /**
   **/
  


  // nome originario nello yaml: email 
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AssociazioneEnteUtente associazioneEnteUtente = (AssociazioneEnteUtente) o;
    return Objects.equals(ente, associazioneEnteUtente.ente) &&
        Objects.equals(telefono, associazioneEnteUtente.telefono) &&
        Objects.equals(email, associazioneEnteUtente.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ente, telefono, email);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssociazioneEnteUtente {\n");
    
    sb.append("    ente: ").append(toIndentedString(ente)).append("\n");
    sb.append("    telefono: ").append(toIndentedString(telefono)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
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


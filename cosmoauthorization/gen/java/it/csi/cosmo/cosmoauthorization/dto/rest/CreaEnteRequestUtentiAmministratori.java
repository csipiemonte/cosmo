/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaEnteRequestUtentiAmministratori  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String email = null;
  private String telefono = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: email 
  @Size(max=255)
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   **/
  


  // nome originario nello yaml: telefono 
  @Size(max=30)
  public String getTelefono() {
    return telefono;
  }
  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaEnteRequestUtentiAmministratori creaEnteRequestUtentiAmministratori = (CreaEnteRequestUtentiAmministratori) o;
    return Objects.equals(id, creaEnteRequestUtentiAmministratori.id) &&
        Objects.equals(email, creaEnteRequestUtentiAmministratori.email) &&
        Objects.equals(telefono, creaEnteRequestUtentiAmministratori.telefono);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, telefono);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaEnteRequestUtentiAmministratori {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    telefono: ").append(toIndentedString(telefono)).append("\n");
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


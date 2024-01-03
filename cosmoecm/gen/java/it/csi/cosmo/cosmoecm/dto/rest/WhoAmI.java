/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class WhoAmI  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Object utente = null;
  private Object applicativo = null;

  /**
   **/
  


  // nome originario nello yaml: utente 
  public Object getUtente() {
    return utente;
  }
  public void setUtente(Object utente) {
    this.utente = utente;
  }

  /**
   **/
  


  // nome originario nello yaml: applicativo 
  public Object getApplicativo() {
    return applicativo;
  }
  public void setApplicativo(Object applicativo) {
    this.applicativo = applicativo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WhoAmI whoAmI = (WhoAmI) o;
    return Objects.equals(utente, whoAmI.utente) &&
        Objects.equals(applicativo, whoAmI.applicativo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(utente, applicativo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WhoAmI {\n");
    
    sb.append("    utente: ").append(toIndentedString(utente)).append("\n");
    sb.append("    applicativo: ").append(toIndentedString(applicativo)).append("\n");
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


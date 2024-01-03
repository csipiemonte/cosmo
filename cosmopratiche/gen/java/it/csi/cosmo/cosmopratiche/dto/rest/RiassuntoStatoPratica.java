/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmopratiche.dto.rest.PaginaCommenti;
import it.csi.cosmo.cosmopratiche.dto.rest.PaginaEventi;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import java.io.Serializable;
import javax.validation.constraints.*;

public class RiassuntoStatoPratica  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Pratica pratica = null;
  private PaginaCommenti commenti = null;
  private PaginaEventi attivita = null;

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
  


  // nome originario nello yaml: commenti 
  public PaginaCommenti getCommenti() {
    return commenti;
  }
  public void setCommenti(PaginaCommenti commenti) {
    this.commenti = commenti;
  }

  /**
   **/
  


  // nome originario nello yaml: attivita 
  public PaginaEventi getAttivita() {
    return attivita;
  }
  public void setAttivita(PaginaEventi attivita) {
    this.attivita = attivita;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiassuntoStatoPratica riassuntoStatoPratica = (RiassuntoStatoPratica) o;
    return Objects.equals(pratica, riassuntoStatoPratica.pratica) &&
        Objects.equals(commenti, riassuntoStatoPratica.commenti) &&
        Objects.equals(attivita, riassuntoStatoPratica.attivita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pratica, commenti, attivita);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiassuntoStatoPratica {\n");
    
    sb.append("    pratica: ").append(toIndentedString(pratica)).append("\n");
    sb.append("    commenti: ").append(toIndentedString(commenti)).append("\n");
    sb.append("    attivita: ").append(toIndentedString(attivita)).append("\n");
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


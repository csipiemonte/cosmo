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

public class AssegnaAttivitaRequestAssegnazione  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long idUtente = null;
  private Long idGruppo = null;
  private String tipologia = null;

  /**
   **/
  


  // nome originario nello yaml: idUtente 
  public Long getIdUtente() {
    return idUtente;
  }
  public void setIdUtente(Long idUtente) {
    this.idUtente = idUtente;
  }

  /**
   **/
  


  // nome originario nello yaml: idGruppo 
  public Long getIdGruppo() {
    return idGruppo;
  }
  public void setIdGruppo(Long idGruppo) {
    this.idGruppo = idGruppo;
  }

  /**
   **/
  


  // nome originario nello yaml: tipologia 
  @NotNull
  public String getTipologia() {
    return tipologia;
  }
  public void setTipologia(String tipologia) {
    this.tipologia = tipologia;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AssegnaAttivitaRequestAssegnazione assegnaAttivitaRequestAssegnazione = (AssegnaAttivitaRequestAssegnazione) o;
    return Objects.equals(idUtente, assegnaAttivitaRequestAssegnazione.idUtente) &&
        Objects.equals(idGruppo, assegnaAttivitaRequestAssegnazione.idGruppo) &&
        Objects.equals(tipologia, assegnaAttivitaRequestAssegnazione.tipologia);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idUtente, idGruppo, tipologia);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssegnaAttivitaRequestAssegnazione {\n");
    
    sb.append("    idUtente: ").append(toIndentedString(idUtente)).append("\n");
    sb.append("    idGruppo: ").append(toIndentedString(idGruppo)).append("\n");
    sb.append("    tipologia: ").append(toIndentedString(tipologia)).append("\n");
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


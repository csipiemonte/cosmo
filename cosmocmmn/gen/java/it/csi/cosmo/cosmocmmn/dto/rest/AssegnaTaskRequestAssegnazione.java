/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AssegnaTaskRequestAssegnazione  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceFiscaleUtente = null;
  private String codiceGruppo = null;
  private String tipologia = null;

  /**
   **/
  


  // nome originario nello yaml: codiceFiscaleUtente 
  @Size(min=1,max=255)
  public String getCodiceFiscaleUtente() {
    return codiceFiscaleUtente;
  }
  public void setCodiceFiscaleUtente(String codiceFiscaleUtente) {
    this.codiceFiscaleUtente = codiceFiscaleUtente;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceGruppo 
  @Size(min=1,max=255)
  public String getCodiceGruppo() {
    return codiceGruppo;
  }
  public void setCodiceGruppo(String codiceGruppo) {
    this.codiceGruppo = codiceGruppo;
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
    AssegnaTaskRequestAssegnazione assegnaTaskRequestAssegnazione = (AssegnaTaskRequestAssegnazione) o;
    return Objects.equals(codiceFiscaleUtente, assegnaTaskRequestAssegnazione.codiceFiscaleUtente) &&
        Objects.equals(codiceGruppo, assegnaTaskRequestAssegnazione.codiceGruppo) &&
        Objects.equals(tipologia, assegnaTaskRequestAssegnazione.tipologia);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceFiscaleUtente, codiceGruppo, tipologia);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssegnaTaskRequestAssegnazione {\n");
    
    sb.append("    codiceFiscaleUtente: ").append(toIndentedString(codiceFiscaleUtente)).append("\n");
    sb.append("    codiceGruppo: ").append(toIndentedString(codiceGruppo)).append("\n");
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


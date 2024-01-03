/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class FunzionalitaAttivita  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long idIstanzaFormLogico = null;
  private String codiceFunzionalita = null;
  private Boolean esecuzioneMassiva = null;

  /**
   **/
  


  // nome originario nello yaml: idIstanzaFormLogico 
  public Long getIdIstanzaFormLogico() {
    return idIstanzaFormLogico;
  }
  public void setIdIstanzaFormLogico(Long idIstanzaFormLogico) {
    this.idIstanzaFormLogico = idIstanzaFormLogico;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceFunzionalita 
  public String getCodiceFunzionalita() {
    return codiceFunzionalita;
  }
  public void setCodiceFunzionalita(String codiceFunzionalita) {
    this.codiceFunzionalita = codiceFunzionalita;
  }

  /**
   **/
  


  // nome originario nello yaml: esecuzioneMassiva 
  public Boolean isEsecuzioneMassiva() {
    return esecuzioneMassiva;
  }
  public void setEsecuzioneMassiva(Boolean esecuzioneMassiva) {
    this.esecuzioneMassiva = esecuzioneMassiva;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FunzionalitaAttivita funzionalitaAttivita = (FunzionalitaAttivita) o;
    return Objects.equals(idIstanzaFormLogico, funzionalitaAttivita.idIstanzaFormLogico) &&
        Objects.equals(codiceFunzionalita, funzionalitaAttivita.codiceFunzionalita) &&
        Objects.equals(esecuzioneMassiva, funzionalitaAttivita.esecuzioneMassiva);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idIstanzaFormLogico, codiceFunzionalita, esecuzioneMassiva);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FunzionalitaAttivita {\n");
    
    sb.append("    idIstanzaFormLogico: ").append(toIndentedString(idIstanzaFormLogico)).append("\n");
    sb.append("    codiceFunzionalita: ").append(toIndentedString(codiceFunzionalita)).append("\n");
    sb.append("    esecuzioneMassiva: ").append(toIndentedString(esecuzioneMassiva)).append("\n");
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


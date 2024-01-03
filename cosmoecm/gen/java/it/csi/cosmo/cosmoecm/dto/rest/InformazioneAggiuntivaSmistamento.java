/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.*;

public class InformazioneAggiuntivaSmistamento  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codInformazione = null;
  private String valore = null;

  /**
   **/
  


  // nome originario nello yaml: codInformazione 
  public String getCodInformazione() {
    return codInformazione;
  }
  public void setCodInformazione(String codInformazione) {
    this.codInformazione = codInformazione;
  }

  /**
   **/
  


  // nome originario nello yaml: valore 
  public String getValore() {
    return valore;
  }
  public void setValore(String valore) {
    this.valore = valore;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InformazioneAggiuntivaSmistamento informazioneAggiuntivaSmistamento = (InformazioneAggiuntivaSmistamento) o;
    return Objects.equals(codInformazione, informazioneAggiuntivaSmistamento.codInformazione) &&
        Objects.equals(valore, informazioneAggiuntivaSmistamento.valore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codInformazione, valore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InformazioneAggiuntivaSmistamento {\n");
    
    sb.append("    codInformazione: ").append(toIndentedString(codInformazione)).append("\n");
    sb.append("    valore: ").append(toIndentedString(valore)).append("\n");
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


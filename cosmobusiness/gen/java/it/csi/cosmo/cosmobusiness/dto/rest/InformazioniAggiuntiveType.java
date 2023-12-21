/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmobusiness.dto.rest.InformazioneType;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class InformazioniAggiuntiveType  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<InformazioneType> informazione = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: informazione 
  @NotNull
  public List<InformazioneType> getInformazione() {
    return informazione;
  }
  public void setInformazione(List<InformazioneType> informazione) {
    this.informazione = informazione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InformazioniAggiuntiveType informazioniAggiuntiveType = (InformazioniAggiuntiveType) o;
    return Objects.equals(informazione, informazioniAggiuntiveType.informazione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(informazione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InformazioniAggiuntiveType {\n");
    
    sb.append("    informazione: ").append(toIndentedString(informazione)).append("\n");
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


/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class FirmaDocumentoFruitore  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private OffsetDateTime data = null;
  private String firmatario = null;
  private String organizzazione = null;

  /**
   **/
  


  // nome originario nello yaml: data 
  public OffsetDateTime getData() {
    return data;
  }
  public void setData(OffsetDateTime data) {
    this.data = data;
  }

  /**
   **/
  


  // nome originario nello yaml: firmatario 
  public String getFirmatario() {
    return firmatario;
  }
  public void setFirmatario(String firmatario) {
    this.firmatario = firmatario;
  }

  /**
   **/
  


  // nome originario nello yaml: organizzazione 
  public String getOrganizzazione() {
    return organizzazione;
  }
  public void setOrganizzazione(String organizzazione) {
    this.organizzazione = organizzazione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FirmaDocumentoFruitore firmaDocumentoFruitore = (FirmaDocumentoFruitore) o;
    return Objects.equals(data, firmaDocumentoFruitore.data) &&
        Objects.equals(firmatario, firmaDocumentoFruitore.firmatario) &&
        Objects.equals(organizzazione, firmaDocumentoFruitore.organizzazione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, firmatario, organizzazione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FirmaDocumentoFruitore {\n");
    
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    firmatario: ").append(toIndentedString(firmatario)).append("\n");
    sb.append("    organizzazione: ").append(toIndentedString(organizzazione)).append("\n");
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


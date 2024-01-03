/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmonotifications.dto.rest.RiferimentoEnte;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AssociazioneFruitoreEnte  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private RiferimentoEnte ente = null;

  /**
   **/
  


  // nome originario nello yaml: ente 
  public RiferimentoEnte getEnte() {
    return ente;
  }
  public void setEnte(RiferimentoEnte ente) {
    this.ente = ente;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AssociazioneFruitoreEnte associazioneFruitoreEnte = (AssociazioneFruitoreEnte) o;
    return Objects.equals(ente, associazioneFruitoreEnte.ente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ente);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssociazioneFruitoreEnte {\n");
    
    sb.append("    ente: ").append(toIndentedString(ente)).append("\n");
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


/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.RiferimentoGruppo;
import it.csi.cosmo.cosmosoap.dto.rest.RiferimentoUtente;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CondivisionePratica  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private RiferimentoUtente condivisaDa = null;
  private RiferimentoUtente condivisaAUtente = null;
  private RiferimentoGruppo condivisaAGruppo = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  @NotNull
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: condivisaDa 
  @NotNull
  public RiferimentoUtente getCondivisaDa() {
    return condivisaDa;
  }
  public void setCondivisaDa(RiferimentoUtente condivisaDa) {
    this.condivisaDa = condivisaDa;
  }

  /**
   **/
  


  // nome originario nello yaml: condivisaAUtente 
  public RiferimentoUtente getCondivisaAUtente() {
    return condivisaAUtente;
  }
  public void setCondivisaAUtente(RiferimentoUtente condivisaAUtente) {
    this.condivisaAUtente = condivisaAUtente;
  }

  /**
   **/
  


  // nome originario nello yaml: condivisaAGruppo 
  public RiferimentoGruppo getCondivisaAGruppo() {
    return condivisaAGruppo;
  }
  public void setCondivisaAGruppo(RiferimentoGruppo condivisaAGruppo) {
    this.condivisaAGruppo = condivisaAGruppo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CondivisionePratica condivisionePratica = (CondivisionePratica) o;
    return Objects.equals(id, condivisionePratica.id) &&
        Objects.equals(condivisaDa, condivisionePratica.condivisaDa) &&
        Objects.equals(condivisaAUtente, condivisionePratica.condivisaAUtente) &&
        Objects.equals(condivisaAGruppo, condivisionePratica.condivisaAGruppo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, condivisaDa, condivisaAUtente, condivisaAGruppo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CondivisionePratica {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    condivisaDa: ").append(toIndentedString(condivisaDa)).append("\n");
    sb.append("    condivisaAUtente: ").append(toIndentedString(condivisaAUtente)).append("\n");
    sb.append("    condivisaAGruppo: ").append(toIndentedString(condivisaAGruppo)).append("\n");
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


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

public class AvanzamentoProcessoRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long idPratica = null;
  private String identificativoEvento = null;

  /**
   **/
  


  // nome originario nello yaml: idPratica 
  @NotNull
  public Long getIdPratica() {
    return idPratica;
  }
  public void setIdPratica(Long idPratica) {
    this.idPratica = idPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: identificativoEvento 
  @NotNull
  public String getIdentificativoEvento() {
    return identificativoEvento;
  }
  public void setIdentificativoEvento(String identificativoEvento) {
    this.identificativoEvento = identificativoEvento;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AvanzamentoProcessoRequest avanzamentoProcessoRequest = (AvanzamentoProcessoRequest) o;
    return Objects.equals(idPratica, avanzamentoProcessoRequest.idPratica) &&
        Objects.equals(identificativoEvento, avanzamentoProcessoRequest.identificativoEvento);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idPratica, identificativoEvento);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AvanzamentoProcessoRequest {\n");
    
    sb.append("    idPratica: ").append(toIndentedString(idPratica)).append("\n");
    sb.append("    identificativoEvento: ").append(toIndentedString(identificativoEvento)).append("\n");
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


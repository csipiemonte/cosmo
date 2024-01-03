/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.PreparaEsposizioneDocumentoResponse;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class PreparaEsposizioneDocumentiResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<PreparaEsposizioneDocumentoResponse> documentiEsposti = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: documentiEsposti 
  public List<PreparaEsposizioneDocumentoResponse> getDocumentiEsposti() {
    return documentiEsposti;
  }
  public void setDocumentiEsposti(List<PreparaEsposizioneDocumentoResponse> documentiEsposti) {
    this.documentiEsposti = documentiEsposti;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PreparaEsposizioneDocumentiResponse preparaEsposizioneDocumentiResponse = (PreparaEsposizioneDocumentiResponse) o;
    return Objects.equals(documentiEsposti, preparaEsposizioneDocumentiResponse.documentiEsposti);
  }

  @Override
  public int hashCode() {
    return Objects.hash(documentiEsposti);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PreparaEsposizioneDocumentiResponse {\n");
    
    sb.append("    documentiEsposti: ").append(toIndentedString(documentiEsposti)).append("\n");
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


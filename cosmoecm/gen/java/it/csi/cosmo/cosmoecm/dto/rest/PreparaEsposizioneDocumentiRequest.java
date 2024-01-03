/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.PreparaEsposizioneTipologiaDocumentiRequest;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class PreparaEsposizioneDocumentiRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<PreparaEsposizioneTipologiaDocumentiRequest> tipologieDaEsporre = new ArrayList<>();
  private Boolean ultimoDocumento = null;

  /**
   **/
  


  // nome originario nello yaml: tipologieDaEsporre 
  public List<PreparaEsposizioneTipologiaDocumentiRequest> getTipologieDaEsporre() {
    return tipologieDaEsporre;
  }
  public void setTipologieDaEsporre(List<PreparaEsposizioneTipologiaDocumentiRequest> tipologieDaEsporre) {
    this.tipologieDaEsporre = tipologieDaEsporre;
  }

  /**
   **/
  


  // nome originario nello yaml: ultimoDocumento 
  public Boolean isUltimoDocumento() {
    return ultimoDocumento;
  }
  public void setUltimoDocumento(Boolean ultimoDocumento) {
    this.ultimoDocumento = ultimoDocumento;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PreparaEsposizioneDocumentiRequest preparaEsposizioneDocumentiRequest = (PreparaEsposizioneDocumentiRequest) o;
    return Objects.equals(tipologieDaEsporre, preparaEsposizioneDocumentiRequest.tipologieDaEsporre) &&
        Objects.equals(ultimoDocumento, preparaEsposizioneDocumentiRequest.ultimoDocumento);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipologieDaEsporre, ultimoDocumento);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PreparaEsposizioneDocumentiRequest {\n");
    
    sb.append("    tipologieDaEsporre: ").append(toIndentedString(tipologieDaEsporre)).append("\n");
    sb.append("    ultimoDocumento: ").append(toIndentedString(ultimoDocumento)).append("\n");
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


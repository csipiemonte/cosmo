/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoCreazioneDocumentoFruitore;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class EsitoCreazioneDocumentiFruitore  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<EsitoCreazioneDocumentoFruitore> esiti = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: esiti 
  @NotNull
  public List<EsitoCreazioneDocumentoFruitore> getEsiti() {
    return esiti;
  }
  public void setEsiti(List<EsitoCreazioneDocumentoFruitore> esiti) {
    this.esiti = esiti;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsitoCreazioneDocumentiFruitore esitoCreazioneDocumentiFruitore = (EsitoCreazioneDocumentiFruitore) o;
    return Objects.equals(esiti, esitoCreazioneDocumentiFruitore.esiti);
  }

  @Override
  public int hashCode() {
    return Objects.hash(esiti);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EsitoCreazioneDocumentiFruitore {\n");
    
    sb.append("    esiti: ").append(toIndentedString(esiti)).append("\n");
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


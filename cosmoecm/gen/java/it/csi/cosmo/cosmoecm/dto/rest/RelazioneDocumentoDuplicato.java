/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class RelazioneDocumentoDuplicato  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long idDocumentoDuplicato = null;
  private Long idDocumentoDaDuplicare = null;

  /**
   **/
  


  // nome originario nello yaml: idDocumentoDuplicato 
  public Long getIdDocumentoDuplicato() {
    return idDocumentoDuplicato;
  }
  public void setIdDocumentoDuplicato(Long idDocumentoDuplicato) {
    this.idDocumentoDuplicato = idDocumentoDuplicato;
  }

  /**
   **/
  


  // nome originario nello yaml: idDocumentoDaDuplicare 
  public Long getIdDocumentoDaDuplicare() {
    return idDocumentoDaDuplicare;
  }
  public void setIdDocumentoDaDuplicare(Long idDocumentoDaDuplicare) {
    this.idDocumentoDaDuplicare = idDocumentoDaDuplicare;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RelazioneDocumentoDuplicato relazioneDocumentoDuplicato = (RelazioneDocumentoDuplicato) o;
    return Objects.equals(idDocumentoDuplicato, relazioneDocumentoDuplicato.idDocumentoDuplicato) &&
        Objects.equals(idDocumentoDaDuplicare, relazioneDocumentoDuplicato.idDocumentoDaDuplicare);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idDocumentoDuplicato, idDocumentoDaDuplicare);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RelazioneDocumentoDuplicato {\n");
    
    sb.append("    idDocumentoDuplicato: ").append(toIndentedString(idDocumentoDuplicato)).append("\n");
    sb.append("    idDocumentoDaDuplicare: ").append(toIndentedString(idDocumentoDaDuplicare)).append("\n");
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


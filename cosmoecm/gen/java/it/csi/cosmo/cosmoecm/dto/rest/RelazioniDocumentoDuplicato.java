/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.RelazioneDocumentoDuplicato;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class RelazioniDocumentoDuplicato  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<RelazioneDocumentoDuplicato> relazioneDocumenti = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: relazioneDocumenti 
  public List<RelazioneDocumentoDuplicato> getRelazioneDocumenti() {
    return relazioneDocumenti;
  }
  public void setRelazioneDocumenti(List<RelazioneDocumentoDuplicato> relazioneDocumenti) {
    this.relazioneDocumenti = relazioneDocumenti;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RelazioniDocumentoDuplicato relazioniDocumentoDuplicato = (RelazioniDocumentoDuplicato) o;
    return Objects.equals(relazioneDocumenti, relazioniDocumentoDuplicato.relazioneDocumenti);
  }

  @Override
  public int hashCode() {
    return Objects.hash(relazioneDocumenti);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RelazioniDocumentoDuplicato {\n");
    
    sb.append("    relazioneDocumenti: ").append(toIndentedString(relazioneDocumenti)).append("\n");
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


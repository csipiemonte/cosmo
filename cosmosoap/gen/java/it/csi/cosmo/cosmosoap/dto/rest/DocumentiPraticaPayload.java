/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiPayload;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class DocumentiPraticaPayload  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long idPratica = null;
  private Long idAttivita = null;
  private List<DocumentiPayload> documentiDaFirmare = new ArrayList<>();
  private Long idFunzionalita = null;

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
  


  // nome originario nello yaml: idAttivita 
  @NotNull
  public Long getIdAttivita() {
    return idAttivita;
  }
  public void setIdAttivita(Long idAttivita) {
    this.idAttivita = idAttivita;
  }

  /**
   **/
  


  // nome originario nello yaml: documentiDaFirmare 
  public List<DocumentiPayload> getDocumentiDaFirmare() {
    return documentiDaFirmare;
  }
  public void setDocumentiDaFirmare(List<DocumentiPayload> documentiDaFirmare) {
    this.documentiDaFirmare = documentiDaFirmare;
  }

  /**
   **/
  


  // nome originario nello yaml: idFunzionalita 
  @NotNull
  public Long getIdFunzionalita() {
    return idFunzionalita;
  }
  public void setIdFunzionalita(Long idFunzionalita) {
    this.idFunzionalita = idFunzionalita;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DocumentiPraticaPayload documentiPraticaPayload = (DocumentiPraticaPayload) o;
    return Objects.equals(idPratica, documentiPraticaPayload.idPratica) &&
        Objects.equals(idAttivita, documentiPraticaPayload.idAttivita) &&
        Objects.equals(documentiDaFirmare, documentiPraticaPayload.documentiDaFirmare) &&
        Objects.equals(idFunzionalita, documentiPraticaPayload.idFunzionalita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idPratica, idAttivita, documentiDaFirmare, idFunzionalita);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DocumentiPraticaPayload {\n");
    
    sb.append("    idPratica: ").append(toIndentedString(idPratica)).append("\n");
    sb.append("    idAttivita: ").append(toIndentedString(idAttivita)).append("\n");
    sb.append("    documentiDaFirmare: ").append(toIndentedString(documentiDaFirmare)).append("\n");
    sb.append("    idFunzionalita: ").append(toIndentedString(idFunzionalita)).append("\n");
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


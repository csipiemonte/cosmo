/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.Attivita;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class DocumentiTask  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<Documento> documenti = new ArrayList<>();
  private Attivita attivita = null;

  /**
   **/
  


  // nome originario nello yaml: documenti 
  public List<Documento> getDocumenti() {
    return documenti;
  }
  public void setDocumenti(List<Documento> documenti) {
    this.documenti = documenti;
  }

  /**
   **/
  


  // nome originario nello yaml: attivita 
  public Attivita getAttivita() {
    return attivita;
  }
  public void setAttivita(Attivita attivita) {
    this.attivita = attivita;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DocumentiTask documentiTask = (DocumentiTask) o;
    return Objects.equals(documenti, documentiTask.documenti) &&
        Objects.equals(attivita, documentiTask.attivita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(documenti, attivita);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DocumentiTask {\n");
    
    sb.append("    documenti: ").append(toIndentedString(documenti)).append("\n");
    sb.append("    attivita: ").append(toIndentedString(attivita)).append("\n");
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


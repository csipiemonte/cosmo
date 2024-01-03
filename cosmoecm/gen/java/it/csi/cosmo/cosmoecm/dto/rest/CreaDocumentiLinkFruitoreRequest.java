/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentoLinkFruitoreRequest;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaDocumentiLinkFruitoreRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<CreaDocumentoLinkFruitoreRequest> documenti = new ArrayList<>();
  private String idPratica = null;
  private String codiceIpaEnte = null;
  private Boolean richiediCallback = false;

  /**
   **/
  


  // nome originario nello yaml: documenti 
  @NotNull
  public List<CreaDocumentoLinkFruitoreRequest> getDocumenti() {
    return documenti;
  }
  public void setDocumenti(List<CreaDocumentoLinkFruitoreRequest> documenti) {
    this.documenti = documenti;
  }

  /**
   **/
  


  // nome originario nello yaml: idPratica 
  @NotNull
  @Size(min=1,max=255)
  public String getIdPratica() {
    return idPratica;
  }
  public void setIdPratica(String idPratica) {
    this.idPratica = idPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceIpaEnte 
  @NotNull
  @Size(min=1,max=255)
  public String getCodiceIpaEnte() {
    return codiceIpaEnte;
  }
  public void setCodiceIpaEnte(String codiceIpaEnte) {
    this.codiceIpaEnte = codiceIpaEnte;
  }

  /**
   **/
  


  // nome originario nello yaml: richiediCallback 
  public Boolean isRichiediCallback() {
    return richiediCallback;
  }
  public void setRichiediCallback(Boolean richiediCallback) {
    this.richiediCallback = richiediCallback;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaDocumentiLinkFruitoreRequest creaDocumentiLinkFruitoreRequest = (CreaDocumentiLinkFruitoreRequest) o;
    return Objects.equals(documenti, creaDocumentiLinkFruitoreRequest.documenti) &&
        Objects.equals(idPratica, creaDocumentiLinkFruitoreRequest.idPratica) &&
        Objects.equals(codiceIpaEnte, creaDocumentiLinkFruitoreRequest.codiceIpaEnte) &&
        Objects.equals(richiediCallback, creaDocumentiLinkFruitoreRequest.richiediCallback);
  }

  @Override
  public int hashCode() {
    return Objects.hash(documenti, idPratica, codiceIpaEnte, richiediCallback);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaDocumentiLinkFruitoreRequest {\n");
    
    sb.append("    documenti: ").append(toIndentedString(documenti)).append("\n");
    sb.append("    idPratica: ").append(toIndentedString(idPratica)).append("\n");
    sb.append("    codiceIpaEnte: ").append(toIndentedString(codiceIpaEnte)).append("\n");
    sb.append("    richiediCallback: ").append(toIndentedString(richiediCallback)).append("\n");
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


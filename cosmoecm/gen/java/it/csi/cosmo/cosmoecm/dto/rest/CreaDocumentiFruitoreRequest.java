/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentoFruitoreRequest;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaDocumentiFruitoreRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<CreaDocumentoFruitoreRequest> documenti = new ArrayList<>();
  private String idPratica = null;
  private String codiceIpaEnte = null;

  /**
   **/
  


  // nome originario nello yaml: documenti 
  @NotNull
  public List<CreaDocumentoFruitoreRequest> getDocumenti() {
    return documenti;
  }
  public void setDocumenti(List<CreaDocumentoFruitoreRequest> documenti) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaDocumentiFruitoreRequest creaDocumentiFruitoreRequest = (CreaDocumentiFruitoreRequest) o;
    return Objects.equals(documenti, creaDocumentiFruitoreRequest.documenti) &&
        Objects.equals(idPratica, creaDocumentiFruitoreRequest.idPratica) &&
        Objects.equals(codiceIpaEnte, creaDocumentiFruitoreRequest.codiceIpaEnte);
  }

  @Override
  public int hashCode() {
    return Objects.hash(documenti, idPratica, codiceIpaEnte);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaDocumentiFruitoreRequest {\n");
    
    sb.append("    documenti: ").append(toIndentedString(documenti)).append("\n");
    sb.append("    idPratica: ").append(toIndentedString(idPratica)).append("\n");
    sb.append("    codiceIpaEnte: ").append(toIndentedString(codiceIpaEnte)).append("\n");
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


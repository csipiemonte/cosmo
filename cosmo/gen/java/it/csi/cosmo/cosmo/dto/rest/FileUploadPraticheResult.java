/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmo.dto.rest.Esito;
import java.io.Serializable;
import javax.validation.constraints.*;

public class FileUploadPraticheResult  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String folderName = null;
  private Esito esito = null;

  /**
   **/
  


  // nome originario nello yaml: folderName 
  public String getFolderName() {
    return folderName;
  }
  public void setFolderName(String folderName) {
    this.folderName = folderName;
  }

  /**
   **/
  


  // nome originario nello yaml: esito 
  public Esito getEsito() {
    return esito;
  }
  public void setEsito(Esito esito) {
    this.esito = esito;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FileUploadPraticheResult fileUploadPraticheResult = (FileUploadPraticheResult) o;
    return Objects.equals(folderName, fileUploadPraticheResult.folderName) &&
        Objects.equals(esito, fileUploadPraticheResult.esito);
  }

  @Override
  public int hashCode() {
    return Objects.hash(folderName, esito);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FileUploadPraticheResult {\n");
    
    sb.append("    folderName: ").append(toIndentedString(folderName)).append("\n");
    sb.append("    esito: ").append(toIndentedString(esito)).append("\n");
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


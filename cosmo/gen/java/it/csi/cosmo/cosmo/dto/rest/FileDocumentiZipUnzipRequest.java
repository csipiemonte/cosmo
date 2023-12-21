/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class FileDocumentiZipUnzipRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String pathFile = null;
  private String nomeFile = null;
  private String utente = null;

  /**
   **/
  


  // nome originario nello yaml: pathFile 
  public String getPathFile() {
    return pathFile;
  }
  public void setPathFile(String pathFile) {
    this.pathFile = pathFile;
  }

  /**
   **/
  


  // nome originario nello yaml: nomeFile 
  public String getNomeFile() {
    return nomeFile;
  }
  public void setNomeFile(String nomeFile) {
    this.nomeFile = nomeFile;
  }

  /**
   **/
  


  // nome originario nello yaml: utente 
  public String getUtente() {
    return utente;
  }
  public void setUtente(String utente) {
    this.utente = utente;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FileDocumentiZipUnzipRequest fileDocumentiZipUnzipRequest = (FileDocumentiZipUnzipRequest) o;
    return Objects.equals(pathFile, fileDocumentiZipUnzipRequest.pathFile) &&
        Objects.equals(nomeFile, fileDocumentiZipUnzipRequest.nomeFile) &&
        Objects.equals(utente, fileDocumentiZipUnzipRequest.utente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pathFile, nomeFile, utente);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FileDocumentiZipUnzipRequest {\n");
    
    sb.append("    pathFile: ").append(toIndentedString(pathFile)).append("\n");
    sb.append("    nomeFile: ").append(toIndentedString(nomeFile)).append("\n");
    sb.append("    utente: ").append(toIndentedString(utente)).append("\n");
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


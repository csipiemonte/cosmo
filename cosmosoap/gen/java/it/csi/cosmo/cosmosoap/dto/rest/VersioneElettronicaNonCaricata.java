/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class VersioneElettronicaNonCaricata  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Integer nroAttachmentAssociato = null;
  private String nomeFile = null;

  /**
   **/
  


  // nome originario nello yaml: nroAttachmentAssociato 
  public Integer getNroAttachmentAssociato() {
    return nroAttachmentAssociato;
  }
  public void setNroAttachmentAssociato(Integer nroAttachmentAssociato) {
    this.nroAttachmentAssociato = nroAttachmentAssociato;
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VersioneElettronicaNonCaricata versioneElettronicaNonCaricata = (VersioneElettronicaNonCaricata) o;
    return Objects.equals(nroAttachmentAssociato, versioneElettronicaNonCaricata.nroAttachmentAssociato) &&
        Objects.equals(nomeFile, versioneElettronicaNonCaricata.nomeFile);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nroAttachmentAssociato, nomeFile);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VersioneElettronicaNonCaricata {\n");
    
    sb.append("    nroAttachmentAssociato: ").append(toIndentedString(nroAttachmentAssociato)).append("\n");
    sb.append("    nomeFile: ").append(toIndentedString(nomeFile)).append("\n");
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


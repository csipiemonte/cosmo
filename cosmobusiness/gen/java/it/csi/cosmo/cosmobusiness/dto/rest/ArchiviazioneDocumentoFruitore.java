/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmobusiness.dto.rest.ProtocolloDocumentoFruitore;
import java.io.Serializable;
import javax.validation.constraints.*;

public class ArchiviazioneDocumentoFruitore  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private ProtocolloDocumentoFruitore protocollo = null;
  private String classificazione = null;

  /**
   **/
  


  // nome originario nello yaml: protocollo 
  public ProtocolloDocumentoFruitore getProtocollo() {
    return protocollo;
  }
  public void setProtocollo(ProtocolloDocumentoFruitore protocollo) {
    this.protocollo = protocollo;
  }

  /**
   **/
  


  // nome originario nello yaml: classificazione 
  public String getClassificazione() {
    return classificazione;
  }
  public void setClassificazione(String classificazione) {
    this.classificazione = classificazione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArchiviazioneDocumentoFruitore archiviazioneDocumentoFruitore = (ArchiviazioneDocumentoFruitore) o;
    return Objects.equals(protocollo, archiviazioneDocumentoFruitore.protocollo) &&
        Objects.equals(classificazione, archiviazioneDocumentoFruitore.classificazione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(protocollo, classificazione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ArchiviazioneDocumentoFruitore {\n");
    
    sb.append("    protocollo: ").append(toIndentedString(protocollo)).append("\n");
    sb.append("    classificazione: ").append(toIndentedString(classificazione)).append("\n");
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


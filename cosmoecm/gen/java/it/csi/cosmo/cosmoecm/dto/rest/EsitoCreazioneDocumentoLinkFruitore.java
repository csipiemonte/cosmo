/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentoLinkFruitoreRequest;
import it.csi.cosmo.cosmoecm.dto.rest.DocumentoFruitore;
import it.csi.cosmo.cosmoecm.dto.rest.Esito;
import java.io.Serializable;
import javax.validation.constraints.*;

public class EsitoCreazioneDocumentoLinkFruitore  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private CreaDocumentoLinkFruitoreRequest input = null;
  private DocumentoFruitore output = null;
  private Esito esito = null;

  /**
   **/
  


  // nome originario nello yaml: input 
  public CreaDocumentoLinkFruitoreRequest getInput() {
    return input;
  }
  public void setInput(CreaDocumentoLinkFruitoreRequest input) {
    this.input = input;
  }

  /**
   **/
  


  // nome originario nello yaml: output 
  public DocumentoFruitore getOutput() {
    return output;
  }
  public void setOutput(DocumentoFruitore output) {
    this.output = output;
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
    EsitoCreazioneDocumentoLinkFruitore esitoCreazioneDocumentoLinkFruitore = (EsitoCreazioneDocumentoLinkFruitore) o;
    return Objects.equals(input, esitoCreazioneDocumentoLinkFruitore.input) &&
        Objects.equals(output, esitoCreazioneDocumentoLinkFruitore.output) &&
        Objects.equals(esito, esitoCreazioneDocumentoLinkFruitore.esito);
  }

  @Override
  public int hashCode() {
    return Objects.hash(input, output, esito);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EsitoCreazioneDocumentoLinkFruitore {\n");
    
    sb.append("    input: ").append(toIndentedString(input)).append("\n");
    sb.append("    output: ").append(toIndentedString(output)).append("\n");
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


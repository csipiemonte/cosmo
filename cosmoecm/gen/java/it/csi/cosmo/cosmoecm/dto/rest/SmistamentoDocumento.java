/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmoecm.dto.rest.InfoAggiuntiveSmistamento;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class SmistamentoDocumento  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceStato = null;
  private String descrizioneStato = null;
  private String codiceEsito = null;
  private String descrizioneEsito = null;
  private List<InfoAggiuntiveSmistamento> infoAggiuntive = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: codiceStato 
  @NotNull
  public String getCodiceStato() {
    return codiceStato;
  }
  public void setCodiceStato(String codiceStato) {
    this.codiceStato = codiceStato;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizioneStato 
  public String getDescrizioneStato() {
    return descrizioneStato;
  }
  public void setDescrizioneStato(String descrizioneStato) {
    this.descrizioneStato = descrizioneStato;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceEsito 
  public String getCodiceEsito() {
    return codiceEsito;
  }
  public void setCodiceEsito(String codiceEsito) {
    this.codiceEsito = codiceEsito;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizioneEsito 
  public String getDescrizioneEsito() {
    return descrizioneEsito;
  }
  public void setDescrizioneEsito(String descrizioneEsito) {
    this.descrizioneEsito = descrizioneEsito;
  }

  /**
   **/
  


  // nome originario nello yaml: infoAggiuntive 
  public List<InfoAggiuntiveSmistamento> getInfoAggiuntive() {
    return infoAggiuntive;
  }
  public void setInfoAggiuntive(List<InfoAggiuntiveSmistamento> infoAggiuntive) {
    this.infoAggiuntive = infoAggiuntive;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SmistamentoDocumento smistamentoDocumento = (SmistamentoDocumento) o;
    return Objects.equals(codiceStato, smistamentoDocumento.codiceStato) &&
        Objects.equals(descrizioneStato, smistamentoDocumento.descrizioneStato) &&
        Objects.equals(codiceEsito, smistamentoDocumento.codiceEsito) &&
        Objects.equals(descrizioneEsito, smistamentoDocumento.descrizioneEsito) &&
        Objects.equals(infoAggiuntive, smistamentoDocumento.infoAggiuntive);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceStato, descrizioneStato, codiceEsito, descrizioneEsito, infoAggiuntive);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SmistamentoDocumento {\n");
    
    sb.append("    codiceStato: ").append(toIndentedString(codiceStato)).append("\n");
    sb.append("    descrizioneStato: ").append(toIndentedString(descrizioneStato)).append("\n");
    sb.append("    codiceEsito: ").append(toIndentedString(codiceEsito)).append("\n");
    sb.append("    descrizioneEsito: ").append(toIndentedString(descrizioneEsito)).append("\n");
    sb.append("    infoAggiuntive: ").append(toIndentedString(infoAggiuntive)).append("\n");
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


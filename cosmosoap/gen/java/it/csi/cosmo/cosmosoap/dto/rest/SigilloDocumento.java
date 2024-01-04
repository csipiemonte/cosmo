/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class SigilloDocumento  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceStato = null;
  private String descrizioneStato = null;
  private String codiceEsito = null;
  private String descrizioneEsito = null;
  private OffsetDateTime dtInserimento = null;

  /**
   **/
  


  // nome originario nello yaml: codiceStato 
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
  


  // nome originario nello yaml: dtInserimento 
  public OffsetDateTime getDtInserimento() {
    return dtInserimento;
  }
  public void setDtInserimento(OffsetDateTime dtInserimento) {
    this.dtInserimento = dtInserimento;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SigilloDocumento sigilloDocumento = (SigilloDocumento) o;
    return Objects.equals(codiceStato, sigilloDocumento.codiceStato) &&
        Objects.equals(descrizioneStato, sigilloDocumento.descrizioneStato) &&
        Objects.equals(codiceEsito, sigilloDocumento.codiceEsito) &&
        Objects.equals(descrizioneEsito, sigilloDocumento.descrizioneEsito) &&
        Objects.equals(dtInserimento, sigilloDocumento.dtInserimento);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceStato, descrizioneStato, codiceEsito, descrizioneEsito, dtInserimento);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SigilloDocumento {\n");
    
    sb.append("    codiceStato: ").append(toIndentedString(codiceStato)).append("\n");
    sb.append("    descrizioneStato: ").append(toIndentedString(descrizioneStato)).append("\n");
    sb.append("    codiceEsito: ").append(toIndentedString(codiceEsito)).append("\n");
    sb.append("    descrizioneEsito: ").append(toIndentedString(descrizioneEsito)).append("\n");
    sb.append("    dtInserimento: ").append(toIndentedString(dtInserimento)).append("\n");
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


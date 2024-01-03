/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class EsitoInvioStiloRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long idUd = null;
  private Integer risultato = null;
  private Integer codiceEsitoInvioStilo = null;
  private String messaggioEsitoInvioStilo = null;

  /**
   **/
  


  // nome originario nello yaml: idUd 
  @NotNull
  public Long getIdUd() {
    return idUd;
  }
  public void setIdUd(Long idUd) {
    this.idUd = idUd;
  }

  /**
   **/
  


  // nome originario nello yaml: risultato 
  @NotNull
  public Integer getRisultato() {
    return risultato;
  }
  public void setRisultato(Integer risultato) {
    this.risultato = risultato;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceEsitoInvioStilo 
  public Integer getCodiceEsitoInvioStilo() {
    return codiceEsitoInvioStilo;
  }
  public void setCodiceEsitoInvioStilo(Integer codiceEsitoInvioStilo) {
    this.codiceEsitoInvioStilo = codiceEsitoInvioStilo;
  }

  /**
   **/
  


  // nome originario nello yaml: messaggioEsitoInvioStilo 
  public String getMessaggioEsitoInvioStilo() {
    return messaggioEsitoInvioStilo;
  }
  public void setMessaggioEsitoInvioStilo(String messaggioEsitoInvioStilo) {
    this.messaggioEsitoInvioStilo = messaggioEsitoInvioStilo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsitoInvioStiloRequest esitoInvioStiloRequest = (EsitoInvioStiloRequest) o;
    return Objects.equals(idUd, esitoInvioStiloRequest.idUd) &&
        Objects.equals(risultato, esitoInvioStiloRequest.risultato) &&
        Objects.equals(codiceEsitoInvioStilo, esitoInvioStiloRequest.codiceEsitoInvioStilo) &&
        Objects.equals(messaggioEsitoInvioStilo, esitoInvioStiloRequest.messaggioEsitoInvioStilo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idUd, risultato, codiceEsitoInvioStilo, messaggioEsitoInvioStilo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EsitoInvioStiloRequest {\n");
    
    sb.append("    idUd: ").append(toIndentedString(idUd)).append("\n");
    sb.append("    risultato: ").append(toIndentedString(risultato)).append("\n");
    sb.append("    codiceEsitoInvioStilo: ").append(toIndentedString(codiceEsitoInvioStilo)).append("\n");
    sb.append("    messaggioEsitoInvioStilo: ").append(toIndentedString(messaggioEsitoInvioStilo)).append("\n");
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


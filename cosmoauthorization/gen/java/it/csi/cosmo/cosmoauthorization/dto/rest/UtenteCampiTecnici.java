/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoauthorization.dto.rest.Utente;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class UtenteCampiTecnici  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Utente utente = null;
  private OffsetDateTime dtInizioValidita = null;
  private OffsetDateTime dtFineValidita = null;

  /**
   **/
  


  // nome originario nello yaml: utente 
  public Utente getUtente() {
    return utente;
  }
  public void setUtente(Utente utente) {
    this.utente = utente;
  }

  /**
   **/
  


  // nome originario nello yaml: dtInizioValidita 
  @NotNull
  public OffsetDateTime getDtInizioValidita() {
    return dtInizioValidita;
  }
  public void setDtInizioValidita(OffsetDateTime dtInizioValidita) {
    this.dtInizioValidita = dtInizioValidita;
  }

  /**
   **/
  


  // nome originario nello yaml: dtFineValidita 
  public OffsetDateTime getDtFineValidita() {
    return dtFineValidita;
  }
  public void setDtFineValidita(OffsetDateTime dtFineValidita) {
    this.dtFineValidita = dtFineValidita;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UtenteCampiTecnici utenteCampiTecnici = (UtenteCampiTecnici) o;
    return Objects.equals(utente, utenteCampiTecnici.utente) &&
        Objects.equals(dtInizioValidita, utenteCampiTecnici.dtInizioValidita) &&
        Objects.equals(dtFineValidita, utenteCampiTecnici.dtFineValidita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(utente, dtInizioValidita, dtFineValidita);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UtenteCampiTecnici {\n");
    
    sb.append("    utente: ").append(toIndentedString(utente)).append("\n");
    sb.append("    dtInizioValidita: ").append(toIndentedString(dtInizioValidita)).append("\n");
    sb.append("    dtFineValidita: ").append(toIndentedString(dtFineValidita)).append("\n");
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


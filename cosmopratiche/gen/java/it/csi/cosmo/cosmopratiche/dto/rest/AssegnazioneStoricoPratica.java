/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoAttivita;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoGruppo;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoUtente;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AssegnazioneStoricoPratica  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private RiferimentoAttivita attivita = null;
  private OffsetDateTime inizio = null;
  private OffsetDateTime fine = null;
  private RiferimentoUtente utente = null;
  private RiferimentoGruppo gruppo = null;

  /**
   **/
  


  // nome originario nello yaml: attivita 
  @NotNull
  public RiferimentoAttivita getAttivita() {
    return attivita;
  }
  public void setAttivita(RiferimentoAttivita attivita) {
    this.attivita = attivita;
  }

  /**
   **/
  


  // nome originario nello yaml: inizio 
  @NotNull
  public OffsetDateTime getInizio() {
    return inizio;
  }
  public void setInizio(OffsetDateTime inizio) {
    this.inizio = inizio;
  }

  /**
   **/
  


  // nome originario nello yaml: fine 
  public OffsetDateTime getFine() {
    return fine;
  }
  public void setFine(OffsetDateTime fine) {
    this.fine = fine;
  }

  /**
   **/
  


  // nome originario nello yaml: utente 
  public RiferimentoUtente getUtente() {
    return utente;
  }
  public void setUtente(RiferimentoUtente utente) {
    this.utente = utente;
  }

  /**
   **/
  


  // nome originario nello yaml: gruppo 
  public RiferimentoGruppo getGruppo() {
    return gruppo;
  }
  public void setGruppo(RiferimentoGruppo gruppo) {
    this.gruppo = gruppo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AssegnazioneStoricoPratica assegnazioneStoricoPratica = (AssegnazioneStoricoPratica) o;
    return Objects.equals(attivita, assegnazioneStoricoPratica.attivita) &&
        Objects.equals(inizio, assegnazioneStoricoPratica.inizio) &&
        Objects.equals(fine, assegnazioneStoricoPratica.fine) &&
        Objects.equals(utente, assegnazioneStoricoPratica.utente) &&
        Objects.equals(gruppo, assegnazioneStoricoPratica.gruppo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attivita, inizio, fine, utente, gruppo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssegnazioneStoricoPratica {\n");
    
    sb.append("    attivita: ").append(toIndentedString(attivita)).append("\n");
    sb.append("    inizio: ").append(toIndentedString(inizio)).append("\n");
    sb.append("    fine: ").append(toIndentedString(fine)).append("\n");
    sb.append("    utente: ").append(toIndentedString(utente)).append("\n");
    sb.append("    gruppo: ").append(toIndentedString(gruppo)).append("\n");
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


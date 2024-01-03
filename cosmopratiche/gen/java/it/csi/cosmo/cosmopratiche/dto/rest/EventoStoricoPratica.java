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
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoFruitore;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoGruppo;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoUtente;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class EventoStoricoPratica  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String tipo = null;
  private OffsetDateTime timestamp = null;
  private RiferimentoAttivita attivita = null;
  private RiferimentoUtente utente = null;
  private RiferimentoFruitore fruitore = null;
  private RiferimentoUtente utenteCoinvolto = null;
  private RiferimentoGruppo gruppoCoinvolto = null;
  private String descrizione = null;

  /**
   **/
  


  // nome originario nello yaml: tipo 
  @NotNull
  public String getTipo() {
    return tipo;
  }
  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  /**
   **/
  


  // nome originario nello yaml: timestamp 
  @NotNull
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }
  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  /**
   **/
  


  // nome originario nello yaml: attivita 
  public RiferimentoAttivita getAttivita() {
    return attivita;
  }
  public void setAttivita(RiferimentoAttivita attivita) {
    this.attivita = attivita;
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
  


  // nome originario nello yaml: fruitore 
  public RiferimentoFruitore getFruitore() {
    return fruitore;
  }
  public void setFruitore(RiferimentoFruitore fruitore) {
    this.fruitore = fruitore;
  }

  /**
   **/
  


  // nome originario nello yaml: utenteCoinvolto 
  public RiferimentoUtente getUtenteCoinvolto() {
    return utenteCoinvolto;
  }
  public void setUtenteCoinvolto(RiferimentoUtente utenteCoinvolto) {
    this.utenteCoinvolto = utenteCoinvolto;
  }

  /**
   **/
  


  // nome originario nello yaml: gruppoCoinvolto 
  public RiferimentoGruppo getGruppoCoinvolto() {
    return gruppoCoinvolto;
  }
  public void setGruppoCoinvolto(RiferimentoGruppo gruppoCoinvolto) {
    this.gruppoCoinvolto = gruppoCoinvolto;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizione 
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventoStoricoPratica eventoStoricoPratica = (EventoStoricoPratica) o;
    return Objects.equals(tipo, eventoStoricoPratica.tipo) &&
        Objects.equals(timestamp, eventoStoricoPratica.timestamp) &&
        Objects.equals(attivita, eventoStoricoPratica.attivita) &&
        Objects.equals(utente, eventoStoricoPratica.utente) &&
        Objects.equals(fruitore, eventoStoricoPratica.fruitore) &&
        Objects.equals(utenteCoinvolto, eventoStoricoPratica.utenteCoinvolto) &&
        Objects.equals(gruppoCoinvolto, eventoStoricoPratica.gruppoCoinvolto) &&
        Objects.equals(descrizione, eventoStoricoPratica.descrizione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipo, timestamp, attivita, utente, fruitore, utenteCoinvolto, gruppoCoinvolto, descrizione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventoStoricoPratica {\n");
    
    sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    attivita: ").append(toIndentedString(attivita)).append("\n");
    sb.append("    utente: ").append(toIndentedString(utente)).append("\n");
    sb.append("    fruitore: ").append(toIndentedString(fruitore)).append("\n");
    sb.append("    utenteCoinvolto: ").append(toIndentedString(utenteCoinvolto)).append("\n");
    sb.append("    gruppoCoinvolto: ").append(toIndentedString(gruppoCoinvolto)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
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


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
import it.csi.cosmo.cosmopratiche.dto.rest.StatoAttivita;
import java.time.OffsetDateTime;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AttivitaStoricoPratica  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private RiferimentoAttivita attivita = null;
  private OffsetDateTime inizio = null;
  private OffsetDateTime fine = null;
  private List<RiferimentoUtente> utentiCoinvolti = new ArrayList<>();
  private List<RiferimentoGruppo> gruppiCoinvolti = new ArrayList<>();
  private StatoAttivita stato = null;
  private RiferimentoUtente esecutore = null;

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
  


  // nome originario nello yaml: utentiCoinvolti 
  public List<RiferimentoUtente> getUtentiCoinvolti() {
    return utentiCoinvolti;
  }
  public void setUtentiCoinvolti(List<RiferimentoUtente> utentiCoinvolti) {
    this.utentiCoinvolti = utentiCoinvolti;
  }

  /**
   **/
  


  // nome originario nello yaml: gruppiCoinvolti 
  public List<RiferimentoGruppo> getGruppiCoinvolti() {
    return gruppiCoinvolti;
  }
  public void setGruppiCoinvolti(List<RiferimentoGruppo> gruppiCoinvolti) {
    this.gruppiCoinvolti = gruppiCoinvolti;
  }

  /**
   **/
  


  // nome originario nello yaml: stato 
  public StatoAttivita getStato() {
    return stato;
  }
  public void setStato(StatoAttivita stato) {
    this.stato = stato;
  }

  /**
   **/
  


  // nome originario nello yaml: esecutore 
  public RiferimentoUtente getEsecutore() {
    return esecutore;
  }
  public void setEsecutore(RiferimentoUtente esecutore) {
    this.esecutore = esecutore;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttivitaStoricoPratica attivitaStoricoPratica = (AttivitaStoricoPratica) o;
    return Objects.equals(attivita, attivitaStoricoPratica.attivita) &&
        Objects.equals(inizio, attivitaStoricoPratica.inizio) &&
        Objects.equals(fine, attivitaStoricoPratica.fine) &&
        Objects.equals(utentiCoinvolti, attivitaStoricoPratica.utentiCoinvolti) &&
        Objects.equals(gruppiCoinvolti, attivitaStoricoPratica.gruppiCoinvolti) &&
        Objects.equals(stato, attivitaStoricoPratica.stato) &&
        Objects.equals(esecutore, attivitaStoricoPratica.esecutore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attivita, inizio, fine, utentiCoinvolti, gruppiCoinvolti, stato, esecutore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttivitaStoricoPratica {\n");
    
    sb.append("    attivita: ").append(toIndentedString(attivita)).append("\n");
    sb.append("    inizio: ").append(toIndentedString(inizio)).append("\n");
    sb.append("    fine: ").append(toIndentedString(fine)).append("\n");
    sb.append("    utentiCoinvolti: ").append(toIndentedString(utentiCoinvolti)).append("\n");
    sb.append("    gruppiCoinvolti: ").append(toIndentedString(gruppiCoinvolti)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    esecutore: ").append(toIndentedString(esecutore)).append("\n");
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


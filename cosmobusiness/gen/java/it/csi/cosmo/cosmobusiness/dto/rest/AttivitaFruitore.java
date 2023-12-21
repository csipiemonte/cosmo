/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmobusiness.dto.rest.AssegnazioneFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.MessaggioFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.SottoAttivitaFruitore;
import java.time.OffsetDateTime;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AttivitaFruitore  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String nome = null;
  private String descrizione = null;
  private OffsetDateTime dataInizio = null;
  private OffsetDateTime dataFine = null;
  private List<AssegnazioneFruitore> assegnazione = new ArrayList<>();
  private List<SottoAttivitaFruitore> sottoAttivita = new ArrayList<>();
  private List<MessaggioFruitore> messaggiCollaboratori = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: nome 
  @NotNull
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
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

  /**
   **/
  


  // nome originario nello yaml: dataInizio 
  public OffsetDateTime getDataInizio() {
    return dataInizio;
  }
  public void setDataInizio(OffsetDateTime dataInizio) {
    this.dataInizio = dataInizio;
  }

  /**
   **/
  


  // nome originario nello yaml: dataFine 
  public OffsetDateTime getDataFine() {
    return dataFine;
  }
  public void setDataFine(OffsetDateTime dataFine) {
    this.dataFine = dataFine;
  }

  /**
   **/
  


  // nome originario nello yaml: assegnazione 
  public List<AssegnazioneFruitore> getAssegnazione() {
    return assegnazione;
  }
  public void setAssegnazione(List<AssegnazioneFruitore> assegnazione) {
    this.assegnazione = assegnazione;
  }

  /**
   * l&#39;oggetto sottotask ha un assegnatario
   **/
  


  // nome originario nello yaml: sottoAttivita 
  public List<SottoAttivitaFruitore> getSottoAttivita() {
    return sottoAttivita;
  }
  public void setSottoAttivita(List<SottoAttivitaFruitore> sottoAttivita) {
    this.sottoAttivita = sottoAttivita;
  }

  /**
   **/
  


  // nome originario nello yaml: messaggiCollaboratori 
  public List<MessaggioFruitore> getMessaggiCollaboratori() {
    return messaggiCollaboratori;
  }
  public void setMessaggiCollaboratori(List<MessaggioFruitore> messaggiCollaboratori) {
    this.messaggiCollaboratori = messaggiCollaboratori;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttivitaFruitore attivitaFruitore = (AttivitaFruitore) o;
    return Objects.equals(nome, attivitaFruitore.nome) &&
        Objects.equals(descrizione, attivitaFruitore.descrizione) &&
        Objects.equals(dataInizio, attivitaFruitore.dataInizio) &&
        Objects.equals(dataFine, attivitaFruitore.dataFine) &&
        Objects.equals(assegnazione, attivitaFruitore.assegnazione) &&
        Objects.equals(sottoAttivita, attivitaFruitore.sottoAttivita) &&
        Objects.equals(messaggiCollaboratori, attivitaFruitore.messaggiCollaboratori);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, descrizione, dataInizio, dataFine, assegnazione, sottoAttivita, messaggiCollaboratori);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttivitaFruitore {\n");
    
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    dataInizio: ").append(toIndentedString(dataInizio)).append("\n");
    sb.append("    dataFine: ").append(toIndentedString(dataFine)).append("\n");
    sb.append("    assegnazione: ").append(toIndentedString(assegnazione)).append("\n");
    sb.append("    sottoAttivita: ").append(toIndentedString(sottoAttivita)).append("\n");
    sb.append("    messaggiCollaboratori: ").append(toIndentedString(messaggiCollaboratori)).append("\n");
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


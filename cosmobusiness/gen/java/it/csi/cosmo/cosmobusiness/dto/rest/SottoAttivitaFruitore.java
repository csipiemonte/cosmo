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
import java.time.OffsetDateTime;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class SottoAttivitaFruitore  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String nome = null;
  private String descrizione = null;
  private OffsetDateTime dataInizio = null;
  private OffsetDateTime dataFine = null;
  private List<AssegnazioneFruitore> assegnazione = new ArrayList<>();

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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SottoAttivitaFruitore sottoAttivitaFruitore = (SottoAttivitaFruitore) o;
    return Objects.equals(nome, sottoAttivitaFruitore.nome) &&
        Objects.equals(descrizione, sottoAttivitaFruitore.descrizione) &&
        Objects.equals(dataInizio, sottoAttivitaFruitore.dataInizio) &&
        Objects.equals(dataFine, sottoAttivitaFruitore.dataFine) &&
        Objects.equals(assegnazione, sottoAttivitaFruitore.assegnazione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, descrizione, dataInizio, dataFine, assegnazione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SottoAttivitaFruitore {\n");
    
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    dataInizio: ").append(toIndentedString(dataInizio)).append("\n");
    sb.append("    dataFine: ").append(toIndentedString(dataFine)).append("\n");
    sb.append("    assegnazione: ").append(toIndentedString(assegnazione)).append("\n");
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


/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AggiornaPraticaEsternaAttivitaFruitoreRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String linkAttivita = null;
  private String nome = null;
  private String descrizione = null;
  private List<AggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest> assegnazione = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: linkAttivita 
  @NotNull
  public String getLinkAttivita() {
    return linkAttivita;
  }
  public void setLinkAttivita(String linkAttivita) {
    this.linkAttivita = linkAttivita;
  }

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
  


  // nome originario nello yaml: assegnazione 
  public List<AggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest> getAssegnazione() {
    return assegnazione;
  }
  public void setAssegnazione(List<AggiornaPraticaEsternaAssegnazioneAttivitaFruitoreRequest> assegnazione) {
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
    AggiornaPraticaEsternaAttivitaFruitoreRequest aggiornaPraticaEsternaAttivitaFruitoreRequest = (AggiornaPraticaEsternaAttivitaFruitoreRequest) o;
    return Objects.equals(linkAttivita, aggiornaPraticaEsternaAttivitaFruitoreRequest.linkAttivita) &&
        Objects.equals(nome, aggiornaPraticaEsternaAttivitaFruitoreRequest.nome) &&
        Objects.equals(descrizione, aggiornaPraticaEsternaAttivitaFruitoreRequest.descrizione) &&
        Objects.equals(assegnazione, aggiornaPraticaEsternaAttivitaFruitoreRequest.assegnazione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(linkAttivita, nome, descrizione, assegnazione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AggiornaPraticaEsternaAttivitaFruitoreRequest {\n");
    
    sb.append("    linkAttivita: ").append(toIndentedString(linkAttivita)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
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


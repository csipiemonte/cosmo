/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Evento  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private String dtCreazione = null;
  private String nome = null;
  private String priorita = null;
  private String dtScadenza = null;
  private String descrizione = null;
  private Boolean sospeso = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: dtCreazione 
  public String getDtCreazione() {
    return dtCreazione;
  }
  public void setDtCreazione(String dtCreazione) {
    this.dtCreazione = dtCreazione;
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
  


  // nome originario nello yaml: priorita 
  public String getPriorita() {
    return priorita;
  }
  public void setPriorita(String priorita) {
    this.priorita = priorita;
  }

  /**
   **/
  


  // nome originario nello yaml: dtScadenza 
  @NotNull
  public String getDtScadenza() {
    return dtScadenza;
  }
  public void setDtScadenza(String dtScadenza) {
    this.dtScadenza = dtScadenza;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizione 
  @NotNull
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  


  // nome originario nello yaml: sospeso 
  public Boolean isSospeso() {
    return sospeso;
  }
  public void setSospeso(Boolean sospeso) {
    this.sospeso = sospeso;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Evento evento = (Evento) o;
    return Objects.equals(id, evento.id) &&
        Objects.equals(dtCreazione, evento.dtCreazione) &&
        Objects.equals(nome, evento.nome) &&
        Objects.equals(priorita, evento.priorita) &&
        Objects.equals(dtScadenza, evento.dtScadenza) &&
        Objects.equals(descrizione, evento.descrizione) &&
        Objects.equals(sospeso, evento.sospeso);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dtCreazione, nome, priorita, dtScadenza, descrizione, sospeso);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Evento {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dtCreazione: ").append(toIndentedString(dtCreazione)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    priorita: ").append(toIndentedString(priorita)).append("\n");
    sb.append("    dtScadenza: ").append(toIndentedString(dtScadenza)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    sospeso: ").append(toIndentedString(sospeso)).append("\n");
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


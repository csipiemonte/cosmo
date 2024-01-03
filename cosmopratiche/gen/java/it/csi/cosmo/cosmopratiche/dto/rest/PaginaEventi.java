/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmopratiche.dto.rest.Evento;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class PaginaEventi  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<Evento> elementi = new ArrayList<>();
  private Integer dimensionePagina = null;
  private Integer totale = null;
  private Integer inizio = null;

  /**
   **/
  


  // nome originario nello yaml: elementi 
  public List<Evento> getElementi() {
    return elementi;
  }
  public void setElementi(List<Evento> elementi) {
    this.elementi = elementi;
  }

  /**
   **/
  


  // nome originario nello yaml: dimensionePagina 
  public Integer getDimensionePagina() {
    return dimensionePagina;
  }
  public void setDimensionePagina(Integer dimensionePagina) {
    this.dimensionePagina = dimensionePagina;
  }

  /**
   **/
  


  // nome originario nello yaml: totale 
  public Integer getTotale() {
    return totale;
  }
  public void setTotale(Integer totale) {
    this.totale = totale;
  }

  /**
   **/
  


  // nome originario nello yaml: inizio 
  public Integer getInizio() {
    return inizio;
  }
  public void setInizio(Integer inizio) {
    this.inizio = inizio;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaginaEventi paginaEventi = (PaginaEventi) o;
    return Objects.equals(elementi, paginaEventi.elementi) &&
        Objects.equals(dimensionePagina, paginaEventi.dimensionePagina) &&
        Objects.equals(totale, paginaEventi.totale) &&
        Objects.equals(inizio, paginaEventi.inizio);
  }

  @Override
  public int hashCode() {
    return Objects.hash(elementi, dimensionePagina, totale, inizio);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaginaEventi {\n");
    
    sb.append("    elementi: ").append(toIndentedString(elementi)).append("\n");
    sb.append("    dimensionePagina: ").append(toIndentedString(dimensionePagina)).append("\n");
    sb.append("    totale: ").append(toIndentedString(totale)).append("\n");
    sb.append("    inizio: ").append(toIndentedString(inizio)).append("\n");
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


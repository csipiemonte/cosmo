/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;
import java.math.BigDecimal;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class PaginaTask  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private BigDecimal dimensionePagina = null;
  private BigDecimal totale = null;
  private BigDecimal inizio = null;
  private List<Task> elementi = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: dimensionePagina 
  public BigDecimal getDimensionePagina() {
    return dimensionePagina;
  }
  public void setDimensionePagina(BigDecimal dimensionePagina) {
    this.dimensionePagina = dimensionePagina;
  }

  /**
   **/
  


  // nome originario nello yaml: totale 
  public BigDecimal getTotale() {
    return totale;
  }
  public void setTotale(BigDecimal totale) {
    this.totale = totale;
  }

  /**
   **/
  


  // nome originario nello yaml: inizio 
  public BigDecimal getInizio() {
    return inizio;
  }
  public void setInizio(BigDecimal inizio) {
    this.inizio = inizio;
  }

  /**
   **/
  


  // nome originario nello yaml: elementi 
  public List<Task> getElementi() {
    return elementi;
  }
  public void setElementi(List<Task> elementi) {
    this.elementi = elementi;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaginaTask paginaTask = (PaginaTask) o;
    return Objects.equals(dimensionePagina, paginaTask.dimensionePagina) &&
        Objects.equals(totale, paginaTask.totale) &&
        Objects.equals(inizio, paginaTask.inizio) &&
        Objects.equals(elementi, paginaTask.elementi);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dimensionePagina, totale, inizio, elementi);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaginaTask {\n");
    
    sb.append("    dimensionePagina: ").append(toIndentedString(dimensionePagina)).append("\n");
    sb.append("    totale: ").append(toIndentedString(totale)).append("\n");
    sb.append("    inizio: ").append(toIndentedString(inizio)).append("\n");
    sb.append("    elementi: ").append(toIndentedString(elementi)).append("\n");
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


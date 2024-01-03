/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.CampiTecnici;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Assegnazione  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String gruppoUtente = null;
  private Boolean assegnatario = null;
  private String idGruppo = null;
  private String idUtente = null;
  private Integer idAttivita = null;
  private CampiTecnici campiTecnici = null;

  /**
   **/
  


  // nome originario nello yaml: gruppoUtente 
  public String getGruppoUtente() {
    return gruppoUtente;
  }
  public void setGruppoUtente(String gruppoUtente) {
    this.gruppoUtente = gruppoUtente;
  }

  /**
   **/
  


  // nome originario nello yaml: assegnatario 
  public Boolean isAssegnatario() {
    return assegnatario;
  }
  public void setAssegnatario(Boolean assegnatario) {
    this.assegnatario = assegnatario;
  }

  /**
   **/
  


  // nome originario nello yaml: idGruppo 
  public String getIdGruppo() {
    return idGruppo;
  }
  public void setIdGruppo(String idGruppo) {
    this.idGruppo = idGruppo;
  }

  /**
   **/
  


  // nome originario nello yaml: idUtente 
  public String getIdUtente() {
    return idUtente;
  }
  public void setIdUtente(String idUtente) {
    this.idUtente = idUtente;
  }

  /**
   **/
  


  // nome originario nello yaml: idAttivita 
  public Integer getIdAttivita() {
    return idAttivita;
  }
  public void setIdAttivita(Integer idAttivita) {
    this.idAttivita = idAttivita;
  }

  /**
   **/
  


  // nome originario nello yaml: campiTecnici 
  public CampiTecnici getCampiTecnici() {
    return campiTecnici;
  }
  public void setCampiTecnici(CampiTecnici campiTecnici) {
    this.campiTecnici = campiTecnici;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Assegnazione assegnazione = (Assegnazione) o;
    return Objects.equals(gruppoUtente, assegnazione.gruppoUtente) &&
        Objects.equals(assegnatario, assegnazione.assegnatario) &&
        Objects.equals(idGruppo, assegnazione.idGruppo) &&
        Objects.equals(idUtente, assegnazione.idUtente) &&
        Objects.equals(idAttivita, assegnazione.idAttivita) &&
        Objects.equals(campiTecnici, assegnazione.campiTecnici);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gruppoUtente, assegnatario, idGruppo, idUtente, idAttivita, campiTecnici);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Assegnazione {\n");
    
    sb.append("    gruppoUtente: ").append(toIndentedString(gruppoUtente)).append("\n");
    sb.append("    assegnatario: ").append(toIndentedString(assegnatario)).append("\n");
    sb.append("    idGruppo: ").append(toIndentedString(idGruppo)).append("\n");
    sb.append("    idUtente: ").append(toIndentedString(idUtente)).append("\n");
    sb.append("    idAttivita: ").append(toIndentedString(idAttivita)).append("\n");
    sb.append("    campiTecnici: ").append(toIndentedString(campiTecnici)).append("\n");
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


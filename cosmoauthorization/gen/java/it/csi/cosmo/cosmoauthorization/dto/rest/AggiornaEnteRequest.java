/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaEnteRequestNuoviUtentiAmministratori;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaEnteRequestUtentiAmministratori;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AggiornaEnteRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String nome = null;
  private String codiceProfiloDefault = null;
  private List<CreaEnteRequestUtentiAmministratori> utentiAmministratori = new ArrayList<>();
  private List<CreaEnteRequestNuoviUtentiAmministratori> nuoviUtentiAmministratori = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: nome 
  @NotNull
  @Size(max=255)
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceProfiloDefault 
  public String getCodiceProfiloDefault() {
    return codiceProfiloDefault;
  }
  public void setCodiceProfiloDefault(String codiceProfiloDefault) {
    this.codiceProfiloDefault = codiceProfiloDefault;
  }

  /**
   **/
  


  // nome originario nello yaml: utentiAmministratori 
  public List<CreaEnteRequestUtentiAmministratori> getUtentiAmministratori() {
    return utentiAmministratori;
  }
  public void setUtentiAmministratori(List<CreaEnteRequestUtentiAmministratori> utentiAmministratori) {
    this.utentiAmministratori = utentiAmministratori;
  }

  /**
   **/
  


  // nome originario nello yaml: nuoviUtentiAmministratori 
  public List<CreaEnteRequestNuoviUtentiAmministratori> getNuoviUtentiAmministratori() {
    return nuoviUtentiAmministratori;
  }
  public void setNuoviUtentiAmministratori(List<CreaEnteRequestNuoviUtentiAmministratori> nuoviUtentiAmministratori) {
    this.nuoviUtentiAmministratori = nuoviUtentiAmministratori;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AggiornaEnteRequest aggiornaEnteRequest = (AggiornaEnteRequest) o;
    return Objects.equals(nome, aggiornaEnteRequest.nome) &&
        Objects.equals(codiceProfiloDefault, aggiornaEnteRequest.codiceProfiloDefault) &&
        Objects.equals(utentiAmministratori, aggiornaEnteRequest.utentiAmministratori) &&
        Objects.equals(nuoviUtentiAmministratori, aggiornaEnteRequest.nuoviUtentiAmministratori);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, codiceProfiloDefault, utentiAmministratori, nuoviUtentiAmministratori);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AggiornaEnteRequest {\n");
    
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    codiceProfiloDefault: ").append(toIndentedString(codiceProfiloDefault)).append("\n");
    sb.append("    utentiAmministratori: ").append(toIndentedString(utentiAmministratori)).append("\n");
    sb.append("    nuoviUtentiAmministratori: ").append(toIndentedString(nuoviUtentiAmministratori)).append("\n");
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


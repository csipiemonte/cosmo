/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaEnteRequestNuoviUtentiAmministratori  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String nome = null;
  private String cognome = null;
  private String codiceFiscale = null;
  private String email = null;
  private String telefono = null;

  /**
   **/
  


  // nome originario nello yaml: nome 
  @Size(max=255)
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   **/
  


  // nome originario nello yaml: cognome 
  @Size(max=255)
  public String getCognome() {
    return cognome;
  }
  public void setCognome(String cognome) {
    this.cognome = cognome;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceFiscale 
  @Size(max=16)
  public String getCodiceFiscale() {
    return codiceFiscale;
  }
  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }

  /**
   **/
  


  // nome originario nello yaml: email 
  @Size(max=255)
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   **/
  


  // nome originario nello yaml: telefono 
  @Size(max=30)
  public String getTelefono() {
    return telefono;
  }
  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaEnteRequestNuoviUtentiAmministratori creaEnteRequestNuoviUtentiAmministratori = (CreaEnteRequestNuoviUtentiAmministratori) o;
    return Objects.equals(nome, creaEnteRequestNuoviUtentiAmministratori.nome) &&
        Objects.equals(cognome, creaEnteRequestNuoviUtentiAmministratori.cognome) &&
        Objects.equals(codiceFiscale, creaEnteRequestNuoviUtentiAmministratori.codiceFiscale) &&
        Objects.equals(email, creaEnteRequestNuoviUtentiAmministratori.email) &&
        Objects.equals(telefono, creaEnteRequestNuoviUtentiAmministratori.telefono);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, cognome, codiceFiscale, email, telefono);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaEnteRequestNuoviUtentiAmministratori {\n");
    
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
    sb.append("    codiceFiscale: ").append(toIndentedString(codiceFiscale)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    telefono: ").append(toIndentedString(telefono)).append("\n");
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


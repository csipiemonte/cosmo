/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmo.dto.rest.Preferenza;
import it.csi.cosmo.cosmo.dto.rest.UserInfoEnte;
import it.csi.cosmo.cosmo.dto.rest.UserInfoGruppo;
import it.csi.cosmo.cosmo.dto.rest.UserInfoProfilo;
import java.time.OffsetDateTime;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class UserInfo  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Boolean anonimo = null;
  private String nome = null;
  private String cognome = null;
  private String codiceFiscale = null;
  private String email = null;
  private String telefono = null;
  private UserInfoEnte ente = null;
  private UserInfoProfilo profilo = null;
  private List<UserInfoGruppo> gruppi = new ArrayList<>();
  private List<Preferenza> preferenze = new ArrayList<>();
  private OffsetDateTime fineValidita = null;
  private String hashIdentita = null;
  private Boolean accessoDiretto = false;

  /**
   **/
  


  // nome originario nello yaml: anonimo 
  public Boolean isAnonimo() {
    return anonimo;
  }
  public void setAnonimo(Boolean anonimo) {
    this.anonimo = anonimo;
  }

  /**
   **/
  


  // nome originario nello yaml: nome 
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   **/
  


  // nome originario nello yaml: cognome 
  public String getCognome() {
    return cognome;
  }
  public void setCognome(String cognome) {
    this.cognome = cognome;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceFiscale 
  public String getCodiceFiscale() {
    return codiceFiscale;
  }
  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }

  /**
   **/
  


  // nome originario nello yaml: email 
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   **/
  


  // nome originario nello yaml: telefono 
  public String getTelefono() {
    return telefono;
  }
  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  /**
   **/
  


  // nome originario nello yaml: ente 
  public UserInfoEnte getEnte() {
    return ente;
  }
  public void setEnte(UserInfoEnte ente) {
    this.ente = ente;
  }

  /**
   **/
  


  // nome originario nello yaml: profilo 
  public UserInfoProfilo getProfilo() {
    return profilo;
  }
  public void setProfilo(UserInfoProfilo profilo) {
    this.profilo = profilo;
  }

  /**
   **/
  


  // nome originario nello yaml: gruppi 
  public List<UserInfoGruppo> getGruppi() {
    return gruppi;
  }
  public void setGruppi(List<UserInfoGruppo> gruppi) {
    this.gruppi = gruppi;
  }

  /**
   **/
  


  // nome originario nello yaml: preferenze 
  public List<Preferenza> getPreferenze() {
    return preferenze;
  }
  public void setPreferenze(List<Preferenza> preferenze) {
    this.preferenze = preferenze;
  }

  /**
   **/
  


  // nome originario nello yaml: fineValidita 
  public OffsetDateTime getFineValidita() {
    return fineValidita;
  }
  public void setFineValidita(OffsetDateTime fineValidita) {
    this.fineValidita = fineValidita;
  }

  /**
   **/
  


  // nome originario nello yaml: hashIdentita 
  public String getHashIdentita() {
    return hashIdentita;
  }
  public void setHashIdentita(String hashIdentita) {
    this.hashIdentita = hashIdentita;
  }

  /**
   **/
  


  // nome originario nello yaml: accessoDiretto 
  public Boolean isAccessoDiretto() {
    return accessoDiretto;
  }
  public void setAccessoDiretto(Boolean accessoDiretto) {
    this.accessoDiretto = accessoDiretto;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserInfo userInfo = (UserInfo) o;
    return Objects.equals(anonimo, userInfo.anonimo) &&
        Objects.equals(nome, userInfo.nome) &&
        Objects.equals(cognome, userInfo.cognome) &&
        Objects.equals(codiceFiscale, userInfo.codiceFiscale) &&
        Objects.equals(email, userInfo.email) &&
        Objects.equals(telefono, userInfo.telefono) &&
        Objects.equals(ente, userInfo.ente) &&
        Objects.equals(profilo, userInfo.profilo) &&
        Objects.equals(gruppi, userInfo.gruppi) &&
        Objects.equals(preferenze, userInfo.preferenze) &&
        Objects.equals(fineValidita, userInfo.fineValidita) &&
        Objects.equals(hashIdentita, userInfo.hashIdentita) &&
        Objects.equals(accessoDiretto, userInfo.accessoDiretto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(anonimo, nome, cognome, codiceFiscale, email, telefono, ente, profilo, gruppi, preferenze, fineValidita, hashIdentita, accessoDiretto);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserInfo {\n");
    
    sb.append("    anonimo: ").append(toIndentedString(anonimo)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
    sb.append("    codiceFiscale: ").append(toIndentedString(codiceFiscale)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    telefono: ").append(toIndentedString(telefono)).append("\n");
    sb.append("    ente: ").append(toIndentedString(ente)).append("\n");
    sb.append("    profilo: ").append(toIndentedString(profilo)).append("\n");
    sb.append("    gruppi: ").append(toIndentedString(gruppi)).append("\n");
    sb.append("    preferenze: ").append(toIndentedString(preferenze)).append("\n");
    sb.append("    fineValidita: ").append(toIndentedString(fineValidita)).append("\n");
    sb.append("    hashIdentita: ").append(toIndentedString(hashIdentita)).append("\n");
    sb.append("    accessoDiretto: ").append(toIndentedString(accessoDiretto)).append("\n");
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


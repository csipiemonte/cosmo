/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmoauthorization.dto.rest.AssociazioneEnteUtente;
import it.csi.cosmo.cosmoauthorization.dto.rest.AssociazioneUtenteProfilo;
import it.csi.cosmo.cosmoauthorization.dto.rest.Gruppo;
import it.csi.cosmo.cosmoauthorization.dto.rest.Preferenza;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Utente  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String codiceFiscale = null;
  private String nome = null;
  private String cognome = null;
  private List<AssociazioneEnteUtente> enti = new ArrayList<>();
  private List<AssociazioneUtenteProfilo> profili = new ArrayList<>();
  private List<Preferenza> preferenze = new ArrayList<>();
  private List<Gruppo> gruppi = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: id 
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceFiscale 
  @NotNull
  public String getCodiceFiscale() {
    return codiceFiscale;
  }
  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
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
  


  // nome originario nello yaml: cognome 
  @NotNull
  public String getCognome() {
    return cognome;
  }
  public void setCognome(String cognome) {
    this.cognome = cognome;
  }

  /**
   **/
  


  // nome originario nello yaml: enti 
  @NotNull
  public List<AssociazioneEnteUtente> getEnti() {
    return enti;
  }
  public void setEnti(List<AssociazioneEnteUtente> enti) {
    this.enti = enti;
  }

  /**
   **/
  


  // nome originario nello yaml: profili 
  @NotNull
  public List<AssociazioneUtenteProfilo> getProfili() {
    return profili;
  }
  public void setProfili(List<AssociazioneUtenteProfilo> profili) {
    this.profili = profili;
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
  


  // nome originario nello yaml: gruppi 
  public List<Gruppo> getGruppi() {
    return gruppi;
  }
  public void setGruppi(List<Gruppo> gruppi) {
    this.gruppi = gruppi;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Utente utente = (Utente) o;
    return Objects.equals(id, utente.id) &&
        Objects.equals(codiceFiscale, utente.codiceFiscale) &&
        Objects.equals(nome, utente.nome) &&
        Objects.equals(cognome, utente.cognome) &&
        Objects.equals(enti, utente.enti) &&
        Objects.equals(profili, utente.profili) &&
        Objects.equals(preferenze, utente.preferenze) &&
        Objects.equals(gruppi, utente.gruppi);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, codiceFiscale, nome, cognome, enti, profili, preferenze, gruppi);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Utente {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    codiceFiscale: ").append(toIndentedString(codiceFiscale)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
    sb.append("    enti: ").append(toIndentedString(enti)).append("\n");
    sb.append("    profili: ").append(toIndentedString(profili)).append("\n");
    sb.append("    preferenze: ").append(toIndentedString(preferenze)).append("\n");
    sb.append("    gruppi: ").append(toIndentedString(gruppi)).append("\n");
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


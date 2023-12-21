/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmoauthorization.dto.rest.Gruppo;
import it.csi.cosmo.cosmoauthorization.dto.rest.Preferenza;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Ente  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String nome = null;
  private String codiceFiscale = null;
  private String codiceIpa = null;
  private String logo = null;
  private List<Preferenza> preferenze = new ArrayList<>();
  private List<Gruppo> gruppi = new ArrayList<>();
  private String codiceProfiloDefault = null;

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
  


  // nome originario nello yaml: codiceFiscale 
  @NotNull
  @Size(max=16)
  public String getCodiceFiscale() {
    return codiceFiscale;
  }
  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceIpa 
  @NotNull
  @Size(max=10)
  public String getCodiceIpa() {
    return codiceIpa;
  }
  public void setCodiceIpa(String codiceIpa) {
    this.codiceIpa = codiceIpa;
  }

  /**
   **/
  


  // nome originario nello yaml: logo 
  public String getLogo() {
    return logo;
  }
  public void setLogo(String logo) {
    this.logo = logo;
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

  /**
   **/
  


  // nome originario nello yaml: codiceProfiloDefault 
  public String getCodiceProfiloDefault() {
    return codiceProfiloDefault;
  }
  public void setCodiceProfiloDefault(String codiceProfiloDefault) {
    this.codiceProfiloDefault = codiceProfiloDefault;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Ente ente = (Ente) o;
    return Objects.equals(id, ente.id) &&
        Objects.equals(nome, ente.nome) &&
        Objects.equals(codiceFiscale, ente.codiceFiscale) &&
        Objects.equals(codiceIpa, ente.codiceIpa) &&
        Objects.equals(logo, ente.logo) &&
        Objects.equals(preferenze, ente.preferenze) &&
        Objects.equals(gruppi, ente.gruppi) &&
        Objects.equals(codiceProfiloDefault, ente.codiceProfiloDefault);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nome, codiceFiscale, codiceIpa, logo, preferenze, gruppi, codiceProfiloDefault);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Ente {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    codiceFiscale: ").append(toIndentedString(codiceFiscale)).append("\n");
    sb.append("    codiceIpa: ").append(toIndentedString(codiceIpa)).append("\n");
    sb.append("    logo: ").append(toIndentedString(logo)).append("\n");
    sb.append("    preferenze: ").append(toIndentedString(preferenze)).append("\n");
    sb.append("    gruppi: ").append(toIndentedString(gruppi)).append("\n");
    sb.append("    codiceProfiloDefault: ").append(toIndentedString(codiceProfiloDefault)).append("\n");
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


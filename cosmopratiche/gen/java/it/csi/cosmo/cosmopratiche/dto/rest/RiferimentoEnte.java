/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.*;

public class RiferimentoEnte  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String nome = null;
  private String codiceFiscale = null;
  private String codiceIpa = null;

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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiferimentoEnte riferimentoEnte = (RiferimentoEnte) o;
    return Objects.equals(id, riferimentoEnte.id) &&
        Objects.equals(nome, riferimentoEnte.nome) &&
        Objects.equals(codiceFiscale, riferimentoEnte.codiceFiscale) &&
        Objects.equals(codiceIpa, riferimentoEnte.codiceIpa);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nome, codiceFiscale, codiceIpa);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiferimentoEnte {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    codiceFiscale: ").append(toIndentedString(codiceFiscale)).append("\n");
    sb.append("    codiceIpa: ").append(toIndentedString(codiceIpa)).append("\n");
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


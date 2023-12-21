/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmo.dto.rest.Preferenza;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class UserInfoEnte  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String nome = null;
  private List<Preferenza> preferenze = new ArrayList<>();

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
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserInfoEnte userInfoEnte = (UserInfoEnte) o;
    return Objects.equals(id, userInfoEnte.id) &&
        Objects.equals(nome, userInfoEnte.nome) &&
        Objects.equals(preferenze, userInfoEnte.preferenze);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nome, preferenze);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserInfoEnte {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    preferenze: ").append(toIndentedString(preferenze)).append("\n");
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


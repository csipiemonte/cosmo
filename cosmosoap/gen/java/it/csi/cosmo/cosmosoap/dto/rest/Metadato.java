/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.ValoriType;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Metadato  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String nome = null;
  private String valore = null;
  private ValoriType valori = null;

  /**
   **/
  


  // nome originario nello yaml: nome 
  @Size(max=200)
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   **/
  


  // nome originario nello yaml: valore 
  @Size(max=1000)
  public String getValore() {
    return valore;
  }
  public void setValore(String valore) {
    this.valore = valore;
  }

  /**
   **/
  


  // nome originario nello yaml: valori 
  public ValoriType getValori() {
    return valori;
  }
  public void setValori(ValoriType valori) {
    this.valori = valori;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Metadato metadato = (Metadato) o;
    return Objects.equals(nome, metadato.nome) &&
        Objects.equals(valore, metadato.valore) &&
        Objects.equals(valori, metadato.valori);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, valore, valori);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Metadato {\n");
    
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    valore: ").append(toIndentedString(valore)).append("\n");
    sb.append("    valori: ").append(toIndentedString(valori)).append("\n");
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


/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.*;

public class IdentitaUtente  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private String identificativoAOO = null;
  private String codiceAOO = null;
  private String descrizioneAOO = null;
  private String identificativoNodo = null;
  private String codiceNodo = null;
  private String descrizioneNodo = null;
  private String identificativoStruttura = null;
  private String codiceStruttura = null;
  private String descrizioneStruttura = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  @NotNull
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: identificativoAOO 
  public String getIdentificativoAOO() {
    return identificativoAOO;
  }
  public void setIdentificativoAOO(String identificativoAOO) {
    this.identificativoAOO = identificativoAOO;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceAOO 
  public String getCodiceAOO() {
    return codiceAOO;
  }
  public void setCodiceAOO(String codiceAOO) {
    this.codiceAOO = codiceAOO;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizioneAOO 
  public String getDescrizioneAOO() {
    return descrizioneAOO;
  }
  public void setDescrizioneAOO(String descrizioneAOO) {
    this.descrizioneAOO = descrizioneAOO;
  }

  /**
   **/
  


  // nome originario nello yaml: identificativoNodo 
  public String getIdentificativoNodo() {
    return identificativoNodo;
  }
  public void setIdentificativoNodo(String identificativoNodo) {
    this.identificativoNodo = identificativoNodo;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceNodo 
  public String getCodiceNodo() {
    return codiceNodo;
  }
  public void setCodiceNodo(String codiceNodo) {
    this.codiceNodo = codiceNodo;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizioneNodo 
  public String getDescrizioneNodo() {
    return descrizioneNodo;
  }
  public void setDescrizioneNodo(String descrizioneNodo) {
    this.descrizioneNodo = descrizioneNodo;
  }

  /**
   **/
  


  // nome originario nello yaml: identificativoStruttura 
  public String getIdentificativoStruttura() {
    return identificativoStruttura;
  }
  public void setIdentificativoStruttura(String identificativoStruttura) {
    this.identificativoStruttura = identificativoStruttura;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceStruttura 
  public String getCodiceStruttura() {
    return codiceStruttura;
  }
  public void setCodiceStruttura(String codiceStruttura) {
    this.codiceStruttura = codiceStruttura;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizioneStruttura 
  public String getDescrizioneStruttura() {
    return descrizioneStruttura;
  }
  public void setDescrizioneStruttura(String descrizioneStruttura) {
    this.descrizioneStruttura = descrizioneStruttura;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IdentitaUtente identitaUtente = (IdentitaUtente) o;
    return Objects.equals(id, identitaUtente.id) &&
        Objects.equals(identificativoAOO, identitaUtente.identificativoAOO) &&
        Objects.equals(codiceAOO, identitaUtente.codiceAOO) &&
        Objects.equals(descrizioneAOO, identitaUtente.descrizioneAOO) &&
        Objects.equals(identificativoNodo, identitaUtente.identificativoNodo) &&
        Objects.equals(codiceNodo, identitaUtente.codiceNodo) &&
        Objects.equals(descrizioneNodo, identitaUtente.descrizioneNodo) &&
        Objects.equals(identificativoStruttura, identitaUtente.identificativoStruttura) &&
        Objects.equals(codiceStruttura, identitaUtente.codiceStruttura) &&
        Objects.equals(descrizioneStruttura, identitaUtente.descrizioneStruttura);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, identificativoAOO, codiceAOO, descrizioneAOO, identificativoNodo, codiceNodo, descrizioneNodo, identificativoStruttura, codiceStruttura, descrizioneStruttura);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IdentitaUtente {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    identificativoAOO: ").append(toIndentedString(identificativoAOO)).append("\n");
    sb.append("    codiceAOO: ").append(toIndentedString(codiceAOO)).append("\n");
    sb.append("    descrizioneAOO: ").append(toIndentedString(descrizioneAOO)).append("\n");
    sb.append("    identificativoNodo: ").append(toIndentedString(identificativoNodo)).append("\n");
    sb.append("    codiceNodo: ").append(toIndentedString(codiceNodo)).append("\n");
    sb.append("    descrizioneNodo: ").append(toIndentedString(descrizioneNodo)).append("\n");
    sb.append("    identificativoStruttura: ").append(toIndentedString(identificativoStruttura)).append("\n");
    sb.append("    codiceStruttura: ").append(toIndentedString(codiceStruttura)).append("\n");
    sb.append("    descrizioneStruttura: ").append(toIndentedString(descrizioneStruttura)).append("\n");
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


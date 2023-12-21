/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.*;
@SuppressWarnings("unused")
public class RegaxPersone  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long persCodice = null;

  private String persNome = null;

  private String persCognome = null;

  private String cognomenome = null;

  /**
   **/
  
  @JsonProperty("pers_codice")
  public Long getPersCodice() {
    return persCodice;
  }
  public void setPersCodice(Long persCodice) {
    this.persCodice = persCodice;
  }

  /**
   **/
  
  @JsonProperty("pers_nome")
  public String getPersNome() {
    return persNome;
  }
  public void setPersNome(String persNome) {
    this.persNome = persNome;
  }

  /**
   **/
  
  @JsonProperty("pers_cognome")
  public String getPersCognome() {
    return persCognome;
  }
  public void setPersCognome(String persCognome) {
    this.persCognome = persCognome;
  }

  /**
   **/
  
  @JsonProperty("cognomenome")
  public String getCognomenome() {
    return cognomenome;
  }
  public void setCognomenome(String cognomenome) {
    this.cognomenome = cognomenome;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxPersone regaxPersone = (RegaxPersone) o;
    return Objects.equals(persCodice, regaxPersone.persCodice) &&
        Objects.equals(persNome, regaxPersone.persNome) &&
        Objects.equals(persCognome, regaxPersone.persCognome) &&
        Objects.equals(cognomenome, regaxPersone.cognomenome);
  }

  @Override
  public int hashCode() {
    return Objects.hash(persCodice, persNome, persCognome, cognomenome);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxPersone {\n");
    
    sb.append("    persCodice: ").append(toIndentedString(persCodice)).append("\n");
    sb.append("    persNome: ").append(toIndentedString(persNome)).append("\n");
    sb.append("    persCognome: ").append(toIndentedString(persCognome)).append("\n");
    sb.append("    cognomenome: ").append(toIndentedString(cognomenome)).append("\n");
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


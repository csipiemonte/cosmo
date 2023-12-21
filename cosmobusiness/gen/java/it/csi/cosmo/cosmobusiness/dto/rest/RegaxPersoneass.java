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
public class RegaxPersoneass  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long persCodice = null;

  private Long assCod = null;

  private Long ruoloCodice = null;

  private String personeassData = null;

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
  
  @JsonProperty("ass_cod")
  public Long getAssCod() {
    return assCod;
  }
  public void setAssCod(Long assCod) {
    this.assCod = assCod;
  }

  /**
   **/
  
  @JsonProperty("ruolo_codice")
  public Long getRuoloCodice() {
    return ruoloCodice;
  }
  public void setRuoloCodice(Long ruoloCodice) {
    this.ruoloCodice = ruoloCodice;
  }

  /**
   **/
  
  @JsonProperty("personeass_data")
  public String getPersoneassData() {
    return personeassData;
  }
  public void setPersoneassData(String personeassData) {
    this.personeassData = personeassData;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxPersoneass regaxPersoneass = (RegaxPersoneass) o;
    return Objects.equals(persCodice, regaxPersoneass.persCodice) &&
        Objects.equals(assCod, regaxPersoneass.assCod) &&
        Objects.equals(ruoloCodice, regaxPersoneass.ruoloCodice) &&
        Objects.equals(personeassData, regaxPersoneass.personeassData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(persCodice, assCod, ruoloCodice, personeassData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxPersoneass {\n");
    
    sb.append("    persCodice: ").append(toIndentedString(persCodice)).append("\n");
    sb.append("    assCod: ").append(toIndentedString(assCod)).append("\n");
    sb.append("    ruoloCodice: ").append(toIndentedString(ruoloCodice)).append("\n");
    sb.append("    personeassData: ").append(toIndentedString(personeassData)).append("\n");
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


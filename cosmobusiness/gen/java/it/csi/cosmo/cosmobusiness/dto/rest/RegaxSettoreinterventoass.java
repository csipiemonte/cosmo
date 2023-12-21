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
public class RegaxSettoreinterventoass  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long assCod = null;

  private Long setintCodice = null;

  private String setintassData = null;

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
  
  @JsonProperty("setint_codice")
  public Long getSetintCodice() {
    return setintCodice;
  }
  public void setSetintCodice(Long setintCodice) {
    this.setintCodice = setintCodice;
  }

  /**
   **/
  
  @JsonProperty("setintass_data")
  public String getSetintassData() {
    return setintassData;
  }
  public void setSetintassData(String setintassData) {
    this.setintassData = setintassData;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxSettoreinterventoass regaxSettoreinterventoass = (RegaxSettoreinterventoass) o;
    return Objects.equals(assCod, regaxSettoreinterventoass.assCod) &&
        Objects.equals(setintCodice, regaxSettoreinterventoass.setintCodice) &&
        Objects.equals(setintassData, regaxSettoreinterventoass.setintassData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(assCod, setintCodice, setintassData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxSettoreinterventoass {\n");
    
    sb.append("    assCod: ").append(toIndentedString(assCod)).append("\n");
    sb.append("    setintCodice: ").append(toIndentedString(setintCodice)).append("\n");
    sb.append("    setintassData: ").append(toIndentedString(setintassData)).append("\n");
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


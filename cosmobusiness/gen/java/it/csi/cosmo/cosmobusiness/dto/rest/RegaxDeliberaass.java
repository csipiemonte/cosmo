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
public class RegaxDeliberaass  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long delCodice = null;

  private Long assCod = null;

  /**
   **/
  
  @JsonProperty("del_codice")
  public Long getDelCodice() {
    return delCodice;
  }
  public void setDelCodice(Long delCodice) {
    this.delCodice = delCodice;
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxDeliberaass regaxDeliberaass = (RegaxDeliberaass) o;
    return Objects.equals(delCodice, regaxDeliberaass.delCodice) &&
        Objects.equals(assCod, regaxDeliberaass.assCod);
  }

  @Override
  public int hashCode() {
    return Objects.hash(delCodice, assCod);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxDeliberaass {\n");
    
    sb.append("    delCodice: ").append(toIndentedString(delCodice)).append("\n");
    sb.append("    assCod: ").append(toIndentedString(assCod)).append("\n");
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


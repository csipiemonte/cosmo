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
public class RegaxProtocollo  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long assCod = null;

  private String protNumprotocollo = null;

  private String protDataprotocollo = null;

  private Long protCodice = null;

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
  
  @JsonProperty("prot_numprotocollo")
  public String getProtNumprotocollo() {
    return protNumprotocollo;
  }
  public void setProtNumprotocollo(String protNumprotocollo) {
    this.protNumprotocollo = protNumprotocollo;
  }

  /**
   **/
  
  @JsonProperty("prot_dataprotocollo")
  public String getProtDataprotocollo() {
    return protDataprotocollo;
  }
  public void setProtDataprotocollo(String protDataprotocollo) {
    this.protDataprotocollo = protDataprotocollo;
  }

  /**
   **/
  
  @JsonProperty("prot_codice")
  public Long getProtCodice() {
    return protCodice;
  }
  public void setProtCodice(Long protCodice) {
    this.protCodice = protCodice;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxProtocollo regaxProtocollo = (RegaxProtocollo) o;
    return Objects.equals(assCod, regaxProtocollo.assCod) &&
        Objects.equals(protNumprotocollo, regaxProtocollo.protNumprotocollo) &&
        Objects.equals(protDataprotocollo, regaxProtocollo.protDataprotocollo) &&
        Objects.equals(protCodice, regaxProtocollo.protCodice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(assCod, protNumprotocollo, protDataprotocollo, protCodice);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxProtocollo {\n");
    
    sb.append("    assCod: ").append(toIndentedString(assCod)).append("\n");
    sb.append("    protNumprotocollo: ").append(toIndentedString(protNumprotocollo)).append("\n");
    sb.append("    protDataprotocollo: ").append(toIndentedString(protDataprotocollo)).append("\n");
    sb.append("    protCodice: ").append(toIndentedString(protCodice)).append("\n");
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


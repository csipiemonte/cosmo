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
public class RegaxRelazioneass  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long assCod = null;

  private Long relCodice = null;

  private Long relassAnno = null;

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
  
  @JsonProperty("rel_codice")
  public Long getRelCodice() {
    return relCodice;
  }
  public void setRelCodice(Long relCodice) {
    this.relCodice = relCodice;
  }

  /**
   **/
  
  @JsonProperty("relass_anno")
  public Long getRelassAnno() {
    return relassAnno;
  }
  public void setRelassAnno(Long relassAnno) {
    this.relassAnno = relassAnno;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxRelazioneass regaxRelazioneass = (RegaxRelazioneass) o;
    return Objects.equals(assCod, regaxRelazioneass.assCod) &&
        Objects.equals(relCodice, regaxRelazioneass.relCodice) &&
        Objects.equals(relassAnno, regaxRelazioneass.relassAnno);
  }

  @Override
  public int hashCode() {
    return Objects.hash(assCod, relCodice, relassAnno);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxRelazioneass {\n");
    
    sb.append("    assCod: ").append(toIndentedString(assCod)).append("\n");
    sb.append("    relCodice: ").append(toIndentedString(relCodice)).append("\n");
    sb.append("    relassAnno: ").append(toIndentedString(relassAnno)).append("\n");
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


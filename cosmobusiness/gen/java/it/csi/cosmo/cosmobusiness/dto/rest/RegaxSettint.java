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
public class RegaxSettint  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long setintCodice = null;

  private String setintSettoreintervento = null;

  private Long setintPredefinito = null;

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
  
  @JsonProperty("setint_settoreintervento")
  public String getSetintSettoreintervento() {
    return setintSettoreintervento;
  }
  public void setSetintSettoreintervento(String setintSettoreintervento) {
    this.setintSettoreintervento = setintSettoreintervento;
  }

  /**
   **/
  
  @JsonProperty("setint_predefinito")
  public Long getSetintPredefinito() {
    return setintPredefinito;
  }
  public void setSetintPredefinito(Long setintPredefinito) {
    this.setintPredefinito = setintPredefinito;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxSettint regaxSettint = (RegaxSettint) o;
    return Objects.equals(setintCodice, regaxSettint.setintCodice) &&
        Objects.equals(setintSettoreintervento, regaxSettint.setintSettoreintervento) &&
        Objects.equals(setintPredefinito, regaxSettint.setintPredefinito);
  }

  @Override
  public int hashCode() {
    return Objects.hash(setintCodice, setintSettoreintervento, setintPredefinito);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxSettint {\n");
    
    sb.append("    setintCodice: ").append(toIndentedString(setintCodice)).append("\n");
    sb.append("    setintSettoreintervento: ").append(toIndentedString(setintSettoreintervento)).append("\n");
    sb.append("    setintPredefinito: ").append(toIndentedString(setintPredefinito)).append("\n");
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


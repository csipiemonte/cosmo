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
public class RegaxTiporuolo  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long ruoloCodice = null;

  private String ruoloDescrizione = null;

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
  
  @JsonProperty("ruolo_descrizione")
  public String getRuoloDescrizione() {
    return ruoloDescrizione;
  }
  public void setRuoloDescrizione(String ruoloDescrizione) {
    this.ruoloDescrizione = ruoloDescrizione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxTiporuolo regaxTiporuolo = (RegaxTiporuolo) o;
    return Objects.equals(ruoloCodice, regaxTiporuolo.ruoloCodice) &&
        Objects.equals(ruoloDescrizione, regaxTiporuolo.ruoloDescrizione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ruoloCodice, ruoloDescrizione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxTiporuolo {\n");
    
    sb.append("    ruoloCodice: ").append(toIndentedString(ruoloCodice)).append("\n");
    sb.append("    ruoloDescrizione: ").append(toIndentedString(ruoloDescrizione)).append("\n");
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


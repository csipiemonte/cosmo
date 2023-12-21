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
public class RegaxNaturagiuridica  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long natgiuCodice = null;

  private String natgiuDescrizione = null;

  /**
   **/
  
  @JsonProperty("natgiu_codice")
  public Long getNatgiuCodice() {
    return natgiuCodice;
  }
  public void setNatgiuCodice(Long natgiuCodice) {
    this.natgiuCodice = natgiuCodice;
  }

  /**
   **/
  
  @JsonProperty("natgiu_descrizione")
  public String getNatgiuDescrizione() {
    return natgiuDescrizione;
  }
  public void setNatgiuDescrizione(String natgiuDescrizione) {
    this.natgiuDescrizione = natgiuDescrizione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxNaturagiuridica regaxNaturagiuridica = (RegaxNaturagiuridica) o;
    return Objects.equals(natgiuCodice, regaxNaturagiuridica.natgiuCodice) &&
        Objects.equals(natgiuDescrizione, regaxNaturagiuridica.natgiuDescrizione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(natgiuCodice, natgiuDescrizione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxNaturagiuridica {\n");
    
    sb.append("    natgiuCodice: ").append(toIndentedString(natgiuCodice)).append("\n");
    sb.append("    natgiuDescrizione: ").append(toIndentedString(natgiuDescrizione)).append("\n");
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


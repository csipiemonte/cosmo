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
public class RegaxRelazioni  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long relCodice = null;

  private String relDescizione = null;

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
  
  @JsonProperty("rel_descizione")
  public String getRelDescizione() {
    return relDescizione;
  }
  public void setRelDescizione(String relDescizione) {
    this.relDescizione = relDescizione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxRelazioni regaxRelazioni = (RegaxRelazioni) o;
    return Objects.equals(relCodice, regaxRelazioni.relCodice) &&
        Objects.equals(relDescizione, regaxRelazioni.relDescizione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(relCodice, relDescizione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxRelazioni {\n");
    
    sb.append("    relCodice: ").append(toIndentedString(relCodice)).append("\n");
    sb.append("    relDescizione: ").append(toIndentedString(relDescizione)).append("\n");
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


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
public class RegaxLivelloassociazione  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long livelloassCodice = null;

  private String livelloassLivello = null;

  /**
   **/
  
  @JsonProperty("livelloass_codice")
  public Long getLivelloassCodice() {
    return livelloassCodice;
  }
  public void setLivelloassCodice(Long livelloassCodice) {
    this.livelloassCodice = livelloassCodice;
  }

  /**
   **/
  
  @JsonProperty("livelloass_livello")
  public String getLivelloassLivello() {
    return livelloassLivello;
  }
  public void setLivelloassLivello(String livelloassLivello) {
    this.livelloassLivello = livelloassLivello;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxLivelloassociazione regaxLivelloassociazione = (RegaxLivelloassociazione) o;
    return Objects.equals(livelloassCodice, regaxLivelloassociazione.livelloassCodice) &&
        Objects.equals(livelloassLivello, regaxLivelloassociazione.livelloassLivello);
  }

  @Override
  public int hashCode() {
    return Objects.hash(livelloassCodice, livelloassLivello);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxLivelloassociazione {\n");
    
    sb.append("    livelloassCodice: ").append(toIndentedString(livelloassCodice)).append("\n");
    sb.append("    livelloassLivello: ").append(toIndentedString(livelloassLivello)).append("\n");
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


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
public class RegaxStatoassociazione  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long statoassCodice = null;

  private String statoassDescrizione = null;

  /**
   **/
  
  @JsonProperty("statoass_codice")
  public Long getStatoassCodice() {
    return statoassCodice;
  }
  public void setStatoassCodice(Long statoassCodice) {
    this.statoassCodice = statoassCodice;
  }

  /**
   **/
  
  @JsonProperty("statoass_descrizione")
  public String getStatoassDescrizione() {
    return statoassDescrizione;
  }
  public void setStatoassDescrizione(String statoassDescrizione) {
    this.statoassDescrizione = statoassDescrizione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxStatoassociazione regaxStatoassociazione = (RegaxStatoassociazione) o;
    return Objects.equals(statoassCodice, regaxStatoassociazione.statoassCodice) &&
        Objects.equals(statoassDescrizione, regaxStatoassociazione.statoassDescrizione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statoassCodice, statoassDescrizione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxStatoassociazione {\n");
    
    sb.append("    statoassCodice: ").append(toIndentedString(statoassCodice)).append("\n");
    sb.append("    statoassDescrizione: ").append(toIndentedString(statoassDescrizione)).append("\n");
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


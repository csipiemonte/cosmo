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
public class RegaxAllegati  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long allegatiCodice = null;

  private String allegatiDescrizione = null;

  private Long allegatiPredefinito = null;

  /**
   **/
  
  @JsonProperty("allegati_codice")
  public Long getAllegatiCodice() {
    return allegatiCodice;
  }
  public void setAllegatiCodice(Long allegatiCodice) {
    this.allegatiCodice = allegatiCodice;
  }

  /**
   **/
  
  @JsonProperty("allegati_descrizione")
  public String getAllegatiDescrizione() {
    return allegatiDescrizione;
  }
  public void setAllegatiDescrizione(String allegatiDescrizione) {
    this.allegatiDescrizione = allegatiDescrizione;
  }

  /**
   **/
  
  @JsonProperty("allegati_predefinito")
  public Long getAllegatiPredefinito() {
    return allegatiPredefinito;
  }
  public void setAllegatiPredefinito(Long allegatiPredefinito) {
    this.allegatiPredefinito = allegatiPredefinito;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxAllegati regaxAllegati = (RegaxAllegati) o;
    return Objects.equals(allegatiCodice, regaxAllegati.allegatiCodice) &&
        Objects.equals(allegatiDescrizione, regaxAllegati.allegatiDescrizione) &&
        Objects.equals(allegatiPredefinito, regaxAllegati.allegatiPredefinito);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allegatiCodice, allegatiDescrizione, allegatiPredefinito);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxAllegati {\n");
    
    sb.append("    allegatiCodice: ").append(toIndentedString(allegatiCodice)).append("\n");
    sb.append("    allegatiDescrizione: ").append(toIndentedString(allegatiDescrizione)).append("\n");
    sb.append("    allegatiPredefinito: ").append(toIndentedString(allegatiPredefinito)).append("\n");
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


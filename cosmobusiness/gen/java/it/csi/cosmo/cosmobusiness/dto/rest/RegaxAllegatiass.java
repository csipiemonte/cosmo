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
public class RegaxAllegatiass  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long id = null;

  private Long assCod = null;

  private Long allegatiCodice = null;

  private String allegatiassData = null;

  private String allegatiDescrizione = null;

  private String dup = null;

  /**
   **/
  
  @JsonProperty("id")
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
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
  
  @JsonProperty("allegatiass_data")
  public String getAllegatiassData() {
    return allegatiassData;
  }
  public void setAllegatiassData(String allegatiassData) {
    this.allegatiassData = allegatiassData;
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
  
  @JsonProperty("dup")
  public String getDup() {
    return dup;
  }
  public void setDup(String dup) {
    this.dup = dup;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxAllegatiass regaxAllegatiass = (RegaxAllegatiass) o;
    return Objects.equals(id, regaxAllegatiass.id) &&
        Objects.equals(assCod, regaxAllegatiass.assCod) &&
        Objects.equals(allegatiCodice, regaxAllegatiass.allegatiCodice) &&
        Objects.equals(allegatiassData, regaxAllegatiass.allegatiassData) &&
        Objects.equals(allegatiDescrizione, regaxAllegatiass.allegatiDescrizione) &&
        Objects.equals(dup, regaxAllegatiass.dup);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, assCod, allegatiCodice, allegatiassData, allegatiDescrizione, dup);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxAllegatiass {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    assCod: ").append(toIndentedString(assCod)).append("\n");
    sb.append("    allegatiCodice: ").append(toIndentedString(allegatiCodice)).append("\n");
    sb.append("    allegatiassData: ").append(toIndentedString(allegatiassData)).append("\n");
    sb.append("    allegatiDescrizione: ").append(toIndentedString(allegatiDescrizione)).append("\n");
    sb.append("    dup: ").append(toIndentedString(dup)).append("\n");
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


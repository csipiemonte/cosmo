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
public class RegaxUffici  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long ufficioCod = null;

  private String ufficioIdentific = null;

  private String ufficioDescrizione = null;

  private String ufficioIndirizzo = null;

  private String ufficioNote = null;

  /**
   **/
  
  @JsonProperty("ufficio_cod")
  public Long getUfficioCod() {
    return ufficioCod;
  }
  public void setUfficioCod(Long ufficioCod) {
    this.ufficioCod = ufficioCod;
  }

  /**
   **/
  
  @JsonProperty("ufficio_identific")
  public String getUfficioIdentific() {
    return ufficioIdentific;
  }
  public void setUfficioIdentific(String ufficioIdentific) {
    this.ufficioIdentific = ufficioIdentific;
  }

  /**
   **/
  
  @JsonProperty("ufficio_descrizione")
  public String getUfficioDescrizione() {
    return ufficioDescrizione;
  }
  public void setUfficioDescrizione(String ufficioDescrizione) {
    this.ufficioDescrizione = ufficioDescrizione;
  }

  /**
   **/
  
  @JsonProperty("ufficio_indirizzo")
  public String getUfficioIndirizzo() {
    return ufficioIndirizzo;
  }
  public void setUfficioIndirizzo(String ufficioIndirizzo) {
    this.ufficioIndirizzo = ufficioIndirizzo;
  }

  /**
   **/
  
  @JsonProperty("ufficio_note")
  public String getUfficioNote() {
    return ufficioNote;
  }
  public void setUfficioNote(String ufficioNote) {
    this.ufficioNote = ufficioNote;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxUffici regaxUffici = (RegaxUffici) o;
    return Objects.equals(ufficioCod, regaxUffici.ufficioCod) &&
        Objects.equals(ufficioIdentific, regaxUffici.ufficioIdentific) &&
        Objects.equals(ufficioDescrizione, regaxUffici.ufficioDescrizione) &&
        Objects.equals(ufficioIndirizzo, regaxUffici.ufficioIndirizzo) &&
        Objects.equals(ufficioNote, regaxUffici.ufficioNote);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ufficioCod, ufficioIdentific, ufficioDescrizione, ufficioIndirizzo, ufficioNote);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxUffici {\n");
    
    sb.append("    ufficioCod: ").append(toIndentedString(ufficioCod)).append("\n");
    sb.append("    ufficioIdentific: ").append(toIndentedString(ufficioIdentific)).append("\n");
    sb.append("    ufficioDescrizione: ").append(toIndentedString(ufficioDescrizione)).append("\n");
    sb.append("    ufficioIndirizzo: ").append(toIndentedString(ufficioIndirizzo)).append("\n");
    sb.append("    ufficioNote: ").append(toIndentedString(ufficioNote)).append("\n");
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


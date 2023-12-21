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
public class RegaxSedelegaleassociazione  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long assCod = null;

  private String sedelegalePresso = null;

  private String sedelegaleIndirizzo = null;

  private String sedelegaleCap = null;

  private String sedelegaleComune = null;

  private String sedelegaleProvincia = null;

  private String sedelegaleNumfax = null;

  private String sedelegaleNumtel1 = null;

  private String sedelegaleNumtel2 = null;

  private String sedelegaleData = null;

  private String sedelegaleEmail1 = null;

  private String sedelegaleSitoweb1 = null;

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
  
  @JsonProperty("sedelegale_presso")
  public String getSedelegalePresso() {
    return sedelegalePresso;
  }
  public void setSedelegalePresso(String sedelegalePresso) {
    this.sedelegalePresso = sedelegalePresso;
  }

  /**
   **/
  
  @JsonProperty("sedelegale_indirizzo")
  public String getSedelegaleIndirizzo() {
    return sedelegaleIndirizzo;
  }
  public void setSedelegaleIndirizzo(String sedelegaleIndirizzo) {
    this.sedelegaleIndirizzo = sedelegaleIndirizzo;
  }

  /**
   **/
  
  @JsonProperty("sedelegale_cap")
  public String getSedelegaleCap() {
    return sedelegaleCap;
  }
  public void setSedelegaleCap(String sedelegaleCap) {
    this.sedelegaleCap = sedelegaleCap;
  }

  /**
   **/
  
  @JsonProperty("sedelegale_comune")
  public String getSedelegaleComune() {
    return sedelegaleComune;
  }
  public void setSedelegaleComune(String sedelegaleComune) {
    this.sedelegaleComune = sedelegaleComune;
  }

  /**
   **/
  
  @JsonProperty("sedelegale_provincia")
  public String getSedelegaleProvincia() {
    return sedelegaleProvincia;
  }
  public void setSedelegaleProvincia(String sedelegaleProvincia) {
    this.sedelegaleProvincia = sedelegaleProvincia;
  }

  /**
   **/
  
  @JsonProperty("sedelegale_numfax")
  public String getSedelegaleNumfax() {
    return sedelegaleNumfax;
  }
  public void setSedelegaleNumfax(String sedelegaleNumfax) {
    this.sedelegaleNumfax = sedelegaleNumfax;
  }

  /**
   **/
  
  @JsonProperty("sedelegale_numtel1")
  public String getSedelegaleNumtel1() {
    return sedelegaleNumtel1;
  }
  public void setSedelegaleNumtel1(String sedelegaleNumtel1) {
    this.sedelegaleNumtel1 = sedelegaleNumtel1;
  }

  /**
   **/
  
  @JsonProperty("sedelegale_numtel2")
  public String getSedelegaleNumtel2() {
    return sedelegaleNumtel2;
  }
  public void setSedelegaleNumtel2(String sedelegaleNumtel2) {
    this.sedelegaleNumtel2 = sedelegaleNumtel2;
  }

  /**
   **/
  
  @JsonProperty("sedelegale_data")
  public String getSedelegaleData() {
    return sedelegaleData;
  }
  public void setSedelegaleData(String sedelegaleData) {
    this.sedelegaleData = sedelegaleData;
  }

  /**
   **/
  
  @JsonProperty("sedelegale_email1")
  public String getSedelegaleEmail1() {
    return sedelegaleEmail1;
  }
  public void setSedelegaleEmail1(String sedelegaleEmail1) {
    this.sedelegaleEmail1 = sedelegaleEmail1;
  }

  /**
   **/
  
  @JsonProperty("sedelegale_sitoweb1")
  public String getSedelegaleSitoweb1() {
    return sedelegaleSitoweb1;
  }
  public void setSedelegaleSitoweb1(String sedelegaleSitoweb1) {
    this.sedelegaleSitoweb1 = sedelegaleSitoweb1;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxSedelegaleassociazione regaxSedelegaleassociazione = (RegaxSedelegaleassociazione) o;
    return Objects.equals(assCod, regaxSedelegaleassociazione.assCod) &&
        Objects.equals(sedelegalePresso, regaxSedelegaleassociazione.sedelegalePresso) &&
        Objects.equals(sedelegaleIndirizzo, regaxSedelegaleassociazione.sedelegaleIndirizzo) &&
        Objects.equals(sedelegaleCap, regaxSedelegaleassociazione.sedelegaleCap) &&
        Objects.equals(sedelegaleComune, regaxSedelegaleassociazione.sedelegaleComune) &&
        Objects.equals(sedelegaleProvincia, regaxSedelegaleassociazione.sedelegaleProvincia) &&
        Objects.equals(sedelegaleNumfax, regaxSedelegaleassociazione.sedelegaleNumfax) &&
        Objects.equals(sedelegaleNumtel1, regaxSedelegaleassociazione.sedelegaleNumtel1) &&
        Objects.equals(sedelegaleNumtel2, regaxSedelegaleassociazione.sedelegaleNumtel2) &&
        Objects.equals(sedelegaleData, regaxSedelegaleassociazione.sedelegaleData) &&
        Objects.equals(sedelegaleEmail1, regaxSedelegaleassociazione.sedelegaleEmail1) &&
        Objects.equals(sedelegaleSitoweb1, regaxSedelegaleassociazione.sedelegaleSitoweb1);
  }

  @Override
  public int hashCode() {
    return Objects.hash(assCod, sedelegalePresso, sedelegaleIndirizzo, sedelegaleCap, sedelegaleComune, sedelegaleProvincia, sedelegaleNumfax, sedelegaleNumtel1, sedelegaleNumtel2, sedelegaleData, sedelegaleEmail1, sedelegaleSitoweb1);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxSedelegaleassociazione {\n");
    
    sb.append("    assCod: ").append(toIndentedString(assCod)).append("\n");
    sb.append("    sedelegalePresso: ").append(toIndentedString(sedelegalePresso)).append("\n");
    sb.append("    sedelegaleIndirizzo: ").append(toIndentedString(sedelegaleIndirizzo)).append("\n");
    sb.append("    sedelegaleCap: ").append(toIndentedString(sedelegaleCap)).append("\n");
    sb.append("    sedelegaleComune: ").append(toIndentedString(sedelegaleComune)).append("\n");
    sb.append("    sedelegaleProvincia: ").append(toIndentedString(sedelegaleProvincia)).append("\n");
    sb.append("    sedelegaleNumfax: ").append(toIndentedString(sedelegaleNumfax)).append("\n");
    sb.append("    sedelegaleNumtel1: ").append(toIndentedString(sedelegaleNumtel1)).append("\n");
    sb.append("    sedelegaleNumtel2: ").append(toIndentedString(sedelegaleNumtel2)).append("\n");
    sb.append("    sedelegaleData: ").append(toIndentedString(sedelegaleData)).append("\n");
    sb.append("    sedelegaleEmail1: ").append(toIndentedString(sedelegaleEmail1)).append("\n");
    sb.append("    sedelegaleSitoweb1: ").append(toIndentedString(sedelegaleSitoweb1)).append("\n");
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


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
public class RegaxSedeoperativa  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long assCod = null;

  private String sedeoperativaPresso = null;

  private Long sedeoperativaSedime = null;

  private String sedeoperativaIndirizzo = null;

  private Long sedeoperativaNumcivico = null;

  private Long sedeoperativaBis = null;

  private String sedeoperativaInterno = null;

  private String sedeoperativaLettera = null;

  private String sedeoperativaCap = null;

  private Long sedeoperativaCircoscriz = null;

  private String sedeoperativaComune = null;

  private String sedeoperativaProvincia = null;

  private String sedeoperativaNumfax = null;

  private String sedeoperativaNumtel1 = null;

  private String sedeoperativaNumtel2 = null;

  private String sedeoperativaEmail1 = null;

  private String sedeoperativaSitoweb1 = null;

  private String sedeoperativaData = null;

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
  
  @JsonProperty("sedeoperativa_presso")
  public String getSedeoperativaPresso() {
    return sedeoperativaPresso;
  }
  public void setSedeoperativaPresso(String sedeoperativaPresso) {
    this.sedeoperativaPresso = sedeoperativaPresso;
  }

  /**
   **/
  
  @JsonProperty("sedeoperativa_sedime")
  public Long getSedeoperativaSedime() {
    return sedeoperativaSedime;
  }
  public void setSedeoperativaSedime(Long sedeoperativaSedime) {
    this.sedeoperativaSedime = sedeoperativaSedime;
  }

  /**
   **/
  
  @JsonProperty("sedeoperativa_indirizzo")
  public String getSedeoperativaIndirizzo() {
    return sedeoperativaIndirizzo;
  }
  public void setSedeoperativaIndirizzo(String sedeoperativaIndirizzo) {
    this.sedeoperativaIndirizzo = sedeoperativaIndirizzo;
  }

  /**
   **/
  
  @JsonProperty("sedeoperativa_numcivico")
  public Long getSedeoperativaNumcivico() {
    return sedeoperativaNumcivico;
  }
  public void setSedeoperativaNumcivico(Long sedeoperativaNumcivico) {
    this.sedeoperativaNumcivico = sedeoperativaNumcivico;
  }

  /**
   **/
  
  @JsonProperty("sedeoperativa_bis")
  public Long getSedeoperativaBis() {
    return sedeoperativaBis;
  }
  public void setSedeoperativaBis(Long sedeoperativaBis) {
    this.sedeoperativaBis = sedeoperativaBis;
  }

  /**
   **/
  
  @JsonProperty("sedeoperativa_interno")
  public String getSedeoperativaInterno() {
    return sedeoperativaInterno;
  }
  public void setSedeoperativaInterno(String sedeoperativaInterno) {
    this.sedeoperativaInterno = sedeoperativaInterno;
  }

  /**
   **/
  
  @JsonProperty("sedeoperativa_lettera")
  public String getSedeoperativaLettera() {
    return sedeoperativaLettera;
  }
  public void setSedeoperativaLettera(String sedeoperativaLettera) {
    this.sedeoperativaLettera = sedeoperativaLettera;
  }

  /**
   **/
  
  @JsonProperty("sedeoperativa_cap")
  public String getSedeoperativaCap() {
    return sedeoperativaCap;
  }
  public void setSedeoperativaCap(String sedeoperativaCap) {
    this.sedeoperativaCap = sedeoperativaCap;
  }

  /**
   **/
  
  @JsonProperty("sedeoperativa_circoscriz")
  public Long getSedeoperativaCircoscriz() {
    return sedeoperativaCircoscriz;
  }
  public void setSedeoperativaCircoscriz(Long sedeoperativaCircoscriz) {
    this.sedeoperativaCircoscriz = sedeoperativaCircoscriz;
  }

  /**
   **/
  
  @JsonProperty("sedeoperativa_comune")
  public String getSedeoperativaComune() {
    return sedeoperativaComune;
  }
  public void setSedeoperativaComune(String sedeoperativaComune) {
    this.sedeoperativaComune = sedeoperativaComune;
  }

  /**
   **/
  
  @JsonProperty("sedeoperativa_provincia")
  public String getSedeoperativaProvincia() {
    return sedeoperativaProvincia;
  }
  public void setSedeoperativaProvincia(String sedeoperativaProvincia) {
    this.sedeoperativaProvincia = sedeoperativaProvincia;
  }

  /**
   **/
  
  @JsonProperty("sedeoperativa_numfax")
  public String getSedeoperativaNumfax() {
    return sedeoperativaNumfax;
  }
  public void setSedeoperativaNumfax(String sedeoperativaNumfax) {
    this.sedeoperativaNumfax = sedeoperativaNumfax;
  }

  /**
   **/
  
  @JsonProperty("sedeoperativa_numtel1")
  public String getSedeoperativaNumtel1() {
    return sedeoperativaNumtel1;
  }
  public void setSedeoperativaNumtel1(String sedeoperativaNumtel1) {
    this.sedeoperativaNumtel1 = sedeoperativaNumtel1;
  }

  /**
   **/
  
  @JsonProperty("sedeoperativa_numtel2")
  public String getSedeoperativaNumtel2() {
    return sedeoperativaNumtel2;
  }
  public void setSedeoperativaNumtel2(String sedeoperativaNumtel2) {
    this.sedeoperativaNumtel2 = sedeoperativaNumtel2;
  }

  /**
   **/
  
  @JsonProperty("sedeoperativa_email1")
  public String getSedeoperativaEmail1() {
    return sedeoperativaEmail1;
  }
  public void setSedeoperativaEmail1(String sedeoperativaEmail1) {
    this.sedeoperativaEmail1 = sedeoperativaEmail1;
  }

  /**
   **/
  
  @JsonProperty("sedeoperativa_sitoweb1")
  public String getSedeoperativaSitoweb1() {
    return sedeoperativaSitoweb1;
  }
  public void setSedeoperativaSitoweb1(String sedeoperativaSitoweb1) {
    this.sedeoperativaSitoweb1 = sedeoperativaSitoweb1;
  }

  /**
   **/
  
  @JsonProperty("sedeoperativa_data")
  public String getSedeoperativaData() {
    return sedeoperativaData;
  }
  public void setSedeoperativaData(String sedeoperativaData) {
    this.sedeoperativaData = sedeoperativaData;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxSedeoperativa regaxSedeoperativa = (RegaxSedeoperativa) o;
    return Objects.equals(assCod, regaxSedeoperativa.assCod) &&
        Objects.equals(sedeoperativaPresso, regaxSedeoperativa.sedeoperativaPresso) &&
        Objects.equals(sedeoperativaSedime, regaxSedeoperativa.sedeoperativaSedime) &&
        Objects.equals(sedeoperativaIndirizzo, regaxSedeoperativa.sedeoperativaIndirizzo) &&
        Objects.equals(sedeoperativaNumcivico, regaxSedeoperativa.sedeoperativaNumcivico) &&
        Objects.equals(sedeoperativaBis, regaxSedeoperativa.sedeoperativaBis) &&
        Objects.equals(sedeoperativaInterno, regaxSedeoperativa.sedeoperativaInterno) &&
        Objects.equals(sedeoperativaLettera, regaxSedeoperativa.sedeoperativaLettera) &&
        Objects.equals(sedeoperativaCap, regaxSedeoperativa.sedeoperativaCap) &&
        Objects.equals(sedeoperativaCircoscriz, regaxSedeoperativa.sedeoperativaCircoscriz) &&
        Objects.equals(sedeoperativaComune, regaxSedeoperativa.sedeoperativaComune) &&
        Objects.equals(sedeoperativaProvincia, regaxSedeoperativa.sedeoperativaProvincia) &&
        Objects.equals(sedeoperativaNumfax, regaxSedeoperativa.sedeoperativaNumfax) &&
        Objects.equals(sedeoperativaNumtel1, regaxSedeoperativa.sedeoperativaNumtel1) &&
        Objects.equals(sedeoperativaNumtel2, regaxSedeoperativa.sedeoperativaNumtel2) &&
        Objects.equals(sedeoperativaEmail1, regaxSedeoperativa.sedeoperativaEmail1) &&
        Objects.equals(sedeoperativaSitoweb1, regaxSedeoperativa.sedeoperativaSitoweb1) &&
        Objects.equals(sedeoperativaData, regaxSedeoperativa.sedeoperativaData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(assCod, sedeoperativaPresso, sedeoperativaSedime, sedeoperativaIndirizzo, sedeoperativaNumcivico, sedeoperativaBis, sedeoperativaInterno, sedeoperativaLettera, sedeoperativaCap, sedeoperativaCircoscriz, sedeoperativaComune, sedeoperativaProvincia, sedeoperativaNumfax, sedeoperativaNumtel1, sedeoperativaNumtel2, sedeoperativaEmail1, sedeoperativaSitoweb1, sedeoperativaData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxSedeoperativa {\n");
    
    sb.append("    assCod: ").append(toIndentedString(assCod)).append("\n");
    sb.append("    sedeoperativaPresso: ").append(toIndentedString(sedeoperativaPresso)).append("\n");
    sb.append("    sedeoperativaSedime: ").append(toIndentedString(sedeoperativaSedime)).append("\n");
    sb.append("    sedeoperativaIndirizzo: ").append(toIndentedString(sedeoperativaIndirizzo)).append("\n");
    sb.append("    sedeoperativaNumcivico: ").append(toIndentedString(sedeoperativaNumcivico)).append("\n");
    sb.append("    sedeoperativaBis: ").append(toIndentedString(sedeoperativaBis)).append("\n");
    sb.append("    sedeoperativaInterno: ").append(toIndentedString(sedeoperativaInterno)).append("\n");
    sb.append("    sedeoperativaLettera: ").append(toIndentedString(sedeoperativaLettera)).append("\n");
    sb.append("    sedeoperativaCap: ").append(toIndentedString(sedeoperativaCap)).append("\n");
    sb.append("    sedeoperativaCircoscriz: ").append(toIndentedString(sedeoperativaCircoscriz)).append("\n");
    sb.append("    sedeoperativaComune: ").append(toIndentedString(sedeoperativaComune)).append("\n");
    sb.append("    sedeoperativaProvincia: ").append(toIndentedString(sedeoperativaProvincia)).append("\n");
    sb.append("    sedeoperativaNumfax: ").append(toIndentedString(sedeoperativaNumfax)).append("\n");
    sb.append("    sedeoperativaNumtel1: ").append(toIndentedString(sedeoperativaNumtel1)).append("\n");
    sb.append("    sedeoperativaNumtel2: ").append(toIndentedString(sedeoperativaNumtel2)).append("\n");
    sb.append("    sedeoperativaEmail1: ").append(toIndentedString(sedeoperativaEmail1)).append("\n");
    sb.append("    sedeoperativaSitoweb1: ").append(toIndentedString(sedeoperativaSitoweb1)).append("\n");
    sb.append("    sedeoperativaData: ").append(toIndentedString(sedeoperativaData)).append("\n");
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


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
public class RegaxSezionelocaleassociazione  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long assCod = null;

  private String sezlocalePresso = null;

  private Long sezlocaleSedime = null;

  private String sezlocaleIndirizzo = null;

  private Long sezlocaleNumcivico = null;

  private Long sezlocaleBis = null;

  private Long sezlocaleInterno = null;

  private String sezlocaleLettera = null;

  private String sezlocaleCap = null;

  private Long sezlocaleCircoscriz = null;

  private String sezlocaleComune = null;

  private String sezlocaleProvincia = null;

  private String sezlocaleNumfax = null;

  private String sezlocaleNumtel1 = null;

  private String sezlocaleNumtel2 = null;

  private String sezlocaleData = null;

  private String sezlocaleEmail2 = null;

  private String sezlocaleSitoweb2 = null;

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
  
  @JsonProperty("sezlocale_presso")
  public String getSezlocalePresso() {
    return sezlocalePresso;
  }
  public void setSezlocalePresso(String sezlocalePresso) {
    this.sezlocalePresso = sezlocalePresso;
  }

  /**
   **/
  
  @JsonProperty("sezlocale_sedime")
  public Long getSezlocaleSedime() {
    return sezlocaleSedime;
  }
  public void setSezlocaleSedime(Long sezlocaleSedime) {
    this.sezlocaleSedime = sezlocaleSedime;
  }

  /**
   **/
  
  @JsonProperty("sezlocale_indirizzo")
  public String getSezlocaleIndirizzo() {
    return sezlocaleIndirizzo;
  }
  public void setSezlocaleIndirizzo(String sezlocaleIndirizzo) {
    this.sezlocaleIndirizzo = sezlocaleIndirizzo;
  }

  /**
   **/
  
  @JsonProperty("sezlocale_numcivico")
  public Long getSezlocaleNumcivico() {
    return sezlocaleNumcivico;
  }
  public void setSezlocaleNumcivico(Long sezlocaleNumcivico) {
    this.sezlocaleNumcivico = sezlocaleNumcivico;
  }

  /**
   **/
  
  @JsonProperty("sezlocale_bis")
  public Long getSezlocaleBis() {
    return sezlocaleBis;
  }
  public void setSezlocaleBis(Long sezlocaleBis) {
    this.sezlocaleBis = sezlocaleBis;
  }

  /**
   **/
  
  @JsonProperty("sezlocale_interno")
  public Long getSezlocaleInterno() {
    return sezlocaleInterno;
  }
  public void setSezlocaleInterno(Long sezlocaleInterno) {
    this.sezlocaleInterno = sezlocaleInterno;
  }

  /**
   **/
  
  @JsonProperty("sezlocale_lettera")
  public String getSezlocaleLettera() {
    return sezlocaleLettera;
  }
  public void setSezlocaleLettera(String sezlocaleLettera) {
    this.sezlocaleLettera = sezlocaleLettera;
  }

  /**
   **/
  
  @JsonProperty("sezlocale_cap")
  public String getSezlocaleCap() {
    return sezlocaleCap;
  }
  public void setSezlocaleCap(String sezlocaleCap) {
    this.sezlocaleCap = sezlocaleCap;
  }

  /**
   **/
  
  @JsonProperty("sezlocale_circoscriz")
  public Long getSezlocaleCircoscriz() {
    return sezlocaleCircoscriz;
  }
  public void setSezlocaleCircoscriz(Long sezlocaleCircoscriz) {
    this.sezlocaleCircoscriz = sezlocaleCircoscriz;
  }

  /**
   **/
  
  @JsonProperty("sezlocale_comune")
  public String getSezlocaleComune() {
    return sezlocaleComune;
  }
  public void setSezlocaleComune(String sezlocaleComune) {
    this.sezlocaleComune = sezlocaleComune;
  }

  /**
   **/
  
  @JsonProperty("sezlocale_provincia")
  public String getSezlocaleProvincia() {
    return sezlocaleProvincia;
  }
  public void setSezlocaleProvincia(String sezlocaleProvincia) {
    this.sezlocaleProvincia = sezlocaleProvincia;
  }

  /**
   **/
  
  @JsonProperty("sezlocale_numfax")
  public String getSezlocaleNumfax() {
    return sezlocaleNumfax;
  }
  public void setSezlocaleNumfax(String sezlocaleNumfax) {
    this.sezlocaleNumfax = sezlocaleNumfax;
  }

  /**
   **/
  
  @JsonProperty("sezlocale_numtel1")
  public String getSezlocaleNumtel1() {
    return sezlocaleNumtel1;
  }
  public void setSezlocaleNumtel1(String sezlocaleNumtel1) {
    this.sezlocaleNumtel1 = sezlocaleNumtel1;
  }

  /**
   **/
  
  @JsonProperty("sezlocale_numtel2")
  public String getSezlocaleNumtel2() {
    return sezlocaleNumtel2;
  }
  public void setSezlocaleNumtel2(String sezlocaleNumtel2) {
    this.sezlocaleNumtel2 = sezlocaleNumtel2;
  }

  /**
   **/
  
  @JsonProperty("sezlocale_data")
  public String getSezlocaleData() {
    return sezlocaleData;
  }
  public void setSezlocaleData(String sezlocaleData) {
    this.sezlocaleData = sezlocaleData;
  }

  /**
   **/
  
  @JsonProperty("sezlocale_email2")
  public String getSezlocaleEmail2() {
    return sezlocaleEmail2;
  }
  public void setSezlocaleEmail2(String sezlocaleEmail2) {
    this.sezlocaleEmail2 = sezlocaleEmail2;
  }

  /**
   **/
  
  @JsonProperty("sezlocale_sitoweb2")
  public String getSezlocaleSitoweb2() {
    return sezlocaleSitoweb2;
  }
  public void setSezlocaleSitoweb2(String sezlocaleSitoweb2) {
    this.sezlocaleSitoweb2 = sezlocaleSitoweb2;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxSezionelocaleassociazione regaxSezionelocaleassociazione = (RegaxSezionelocaleassociazione) o;
    return Objects.equals(assCod, regaxSezionelocaleassociazione.assCod) &&
        Objects.equals(sezlocalePresso, regaxSezionelocaleassociazione.sezlocalePresso) &&
        Objects.equals(sezlocaleSedime, regaxSezionelocaleassociazione.sezlocaleSedime) &&
        Objects.equals(sezlocaleIndirizzo, regaxSezionelocaleassociazione.sezlocaleIndirizzo) &&
        Objects.equals(sezlocaleNumcivico, regaxSezionelocaleassociazione.sezlocaleNumcivico) &&
        Objects.equals(sezlocaleBis, regaxSezionelocaleassociazione.sezlocaleBis) &&
        Objects.equals(sezlocaleInterno, regaxSezionelocaleassociazione.sezlocaleInterno) &&
        Objects.equals(sezlocaleLettera, regaxSezionelocaleassociazione.sezlocaleLettera) &&
        Objects.equals(sezlocaleCap, regaxSezionelocaleassociazione.sezlocaleCap) &&
        Objects.equals(sezlocaleCircoscriz, regaxSezionelocaleassociazione.sezlocaleCircoscriz) &&
        Objects.equals(sezlocaleComune, regaxSezionelocaleassociazione.sezlocaleComune) &&
        Objects.equals(sezlocaleProvincia, regaxSezionelocaleassociazione.sezlocaleProvincia) &&
        Objects.equals(sezlocaleNumfax, regaxSezionelocaleassociazione.sezlocaleNumfax) &&
        Objects.equals(sezlocaleNumtel1, regaxSezionelocaleassociazione.sezlocaleNumtel1) &&
        Objects.equals(sezlocaleNumtel2, regaxSezionelocaleassociazione.sezlocaleNumtel2) &&
        Objects.equals(sezlocaleData, regaxSezionelocaleassociazione.sezlocaleData) &&
        Objects.equals(sezlocaleEmail2, regaxSezionelocaleassociazione.sezlocaleEmail2) &&
        Objects.equals(sezlocaleSitoweb2, regaxSezionelocaleassociazione.sezlocaleSitoweb2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(assCod, sezlocalePresso, sezlocaleSedime, sezlocaleIndirizzo, sezlocaleNumcivico, sezlocaleBis, sezlocaleInterno, sezlocaleLettera, sezlocaleCap, sezlocaleCircoscriz, sezlocaleComune, sezlocaleProvincia, sezlocaleNumfax, sezlocaleNumtel1, sezlocaleNumtel2, sezlocaleData, sezlocaleEmail2, sezlocaleSitoweb2);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxSezionelocaleassociazione {\n");
    
    sb.append("    assCod: ").append(toIndentedString(assCod)).append("\n");
    sb.append("    sezlocalePresso: ").append(toIndentedString(sezlocalePresso)).append("\n");
    sb.append("    sezlocaleSedime: ").append(toIndentedString(sezlocaleSedime)).append("\n");
    sb.append("    sezlocaleIndirizzo: ").append(toIndentedString(sezlocaleIndirizzo)).append("\n");
    sb.append("    sezlocaleNumcivico: ").append(toIndentedString(sezlocaleNumcivico)).append("\n");
    sb.append("    sezlocaleBis: ").append(toIndentedString(sezlocaleBis)).append("\n");
    sb.append("    sezlocaleInterno: ").append(toIndentedString(sezlocaleInterno)).append("\n");
    sb.append("    sezlocaleLettera: ").append(toIndentedString(sezlocaleLettera)).append("\n");
    sb.append("    sezlocaleCap: ").append(toIndentedString(sezlocaleCap)).append("\n");
    sb.append("    sezlocaleCircoscriz: ").append(toIndentedString(sezlocaleCircoscriz)).append("\n");
    sb.append("    sezlocaleComune: ").append(toIndentedString(sezlocaleComune)).append("\n");
    sb.append("    sezlocaleProvincia: ").append(toIndentedString(sezlocaleProvincia)).append("\n");
    sb.append("    sezlocaleNumfax: ").append(toIndentedString(sezlocaleNumfax)).append("\n");
    sb.append("    sezlocaleNumtel1: ").append(toIndentedString(sezlocaleNumtel1)).append("\n");
    sb.append("    sezlocaleNumtel2: ").append(toIndentedString(sezlocaleNumtel2)).append("\n");
    sb.append("    sezlocaleData: ").append(toIndentedString(sezlocaleData)).append("\n");
    sb.append("    sezlocaleEmail2: ").append(toIndentedString(sezlocaleEmail2)).append("\n");
    sb.append("    sezlocaleSitoweb2: ").append(toIndentedString(sezlocaleSitoweb2)).append("\n");
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


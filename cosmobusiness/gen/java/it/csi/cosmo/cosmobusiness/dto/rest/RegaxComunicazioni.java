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
public class RegaxComunicazioni  implements Serializable {
  private static final long serialVersionUID = 1L;
  

  private Long assCod = null;

  private String comunicazioniPresso = null;

  private Long comunicazioniSedime = null;

  private String comunicazioniIndirizzo = null;

  private Long comunicazioniNumcivico = null;

  private Long comunicazioniBis = null;

  private Long comunicazioniInterno = null;

  private String comunicazioniLettera = null;

  private String comunicazioniCap = null;

  private String comunicazioniCircoscriz = null;

  private String comunicazioniComune = null;

  private String comunicazioniProvincia = null;

  private String comunicazioniNumfax = null;

  private String comunicazioniNumtel1 = null;

  private String comunicazioniIntstatario1 = null;

  private String comunicazioniNumtel2 = null;

  private String comunicazioniIntstatario2 = null;

  private String comunicazioniNumtel3 = null;

  private String comunicazioniData = null;

  private String comunicazioniEmail1 = null;

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
  
  @JsonProperty("comunicazioni_presso")
  public String getComunicazioniPresso() {
    return comunicazioniPresso;
  }
  public void setComunicazioniPresso(String comunicazioniPresso) {
    this.comunicazioniPresso = comunicazioniPresso;
  }

  /**
   **/
  
  @JsonProperty("comunicazioni_sedime")
  public Long getComunicazioniSedime() {
    return comunicazioniSedime;
  }
  public void setComunicazioniSedime(Long comunicazioniSedime) {
    this.comunicazioniSedime = comunicazioniSedime;
  }

  /**
   **/
  
  @JsonProperty("comunicazioni_indirizzo")
  public String getComunicazioniIndirizzo() {
    return comunicazioniIndirizzo;
  }
  public void setComunicazioniIndirizzo(String comunicazioniIndirizzo) {
    this.comunicazioniIndirizzo = comunicazioniIndirizzo;
  }

  /**
   **/
  
  @JsonProperty("comunicazioni_numcivico")
  public Long getComunicazioniNumcivico() {
    return comunicazioniNumcivico;
  }
  public void setComunicazioniNumcivico(Long comunicazioniNumcivico) {
    this.comunicazioniNumcivico = comunicazioniNumcivico;
  }

  /**
   **/
  
  @JsonProperty("comunicazioni_bis")
  public Long getComunicazioniBis() {
    return comunicazioniBis;
  }
  public void setComunicazioniBis(Long comunicazioniBis) {
    this.comunicazioniBis = comunicazioniBis;
  }

  /**
   **/
  
  @JsonProperty("comunicazioni_interno")
  public Long getComunicazioniInterno() {
    return comunicazioniInterno;
  }
  public void setComunicazioniInterno(Long comunicazioniInterno) {
    this.comunicazioniInterno = comunicazioniInterno;
  }

  /**
   **/
  
  @JsonProperty("comunicazioni_lettera")
  public String getComunicazioniLettera() {
    return comunicazioniLettera;
  }
  public void setComunicazioniLettera(String comunicazioniLettera) {
    this.comunicazioniLettera = comunicazioniLettera;
  }

  /**
   **/
  
  @JsonProperty("comunicazioni_cap")
  public String getComunicazioniCap() {
    return comunicazioniCap;
  }
  public void setComunicazioniCap(String comunicazioniCap) {
    this.comunicazioniCap = comunicazioniCap;
  }

  /**
   **/
  
  @JsonProperty("comunicazioni_circoscriz")
  public String getComunicazioniCircoscriz() {
    return comunicazioniCircoscriz;
  }
  public void setComunicazioniCircoscriz(String comunicazioniCircoscriz) {
    this.comunicazioniCircoscriz = comunicazioniCircoscriz;
  }

  /**
   **/
  
  @JsonProperty("comunicazioni_comune")
  public String getComunicazioniComune() {
    return comunicazioniComune;
  }
  public void setComunicazioniComune(String comunicazioniComune) {
    this.comunicazioniComune = comunicazioniComune;
  }

  /**
   **/
  
  @JsonProperty("comunicazioni_provincia")
  public String getComunicazioniProvincia() {
    return comunicazioniProvincia;
  }
  public void setComunicazioniProvincia(String comunicazioniProvincia) {
    this.comunicazioniProvincia = comunicazioniProvincia;
  }

  /**
   **/
  
  @JsonProperty("comunicazioni_numfax")
  public String getComunicazioniNumfax() {
    return comunicazioniNumfax;
  }
  public void setComunicazioniNumfax(String comunicazioniNumfax) {
    this.comunicazioniNumfax = comunicazioniNumfax;
  }

  /**
   **/
  
  @JsonProperty("comunicazioni_numtel1")
  public String getComunicazioniNumtel1() {
    return comunicazioniNumtel1;
  }
  public void setComunicazioniNumtel1(String comunicazioniNumtel1) {
    this.comunicazioniNumtel1 = comunicazioniNumtel1;
  }

  /**
   **/
  
  @JsonProperty("comunicazioni_intstatario1")
  public String getComunicazioniIntstatario1() {
    return comunicazioniIntstatario1;
  }
  public void setComunicazioniIntstatario1(String comunicazioniIntstatario1) {
    this.comunicazioniIntstatario1 = comunicazioniIntstatario1;
  }

  /**
   **/
  
  @JsonProperty("comunicazioni_numtel2")
  public String getComunicazioniNumtel2() {
    return comunicazioniNumtel2;
  }
  public void setComunicazioniNumtel2(String comunicazioniNumtel2) {
    this.comunicazioniNumtel2 = comunicazioniNumtel2;
  }

  /**
   **/
  
  @JsonProperty("comunicazioni_intstatario2")
  public String getComunicazioniIntstatario2() {
    return comunicazioniIntstatario2;
  }
  public void setComunicazioniIntstatario2(String comunicazioniIntstatario2) {
    this.comunicazioniIntstatario2 = comunicazioniIntstatario2;
  }

  /**
   **/
  
  @JsonProperty("comunicazioni_numtel3")
  public String getComunicazioniNumtel3() {
    return comunicazioniNumtel3;
  }
  public void setComunicazioniNumtel3(String comunicazioniNumtel3) {
    this.comunicazioniNumtel3 = comunicazioniNumtel3;
  }

  /**
   **/
  
  @JsonProperty("comunicazioni_data")
  public String getComunicazioniData() {
    return comunicazioniData;
  }
  public void setComunicazioniData(String comunicazioniData) {
    this.comunicazioniData = comunicazioniData;
  }

  /**
   **/
  
  @JsonProperty("comunicazioni_email1")
  public String getComunicazioniEmail1() {
    return comunicazioniEmail1;
  }
  public void setComunicazioniEmail1(String comunicazioniEmail1) {
    this.comunicazioniEmail1 = comunicazioniEmail1;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegaxComunicazioni regaxComunicazioni = (RegaxComunicazioni) o;
    return Objects.equals(assCod, regaxComunicazioni.assCod) &&
        Objects.equals(comunicazioniPresso, regaxComunicazioni.comunicazioniPresso) &&
        Objects.equals(comunicazioniSedime, regaxComunicazioni.comunicazioniSedime) &&
        Objects.equals(comunicazioniIndirizzo, regaxComunicazioni.comunicazioniIndirizzo) &&
        Objects.equals(comunicazioniNumcivico, regaxComunicazioni.comunicazioniNumcivico) &&
        Objects.equals(comunicazioniBis, regaxComunicazioni.comunicazioniBis) &&
        Objects.equals(comunicazioniInterno, regaxComunicazioni.comunicazioniInterno) &&
        Objects.equals(comunicazioniLettera, regaxComunicazioni.comunicazioniLettera) &&
        Objects.equals(comunicazioniCap, regaxComunicazioni.comunicazioniCap) &&
        Objects.equals(comunicazioniCircoscriz, regaxComunicazioni.comunicazioniCircoscriz) &&
        Objects.equals(comunicazioniComune, regaxComunicazioni.comunicazioniComune) &&
        Objects.equals(comunicazioniProvincia, regaxComunicazioni.comunicazioniProvincia) &&
        Objects.equals(comunicazioniNumfax, regaxComunicazioni.comunicazioniNumfax) &&
        Objects.equals(comunicazioniNumtel1, regaxComunicazioni.comunicazioniNumtel1) &&
        Objects.equals(comunicazioniIntstatario1, regaxComunicazioni.comunicazioniIntstatario1) &&
        Objects.equals(comunicazioniNumtel2, regaxComunicazioni.comunicazioniNumtel2) &&
        Objects.equals(comunicazioniIntstatario2, regaxComunicazioni.comunicazioniIntstatario2) &&
        Objects.equals(comunicazioniNumtel3, regaxComunicazioni.comunicazioniNumtel3) &&
        Objects.equals(comunicazioniData, regaxComunicazioni.comunicazioniData) &&
        Objects.equals(comunicazioniEmail1, regaxComunicazioni.comunicazioniEmail1);
  }

  @Override
  public int hashCode() {
    return Objects.hash(assCod, comunicazioniPresso, comunicazioniSedime, comunicazioniIndirizzo, comunicazioniNumcivico, comunicazioniBis, comunicazioniInterno, comunicazioniLettera, comunicazioniCap, comunicazioniCircoscriz, comunicazioniComune, comunicazioniProvincia, comunicazioniNumfax, comunicazioniNumtel1, comunicazioniIntstatario1, comunicazioniNumtel2, comunicazioniIntstatario2, comunicazioniNumtel3, comunicazioniData, comunicazioniEmail1);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RegaxComunicazioni {\n");
    
    sb.append("    assCod: ").append(toIndentedString(assCod)).append("\n");
    sb.append("    comunicazioniPresso: ").append(toIndentedString(comunicazioniPresso)).append("\n");
    sb.append("    comunicazioniSedime: ").append(toIndentedString(comunicazioniSedime)).append("\n");
    sb.append("    comunicazioniIndirizzo: ").append(toIndentedString(comunicazioniIndirizzo)).append("\n");
    sb.append("    comunicazioniNumcivico: ").append(toIndentedString(comunicazioniNumcivico)).append("\n");
    sb.append("    comunicazioniBis: ").append(toIndentedString(comunicazioniBis)).append("\n");
    sb.append("    comunicazioniInterno: ").append(toIndentedString(comunicazioniInterno)).append("\n");
    sb.append("    comunicazioniLettera: ").append(toIndentedString(comunicazioniLettera)).append("\n");
    sb.append("    comunicazioniCap: ").append(toIndentedString(comunicazioniCap)).append("\n");
    sb.append("    comunicazioniCircoscriz: ").append(toIndentedString(comunicazioniCircoscriz)).append("\n");
    sb.append("    comunicazioniComune: ").append(toIndentedString(comunicazioniComune)).append("\n");
    sb.append("    comunicazioniProvincia: ").append(toIndentedString(comunicazioniProvincia)).append("\n");
    sb.append("    comunicazioniNumfax: ").append(toIndentedString(comunicazioniNumfax)).append("\n");
    sb.append("    comunicazioniNumtel1: ").append(toIndentedString(comunicazioniNumtel1)).append("\n");
    sb.append("    comunicazioniIntstatario1: ").append(toIndentedString(comunicazioniIntstatario1)).append("\n");
    sb.append("    comunicazioniNumtel2: ").append(toIndentedString(comunicazioniNumtel2)).append("\n");
    sb.append("    comunicazioniIntstatario2: ").append(toIndentedString(comunicazioniIntstatario2)).append("\n");
    sb.append("    comunicazioniNumtel3: ").append(toIndentedString(comunicazioniNumtel3)).append("\n");
    sb.append("    comunicazioniData: ").append(toIndentedString(comunicazioniData)).append("\n");
    sb.append("    comunicazioniEmail1: ").append(toIndentedString(comunicazioniEmail1)).append("\n");
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


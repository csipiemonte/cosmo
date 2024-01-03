/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CredenzialiSigilloElettronico  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Integer id = null;
  private String alias = null;
  private String delegatedDomain = null;
  private String delegatedPassword = null;
  private String delegatedUser = null;
  private String otpPwd = null;
  private String tipoHsm = null;
  private String tipoOtpAuth = null;
  private String utente = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: alias 
  public String getAlias() {
    return alias;
  }
  public void setAlias(String alias) {
    this.alias = alias;
  }

  /**
   **/
  


  // nome originario nello yaml: delegatedDomain 
  public String getDelegatedDomain() {
    return delegatedDomain;
  }
  public void setDelegatedDomain(String delegatedDomain) {
    this.delegatedDomain = delegatedDomain;
  }

  /**
   **/
  


  // nome originario nello yaml: delegatedPassword 
  public String getDelegatedPassword() {
    return delegatedPassword;
  }
  public void setDelegatedPassword(String delegatedPassword) {
    this.delegatedPassword = delegatedPassword;
  }

  /**
   **/
  


  // nome originario nello yaml: delegatedUser 
  public String getDelegatedUser() {
    return delegatedUser;
  }
  public void setDelegatedUser(String delegatedUser) {
    this.delegatedUser = delegatedUser;
  }

  /**
   **/
  


  // nome originario nello yaml: otpPwd 
  public String getOtpPwd() {
    return otpPwd;
  }
  public void setOtpPwd(String otpPwd) {
    this.otpPwd = otpPwd;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoHsm 
  public String getTipoHsm() {
    return tipoHsm;
  }
  public void setTipoHsm(String tipoHsm) {
    this.tipoHsm = tipoHsm;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoOtpAuth 
  public String getTipoOtpAuth() {
    return tipoOtpAuth;
  }
  public void setTipoOtpAuth(String tipoOtpAuth) {
    this.tipoOtpAuth = tipoOtpAuth;
  }

  /**
   **/
  


  // nome originario nello yaml: utente 
  public String getUtente() {
    return utente;
  }
  public void setUtente(String utente) {
    this.utente = utente;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CredenzialiSigilloElettronico credenzialiSigilloElettronico = (CredenzialiSigilloElettronico) o;
    return Objects.equals(id, credenzialiSigilloElettronico.id) &&
        Objects.equals(alias, credenzialiSigilloElettronico.alias) &&
        Objects.equals(delegatedDomain, credenzialiSigilloElettronico.delegatedDomain) &&
        Objects.equals(delegatedPassword, credenzialiSigilloElettronico.delegatedPassword) &&
        Objects.equals(delegatedUser, credenzialiSigilloElettronico.delegatedUser) &&
        Objects.equals(otpPwd, credenzialiSigilloElettronico.otpPwd) &&
        Objects.equals(tipoHsm, credenzialiSigilloElettronico.tipoHsm) &&
        Objects.equals(tipoOtpAuth, credenzialiSigilloElettronico.tipoOtpAuth) &&
        Objects.equals(utente, credenzialiSigilloElettronico.utente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, alias, delegatedDomain, delegatedPassword, delegatedUser, otpPwd, tipoHsm, tipoOtpAuth, utente);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CredenzialiSigilloElettronico {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    alias: ").append(toIndentedString(alias)).append("\n");
    sb.append("    delegatedDomain: ").append(toIndentedString(delegatedDomain)).append("\n");
    sb.append("    delegatedPassword: ").append(toIndentedString(delegatedPassword)).append("\n");
    sb.append("    delegatedUser: ").append(toIndentedString(delegatedUser)).append("\n");
    sb.append("    otpPwd: ").append(toIndentedString(otpPwd)).append("\n");
    sb.append("    tipoHsm: ").append(toIndentedString(tipoHsm)).append("\n");
    sb.append("    tipoOtpAuth: ").append(toIndentedString(tipoOtpAuth)).append("\n");
    sb.append("    utente: ").append(toIndentedString(utente)).append("\n");
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


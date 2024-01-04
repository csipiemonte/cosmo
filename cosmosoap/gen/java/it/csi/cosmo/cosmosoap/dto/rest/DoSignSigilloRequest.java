/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.Documento;
import java.io.Serializable;
import javax.validation.constraints.*;

public class DoSignSigilloRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String delegatedDomain = null;
  private String delegatedUser = null;
  private String delegatedPassword = null;
  private String otpPwd = null;
  private String tipoHsm = null;
  private String tipoOtpAuth = null;
  private String user = null;
  private Documento documento = null;

  /**
   **/
  


  // nome originario nello yaml: delegatedDomain 
  @NotNull
  public String getDelegatedDomain() {
    return delegatedDomain;
  }
  public void setDelegatedDomain(String delegatedDomain) {
    this.delegatedDomain = delegatedDomain;
  }

  /**
   **/
  


  // nome originario nello yaml: delegatedUser 
  @NotNull
  public String getDelegatedUser() {
    return delegatedUser;
  }
  public void setDelegatedUser(String delegatedUser) {
    this.delegatedUser = delegatedUser;
  }

  /**
   **/
  


  // nome originario nello yaml: delegatedPassword 
  @NotNull
  public String getDelegatedPassword() {
    return delegatedPassword;
  }
  public void setDelegatedPassword(String delegatedPassword) {
    this.delegatedPassword = delegatedPassword;
  }

  /**
   **/
  


  // nome originario nello yaml: otpPwd 
  @NotNull
  public String getOtpPwd() {
    return otpPwd;
  }
  public void setOtpPwd(String otpPwd) {
    this.otpPwd = otpPwd;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoHsm 
  @NotNull
  public String getTipoHsm() {
    return tipoHsm;
  }
  public void setTipoHsm(String tipoHsm) {
    this.tipoHsm = tipoHsm;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoOtpAuth 
  @NotNull
  public String getTipoOtpAuth() {
    return tipoOtpAuth;
  }
  public void setTipoOtpAuth(String tipoOtpAuth) {
    this.tipoOtpAuth = tipoOtpAuth;
  }

  /**
   **/
  


  // nome originario nello yaml: user 
  @NotNull
  public String getUser() {
    return user;
  }
  public void setUser(String user) {
    this.user = user;
  }

  /**
   **/
  


  // nome originario nello yaml: documento 
  @NotNull
  public Documento getDocumento() {
    return documento;
  }
  public void setDocumento(Documento documento) {
    this.documento = documento;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DoSignSigilloRequest doSignSigilloRequest = (DoSignSigilloRequest) o;
    return Objects.equals(delegatedDomain, doSignSigilloRequest.delegatedDomain) &&
        Objects.equals(delegatedUser, doSignSigilloRequest.delegatedUser) &&
        Objects.equals(delegatedPassword, doSignSigilloRequest.delegatedPassword) &&
        Objects.equals(otpPwd, doSignSigilloRequest.otpPwd) &&
        Objects.equals(tipoHsm, doSignSigilloRequest.tipoHsm) &&
        Objects.equals(tipoOtpAuth, doSignSigilloRequest.tipoOtpAuth) &&
        Objects.equals(user, doSignSigilloRequest.user) &&
        Objects.equals(documento, doSignSigilloRequest.documento);
  }

  @Override
  public int hashCode() {
    return Objects.hash(delegatedDomain, delegatedUser, delegatedPassword, otpPwd, tipoHsm, tipoOtpAuth, user, documento);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DoSignSigilloRequest {\n");
    
    sb.append("    delegatedDomain: ").append(toIndentedString(delegatedDomain)).append("\n");
    sb.append("    delegatedUser: ").append(toIndentedString(delegatedUser)).append("\n");
    sb.append("    delegatedPassword: ").append(toIndentedString(delegatedPassword)).append("\n");
    sb.append("    otpPwd: ").append(toIndentedString(otpPwd)).append("\n");
    sb.append("    tipoHsm: ").append(toIndentedString(tipoHsm)).append("\n");
    sb.append("    tipoOtpAuth: ").append(toIndentedString(tipoOtpAuth)).append("\n");
    sb.append("    user: ").append(toIndentedString(user)).append("\n");
    sb.append("    documento: ").append(toIndentedString(documento)).append("\n");
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


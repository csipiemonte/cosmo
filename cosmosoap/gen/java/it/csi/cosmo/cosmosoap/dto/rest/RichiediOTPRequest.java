/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class RichiediOTPRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String alias = null;
  private String pin = null;
  private String codiceCa = null;
  private String password = null;
  private String codiceEnteCertificatore = null;
  private String provider = null;

  /**
   **/
  


  // nome originario nello yaml: alias 
  @NotNull
  public String getAlias() {
    return alias;
  }
  public void setAlias(String alias) {
    this.alias = alias;
  }

  /**
   **/
  


  // nome originario nello yaml: pin 
  @NotNull
  public String getPin() {
    return pin;
  }
  public void setPin(String pin) {
    this.pin = pin;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceCa 
  public String getCodiceCa() {
    return codiceCa;
  }
  public void setCodiceCa(String codiceCa) {
    this.codiceCa = codiceCa;
  }

  /**
   **/
  


  // nome originario nello yaml: password 
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceEnteCertificatore 
  @NotNull
  public String getCodiceEnteCertificatore() {
    return codiceEnteCertificatore;
  }
  public void setCodiceEnteCertificatore(String codiceEnteCertificatore) {
    this.codiceEnteCertificatore = codiceEnteCertificatore;
  }

  /**
   **/
  


  // nome originario nello yaml: provider 
  public String getProvider() {
    return provider;
  }
  public void setProvider(String provider) {
    this.provider = provider;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RichiediOTPRequest richiediOTPRequest = (RichiediOTPRequest) o;
    return Objects.equals(alias, richiediOTPRequest.alias) &&
        Objects.equals(pin, richiediOTPRequest.pin) &&
        Objects.equals(codiceCa, richiediOTPRequest.codiceCa) &&
        Objects.equals(password, richiediOTPRequest.password) &&
        Objects.equals(codiceEnteCertificatore, richiediOTPRequest.codiceEnteCertificatore) &&
        Objects.equals(provider, richiediOTPRequest.provider);
  }

  @Override
  public int hashCode() {
    return Objects.hash(alias, pin, codiceCa, password, codiceEnteCertificatore, provider);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RichiediOTPRequest {\n");
    
    sb.append("    alias: ").append(toIndentedString(alias)).append("\n");
    sb.append("    pin: ").append(toIndentedString(pin)).append("\n");
    sb.append("    codiceCa: ").append(toIndentedString(codiceCa)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    codiceEnteCertificatore: ").append(toIndentedString(codiceEnteCertificatore)).append("\n");
    sb.append("    provider: ").append(toIndentedString(provider)).append("\n");
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


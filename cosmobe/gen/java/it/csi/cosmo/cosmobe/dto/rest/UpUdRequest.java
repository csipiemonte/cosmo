/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmobe.dto.rest.StiloAllegatoRequest;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class UpUdRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codApplicazione = null;
  private String istanzaApplicazione = null;
  private String userName = null;
  private String password = null;
  private String xml = null;
  private String hash = null;
  private List<StiloAllegatoRequest> allegati = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: codApplicazione 
  public String getCodApplicazione() {
    return codApplicazione;
  }
  public void setCodApplicazione(String codApplicazione) {
    this.codApplicazione = codApplicazione;
  }

  /**
   **/
  


  // nome originario nello yaml: istanzaApplicazione 
  public String getIstanzaApplicazione() {
    return istanzaApplicazione;
  }
  public void setIstanzaApplicazione(String istanzaApplicazione) {
    this.istanzaApplicazione = istanzaApplicazione;
  }

  /**
   **/
  


  // nome originario nello yaml: userName 
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
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
  


  // nome originario nello yaml: xml 
  public String getXml() {
    return xml;
  }
  public void setXml(String xml) {
    this.xml = xml;
  }

  /**
   **/
  


  // nome originario nello yaml: hash 
  public String getHash() {
    return hash;
  }
  public void setHash(String hash) {
    this.hash = hash;
  }

  /**
   **/
  


  // nome originario nello yaml: allegati 
  public List<StiloAllegatoRequest> getAllegati() {
    return allegati;
  }
  public void setAllegati(List<StiloAllegatoRequest> allegati) {
    this.allegati = allegati;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpUdRequest upUdRequest = (UpUdRequest) o;
    return Objects.equals(codApplicazione, upUdRequest.codApplicazione) &&
        Objects.equals(istanzaApplicazione, upUdRequest.istanzaApplicazione) &&
        Objects.equals(userName, upUdRequest.userName) &&
        Objects.equals(password, upUdRequest.password) &&
        Objects.equals(xml, upUdRequest.xml) &&
        Objects.equals(hash, upUdRequest.hash) &&
        Objects.equals(allegati, upUdRequest.allegati);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codApplicazione, istanzaApplicazione, userName, password, xml, hash, allegati);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpUdRequest {\n");
    
    sb.append("    codApplicazione: ").append(toIndentedString(codApplicazione)).append("\n");
    sb.append("    istanzaApplicazione: ").append(toIndentedString(istanzaApplicazione)).append("\n");
    sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    xml: ").append(toIndentedString(xml)).append("\n");
    sb.append("    hash: ").append(toIndentedString(hash)).append("\n");
    sb.append("    allegati: ").append(toIndentedString(allegati)).append("\n");
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


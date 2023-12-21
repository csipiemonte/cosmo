/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CredenzialiAutenticazioneFruitore  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String username = null;
  private String password = null;
  private String clientId = null;
  private String clientSecret = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  @NotNull
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: username 
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
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
  


  // nome originario nello yaml: clientId 
  public String getClientId() {
    return clientId;
  }
  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  /**
   **/
  


  // nome originario nello yaml: clientSecret 
  public String getClientSecret() {
    return clientSecret;
  }
  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CredenzialiAutenticazioneFruitore credenzialiAutenticazioneFruitore = (CredenzialiAutenticazioneFruitore) o;
    return Objects.equals(id, credenzialiAutenticazioneFruitore.id) &&
        Objects.equals(username, credenzialiAutenticazioneFruitore.username) &&
        Objects.equals(password, credenzialiAutenticazioneFruitore.password) &&
        Objects.equals(clientId, credenzialiAutenticazioneFruitore.clientId) &&
        Objects.equals(clientSecret, credenzialiAutenticazioneFruitore.clientSecret);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, password, clientId, clientSecret);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CredenzialiAutenticazioneFruitore {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
    sb.append("    clientSecret: ").append(toIndentedString(clientSecret)).append("\n");
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


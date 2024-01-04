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

public class CommonRemoteData  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private byte[] data = null;
  private String authData = null;

  /**
   **/
  


  // nome originario nello yaml: data 
  @Pattern(regexp="^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$")
  public byte[] getData() {
    return data;
  }
  public void setData(byte[] data) {
    this.data = data;
  }

  /**
   **/
  


  // nome originario nello yaml: authData 
  public String getAuthData() {
    return authData;
  }
  public void setAuthData(String authData) {
    this.authData = authData;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommonRemoteData commonRemoteData = (CommonRemoteData) o;
    return Objects.equals(data, commonRemoteData.data) &&
        Objects.equals(authData, commonRemoteData.authData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, authData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommonRemoteData {\n");
    
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    authData: ").append(toIndentedString(authData)).append("\n");
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


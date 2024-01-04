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

public class SignatureVerificationParameters  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Boolean verifyCertificateList = null;

  /**
   **/
  


  // nome originario nello yaml: verifyCertificateList 
  public Boolean isVerifyCertificateList() {
    return verifyCertificateList;
  }
  public void setVerifyCertificateList(Boolean verifyCertificateList) {
    this.verifyCertificateList = verifyCertificateList;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SignatureVerificationParameters signatureVerificationParameters = (SignatureVerificationParameters) o;
    return Objects.equals(verifyCertificateList, signatureVerificationParameters.verifyCertificateList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(verifyCertificateList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SignatureVerificationParameters {\n");
    
    sb.append("    verifyCertificateList: ").append(toIndentedString(verifyCertificateList)).append("\n");
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


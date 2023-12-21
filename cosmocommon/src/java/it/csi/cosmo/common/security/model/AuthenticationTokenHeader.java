/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.security.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;


public class AuthenticationTokenHeader implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @JsonProperty("alg")
  private String algorithm;

  @JsonProperty("typ")
  private String type;

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "AuthenticationTokenHeader [algorithm=" + algorithm + ", type=" + type + "]";
  }

}

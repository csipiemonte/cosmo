/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.integration.apimgr.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;


public class ApiMgrJWTAssertionHeader {

  @JsonProperty("alg")
  private String algorithm;

  @JsonProperty("typ")
  private String type;

  @JsonProperty("x5t")
  private String x5tSignature;

  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonAnySetter
  public void setAdditionalProperty(String propertyKey, Object value) {
    this.additionalProperties.put(propertyKey, value);
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

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

  public String getX5tSignature() {
    return x5tSignature;
  }

  public void setX5tSignature(String x5tSignature) {
    this.x5tSignature = x5tSignature;
  }

  @Override
  public String toString() {
    return "ApiMgrJWTAssertionHeader [algorithm=" + algorithm + ", type=" + type + "]";
  }

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.integration.apimgr.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;


public class ApiMgrJWTAssertionPayload {

  /**
   * iss: L'issuer del JWT cioe' l'Api Manager
   *
   * es.: "wso2.org/products/am"
   */
  @JsonProperty("iss")
  private String issuer;

  /**
   * exp: La data di scadenza del token
   *
   * es.: 1582733023
   */
  @JsonProperty("exp")
  private Long expireEpoch;

  /**
   * http:// wso2.org/claims/version: La versione dell'API
   *
   * es.: "v1",
   */
  @JsonProperty(ApiMgrConfig.JWT_ASSERTION_CLAIMS_NAMESPACE + "/version")
  private String version;

  /**
   * http:// wso2.org/claims/applicationname: Il nome dell'applicazione censita sull'Api Manager
   *
   * es.: "Estate Ragazzi",
   */
  @JsonProperty(ApiMgrConfig.JWT_ASSERTION_CLAIMS_NAMESPACE + "/applicationname")
  private String applicationName;

  /**
   * http:// wso2.org/claims/enduser: l'utente finale dell'APP che ha fatto l'invocazione all'API
   *
   * es.: "mrossi@carbon.super",
   */
  @JsonProperty(ApiMgrConfig.JWT_ASSERTION_CLAIMS_NAMESPACE + "/enduser")
  private String enduser;

  /**
   * http:// wso2.org/claims/subscriber: Il sottoscritto dell'API,
   *
   * es.: "mrossi",
   */
  @JsonProperty(ApiMgrConfig.JWT_ASSERTION_CLAIMS_NAMESPACE + "/subscriber")
  private String subscriber;

  /**
   * http:// wso2.org/claims/tier: La banda di sottoscrizione per la sottoscrizione (chiamate al
   * minuto)
   *
   * es.: "Unlimited",
   */
  @JsonProperty(ApiMgrConfig.JWT_ASSERTION_CLAIMS_NAMESPACE + "/tier")
  private String tier;

  /**
   * http:// wso2.org/claims/usertype: Puo' avere valore APPLICATION o APPLICATION_USER per
   * identificare se si riferisce all'asserzione generata per un applicazione con l'utente finale o
   * per un'applicazione senza utente
   *
   * es.: "APPLICATION",
   */
  @JsonProperty(ApiMgrConfig.JWT_ASSERTION_CLAIMS_NAMESPACE + "/usertype")
  private ApiMgrJWTAssertionUserType userType;

  /**
   * http:// wso2.org/claims/apicontext: Il contesto dell'API
   *
   * es.: "/demografia/v1"
   */
  @JsonProperty(ApiMgrConfig.JWT_ASSERTION_CLAIMS_NAMESPACE + "/apicontext")
  private String apiContext;

  @JsonProperty(ApiMgrConfig.JWT_ASSERTION_CLAIMS_NAMESPACE + "/role")
  private List<String> roles;

  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonAnySetter
  public void setAdditionalProperty(String propertyKey, Object value) {
    this.additionalProperties.put(propertyKey, value);
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  public String getIssuer() {
    return issuer;
  }

  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }

  public Long getExpireEpoch() {
    return expireEpoch;
  }

  public void setExpireEpoch(Long expireEpoch) {
    this.expireEpoch = expireEpoch;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getApplicationName() {
    return applicationName;
  }

  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  public String getEnduser() {
    return enduser;
  }

  public void setEnduser(String enduser) {
    this.enduser = enduser;
  }

  public String getSubscriber() {
    return subscriber;
  }

  public void setSubscriber(String subscriber) {
    this.subscriber = subscriber;
  }

  public String getTier() {
    return tier;
  }

  public void setTier(String tier) {
    this.tier = tier;
  }

  public ApiMgrJWTAssertionUserType getUserType() {
    return userType;
  }

  public void setUserType(ApiMgrJWTAssertionUserType userType) {
    this.userType = userType;
  }

  public String getApiContext() {
    return apiContext;
  }

  public void setApiContext(String apiContext) {
    this.apiContext = apiContext;
  }

  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

  @Override
  public String toString() {
    return "ApiMgrJWTAssertionPayload [" + (issuer != null ? "issuer=" + issuer + ", " : "")
        + (expireEpoch != null ? "expireEpoch=" + expireEpoch + ", " : "")
        + (version != null ? "version=" + version + ", " : "")
        + (applicationName != null ? "applicationName=" + applicationName + ", " : "")
        + (enduser != null ? "enduser=" + enduser + ", " : "")
        + (subscriber != null ? "subscriber=" + subscriber + ", " : "")
        + (tier != null ? "tier=" + tier + ", " : "")
        + (userType != null ? "userType=" + userType + ", " : "")
        + (apiContext != null ? "apiContext=" + apiContext + ", " : "")
        + (roles != null ? "roles=" + roles + ", " : "")
        + (additionalProperties != null ? "additionalProperties=" + additionalProperties : "")
        + "]";
  }

}

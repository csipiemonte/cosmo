/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class AuthenticationTokenPayload<T> {

  @JsonProperty("sub")
  private String subject;

  @JsonProperty("iss")
  private String issuer;

  @JsonProperty("iat")
  private Long issuedAtEpoch;

  @JsonProperty("exp")
  private Long expiresAtEpoch;

  @JsonProperty("content")
  private T content;

  public Long getExpiresAtEpoch() {
    return expiresAtEpoch;
  }

  public void setExpiresAtEpoch(Long expiresAtEpoch) {
    this.expiresAtEpoch = expiresAtEpoch;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getIssuer() {
    return issuer;
  }

  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }

  public Long getIssuedAtEpoch() {
    return issuedAtEpoch;
  }

  public void setIssuedAtEpoch(Long issuedAtEpoch) {
    this.issuedAtEpoch = issuedAtEpoch;
  }

  public T getContent() {
    return content;
  }

  public void setContent(T content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return "AuthenticationTokenPayload [subject=" + subject + "]";
  }

}

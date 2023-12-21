/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.security.model;

public class AuthenticationToken<T> {

  private AuthenticationTokenHeader header;

  private AuthenticationTokenPayload<T> payload;

  private String signature;

  public AuthenticationTokenHeader getHeader() {
    return header;
  }

  public void setHeader(AuthenticationTokenHeader header) {
    this.header = header;
  }

  public AuthenticationTokenPayload<T> getPayload() {
    return payload;
  }

  public void setPayload(AuthenticationTokenPayload<T> payload) {
    this.payload = payload;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  @Override
  public String toString() {
    return "AuthenticationToken [header=" + header + "]";
  }

}

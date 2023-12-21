/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.integration.apimgr.model;

/**
 * L'Api Manager genera un'asserzione JWT contenente informazioni specifiche di ogni applicazione.
 * Il formato generale del JWT e' {token infor}.{claims list}.{signature}
 *
 * L'asserzione e' in formato Base64 e viene inviata al Backend in un header HTTP con il nome
 * X-JWT-Assertion.
 *
 * A seconda del grant type utilizzato l'Api Manager arricchisce il JWT con claims supplementari, ad
 * esempio se si utilizza il grant type Authorization code, che richiede l'autenticazione
 * dell'utente finale, si avranno i claims relativi all'utente (nome,cognome,ecc..) oltre a quelli
 * dell'applicazione.
 *
 */

public class ApiMgrJWTAssertion {

  private ApiMgrJWTAssertionHeader header;

  private ApiMgrJWTAssertionPayload payload;

  private String signature;

  public ApiMgrJWTAssertionHeader getHeader() {
    return header;
  }

  public void setHeader(ApiMgrJWTAssertionHeader header) {
    this.header = header;
  }

  public ApiMgrJWTAssertionPayload getPayload() {
    return payload;
  }

  public void setPayload(ApiMgrJWTAssertionPayload payload) {
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
    return "ApiMgrJWTAssertion [header=" + header + "]";
  }
}

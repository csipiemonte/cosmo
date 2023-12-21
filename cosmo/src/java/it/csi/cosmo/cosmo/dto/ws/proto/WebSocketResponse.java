/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.dto.ws.proto;

import it.csi.cosmo.cosmo.dto.ws.WebSocketMessageType;

/**
 *
 */

public class WebSocketResponse {

  private String code = null;

  private Integer status = null;

  private String title = null;

  private Object response = null;

  private WebSocketMessageType messageType = WebSocketMessageType.RESPONSE;

  @Override
  public String toString() {
    return "WebSocketResponse [code=" + code + ", status=" + status + ", title=" + title
        + ", response=" + response + "]";
  }

  public void setMessageType(WebSocketMessageType messageType) {
    this.messageType = messageType;
  }

  public WebSocketMessageType getMessageType() {
    return messageType;
  }

  public Object getResponse() {
    return response;
  }

  public void setResponse(Object response) {
    this.response = response;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

}

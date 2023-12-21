/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.dto.ws.proto;

import it.csi.cosmo.cosmo.dto.ws.WebSocketMessageType;

/**
 *
 */

public class WebSocketRequest<T> {

  private T request = null;

  private WebSocketMessageType messageType = WebSocketMessageType.REQUEST;

  @Override
  public String toString() {
    return "WebSocketRequest [request=" + request + "]";
  }

  public void setMessageType(WebSocketMessageType messageType) {
    this.messageType = messageType;
  }

  public WebSocketMessageType getMessageType() {
    return messageType;
  }

  public T getRequest() {
    return request;
  }

  public void setRequest(T request) {
    this.request = request;
  }

}

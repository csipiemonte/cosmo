/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.dto.ws;

/**
 *
 */

public class WebSocketEvent<T> {

  private WebSocketEventType event = null;

  private T payload = null;

  private WebSocketMessageType messageType = WebSocketMessageType.EVENT;

  public WebSocketEvent() {
    // NOP
  }

  public WebSocketEvent(WebSocketEventType event, T payload) {
    super();
    this.event = event;
    this.payload = payload;
  }

  public void setMessageType(WebSocketMessageType messageType) {
    this.messageType = messageType;
  }

  public WebSocketMessageType getMessageType() {
    return messageType;
  }

  public WebSocketEventType getEvent() {
    return event;
  }

  public void setEvent(WebSocketEventType event) {
    this.event = event;
  }

  public T getPayload() {
    return payload;
  }

  public void setPayload(T payload) {
    this.payload = payload;
  }

  @Override
  public String toString() {
    return "WebSocketEvent [event=" + event + ", payload=" + payload + "]";
  }


}

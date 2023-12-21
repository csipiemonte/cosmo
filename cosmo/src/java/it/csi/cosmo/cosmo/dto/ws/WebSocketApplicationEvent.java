/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.dto.ws;

/**
 *
 */

public class WebSocketApplicationEvent {

  private String event = null;

  private Object payload = null;

  private WebSocketMessageType messageType = WebSocketMessageType.EVENT;

  public WebSocketApplicationEvent() {
    // NOP
  }

  public WebSocketApplicationEvent(String event, Object payload) {
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

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public Object getPayload() {
    return payload;
  }

  public void setPayload(Object payload) {
    this.payload = payload;
  }

  @Override
  public String toString() {
    return "WebSocketEvent [event=" + event + ", payload=" + payload + "]";
  }


}

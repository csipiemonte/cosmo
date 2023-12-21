/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.dto.rest.ws;

/**
 *
 */

public class WebSocketEventPostRequest extends WebSocketPostRequest {

  private String event;

  public WebSocketEventPostRequest() {
    // NOP
  }

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

}

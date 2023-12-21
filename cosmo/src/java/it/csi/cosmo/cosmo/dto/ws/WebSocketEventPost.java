/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.dto.ws;

/**
 *
 */

public class WebSocketEventPost extends WebSocketPost {

  private String event;

  public WebSocketEventPost() {
    // NOP
  }

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

}

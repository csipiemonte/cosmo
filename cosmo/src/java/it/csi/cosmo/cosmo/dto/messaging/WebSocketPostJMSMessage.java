/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.dto.messaging;

import java.util.Arrays;
import it.csi.cosmo.cosmo.dto.rest.ws.WebSocketTargetSelector;

/**
 *
 */

public class WebSocketPostJMSMessage extends ParentJMSMessage {

  /**
   *
   */
  private static final long serialVersionUID = 4623905630061023359L;

  private WebSocketTargetSelector[] targets;

  private String jsonPayload;

  private String event;

  public WebSocketPostJMSMessage() {
    // NOP
  }

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public WebSocketTargetSelector[] getTargets() {
    return targets;
  }

  public void setTargets(WebSocketTargetSelector[] targets) {
    this.targets = targets;
  }

  public String getJsonPayload() {
    return jsonPayload;
  }

  public void setJsonPayload(String jsonPayload) {
    this.jsonPayload = jsonPayload;
  }

  @Override
  public String toString() {
    return "WebSocketPostJMSMessage ["
        + (targets != null ? "targets=" + Arrays.toString(targets) + ", " : "")
        + (jsonPayload != null ? "jsonPayload=" + jsonPayload + ", " : "")
        + (event != null ? "event=" + event : "") + "]";
  }

}

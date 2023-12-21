/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.dto.ws;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.cosmo.dto.rest.ws.WebSocketTargetSelector;

/**
 *
 */

public class WebSocketPost {

  private List<WebSocketTargetSelector> targets = new ArrayList<>();

  private JsonNode payload;

  public WebSocketPost() {
    // NOP
  }

  public List<WebSocketTargetSelector> getTargets() {
    return targets;
  }

  public void setTargets(List<WebSocketTargetSelector> targets) {
    this.targets = targets;
  }

  public JsonNode getPayload() {
    return payload;
  }

  public void setPayload(JsonNode payload) {
    this.payload = payload;
  }

  @Override
  public String toString() {
    return "WebSocketPostJMSMessage [targets=" + targets + ", payload=" + payload + "]";
  }

}

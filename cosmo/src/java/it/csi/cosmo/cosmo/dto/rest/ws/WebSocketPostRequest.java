/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.dto.rest.ws;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class WebSocketPostRequest {

  private List<WebSocketTargetSelector> targets = new ArrayList<>();

  private Object payload;

  public WebSocketPostRequest() {
    // NOP
  }

  public List<WebSocketTargetSelector> getTargets() {
    return targets;
  }

  public void setTargets(List<WebSocketTargetSelector> targets) {
    this.targets = targets;
  }

  public Object getPayload() {
    return payload;
  }

  public void setPayload(Object payload) {
    this.payload = payload;
  }

  @Override
  public String toString() {
    return "WebSocketPostRequest [targets=" + targets + ", payload=" + payload + "]";
  }

}

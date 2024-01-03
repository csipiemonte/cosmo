/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.config.ws;

import org.apache.commons.lang3.StringUtils;

/**
 * ENUM per i tipi di eventi da gestire su frontend quando arriva una notifica da websocket
 */

public enum WebsocketPostEventName {


  //@formatter:off
  NOTIFICA_ULTIME_LAVORATE("notifica.ultime.lavorate"),
  NOTIFICA_STATO_SMISTAMENTO("notifica.stato.smistamento"),
  NOTIFICA_COMMENTO("notifica.commento")
  ;
  //@formatter:on

  private String eventName;

  private WebsocketPostEventName(String eventName) {
    this.eventName = eventName;
  }

  public String getEventName() {
    return eventName;
  }

  public static WebsocketPostEventName fromEventName(String eventName) {
    if (StringUtils.isBlank(eventName)) {
      return null;
    }
    for (WebsocketPostEventName candidate : WebsocketPostEventName.values()) {
      if (candidate.getEventName().equalsIgnoreCase(eventName)) {
        return candidate;
      }
    }
    return null;
  }
}

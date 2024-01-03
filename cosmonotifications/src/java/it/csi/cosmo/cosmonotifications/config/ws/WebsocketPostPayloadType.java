/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.config.ws;

import org.apache.commons.lang3.StringUtils;

/**
 * ENUM per i tipi di payload del websocket
 */

public enum WebsocketPostPayloadType {

  //@formatter:off
  INFO("INFO"),
  SUCCESS("SUCCESS"),
  PRIMARY("PRIMARY"),
  WARNING("WARNING"),
  DANGER("DANGER")
  ;
  //@formatter:on

  private String type;

  private WebsocketPostPayloadType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public static WebsocketPostPayloadType fromCode(String code) {
    if (StringUtils.isBlank(code)) {
      return null;
    }
    for (WebsocketPostPayloadType candidate : WebsocketPostPayloadType.values()) {
      if (candidate.getType().equalsIgnoreCase(code)) {
        return candidate;
      }
    }
    return null;
  }

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.dto.ws;

import java.time.OffsetDateTime;
import java.util.UUID;
import javax.websocket.Session;
import it.csi.cosmo.common.security.model.UserInfoDTO;

/**
 *
 */

public class WebSocketJointSession {

  private String id;

  private Session wsSession;

  private UserInfoDTO principal;

  private OffsetDateTime openedAt;

  private OffsetDateTime lastIncomingMessage;

  public WebSocketJointSession() {
    // NOP
  }

  public WebSocketJointSession(Session wsSession, UserInfoDTO principal) {
    super();
    this.wsSession = wsSession;
    this.principal = principal;
    this.openedAt = OffsetDateTime.now();
    this.id = wsSession != null ? wsSession.getId() : ("orphan-" + UUID.randomUUID().toString());
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public OffsetDateTime getOpenedAt() {
    return openedAt;
  }

  public void setOpenedAt(OffsetDateTime openedAt) {
    this.openedAt = openedAt;
  }

  public OffsetDateTime getLastIncomingMessage() {
    return lastIncomingMessage;
  }

  public void setLastIncomingMessage(OffsetDateTime lastIncomingMessage) {
    this.lastIncomingMessage = lastIncomingMessage;
  }

  public Session getWsSession() {
    return wsSession;
  }

  public void setWsSession(Session wsSession) {
    this.wsSession = wsSession;
  }

  public UserInfoDTO getPrincipal() {
    return principal;
  }

  public void setPrincipal(UserInfoDTO principal) {
    this.principal = principal;
  }

  @Override
  public String toString() {
    return "WebSocketJointSession [id=" + id + ", wsSession=" + wsSession + ", principal="
        + principal + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    WebSocketJointSession other = (WebSocketJointSession) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }

}

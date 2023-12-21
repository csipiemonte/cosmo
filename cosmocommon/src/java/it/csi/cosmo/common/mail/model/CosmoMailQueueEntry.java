/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.mail.model;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 *
 */

public class CosmoMailQueueEntry {

  private String uuid;

  private CosmoMail mail;

  private MailStatus status;

  private Integer failedAttempts;

  private CompletableFuture<CosmoMailDeliveryResult> resolver;

  public CosmoMailQueueEntry(CosmoMail mail) {
    this.uuid = UUID.randomUUID().toString();
    this.mail = mail;
    status = MailStatus.PREPARED;
    failedAttempts = 0;
    this.resolver = new CompletableFuture<>();
  }

  public String getUuid() {
    return uuid;
  }

  public CosmoMail getMail() {
    return mail;
  }

  public void setMail(CosmoMail mail) {
    this.mail = mail;
  }

  public MailStatus getStatus() {
    return status;
  }

  public void setStatus(MailStatus status) {
    this.status = status;
  }

  public Integer getFailedAttempts() {
    return failedAttempts;
  }

  public void setFailedAttempts(Integer failedAttempts) {
    this.failedAttempts = failedAttempts;
  }

  public CompletableFuture<CosmoMailDeliveryResult> getResolver() {
    return resolver;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
    CosmoMailQueueEntry other = (CosmoMailQueueEntry) obj;
    if (uuid == null) {
      if (other.uuid != null) {
        return false;
      }
    } else if (!uuid.equals(other.uuid)) {
      return false;
    }
    return true;
  }

}

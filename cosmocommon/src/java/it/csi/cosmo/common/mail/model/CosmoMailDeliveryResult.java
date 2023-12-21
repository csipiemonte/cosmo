/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.mail.model;

/**
 *
 */

public class CosmoMailDeliveryResult {

  private String uuid;

  private CosmoMail mail;

  private MailStatus status;

  private Integer failedAttempts;

  public CosmoMailDeliveryResult() {
    // NOP
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
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

}

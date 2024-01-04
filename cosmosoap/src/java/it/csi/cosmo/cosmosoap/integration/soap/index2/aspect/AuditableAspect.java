/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.aspect;

import java.time.ZonedDateTime;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexProperty;

/**
 *
 */

public class AuditableAspect {

  @IndexProperty(value = "cm:created", required = false, readOnly = true)
  protected ZonedDateTime creationTimestamp;

  @IndexProperty(value = "cm:creator", required = false, readOnly = true)
  protected String creatorUser;

  @IndexProperty(value = "cm:modified", required = false, readOnly = true)
  protected ZonedDateTime modifiedTimestamp;

  @IndexProperty(value = "cm:modifier", required = false, readOnly = true)
  protected String modifierUser;

  @IndexProperty(value = "cm:accessed", required = false, readOnly = true)
  protected ZonedDateTime accessedTimestamp;

  public AuditableAspect() {
    // NOP
  }

  public ZonedDateTime getCreationTimestamp() {
    return creationTimestamp;
  }

  public void setCreationTimestamp(ZonedDateTime creationTimestamp) {
    this.creationTimestamp = creationTimestamp;
  }

  public String getCreatorUser() {
    return creatorUser;
  }

  public void setCreatorUser(String creatorUser) {
    this.creatorUser = creatorUser;
  }

  public ZonedDateTime getModifiedTimestamp() {
    return modifiedTimestamp;
  }

  public void setModifiedTimestamp(ZonedDateTime modifiedTimestamp) {
    this.modifiedTimestamp = modifiedTimestamp;
  }

  public String getModifierUser() {
    return modifierUser;
  }

  public void setModifierUser(String modifierUser) {
    this.modifierUser = modifierUser;
  }

  public ZonedDateTime getAccessedTimestamp() {
    return accessedTimestamp;
  }

  public void setAccessedTimestamp(ZonedDateTime accessedTimestamp) {
    this.accessedTimestamp = accessedTimestamp;
  }

  @Override
  public String toString() {
    return "AuditableAspect [creationTimestamp=" + creationTimestamp + ", creatorUser="
        + creatorUser + ", modifiedTimestamp=" + modifiedTimestamp + ", modifierUser="
        + modifierUser + ", accessedTimestamp=" + accessedTimestamp + "]";
  }

}

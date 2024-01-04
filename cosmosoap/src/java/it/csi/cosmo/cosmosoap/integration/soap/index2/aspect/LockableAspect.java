/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.aspect;

import java.time.ZonedDateTime;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexProperty;

/**
 *
 */

@IndexAspect(value = "cm:lockable")
public class LockableAspect {

  @IndexProperty(value = "cm:lockOwner", required = false, readOnly = true)
  protected String lockOwner;

  @IndexProperty(value = "cm:lockType", required = false, readOnly = true)
  protected String lockType;

  @IndexProperty(value = "cm:expiryDate", required = false, readOnly = true)
  protected ZonedDateTime expiryDate;

  @IndexProperty(value = "cm:lockIsDeep", required = false, readOnly = true)
  protected Boolean lockIsDeep;

  public LockableAspect() {
    // NOP
  }

  @JsonProperty("isCurrentlyLocked")
  public boolean isCurrentlyLocked() {
    return !StringUtils.isBlank(lockType) && !StringUtils.isBlank(lockOwner);
  }

  public String getLockOwner() {
    return lockOwner;
  }

  public void setLockOwner(String lockOwner) {
    this.lockOwner = lockOwner;
  }

  public String getLockType() {
    return lockType;
  }

  public void setLockType(String lockType) {
    this.lockType = lockType;
  }

  public ZonedDateTime getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(ZonedDateTime expiryDate) {
    this.expiryDate = expiryDate;
  }

  public Boolean getLockIsDeep() {
    return lockIsDeep;
  }

  public void setLockIsDeep(Boolean lockIsDeep) {
    this.lockIsDeep = lockIsDeep;
  }

  @Override
  public String toString() {
    return "LockableAspect [lockOwner=" + lockOwner + ", lockType=" + lockType + ", expiryDate="
        + expiryDate + ", lockIsDeep=" + lockIsDeep + "]";
  }

}

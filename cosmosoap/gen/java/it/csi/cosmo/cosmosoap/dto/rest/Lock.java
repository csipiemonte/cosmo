/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Lock  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String lockOwner = null;
  private String lockType = null;
  private OffsetDateTime expiryDate = null;
  private Boolean lockIsDeep = null;

  /**
   **/
  


  // nome originario nello yaml: lockOwner 
  public String getLockOwner() {
    return lockOwner;
  }
  public void setLockOwner(String lockOwner) {
    this.lockOwner = lockOwner;
  }

  /**
   **/
  


  // nome originario nello yaml: lockType 
  public String getLockType() {
    return lockType;
  }
  public void setLockType(String lockType) {
    this.lockType = lockType;
  }

  /**
   **/
  


  // nome originario nello yaml: expiryDate 
  public OffsetDateTime getExpiryDate() {
    return expiryDate;
  }
  public void setExpiryDate(OffsetDateTime expiryDate) {
    this.expiryDate = expiryDate;
  }

  /**
   **/
  


  // nome originario nello yaml: lockIsDeep 
  public Boolean isLockIsDeep() {
    return lockIsDeep;
  }
  public void setLockIsDeep(Boolean lockIsDeep) {
    this.lockIsDeep = lockIsDeep;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Lock lock = (Lock) o;
    return Objects.equals(lockOwner, lock.lockOwner) &&
        Objects.equals(lockType, lock.lockType) &&
        Objects.equals(expiryDate, lock.expiryDate) &&
        Objects.equals(lockIsDeep, lock.lockIsDeep);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lockOwner, lockType, expiryDate, lockIsDeep);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Lock {\n");
    
    sb.append("    lockOwner: ").append(toIndentedString(lockOwner)).append("\n");
    sb.append("    lockType: ").append(toIndentedString(lockType)).append("\n");
    sb.append("    expiryDate: ").append(toIndentedString(expiryDate)).append("\n");
    sb.append("    lockIsDeep: ").append(toIndentedString(lockIsDeep)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


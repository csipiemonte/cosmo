/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.infinispan.model;

import java.time.LocalDateTime;
import java.util.UUID;


public class InfinispanDistributedLockToken implements java.io.Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -5935587834124419084L;

  private String lockId = UUID.randomUUID ().toString ();

  private String code;

  private String instanceId;

  private LocalDateTime acquired;

  private LocalDateTime expires;

  public InfinispanDistributedLockToken () {
    // NOP
  }

  private InfinispanDistributedLockToken ( Builder builder ) {
    lockId = builder.lockId;
    code = builder.code;
    instanceId = builder.instanceId;
    acquired = builder.acquired;
    expires = builder.expires;
  }

  public boolean isExpired () {
    return !LocalDateTime.now ().isBefore ( expires );
  }

  public void setLockId ( String lockId ) {
    this.lockId = lockId;
  }

  public String getLockId () {
    return lockId;
  }

  public String getCode () {
    return code;
  }

  public void setCode ( String code ) {
    this.code = code;
  }

  public String getInstanceId () {
    return instanceId;
  }

  public void setInstanceId ( String instanceId ) {
    this.instanceId = instanceId;
  }

  public LocalDateTime getAcquired () {
    return acquired;
  }

  public void setAcquired ( LocalDateTime acquired ) {
    this.acquired = acquired;
  }

  public LocalDateTime getExpires () {
    return expires;
  }

  public void setExpires ( LocalDateTime expires ) {
    this.expires = expires;
  }

  /**
   * Creates builder to build {@link InfinispanDistributedLockToken}.
   *
   * @return created builder
   */
  public static Builder builder () {
    return new Builder ();
  }

  /**
   * Builder to build {@link InfinispanDistributedLockToken}.
   */
  public static final class Builder {

    private String lockId;

    private String code;

    private String instanceId;

    private LocalDateTime acquired;

    private LocalDateTime expires;

    private Builder () {
    }

    public Builder withLockId ( String lockId ) {
      this.lockId = lockId;
      return this;
    }

    public Builder withCode ( String code ) {
      this.code = code;
      return this;
    }

    public Builder withInstanceId ( String instanceId ) {
      this.instanceId = instanceId;
      return this;
    }

    public Builder withAcquired ( LocalDateTime acquired ) {
      this.acquired = acquired;
      return this;
    }

    public Builder withExpires ( LocalDateTime expires ) {
      this.expires = expires;
      return this;
    }

    public InfinispanDistributedLockToken build () {
      return new InfinispanDistributedLockToken ( this );
    }
  }

}

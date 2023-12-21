/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.async.store;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import it.csi.cosmo.common.async.model.LongTask;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;

/**
 *
 */

public class LongTaskInMemoryStore {

  private final CosmoLogger log;

  private final Map<String, LongTask<?>> publicTaskMap = new HashMap<>();

  private LongTaskInMemoryStore(Builder builder) {
    this.log = builder.log;
  }

  public <T extends Serializable> void save(LongTask<?> task) {
    final var method = "save";
    if (log.isDebugEnabled()) {
      log.debug(method, "request for saving in-memory task {}: {}", task.getUuid(), ObjectUtils.represent(task));
    }
    
    synchronized (publicTaskMap) {
      log.debug(method, "saving in-memory task {}", task.getUuid());
      publicTaskMap.put(task.getUuid(), task);
    }
  }

  @SuppressWarnings("unchecked")
  public <T extends Serializable> LongTask<T> get(String taskUUID) {
    final var method = "get";
    log.debug(method, "request for getting in-memory task {}", taskUUID);
    
    synchronized (publicTaskMap) {
      log.debug(method, "getting in-memory task {}", taskUUID);
      return (LongTask<T>) publicTaskMap.getOrDefault(taskUUID, null);
    }
  }

  public void delete(String taskUUID) {
    final var method = "delete";
    log.debug(method, "request for deleting in-memory task {}", taskUUID);
    
    synchronized (publicTaskMap) {
      log.debug(method, "deleting in-memory task {}", taskUUID);
      if (publicTaskMap.containsKey(taskUUID)) {
        publicTaskMap.remove(taskUUID);
      }
    }
  }

  /**
   * Creates builder to build {@link LongTaskInMemoryStore}.
   * 
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link LongTaskInMemoryStore}.
   */
  public static final class Builder {
    private CosmoLogger log;

    private Builder() {}

    public Builder withLogger(CosmoLogger log) {
      this.log = log;
      return this;
    }

    public LongTaskInMemoryStore build() {
      return new LongTaskInMemoryStore(this);
    }
  }

}

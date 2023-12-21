/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.discovery;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import it.csi.cosmo.common.discovery.model.DiscoveryRegistry;
import it.csi.cosmo.common.infinispan.exceptions.InfinispanResourceBusyException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmo.business.service.ClusterService;
import it.csi.cosmo.cosmo.dto.exception.ResourceLockException;
import it.csi.cosmo.cosmo.util.logger.LogCategory;
import it.csi.cosmo.cosmo.util.logger.LoggerFactory;

/**
 *
 */
@Component
public class DiscoveryRegistryProvider implements InitializingBean, DisposableBean {

  private static final CosmoLogger LOGGER =
      LoggerFactory.getLogger(LogCategory.DISCOVERY_LOG_CATEGORY, "DiscoveryRegistryProvider");

  private static final String CLUSTER_RESOURCE_LOCK = "cosmo_discovery_lock";

  private static final String CLUSTER_KEY_REGISTRY = "cosmo_discovery_registry";

  private static final String CLUSTER_KEY_REGISTRY_ID = "cosmo_discovery_registry_id";

  private static final String CLUSTER_KEY_REGISTRY_VERSION = "cosmo_discovery_registry_version";

  @Autowired
  private ClusterService clusterService;

  private DiscoveryRegistry registry;

  private Object registryLock;

  public DiscoveryRegistryProvider() {
    LOGGER.info("constructor", "instantiating discovery registry provider");
    registryLock = new Object();
    registry = new DiscoveryRegistry();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    LOGGER.info("afterPropertiesSet", "initializing discovery registry provider");
  }

  @Override
  public void destroy() throws Exception {
    LOGGER.info("destroy", "destroying discovery service");
  }

  public DiscoveryRegistry get() {
    executeWithRegistryLock(() -> {
      LOGGER.debug("get", "reading registry from shared cache");
      readRegistryIfNeeded();
    });
    return this.registry;
  }

  // REQUIRES PARENT SYNCH
  private void readRegistryIfNeeded() {
    String method = "readRegistryIfNeeded";

    // check if a registry is available in cache
    if (cacheHasRegistry()) {
      readRegistryFromCache();
    } else {
      // there is no registry in cache. create a new one locally
      this.registry = new DiscoveryRegistry();
      LOGGER.debug(method, "created new registry " + this.registry.getFullId());
    }
  }

  private void readRegistryFromCache() {
    final String method = "readRegistryFromCache";
    // there is a registry in cache. check if there is a local registry
    if (this.registry == null) {
      // no local register. import from cache
      readRegistryNoLocal();

    } else {
      // there is a local registry. check if registry ID matches
      if (this.registry.getRegistryId().equals(getRegistryIDFromCache())) {
        // registry ID matches. check if version matches
        if (this.registry.getVersion().equals(getRegistryVersionFromCache())) {
          // registry version matches. keep local version
          LOGGER.debug(method, "local registry version is still up-to-date");
        } else {
          // registry version is different. import from cache
          this.registry = getRegistryFromCache();
          LOGGER.debug(method, "imported registry {} to local due to remote version update",
              this.registry.getFullId());
        }
      } else {
        // registry ID mismatch. check if i am master
        if (clusterService.isMaster()) {
          // i am master
          LOGGER.warn(method,
              "master instance detected a registry ID mismatch. importing cache version.");
        } else {
          LOGGER.debug(method,
              "slave instance detected a registry ID mismatch. importing cache version.");
        }
        this.registry = getRegistryFromCache();
        LOGGER.debug(method,
            "imported registry " + this.registry.getFullId() + " to local due to ID mismatch");
      }
    }
  }

  private void readRegistryNoLocal() {
    final String method = "readRegistryNoLocal";
    this.registry = getRegistryFromCache();
    LOGGER.debug(method, "imported registry {} from remote due to missing local registry",
        this.registry.getFullId());
  }

  private boolean cacheHasRegistry() {
    Map<String, Object> cache = clusterService.getSharedCache();
    return cache.containsKey(CLUSTER_KEY_REGISTRY) && cache.containsKey(CLUSTER_KEY_REGISTRY_ID)
        && cache.containsKey(CLUSTER_KEY_REGISTRY_VERSION);
  }

  private DiscoveryRegistry getRegistryFromCache() {
    return (DiscoveryRegistry) clusterService.getSharedCache().getOrDefault(CLUSTER_KEY_REGISTRY,
        null);
  }

  private Long getRegistryVersionFromCache() {
    return (Long) clusterService.getSharedCache().getOrDefault(CLUSTER_KEY_REGISTRY_VERSION, null);
  }

  private String getRegistryIDFromCache() {
    return (String) clusterService.getSharedCache().getOrDefault(CLUSTER_KEY_REGISTRY_ID, null);
  }

  // REQUIRES PARENT SYNCH
  private void writeRegistryInCache() {
    Map<String, Object> cache = clusterService.getSharedCache();

    // ATTENZIONE: NON CAMBIARE L'ORDINE DI SCRITTURA
    cache.put(CLUSTER_KEY_REGISTRY, this.registry);
    cache.put(CLUSTER_KEY_REGISTRY_ID, this.registry.getRegistryId());
    cache.put(CLUSTER_KEY_REGISTRY_VERSION, this.registry.getVersion());
  }

  // REQUIRES PARENT SYNCH
  public void save() {
    final String method = "save";

    /*
     * needed if: - am master instance OR am not master instance but ID matches - AND version
     * differs (or null/not null)
     */
    if (this.registry == null) {
      return;
    }

    Long cacheVersion = getRegistryVersionFromCache();
    String cacheId = getRegistryIDFromCache();

    LOGGER.debug(method,
        "requested registry save for local registry " + this.registry.getFullId()
        + (cacheId != null && cacheVersion != null
        ? " over remote " + cacheId + ":" + cacheVersion
            : "over missing remote"));

    if (shouldWriteCache(cacheId, cacheVersion)) {
      writeRegistryInCache();
    }
  }

  private boolean shouldWriteCache(String cacheId, Long cacheVersion) {
    final String method = "shouldWriteCache";

    if (cacheId == null) {
      // no registry in cache
      LOGGER.info(method,
          "no registry in cache, writing local registry " + this.registry.getFullId());
    } else {
      // there is a registry in cache. check if ID matches
      if (this.registry.getRegistryId().equals(cacheId)) {
        // same ID
        if (this.registry.getVersion().equals(cacheVersion)) {
          // same version, write not needed
          LOGGER.debug(method, "same ID and version, write in cache not needed");
          return false;
        } else {
          // version changed
          LOGGER.debug(method, "same ID but version changed, write in cache is needed");
        }
      } else {
        // ID mismatch
        if (clusterService.isMaster()) {
          LOGGER.warn(method, "cached registry ID mismatch, rewriting since I am master");
        } else {
          LOGGER.warn(method,
              "cached registry ID mismatch on write, not rewriting because I am not master");
          return false;
        }
      }
    }

    return true;
  }

  public void executeWithRegistryLock(Runnable task) {

    String method = "executeWithRegistryLock";

    LOGGER.debug(method, "acquiring registry lock");
    synchronized (registryLock) {
      LOGGER.debug(method, "registry lock acquired, executing in locked context");
      try {
        task.run();
      } finally {
        LOGGER.debug(method, "registry lock released, exiting locked context");
      }
    }
  }

  public void executeWithClusterLock(Runnable task, int executionTimeout,
      int lockAcquisitionTimeout) throws InfinispanResourceBusyException {

    String method = "executeWithClusterLock";

    LOGGER.debug(method, "acquiring cluster shared lock");

    try {
      clusterService.executeLocking(CLUSTER_RESOURCE_LOCK, () -> {
        LOGGER.debug(method, "cluster shared lock acquired, executing in locked context");
        try {
          task.run();
        } finally {
          LOGGER.debug(method, "cluster shared lock released, exiting locked context");
        }
        return null;
      }, TimeUnit.MILLISECONDS, executionTimeout, lockAcquisitionTimeout);
    } catch (InfinispanResourceBusyException e) {
      LOGGER.error(method, "error acquiring shared lock on cluster", e);
      throw e;
    }
  }

  public void executeWithDoubleLock(Runnable task, int executionTimeout,
      int lockAcquisitionTimeout) {

    String method = "executeWithDoubleLock";

    LOGGER.debug(method, "acquiring double nested lock - registry lock");
    executeWithRegistryLock(() -> {
      LOGGER.debug(method,
          "acquiring double nested lock - registry lock acquired, acquiring cluster lock");
      try {
        executeWithClusterLock(() -> {
          LOGGER.debug(method, "double nested lock acquired, executing in locked context");
          try {
            task.run();
          } finally {
            LOGGER.debug(method, "double nested lock released, exiting locked context");
          }
        }, executionTimeout, lockAcquisitionTimeout);
      } catch (InfinispanResourceBusyException e) {
        throw new ResourceLockException("Error acquiring double nested lock", e);
      }
    });

  }

}

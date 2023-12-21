/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.monitoring;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.logger.CosmoLogger;

/**
 * Per-request Statistics collector
 */
public class HibernatePerRequestStatisticsCollector extends EmptyInterceptor {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private transient CosmoLogger statsLogger;

  private String loggingCategory;

  public HibernatePerRequestStatisticsCollector() {
    // NOP
  }

  private CosmoLogger getLogger() {
    if (this.statsLogger != null) {
      return this.statsLogger;
    }
    if (this.loggingCategory == null) {
      throw new RuntimeException("no logging category defined");
    }
    this.statsLogger =
        new CosmoLogger(Constants.PRODUCT + ".profiling",
        "HibernatePerRequestStatisticsCollector");
    return this.statsLogger;
  }

  public String getLoggingCategory() {
    return loggingCategory;
  }

  public void setLoggingCategory(String loggingCategory) {
    this.loggingCategory = loggingCategory;
  }

  public Map<String, Object> gatherRelevantStatistics() {
    if (!isStatsEnabled()) {
      return null;
    }
    var stats = localStats != null ? localStats.get() : null;
    if (stats == null) {
      return null;
    }

    final Map<String, Object> output = new HashMap<>();

    addIfRelevant(output, stats.flushCounter, "flushCounter", 0);
    addIfRelevant(output, stats.saveCounter, "saveCounter", 0);
    addIfRelevant(output, stats.loadCounter, "loadCounter", 0);
    addIfRelevant(output, stats.dirtyFlushCounter, "dirtyFlushCounter", 0);
    addIfRelevant(output, stats.deleteCounter, "deleteCounter", 0);
    addIfRelevant(output, stats.collectionUpdates, "collectionUpdates", 0);
    addIfRelevant(output, stats.collectionRemoves, "entityUpdateCount", 0);
    addIfRelevant(output, stats.collectionRecreates, "collectionRecreates", 0);
    addIfRelevant(output, stats.queryCount, "queryCount", 0);
    addIfRelevant(output, stats.openedTransactions, "openedTransactions", 0);
    addIfRelevant(output, stats.completedTransactions, "completedTransactions", 0);
    addIfRelevant(output, stats.committedTransactions, "committedTransactions", 0);
    addIfRelevant(output, stats.rolledBackTransactions, "rolledBackTransactions", 0);

    if (stats.queries != null && !stats.queries.isEmpty()) {
      output.put("queries",
          stats.queries.stream().collect(Collectors.toCollection(LinkedList::new)));
    }

    return output;
  }

  private <T> void addIfRelevant(Map<String, Object> output, T value, String key, T nullValue) {
    if (value == null || String.valueOf(value).equals(String.valueOf(nullValue))) {
      return;
    }
    output.put(key, value);
  }

  private boolean isStatsEnabled() {
    var l = getLogger();
    return l != null && l.isDebugEnabled();
  }

  private transient ThreadLocal<PerRequestStats> localStats =
      ThreadLocal.withInitial(PerRequestStats::new);

  public void clearCounter() {
    if (!isStatsEnabled()) {
      return;
    }

    // previously was doing just localStats.remove();
    PerRequestStats stats = localStats.get();
    if (stats != null) {
      stats.flushCounter = 0;
      stats.saveCounter = 0;
      stats.loadCounter = 0;
      stats.dirtyFlushCounter = 0;
      stats.deleteCounter = 0;
      stats.collectionUpdates = 0;
      stats.collectionRemoves = 0;
      stats.collectionRecreates = 0;
      stats.queryCount = 0;
      stats.openedTransactions = 0;
      stats.completedTransactions = 0;
      stats.committedTransactions = 0;
      stats.rolledBackTransactions = 0;
      stats.queries = new LinkedList<>();
    }
  }

  @Override
  public String onPrepareStatement(String sql) {
    if (isStatsEnabled()) {
      PerRequestStats stats = localStats.get();
      if (stats != null) {
        stats.queryCount++;
      }
    }

    var r = super.onPrepareStatement(sql);

    if (isStatsEnabled()) {
      PerRequestStats stats = localStats.get();
      stats.queries.add(r);
    }

    return r;
  }

  // Called when a Hibernate transaction is begun via the Hibernate Transaction API.
  @Override
  public void afterTransactionBegin(Transaction tx) {
    // NOP
    if (isStatsEnabled()) {
      localStats.get().openedTransactions++;
    }

    super.afterTransactionBegin(tx);
  }

  // Called after a transaction is committed or rolled back.
  @Override
  public void afterTransactionCompletion(Transaction tx) {
    // NOP
    if (isStatsEnabled()) {
      localStats.get().completedTransactions++;
      // if (tx.wasCommitted()) { // non c'e' in hibernate 5. per CMMN serve un altra versione
      // dell'interceptor
      // localStats.get().committedTransactions++;
      // } else if (tx.wasRolledBack()) {
      // localStats.get().rolledBackTransactions++;
      // }
    }

    super.afterTransactionCompletion(tx);
  }

  // Called before a collection is (re)created.
  @Override
  public void onCollectionRecreate(Object collection, Serializable key) {
    // NOP
    if (isStatsEnabled()) {
      localStats.get().collectionRecreates++;
    }

    super.onCollectionRecreate(collection, key);
  }

  // Called before a collection is deleted.
  @Override
  public void onCollectionRemove(Object collection, Serializable key) {
    // NOP
    if (isStatsEnabled()) {
      localStats.get().collectionRemoves++;
    }

    super.onCollectionRemove(collection, key);
  }

  // Called before a collection is updated.
  @Override
  public void onCollectionUpdate(Object collection, Serializable key) {
    // NOP
    if (isStatsEnabled()) {
      localStats.get().collectionUpdates++;
    }

    super.onCollectionUpdate(collection, key);
  }

  // Called before an object is deleted.
  @Override
  public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames,
      Type[] types) {
    // NOP
    if (isStatsEnabled()) {
      localStats.get().deleteCounter++;
    }

    super.onDelete(entity, id, state, propertyNames, types);
  }

  // Called when an object is detected to be dirty, during a flush.
  @Override
  public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState,
      Object[] previousState, String[] propertyNames, Type[] types) {
    // NOP
    if (isStatsEnabled()) {
      localStats.get().dirtyFlushCounter++;
    }

    return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
  }

  // Called just before an object is initialized.
  @Override
  public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames,
      Type[] types) {
    // NOP
    if (isStatsEnabled()) {
      localStats.get().loadCounter++;
    }

    return super.onLoad(entity, id, state, propertyNames, types);
  }

  // Called before an object is saved.
  @Override
  public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames,
      Type[] types) {
    // NOP
    if (isStatsEnabled()) {
      localStats.get().saveCounter++;
    }

    return super.onSave(entity, id, state, propertyNames, types);
  }

  // Called after a flush that actually ends in execution of the SQL statements required to
  // synchronize in-memory state with the database.
  @Override
  public void postFlush(Iterator entities) {
    // NOP
    if (isStatsEnabled()) {
      localStats.get().flushCounter++;
    }

    super.postFlush(entities);
  }

  private static class PerRequestStats {
    long flushCounter = 0;
    long saveCounter = 0;
    long loadCounter = 0;
    long dirtyFlushCounter = 0;
    long deleteCounter = 0;
    long collectionUpdates = 0;
    long collectionRemoves = 0;
    long collectionRecreates = 0;
    long queryCount = 0;
    long openedTransactions = 0;
    long completedTransactions = 0;
    long committedTransactions = 0;
    long rolledBackTransactions = 0;
    List<String> queries = new LinkedList<>();
  }
}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.monitoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.CollectionStatistics;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.NaturalIdCacheStatistics;
import org.hibernate.stat.QueryStatistics;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Statistics collector
 */
public class StatisticsCollector implements Statistics {

  @Autowired
  private EntityManagerFactory entityManagerFactory;

  public StatisticsCollector() {
    super();
  }

  /**
   * @param entityManagerFactory
   */
  public StatisticsCollector(EntityManagerFactory entityManagerFactory) {
    super();
    this.entityManagerFactory = entityManagerFactory;
  }

  private Statistics stats() {
    SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    return sessionFactory.getStatistics();
  }

  @Override
  public boolean isStatisticsEnabled() {
    return stats().isStatisticsEnabled();
  }

  public Map<String, Object> gatherRelevantStatistics() {
    var stats = stats();
    if (stats == null) {
      return null;
    }

    final Map<String, Object> output = new HashMap<>();
    addIfRelevant(output, stats.getSessionOpenCount(), "sessionOpenCount", 0);
    addIfRelevant(output, stats.getSessionCloseCount(), "sessionCloseCount", 0);
    addIfRelevant(output, stats.getFlushCount(), "flushCount", 0);
    addIfRelevant(output, stats.getConnectCount(), "connectCount", 0);
    addIfRelevant(output, stats.getPrepareStatementCount(), "prepareStatementCount", 0);
    addIfRelevant(output, stats.getCloseStatementCount(), "closeStatementCount", 0);
    addIfRelevant(output, stats.getEntityLoadCount(), "entityLoadCount", 0);
    addIfRelevant(output, stats.getEntityUpdateCount(), "entityUpdateCount", 0);
    addIfRelevant(output, stats.getEntityInsertCount(), "entityInsertCount", 0);
    addIfRelevant(output, stats.getEntityDeleteCount(), "entityDeleteCount", 0);
    addIfRelevant(output, stats.getEntityFetchCount(), "entityFetchCount", 0);
    addIfRelevant(output, stats.getCollectionLoadCount(), "collectionLoadCount", 0);
    addIfRelevant(output, stats.getCollectionUpdateCount(), "collectionUpdateCount", 0);
    addIfRelevant(output, stats.getCollectionRemoveCount(), "collectionRemoveCount", 0);
    addIfRelevant(output, stats.getCollectionRecreateCount(), "collectionRecreateCount", 0);
    addIfRelevant(output, stats.getCollectionFetchCount(), "collectionFetchCount", 0);
    addIfRelevant(output, stats.getSecondLevelCacheHitCount(), "secondLevelCacheHitCount", 0);
    addIfRelevant(output, stats.getSecondLevelCacheMissCount(), "secondLevelCacheMissCount", 0);
    addIfRelevant(output, stats.getSecondLevelCachePutCount(), "secondLevelCachePutCount", 0);
    addIfRelevant(output, stats.getNaturalIdCacheHitCount(), "naturalIdCacheHitCount", 0);
    addIfRelevant(output, stats.getNaturalIdCacheMissCount(), "naturalIdCacheMissCount", 0);
    addIfRelevant(output, stats.getNaturalIdCachePutCount(), "naturalIdCachePutCount", 0);
    addIfRelevant(output, stats.getNaturalIdQueryExecutionCount(), "naturalIdQueryExecutionCount",
        0);
    addIfRelevant(output, stats.getNaturalIdQueryExecutionMaxTime(),
        "naturalIdQueryExecutionMaxTime", 0);
    addIfRelevant(output, stats.getNaturalIdQueryExecutionMaxTimeRegion(),
        "naturalIdQueryExecutionMaxTimeRegion", null);
    addIfRelevant(output, stats.getQueryExecutionCount(), "queryExecutionCount", 0);
    addIfRelevant(output, stats.getQueryExecutionMaxTime(), "queryExecutionMaxTime", 0);
    addIfRelevant(output, stats.getQueryExecutionMaxTimeQueryString(),
        "queryExecutionMaxTimeQueryString", 0);
    addIfRelevant(output, stats.getQueryCacheHitCount(), "queryCacheHitCount", 0);
    addIfRelevant(output, stats.getQueryCacheMissCount(), "queryCacheMissCount", 0);
    addIfRelevant(output, stats.getQueryCachePutCount(), "queryCachePutCount", 0);
    addIfRelevant(output, stats.getUpdateTimestampsCacheHitCount(), "updateTimestampsCacheHitCount",
        0);
    addIfRelevant(output, stats.getUpdateTimestampsCacheMissCount(),
        "updateTimestampsCacheMissCount", 0);
    addIfRelevant(output, stats.getUpdateTimestampsCachePutCount(), "updateTimestampsCachePutCount",
        0);
    addIfRelevant(output, stats.getTransactionCount(), "transactionCount", 0);
    addIfRelevant(output, stats.getOptimisticFailureCount(), "optimisticFailureCount", 0);
    addIfRelevant(output, stats.getQueries(), "queries", 0);
    addIfRelevant(output, stats.getSuccessfulTransactionCount(), "successfulTransactionCount", 0);

    if (stats.getQueries() != null && stats.getQueries().length > 0) {
      List<Map<String, Object>> queryStats = new ArrayList<>();
      for (String query: stats.getQueries()) {
        var singleQueryStats = stats.getQueryStatistics(query);
        Map<String, Object> entry = new HashMap<>();
        entry.put("query", query);
        entry.put("statistics", singleQueryStats);
        queryStats.add(entry);
      }
      output.put("queryStats", queryStats);
    }
    
    return output;
  }

  private <T> void addIfRelevant(Map<String, Object> output, T value, String key, T nullValue) {
    if (value == null || String.valueOf(value).equals(String.valueOf(nullValue))) {
      return;
    }
    output.put(key, value);
  }

  @Override
  public void setStatisticsEnabled(boolean b) {
    // NOT SUPPORTED
  }

  @Override
  public void clear() {
    // NOT SUPPORTED
  }

  @Override
  public void logSummary() {
    // NOT SUPPORTED
  }

  public Statistics getRawStatistics() {
    return stats();
  }

  @Override
  public EntityStatistics getEntityStatistics(String entityName) {
    return stats().getEntityStatistics(entityName);
  }

  @Override
  public CollectionStatistics getCollectionStatistics(String role) {
    // NOT SUPPORTED
    return null;
  }

  @Override
  public QueryStatistics getQueryStatistics(String queryString) {
    // NOT SUPPORTED
    return null;
  }

  @Override
  public long getEntityDeleteCount() {
    return stats().getEntityDeleteCount();
  }

  @Override
  public long getEntityInsertCount() {
    return stats().getEntityInsertCount();
  }

  @Override
  public long getEntityLoadCount() {
    return stats().getEntityLoadCount();
  }

  @Override
  public long getEntityFetchCount() {
    return stats().getEntityFetchCount();
  }

  @Override
  public long getEntityUpdateCount() {
    return stats().getEntityUpdateCount();
  }

  @Override
  public long getQueryExecutionCount() {
    return stats().getQueryExecutionCount();
  }

  @Override
  public long getQueryExecutionMaxTime() {
    return stats().getQueryExecutionMaxTime();
  }

  @Override
  public String getQueryExecutionMaxTimeQueryString() {
    return stats().getQueryExecutionMaxTimeQueryString();
  }

  @Override
  public long getQueryCacheHitCount() {
    return stats().getQueryCacheHitCount();
  }

  @Override
  public long getQueryCacheMissCount() {
    return stats().getQueryCacheMissCount();
  }

  @Override
  public long getQueryCachePutCount() {
    return stats().getQueryCachePutCount();
  }

  @Override
  public long getNaturalIdQueryExecutionCount() {
    return stats().getNaturalIdQueryExecutionCount();
  }

  @Override
  public long getNaturalIdQueryExecutionMaxTime() {
    return stats().getNaturalIdQueryExecutionMaxTime();
  }

  @Override
  public String getNaturalIdQueryExecutionMaxTimeRegion() {
    return stats().getNaturalIdQueryExecutionMaxTimeRegion();
  }

  @Override
  public long getNaturalIdCacheHitCount() {
    return stats().getNaturalIdCacheHitCount();
  }

  @Override
  public long getNaturalIdCacheMissCount() {
    return stats().getNaturalIdCacheMissCount();
  }

  @Override
  public long getNaturalIdCachePutCount() {
    return stats().getNaturalIdCachePutCount();
  }

  @Override
  public long getUpdateTimestampsCacheHitCount() {
    return stats().getUpdateTimestampsCacheHitCount();
  }

  @Override
  public long getUpdateTimestampsCacheMissCount() {
    return stats().getUpdateTimestampsCacheMissCount();
  }

  @Override
  public long getUpdateTimestampsCachePutCount() {
    return stats().getUpdateTimestampsCachePutCount();
  }

  @Override
  public long getFlushCount() {
    return stats().getFlushCount();
  }

  @Override
  public long getConnectCount() {
    return stats().getConnectCount();
  }

  @Override
  public long getSecondLevelCacheHitCount() {
    return stats().getSecondLevelCacheHitCount();
  }

  @Override
  public long getSecondLevelCacheMissCount() {
    return stats().getSecondLevelCacheMissCount();
  }

  @Override
  public long getSecondLevelCachePutCount() {
    return stats().getSecondLevelCachePutCount();
  }

  @Override
  public long getSessionCloseCount() {
    return stats().getSessionCloseCount();
  }

  @Override
  public long getSessionOpenCount() {
    return stats().getSessionOpenCount();
  }

  @Override
  public long getCollectionLoadCount() {
    return stats().getCollectionLoadCount();
  }

  @Override
  public long getCollectionFetchCount() {
    return stats().getCollectionFetchCount();
  }

  @Override
  public long getCollectionUpdateCount() {
    return stats().getCollectionUpdateCount();
  }

  @Override
  public long getCollectionRemoveCount() {
    return stats().getCollectionRemoveCount();
  }

  @Override
  public long getCollectionRecreateCount() {
    return stats().getCollectionRecreateCount();
  }

  @Override
  public long getStartTime() {
    return stats().getStartTime();
  }

  @Override
  public String[] getQueries() {
    return stats().getQueries();
  }

  @Override
  public String[] getEntityNames() {
    return stats().getEntityNames();
  }

  @Override
  public String[] getCollectionRoleNames() {
    return stats().getCollectionRoleNames();
  }

  @Override
  public String[] getSecondLevelCacheRegionNames() {
    return stats().getSecondLevelCacheRegionNames();
  }

  @Override
  public long getSuccessfulTransactionCount() {
    return stats().getSuccessfulTransactionCount();
  }

  @Override
  public long getTransactionCount() {
    return stats().getTransactionCount();
  }

  @Override
  public long getPrepareStatementCount() {
    return stats().getPrepareStatementCount();
  }

  @Override
  public long getCloseStatementCount() {
    return stats().getCloseStatementCount();
  }

  @Override
  public long getOptimisticFailureCount() {
    return stats().getOptimisticFailureCount();
  }

  @Override
  public SecondLevelCacheStatistics getSecondLevelCacheStatistics(String regionName) {
    return stats().getSecondLevelCacheStatistics(regionName);
  }

  @Override
  public NaturalIdCacheStatistics getNaturalIdCacheStatistics(String regionName) {
    return stats().getNaturalIdCacheStatistics(regionName);
  }
}

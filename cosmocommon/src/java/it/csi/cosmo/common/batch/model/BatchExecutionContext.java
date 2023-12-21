/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.batch.model;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */

/**
 *
 *
 */
public class BatchExecutionContext {

  private String batchName;

  private LocalDateTime startedAt;

  private List<BatchReportedEvent> events;

  private BatchExecutionContext(Builder builder) {
    this.batchName = builder.batchName;
    this.startedAt = builder.startedAt;
    this.events = new LinkedList<>();
  }

  public BatchExecutionContext() {
    // Auto-generated constructor stub
  }

  public void reportError(String message, Throwable error) {
    this.events.add(BatchReportedEvent.builder()
        .withTimestamp(LocalDateTime.now())
        .withMessage(message)
        .withError(error)
        .withLevel(BatchReportedEventLevel.ERROR)
        .build());
  }

  public void reportError(String message) {
    this.events.add(BatchReportedEvent.builder()
        .withTimestamp(LocalDateTime.now())
        .withMessage(message)
        .withLevel(BatchReportedEventLevel.ERROR)
        .build());
  }

  public void reportWarning(String message, Throwable error) {
    this.events.add(BatchReportedEvent.builder()
        .withTimestamp(LocalDateTime.now())
        .withMessage(message)
        .withError(error)
        .withLevel(BatchReportedEventLevel.WARNING)
        .build());
  }

  public void reportWarning(String message) {
    this.events.add(BatchReportedEvent.builder()
        .withTimestamp(LocalDateTime.now())
        .withMessage(message)
        .withLevel(BatchReportedEventLevel.WARNING)
        .build());
  }

  public void reportInfo(String message) {
    this.events.add(BatchReportedEvent.builder()
        .withTimestamp(LocalDateTime.now())
        .withMessage(message)
        .withLevel(BatchReportedEventLevel.INFO)
        .build());
  }

  public LocalDateTime getStartedAt() {
    return startedAt;
  }

  public List<BatchReportedEvent> getEvents() {
    return events;
  }

  public String getBatchName() {
    return batchName;
  }

  /**
   * Creates builder to build {@link BatchExecutionContext}.
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link BatchExecutionContext}.
   */
  public static final class Builder {
    private String batchName;
    private LocalDateTime startedAt;

    private Builder() {}

    public Builder withBatchName(String batchName) {
      this.batchName = batchName;
      return this;
    }

    public Builder withStartedAt(LocalDateTime startedAt) {
      this.startedAt = startedAt;
      return this;
    }

    public BatchExecutionContext build() {
      return new BatchExecutionContext(this);
    }
  }

}

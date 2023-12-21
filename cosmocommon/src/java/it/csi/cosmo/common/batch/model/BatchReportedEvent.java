/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.batch.model;

import java.time.LocalDateTime;

/**
 *
 */
public class BatchReportedEvent {

  private BatchReportedEventLevel level;

  private String message;

  private Throwable error;

  private LocalDateTime timestamp;

  private BatchReportedEvent(Builder builder) {
    this.level = builder.level;
    this.message = builder.message;
    this.error = builder.error;
    this.timestamp = builder.timestamp;
  }

  public BatchReportedEvent() {
    // Auto-generated constructor stub
  }

  public String getMessage() {
    return message;
  }

  public Throwable getError() {
    return error;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public BatchReportedEventLevel getLevel() {
    return level;
  }

  /**
   * Creates builder to build {@link BatchReportedEvent}.
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link BatchReportedEvent}.
   */
  public static final class Builder {
    private BatchReportedEventLevel level;
    private String message;
    private Throwable error;
    private LocalDateTime timestamp;

    private Builder() {}

    public Builder withLevel(BatchReportedEventLevel level) {
      this.level = level;
      return this;
    }

    public Builder withMessage(String message) {
      this.message = message;
      return this;
    }

    public Builder withError(Throwable error) {
      this.error = error;
      return this;
    }

    public Builder withTimestamp(LocalDateTime timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public BatchReportedEvent build() {
      return new BatchReportedEvent(this);
    }
  }


}

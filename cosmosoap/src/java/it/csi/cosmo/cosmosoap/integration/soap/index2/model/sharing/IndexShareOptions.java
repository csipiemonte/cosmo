/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing;

import java.time.OffsetDateTime;

/**
 *
 */

public class IndexShareOptions {

  private String filename;
  private IndexContentDisposition contentDisposition;
  private IndexShareScope source;
  private OffsetDateTime fromDate;
  private OffsetDateTime toDate;

  private IndexShareOptions(Builder builder) {
    this.filename = builder.filename;
    this.contentDisposition = builder.contentDisposition;
    this.source = builder.source;
    this.fromDate = builder.fromDate;
    this.toDate = builder.toDate;
  }

  public IndexShareOptions() {
    // NOP
  }

  public String getFilename() {
    return filename;
  }

  public IndexContentDisposition getContentDisposition() {
    return contentDisposition;
  }

  public IndexShareScope getSource() {
    return source;
  }

  public OffsetDateTime getFromDate() {
    return fromDate;
  }

  public OffsetDateTime getToDate() {
    return toDate;
  }

  @Override
  public String toString() {
    return "IndexShareOptions [" + (source != null ? "source=" + source + ", " : "")
        + (fromDate != null ? "fromDate=" + fromDate + ", " : "")
        + (toDate != null ? "toDate=" + toDate : "") + "]";
  }

  /**
   * Creates builder to build {@link IndexShareOptions}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link IndexShareOptions}.
   */
  public static final class Builder {
    private String filename;
    private IndexContentDisposition contentDisposition;
    private IndexShareScope source;
    private OffsetDateTime fromDate;
    private OffsetDateTime toDate;

    private Builder() {}

    public Builder withFilename(String filename) {
      this.filename = filename;
      return this;
    }

    public Builder withContentDisposition(IndexContentDisposition contentDisposition) {
      this.contentDisposition = contentDisposition;
      return this;
    }

    public Builder withSource(IndexShareScope source) {
      this.source = source;
      return this;
    }

    public Builder withFromDate(OffsetDateTime fromDate) {
      this.fromDate = fromDate;
      return this;
    }

    public Builder withToDate(OffsetDateTime toDate) {
      this.toDate = toDate;
      return this;
    }

    public IndexShareOptions build() {
      return new IndexShareOptions(this);
    }
  }
}

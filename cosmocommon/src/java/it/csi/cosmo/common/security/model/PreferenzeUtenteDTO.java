/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.security.model;

import java.io.Serializable;

public class PreferenzeUtenteDTO implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1285182471L;

  private Integer maxPageSize;

  private PreferenzeUtenteDTO(Builder builder) {
    this.maxPageSize = builder.maxPageSize;
  }

  public PreferenzeUtenteDTO() {}

  public Integer getMaxPageSize() {
    return maxPageSize;
  }

  public void setMaxPageSize(Integer maxPageSize) {
    this.maxPageSize = maxPageSize;
  }

  @Override
  public String toString() {
    return "PreferenzeUtenteDTO [" + (maxPageSize != null ? "maxPageSize=" + maxPageSize : "")
        + "]";
  }

  /**
   * Creates builder to build {@link PreferenzeUtenteDTO}.
   * 
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link PreferenzeUtenteDTO}.
   */
  public static final class Builder {
    private Integer maxPageSize;

    private Builder() {}

    public Builder withMaxPageSize(Integer maxPageSize) {
      this.maxPageSize = maxPageSize;
      return this;
    }

    public PreferenzeUtenteDTO build() {
      return new PreferenzeUtenteDTO(this);
    }
  }


}

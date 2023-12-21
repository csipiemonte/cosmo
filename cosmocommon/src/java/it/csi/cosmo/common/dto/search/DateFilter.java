/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.dto.search;

import java.time.LocalDate;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 *
 */
public class DateFilter extends AbstractFilter<LocalDate> {

  @JsonProperty("lt")
  protected LocalDate lessThan;

  @JsonProperty("lte")
  protected LocalDate lessThanOrEqualTo;

  @JsonProperty("gt")
  protected LocalDate greaterThan;

  @JsonProperty("gte")
  protected LocalDate greaterThanOrEqualTo;

  public DateFilter() {
    // Auto-generated constructor stub
  }

  @Override
  public boolean isEmpty() {
    if (!super.isEmpty()) {
      return false;
    }
    return lessThan == null && lessThanOrEqualTo == null && greaterThan == null
        && greaterThanOrEqualTo == null;
  }

  public LocalDate getLessThan() {
    return lessThan;
  }

  public void setLessThan(LocalDate lessThan) {
    this.lessThan = lessThan;
  }

  public LocalDate getLessThanOrEqualTo() {
    return lessThanOrEqualTo;
  }

  public void setLessThanOrEqualTo(LocalDate lessThanOrEqualTo) {
    this.lessThanOrEqualTo = lessThanOrEqualTo;
  }

  public LocalDate getGreaterThan() {
    return greaterThan;
  }

  public void setGreaterThan(LocalDate greaterThan) {
    this.greaterThan = greaterThan;
  }

  public LocalDate getGreaterThanOrEqualTo() {
    return greaterThanOrEqualTo;
  }

  public void setGreaterThanOrEqualTo(LocalDate greaterThanOrEqualTo) {
    this.greaterThanOrEqualTo = greaterThanOrEqualTo;
  }

  @Override
  public String toString() {
    return "DateFilter [lessThan=" + lessThan + ", lessThanOrEqualTo=" + lessThanOrEqualTo
        + ", greaterThan=" + greaterThan + ", greaterThanOrEqualTo=" + greaterThanOrEqualTo
        + ", equals=" + equals + ", notEquals=" + notEquals + ", in=" + Arrays.toString(in)
        + ", notIn=" + Arrays.toString(notIn) + ", defined=" + defined + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((greaterThan == null) ? 0 : greaterThan.hashCode());
    result =
        prime * result + ((greaterThanOrEqualTo == null) ? 0 : greaterThanOrEqualTo.hashCode());
    result = prime * result + ((lessThan == null) ? 0 : lessThan.hashCode());
    result = prime * result + ((lessThanOrEqualTo == null) ? 0 : lessThanOrEqualTo.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    DateFilter other = (DateFilter) obj;
    if (greaterThan == null) {
      if (other.greaterThan != null)
        return false;
    } else if (!greaterThan.equals(other.greaterThan))
      return false;
    if (greaterThanOrEqualTo == null) {
      if (other.greaterThanOrEqualTo != null)
        return false;
    } else if (!greaterThanOrEqualTo.equals(other.greaterThanOrEqualTo))
      return false;
    if (lessThan == null) {
      if (other.lessThan != null)
        return false;
    } else if (!lessThan.equals(other.lessThan))
      return false;
    if (lessThanOrEqualTo == null) {
      if (other.lessThanOrEqualTo != null)
        return false;
    } else if (!lessThanOrEqualTo.equals(other.lessThanOrEqualTo))
      return false;
    return true;
  }

}

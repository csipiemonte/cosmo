/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.dto.search;

import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 *
 */
public abstract class AbstractNumericFilter<T extends Comparable<T>> extends AbstractFilter<T> {

  @JsonProperty("lt")
  protected T lessThan;

  @JsonProperty("lte")
  protected T lessThanOrEqualTo;

  @JsonProperty("gt")
  protected T greaterThan;

  @JsonProperty("gte")
  protected T greaterThanOrEqualTo;

  public AbstractNumericFilter() {
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

  public T getLessThan() {
    return lessThan;
  }

  public void setLessThan(T lessThan) {
    this.lessThan = lessThan;
  }

  public T getLessThanOrEqualTo() {
    return lessThanOrEqualTo;
  }

  public void setLessThanOrEqualTo(T lessThanOrEqualTo) {
    this.lessThanOrEqualTo = lessThanOrEqualTo;
  }

  public T getGreaterThan() {
    return greaterThan;
  }

  public void setGreaterThan(T greaterThan) {
    this.greaterThan = greaterThan;
  }

  public T getGreaterThanOrEqualTo() {
    return greaterThanOrEqualTo;
  }

  public void setGreaterThanOrEqualTo(T greaterThanOrEqualTo) {
    this.greaterThanOrEqualTo = greaterThanOrEqualTo;
  }

  @Override
  public String toString() {
    return "AbstractNumericFilter [lessThan=" + lessThan + ", lessThanOrEqualTo="
        + lessThanOrEqualTo + ", greaterThan=" + greaterThan + ", greaterThanOrEqualTo="
        + greaterThanOrEqualTo + ", equals=" + equals + ", notEquals=" + notEquals + ", in="
        + Arrays.toString(in) + ", notIn=" + Arrays.toString(notIn) + ", defined=" + defined + "]";
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
  public boolean equals(Object obj) { // NOSONAR
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    AbstractNumericFilter<?> other = (AbstractNumericFilter<?>) obj;
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

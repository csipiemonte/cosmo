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
public abstract class AbstractFilter<T> {

  @JsonProperty("eq")
  protected T equals;

  @JsonProperty("ne")
  protected T notEquals;

  @JsonProperty("in")
  protected T[] in;

  @JsonProperty("nin")
  protected T[] notIn;

  @JsonProperty("defined")
  protected Boolean defined;

  public boolean isEmpty() {
    return !(defined != null || equals != null || notEquals != null || in != null || notIn != null);
  }

  public AbstractFilter() {
    // Auto-generated constructor stub
  }

  public T getEquals() {
    return equals;
  }

  public void setEquals(T equals) {
    this.equals = equals;
  }

  public T getNotEquals() {
    return notEquals;
  }

  public void setNotEquals(T notEquals) {
    this.notEquals = notEquals;
  }

  public T[] getIn() {
    return in;
  }

  public void setIn(T[] in) {
    this.in = in;
  }

  public T[] getNotIn() {
    return notIn;
  }

  public void setNotIn(T[] notIn) {
    this.notIn = notIn;
  }

  public Boolean getDefined() {
    return defined;
  }

  public void setDefined(Boolean defined) {
    this.defined = defined;
  }

  @Override
  public String toString() {
    return "AbstractFilter [equals=" + equals + ", notEquals=" + notEquals + ", in="
        + Arrays.toString(in) + ", notIn=" + Arrays.toString(notIn) + ", defined=" + defined + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (defined ? 1231 : 1237);
    result = prime * result + ((equals == null) ? 0 : equals.hashCode());
    result = prime * result + Arrays.deepHashCode(in);
    result = prime * result + ((notEquals == null) ? 0 : notEquals.hashCode());
    result = prime * result + Arrays.deepHashCode(notIn);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AbstractFilter<?> other = (AbstractFilter<?>) obj;
    if (defined != other.defined)
      return false;
    if (equals == null) {
      if (other.equals != null)
        return false;
    } else if (!equals.equals(other.equals))
      return false;
    if (!Arrays.deepEquals(in, other.in))
      return false;
    if (notEquals == null) {
      if (other.notEquals != null)
        return false;
    } else if (!notEquals.equals(other.notEquals))
      return false;
    if (!Arrays.deepEquals(notIn, other.notIn)) // NOSONAR
      return false;
    return true;
  }

}

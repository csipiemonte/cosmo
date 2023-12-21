/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.dto.search;

import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 *
 */
public class StringFilter extends AbstractFilter<String> {

  @JsonProperty("c")
  protected String contains;

  @JsonProperty("ci")
  protected String containsIgnoreCase;

  @JsonProperty("nc")
  protected String notContains;

  @JsonProperty("nci")
  protected String notContainsIgnoreCase;

  @JsonProperty("lk")
  protected String like;

  @JsonProperty("nlk")
  protected String notLike;

  @JsonProperty("eqic")
  protected String equalsIgnoreCase;

  @JsonProperty("neic")
  protected String notEqualsIgnoreCase;

  @JsonProperty("inic")
  protected String[] inIgnoreCase;

  @JsonProperty("ninic")
  protected String[] notInIgnoreCase;

  @JsonProperty("s")
  protected String startsWith;

  @JsonProperty("e")
  protected String endsWith;

  @JsonProperty("si")
  protected String startsWithIgnoreCase;

  @JsonProperty("ei")
  protected String endsWithIgnoreCase;


  public StringFilter() {
    // Auto-generated constructor stub
  }

  @Override
  public boolean isEmpty() {
    if (!super.isEmpty()) {
      return false;
    }
    return StringUtils.isEmpty(contains) && StringUtils.isEmpty(containsIgnoreCase)
        && StringUtils.isEmpty(notContains) && StringUtils.isEmpty(notContainsIgnoreCase)
        && StringUtils.isEmpty(like) && StringUtils.isEmpty(notLike)
        && StringUtils.isEmpty(equalsIgnoreCase) && StringUtils.isEmpty(notEqualsIgnoreCase)
        && inIgnoreCase == null && notInIgnoreCase == null && StringUtils.isEmpty(startsWith)
        && StringUtils.isEmpty(endsWith) && StringUtils.isEmpty(startsWithIgnoreCase)
        && StringUtils.isEmpty(endsWithIgnoreCase);
  }

  public String getLike() {
    return like;
  }

  public void setLike(String like) {
    this.like = like;
  }

  public String getNotLike() {
    return notLike;
  }

  public void setNotLike(String notLike) {
    this.notLike = notLike;
  }

  public String getContains() {
    return contains;
  }

  public void setContains(String contains) {
    this.contains = contains;
  }

  public String getContainsIgnoreCase() {
    return containsIgnoreCase;
  }

  public void setContainsIgnoreCase(String containsIgnoreCase) {
    this.containsIgnoreCase = containsIgnoreCase;
  }

  public String getNotContains() {
    return notContains;
  }

  public void setNotContains(String notContains) {
    this.notContains = notContains;
  }

  public String getNotContainsIgnoreCase() {
    return notContainsIgnoreCase;
  }

  public void setNotContainsIgnoreCase(String notContainsIgnoreCase) {
    this.notContainsIgnoreCase = notContainsIgnoreCase;
  }

  public String getEqualsIgnoreCase() {
    return equalsIgnoreCase;
  }

  public void setEqualsIgnoreCase(String equalsIgnoreCase) {
    this.equalsIgnoreCase = equalsIgnoreCase;
  }

  public String getNotEqualsIgnoreCase() {
    return notEqualsIgnoreCase;
  }

  public void setNotEqualsIgnoreCase(String notEqualsIgnoreCase) {
    this.notEqualsIgnoreCase = notEqualsIgnoreCase;
  }

  public String[] getInIgnoreCase() {
    return inIgnoreCase;
  }

  public void setInIgnoreCase(String[] inIgnoreCase) {
    this.inIgnoreCase = inIgnoreCase;
  }

  public String[] getNotInIgnoreCase() {
    return notInIgnoreCase;
  }

  public void setNotInIgnoreCase(String[] notInIgnoreCase) {
    this.notInIgnoreCase = notInIgnoreCase;
  }

  public String getStartsWith() {
    return startsWith;
  }

  public void setStartsWith(String startsWith) {
    this.startsWith = startsWith;
  }

  public String getEndsWith() {
    return endsWith;
  }

  public void setEndsWith(String endsWith) {
    this.endsWith = endsWith;
  }

  public String getStartsWithIgnoreCase() {
    return startsWithIgnoreCase;
  }

  public void setStartsWithIgnoreCase(String startsWithIgnoreCase) {
    this.startsWithIgnoreCase = startsWithIgnoreCase;
  }

  public String getEndsWithIgnoreCase() {
    return endsWithIgnoreCase;
  }

  public void setEndsWithIgnoreCase(String endsWithIgnoreCase) {
    this.endsWithIgnoreCase = endsWithIgnoreCase;
  }

  @Override
  public String toString() {
    return "StringFilter [contains=" + contains + ", containsIgnoreCase=" + containsIgnoreCase
        + ", notContains=" + notContains + ", notContainsIgnoreCase=" + notContainsIgnoreCase
        + ", like=" + like + ", notLike=" + notLike + ", equalsIgnoreCase=" + equalsIgnoreCase
        + ", notEqualsIgnoreCase=" + notEqualsIgnoreCase + ", inIgnoreCase="
        + Arrays.toString(inIgnoreCase) + ", notInIgnoreCase=" + Arrays.toString(notInIgnoreCase)
        + ", startsWith=" + startsWith + ", endsWith=" + endsWith
        + ", startsWithIgnoreCase=" + startsWithIgnoreCase + ", endsWithIgnoreCase="
        + endsWithIgnoreCase
        + "]";

  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((contains == null) ? 0 : contains.hashCode());
    result = prime * result + ((containsIgnoreCase == null) ? 0 : containsIgnoreCase.hashCode());
    result = prime * result + ((equalsIgnoreCase == null) ? 0 : equalsIgnoreCase.hashCode());
    result = prime * result + Arrays.hashCode(inIgnoreCase);
    result = prime * result + ((like == null) ? 0 : like.hashCode());
    result = prime * result + ((notContains == null) ? 0 : notContains.hashCode());
    result =
        prime * result + ((notContainsIgnoreCase == null) ? 0 : notContainsIgnoreCase.hashCode());
    result = prime * result + ((notEqualsIgnoreCase == null) ? 0 : notEqualsIgnoreCase.hashCode());
    result = prime * result + Arrays.hashCode(notInIgnoreCase);
    result = prime * result + ((notLike == null) ? 0 : notLike.hashCode());
    result = prime * result + ((startsWith == null) ? 0 : startsWith.hashCode());
    result = prime * result + ((endsWith == null) ? 0 : endsWith.hashCode());
    result =
        prime * result + ((startsWithIgnoreCase == null) ? 0 : startsWithIgnoreCase.hashCode());
    result = prime * result + ((endsWithIgnoreCase == null) ? 0 : endsWithIgnoreCase.hashCode());

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
    StringFilter other = (StringFilter) obj;
    if (contains == null) {
      if (other.contains != null)
        return false;
    } else if (!contains.equals(other.contains))
      return false;
    if (containsIgnoreCase == null) {
      if (other.containsIgnoreCase != null)
        return false;
    } else if (!containsIgnoreCase.equals(other.containsIgnoreCase))
      return false;
    if (equalsIgnoreCase == null) {
      if (other.equalsIgnoreCase != null)
        return false;
    } else if (!equalsIgnoreCase.equals(other.equalsIgnoreCase))
      return false;
    if (!Arrays.equals(inIgnoreCase, other.inIgnoreCase))
      return false;
    if (like == null) {
      if (other.like != null)
        return false;
    } else if (!like.equals(other.like))
      return false;
    if (notContains == null) {
      if (other.notContains != null)
        return false;
    } else if (!notContains.equals(other.notContains))
      return false;
    if (notContainsIgnoreCase == null) {
      if (other.notContainsIgnoreCase != null)
        return false;
    } else if (!notContainsIgnoreCase.equals(other.notContainsIgnoreCase))
      return false;
    if (notEqualsIgnoreCase == null) {
      if (other.notEqualsIgnoreCase != null)
        return false;
    } else if (!notEqualsIgnoreCase.equals(other.notEqualsIgnoreCase))
      return false;
    if (startsWith == null) {
      if (other.startsWith != null)
        return false;
    } else if (!startsWith.equals(other.startsWith))
      return false;
    if (endsWith == null) {
      if (other.endsWith != null)
        return false;
    } else if (!endsWith.equals(other.endsWith))
      return false;
    if (startsWithIgnoreCase == null) {
      if (other.startsWithIgnoreCase != null)
        return false;
    } else if (!startsWithIgnoreCase.equals(other.startsWithIgnoreCase))
      return false;
    if (endsWithIgnoreCase == null) {
      if (other.endsWithIgnoreCase != null)
        return false;
    } else if (!endsWithIgnoreCase.equals(other.endsWithIgnoreCase))
      return false;
    if (!Arrays.equals(notInIgnoreCase, other.notInIgnoreCase))
      return false;
    if (notLike == null) {
      if (other.notLike != null)
        return false;
    } else if (!notLike.equals(other.notLike))
      return false;
    return true;
  }

}

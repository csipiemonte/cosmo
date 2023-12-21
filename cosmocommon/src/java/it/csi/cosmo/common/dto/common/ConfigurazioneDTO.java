/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.dto.common;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import it.csi.cosmo.common.exception.InternalServerException;


public class ConfigurazioneDTO implements Serializable, Comparable<ConfigurazioneDTO> {

  private static final long serialVersionUID = -8632080607351435058L;

  private String key;

  private String value;

  public ConfigurazioneDTO() {
    // NOP
  }

  private ConfigurazioneDTO(Builder builder) {
    key = builder.key;
    value = builder.value;
  }

  public boolean isEmpty() {
    return (value == null || value.trim().isEmpty());
  }

  public boolean asBool() {
    return asBoolean(null).booleanValue();
  }

  public Long asLong(Long defaultValue) {
    if (isEmpty()) {
      return defaultValue;
    } else {
      return Long.valueOf(trimmed());
    }
  }

  public Integer asInteger(Integer defaultValue) {
    if (isEmpty()) {
      return defaultValue;
    } else {
      return Integer.valueOf(trimmed());
    }
  }

  public Double asDouble(Double defaultValue) {
    if (isEmpty()) {
      return defaultValue;
    } else {
      return Double.valueOf(trimmed());
    }
  }

  public Boolean asBoolean(Boolean defaultValue) {
    if (isEmpty()) {
      return defaultValue;
    } else {
      String trimmed = trimmed();
      if (trimmed == null) {
        return defaultValue;
      }
      trimmed = trimmed.toLowerCase();
      if (trimmed.equals("yes")) {
        return true;
      } else if (trimmed.equals("no")) {
        return false;
      }
      return Boolean.valueOf(trimmed);
    }
  }

  public String asString(String defaultValue) {
    if (isEmpty()) {
      return defaultValue;
    } else {
      return trimmed();
    }
  }

  public List<String> asStringList(String separator, List<String> defaultValue) {
    if (isEmpty()) {
      return defaultValue;
    } else {
      List<String> output = new LinkedList<>();
      String trimmed = trimmed();
      if (trimmed == null) {
        return defaultValue;
      }
      trimmed = trimmed.toLowerCase();
      for (String token : trimmed.split(separator)) {
        output.add(token.trim());
      }

      return output;
    }
  }

  private String trimmed() {
    return this.value != null ? this.value.trim() : this.value;
  }

  public String asString() {
    return asString(null);
  }

  public URI asURI() {
    try {
      return new URI(asString(null));
    } catch (URISyntaxException e) {
      throw new InternalServerException("parameter not valid as URI: " + this.asString("<null>"));
    }
  }

  public List<String> asStringList(String separator) {
    return asStringList(separator, Collections.emptyList());
  }

  public Long asLong() {
    return asLong(null);
  }

  public Integer asInteger() {
    return asInteger(null);
  }

  public Double asDouble() {
    return asDouble(null);
  }

  public Boolean asBoolean() {
    return asBoolean(null);
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public int compareTo(ConfigurazioneDTO toCompare) {
    return key.compareTo(toCompare.getKey()) + value.compareTo(toCompare.getValue());
  }

  @Override
  public String toString() {
    StringBuilder stringBuffer = new StringBuilder();
    stringBuffer.append("key: [" + key + "]");
    stringBuffer.append("value: [" + value + "]");
    return stringBuffer.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((key == null) ? 0 : key.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ConfigurazioneDTO other = (ConfigurazioneDTO) obj;

    if (key == null) {
      if (other.key != null) {
        return false;
      }
    } else if (!key.equals(other.key)) {
      return false;
    }
    if (value == null) {
      if (other.value != null) {
        return false;
      }
    } else if (!value.equals(other.value)) {
      return false;
    }
    return true;
  }

  /**
   * Creates builder to build {@link ConfigurazioneDTO}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(String key, String value) {
    return new Builder().withKey(key).withValue(value);
  }

  /**
   * Builder to build {@link ConfigurazioneDTO}.
   */
  public static final class Builder {

    private String key;

    private String value;

    private Builder() {}

    public Builder withKey(String key) {
      this.key = key;
      return this;
    }

    public Builder withValue(String value) {
      this.value = value;
      return this;
    }

    public ConfigurazioneDTO build() {
      return new ConfigurazioneDTO(this);
    }
  }

}

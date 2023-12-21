/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.discovery.model;

import java.io.Serializable;
import java.security.InvalidParameterException;
import org.apache.commons.lang3.StringUtils;

/**
 * Continene la configurazione di una singola istanza
 */
public class DiscoveryServiceConfiguration implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 8646345928115108334L;


  private String name;

  private String description;

  private String route;

  public DiscoveryServiceConfiguration() {
    // NOP
  }

  private DiscoveryServiceConfiguration(Builder builder) {
    if (StringUtils.isEmpty(builder.name)) {
      throw new InvalidParameterException("service name is required");
    }
    if (StringUtils.isEmpty(builder.route)) {
      throw new InvalidParameterException("service route is required");
    }

    this.name = builder.name;
    this.route = builder.route;
    this.description = builder.description;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRoute() {
    return route;
  }

  public void setRoute(String route) {
    this.route = route;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
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
    DiscoveryServiceConfiguration other = (DiscoveryServiceConfiguration) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "DiscoveryServiceConfiguration [name=" + name + ", route=" + route + "]";
  }

  /**
   * Creates builder to build {@link DiscoveryServiceConfiguration}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link DiscoveryServiceConfiguration}.
   */
  public static final class Builder {
    private String name;
    private String route;
    private String description;

    private Builder() {}

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withDescription(String description) {
      this.description = description;
      return this;
    }

    public Builder withRoute(String route) {
      this.route = route;
      return this;
    }

    public DiscoveryServiceConfiguration build() {
      return new DiscoveryServiceConfiguration(this);
    }
  }

}

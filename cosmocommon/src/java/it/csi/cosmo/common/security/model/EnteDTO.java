/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.security.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class EnteDTO implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private Long id;

  private String nome;

  private String tenantId;

  private List<PreferenzeEnteDTO> preferenze;

  private EnteDTO(Builder builder) {
    this.id = builder.id;
    this.nome = builder.nome;
    this.tenantId = builder.tenantId;
    this.preferenze = builder.preferenze;
  }

  public EnteDTO() {
    // public
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public List<PreferenzeEnteDTO> getPreferenze() {
    return preferenze;
  }

  public void setPreferenze(List<PreferenzeEnteDTO> preferenze) {
    this.preferenze = preferenze;
  }


  @Override
  public String toString() {
    return "EnteDTO [id=" + id + ", nome=" + nome + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    EnteDTO other = (EnteDTO) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }

  /**
   * Creates builder to build {@link EnteDTO}.
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link EnteDTO}.
   */
  public static final class Builder {
    private Long id;
    private String nome;
    private String tenantId;
    private List<PreferenzeEnteDTO> preferenze = Collections.emptyList();

    private Builder() {}

    public Builder withId(Long id) {
      this.id = id;
      return this;
    }

    public Builder withNome(String nome) {
      this.nome = nome;
      return this;
    }

    public Builder withTenantId(String tenantId) {
      this.tenantId = tenantId;
      return this;
    }

    public Builder withPreferenze(List<PreferenzeEnteDTO> preferenze) {
      this.preferenze = preferenze;
      return this;
    }

    public EnteDTO build() {
      return new EnteDTO(this);
    }
  }

}

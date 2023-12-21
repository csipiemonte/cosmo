/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.security.model;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * Classe contenente le informazioni di profilazione sul client corrente.
 */
public class ClientInfoDTO implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private String nome = null;

  private String codice = null;

  private Boolean anonimo;

  private List<ScopeDTO> scopes = new LinkedList<>();

  public ClientInfoDTO() {
    // NOP
  }

  private ClientInfoDTO(Builder builder) {
    this.nome = builder.nome;
    this.codice = builder.codice;
    this.anonimo = builder.anonimo;
    this.scopes = builder.scopes;
  }

  public boolean hasScope(String requiredScope) {
    if (requiredScope == null) {
      throw new InvalidParameterException();
    }
    return scopes != null && scopes.stream().anyMatch(o -> o.getCodice().equals(requiredScope));
  }

  public boolean hasScopes(Collection<String> requiredScopes) {
    if (requiredScopes == null) {
      throw new InvalidParameterException();
    }
    return requiredScopes.stream().allMatch(this::hasScope);
  }

  public boolean hasAnyScope(Collection<String> requiredScopes) {
    if (requiredScopes == null) {
      throw new InvalidParameterException();
    }
    return requiredScopes.stream().anyMatch(this::hasScope);
  }

  public boolean hasScopes(String... requiredScopes) {
    if (requiredScopes == null) {
      throw new InvalidParameterException();
    }
    return hasScopes(Arrays.asList(requiredScopes));
  }

  public boolean hasAnyScope(String... requiredScopes) {
    if (requiredScopes == null) {
      throw new InvalidParameterException();
    }
    return hasAnyScope(Arrays.asList(requiredScopes));
  }

  public Boolean getAnonimo() {
    return anonimo;
  }

  public String getNome() {
    return nome;
  }

  public String getCodice() {
    return codice;
  }

  public List<ScopeDTO> getScopes() {
    return scopes;
  }

  @Override
  public String toString() {
    return "ClientInfoDTO [nome=" + nome + ", codice=" + codice + "]";
  }

  /**
   * Creates builder to build {@link ClientInfoDTO}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link ClientInfoDTO}.
   */
  public static final class Builder {
    private String nome;
    private String codice;
    private Boolean anonimo;
    private List<ScopeDTO> scopes = Collections.emptyList();

    private Builder() {}

    public Builder withNome(String nome) {
      this.nome = nome;
      return this;
    }

    public Builder withCodice(String codice) {
      this.codice = codice;
      return this;
    }

    public Builder withAnonimo(Boolean anonimo) {
      this.anonimo = anonimo;
      return this;
    }

    public Builder withScopes(List<ScopeDTO> scopes) {
      this.scopes = scopes;
      return this;
    }

    public ClientInfoDTO build() {
      return new ClientInfoDTO(this);
    }
  }
}

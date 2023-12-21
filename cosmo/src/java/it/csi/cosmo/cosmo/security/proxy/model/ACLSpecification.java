/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.security.proxy.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

@SuppressWarnings("unchecked")
public abstract class ACLSpecification<T> {

  protected boolean permitAll = false;

  protected boolean denyAll = false;

  protected boolean authenticated = false;

  protected boolean clientAuthenticated = false;

  protected boolean testOnly = false;

  protected List<String> hasScope = new ArrayList<>();

  protected List<String> hasAnyScope = new ArrayList<>();

  protected List<String> hasUseCase = new ArrayList<>();

  protected List<String> hasAnyUseCase = new ArrayList<>();

  public ACLSpecification() {
    // NOP
  }

  public T permitAll() {
    this.permitAll = true;
    return (T) this;
  }

  public T denyAll() {
    this.denyAll = true;
    return (T) this;
  }

  public T authenticated() {
    this.authenticated = true;
    return (T) this;
  }

  public T clientAuthenticated() {
    this.clientAuthenticated = true;
    return (T) this;
  }

  public T testOnly() {
    this.testOnly = true;
    return (T) this;
  }

  public T hasScope(String hasScope) {
    this.hasScope.add(hasScope);
    return (T) this;
  }

  public T hasScope(List<String> hasScope) {
    this.hasScope.addAll(hasScope);
    return (T) this;
  }

  public T hasScope(String... hasScope) {
    for (String s : hasScope) {
      this.hasScope.add(s);
    }
    return (T) this;
  }

  public T hasAnyScope(List<String> hasAnyScope) {
    this.hasAnyScope.addAll(hasAnyScope);
    return (T) this;
  }

  public T hasAnyScope(String... hasScope) {
    for (String s : hasScope) {
      this.hasAnyScope.add(s);
    }
    return (T) this;
  }

  public T hasUseCase(String hasUseCase) {
    this.hasUseCase.add(hasUseCase);
    return (T) this;
  }

  public T hasUseCase(List<String> hasUseCase) {
    this.hasUseCase.addAll(hasUseCase);
    return (T) this;
  }

  public T hasUseCase(String... hasUseCase) {
    for (String s : hasUseCase) {
      this.hasUseCase.add(s);
    }
    return (T) this;
  }

  public T hasAnyUseCase(List<String> hasAnyUseCase) {
    this.hasAnyUseCase.addAll(hasAnyUseCase);
    return (T) this;
  }

  public T hasAnyUseCase(String... hasUseCase) {
    for (String s : hasUseCase) {
      this.hasAnyUseCase.add(s);
    }
    return (T) this;
  }

  public boolean isPermitAll() {
    return permitAll;
  }

  public boolean isDenyAll() {
    return denyAll;
  }

  public boolean isAuthenticated() {
    return authenticated;
  }

  public boolean isTestOnly() {
    return testOnly;
  }

  public List<String> getHasScope() {
    return hasScope;
  }

  public List<String> getHasAnyScope() {
    return hasAnyScope;
  }

  public List<String> getHasUseCase() {
    return hasUseCase;
  }

  public List<String> getHasAnyUseCase() {
    return hasAnyUseCase;
  }

  public boolean isClientAuthenticated() {
    return clientAuthenticated;
  }

}

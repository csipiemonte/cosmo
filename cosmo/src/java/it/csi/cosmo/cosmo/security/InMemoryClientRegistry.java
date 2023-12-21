/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.ScopeDTO;
import it.csi.cosmo.cosmo.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmo.config.Constants;
import it.csi.cosmo.cosmo.config.ParametriApplicativo;

/**
 *
 */
@Component
public class InMemoryClientRegistry {

  private List<InMemoryClientRegistryEntry> registeredClients;

  @Autowired
  private ConfigurazioneService configurazioneService;

  public InMemoryClientRegistry() {
    registeredClients = new ArrayList<>();

    //@formatter:off
    registeredClients.add(InMemoryClientRegistryEntry.withBasicAuth(ClientInfoDTO.builder()
        .withNome("COSMO Angular Webapp")
        .withCodice("COSMO_ANGULAR_UI")
        .withScopes(Collections.emptyList())
        .withAnonimo(false)
        .build(), () -> configurazioneService.requireConfig(ParametriApplicativo.UI_CLIENT_PASSWORD).asString()));
    //@formatter:on

    //@formatter:off
    registeredClients.add(InMemoryClientRegistryEntry.withNoLogin(ClientInfoDTO.builder()
        .withNome("COSMO Orchestrator")
        .withCodice(Constants.ORCHESTRATOR_USERNAME)
        .withScopes(Arrays.asList(
            fromScope(Scopes.ORCHESTRATOR),
            fromScope(Scopes.MONITORING)))
        .withAnonimo(false)
        .build()));
    //@formatter:on

    //@formatter:off
    registeredClients.add(InMemoryClientRegistryEntry.withBasicAuth(ClientInfoDTO.builder()
        .withNome("Generic COSMO client")
        .withCodice("COSMO_CLIENT")
        .withScopes(Arrays.asList(
            fromScope(Scopes.PUSH_EVENTS),
            fromScope(Scopes.DISCOVERY_REGISTER),
            fromScope(Scopes.DEPLOY_PROCESS)))
        .withAnonimo(false)
        .build(), () -> configurazioneService.requireConfig(ParametriApplicativo.DISCOVERY_CLIENTS_GENERIC_PASSWORD).asString()));
    //@formatter:on

    //@formatter:off
    registeredClients.add(InMemoryClientRegistryEntry.withBasicAuth(ClientInfoDTO.builder()
        .withNome("COSMO CaseManagement")
        .withCodice("COSMO_CMMN")
        .withScopes(Arrays.asList(
            fromScope(Scopes.DISCOVERY_REGISTER),
            fromScope(Scopes.PUSH_NOTIFICATIONS),
            fromScope(Scopes.PROCESS_EVENTS)))
        .withAnonimo(false)
        .build(), () -> configurazioneService.requireConfig(ParametriApplicativo.DISCOVERY_CLIENTS_COSMOCMMN_PASSWORD).asString()));
    //@formatter:on

    //@formatter:off
    registeredClients.add(InMemoryClientRegistryEntry.withBasicAuth(ClientInfoDTO.builder()
        .withNome("COSMO Notifications")
        .withCodice("COSMO_NOTIFICATIONS")
        .withScopes(Arrays.asList(
            fromScope(Scopes.DISCOVERY_REGISTER),
            fromScope(Scopes.PUSH_EVENTS),
            fromScope(Scopes.PUSH_NOTIFICATIONS)))
        .withAnonimo(false)
        .build(), () -> configurazioneService.requireConfig(ParametriApplicativo.DISCOVERY_CLIENTS_COSMONOTIFICATIONS_PASSWORD).asString()));
    //@formatter:on

    //@formatter:off
    registeredClients.add(InMemoryClientRegistryEntry.withBasicAuth(ClientInfoDTO.builder()
        .withNome("COSMO BE")
        .withCodice("COSMO_BE")
        .withScopes(Arrays.asList(
            fromScope(Scopes.DISCOVERY_REGISTER),
            fromScope(Scopes.DISCOVERY_FETCH)))
        .withAnonimo(false)
        .build(), () -> configurazioneService.requireConfig(ParametriApplicativo.DISCOVERY_CLIENTS_COSMOBE_PASSWORD).asString()));
    //@formatter:on

  }

  public Optional<InMemoryClientRegistryEntry> find(String codice) {
    return this.registeredClients.stream().filter(o -> o.getClientInfo().getCodice().equals(codice))
        .findFirst();
  }

  public void checkPassword(InMemoryClientRegistryEntry entry, String password) {
    if (StringUtils.isBlank(entry.getPassword())) {
      throw new ForbiddenException("Login non permesso per il fruitore specificato");
    }

    if (!entry.getPassword().equals(password)) {
      throw new ForbiddenException("Credenziali del fruitore non corrette");
    }
  }

  public List<InMemoryClientRegistryEntry> getRegisteredClients() {
    return registeredClients;
  }

  private ScopeDTO fromScope(String auth) {
    //@formatter:off
    return ScopeDTO.builder ()
        .withCodice ( auth )
        .withDescrizione ( auth )
        .build ();
    //@formatter:on
  }

  public static class InMemoryClientRegistryEntry {
    private ClientInfoDTO clientInfo;
    private InMemoryClientPasswordProvider passwordProvider;

    public static InMemoryClientRegistryEntry withBasicAuth(ClientInfoDTO clientInfo,
        InMemoryClientPasswordProvider passwordProvider) {
      InMemoryClientRegistryEntry entry = new InMemoryClientRegistryEntry();
      entry.clientInfo = clientInfo;
      entry.passwordProvider = passwordProvider;
      return entry;
    }

    public static InMemoryClientRegistryEntry withBasicAuth(ClientInfoDTO clientInfo,
        String password) {
      InMemoryClientRegistryEntry entry = new InMemoryClientRegistryEntry();
      entry.clientInfo = clientInfo;
      entry.passwordProvider = () -> password;
      return entry;
    }

    public static InMemoryClientRegistryEntry withNoLogin(ClientInfoDTO clientInfo) {
      InMemoryClientRegistryEntry entry = new InMemoryClientRegistryEntry();
      entry.clientInfo = clientInfo;
      entry.passwordProvider = null;
      return entry;
    }

    public ClientInfoDTO getClientInfo() {
      return clientInfo;
    }

    public String getPassword() {
      return passwordProvider != null ? passwordProvider.provide() : null;
    }

  }

  @FunctionalInterface
  public static interface InMemoryClientPasswordProvider {
    String provide();
  }
}

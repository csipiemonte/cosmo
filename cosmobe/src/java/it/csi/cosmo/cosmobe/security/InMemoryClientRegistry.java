/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.ScopeDTO;
import it.csi.cosmo.cosmobe.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmobe.config.Constants;
import it.csi.cosmo.cosmobe.config.ParametriApplicativo;

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
    registeredClients.add(InMemoryClientRegistryEntry.withNoLogin(ClientInfoDTO.builder()
        .withNome("COSMO BE Orchestrator")
        .withCodice(Constants.BE_ORCHESTRATOR_USERNAME)
        .withScopes(Arrays.asList(
            fromScope(Scopes.BE_ORCHESTRATOR),
            fromScope(Scopes.MONITORING)))
        .withAnonimo(false)
        .build()));

    registeredClients.add(InMemoryClientRegistryEntry.withBasicAuth(ClientInfoDTO.builder()
        .withNome("ApiManager")
        .withCodice("API_MANAGER")
        .withScopes(Arrays.asList(fromScope(Scopes.API_MANAGER)))
        .withAnonimo(false)
        .build(),
        () -> configurazioneService.requireConfig(
            ParametriApplicativo.AUTHENTICATION_BASIC_API_MANAGER_USERNAME).asString(),
        () -> configurazioneService.requireConfig(
            ParametriApplicativo.AUTHENTICATION_BASIC_API_MANAGER_PASSWORD).asString()));

    registeredClients.add(InMemoryClientRegistryEntry.withBasicAuth(ClientInfoDTO.builder()
        .withNome("StarDas")
        .withCodice("STARDAS")
        .withScopes(Arrays.asList(fromScope(Scopes.STARDAS_CALLBACK)))
        .withAnonimo(false)
        .build(),
        () -> configurazioneService.requireConfig(
            ParametriApplicativo.AUTHENTICATION_BASIC_STARDAS_USERNAME).asString(),
        () -> configurazioneService.requireConfig(
            ParametriApplicativo.AUTHENTICATION_BASIC_STARDAS_PASSWORD).asString()));
    //@formatter:on
  }

  public Optional<InMemoryClientRegistryEntry> find(String codice) {
    return this.registeredClients.stream()
        .filter(o -> (o.getUsername() != null && o.getUsername().equals(codice))
            || (o.getClientInfo().getCodice() != null
                && o.getClientInfo().getCodice().equals(codice)))
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

  private ScopeDTO fromScope(Scopes auth) {
    //@formatter:off
    return ScopeDTO.builder ()
        .withCodice ( auth.name() )
        .withDescrizione ( auth.getDescrizione() )
        .build ();
    //@formatter:on
  }

  public static class InMemoryClientRegistryEntry {
    private ClientInfoDTO clientInfo;
    private InMemoryClientUsernameProvider usernameProvider;
    private InMemoryClientPasswordProvider passwordProvider;

    public static InMemoryClientRegistryEntry withBasicAuth(ClientInfoDTO clientInfo,
        InMemoryClientUsernameProvider usernameProvider,
        InMemoryClientPasswordProvider passwordProvider) {
      InMemoryClientRegistryEntry entry = new InMemoryClientRegistryEntry();
      entry.clientInfo = clientInfo;
      entry.usernameProvider = usernameProvider;
      entry.passwordProvider = passwordProvider;
      return entry;
    }

    public static InMemoryClientRegistryEntry withBasicAuth(ClientInfoDTO clientInfo,
        String password) {
      InMemoryClientRegistryEntry entry = new InMemoryClientRegistryEntry();
      entry.clientInfo = clientInfo;
      entry.usernameProvider = clientInfo::getCodice;
      entry.passwordProvider = () -> password;
      return entry;
    }

    public static InMemoryClientRegistryEntry withNoLogin(ClientInfoDTO clientInfo) {
      InMemoryClientRegistryEntry entry = new InMemoryClientRegistryEntry();
      entry.clientInfo = clientInfo;
      entry.usernameProvider = null;
      entry.passwordProvider = null;
      return entry;
    }

    public ClientInfoDTO getClientInfo() {
      return clientInfo;
    }

    public String getPassword() {
      return passwordProvider != null ? passwordProvider.provide() : null;
    }

    public String getUsername() {
      return usernameProvider != null ? usernameProvider.provide() : null;
    }
  }

  @FunctionalInterface
  public static interface InMemoryClientPasswordProvider {
    String provide();
  }

  @FunctionalInterface
  public static interface InMemoryClientUsernameProvider {
    String provide();
  }
}

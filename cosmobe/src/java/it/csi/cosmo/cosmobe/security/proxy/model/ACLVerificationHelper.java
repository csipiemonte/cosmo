/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.security.proxy.model;

import javax.servlet.http.HttpServletRequest;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.util.RequestUtils;
import it.csi.cosmo.cosmobe.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmobe.security.SecurityUtils;
import it.csi.cosmo.cosmobe.util.logger.LogCategory;
import it.csi.cosmo.cosmobe.util.logger.LoggerFactory;

/**
 *
 */

public abstract class ACLVerificationHelper {

  private static final String POLICY_PREFIX = "POLICY::";

  private static final CosmoLogger LOGGER =
      LoggerFactory.getLogger(LogCategory.SECURITY_LOG_CATEGORY, "ACLVerificationHelper");

  private ACLVerificationHelper() {
    // NOP
  }

  public static void verifyACL(ACLSpecification<?> specification) {

    // verifico testOnly
    if (specification.isTestOnly()) {
      processSpecTestOnly();
    }

    // verifico i qualificatori assoluti
    if (specification.isPermitAll()) {
      return;
    }

    if (specification.isDenyAll()) {
      handleFailure(POLICY_PREFIX + "DENY_ALL");
    }

    processSpecScopesRequirements(specification);
  }

  private static void processSpecTestOnly() {
    if (!ConfigurazioneService.getInstance().isTestModeEnabled()) {
      handleFailure(POLICY_PREFIX + "TEST_ONLY");
    }
  }

  private static void processSpecScopesRequirements(ACLSpecification<?> specification) {
    if (specification == null) {
      return;
    }

    // ottengo la profilazione per il client corrente
    ClientInfoDTO client = SecurityUtils.getClientCorrente();

    if (specification.isAuthenticated() && (client == null
        || (client.getAnonimo() != null && client.getAnonimo().booleanValue()))) {
      handleFailure(POLICY_PREFIX + "AUTHENTICATED");
    }

    // verifico se e' richiesta una lista di scopes
    if (!specification.getHasScope().isEmpty() && !client.hasScopes(specification.getHasScope())) {
      handleFailure(specification.getHasScope());
    }

    // verifico se e' richiesta una lista di scopes alternativi
    if (!specification.getHasAnyScope().isEmpty()
        && !client.hasAnyScope(specification.getHasAnyScope())) {
      handleFailure(specification.getHasAnyScope());
    }
  }

  private static void handleFailure(Object missing) {
    HttpServletRequest servletRequest = RequestUtils.getCurrentRequest().orElse(null);

    String url = (servletRequest != null) ? servletRequest.getRequestURL().toString() : "<UNKNOWN>";

    LOGGER.warn("logFailed",
        "accesso negato per proxy il client: " + SecurityUtils.getClientCorrente()
        + " all'endpoint " + url + " a causa di autorizzazione mancante: " + missing);

    if (missing instanceof String && ((String) missing).startsWith(POLICY_PREFIX)) {
      throw new ForbiddenException(missing.toString());
    } else {
      throw new ForbiddenException();
    }
  }

}

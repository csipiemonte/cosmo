/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.security;

import java.io.IOException;
import java.lang.reflect.Method;
import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import org.jboss.resteasy.annotations.interception.HeaderDecoratorPrecedence;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.ManagedException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.RequestUtils;
import it.csi.cosmo.cosmoecm.business.rest.impl.ParentApiImpl;
import it.csi.cosmo.cosmoecm.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoecm.business.service.impl.ConfigurazioneServiceImpl;
import it.csi.cosmo.cosmoecm.util.listener.SpringApplicationContextHelper;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;


/**
 * Interceptor utilizzato per verificare che l'utente disponga delle autorizzazioni richieste prima
 * di accedere ai metodi delle risorse RestEasy
 */

@Provider
@HeaderDecoratorPrecedence
@Priority(20000)
public class SecurityInterceptor implements ContainerRequestFilter {

  private static final CosmoLogger LOGGER =
      LoggerFactory.getLogger(LogCategory.SECURITY_LOG_CATEGORY, "SecurityInterceptor");

  private static final String POLICY_PREFIX = "POLICY::";

  @Context
  private ResourceInfo resourceInfo;

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {

    process(resourceInfo);
  }

  private void process(ResourceInfo resourceInfo) {
    Class<?> resourceClass = resourceInfo.getResourceClass();
    Method method = resourceInfo.getResourceMethod();

    try {
      // verifico annotation Secured a livello di interfaccia
      if (resourceClass.isAnnotationPresent(Secured.class)) {
        processAnnotation(resourceClass.getAnnotation(Secured.class));
      }

      // verifico annotation Secured a livello di metodo sull'interfaccia
      if (method.isAnnotationPresent(Secured.class)) {
        processAnnotation(method.getAnnotation(Secured.class));
      }

      // verifico annotation Secured a livello di classe
      Class<? extends ParentApiImpl> implClass =
          SpringApplicationContextHelper.getImplementationClass(resourceClass);
      if (implClass != null && implClass.isAnnotationPresent(Secured.class)) {
        processAnnotation(implClass.getAnnotation(Secured.class));
      }

      // verifico annotation Secured a livello di metodo sulla classe
      Method implMethod =
          SpringApplicationContextHelper.getImplementationMethod(resourceClass, method);
      if (implMethod != null && implMethod.isAnnotationPresent(Secured.class)) {
        processAnnotation(implMethod.getAnnotation(Secured.class));
      }

    } catch (ManagedException e) {
      throw e;

    } catch (Exception e) {
      LOGGER.error("process", "errore imprevisto nel controllo delle autorizzazioni", e);

      throw new InternalServerException("errore in fase di controllo delle autorizzazioni.");
    }
  }

  private void processAnnotation(Secured annotation) {
    if (annotation == null) {
      return;
    }

    // verifico notSecured
    if (annotation.notSecured()) {
      processAnnotationNotSecured();
      return;
    }

    // verifico testOnly
    if (annotation.testOnly()) {
      processAnnotationTestOnly();
    }

    // verifico i qualificatori assoluti
    if (annotation.permitAll()) {
      return;
    }

    if (annotation.denyAll()) {
      handleFailure(POLICY_PREFIX + "DENY_ALL");
    }

    processAnnotationUseCaseRequirements(annotation);
    processAnnotationScopesRequirements(annotation);
  }

  private void processAnnotationTestOnly() {
    if (!ConfigurazioneService.getInstance().isTestModeEnabled()) {
      handleFailure(POLICY_PREFIX + "TEST_ONLY");
    }
  }

  private void processAnnotationNotSecured() {
    if (!ConfigurazioneServiceImpl.isLocal()) {
      handleFailure(POLICY_PREFIX + "NOT_SECURED");
    } else {
      LOGGER.warn("verificaAutorizzazioniUtenteCorrente",
          "\n\n\tATTENZIONE: ENDPOINT CORRENTE PRIVO DI SPECIFICA DI SICUREZZA:" + "\n\t"
              + RequestUtils.getCurrentRequest().map(HttpServletRequest::getRequestURL)
              + "\n\tQUESTO ENDPOINT NON SARA' FUNZIONANTE IN AMBIENTI NON LOCALI \n");
    }
  }

  private void processAnnotationUseCaseRequirements(Secured annotation) {
    if (annotation == null) {
      return;
    }

    // ottengo la profilazione per l'utente corrente
    UserInfoDTO user = SecurityUtils.getUtenteCorrente();

    if (annotation.authenticated() && !Boolean.FALSE.equals(user.getAnonimo())) {
      handleFailure(POLICY_PREFIX + "AUTHENTICATED");
    }

    if (annotation.profiled() && (user.getProfilo() == null)) {
      handleFailure(POLICY_PREFIX + "PROFILED");
    }

    if (annotation.guest() && Boolean.FALSE.equals(user.getAnonimo())) {
      handleFailure(POLICY_PREFIX + "GUEST");
    }

    // verifico se e' richiesta una lista di authorities
    if (annotation.value().length > 0 && !user.hasAuthorities(annotation.value())) {
      handleFailure(annotation.value());
    }

    // verifico se e' richiesta una lista di authorities alternative
    if (annotation.hasAnyAuthority().length > 0
        && !user.hasAnyAuthority(annotation.hasAnyAuthority())) {
      handleFailure(annotation.hasAnyAuthority());
    }
  }

  private void processAnnotationScopesRequirements(Secured annotation) {
    if (annotation == null) {
      return;
    }

    // ottengo la profilazione per il client corrente
    ClientInfoDTO client = SecurityUtils.getClientCorrente();

    if (annotation.authenticatedClient() && !Boolean.FALSE.equals(client.getAnonimo())) {
      handleFailure(POLICY_PREFIX + "AUTHENTICATED");
    }

    if (annotation.guestClient() && Boolean.FALSE.equals(client.getAnonimo())) {
      handleFailure(POLICY_PREFIX + "GUEST");
    }

    // verifico se e' richiesta una lista di scopes
    if (annotation.hasScopes().length > 0 && !client.hasScopes(annotation.hasScopes())) {
      handleFailure(annotation.hasScopes());
    }

    // verifico se e' richiesta una lista di scopes alternativi
    if (annotation.hasAnyScope().length > 0 && !client.hasAnyScope(annotation.hasAnyScope())) {
      handleFailure(annotation.hasAnyScope());
    }
  }

  private void handleFailure(Object missing) {
    HttpServletRequest servletRequest = RequestUtils.getCurrentRequest().orElse(null);

    String url = (servletRequest != null) ? servletRequest.getRequestURL().toString() : "<UNKNOWN>";

    LOGGER.warn("logFailed",
        "accesso negato per l'utente: " + SecurityUtils.getUtenteCorrente() + " e client: "
            + SecurityUtils.getClientCorrente() + " all'endpoint " + url
            + " a causa di autorizzazione mancante: " + missing);

    if (missing instanceof String && ((String) missing).startsWith(POLICY_PREFIX)) {
      throw new ForbiddenException(missing.toString());
    } else {
      throw new ForbiddenException();
    }
  }

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.security;

import it.csi.cosmo.common.exception.UnauthorizedException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmocmmn.business.service.ClientService;
import it.csi.cosmo.cosmocmmn.business.service.UserService;
import it.csi.cosmo.cosmocmmn.util.logger.LogCategory;
import it.csi.cosmo.cosmocmmn.util.logger.LoggerFactory;

/**
 * Utils per gestire l'utenza corrente
 */

public abstract class SecurityUtils {

  private static final String MSG_SECURITY_ASSERTION_FAILED_USER =
      "Security assertion failed: user ";

  private static CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.SECURITY_LOG_CATEGORY, "SecurityUtils");

  private SecurityUtils() {
    // NOP
  }


  /*
   * Ritorna true se l'utente corrente ha tutti gli use case specificati
   */
  public static boolean currentUserHasUseCases(UseCase... useCases) {
    UserInfoDTO currentUser = getUtenteCorrente();

    if (currentUser == null || currentUser.getProfilo() == null
        || currentUser.getProfilo().getUseCases() == null
        || currentUser.getProfilo().getUseCases().isEmpty()) {
      return false;
    }

    for (UseCase useCase : useCases) {
      if (!currentUser.hasAuthority(useCase.name())) {
        return false;
      }
    }

    return true;
  }

  /*
   * Ritorna true se l'utente corrente ha almeno uno degli use case specificati
   */
  public static boolean currentUserHasAnyUseCase(UseCase... useCases) {
    UserInfoDTO currentUser = getUtenteCorrente();

    if (currentUser == null || currentUser.getProfilo() == null
        || currentUser.getProfilo().getUseCases() == null
        || currentUser.getProfilo().getUseCases().isEmpty()) {
      return false;
    }

    for (UseCase useCase : useCases) {
      if (currentUser.hasAuthority(useCase.name())) {
        return true;
      }
    }

    return false;
  }

  /*
   * Solleva eccezione se l'utente corrente ha tutti gli use case specificati
   */
  public static void assertCurrentUserHasUseCases(UseCase... useCases) {
    if (!currentUserHasUseCases(useCases)) {
      StringBuilder messageBuilder = new StringBuilder(MSG_SECURITY_ASSERTION_FAILED_USER);
      messageBuilder.append(getUtenteCorrente());
      messageBuilder.append(" is not in one of the use cases: ");

      for (UseCase uc : useCases) {
        messageBuilder.append(uc.name());
        messageBuilder.append(",");
      }

      String message = messageBuilder.toString().substring(0, messageBuilder.length() - 1);
      logger.error("assertCurrentUserHasUseCases", message);

      throw new UnauthorizedException(message);
    }
  }

  /*
   * Solleva eccezione se l'utente corrente ha almeno uno degli use case specificati
   */
  public static void assertCurrentUserHasAnyUseCase(UseCase... useCases) {
    if (!currentUserHasAnyUseCase(useCases)) {
      StringBuilder messageBuilder = new StringBuilder(MSG_SECURITY_ASSERTION_FAILED_USER);
      messageBuilder.append(getUtenteCorrente());
      messageBuilder.append(" is not in any of the use cases: ");

      for (UseCase uc : useCases) {
        messageBuilder.append(uc.name());
        messageBuilder.append(",");
      }

      String message = messageBuilder.toString().substring(0, messageBuilder.length() - 1);
      logger.error("assertCurrentUserHasAnyUseCase", message);

      throw new UnauthorizedException(message);
    }
  }

  public static UserInfoDTO getUtenteCorrente() {
    return UserService.getInstance().getUtenteCorrente();
  }

  public static UserInfoDTO requireUtenteCorrente() {
    UserInfoDTO user = UserService.getInstance().getUtenteCorrente();
    if (user != null && !user.getAnonimo()) {
      return user;
    } else {
      throw new UnauthorizedException("User authentication is required");
    }
  }

  public static ClientInfoDTO getClientCorrente() {
    return ClientService.getInstance().getClientCorrente();
  }

  public static ClientInfoDTO requireClientCorrente() {
    ClientInfoDTO client = ClientService.getInstance().getClientCorrente();
    if (client != null && !client.getAnonimo()) {
      return client;
    } else {
      throw new UnauthorizedException("Client authentication is required");
    }
  }
}

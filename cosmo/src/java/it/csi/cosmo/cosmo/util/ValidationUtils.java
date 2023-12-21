/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.util;

import java.time.OffsetDateTime;
import org.apache.commons.lang3.StringUtils;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmo.config.ErrorMessages;
import it.csi.cosmo.cosmo.util.logger.LogCategory;
import it.csi.cosmo.cosmo.util.logger.LoggerFactory;

/**
 *
 */

public class ValidationUtils {

  private static final CosmoLogger LOGGER =
      LoggerFactory.getLogger(LogCategory.UTIL_LOG_CATEGORY, "ValidationUtils");

  private ValidationUtils() {
    // helper class
  }

  public static boolean inPeriodoValido(OffsetDateTime start, OffsetDateTime end) {
    return inPeriodoValido(start, end, null);
  }

  public static boolean inPeriodoValido(OffsetDateTime start, OffsetDateTime end,
      OffsetDateTime reference) {

    if (reference == null) {
      reference = OffsetDateTime.now();
    }

    return start != null
        && !start.isAfter(reference)
        && (end == null
        || end.isAfter(reference));
  }

  public static void require(String id, String parametro) {
    String methodName = "requireString";

    if (StringUtils.isBlank(id)) {
      LOGGER.error(methodName, String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, parametro));
      throw new BadRequestException(
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, parametro));
    }

    if (!StringUtils.isNumeric(id)) {
      LOGGER.error(methodName,
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO_CORRETTAMENTE, parametro));
      throw new BadRequestException(
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO_CORRETTAMENTE, parametro));
    }
  }

  public static void require(Long id, String parametro) {
    String methodName = "requireLong";

    if (id == null) {
      LOGGER.error(methodName, String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, parametro));
      throw new BadRequestException(
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, parametro));
    }
  }

  public static void require(Object id, String parametro) {
    String methodName = "requireObject";

    if (id == null) {
      LOGGER.error(methodName, String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, parametro));
      throw new BadRequestException(
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, parametro));
    }
  }

}

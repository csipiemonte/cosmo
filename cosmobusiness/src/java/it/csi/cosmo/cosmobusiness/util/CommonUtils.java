/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.util;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmobusiness.config.ErrorMessages;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;


/**
 * Classe per funzioni di utilities comuni a tutto l'applicativo
 */
public abstract class CommonUtils {

  private static final CosmoLogger LOGGER =
      LoggerFactory.getLogger(LogCategory.UTIL_LOG_CATEGORY, "CommonUtils");

  private CommonUtils() {
    // NOP
  }

  public static void validaDatiInput(String id, String parametro) {
    String methodName = "validaDatiInput";

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

  public static RestVariable variable(String name, Object value) {
    var output = new RestVariable();
    output.setName(name);
    output.setValue(value);
    return output;
  }

  public static RestVariable variable(String name, String value, String scope, String type) {
    var output = new RestVariable();
    output.setName(name);
    output.setValue(value);
    output.setScope(scope);
    output.setType(type);
    return output;
  }

  public static String clean(String raw) {
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    return raw.strip();
  }

  public static String dateToISO(Date date) {
    if (date == null) {
      return null;
    }

    return OffsetDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
        .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.util;

import java.time.OffsetDateTime;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmoecm.config.ErrorMessages;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;


/**
 * Classe per funzioni di utilities comuni a tutto l'applicativo
 */
public abstract class CommonUtils {

  private static final CosmoLogger LOGGER =
      LoggerFactory.getLogger(LogCategory.UTIL_LOG_CATEGORY, "CommonUtils");

  /*
   * ObjectMapper usato per stampare, a fini di logging, i JSON delle request
   */
  private static ObjectMapper mapper = null;

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

  private static ObjectMapper getMapper() {
    if (mapper == null) {
      mapper = new ObjectMapper();
      mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
      mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
      mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    }

    return mapper;
  }

  public static String represent(Object o) {
    try {
      return getMapper().writeValueAsString(o);
    } catch (Exception e) {
      return "<CANNOT REPRESENT (" + e.getClass().getSimpleName() + " - " + e.getMessage() + ")>";
    }
  }
  
  public static boolean inPeriodoValido(OffsetDateTime start, OffsetDateTime end) {
    return inPeriodoValido(start, end, null);
  }

  public static boolean inPeriodoValido(OffsetDateTime start, OffsetDateTime end,
      OffsetDateTime reference) {

    if (reference == null) {
      reference = OffsetDateTime.now();
    }

    return start != null && !start.isAfter(reference) && (end == null || end.isAfter(reference));
  }

  public static void requireString(String id, String parametro) {
    String methodName = "requireString";

    if (StringUtils.isBlank(id)) {
      LOGGER.error(methodName, String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, parametro));
      throw new BadRequestException(
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, parametro));
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
}

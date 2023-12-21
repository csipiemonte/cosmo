/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.util;

import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.regex.Pattern;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.commons.lang3.StringUtils;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.UnprocessableEntityException;

/**
 *
 */

public abstract class ValidationUtils {

  private static final String PARAMETRO_NON_VALORIZZATO = "Parametro '%s' non valorizzato";

  private static final String CAMPO_NON_PRESENTE = "Campo '%s' necessario ma non valorizzato";

  private static final String PARAMETRO_NON_VALORIZZATO_CORRETTAMENTE =
      "Parametro '%s' non valorizzato correttamente";
  
  private static final Pattern IPV4_6_REGEX = Pattern.compile(
      "((^\\s*((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.)"
          + "{3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))\\s*$)|"
          + "(^\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|"
          + "(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)"
          + "(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|"
          + "(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|"
          + ":((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|"
          + "(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|"
          + "((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)"
          + "(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|" // NOSONAR
          + "(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|"
          + "((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)"
          + "(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|"
          + "(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|"
          + "((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)"
          + "(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|"
          + "(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|"
          + "((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)"
          + "(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|"
          + "(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:"
          + "((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*$))");

  private ValidationUtils() {
    // private
  }

  public static boolean isValidIpAddress(String raw) {
    if (raw == null || raw.isBlank()) {
      return false;
    }
    return IPV4_6_REGEX.matcher(raw.strip()).matches();
  }

  public static String require(String id, String parametro) {

    if (StringUtils.isBlank(id)) {
      throw new BadRequestException(
          String.format(PARAMETRO_NON_VALORIZZATO, parametro));
    }
    return id;
  }

  public static Long require(Long id, String parametro) {

    if (id == null) {
      throw new BadRequestException(
          String.format(PARAMETRO_NON_VALORIZZATO, parametro));
    }


    if (id <= 0L) {
      throw new BadRequestException(
          String.format(PARAMETRO_NON_VALORIZZATO_CORRETTAMENTE, parametro));
    }

    return id;
  }

  public static <T> T require(T id, String parametro) {

    if (id == null) {
      throw new BadRequestException(
          String.format(PARAMETRO_NON_VALORIZZATO, parametro));
    }

    return id;
  }

  public static void checkThat(BooleanSupplier predicate, String message) {
    boolean result;
    try {
      result = predicate.getAsBoolean();
    } catch (Exception e) {
      throw new InternalServerException("Errore nel controllo del formato.", e);
    }

    if (!result) {
      throw new BadRequestException(message);
    }
  }

  public static <T> void validaAnnotations(T body) {
    // Validazione campi obbligatori

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    Set<ConstraintViolation<T>> violations = validator.validate(body);
    if (!violations.isEmpty()) {
      StringBuilder errorMessage = new StringBuilder("I dati inviati non sono corretti. ");
      for (ConstraintViolation<T> violation : violations) {
        errorMessage.append(violation.getPropertyPath()).append(" ").append(violation.getMessage())
            .append("; ");
      }
      throw new BadRequestException(errorMessage.toString());
    }
  }

  public static void notSupported(Object field, String name) {
    if (field != null) {
      throw new UnprocessableEntityException("Il campo di input '" + name + "' non e' supportato");
    }
  }

  public static <T> T assertNotNull(T id, String parametro) {

    if (id == null) {
      throw new InternalServerException(String.format(CAMPO_NON_PRESENTE, parametro));
    }

    return id;
  }
}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.beanutils.ConvertUtils;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmosoap.integration.soap.acta.exception.ActaModelTranslationException;
import it.csi.cosmo.cosmosoap.integration.soap.acta.parser.ActaFieldConverterMap.ValueConverter;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;


/**
 * Classe di utilities che converte i vari campi con l'oggetto corrispettivo
 */
public class ActaFieldConverter {

  protected static final CosmoLogger log = LoggerFactory
      .getLogger(LogCategory.BUSINESS_LOG_CATEGORY, ActaFieldConverter.class.getSimpleName());

  public static Object[] getConvertedValueToArray(Object rawValue, Class<?> fromClass,
      Class<?> toClass) {
    log.trace("getConvertedValueToArray", "converting array");

    Map<Class<?>, ValueConverter> map = ActaFieldConverterMap.getConverterMap();

    Class<?> targetItemType = toClass.getComponentType();

    if (targetItemType.getName().startsWith("it.doqui.acta") && !map.containsKey(targetItemType)) {
      throw new ActaModelTranslationException(
          "Custom class " + toClass.getName() + " has no registered custom mapper");
    }

    if (Collection.class.isAssignableFrom(fromClass)) {
      int size = rawValue == null ? 0 : ((Collection<?>) rawValue).size();
      Object[] outputArray =
          (Object[]) java.lang.reflect.Array.newInstance(toClass.getComponentType(), size);

      if (size > 0) {
        int cnt = 0;
        for (Object rawValueItem : (Collection<?>) rawValue) {
          log.trace("getConvertedValueToArray", "converting array item from "
              + rawValue.getClass().getName() + " -> " + toClass.getComponentType().getName());
          outputArray[cnt++] =
              getConvertedValue(rawValueItem, rawValue.getClass(), toClass.getComponentType());
        }
      }

      // LOGGER.trace ( "" + rawValue + " -> " + outputArray );
      return outputArray;

    } else if (targetItemType.equals(fromClass)) {

      log.trace("getConvertedValueToArray", "applying value - to - array wrapping");
      Object[] outputArray =
          (Object[]) java.lang.reflect.Array.newInstance(toClass.getComponentType(), 1);
      outputArray[0] = rawValue;

      // LOGGER.trace ( "" + rawValue + " -> " + outputArray )
      return outputArray;

    } else {
      throw new RuntimeException(
          "Invalid type-to-array translation " + fromClass.getName() + " -> " + toClass.getName());
    }
  }

  public static Object getConvertedValue(Object rawValue, Class<?> fromClass, Class<?> toClass) {
    log.trace("getConvertedValue",
        "evaluating " + fromClass.getName() + " -> " + toClass.getName());
    Object output;

    if (fromClass.equals(toClass)) {
      log.trace("getConvertedValue", "returning identity");
      return rawValue;
    }

    if (toClass.getName().startsWith("it.doqui.acta")) {
      log.trace("getConvertedValue", "converting custom");
      output = getConvertedValueToCustom(rawValue, fromClass, toClass);

    } else if (java.util.Date.class.isAssignableFrom(toClass)) {
      log.trace("getConvertedValue", "converting date");
      output = getConvertedValueToDate(rawValue, fromClass);

    } else {
      log.trace("getConvertedValue", "converting automatically");
      try {
        output = ConvertUtils.convert(rawValue, toClass);

      } catch (Exception e) {
        throw new ActaModelTranslationException("Error converting " + rawValue + " from "
            + fromClass.getName() + " to " + toClass.getName(), e);
      }
    }

    log.trace("getConvertedValue", "" + rawValue + " -> " + output);
    return output;
  }

  private static Object getConvertedValueToCustom(Object rawValue, Class<?> fromClass,
      Class<?> toClass) {

    Map<Class<?>, ValueConverter> map = ActaFieldConverterMap.getConverterMap();

    if (map.containsKey(toClass)) {
      if (rawValue == null || rawValue.equals("")) {
        return null;
      }
      String stringValue = (String) getConvertedValue(rawValue, fromClass, String.class);
      return map.get(toClass).convert(stringValue);
    } else {
      throw new ActaModelTranslationException(
          "Custom class " + toClass.getName() + " has no registered custom mapper");
    }
  }

  private static Object getConvertedValueToDate(Object rawValue, Class<?> fromClass) {
    if (rawValue == null) {
      return rawValue;
    }

    if (java.util.Date.class.isAssignableFrom(fromClass)) {
      return rawValue;
    } else if (LocalDate.class.isAssignableFrom(fromClass)) {
      return java.util.Date
          .from(((LocalDate) rawValue).atTime(0, 0).atZone(ZoneOffset.UTC).toInstant());
    } else if (LocalDateTime.class.isAssignableFrom(fromClass)) {
      return java.util.Date.from(((LocalDateTime) rawValue).atZone(ZoneOffset.UTC).toInstant());
    } else if (ZonedDateTime.class.isAssignableFrom(fromClass)) {
      return java.util.Date.from(((ZonedDateTime) rawValue).toInstant());
    } else {
      throw new ActaModelTranslationException("Unsupported date type " + fromClass.getName());
    }

  }
}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import it.csi.cosmo.cosmosoap.integration.soap.acta.exception.ActaModelTranslationException;



/**
 *
 */

public class ActaFieldDeserializer {

  public static Object getConvertedValueToClassAndType(List<String> values, Class<?> targetClass,
      Class<?> targetItemClass) {
    if (Collection.class.isAssignableFrom(targetClass)) {
      return convertCollection(values, targetClass, targetItemClass);
    }

    if (values.isEmpty()) {
      return null;
    }

    String value = values.get(0);

    if (empty(value)) {
      return null;
    }

    if (String.class.isAssignableFrom(targetClass)) {
      return value;

    } else if (Long.class.isAssignableFrom(targetClass)) {
      return Long.valueOf(value);

    } else if (Integer.class.isAssignableFrom(targetClass)) {
      return Integer.valueOf(value);

    } else if (Boolean.class.isAssignableFrom(targetClass)) {
      return toBoolean(value);

    } else if (java.util.Date.class.isAssignableFrom(targetClass)) {
      return toDate(value);

    } else if (LocalDate.class.isAssignableFrom(targetClass)) {
      return toLocalDate(value);

    } else if (ZonedDateTime.class.isAssignableFrom(targetClass)) {
      return toZonedDateTime(value);

    } else if (OffsetDateTime.class.isAssignableFrom(targetClass)) {
      return toOffsetDateTime(value);

    } else if (LocalDateTime.class.isAssignableFrom(targetClass)) {
      return toLocalDateTime(value);

    } else {
      throw new ActaModelTranslationException(
          "Invalid target property class: " + targetClass.getName());
    }
  }

  private static Boolean toBoolean(String raw) {
    if (raw == null) {
      return false;
    } else if (raw.equalsIgnoreCase("true")) {
      return true;
    } else if (raw.equalsIgnoreCase("false")) {
      return false;
    } else if (raw.equalsIgnoreCase("S")) {
      return true;
    } else if (raw.equalsIgnoreCase("N")) {
      return false;
    } else if (raw.equalsIgnoreCase("1")) {
      return true;
    } else if (raw.equalsIgnoreCase("0")) {
      return false;
    } else {
      return false;
    }
  }

  @SuppressWarnings("unchecked")
  private static Collection<?> convertCollection(List<String> values, Class<?> targetClass,
      Class<?> targetItemClass) {
    Collection<Object> output;

    if (targetClass.isInterface()) {
      if (List.class.isAssignableFrom(targetClass)) {
        output = new LinkedList<>();
      } else if (Set.class.isAssignableFrom(targetClass)) {
        output = new HashSet<>();
      } else if (Collection.class.isAssignableFrom(targetClass)) {
        output = new LinkedList<>();
      } else {
        throw new ActaModelTranslationException("Unsupported collection interface: " + targetClass);
      }

    } else {
      try {
        output = (Collection<Object>) targetClass.newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
        throw new ActaModelTranslationException(
            "Error instantiating collection of type " + targetClass, e);
      }
    }

    for (String value : values) {
      output.add(getConvertedValueToClassAndType(Arrays.asList(value), targetItemClass, null));
    }

    return output;
  }

  private static boolean empty(String raw) {
    return StringUtils.isBlank(raw);
  }

  private static String fillRight(String raw, int num, String with) {
    while (raw.length() < num) {
      raw += with;
    }
    return raw;
  }

  private static LocalDate toLocalDate(String raw) {
    if (raw.matches(FORMAT_DATE_ONLY)) {
      return LocalDate.parse(raw, FORMATTER_DATE_ONLY);
    } else if (raw.matches(FORMAT_LOCAL_DATE_TIME_NANO)) {
      return LocalDateTime.parse(fillRight(raw, 23, "0"), FORMATTER_LOCAL_DATE_TIME_NANO)
          .toLocalDate();
    } else if (raw.matches(FORMAT_LOCAL_DATE_TIME)) {
      return LocalDateTime.parse(raw, FORMATTER_LOCAL_DATE_TIME).toLocalDate();
    } else if (raw.matches(FORMAT_ZONED_DATE_TIME)) {
      return ZonedDateTime.parse(raw).toLocalDate();
    } else {
      throw new ActaModelTranslationException("Value not translatable to LocalDate: " + raw);
    }
  }

  private static LocalDateTime toLocalDateTime(String raw) {
    if (raw.matches(FORMAT_DATE_ONLY)) {
      return LocalDate.parse(raw, FORMATTER_DATE_ONLY).atTime(LocalTime.of(0, 0));
    } else if (raw.matches(FORMAT_LOCAL_DATE_TIME_NANO)) {
      return LocalDateTime.parse(fillRight(raw, 23, "0"), FORMATTER_LOCAL_DATE_TIME_NANO);
    } else if (raw.matches(FORMAT_LOCAL_DATE_TIME)) {
      return LocalDateTime.parse(raw, FORMATTER_LOCAL_DATE_TIME);
    } else if (raw.matches(FORMAT_ZONED_DATE_TIME)) {
      return ZonedDateTime.parse(raw).toLocalDateTime();
    } else {
      throw new ActaModelTranslationException("Value not translatable to toLocalDateTime: " + raw);
    }
  }

  private static ZonedDateTime toZonedDateTime(String raw) {
    if (raw.matches(FORMAT_DATE_ONLY)) {
      return LocalDate.parse(raw, FORMATTER_DATE_ONLY).atTime(LocalTime.of(0, 0))
          .atZone(ZoneId.systemDefault());
    } else if (raw.matches(FORMAT_LOCAL_DATE_TIME_NANO)) {
      return LocalDateTime.parse(fillRight(raw, 23, "0"), FORMATTER_LOCAL_DATE_TIME_NANO)
          .atZone(ZoneId.systemDefault());
    } else if (raw.matches(FORMAT_LOCAL_DATE_TIME)) {
      return LocalDateTime.parse(raw, FORMATTER_LOCAL_DATE_TIME).atZone(ZoneId.systemDefault());
    } else if (raw.matches(FORMAT_ZONED_DATE_TIME)) {
      return ZonedDateTime.parse(raw);
    } else {
      throw new ActaModelTranslationException("Value not translatable to ZonedDateTime: " + raw);
    }
  }

  private static OffsetDateTime toOffsetDateTime(String raw) {
    if (raw.matches(FORMAT_DATE_ONLY)) {
      return LocalDate.parse(raw, FORMATTER_DATE_ONLY).atTime(LocalTime.of(0, 0))
          .atZone(ZoneId.systemDefault()).toOffsetDateTime();
    } else if (raw.matches(FORMAT_LOCAL_DATE_TIME_NANO)) {
      return LocalDateTime.parse(fillRight(raw, 23, "0"), FORMATTER_LOCAL_DATE_TIME_NANO)
          .atZone(ZoneId.systemDefault()).toOffsetDateTime();
    } else if (raw.matches(FORMAT_LOCAL_DATE_TIME)) {
      return LocalDateTime.parse(raw, FORMATTER_LOCAL_DATE_TIME).atZone(ZoneId.systemDefault())
          .toOffsetDateTime();
    } else if (raw.matches(FORMAT_ZONED_DATE_TIME)) {
      return OffsetDateTime.parse(raw);
    } else {
      throw new ActaModelTranslationException("Value not translatable to OffsetDateTime: " + raw);
    }
  }

  private static java.util.Date toDate(String raw) {
    ZonedDateTime converted;

    try {
      converted = toZonedDateTime(raw);
    } catch (ActaModelTranslationException e) {
      throw new ActaModelTranslationException("Value not translatable to java.util.Date: " + raw,
          e);
    }

    return java.util.Date.from(converted.toInstant());
  }

  private static final String FORMAT_DATE_ONLY = "[0-9]{1,2}/[0-9]{1,2}/[0-9]{1,4}";

  private static final DateTimeFormatter FORMATTER_DATE_ONLY =
      DateTimeFormatter.ofPattern("d/MM/yyyy");

  private static final String FORMAT_LOCAL_DATE_TIME =
      "[0-9]{1,4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}\\:[0-9]{1,2}\\:[0-9]{1,2}";

  private static final DateTimeFormatter FORMATTER_LOCAL_DATE_TIME =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private static final String FORMAT_LOCAL_DATE_TIME_NANO =
      "[0-9]{1,4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}\\:[0-9]{1,2}\\:[0-9]{1,2}\\.[0-9]+";

  private static final DateTimeFormatter FORMATTER_LOCAL_DATE_TIME_NANO =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  private static final String FORMAT_ZONED_DATE_TIME =
      "[0-9]{1,4}-[0-9]{1,2}-[0-9]{1,2}T[0-9]{1,2}\\:[0-9]{1,2}\\:[0-9]{1,2}.*";


}

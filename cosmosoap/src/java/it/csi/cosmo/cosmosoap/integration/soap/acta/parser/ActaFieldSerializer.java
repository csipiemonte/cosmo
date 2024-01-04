/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import it.csi.cosmo.cosmosoap.integration.soap.acta.exception.ActaModelTranslationException;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.ActaClientContext;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.ActaVitalRecordCodes;
import it.doqui.acta.acaris.archive.EnumTipologiaNumerazioneType;
import it.doqui.acta.acaris.management.VitalRecordCodeType;


/**
 *
 */

public class ActaFieldSerializer {

  public static String[] getConvertedValueFromClassAndType(Object value) {
    if (value == null) {
      return new String[] {};
    }

    Class<?> fromClass = value.getClass();

    if (Collection.class.isAssignableFrom(fromClass)) {
      return convertCollection((Collection<?>) value);
    }

    if (String.class.isAssignableFrom(fromClass)) {
      return new String[] {(String) value};

    } else if (Long.class.isAssignableFrom(fromClass)) {
      return new String[] {value.toString()};

    } else if (Integer.class.isAssignableFrom(fromClass)) {
      return new String[] {value.toString()};

    } else if (Boolean.class.isAssignableFrom(fromClass)) {
      return new String[] {value.toString()};

    } else if (java.util.Date.class.isAssignableFrom(fromClass)) {
      return new String[] {fromDate((java.util.Date) value)};

    } else if (LocalDate.class.isAssignableFrom(fromClass)) {
      return new String[] {fromLocalDate((LocalDate) value)};

    } else if (ZonedDateTime.class.isAssignableFrom(fromClass)) {
      return new String[] {fromZonedDateTime((ZonedDateTime) value)};

    } else if (LocalDateTime.class.isAssignableFrom(fromClass)) {
      return new String[] {fromLocalDateTime((LocalDateTime) value)};

    } else {
      throw new ActaModelTranslationException(
          "Invalid target property class: " + fromClass.getName());
    }
  }

  private static String[] convertCollection(Collection<?> values) {
    List<String> output = new ArrayList<>(values.size());

    for (Object value : values) {
      String[] convVal = getConvertedValueFromClassAndType(value);
      if (convVal.length > 0) {
        output.add(convVal[0]);
      }
    }

    return output.toArray(new String[] {});
  }

  private static String fromLocalDate(LocalDate raw) {
    return raw.format(FORMATTER_DATE_ONLY);
  }

  private static String fromLocalDateTime(LocalDateTime raw) {
    return raw.format(FORMATTER_LOCAL_DATE_TIME_NANO);
  }

  private static String fromZonedDateTime(ZonedDateTime raw) {
    return raw.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }

  private static String fromDate(java.util.Date raw) {
    return fromZonedDateTime(ZonedDateTime.ofInstant(raw.toInstant(), ZoneOffset.UTC));
  }

  private static final DateTimeFormatter FORMATTER_DATE_ONLY =
      DateTimeFormatter.ofPattern("d/MM/yyyy");

  private static final DateTimeFormatter FORMATTER_LOCAL_DATE_TIME_NANO =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  public static String getSerializedCustomField(ActaClientContext context, String propertyName,
      Object content) {
    if (content == null) {
      return null;
    }

    if ("idVitalRecordCode".equals(propertyName)) {

      for (Entry<ActaVitalRecordCodes, VitalRecordCodeType> entry : context.getVrcMap()
          .entrySet()) {
        if (entry.getKey().name().equals(content)) {
          return String.valueOf(entry.getValue().getIdVitalRecordCode().getValue());
        }
      }
      return null;

    } else if ("tipologiaNumerazione".equals(propertyName)) {

      String cstr = (String) content;

      if (cstr.length() > 1) {
        return String.valueOf(EnumTipologiaNumerazioneType.fromValue(cstr).ordinal());
      } else if (cstr.matches("[0-9]+")) {
        return cstr;
      } else {
        throw new RuntimeException("Invalid EnumTipologiaNumerazioneType raw value: " + cstr);
      }

    } else {
      throw new RuntimeException("unsupported custom field: " + propertyName);
    }
  }

}

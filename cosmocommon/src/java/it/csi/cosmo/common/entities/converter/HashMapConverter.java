/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities.converter;

import java.util.HashMap;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import it.csi.cosmo.common.exception.DataConversionException;
import it.csi.cosmo.common.util.ObjectUtils;

/**
 *
 */


@SuppressWarnings("rawtypes")
@Converter
public class HashMapConverter
    implements AttributeConverter<HashMap, String> {

  @Override
  public String convertToDatabaseColumn(HashMap myCustomObject) {
    try {
      return ObjectUtils.getDataMapper().writeValueAsString(myCustomObject);
    } catch (Exception ex) {
      throw new DataConversionException("Errore nella conversione dell'oggetto a JSON", ex);
    }

  }

  @Override
  public HashMap convertToEntityAttribute(String databaseObject) {
    try {
      return ObjectUtils.getDataMapper().readValue(databaseObject, HashMap.class);
    } catch (Exception ex) {
      throw new DataConversionException("Errore nella conversione da JSON ad oggetto", ex);
    }
  }
}

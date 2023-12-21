/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.exception.DataConversionException;
import it.csi.cosmo.common.util.ObjectUtils;

/**
 *
 */


@Converter
public class JsonNodeConverter
    implements AttributeConverter<JsonNode, String> {

  @Override
  public String convertToDatabaseColumn(JsonNode myCustomObject) {
    try {
      return ObjectUtils.getDataMapper().writeValueAsString(myCustomObject);
    } catch (Exception ex) {
      throw new DataConversionException("Errore nella conversione dell'oggetto a JSON", ex);
    }

  }

  @Override
  public JsonNode convertToEntityAttribute(String databaseObject) {
    try {
      if (StringUtils.isBlank(databaseObject)) {
        return null;
      }
      return ObjectUtils.getDataMapper().readTree(databaseObject);
    } catch (Exception ex) {
      throw new DataConversionException("Errore nella conversione da JSON ad oggetto", ex);
    }
  }
}

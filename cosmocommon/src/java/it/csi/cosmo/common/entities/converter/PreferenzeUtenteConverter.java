/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import it.csi.cosmo.common.entities.dto.PreferenzeUtenteEntity;
import it.csi.cosmo.common.exception.DataConversionException;
import it.csi.cosmo.common.util.ObjectUtils;

/**
 *
 */


@Converter
public class PreferenzeUtenteConverter
    implements AttributeConverter<PreferenzeUtenteEntity, String> {

  @Override
  public String convertToDatabaseColumn(PreferenzeUtenteEntity myCustomObject) {
    try {
      return ObjectUtils.getDataMapper().writeValueAsString(myCustomObject);
    } catch (Exception ex) {
      throw new DataConversionException("Errore nella conversione dell'oggetto a JSON", ex);
    }

  }

  @Override
  public PreferenzeUtenteEntity convertToEntityAttribute(String databaseObject) {
    try {
      return ObjectUtils.getDataMapper().readValue(databaseObject, PreferenzeUtenteEntity.class);
    } catch (Exception ex) {
      throw new DataConversionException("Errore nella conversione da JSON ad oggetto", ex);
    }
  }
}

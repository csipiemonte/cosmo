/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.parser;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmosoap.integration.soap.acta.exception.ActaModelTranslationException;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.ActaClientContext;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.ActaVitalRecordCodes;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaProperty;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.EntitaActa;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;
import it.doqui.acta.acaris.archive.EnumTipologiaNumerazioneType;
import it.doqui.acta.acaris.common.PropertiesType;
import it.doqui.acta.acaris.common.PropertyType;
import it.doqui.acta.acaris.common.QueryNameType;
import it.doqui.acta.acaris.common.ValueType;
import it.doqui.acta.acaris.management.VitalRecordCodeType;


/**
 * Classe che mappa gli oggetti java di appoggio con gli stub di acta
 */
public abstract class ActaBuilder {

  protected static final CosmoLogger log =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, ActaBuilder.class.getSimpleName());

  private final static String IDVITALRECORDCODE = "idVitalRecordCode";

  private final static String TIPOLOGIANUMERAZIONE = "tipologiaNumerazione";

  private final static List<String> customProcessableFields =
      Arrays.asList(IDVITALRECORDCODE, TIPOLOGIANUMERAZIONE);

  public static <T> T actaEntityToClass(ActaClientContext context, EntitaActa entity,
      Class<T> targetClass) throws ActaModelTranslationException {

    // creo istanza della classe di output
    T outputObject;
    try {
      outputObject = targetClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new ActaModelTranslationException("Error instantiating class " + targetClass.getName(),
          e);
    }

    // ottengo informazioni sulla classe
    Map<String, Field> translationMap = ActaParser.resolvePropertiesAnnotations(entity.getClass());
    Map<String, Field> targetFields = ActaParser.resolveProperties(targetClass);

    Object output;

    for (Entry<String, Field> entityFieldEntry : translationMap.entrySet()) {
      output = outputObject;
      String nameAnnotatedOnTargetField = entityFieldEntry.getKey();
      Object rawValue =
          ActaParser.getFieldFromEntity(entity, entityFieldEntry.getValue().getName());

      while (rawValue != null && nameAnnotatedOnTargetField.contains(".")) {
        String[] nameAnnotatedOnTargetFieldSplitted = nameAnnotatedOnTargetField.split("\\.");
        Field fieldToCreate;
        log.trace("actaEntityToClass",
            "checking intermediate field " + nameAnnotatedOnTargetFieldSplitted[0]);

        try {
          fieldToCreate =
              ActaParser.getField(output.getClass(), nameAnnotatedOnTargetFieldSplitted[0]);
        } catch (NoSuchFieldException | SecurityException e) {
          throw new ActaModelTranslationException(
              "Error getting intermediate field " + nameAnnotatedOnTargetFieldSplitted[0]
                  + " of class " + output != null ? output.getClass().getName() : "null",
              e);
        }

        Object intermediateValue = ActaParser.getFieldFromEntity(output, fieldToCreate.getName());
        log.trace("actaEntityToClass", "intermediate field value is " + intermediateValue);

        if (intermediateValue == null) {
          Object intermediateFieldValue;
          try {
            intermediateFieldValue = fieldToCreate.getType().newInstance();
          } catch (InstantiationException | IllegalAccessException e) {
            throw new ActaModelTranslationException(
                "Error creating intermediate field " + nameAnnotatedOnTargetFieldSplitted[0]
                    + " for class " + output != null ? output.getClass().getName() : "null",
                e);
          }

          log.trace("actaEntityToClass", "creating intermediate field with class "
              + intermediateFieldValue.getClass().getName());
          ActaParser.setFieldOnEntity(output, nameAnnotatedOnTargetFieldSplitted[0],
              intermediateFieldValue);

          nameAnnotatedOnTargetField =
              nameAnnotatedOnTargetField.substring(nameAnnotatedOnTargetField.indexOf("."));
          log.trace("actaEntityToClass", "reducing target field to " + nameAnnotatedOnTargetField);
          output = intermediateFieldValue;
        } else {
          output = intermediateValue;
        }
      }

      Field targetField = targetFields.get(nameAnnotatedOnTargetField);

      if (targetFields.containsKey(nameAnnotatedOnTargetField)) {

        Object converted;
        log.trace("actaEntityToClass", "\n" + nameAnnotatedOnTargetField + " ["
            + entityFieldEntry.getValue().getType().getName() + "] -> ...");

        if (customProcessableFields.contains(nameAnnotatedOnTargetField)) {
          converted = getConvertedCustomFieldToActa(context, nameAnnotatedOnTargetField, rawValue);

        } else if (targetField.getType().isArray()) {
          converted = ActaFieldConverter.getConvertedValueToArray(rawValue,
              entityFieldEntry.getValue().getType(), targetField.getType());

        } else {
          converted = ActaFieldConverter.getConvertedValue(rawValue,
              entityFieldEntry.getValue().getType(), targetField.getType());
        }

        log.trace("actaEntityToClass", "[" + rawValue + "] -> [" + converted + "]\n");

        ActaParser.setFieldOnEntity(output, nameAnnotatedOnTargetField, converted);
      } else {
        // throw new ActaModelTranslationException ( "Error bulding acta payload from custom
        // entity", e )
        log.trace("actaEntityToClass", "NO SUCH FIELD");
      }
    }

    return outputObject;
  }

  public static <T extends PropertiesType> T actaEntityToProperties(ActaClientContext context,
      EntitaActa entity, Class<T> targetClass) throws ActaModelTranslationException {

    // creo istanza della classe di output
    T output;
    try {
      output = targetClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new ActaModelTranslationException("Error instantiating class " + targetClass.getName(),
          e);
    }

    // ottengo informazioni sulla classe
    Map<String, Field> translationMap = ActaParser.resolvePropertiesAnnotations(entity.getClass());
    Map<String, Field> targetFields = ActaParser.resolveProperties(targetClass);

    for (Entry<String, Field> entityFieldEntry : translationMap.entrySet()) {
      Field targetField = targetFields.get(entityFieldEntry.getKey());
      Object rawValue =
          ActaParser.getFieldFromEntity(entity, entityFieldEntry.getValue().getName());

      if (targetFields.containsKey(entityFieldEntry.getKey())) {

        Object converted;
        log.trace("actaEntityToProperties", "\n" + entityFieldEntry.getKey() + " ["
            + entityFieldEntry.getValue().getType().getName() + "] -> ...");

        if (customProcessableFields.contains(entityFieldEntry.getKey())) {
          converted = getConvertedCustomFieldToActa(context, entityFieldEntry.getKey(), rawValue);

        } else if (targetField.getType().isArray()) {
          converted = ActaFieldConverter.getConvertedValueToArray(rawValue,
              entityFieldEntry.getValue().getType(), targetField.getType());

        } else {
          converted = ActaFieldConverter.getConvertedValue(rawValue,
              entityFieldEntry.getValue().getType(), targetField.getType());
        }

        log.trace("actaEntityToProperties", "[" + rawValue + "] -> [" + converted + "]\n");

        ActaParser.setFieldOnEntity(output, entityFieldEntry.getKey(), converted);
      } else {
        // throw new ActaModelTranslationException ( "Error bulding acta payload from custom
        // entity", e )
        log.trace("actaEntityToProperties", "NO SUCH FIELD");
      }
    }

    return output;
  }

  public static <T extends PropertiesType> List<PropertyType> actaEntityToPropertiesArray(
      ActaClientContext context, EntitaActa entity, Class<T> targetClass)
      throws ActaModelTranslationException {

    String actaModel = ActaParser.findActaModelClassNameForClass(entity.getClass());

    // creo istanza della classe di output
    List<PropertyType> output = new LinkedList<>();

    // ottengo informazioni sulla classe
    Map<String, Field> translationMap = ActaParser.resolvePropertiesAnnotations(entity.getClass());
    Map<String, Field> targetFields = ActaParser.resolveProperties(targetClass);

    for (Entry<String, Field> entityFieldEntry : translationMap.entrySet()) {
      ActaProperty actaPropertyOnSource =
          entityFieldEntry.getValue().getAnnotation(ActaProperty.class);

      Object rawValue =
          ActaParser.getFieldFromEntity(entity, entityFieldEntry.getValue().getName());

      if (targetFields.containsKey(entityFieldEntry.getKey()) && actaPropertyOnSource != null
          && actaPropertyOnSource.updatable() && (actaPropertyOnSource.className().isEmpty()
              || actaPropertyOnSource.className().equals(actaModel))) {

        PropertyType property = new PropertyType();
        QueryNameType queryValue = new QueryNameType();
        property.setQueryName(queryValue);
        ValueType valueTypeValue = new ValueType();
        property.setValue(valueTypeValue);

        queryValue.setClassName(actaPropertyOnSource.className().isEmpty() ? actaModel
            : actaPropertyOnSource.className());
        queryValue.setPropertyName(entityFieldEntry.getKey());

        String[] converted;
        log.trace("actaEntityToPropertiesArray", "\n" + entityFieldEntry.getKey() + " ["
            + entityFieldEntry.getValue().getType().getName() + "] -> ...");

        if (customProcessableFields.contains(entityFieldEntry.getKey())) {
          String convertedValue = ActaFieldSerializer.getSerializedCustomField(context,
              entityFieldEntry.getKey(), rawValue);
          converted = new String[] {convertedValue};

        } else {
          converted = ActaFieldSerializer.getConvertedValueFromClassAndType(rawValue);
        }

        log.trace("actaEntityToPropertiesArray", "[" + rawValue + "] -> [" + converted + "]\n");

        for (String entry : converted) {
          valueTypeValue.getContent().add(entry);
        }

        output.add(property);

      } else {
        log.trace("actaEntityToPropertiesArray", "NO SUCH FIELD");
      }
    }

    return output;
  }

  private static Object getConvertedCustomFieldToActa(ActaClientContext context,
      String propertyName, Object content) {
    if (content == null) {
      return null;
    }

    if (IDVITALRECORDCODE.equals(propertyName)) {

      for (Entry<ActaVitalRecordCodes, VitalRecordCodeType> entry : context.getVrcMap()
          .entrySet()) {
        if (entry.getKey().name().equals(content)) {
          return entry.getValue().getIdVitalRecordCode();
        }
      }
      return null;
    } else if (TIPOLOGIANUMERAZIONE.equals(propertyName)) {

      String cstr = (String) content;

      if (cstr.length() > 1) {
        return EnumTipologiaNumerazioneType.fromValue(cstr);
      } else if (cstr.matches("[0-9]+")) {
        return EnumTipologiaNumerazioneType.values()[Integer.valueOf(cstr) - 1];
      } else {
        throw new RuntimeException("Invalid EnumTipologiaNumerazioneType raw value: " + cstr);
      }
    } else {
      throw new RuntimeException("unsupported custom field: " + propertyName);
    }
  }
}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmosoap.integration.soap.acta.exception.ActaModelTranslationException;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.ActaClientContext;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.ActaVitalRecordCodes;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaModel;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaProperty;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.EntitaActa;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;
import it.doqui.acta.acaris.archive.EnumTipologiaNumerazioneType;
import it.doqui.acta.acaris.common.PropertyType;
import it.doqui.acta.acaris.management.VitalRecordCodeType;


/**
 *
 */

public abstract class ActaParser {

  protected static final CosmoLogger log =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, ActaParser.class.getSimpleName());

  private final static List<String> customProcessableFields =
      Arrays.asList("idVitalRecordCode", "tipologiaNumerazione");

  public static <T> T actaPropertiesToEntity(ActaClientContext context,
      List<PropertyType> actaProperties, Class<T> targetClass)
      throws ActaModelTranslationException {

    // creo istanza della classe di output
    T output;
    try {
      output = targetClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new ActaModelTranslationException("Error instantiating class " + targetClass.getName(),
          e);
    }

    // ottengo informazioni sulla classe
    Map<String, Field> translationMap = resolvePropertiesAnnotations(targetClass);

    for (PropertyType actaProperty : actaProperties) {
      if (translationMap.containsKey(actaProperty.getQueryName().getPropertyName())) {

        Field correspondingField =
            translationMap.get(actaProperty.getQueryName().getPropertyName());
        ActaProperty annotation = correspondingField.getAnnotation(ActaProperty.class);
        Object convertedValue;

        if (customProcessableFields.contains(actaProperty.getQueryName().getPropertyName())) {
          convertedValue = getConvertedCustomField(context,
              actaProperty.getQueryName().getPropertyName(), actaProperty.getValue().getContent());

        } else {
          convertedValue = ActaFieldDeserializer.getConvertedValueToClassAndType(
              actaProperty.getValue().getContent(), correspondingField.getType(),
              annotation.itemType());
        }

        setFieldOnEntity(output, correspondingField.getName(), convertedValue);
      }
    }

    return output;
  }

  private static Object getConvertedCustomField(ActaClientContext context, String propertyName,
      List<String> contents) {
    if (contents == null || contents.isEmpty()) {
      return null;
    }
    String content = contents.get(0);
    if (StringUtils.isEmpty(content)) {
      return null;
    }

    if ("idVitalRecordCode".equals(propertyName)) {
      if (context == null || context.getVrcMap() == null || context.getVrcMap().isEmpty()) {
        log.warn("getConvertedCustomField",
            "cannot decode idVitalRecordCode without VitalRecordCodes (no context or no vital record codes on provided context) - returned property will be null");
      } else if (!StringUtils.isBlank(content)) {
        long inputId = Long.valueOf(content).longValue();

        for (Entry<ActaVitalRecordCodes, VitalRecordCodeType> entry : context.getVrcMap()
            .entrySet()) {
          if (entry.getValue().getIdVitalRecordCode().getValue() == inputId) {
            return entry.getKey().name();
          }
        }
      }

      return null;
    } else if ("tipologiaNumerazione".equals(propertyName)) {

      if (content.length() > 1) {
        return EnumTipologiaNumerazioneType.fromValue(content).value();
      } else if (content.matches("[0-9]+")) {
        return EnumTipologiaNumerazioneType.values()[Integer.valueOf(content) - 1].value();
      } else {
        throw new RuntimeException("Invalid EnumTipologiaNumerazioneType raw value: " + content);
      }
    } else {
      throw new RuntimeException("unsupported custom field: " + propertyName);
    }
  }

  public static Map<String, Field> resolvePropertiesAnnotations(Class<?> targetClass) {
    Map<String, Field> output = new HashMap<>();

    Set<Field> fields = findFieldsWithAnnotation(targetClass, ActaProperty.class);

    for (Field annotatedFieldOnClass : fields) {
      ActaProperty annotation = annotatedFieldOnClass.getAnnotation(ActaProperty.class);
      output.put(resolveActaPropertyNameForEntityField(annotatedFieldOnClass, annotation),
          annotatedFieldOnClass);
    }

    return output;
  }

  public static Map<String, Field> resolveProperties(Class<?> targetClass) {
    Map<String, Field> output = new HashMap<>();

    Set<Field> fields = findFields(targetClass);

    for (Field annotatedFieldOnClass : fields) {
      output.put(annotatedFieldOnClass.getName(), annotatedFieldOnClass);
    }

    return output;
  }

  private static String resolveActaPropertyNameForEntityField(Field field,
      ActaProperty annotation) {
    if (annotation.propertyName() == null || annotation.propertyName().isEmpty()) {
      return field.getName();
    } else {
      return annotation.propertyName();
    }
  }

  private static Set<Field> findFieldsWithAnnotation(Class<?> targetClass,
      Class<? extends Annotation> withAnnotation) {
    Set<Field> set = new HashSet<>();
    Class<?> c = targetClass;
    while (c != null) {
      for (Field field : c.getDeclaredFields()) {
        if (field.isAnnotationPresent(withAnnotation)) {
          set.add(field);
        }
      }
      c = c.getSuperclass();
    }
    return set;
  }

  private static Set<Field> findFields(Class<?> targetClass) {
    Set<Field> set = new HashSet<>();
    Class<?> c = targetClass;
    while (c != null) {
      for (Field field : c.getDeclaredFields()) {
        set.add(field);
      }
      c = c.getSuperclass();
    }
    return set;
  }

  public static void setFieldOnEntity(Object entity, String fieldName, Object value)
      throws ActaModelTranslationException {
    // TODO verificare efficenza implementazione
    try {
      FieldUtils.writeField(entity, fieldName, value, true);
    } catch (Exception e) {
      throw new ActaModelTranslationException("Error setting " + fieldName + " on model "
          + entity.getClass().getName() + ": " + e.getMessage(), e);
    }
  }

  public static Object getFieldFromEntity(Object entity, String fieldName)
      throws ActaModelTranslationException {
    // TODO verificare efficenza implementazione
    try {
      return FieldUtils.readField(entity, fieldName, true);
    } catch (Exception e) {
      throw new ActaModelTranslationException("Error getting " + fieldName + " on model "
          + entity.getClass().getName() + ": " + e.getMessage(), e);
    }
  }

  public static ActaModel findActaModelAnnotationForClass(Class<? extends EntitaActa> targetClass) {
    Class<?> iteratingClass = targetClass;

    ActaModel output = null;
    do {
      output = iteratingClass.getAnnotation(ActaModel.class);
      iteratingClass = iteratingClass.getSuperclass();
    } while (output == null && iteratingClass != null);

    return output;
  }

  public static String findActaModelClassNameForClass(Class<? extends EntitaActa> targetClass)
      throws ActaModelTranslationException {
    ActaModel model = ActaParser.findActaModelAnnotationForClass(targetClass);
    if (model == null) {
      throw new ActaModelTranslationException(
          "No ActaModel annotated on class " + targetClass.getName() + " or superclasses");
    }

    if (model.className() == null || model.className().isEmpty()) {
      throw new ActaModelTranslationException(
          "No className provided on ActaModel annotation on class " + targetClass.getName()
              + " or superclasses");
    }

    return model.className();
  }

  public static Field getField(Class<?> c, String name) throws NoSuchFieldException {
    Field output = null;
    try {
      output = c.getField(name);
    } catch (NoSuchFieldException e) {
      try {
        output = c.getDeclaredField(name);
      } catch (NoSuchFieldException e2) {
        throw e2;
      }
    }
    return output;
  }

}

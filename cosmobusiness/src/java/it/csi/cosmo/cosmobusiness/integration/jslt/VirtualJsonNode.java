/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import it.csi.cosmo.common.util.ObjectUtils;

/**
 *
 */

public class VirtualJsonNode extends BaseJsonNode {

  private ObjectMapper mapper = ObjectUtils.getDataMapper();
  private Object backingObject;
  private List<PropertyDescriptor> backingObjectPropertiesDescriptors;
  private boolean isAnyCollection;
  private boolean isArray;
  private boolean isEmpty;
  private boolean isMap;

  private Map<String, Object> backingMap = new HashMap<>();
  private Map<String, JsonNode> backingMapNodes = new HashMap<>();

  private List<PropertyDescriptor> backingItemsPropertiesDescriptors;

  private List<JsonNode> backingCollectionNodes = new ArrayList<>();

  public static JsonNode toVirtualJsonNode(Object backingObject) {
    if (backingObject instanceof JsonNode) {
      return (JsonNode) backingObject;
    }
    if (backingObject == null || !needsVirtualization(backingObject.getClass())) {
      var mapper = ObjectUtils.getDataMapper();
      return mapper.valueToTree(backingObject);
    }
    return new VirtualJsonNode(backingObject);
  }

  public static boolean needsVirtualization(Class<?> clazz) {
    if (BeanUtils.isSimpleValueType(clazz)) {
      return false;
    }
    return true;
  }

  public VirtualJsonNode(Object backingObject) {
    this.backingObject = backingObject;

    isAnyCollection = backingObject instanceof Collection || backingObject.getClass().isArray();
    isArray = backingObject.getClass().isArray();
    isMap = backingObject instanceof java.util.Map;

    if (isMap) {
      for (var backingObjectEntry : ((java.util.Map<Object, Object>) backingObject).entrySet()) {
        if (backingObjectEntry.getValue() == null) {
          backingMap.put(backingObjectEntry.getKey().toString(), null);
          backingMapNodes.put(backingObjectEntry.getKey().toString(), toVirtualJsonNode(null));
          continue;
        }

        try {
          if (!needsVirtualization(backingObjectEntry.getValue().getClass())) {
            backingMap.put(backingObjectEntry.getKey().toString(), backingObjectEntry.getValue());
            backingMapNodes.put(backingObjectEntry.getKey().toString(),
                toVirtualJsonNode(backingObjectEntry.getValue()));
          }
        } catch (Exception e) {
          throw new JsltLazyFieldAccessException("Error reading properties from backing object", e);
        }
      }
    } else if (!isAnyCollection) {
      backingObjectPropertiesDescriptors = Arrays
          .stream(
              org.springframework.beans.BeanUtils.getPropertyDescriptors(backingObject.getClass()))
          .filter(p -> p.getReadMethod() != null && p.getReadMethod().canAccess(backingObject))
          .collect(Collectors.toList());

      for (var property : backingObjectPropertiesDescriptors) {
        try {
          if (!needsVirtualization(property.getPropertyType())) {
            var readValue = property.getReadMethod().invoke(backingObject);
            backingMap.put(property.getName(), readValue);
            backingMapNodes.put(property.getName(), toVirtualJsonNode(readValue));
          }
        } catch (Exception e) {
          throw new JsltLazyFieldAccessException("Error reading properties from backing object", e);
        }
      }
    } else {
      @SuppressWarnings("unchecked")
      Collection<Object> inputCollection =
      backingObject instanceof Collection ? ((Collection<Object>) backingObject)
          : Arrays.asList((Object[]) backingObject);
      isEmpty = inputCollection.isEmpty();

      Class<?> inputItemType = isArray ? backingObject.getClass().getComponentType()
          : isEmpty ? null : inputCollection.stream().findFirst().get().getClass();

      if (inputItemType != null) {
        backingItemsPropertiesDescriptors =
            Arrays.stream(org.springframework.beans.BeanUtils.getPropertyDescriptors(inputItemType))
            .filter(p -> p.getReadMethod() != null).collect(Collectors.toList());
      }

      for (Object inputItem : inputCollection) {
        var converted = toVirtualJsonNode(inputItem);
        backingCollectionNodes.add(converted);
      }
    }
  }

  private JsonNode getBackingNode() {
    return isAnyCollection ? mapper.valueToTree(backingCollectionNodes)
        : mapper.valueToTree(backingMap);
  }

  private void unsupported() {
    throw new UnsupportedOperationException("VirtualJsonNode does not support this operation");
  }

  @Override
  public JsonNodeType getNodeType() {
    return isAnyCollection ? JsonNodeType.ARRAY : JsonNodeType.OBJECT;
  }

  @Override
  public boolean isObject() {
    return !this.isAnyCollection;
  }

  @Override
  public boolean isArray() {
    return this.isAnyCollection;
  }

  @Override
  public int size() {
    return this.isAnyCollection ? this.backingCollectionNodes.size()
        : this.backingMap.keySet().size();
  }

  @Override
  public boolean has(String fieldName) {
    if (this.isAnyCollection) {
      this.unsupported();
      return false;
    }

    return backingMap.containsKey(fieldName);
  }

  @Override
  public JsonNode get(String fieldName) {
    if (this.isAnyCollection) {
      this.unsupported();
      return null;
    }

    if (backingMap.containsKey(fieldName)) {
      return backingMapNodes.get(fieldName);
    }

    if (isMap) {
      try {
        var fieldValue = ((java.util.Map) backingObject).get(fieldName);

        backingMap.put(fieldName, fieldValue);

        var fieldNode = toVirtualJsonNode(fieldValue);

        backingMapNodes.put(fieldName, fieldNode);

        return fieldNode;

      } catch (Exception e) {
        throw new JsltLazyFieldAccessException("Error reading field", e);
      }

    } else {

      var property = backingObjectPropertiesDescriptors.stream()
          .filter(p -> p.getName().equals(fieldName)).findFirst()
          .orElseThrow(() -> new JsltLazyFieldAccessException("Unknown or unreadable field "
              + fieldName + " for object " + this.backingObject.getClass()));

      try {
        var fieldValue = property.getReadMethod().invoke(backingObject);

        backingMap.put(fieldName, fieldValue);

        var fieldNode = toVirtualJsonNode(fieldValue);

        backingMapNodes.put(fieldName, fieldNode);

        return fieldNode;

      } catch (Exception e) {
        throw new JsltLazyFieldAccessException("Error reading field", e);
      }
    }

  }

  @Override
  public JsonNode get(int arg0) {
    if (!this.isAnyCollection) {
      this.unsupported();
      return null;
    }

    return backingCollectionNodes.get(arg0);
  }

  // delegated methods

  @Override
  public String toString() {
    return getBackingNode().toString();
  }

  @Override
  public JsonToken asToken() {
    return getBackingNode().asToken();
  }

  @Override
  public NumberType numberType() {
    return null;
  }

  @Override
  public void serialize(JsonGenerator arg0, SerializerProvider arg1) throws IOException {
    this.getBackingNode().serialize(arg0, arg1);
  }

  // unsupported methods

  @Override
  public JsonParser traverse() {
    this.unsupported();
    return null;
  }

  @Override
  public JsonParser traverse(ObjectCodec arg0) {
    this.unsupported();
    return null;
  }

  @Override
  public void serializeWithType(JsonGenerator arg0, SerializerProvider arg1, TypeSerializer arg2)
      throws IOException {
    this.unsupported();
  }

  @Override
  protected JsonNode _at(JsonPointer arg0) {
    this.unsupported();
    return null;
  }

  @Override
  public String asText() {
    this.unsupported();
    return null;
  }

  @Override
  public <T extends JsonNode> T deepCopy() {
    this.unsupported();
    return null;
  }

  @Override
  public boolean equals(Object arg0) {
    this.unsupported();
    return false;
  }

  @Override
  public JsonNode findParent(String arg0) {
    this.unsupported();
    return null;
  }

  @Override
  public List<JsonNode> findParents(String arg0, List<JsonNode> arg1) {
    this.unsupported();
    return null;
  }

  @Override
  public JsonNode findValue(String arg0) {
    this.unsupported();
    return null;
  }

  @Override
  public List<JsonNode> findValues(String arg0, List<JsonNode> arg1) {
    this.unsupported();
    return null;
  }

  @Override
  public List<String> findValuesAsText(String arg0, List<String> arg1) {
    this.unsupported();
    return null;
  }

  @Override
  public JsonNode path(String arg0) {
    this.unsupported();
    return null;
  }

  @Override
  public JsonNode path(int arg0) {
    this.unsupported();
    return null;
  }

  @Override
  public int hashCode() {
    return backingObject.hashCode();
  }

  @Override
  public Iterator<JsonNode> elements() {
    return this.backingCollectionNodes.iterator();
  }
}

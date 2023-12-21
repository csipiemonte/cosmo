/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


/**
 * Classe per funzioni di utilities comuni a tutto l'applicativo
 */
public abstract class ObjectUtils {

  private ObjectUtils() {
    // NOP
  }

  /*
   * ObjectMapper usato per stampare, a fini di logging, i JSON delle request
   */
  private static ObjectMapper mapper = null;

  private static ObjectMapper dataMapper = null;

  public static Class<? extends Object> getTargetObject(Object proxy) throws Exception {

    if (AopUtils.isJdkDynamicProxy(proxy)) {
      while ((AopUtils.isJdkDynamicProxy(proxy))) {
        proxy = getTargetObject(((Advised) proxy).getTargetSource().getTarget());
      }
      return proxy.getClass();
    } else if (AopUtils.isCglibProxy(proxy)) {
      Class<?> proxyClass = proxy.getClass().getSuperclass();
      while (AopUtils.isCglibProxy(proxyClass)) {
        proxyClass = proxy.getClass().getSuperclass();
      }
      return proxyClass;
    } else if (proxy.getClass().getCanonicalName().contains("com.sun.proxy.$Proxy")) {
      Class<?>[] interfaces = proxy.getClass().getInterfaces();
      if (interfaces.length > 0) {
        return interfaces[0];
      } else {
        return proxy.getClass();
      }
    } else {
      return proxy.getClass();
    }
  }

  public static synchronized ObjectMapper getMapper() {
    if (mapper == null) {
      mapper = new ObjectMapper();
      mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
      mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
      mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
      mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      mapper.setSerializationInclusion(Include.NON_NULL);
      mapper.registerModule(new JavaTimeModule());
    }

    return mapper;
  }

  public static synchronized ObjectMapper getDataMapper() {
    if (dataMapper == null) {
      dataMapper = new ObjectMapper();
      dataMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      dataMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
      dataMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      dataMapper.setSerializationInclusion(Include.NON_NULL);
      dataMapper.registerModule(new JavaTimeModule());
    }

    return dataMapper;
  }

  public static String toJson(Object raw) {
    try {
      return getDataMapper().writeValueAsString(raw);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error writing JSON from object", e); // NOSONAR
    }
  }

  public static byte[] json2Byte(Object raw) {
    try {
      return getDataMapper().writeValueAsBytes(raw);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error writing JSON from object", e); // NOSONAR
    }
  }

  public static <T> T fromJson(String json, Class<T> clazz) {
    try {
      return getDataMapper().readValue(json, clazz);
    } catch (IOException e) {
      throw new RuntimeException("Error reading from JSON", e); // NOSONAR
    }
  }

  public static String represent(Object raw) {
    if (raw == null) {
      return "null";
    }
    try {
      if (raw instanceof String) {
        return "\"" + (String) raw + "\"";
      } else {
        if (raw.getClass().getName().startsWith("it.csi")) {
          return raw.getClass().getName() + "#"
              + getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(raw);
        } else if (raw instanceof Collection) {
          StringBuilder stringCollection = new StringBuilder();
          stringCollection.append(raw.getClass().getName());
          stringCollection.append("#[\n");
          for (Object o : ((Collection<?>) raw)) {
            stringCollection.append("\t");
            stringCollection.append(represent(o));
            stringCollection.append(",");
          }
          stringCollection.append("\n]");
          return stringCollection.toString();

        } else if (raw.getClass().isArray()
            && raw.getClass().getComponentType().getName().startsWith("it.csi")) {
          return raw.getClass().getComponentType().getName() + "[]#"
              + getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(raw);
        } else if (raw instanceof Map) {
          return raw.getClass().getName() + "#"
              + getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(raw);
        } else {
          return raw.toString();
        }
      }
    } catch (IOException e) {
      return String.valueOf(raw);
    }
  }

  public static boolean differ(Object id1, Object id2) {
    if (id1 == null && id2 == null) {
      return false;
    } else if (id1 == null || id2 == null) {
      return true;
    } else {
      return !id1.equals(id2);
    }
  }

  public static String getIdFromLink(String raw) {
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    raw = raw.strip();
    while (raw.startsWith("/")) {
      raw = raw.substring(1).strip();
    }
    if (raw.contains("/")) {
      return raw.substring(raw.indexOf('/') + 1);
    }
    return null;
  }

  public static <T> T coalesce(@SuppressWarnings("unchecked") T... candidates) {
    for (T candidate : candidates) {
      if (candidate != null) {
        return candidate;
      }
    }
    return null;
  }
}

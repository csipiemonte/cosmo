/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.config;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 *
 */

public abstract class CosmoJacksonDialect {

  private CosmoJacksonDialect() {
    // private class
  }

  private static ObjectMapper objectMapper;

  public static synchronized ObjectMapper getDialectMapper() {

    if (objectMapper == null) {
      objectMapper = new ObjectMapper();

      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
      objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

      objectMapper.setSerializationInclusion(Include.NON_NULL);

      objectMapper.registerModule(new JavaTimeModule());
    }

    return objectMapper;
  }

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.csi.cosmo.common.entities.CosmoRIstanzaFormLogicoParametroValore;

/**
 *
 */

public class ValoreParametroFormLogicoWrapper {

  private ObjectMapper mapper;
  private String codice;
  private String rawValue;

  public ValoreParametroFormLogicoWrapper(CosmoRIstanzaFormLogicoParametroValore entity) {
    this.mapper = ObjectUtils.getDataMapper();
    this.rawValue = entity.getValoreParametro();
    this.codice = entity.getCosmoDChiaveParametroFunzionalitaFormLogico().getCodice();
  }

  public String getCodice() {
    return codice;
  }

  public String asString() {
    if (isEmpty()) {
      return null;
    }
    String v = rawValue.strip();
    if (v.startsWith("\"") && v.endsWith("\"")) {
      try {
        return mapper.readValue(v.getBytes(StandardCharsets.UTF_8), String.class);
      } catch (IOException e) {
        throw ExceptionUtils.toChecked(e);
      }
    }
    return v;
  }

  public Boolean asBoolean() {
    if (isEmpty()) {
      return null;
    }
    return Boolean.valueOf(rawValue.strip().toLowerCase());
  }

  public Long asLong() {
    if (isEmpty()) {
      return null;
    }
    return Long.valueOf(rawValue.strip().toLowerCase());
  }

  public JsonNode asObject() {
    if (isEmpty()) {
      return null;
    }
    try {
      return mapper.readTree(rawValue.strip().getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw ExceptionUtils.toChecked(e);
    }
  }

  public boolean isEmpty() {
    return rawValue == null || StringUtils.isBlank(rawValue);
  }

}

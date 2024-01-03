/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.config;

import java.io.IOException;
import java.time.OffsetDateTime;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class DateDeserializer extends StdDeserializer<OffsetDateTime> {

  protected DateDeserializer(Class<?> vc) {
    super(vc);
  }

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @Override
  public OffsetDateTime deserialize(JsonParser json, DeserializationContext ctx)
      throws IOException {
    String stringa = json.getValueAsString();
    return OffsetDateTime.parse(stringa);
  }

}

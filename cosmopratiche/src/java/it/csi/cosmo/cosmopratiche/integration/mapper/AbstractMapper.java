/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.integration.mapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class AbstractMapper {


  public Timestamp toTimestamp(OffsetDateTime src) {
    if (src == null) {
      return null;
    }
    return Timestamp.from(src.toInstant());
  }

  public OffsetDateTime toISO8601(Timestamp timestamp) {
    if (timestamp == null) {
      return null;
    }
    return OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp.getTime()),
        ZoneId.systemDefault());
  }
}

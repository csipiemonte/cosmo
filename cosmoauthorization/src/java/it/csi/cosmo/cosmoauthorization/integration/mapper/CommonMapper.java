/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface CommonMapper {

  default java.time.OffsetDateTime map(java.time.Instant value) {
    if (value == null) {
      return null;
    }
    return OffsetDateTime.ofInstant(value, ZoneId.systemDefault());
  }

  default Timestamp mapTimestamp(OffsetDateTime value) {
    return value == null ? null
        : Timestamp.valueOf(value.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());
  }

  default String mapByteToString(byte[] value) {
    return value == null ? null : new String(value);
  }

  default byte[] mapStringToByte(String value) {
    return StringUtils.isBlank(value) ? null : value.getBytes();
  }
}

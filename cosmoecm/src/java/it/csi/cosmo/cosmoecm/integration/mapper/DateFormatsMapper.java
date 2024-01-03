/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.mapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface DateFormatsMapper {

  default Timestamp mapTimestamp(OffsetDateTime value) {
    return value == null ? null
        : Timestamp.valueOf(value.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());
  }

  default OffsetDateTime mapOffsetDateTime(Timestamp value) {
    return value == null ? null
        : OffsetDateTime.ofInstant(Instant.ofEpochMilli(value.getTime()), ZoneId.systemDefault());
  }

  default java.time.OffsetDateTime map(java.time.LocalDate value) {
    return value == null ? null
        : value.atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime();
  }

  default java.time.OffsetDateTime map(java.time.LocalDateTime value) {
    return value == null ? null : value.atZone(ZoneId.systemDefault()).toOffsetDateTime();
  }

}

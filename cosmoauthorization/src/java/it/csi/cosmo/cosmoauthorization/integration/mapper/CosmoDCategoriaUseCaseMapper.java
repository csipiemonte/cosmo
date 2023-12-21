/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDCategoriaUseCase;
import it.csi.cosmo.cosmoauthorization.dto.rest.CategoriaUseCase;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {CommonMapper.class},
componentModel = "spring")
public interface CosmoDCategoriaUseCaseMapper {

  CategoriaUseCase toDTO(CosmoDCategoriaUseCase input);

  @Mapping(ignore = true, target = "dtInizioVal")
  @Mapping(ignore = true, target = "dtFineVal")
  @Mapping(ignore = true, target = "cosmoDUseCases")
  CosmoDCategoriaUseCase toRecord(CategoriaUseCase input);

  default Timestamp mapTimestamp(OffsetDateTime value) {
    return value == null ? null
        : Timestamp.valueOf(value.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());
  }

  default OffsetDateTime mapOffsetDateTime(Timestamp value) {
    return value == null ? null
        : OffsetDateTime.ofInstant(Instant.ofEpochMilli(value.getTime()), ZoneId.systemDefault());
  }


}

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
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTProfilo;
import it.csi.cosmo.cosmoauthorization.dto.rest.Profilo;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
uses = {CommonMapper.class, CosmoDUseCaseMapper.class, CosmoRUtenteProfiloMapper.class},
componentModel = "spring")
public interface CosmoTProfiloMapper {

  @Named("profilo_toDTO")
  @Mapping(source = "cosmoDUseCases", target = "useCases")
  Profilo toDTO(CosmoTProfilo input);

  @Mapping(source = "useCases", target = "cosmoDUseCases")
  @Mapping(ignore = true, target = "cosmoRUtenteProfilos")
  @Mapping(ignore = true, target = "dtCancellazione")
  @Mapping(ignore = true, target = "dtInserimento")
  @Mapping(ignore = true, target = "dtUltimaModifica")
  @Mapping(ignore = true, target = "utenteCancellazione")
  @Mapping(ignore = true, target = "utenteInserimento")
  @Mapping(ignore = true, target = "utenteUltimaModifica")
  @Mapping(ignore = true, target = "assegnabile")
  CosmoTProfilo toRecord(Profilo input);


  default Timestamp mapTimestamp(OffsetDateTime value) {
    return value == null ? null
        : Timestamp.valueOf(value.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());
  }

  default OffsetDateTime mapOffsetDateTime(Timestamp value) {
    return value == null ? null
        : OffsetDateTime.ofInstant(Instant.ofEpochMilli(value.getTime()), ZoneId.systemDefault());
  }

}

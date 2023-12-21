/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEnte;
import it.csi.cosmo.cosmoauthorization.dto.rest.ConfigurazioneEnte;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {CommonMapper.class},
componentModel = "spring")
public interface CosmoCConfigurazioneEnteMapper {

  @Mapping(ignore = true, target = "dtInizioVal")
  @Mapping(ignore = true, target = "dtFineVal")
  @Mapping(ignore = true, target = "cosmoTEnte")
  @Mapping(ignore = true, target = "id")
  CosmoCConfigurazioneEnte toRecord(ConfigurazioneEnte input);

  @Mapping(source = "id.chiave", target = "chiave")
  ConfigurazioneEnte toDTO(CosmoCConfigurazioneEnte input);

  List<ConfigurazioneEnte> toDTOs(List<CosmoCConfigurazioneEnte> input);

}

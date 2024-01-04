/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.dto.common.ConfigurazioneDTO;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEnte;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface CosmoCConfigurazioneEnteMapper {

  @Mapping(source = "id.chiave", target = "withKey")
  @Mapping(source = "valore", target = "withValue")
  ConfigurazioneDTO toDTO(CosmoCConfigurazioneEnte input);

}

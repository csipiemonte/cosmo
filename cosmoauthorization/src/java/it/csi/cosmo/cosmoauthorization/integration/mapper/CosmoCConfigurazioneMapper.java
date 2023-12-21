/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoCConfigurazione;
import it.csi.cosmo.cosmoauthorization.dto.rest.ParametroDiSistema;
import it.csi.cosmo.common.dto.common.ConfigurazioneDTO;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {CommonMapper.class},
componentModel = "spring")
public interface CosmoCConfigurazioneMapper {

  @Mapping(source = "chiave", target = "withKey")
  @Mapping(source = "valore", target = "withValue")
  ConfigurazioneDTO toDTO(CosmoCConfigurazione input);

  @Mapping(source = "key", target = "chiave")
  @Mapping(source = "value", target = "valore")
  @Mapping(ignore = true, target = "descrizione")
  @Mapping(ignore = true, target = "dtInizioVal")
  @Mapping(ignore = true, target = "dtFineVal")
  CosmoCConfigurazione toRecord(ConfigurazioneDTO input);


  ParametroDiSistema toDTOLight(CosmoCConfigurazione input);


}

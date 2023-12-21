/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoRUtenteEnte;
import it.csi.cosmo.cosmoauthorization.dto.rest.AssociazioneEnteUtente;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
uses = {CommonMapper.class, CosmoTEnteMapper.class, CosmoTUtenteMapper.class},
componentModel = "spring")
public interface CosmoRUtenteEnteMapper {

  @Mapping(source = "cosmoTEnte", target = "ente", qualifiedByName = "ente_toDTO")
  AssociazioneEnteUtente toDTO(CosmoRUtenteEnte input);

  @Mapping(source = "ente", target = "cosmoTEnte")
  @Mapping(ignore = true, target = "id")
  @Mapping(ignore = true, target = "cosmoTUtente")
  @Mapping(ignore = true, target = "dtInizioVal")
  @Mapping(ignore = true, target = "dtFineVal")
  CosmoRUtenteEnte toRecord(AssociazioneEnteUtente input);

}

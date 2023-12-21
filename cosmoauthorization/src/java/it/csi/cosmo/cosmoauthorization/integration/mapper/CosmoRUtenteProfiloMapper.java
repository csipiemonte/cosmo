/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoRUtenteProfilo;
import it.csi.cosmo.cosmoauthorization.dto.rest.AssociazioneUtenteProfilo;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
uses = {CommonMapper.class, CosmoTEnteMapper.class, CosmoTUtenteMapper.class,
    CosmoTProfiloMapper.class},
componentModel = "spring")
public interface CosmoRUtenteProfiloMapper {

  @Mapping(source = "cosmoTEnte", target = "ente", qualifiedByName = "ente_toDTOSenzaGruppiELogo")
  @Mapping(source = "cosmoTProfilo", target = "profilo", qualifiedByName = "profilo_toDTO")
  AssociazioneUtenteProfilo toDTO(CosmoRUtenteProfilo input);

  @Mapping(source = "ente", target = "cosmoTEnte")
  @Mapping(source = "profilo", target = "cosmoTProfilo")
  @Mapping(ignore = true, target = "id")
  @Mapping(ignore = true, target = "cosmoTUtente")
  @Mapping(ignore = true, target = "dtInizioVal")
  @Mapping(ignore = true, target = "dtFineVal")
  CosmoRUtenteProfilo toRecord(AssociazioneUtenteProfilo input);

}

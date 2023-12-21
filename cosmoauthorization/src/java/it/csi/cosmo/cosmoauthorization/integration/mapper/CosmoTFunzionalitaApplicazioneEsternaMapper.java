/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTFunzionalitaApplicazioneEsterna;
import it.csi.cosmo.cosmoauthorization.dto.rest.FunzionalitaApplicazioneEsterna;
import it.csi.cosmo.cosmoauthorization.dto.rest.FunzionalitaApplicazioneEsternaConValidita;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
uses = {CommonMapper.class},
componentModel = "spring")
public interface CosmoTFunzionalitaApplicazioneEsternaMapper {

  @Mapping(ignore = true, target = "campiTecnici")
  @Mapping(ignore = true, target = "associataUtenti")
  FunzionalitaApplicazioneEsternaConValidita toDTOconValidita(
      CosmoTFunzionalitaApplicazioneEsterna input);

  FunzionalitaApplicazioneEsterna toDTO(CosmoTFunzionalitaApplicazioneEsterna input);

  @Mapping(ignore = true, target = "dtCancellazione")
  @Mapping(ignore = true, target = "dtInserimento")
  @Mapping(ignore = true, target = "dtUltimaModifica")
  @Mapping(ignore = true, target = "utenteCancellazione")
  @Mapping(ignore = true, target = "utenteInserimento")
  @Mapping(ignore = true, target = "utenteUltimaModifica")
  @Mapping(ignore = true, target = "cosmoREnteApplicazioneEsterna")
  @Mapping(ignore = true, target = "cosmoRUtenteFunzionalitaApplicazioneEsternas")
  @Mapping(ignore = true, target = "cosmoREnteFunzionalitaApplicazioneEsternas")
  CosmoTFunzionalitaApplicazioneEsterna toRecord(FunzionalitaApplicazioneEsterna input);

  @Mapping(ignore = true, target = "dtCancellazione")
  @Mapping(ignore = true, target = "dtInserimento")
  @Mapping(ignore = true, target = "dtUltimaModifica")
  @Mapping(ignore = true, target = "utenteCancellazione")
  @Mapping(ignore = true, target = "utenteInserimento")
  @Mapping(ignore = true, target = "utenteUltimaModifica")
  @Mapping(ignore = true, target = "cosmoREnteApplicazioneEsterna")
  @Mapping(ignore = true, target = "cosmoRUtenteFunzionalitaApplicazioneEsternas")
  @Mapping(ignore = true, target = "cosmoREnteFunzionalitaApplicazioneEsternas")
  CosmoTFunzionalitaApplicazioneEsterna toRecord(FunzionalitaApplicazioneEsternaConValidita input);
}

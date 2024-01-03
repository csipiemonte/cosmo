/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDFormatoFile;
import it.csi.cosmo.cosmopratiche.dto.rest.FormatoFile;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {AbstractMapper.class},
    componentModel = "spring")
public interface CosmoDFormatoFileMapper {

  @Mapping(ignore = true, target = "formatoFileProfiloFeqTipoFirma")
  FormatoFile toDTO(CosmoDFormatoFile input);

  List<FormatoFile> toDTOs(List<CosmoDFormatoFile> input);

  @Mapping(target = "dtInizioVal", ignore = true)
  @Mapping(target = "dtFineVal", ignore = true)
  @Mapping(target = "supportaSbustamento", ignore = true)
  @Mapping(target = "cosmoRFormatoFileProfiloFeqTipoFirmas", ignore = true)
  @Mapping(target = "uploadConsentito", ignore = true)
  @Mapping(target = "estensioneDefault", ignore = true)
  CosmoDFormatoFile toRecord(FormatoFile input);

}

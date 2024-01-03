/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDFormatoFile;
import it.csi.cosmo.cosmoecm.dto.rest.FormatoFile;
import it.csi.cosmo.cosmoecm.dto.rest.RaggruppamentoFormatoFile;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
uses = {CosmoRFormatoFileProfiloFeqTipoFirmaMapper.class}, componentModel = "spring")
public interface CosmoDFormatoFileMapper {

  @Mapping(target = "formatoFileProfiloFeqTipoFirma",
      source = "cosmoRFormatoFileProfiloFeqTipoFirmas")
  FormatoFile toDTO(CosmoDFormatoFile input);

  @Mapping(ignore = true, target = "raggruppato")
  RaggruppamentoFormatoFile toGroupedDTO(CosmoDFormatoFile input);

}

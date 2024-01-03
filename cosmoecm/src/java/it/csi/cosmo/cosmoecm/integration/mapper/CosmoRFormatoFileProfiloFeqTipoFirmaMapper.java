/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoRFormatoFileProfiloFeqTipoFirma;
import it.csi.cosmo.cosmoecm.dto.rest.FormatoFileProfiloFeqTipoFirma;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface CosmoRFormatoFileProfiloFeqTipoFirmaMapper {

  @Mapping(source = "cosmoDProfiloFeq", target = "profiloFeq")
  @Mapping(source = "cosmoDTipoFirma", target = "tipoFirma")
  FormatoFileProfiloFeqTipoFirma toDTO(CosmoRFormatoFileProfiloFeqTipoFirma input);


}

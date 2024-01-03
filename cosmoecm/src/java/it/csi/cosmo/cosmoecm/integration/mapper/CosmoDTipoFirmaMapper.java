/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDTipoFirma;
import it.csi.cosmo.cosmoecm.dto.rest.TipoFirma;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface CosmoDTipoFirmaMapper {

  TipoFirma toDTO(CosmoDTipoFirma input);

}

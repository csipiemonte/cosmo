/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDHelperPagina;
import it.csi.cosmo.cosmonotifications.dto.rest.CodicePagina;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring", uses = {CosmoDHelperTabMapper.class})
public interface CosmoDHelperPaginaMapper {

  CodicePagina toDTO(CosmoDHelperPagina input);

}

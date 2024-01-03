/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDHelperModale;
import it.csi.cosmo.cosmonotifications.dto.rest.CodiceModale;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface CosmoDHelperModaleMapper {

  @Mapping(ignore = true, target = "codicePagina")
  @Mapping(ignore = true, target = "codiceTab")
  CodiceModale toDTO(CosmoDHelperModale input);
}

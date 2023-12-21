/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDTipoTag;
import it.csi.cosmo.cosmoauthorization.dto.rest.TipoTag;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {DateFormatsMapper.class},
    componentModel = "spring")
public interface CosmoDTipoTagMapper {

  TipoTag toDTO(CosmoDTipoTag input);

  List<TipoTag> toDTOs(List<CosmoDTipoTag> input);

}

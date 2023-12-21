/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTTag;
import it.csi.cosmo.cosmoauthorization.dto.rest.Tag;

/**
*
*/
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
    uses = {CommonMapper.class, CosmoTEnteMapper.class, CosmoTUtenteMapper.class},
    componentModel = "spring")
public interface CosmoTTagMapper {

  @Mapping(source = "cosmoDTipoTag", target = "tipoTag")
  @Mapping(source = "cosmoTEnte", target = "ente")
  @Mapping(target = "utenti", ignore = true)
  Tag toDTO(CosmoTTag input);

}

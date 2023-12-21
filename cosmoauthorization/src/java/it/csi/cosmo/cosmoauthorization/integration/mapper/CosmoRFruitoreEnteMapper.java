/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoRFruitoreEnte;
import it.csi.cosmo.cosmoauthorization.dto.rest.AssociazioneFruitoreEnte;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
uses = {CommonMapper.class, CosmoTEnteMapper.class}, componentModel = "spring")
public interface CosmoRFruitoreEnteMapper {

  @Named("rFruitoreEnte_toDTO")
  @Mapping(source = "cosmoTEnte", target = "ente")
  AssociazioneFruitoreEnte toDTO(CosmoRFruitoreEnte input);

}

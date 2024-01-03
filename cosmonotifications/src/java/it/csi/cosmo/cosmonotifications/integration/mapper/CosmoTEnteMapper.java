/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.cosmonotifications.dto.rest.RiferimentoEnte;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
componentModel = "spring")
public interface CosmoTEnteMapper {

  RiferimentoEnte toRiferimentoDTO(CosmoTEnte input);

}

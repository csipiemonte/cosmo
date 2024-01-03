/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDStatoPratica;
import it.csi.cosmo.common.entities.CosmoRStatoTipoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.StatoPratica;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring",
    uses = {AbstractMapper.class})
public interface CosmoDStatoPraticaMapper {


  StatoPratica toDTO(CosmoDStatoPratica input);

  default StatoPratica toDTO(CosmoRStatoTipoPratica input) {
    if (input == null) {
      return null;
    }
    return this.toDTO(input.getCosmoDStatoPratica());
  }
}

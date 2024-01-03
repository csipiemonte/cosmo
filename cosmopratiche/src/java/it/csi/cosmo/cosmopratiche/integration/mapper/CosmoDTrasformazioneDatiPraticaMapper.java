/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDTrasformazioneDatiPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.TrasformazioneDatiPratica;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring",
    uses = {AbstractMapper.class})
public interface CosmoDTrasformazioneDatiPraticaMapper {

  TrasformazioneDatiPratica toDTO(CosmoDTrasformazioneDatiPratica input);

}

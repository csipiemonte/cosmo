/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDTipoRelazionePratica;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoRelazionePraticaPratica;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring",
uses = {AbstractMapper.class})
public interface CosmoDTipoRelazionePraticaMapper {

  @Mapping(target = "descrizione", source = "descrizione")
  TipoRelazionePraticaPratica toDTOA(CosmoDTipoRelazionePratica input);

  @Mapping(target = "descrizione", source = "descrizioneInversa")
  TipoRelazionePraticaPratica toDTODa(CosmoDTipoRelazionePratica input);

}

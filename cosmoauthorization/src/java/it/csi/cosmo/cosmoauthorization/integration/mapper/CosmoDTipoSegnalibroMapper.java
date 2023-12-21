/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDTipoSegnalibro;
import it.csi.cosmo.cosmoauthorization.dto.rest.TipoSegnalibro;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {DateFormatsMapper.class},
componentModel = "spring")
public interface CosmoDTipoSegnalibroMapper {

  TipoSegnalibro toDTO(CosmoDTipoSegnalibro input);

  List<TipoSegnalibro> toDTO(List<CosmoDTipoSegnalibro> input);

  @Mapping(ignore = true, target = "dtInizioVal")
  @Mapping(ignore = true, target = "dtFineVal")
  CosmoDTipoSegnalibro toRecord(TipoSegnalibro input);

}

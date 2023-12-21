/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDSceltaMarcaTemporale;
import it.csi.cosmo.cosmoauthorization.dto.rest.SceltaMarcaTemporale;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {DateFormatsMapper.class},
componentModel = "spring")
public interface CosmoDSceltaMarcaTemporaleMapper {

  SceltaMarcaTemporale toDTO(CosmoDSceltaMarcaTemporale input);

  List<SceltaMarcaTemporale> toDTO(List<CosmoDSceltaMarcaTemporale> input);

  @Mapping(ignore = true, target = "dtInizioVal")
  @Mapping(ignore = true, target = "dtFineVal")
  @Mapping(ignore = true, target = "cosmoTCertificatoFirmas")
  @Mapping(ignore = true, target = "cosmoDTipoPraticas")
  CosmoDSceltaMarcaTemporale toRecord(SceltaMarcaTemporale input);

}

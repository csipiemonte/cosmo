/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDTipoCredenzialiFirma;
import it.csi.cosmo.cosmoauthorization.dto.rest.TipoCredenzialiFirma;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {DateFormatsMapper.class},
componentModel = "spring")
public interface CosmoDTipoCredenzialiFirmaMapper {

  TipoCredenzialiFirma toDTO(CosmoDTipoCredenzialiFirma input);

  List<TipoCredenzialiFirma> toDTO(List<CosmoDTipoCredenzialiFirma> input);

  @Mapping(ignore = true, target = "dtInizioVal")
  @Mapping(ignore = true, target = "dtFineVal")
  @Mapping(ignore = true, target = "cosmoTCertificatoFirmas")
  @Mapping(ignore = true, target = "cosmoDTipoPraticas")
  CosmoDTipoCredenzialiFirma toRecord(TipoCredenzialiFirma input);

}

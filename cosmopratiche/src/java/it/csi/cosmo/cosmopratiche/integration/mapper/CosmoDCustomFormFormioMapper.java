/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDCustomFormFormio;
import it.csi.cosmo.cosmopratiche.dto.rest.CustomForm;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface CosmoDCustomFormFormioMapper {


  @Mapping(target = "codiceTipoPratica", source = "cosmoDTipoPratica.codice")
  CustomForm toDTO(CosmoDCustomFormFormio input);

  List<CustomForm> toDTOs(List<CosmoDCustomFormFormio> input);

  @Mapping(target = "dtInizioVal", ignore = true)
  @Mapping(target = "dtFineVal", ignore = true)
  @Mapping(target = "cosmoDTipoPratica", ignore = true)
  @Mapping(target = "cosmoTHelpers", ignore = true)
  CosmoDCustomFormFormio toRecord(CustomForm input);

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDProfiloFeq;
import it.csi.cosmo.cosmoauthorization.dto.rest.ProfiloFEQ;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {DateFormatsMapper.class},
componentModel = "spring")
public interface CosmoDProfiloFeqMapper {

  ProfiloFEQ toDTO(CosmoDProfiloFeq input);

  List<ProfiloFEQ> toDTO(List<CosmoDProfiloFeq> input);

  @Mapping(ignore = true, target = "dtInizioVal")
  @Mapping(ignore = true, target = "dtFineVal")
  @Mapping(ignore = true, target = "cosmoTCertificatoFirmas")
  @Mapping(ignore = true, target = "cosmoRFormatoFileProfiloFeqTipoFirmas")
  @Mapping(ignore = true, target = "cosmoDTipoPraticas")
  CosmoDProfiloFeq toRecord(ProfiloFEQ input);

}

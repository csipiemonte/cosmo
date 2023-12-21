/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDUseCase;
import it.csi.cosmo.cosmoauthorization.dto.rest.UseCase;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
uses = {CommonMapper.class, CosmoDCategoriaUseCaseMapper.class}, componentModel = "spring")
public interface CosmoDUseCaseMapper {

  @Mapping(source = "cosmoDCategoriaUseCase", target = "codiceCategoria")
  UseCase toDTO(CosmoDUseCase input);

  @Mapping(ignore = true, target = "dtInizioVal")
  @Mapping(ignore = true, target = "dtFineVal")
  @Mapping(source = "codiceCategoria", target = "cosmoDCategoriaUseCase")
  @Mapping(ignore = true, target = "cosmoTProfilos")
  CosmoDUseCase toRecord(UseCase input);

  List<CosmoDUseCase> toRecords(List<UseCase> input);

  List<UseCase> toDTOs(List<CosmoDUseCase> input);

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDTipoTag;
import it.csi.cosmo.common.entities.CosmoRPraticaTag;
import it.csi.cosmo.common.entities.CosmoTTag;
import it.csi.cosmo.cosmopratiche.dto.rest.TagRidotto;
import it.csi.cosmo.cosmopratiche.dto.rest.TagRidottoResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoTag;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
componentModel = "spring")
public interface CosmoRPraticaTagsMapper {

  @Mapping(target = "codice", source = "codice")
  @Mapping(target = "descrizione", source = "descrizione")
  @Mapping(target = "tipoTag", source = "cosmoDTipoTag")
  @Mapping(target = "warning", ignore = true)
  TagRidottoResponse toDTO(CosmoTTag input);
  
  @Mapping(target = "codice", source = "cosmoTTag.codice")
  @Mapping(target = "descrizione", source = "cosmoTTag.descrizione")
  @Mapping(target = "tipoTag", source = "cosmoTTag.cosmoDTipoTag", qualifiedByName = "toTipoTagRidotto")
  TagRidotto toDTOTagRidotto(CosmoRPraticaTag input);
  
  @Named("toTipoTagRidotto")
  @Mapping(target = "codice", ignore = true)
  @Mapping(target = "descrizione", source = "descrizione")
  @Mapping(target = "label", source = "label")
  TipoTag toTipoTagRidotto(CosmoDTipoTag input);
}

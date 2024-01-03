/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoRTipoDocumentoTipoDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.TipoDocumento;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
    uses = {CosmoDTipoDocumentoMapper.class},
    componentModel = "spring")

public interface CosmoRTipoDocumentoTipoDocumentoMapper {

  @Mapping(source = "cosmoDTipoDocumentoAllegato.codice", target = "codice")
  @Mapping(source = "cosmoDTipoDocumentoAllegato.codiceStardas", target = "codiceStardas")
  @Mapping(source = "cosmoDTipoDocumentoAllegato.descrizione", target = "descrizione")
  @Mapping(source = "cosmoDTipoDocumentoAllegato.firmabile", target = "firmabile")
  @Mapping(source = "cosmoDTipoDocumentoAllegato.dimensioneMassima", target = "dimensioneMassima")
  @Mapping(ignore = true, target = "principali")
  @Mapping(ignore = true, target = "allegati")
  @Mapping(ignore = true, target = "formatiFile")
  TipoDocumento toDTO(CosmoRTipoDocumentoTipoDocumento input);

  List<TipoDocumento> toDTOs(List<CosmoRTipoDocumentoTipoDocumento> input);

}

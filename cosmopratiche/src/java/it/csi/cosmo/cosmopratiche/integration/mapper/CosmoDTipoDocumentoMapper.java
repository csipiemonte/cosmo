/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento;
import it.csi.cosmo.common.entities.CosmoRTipodocTipopratica;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoDocumento;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring",
uses = {AbstractMapper.class, CosmoRFormatoFileTipoDocumentoMapper.class, CosmoRTipoDocumentoTipoDocumentoMapper.class })
public interface CosmoDTipoDocumentoMapper {

  @Named("CosmoDTipoDocumento_toLightDTO")
  @Mapping(target = "principali", ignore = true)
  @Mapping(target = "allegati", ignore = true)
  @Mapping(target = "formatiFile", ignore = true)
  TipoDocumento toLightDTO(CosmoDTipoDocumento input);

  @Mapping(target = "principali", ignore = true)
  @Mapping(target = "allegati", ignore = true)
  @Mapping(target = "formatiFile", source = "cosmoRFormatoFileTipoDocumentos")
  TipoDocumento toDTO(CosmoDTipoDocumento input);

  @Named("CosmoRTipodocTipopratica_toLightDTO")
  default TipoDocumento toLightDTO(CosmoRTipodocTipopratica input) {
    if (input == null) {
      return null;
    }
    return this.toLightDTO(input.getCosmoDTipoDocumento());
  }

  default TipoDocumento toDTO(CosmoRTipodocTipopratica input) {
    if (input == null) {
      return null;
    }
    return this.toDTO(input.getCosmoDTipoDocumento());
  }
}

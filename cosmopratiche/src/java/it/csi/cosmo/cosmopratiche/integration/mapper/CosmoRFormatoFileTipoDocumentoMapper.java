/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoRFormatoFileTipoDocumento;
import it.csi.cosmo.cosmopratiche.dto.rest.FormatoFile;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
    uses = {CosmoDTipoDocumentoMapper.class, CosmoDFormatoFileMapper.class},
    componentModel = "spring")

public interface CosmoRFormatoFileTipoDocumentoMapper {

  @Mapping(source = "cosmoDFormatoFile.codice", target = "codice")
  @Mapping(source = "cosmoDFormatoFile.descrizione", target = "descrizione")
  @Mapping(source = "cosmoDFormatoFile.icona", target = "icona")
  @Mapping(source = "cosmoDFormatoFile.mimeType", target = "mimeType")
  @Mapping(source = "cosmoDFormatoFile.supportaAnteprima", target = "supportaAnteprima")
  @Mapping(ignore = true, target = "formatoFileProfiloFeqTipoFirma")
  FormatoFile toDTO(CosmoRFormatoFileTipoDocumento input);

}

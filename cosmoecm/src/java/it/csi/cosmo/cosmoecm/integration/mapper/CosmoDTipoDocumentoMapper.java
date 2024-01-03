/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.TipoDocumento;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
uses = {CosmoRFormatoFileTipoDocumentoMapper.class, CosmoRTipoDocumentoTipoDocumentoMapper.class}, componentModel = "spring")
public interface CosmoDTipoDocumentoMapper {

  @Mapping(target = "allegati", ignore = true)
  @Mapping(target = "principali", ignore = true)
  @Mapping(target = "formatiFile", ignore = true)
  TipoDocumento toDTO(CosmoDTipoDocumento input);

  List<TipoDocumento> toDTOs(List<CosmoDTipoDocumento> input);

  @Mapping(target = "dtFineVal", ignore = true)
  @Mapping(target = "dtInizioVal", ignore = true)
  @Mapping(target = "cosmoRTipodocTipopraticas", ignore = true)
  @Mapping(target = "cosmoTDocumentos", ignore = true)
  @Mapping(target = "cosmoRFormatoFileTipoDocumentos", ignore = true)
  @Mapping(target = "cosmoRTipoDocumentoTipoDocumentosAllegato", ignore = true)
  @Mapping(target = "cosmoRTipoDocumentoTipoDocumentosPadre", ignore = true)
  @Mapping(target = "cosmoTTemplateFeas", ignore = true)
  CosmoDTipoDocumento toRecord(TipoDocumento input);

}

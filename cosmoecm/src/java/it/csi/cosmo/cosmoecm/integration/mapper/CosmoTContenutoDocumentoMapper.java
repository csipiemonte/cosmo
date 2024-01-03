/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTAnteprimaContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.AnteprimaContenutoDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.ContenutoDocumento;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
uses = {CosmoDTipoFirmaMapper.class, CosmoTInfoVerificaFirmaMapper.class,
    DateFormatsMapper.class, CosmoDFormatoFileMapper.class},
componentModel = "spring")
public interface CosmoTContenutoDocumentoMapper {

  @Mapping(source = "tipo", target = "tipo")
  @Mapping(ignore = true, target = "infoFirmaFea")
  ContenutoDocumento toDTO(CosmoTContenutoDocumento input);

  @Named(value = "lightMapper")
  @Mapping(ignore = true, target = "tipoFirma")
  @Mapping(ignore = true, target = "dataVerificaFirma")
  @Mapping(ignore = true, target = "infoVerificaFirme")
  @Mapping(ignore = true, target = "esitoVerificaFirma")
  @Mapping(ignore = true, target = "anteprime")
  @Mapping(ignore = true, target = "infoFirmaFea")
  ContenutoDocumento toDTOLight(CosmoTContenutoDocumento input);

  @Mapping(source = "nomeFile", target = "nomeFile")
  @Mapping(source = "shareUrl", target = "shareUrl")
  @Mapping(source = "dimensione", target = "dimensione")
  @Mapping(source = "formatoFile", target = "formatoFile")
  @Mapping(source = "id", target = "id")
  AnteprimaContenutoDocumento toDTO(CosmoTAnteprimaContenutoDocumento input,
      @Context CycleAvoidingMappingContext context);


}

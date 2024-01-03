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
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;
import it.csi.cosmo.cosmoecm.dto.rest.DocumentoFruitore;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
uses = {CosmoDTipoDocumentoMapper.class, CosmoTContenutoDocumentoMapper.class,
    DateFormatsMapper.class},
componentModel = "spring")
public interface CosmoTDocumentoMapper {

  @Mapping(source = "pratica.id", target = "idPratica")
  @Mapping(source = "idDocParentExt", target = "idDocumentoParentExt")
  @Mapping(source = "tipo", target = "tipo")
  @Mapping(source = "stato", target = "stato")
  @Mapping(source = "dtUltimaModifica", target = "ultimaModifica")
  @Mapping(ignore = true, target = "smistamento")
  @Mapping(source = "documentiFigli", target = "allegati")
  @Mapping(ignore = true, target = "approvazioni")
  @Mapping(ignore = true, target = "sigillo")
  Documento toDTO(CosmoTDocumento input, @Context CycleAvoidingMappingContext context);

  @Named(value = "lightMapper")
  @Mapping(source = "pratica.id", target = "idPratica")
  @Mapping(source = "idDocParentExt", target = "idDocumentoParentExt")
  @Mapping(source = "tipo", target = "tipo")
  @Mapping(source = "stato", target = "stato")
  @Mapping(source = "dtUltimaModifica", target = "ultimaModifica")
  @Mapping(ignore = true, target = "smistamento")
  @Mapping(ignore = true, target = "sigillo")
  @Mapping(source = "contenuti", target = "contenuti", qualifiedByName = "lightMapper")
  @Mapping(source = "documentiFigli", target = "allegati")
  @Mapping(ignore = true, target = "approvazioni")
  Documento toDTOLight(CosmoTDocumento input, @Context CycleAvoidingMappingContext context);

  @Mapping(source = "idDocumentoExt", target = "id")
  @Mapping(source = "idDocParentExt", target = "idPadre")
  @Mapping(source = "pratica.idPraticaExt", target = "idPratica")
  @Mapping(source = "stato.codice", target = "codiceStato")
  @Mapping(source = "tipo.codice", target = "codiceTipo")
  @Mapping(source = "documentiFigli", target = "allegati")
  DocumentoFruitore toFruitoreDTO(CosmoTDocumento input,
      @Context CycleAvoidingMappingContext context);

}

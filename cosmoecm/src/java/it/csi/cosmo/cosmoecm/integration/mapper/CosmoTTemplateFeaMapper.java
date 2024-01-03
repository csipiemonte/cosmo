/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTTemplateFea;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateFea;


/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
componentModel = "spring")

public interface CosmoTTemplateFeaMapper {

  @Mapping(source = "tipologiaPratica.codice", target = "tipoPratica.codice")
  @Mapping(source = "tipologiaDocumento.codice", target = "tipoDocumento.codice")
  @Mapping(ignore = true, target = "tipoDocumento.allegati")
  @Mapping(ignore = true, target = "tipoDocumento.principali")
  @Mapping(ignore = true, target = "tipoDocumento.formatiFile")
  @Mapping(ignore = true, target = "tipoPratica.ente")
  @Mapping(ignore = true, target = "tipoPratica.stati")
  @Mapping(ignore = true, target = "tipoPratica.tipiDocumento")
  @Mapping(ignore = true, target = "tipoPratica.customForm")
  @Mapping(ignore = true, target = "tipoPratica.tabsDettaglio")
  @Mapping(ignore = true, target = "tipoPratica.gruppoCreatore")
  @Mapping(ignore = true, target = "tipoPratica.gruppoSupervisore")
  @Mapping(ignore = true, target = "tipoPratica.immagine")
  @Mapping(ignore = true, target = "tipoPratica.trasformazioni")
  @Mapping(ignore = true, target = "tipoPratica.enteCertificatore")
  @Mapping(ignore = true, target = "tipoPratica.tipoCredenziale")
  @Mapping(ignore = true, target = "tipoPratica.tipoOtp")
  @Mapping(ignore = true, target = "tipoPratica.profiloFEQ")
  @Mapping(ignore = true, target = "tipoPratica.sceltaMarcaTemporale")
  @Mapping(ignore = true, target = "idPratica")
  TemplateFea toDTO(CosmoTTemplateFea input);

}

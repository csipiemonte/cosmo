/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.cosmonotifications.dto.rest.TipoPratica;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
componentModel = "spring")
public interface CosmoDTipoPraticaMapper {

  @Mapping(ignore = true, target = "ente")
  @Mapping(ignore = true, target = "stati")
  @Mapping(ignore = true, target = "tipiDocumento")
  @Mapping(ignore = true, target = "customForm")
  @Mapping(ignore = true, target = "tabsDettaglio")
  @Mapping(ignore = true, target = "codiceFruitoreStardas")
  @Mapping(ignore = true, target = "overrideFruitoreDefault")
  @Mapping(ignore = true, target = "gruppoCreatore")
  @Mapping(ignore = true, target = "gruppoSupervisore")
  @Mapping(ignore = true, target = "immagine")
  @Mapping(ignore = true, target = "enteCertificatore")
  @Mapping(ignore = true, target = "tipoCredenziale")
  @Mapping(ignore = true, target = "tipoOtp")
  @Mapping(ignore = true, target = "profiloFEQ")
  @Mapping(ignore = true, target = "sceltaMarcaTemporale")
  TipoPratica toDTO(CosmoDTipoPratica input);

}

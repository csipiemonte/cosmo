/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.cosmobusiness.dto.rest.Assegnazione;
import it.csi.cosmo.cosmobusiness.dto.rest.Attivita;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaTask;
import it.csi.cosmo.cosmobusiness.dto.rest.Pratica;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
componentModel = "spring")
public interface CosmoPraticheMapper {

  PaginaTask toDTO(it.csi.cosmo.cosmopratiche.dto.rest.PaginaTask input);

  @Mapping(ignore = true, target = "associata")
  @Mapping(ignore = true, target = "tipo.customForm")
  @Mapping(ignore = true, target = "tipo.responsabileTrattamentoStardas")
  @Mapping(ignore = true, target = "tipo.overrideResponsabileTrattamento")
  @Mapping(ignore = true, target = "tipo.codiceFruitoreStardas")
  @Mapping(ignore = true, target = "tipo.overrideFruitoreDefault")
  @Mapping(ignore = true, target = "tipo.gruppoCreatore")
  @Mapping(ignore = true, target = "tipo.immagine")
  @Mapping(ignore = true, target = "tipo.enteCertificatore")
  @Mapping(ignore = true, target = "tipo.tipoCredenziale")
  @Mapping(ignore = true, target = "tipo.tipoOtp")
  @Mapping(ignore = true, target = "tipo.profiloFEQ")
  @Mapping(ignore = true, target = "tipo.sceltaMarcaTemporale")
  Pratica toPratica(it.csi.cosmo.cosmopratiche.dto.rest.Pratica input);

  it.csi.cosmo.cosmopratiche.dto.rest.Pratica praticaBusinessToPratiche(Pratica input);

  @Mapping(ignore = true, target = "campiTecnici")
  it.csi.cosmo.cosmopratiche.dto.rest.Assegnazione businessToPratiche(Assegnazione input);

  @Mapping(ignore = true, target = "campiTecnici")
  it.csi.cosmo.cosmopratiche.dto.rest.Attivita businessToPratiche(Attivita input);

  it.csi.cosmo.cosmopratiche.dto.rest.Pratica businessToPratiche(Pratica input);

}

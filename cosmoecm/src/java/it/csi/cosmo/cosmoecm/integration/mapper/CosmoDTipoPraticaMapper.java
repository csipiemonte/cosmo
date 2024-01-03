/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.cosmoecm.dto.rest.TipoPratica;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring",
uses = {CosmoTEnteMapper.class, CosmoDTipoDocumentoMapper.class})
public interface CosmoDTipoPraticaMapper {

  @Mapping(target = "ente", ignore = true)
  @Mapping(target = "stati", ignore = true)
  @Mapping(target = "tipiDocumento", ignore = true)
  @Mapping(target = "customForm", ignore = true)
  @Mapping(target = "tabsDettaglio", ignore = true)
  @Mapping(target = "annullabile", ignore = true)
  @Mapping(target = "condivisibile", ignore = true)
  @Mapping(target = "gruppoCreatore", ignore = true)
  @Mapping(target = "gruppoSupervisore", ignore = true)
  @Mapping(target = "assegnabile", ignore = true)
  @Mapping(target = "immagine", ignore = true)
  @Mapping(ignore = true, target = "enteCertificatore")
  @Mapping(ignore = true, target = "tipoCredenziale")
  @Mapping(ignore = true, target = "tipoOtp")
  @Mapping(ignore = true, target = "profiloFEQ")
  @Mapping(ignore = true, target = "sceltaMarcaTemporale")
  TipoPratica toLightDTO(CosmoDTipoPratica input);




}

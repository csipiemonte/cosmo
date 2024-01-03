/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTTemplateReport;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateReport;


/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
uses = {CosmoTEnteMapper.class, CosmoDTipoPraticaMapper.class},
componentModel = "spring")

public interface CosmoTTemplateReportMapper {
  TemplateReport toDTO(CosmoTTemplateReport input);

  @Mapping(target = "tipoPratica.trasformazioni", ignore = true)
  @Mapping(target = "tipoPratica.ente", ignore = true)
  @Mapping(target = "tipoPratica.stati", ignore = true)
  @Mapping(target = "tipoPratica.tipiDocumento", ignore = true)
  @Mapping(target = "tipoPratica.customForm", ignore = true)
  @Mapping(target = "tipoPratica.tabsDettaglio", ignore = true)
  @Mapping(target = "tipoPratica.gruppoCreatore", ignore = true)
  @Mapping(target = "tipoPratica.gruppoSupervisore", ignore = true)
  @Mapping(target = "tipoPratica.immagine", ignore = true)
  @Mapping(ignore = true, target = "tipoPratica.enteCertificatore")
  @Mapping(ignore = true, target = "tipoPratica.tipoCredenziale")
  @Mapping(ignore = true, target = "tipoPratica.tipoOtp")
  @Mapping(ignore = true, target = "tipoPratica.profiloFEQ")
  @Mapping(ignore = true, target = "tipoPratica.sceltaMarcaTemporale")
  TemplateReport toDTOsenzaTrasformazioni(CosmoTTemplateReport input);

}

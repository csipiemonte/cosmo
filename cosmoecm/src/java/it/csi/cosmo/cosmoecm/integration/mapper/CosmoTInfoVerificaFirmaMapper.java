/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTInfoVerificaFirma;
import it.csi.cosmo.cosmoecm.dto.rest.InfoVerificaFirma;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
    uses = {CosmoDTipoFirmaMapper.class, DateFormatsMapper.class},
componentModel = "spring")
public interface CosmoTInfoVerificaFirmaMapper {

  @Mapping(source = "cfFirmatario", target = "codiceFiscaleFirmatario")
  @Mapping(source = "dtVerificaFirma", target = "dataVerificaFirma")
  @Mapping(source = "dtApposizione", target = "dataApposizione")
  @Mapping(source = "dtApposizioneMarcaturaTemporale", target = "dataApposizioneMarcaturaTemporale")
  @Mapping(source = "infoVerificaFirmeFiglie", target = "infoVerificaFirme")
  InfoVerificaFirma toDTO(CosmoTInfoVerificaFirma input);

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDEnteCertificatore;
import it.csi.cosmo.cosmoauthorization.dto.rest.EnteCertificatore;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {DateFormatsMapper.class},
componentModel = "spring")
public interface CosmoDEnteCertificatoreMapper {

  EnteCertificatore toDTO(CosmoDEnteCertificatore input);

  List<EnteCertificatore> toDTO(List<CosmoDEnteCertificatore> input);

  @Mapping(ignore = true, target = "dtInizioVal")
  @Mapping(ignore = true, target = "dtFineVal")
  @Mapping(ignore = true, target = "codiceCa")
  @Mapping(ignore = true, target = "codiceTsa")
  @Mapping(ignore = true, target = "cosmoTCertificatoFirmas")
  @Mapping(ignore = true, target = "cosmoREnteCertificatoreEntes")
  @Mapping(ignore = true, target = "cosmoDTipoPraticas")
  CosmoDEnteCertificatore toRecord(EnteCertificatore input);

}

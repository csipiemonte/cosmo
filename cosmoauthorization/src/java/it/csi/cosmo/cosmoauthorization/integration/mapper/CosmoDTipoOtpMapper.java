/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDTipoOtp;
import it.csi.cosmo.cosmoauthorization.dto.rest.TipoOTP;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {DateFormatsMapper.class},
componentModel = "spring")
public interface CosmoDTipoOtpMapper {

  TipoOTP toDTO(CosmoDTipoOtp input);

  List<TipoOTP> toDTO(List<CosmoDTipoOtp> input);

  @Mapping(ignore = true, target = "dtInizioVal")
  @Mapping(ignore = true, target = "dtFineVal")
  @Mapping(ignore = true, target = "cosmoTCertificatoFirmas")
  @Mapping(ignore = true, target = "cosmoDTipoPraticas")
  CosmoDTipoOtp toRecord(TipoOTP input);

}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTMessaggioNotifica;
import it.csi.cosmo.cosmonotifications.dto.rest.ConfigurazioneMessaggioNotifica;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
uses = {CosmoTEnteMapper.class, CosmoDTipoPraticaMapper.class, CosmoDTipoNotificaMapper.class},
componentModel = "spring")
public interface CosmoTMessaggioNotificaMapper {

  @Mapping(target = "tipoMessaggio", source = "cosmoDTipoNotifica")
  @Mapping(target = "ente", source = "cosmoTEnte")
  @Mapping(target = "tipoPratica", source = "cosmoDTipoPratica")
  ConfigurazioneMessaggioNotifica toDTO(CosmoTMessaggioNotifica input);

  @Mapping(target = "testo", ignore = true)
  @Mapping(target = "tipoMessaggio", source = "cosmoDTipoNotifica")
  @Mapping(target = "ente", source = "cosmoTEnte")
  @Mapping(target = "tipoPratica", source = "cosmoDTipoPratica")
  ConfigurazioneMessaggioNotifica toLightDTO(CosmoTMessaggioNotifica input);
}

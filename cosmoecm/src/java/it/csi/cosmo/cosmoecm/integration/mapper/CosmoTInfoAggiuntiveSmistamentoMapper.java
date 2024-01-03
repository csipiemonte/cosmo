/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTInfoAggiuntiveSmistamento;
import it.csi.cosmo.cosmoecm.dto.rest.InfoAggiuntiveSmistamento;

/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface CosmoTInfoAggiuntiveSmistamentoMapper {

  @Mapping(source = "codInformazione", target = "chiave")
  InfoAggiuntiveSmistamento toDTO(CosmoTInfoAggiuntiveSmistamento input);

}

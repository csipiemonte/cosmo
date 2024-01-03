/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.integration.mapper;

import org.springframework.stereotype.Component;
import it.csi.cosmo.common.dto.common.ConfigurazioneDTO;
import it.csi.cosmo.common.dto.common.ConfigurazioneDTO.Builder;
import it.csi.cosmo.common.entities.CosmoCConfigurazione;

/**
 *
 */

@Component
public class CosmoCConfigurazioneMapper {

  public ConfigurazioneDTO toDTO(CosmoCConfigurazione input) {
    if (input == null) {
      return null;
    }

    Builder configurazioneDTO = ConfigurazioneDTO.builder();

    configurazioneDTO.withValue(input.getValore());
    configurazioneDTO.withKey(input.getChiave());

    return configurazioneDTO.build();
  }
}

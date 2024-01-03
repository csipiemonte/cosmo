/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTApprovazione;
import it.csi.cosmo.cosmoecm.dto.rest.ApprovazioneDocumento;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring",uses = {DateFormatsMapper.class})
public interface CosmoTApprovazioneMapper {

  @Mapping(target = "id", source = "id")
  @Mapping(target = "nomeUtente", source = "cosmoTUtente.nome")
  @Mapping(target = "cognomeUtente", source = "cosmoTUtente.cognome")
  @Mapping(target = "nomeAttivita", source = "cosmoTAttivita.nome")
  @Mapping(target = "dataApprovazione", source = "dtApprovazione")
  ApprovazioneDocumento toDto(CosmoTApprovazione input);
}

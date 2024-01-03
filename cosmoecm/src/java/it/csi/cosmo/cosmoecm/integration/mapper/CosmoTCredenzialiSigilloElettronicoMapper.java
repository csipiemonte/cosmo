/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTCredenzialiSigilloElettronico;
import it.csi.cosmo.cosmoecm.dto.rest.CreaCredenzialiSigilloElettronicoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CredenzialiSigilloElettronico;


/**
 *
 */

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface CosmoTCredenzialiSigilloElettronicoMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "dtInserimento", ignore = true)
  @Mapping(target = "utenteInserimento", ignore = true)
  @Mapping(target = "dtUltimaModifica", ignore = true)
  @Mapping(target = "utenteUltimaModifica", ignore = true)
  @Mapping(target = "dtCancellazione", ignore = true)
  @Mapping(target = "utenteCancellazione", ignore = true)
  CosmoTCredenzialiSigilloElettronico toRecord(CreaCredenzialiSigilloElettronicoRequest request);

  CredenzialiSigilloElettronico toDto(CosmoTCredenzialiSigilloElettronico entity);
}

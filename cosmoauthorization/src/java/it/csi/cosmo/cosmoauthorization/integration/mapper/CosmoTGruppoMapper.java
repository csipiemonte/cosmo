/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTGruppo;
import it.csi.cosmo.cosmoauthorization.dto.rest.Gruppo;
import it.csi.cosmo.cosmoauthorization.dto.rest.RiferimentoGruppo;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
uses = {CommonMapper.class, CosmoTEnteMapper.class, CosmoTUtenteMapper.class},
componentModel = "spring")
public interface CosmoTGruppoMapper {

  @Mapping(ignore = true, target = "utenti")
  @Mapping(ignore = true, target = "tipologiePratiche")
  Gruppo toDTO(CosmoTGruppo input);

  RiferimentoGruppo toDTOLight(CosmoTGruppo input);

}

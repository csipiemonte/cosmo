/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTLock;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.cosmobusiness.dto.rest.Lock;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoUtente;


@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring",
    uses = {DateFormatsMapper.class})
public interface LockMapper {

  @Mapping(source = "dtScadenza", target = "dataScadenza")
  @Mapping(ignore = true, target = "utente")
  Lock toDTO(CosmoTLock entity);

  RiferimentoUtente toDTO(CosmoTUtente entity);

}

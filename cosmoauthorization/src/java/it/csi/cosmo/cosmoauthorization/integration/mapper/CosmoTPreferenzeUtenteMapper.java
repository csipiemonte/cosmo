/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTPreferenzeUtente;
import it.csi.cosmo.common.entities.dto.PreferenzeUtenteEntity;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmoauthorization.dto.rest.Preferenza;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {CommonMapper.class},
componentModel = "spring")
public interface CosmoTPreferenzeUtenteMapper {

  @Mapping(source = "valore", target = "valore", qualifiedByName = "toString")
  Preferenza toDTO(CosmoTPreferenzeUtente input);

  @Mapping(ignore = true, target = "cosmoTUtente")
  @Mapping(source = "valore", target = "valore", qualifiedByName = "fromString")
  @Mapping(ignore = true, target = "dtInserimento")
  @Mapping(ignore = true, target = "utenteInserimento")
  @Mapping(ignore = true, target = "dtUltimaModifica")
  @Mapping(ignore = true, target = "utenteUltimaModifica")
  @Mapping(ignore = true, target = "dtCancellazione")
  @Mapping(ignore = true, target = "utenteCancellazione")
  CosmoTPreferenzeUtente toRecord(Preferenza input);

  @Named("fromString")
  default PreferenzeUtenteEntity mapPreferenzeUtente(String input) {

    try {
      return ObjectUtils.getDataMapper().readValue(input, PreferenzeUtenteEntity.class);
    } catch (Exception ex) {
      return null;
    }
  }


  @Named("toString")
  default String mapString(PreferenzeUtenteEntity input) {
    try {
      return ObjectUtils.getDataMapper().writeValueAsString(input);
    } catch (Exception ex) {
      return null;
    }
  }

}

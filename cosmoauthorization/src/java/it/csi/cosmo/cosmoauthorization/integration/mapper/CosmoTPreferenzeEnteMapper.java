/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTPreferenzeEnte;
import it.csi.cosmo.common.entities.dto.PreferenzeEnteEntity;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmoauthorization.dto.rest.Preferenza;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {CommonMapper.class},
componentModel = "spring")
public interface CosmoTPreferenzeEnteMapper {

  @Mapping(source = "valore", target = "valore", qualifiedByName = "toString")
  Preferenza toDTO(CosmoTPreferenzeEnte input);


  @Mapping(ignore = true, target = "cosmoTEnte")
  @Mapping(source = "valore", target = "valore", qualifiedByName = "fromString")
  @Mapping(ignore = true, target = "dtInserimento")
  @Mapping(ignore = true, target = "utenteInserimento")
  @Mapping(ignore = true, target = "dtUltimaModifica")
  @Mapping(ignore = true, target = "utenteUltimaModifica")
  @Mapping(ignore = true, target = "dtCancellazione")
  @Mapping(ignore = true, target = "utenteCancellazione")
  CosmoTPreferenzeEnte toRecord(Preferenza input);

  @Named("fromString")
  default PreferenzeEnteEntity mapPreferenzeEnte(String input) {

    try {
      return ObjectUtils.getDataMapper().readValue(input, PreferenzeEnteEntity.class);
    } catch (Exception ex) {
      throw new BadRequestException("Error parsing JSON", ex);
    }
  }


  @Named("toString")
  default String mapString(PreferenzeEnteEntity input) {
    try {
      return ObjectUtils.getDataMapper().writeValueAsString(input);
    } catch (Exception ex) {
      throw new BadRequestException("Error parsing JSON", ex);
    }
  }


}

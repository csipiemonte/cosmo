/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.mapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheFruitore;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
uses = {AbstractMapper.class, CosmoRPraticaTagsMapper.class},
componentModel = "spring")
public interface CosmoTPraticheFruitoreMapper {
  
  @Mapping(source = "fruitore.apiManagerId", target = "apiManager")
  @Mapping(source = "utenteCreazionePratica", target = "utenteCreazione")
  @Mapping(source = "dataCreazionePratica", target = "dataCreazione", qualifiedByName = "toISO8601")
  @Mapping(source = "stato.descrizione", target = "statoPratica")
  @Mapping(source = "tipo.descrizione", target = "tipoPratica")
  @Mapping(source = "idPraticaExt", target = "idPraticaExt")
  @Mapping(source = "oggetto", target = "oggetto")
  @Mapping(source = "riassunto", target = "riassunto")
  @Mapping(ignore = true, target = "attivita")
  @Mapping(ignore = true , target = "tag")
  PraticheFruitore toDto(CosmoTPratica input);
  
  @Named("toISO8601")
  public default OffsetDateTime toISO8601(Timestamp timestamp) {
    if (timestamp == null) {
      return null;
    }
    return OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp.getTime()),
        ZoneId.systemDefault());
  }
}

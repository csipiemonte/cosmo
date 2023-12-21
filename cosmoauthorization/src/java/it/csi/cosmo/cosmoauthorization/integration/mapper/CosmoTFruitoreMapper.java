/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.integration.mapper;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDAutorizzazioneFruitore;
import it.csi.cosmo.common.entities.CosmoDOperazioneFruitore;
import it.csi.cosmo.common.entities.CosmoTCredenzialiAutenticazioneFruitore;
import it.csi.cosmo.common.entities.CosmoTEndpointFruitore;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.entities.CosmoTSchemaAutenticazioneFruitore;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.cosmoauthorization.dto.rest.AutorizzazioneFruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.CredenzialiAutenticazioneFruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.EndpointFruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.Fruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.OperazioneFruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.SchemaAutenticazioneFruitore;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
uses = {CommonMapper.class, CosmoTEnteMapper.class, CosmoRFruitoreEnteMapper.class},
componentModel = "spring")
public interface CosmoTFruitoreMapper {

  @Mapping(source = "cosmoRFruitoreEntes", target = "enti", qualifiedByName = "rFruitoreEnte_toDTO")
  @Mapping(source = "cosmoDAutorizzazioneFruitores", target = "autorizzazioni")
  Fruitore toDTO(CosmoTFruitore input);

  AutorizzazioneFruitore toDTO(CosmoDAutorizzazioneFruitore input);

  EndpointFruitore toDTO(CosmoTEndpointFruitore newEntity);

  @Mapping(source = "credenziali", target = "credenziali",
      qualifiedByName = "credenzialiValideUnivoche")
  SchemaAutenticazioneFruitore toDTO(CosmoTSchemaAutenticazioneFruitore input);

  @Mapping(source = "password", target = "password", qualifiedByName = "obfuscate")
  @Mapping(source = "clientSecret", target = "clientSecret", qualifiedByName = "obfuscate")
  CredenzialiAutenticazioneFruitore toDTO(CosmoTCredenzialiAutenticazioneFruitore input);

  OperazioneFruitore toDTO(CosmoDOperazioneFruitore input);

  @Named("credenzialiValideUnivoche")
  default CredenzialiAutenticazioneFruitore toDTOCredenzialiValide(
      List<CosmoTCredenzialiAutenticazioneFruitore> input) {
    if (input == null || input.isEmpty()) {
      return null;
    }
    CosmoTCredenzialiAutenticazioneFruitore found = null;
    for (CosmoTCredenzialiAutenticazioneFruitore candidate : input) {
      if (candidate.nonCancellato()) {
        if (found != null) {
          throw new InternalServerException(
              "Credenziali valide multiple per uno schema di autenticazione");
        } else {
          found = candidate;
        }
      }
    }
    return toDTO(found);
  }

  @Named("obfuscate")
  default String obfuscate(String input) {
    if (StringUtils.isBlank(input)) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < input.length(); i++) {
      sb.append("*");
    }
    return sb.toString();
  }

}

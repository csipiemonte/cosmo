/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDStatoCaricamentoPratica;
import it.csi.cosmo.common.entities.CosmoTCaricamentoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.CaricamentoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.StatoCaricamentoPratica;

/**
 *
 */


@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring",
uses = {CosmoTEnteMapper.class, CosmoTPraticheMapper.class})
public interface CosmoTCaricamentoPraticaMapper {

  @Mapping(target = "ente", source = "cosmoTEnte")
  @Mapping(target = "idPratica", ignore = true)
  @Mapping(target = "utente", ignore = true)
  @Mapping(target = "statoCaricamentoPratica", source = "cosmoDStatoCaricamentoPratica")
  @Mapping(target = "elaborazioni", ignore = true)
  @Mapping(target = "hasElaborazioni", ignore = true)
  @Mapping(target = "visualizzaDettaglio", ignore = true)
  CaricamentoPratica toDTO(CosmoTCaricamentoPratica input);

  StatoCaricamentoPratica toDTO(CosmoDStatoCaricamentoPratica input);

  @Mapping(target = "ente", ignore = true)
  @Mapping(target = "utente", ignore = true)
  @Mapping(target = "idPratica", ignore = true)
  @Mapping(target = "nomeFile", ignore = true)
  @Mapping(target = "pathFile", ignore = true)
  @Mapping(target = "statoCaricamentoPratica", source = "cosmoDStatoCaricamentoPratica")
  @Mapping(target = "elaborazioni", ignore = true)
  @Mapping(target = "hasElaborazioni", ignore = true)
  @Mapping(target = "visualizzaDettaglio", ignore = true)
  CaricamentoPratica toLightDTO(CosmoTCaricamentoPratica input);


  List<StatoCaricamentoPratica> toListDTO(List<CosmoDStatoCaricamentoPratica> input);
}

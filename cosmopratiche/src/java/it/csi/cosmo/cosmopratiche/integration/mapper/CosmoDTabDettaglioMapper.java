/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import it.csi.cosmo.common.entities.CosmoDTabDettaglio;
import it.csi.cosmo.common.entities.CosmoRTabDettaglioTipoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.TabsDettaglio;

/**
 *
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring", uses = {AbstractMapper.class})
public interface CosmoDTabDettaglioMapper {


	@Mapping(target = "ordine", ignore = true)
	TabsDettaglio toDTO(CosmoDTabDettaglio input);

	List<TabsDettaglio> toDTO(List<CosmoDTabDettaglio> input);
  
	@Mapping(target = "dtInizioVal", ignore = true)
	@Mapping(target = "dtFineVal", ignore = true)
	@Mapping(target = "cosmoRTabDettaglioTipoPraticas", ignore = true)
	CosmoDTabDettaglio toRecord(TabsDettaglio input);

	
	@Mapping(target = "codice", source = "cosmoDTabDettaglio.codice")
	@Mapping(target = "descrizione", source = "cosmoDTabDettaglio.descrizione")
	TabsDettaglio toDTO(CosmoRTabDettaglioTipoPratica input);

	List<TabsDettaglio> toDTOS(List<CosmoRTabDettaglioTipoPratica> input);

	
}

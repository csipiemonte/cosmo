/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.cosmo.common.entities.CosmoDOperazioneFruitore_;
import it.csi.cosmo.common.entities.CosmoDStatoPratica;
import it.csi.cosmo.common.entities.CosmoDTabDettaglio;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoRTabDettaglioTipoPratica;
import it.csi.cosmo.common.entities.CosmoRTabDettaglioTipoPratica_;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmopratiche.config.ErrorMessages;
import it.csi.cosmo.cosmopratiche.business.service.TabsDettaglioService;
import it.csi.cosmo.cosmopratiche.dto.rest.StatoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.TabsDettaglio;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoPratica;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoDTabDettaglioMapper;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDTabsDettaglioRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDTipoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRTabDettaglioTipoPraticaRepository;
import it.csi.cosmo.cosmopratiche.util.logger.LogCategory;
import it.csi.cosmo.cosmopratiche.util.logger.LoggerFactory;

@Service
@Transactional
public class TabsDettaglioServiceImpl implements TabsDettaglioService {

	private static final String CLASS_NAME = TabsDettaglioServiceImpl.class.getSimpleName();

	private static final CosmoLogger logger = LoggerFactory.getLogger(LogCategory.COSMOPRATICHE_BUSINESS_LOG_CATEGORY,
			CLASS_NAME);

	@Autowired
	private CosmoDTabsDettaglioRepository cosmoDTabsDettaglioRepository;
	
	@Autowired
	private CosmoDTabDettaglioMapper cosmoDTabDettaglioMapper;
	
	@Autowired
	private CosmoRTabDettaglioTipoPraticaRepository cosmoRTabDettaglioTipoPraticaRepository;
	
	@Autowired
	private CosmoDTipoPraticaRepository cosmoDTipoPraticaRepository;

	@Override
	@Transactional(readOnly = true)
	public List<TabsDettaglio> getTabsDettaglio() {
		List<CosmoDTabDettaglio> td = cosmoDTabsDettaglioRepository.findAllActive();
		return cosmoDTabDettaglioMapper.toDTO(td);
	}
	

	@Override
	@Transactional(readOnly = true)
	public List<TabsDettaglio> getTabsDettaglioCodiceTipoPratica(String codiceTipoPratica){
		CosmoDTipoPratica tp = cosmoDTipoPraticaRepository.findOneActive(codiceTipoPratica).orElseThrow(() -> {
	              throw new NotFoundException("Il codice del tipo della pratica '" + codiceTipoPratica + "' non e' stato trovato");
	            });
			List <CosmoRTabDettaglioTipoPratica> tdsp = 
				cosmoRTabDettaglioTipoPraticaRepository.findActiveByField(CosmoRTabDettaglioTipoPratica_.cosmoDTipoPratica, tp,
						new Sort(Direction.ASC, "ordine"));
			return cosmoDTabDettaglioMapper.toDTOS(tdsp);
	
	}
}

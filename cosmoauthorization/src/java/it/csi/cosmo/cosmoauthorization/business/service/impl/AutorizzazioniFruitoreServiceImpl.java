/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.CosmoDAutorizzazioneFruitore;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.cosmoauthorization.business.service.AutorizzazioniFruitoreService;
import it.csi.cosmo.cosmoauthorization.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoauthorization.config.ParametriApplicativo;
import it.csi.cosmo.cosmoauthorization.dto.rest.AutorizzazioneFruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.AutorizzazioniFruitoreResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.FiltroRicercaAutorizzazioniFruitoriDTO;
import it.csi.cosmo.cosmoauthorization.dto.rest.PageInfo;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoTFruitoreMapper;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoDAutorizzazioneFruitoreRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.specifications.CosmoDAutorizzazioniFruitoreSpecifications;

/**
 *
 */

@Service
@Transactional
public class AutorizzazioniFruitoreServiceImpl implements AutorizzazioniFruitoreService {

  @Autowired
  private CosmoDAutorizzazioneFruitoreRepository cosmoDAutorizzazioneFruitoreRepository;

  @Autowired
  private CosmoTFruitoreMapper cosmoTFruitoreMapper;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Override
  @Transactional(readOnly = true)
  public AutorizzazioniFruitoreResponse getAutorizzazioniFruitore(String filter) {
    var output = new AutorizzazioniFruitoreResponse();

    GenericRicercaParametricaDTO<FiltroRicercaAutorizzazioniFruitoriDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter,
          FiltroRicercaAutorizzazioniFruitoriDTO.class);

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(ParametriApplicativo.MAX_PAGE_SIZE).asInteger());

    Page<CosmoDAutorizzazioneFruitore> pageFruitori =
        cosmoDAutorizzazioneFruitoreRepository
            .findAllActive(CosmoDAutorizzazioniFruitoreSpecifications
            .findByFilters(ricercaParametrica.getFilter(), ricercaParametrica.getSort()), paging);

    List<CosmoDAutorizzazioneFruitore> fruitoriSuDB = pageFruitori.getContent();

    List<AutorizzazioneFruitore> fruitori = new LinkedList<>();
    fruitoriSuDB.forEach(fruitoreSuDB -> fruitori.add(cosmoTFruitoreMapper.toDTO(fruitoreSuDB)));
    output.setAutorizzazioni(fruitori);

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      it.csi.cosmo.common.util.SearchUtils.filterFields(fruitori,
          Arrays.asList(ricercaParametrica.getFields().split(",")));
    }

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(pageFruitori.getNumber());
    pageInfo.setPageSize(pageFruitori.getSize());
    pageInfo.setTotalElements(Math.toIntExact(pageFruitori.getTotalElements()));
    pageInfo.setTotalPages(pageFruitori.getTotalPages());

    output.setPageInfo(pageInfo);

    return output;
  }

}

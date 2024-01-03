/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

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
import it.csi.cosmo.common.entities.CosmoDFormatoFile;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.cosmoecm.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoecm.business.service.FormatoFileService;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo;
import it.csi.cosmo.cosmoecm.dto.FiltroRicercaFormatoFileDTO;
import it.csi.cosmo.cosmoecm.dto.rest.FormatoFile;
import it.csi.cosmo.cosmoecm.dto.rest.FormatoFileResponse;
import it.csi.cosmo.cosmoecm.dto.rest.PageInfo;
import it.csi.cosmo.cosmoecm.dto.rest.RaggruppamentoFormatiFileResponse;
import it.csi.cosmo.cosmoecm.dto.rest.RaggruppamentoFormatoFile;
import it.csi.cosmo.cosmoecm.integration.mapper.CosmoDFormatoFileMapper;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDFormatoFileRepository;
import it.csi.cosmo.cosmoecm.integration.repository.specifications.CosmoDFormatoFileSpecifications;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;

/**
 *
 */

@Service
@Transactional
public class FormatoFileServiceImpl implements FormatoFileService {

  private static final String CLASS_NAME = FormatoFileServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoDFormatoFileRepository cosmoDFormatoFileRepository;

  @Autowired
  private CosmoDFormatoFileMapper cosmoDFormatoFileMapper;


  @Override
  @Transactional(readOnly = true)
  public FormatoFileResponse getFormatiFile(String filter) {

    FormatoFileResponse output = new FormatoFileResponse();

    GenericRicercaParametricaDTO<FiltroRicercaFormatoFileDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaFormatoFileDTO.class);

    var size = ParametriApplicativo.MAX_PAGE_SIZE;

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(size).asInteger());

    Page<CosmoDFormatoFile> pageFormatoFile =
        cosmoDFormatoFileRepository.findAllActive(CosmoDFormatoFileSpecifications
            .findByFilters(ricercaParametrica.getFilter(), ricercaParametrica.getSort()), paging);

    List<CosmoDFormatoFile> formatiFileSuDB = pageFormatoFile.getContent();

    List<FormatoFile> formatiFile = new LinkedList<>();

    formatiFileSuDB
        .forEach(formatoFileDB -> formatiFile.add(cosmoDFormatoFileMapper.toDTO(formatoFileDB)));

    output.setFormatiFile(formatiFile);

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      SearchUtils.filterFields(formatiFile, Arrays.asList(ricercaParametrica.getFields().split(",")));
    }

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(pageFormatoFile.getNumber());
    pageInfo.setPageSize(pageFormatoFile.getSize());
    pageInfo.setTotalElements(Math.toIntExact(pageFormatoFile.getTotalElements()));
    pageInfo.setTotalPages(pageFormatoFile.getTotalPages());

    output.setPageInfo(pageInfo);

    return output;
  }


  @Override
  public RaggruppamentoFormatiFileResponse getFormatiFileRaggruppati(String filter) {

    RaggruppamentoFormatiFileResponse output = new RaggruppamentoFormatiFileResponse();
    GenericRicercaParametricaDTO<FiltroRicercaFormatoFileDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaFormatoFileDTO.class);

    var size = ParametriApplicativo.MAX_PAGE_SIZE;

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(size).asInteger());

    Page<Object[]> pageFormatoFile;

    int totCount = cosmoDFormatoFileRepository.countByGroupedDescriptions();
    if (null != ricercaParametrica.getFilter() && null != ricercaParametrica.getFilter().getFullText()) {
      var c = ricercaParametrica.getFilter().getFullText().getContainsIgnoreCase();
      pageFormatoFile =
          cosmoDFormatoFileRepository.findAllActiveGroupedByDescription(c, paging);
    } else {
      pageFormatoFile =
          cosmoDFormatoFileRepository.findAllActiveGroupedByDescription(paging);
    }

    List<RaggruppamentoFormatoFile> formatiFile = new LinkedList<>();
    formatiFile.addAll(castObjectToRaggruppamentoFormatoFile(pageFormatoFile.getContent()));

    output.setFormatiFile(formatiFile);

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      SearchUtils.filterFields(formatiFile,
          Arrays.asList(ricercaParametrica.getFields().split(",")));
    }

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(pageFormatoFile.getNumber());
    pageInfo.setPageSize(pageFormatoFile.getSize());
    pageInfo.setTotalElements(totCount);
    double totPages = (pageInfo.getTotalElements() * 1.0) / pageInfo.getPageSize();
    totPages = Math.ceil(totPages);
    pageInfo.setTotalPages((int) totPages);

    output.setPageInfo(pageInfo);


    return output;
  }

  private List<RaggruppamentoFormatoFile> castObjectToRaggruppamentoFormatoFile(List<Object[]> listObject) {
    List<RaggruppamentoFormatoFile> entities = new LinkedList<>();
    listObject.forEach(o -> {
      RaggruppamentoFormatoFile entity = new RaggruppamentoFormatoFile();
      entity.setDescrizione((String) o[0]);
      entity.setCodice((String) o[1]);
      entity.setMimeType((String) o[2]);

      entities.add(entity);
    });

    return entities;
  }


}

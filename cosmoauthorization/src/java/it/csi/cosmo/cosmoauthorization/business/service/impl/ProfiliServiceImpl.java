/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
import it.csi.cosmo.common.entities.CosmoDUseCase;
import it.csi.cosmo.common.entities.CosmoTProfilo;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.cosmoauthorization.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoauthorization.business.service.ProfiliService;
import it.csi.cosmo.cosmoauthorization.config.ErrorMessages;
import it.csi.cosmo.cosmoauthorization.config.ParametriApplicativo;
import it.csi.cosmo.cosmoauthorization.dto.rest.FiltroRicercaProfiliDTO;
import it.csi.cosmo.cosmoauthorization.dto.rest.PageInfo;
import it.csi.cosmo.cosmoauthorization.dto.rest.ProfiliResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.Profilo;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoTProfiloMapper;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoDUseCaseRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTProfiloRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.specifications.CosmoTProfiloSpecifications;
import it.csi.cosmo.cosmoauthorization.util.CommonUtils;
import it.csi.cosmo.cosmoauthorization.util.logger.LogCategory;
import it.csi.cosmo.cosmoauthorization.util.logger.LoggerFactory;

/**
 *
 */
@Service
@Transactional
public class ProfiliServiceImpl implements ProfiliService {

  private static final String CLASS_NAME = ProfiliServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private CosmoTProfiloRepository cosmoTProfiloRepository;

  @Autowired
  private CosmoDUseCaseRepository cosmoDUseCaseRepository;

  @Autowired
  private CosmoTProfiloMapper cosmoTProfiloMapper;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Override
  @Transactional(readOnly = true)
  public Profilo getProfilo(String id) {

    CommonUtils.validaDatiInput(id, "id profilo");

    CosmoTProfilo profilo = cosmoTProfiloRepository.findOneNotDeleted(Long.valueOf(id)).orElseThrow(
        () -> new NotFoundException(String.format(ErrorMessages.P_PROFILO_NON_TROVATO, id)));

    return cosmoTProfiloMapper.toDTO(profilo);
  }

  @Override
  public ProfiliResponse getProfili(String filter) {

    ProfiliResponse output = new ProfiliResponse();

    GenericRicercaParametricaDTO<FiltroRicercaProfiliDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter,
            FiltroRicercaProfiliDTO.class);

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(ParametriApplicativo.MAX_PAGE_SIZE).asInteger());

    Page<CosmoTProfilo> pageProfili =
        cosmoTProfiloRepository.findAllNotDeleted(CosmoTProfiloSpecifications
            .findByFilters(ricercaParametrica.getFilter(), ricercaParametrica.getSort()), paging);

    List<CosmoTProfilo> profiliSuDB = pageProfili.getContent();

    List<Profilo> profili = new LinkedList<>();
    profiliSuDB.forEach(profiloSuDB -> profili.add(cosmoTProfiloMapper.toDTO(profiloSuDB)));
    output.setProfili(profili);

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      it.csi.cosmo.common.util.SearchUtils.filterFields(profili,
          Arrays.asList(ricercaParametrica.getFields().split(",")));
    }

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(pageProfili.getNumber());
    pageInfo.setPageSize(pageProfili.getSize());
    pageInfo.setTotalElements(Math.toIntExact(pageProfili.getTotalElements()));
    pageInfo.setTotalPages(pageProfili.getTotalPages());

    output.setPageInfo(pageInfo);

    return output;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Profilo createProfilo(Profilo profilo) {
    String methodName = "createProfilo";

    CommonUtils.validaProfilo(profilo);

    CosmoTProfilo profiloDaCreare = cosmoTProfiloRepository.findByCodice(profilo.getCodice());
    if (profiloDaCreare != null) {
      logger.error(methodName,
          String.format(ErrorMessages.P_PROFILO_GIA_ESISTENTE, profilo.getCodice()));
      throw new ConflictException(
          String.format(ErrorMessages.P_PROFILO_GIA_ESISTENTE, profilo.getCodice()));
    }

    CosmoTProfilo profiloDaSalvare = cosmoTProfiloMapper.toRecord(profilo);
    profiloDaSalvare.setAssegnabile(Boolean.TRUE);

    List<CosmoDUseCase> useCasesOnDB = new LinkedList<>();
    profilo.getUseCases().forEach(useCase -> {
      CosmoDUseCase useCaseDB =
          cosmoDUseCaseRepository.findOneActive(useCase.getCodice()).orElseThrow(() -> {
            final String message =
                String.format(ErrorMessages.UC_USECASE_NON_TROVATO, useCase.getCodice());
            logger.error(methodName, message);
            throw new NotFoundException(message);
          });

      useCasesOnDB.add(useCaseDB);
    });

    profiloDaSalvare.setCosmoDUseCases(useCasesOnDB);

    profiloDaSalvare = cosmoTProfiloRepository.save(profiloDaSalvare);

    logger.info(methodName, "Profilo con id " + profiloDaSalvare.getId() + " creato");
    return cosmoTProfiloMapper.toDTO(profiloDaSalvare);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Profilo updateProfilo(String id, Profilo profilo) {
    String methodName = "updateProfilo";
    CommonUtils.validaDatiInput(id, "id profilo");

    CommonUtils.validaDatiInput(id, "id profilo");

    CosmoTProfilo profiloDaAggiornare =
        cosmoTProfiloRepository.findOneNotDeleted(Long.valueOf(id)).orElseThrow(
            () -> new NotFoundException(String.format(ErrorMessages.P_PROFILO_NON_TROVATO, id)));

    profiloDaAggiornare.setDescrizione(profilo.getDescrizione());

    List<CosmoDUseCase> useCasesOnDB = new LinkedList<>();
    profilo.getUseCases().forEach(useCase -> {
      CosmoDUseCase useCaseDB =
          cosmoDUseCaseRepository.findOneActive(useCase.getCodice()).orElseThrow(() -> {
            final String message =
                String.format(ErrorMessages.UC_USECASE_NON_TROVATO, useCase.getCodice());
            logger.error(methodName, message);
            throw new NotFoundException(message);
          });

      useCasesOnDB.add(useCaseDB);
    });

    profiloDaAggiornare.setCosmoDUseCases(useCasesOnDB);

    profiloDaAggiornare = cosmoTProfiloRepository.save(profiloDaAggiornare);

    logger.info(methodName, "Profilo con id " + profiloDaAggiornare.getId() + " aggiornato");
    return cosmoTProfiloMapper.toDTO(profiloDaAggiornare);

  }

  @Override
  public void deleteProfilo(String id) {
    String methodName = "deleteProfilo";

    CommonUtils.validaDatiInput(id, "id profilo");

    CosmoTProfilo profilo = cosmoTProfiloRepository.findOneNotDeleted(Long.valueOf(id)).orElseThrow(
        () -> new NotFoundException(String.format(ErrorMessages.P_PROFILO_NON_TROVATO, id)));

    profilo.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));
    profilo.setUtenteCancellazione(AuditServiceImpl.getPrincipalCode());
    profilo = cosmoTProfiloRepository.save(profilo);

    logger.info(methodName, "Profilo con id {} eliminato", profilo.getId());

  }

}

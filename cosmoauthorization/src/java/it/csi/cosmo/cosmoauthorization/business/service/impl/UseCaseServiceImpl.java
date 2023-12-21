/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service.impl;

import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoDCategoriaUseCase;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmoauthorization.business.service.UseCaseService;
import it.csi.cosmo.cosmoauthorization.config.ErrorMessages;
import it.csi.cosmo.cosmoauthorization.dto.rest.CategorieUseCaseResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.UseCaseResponse;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoDCategoriaUseCaseMapper;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoDUseCaseMapper;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoDCategoriaUseCaseRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoDUseCaseRepository;
import it.csi.cosmo.cosmoauthorization.util.logger.LogCategory;
import it.csi.cosmo.cosmoauthorization.util.logger.LoggerFactory;

/**
 *
 */
@Service
@Transactional
public class UseCaseServiceImpl implements UseCaseService {

  private static final String CLASS_NAME = UseCaseServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private CosmoDCategoriaUseCaseRepository cosmoDCategoriaUseCaseRepository;

  @Autowired
  private CosmoDCategoriaUseCaseMapper cosmoDCategoriaUseCaseMapper;

  @Autowired
  private CosmoDUseCaseRepository cosmoDUseCaseRepository;

  @Autowired
  private CosmoDUseCaseMapper cosmoDUseCaseMapper;
  @Override
  public UseCaseResponse getUseCases(String filter) {

    if (filter == null || filter.isBlank()) {
      UseCaseResponse output = new UseCaseResponse();
      output.setUseCases(cosmoDUseCaseMapper.toDTOs(cosmoDUseCaseRepository.findAllActive()));
      return output;
    }

    return null;
  }

  @Override
  public CategorieUseCaseResponse getUseCase(String codice) {

    String methodName = "getUseCase";

    if (StringUtils.isBlank(codice)) {
      logger.error(methodName, ErrorMessages.UC_CODICE_USECASE_NON_VALORIZZATO);
      throw new BadRequestException(ErrorMessages.UC_CODICE_USECASE_NON_VALORIZZATO);
    }

    CosmoDCategoriaUseCase cosmoDCategoriaUseCase =
        cosmoDCategoriaUseCaseRepository.findOne(codice);
    if (cosmoDCategoriaUseCase == null) {
      logger.error(methodName,
          String.format(ErrorMessages.UC_CODICE_CATEGORIA_USECASE_NON_TROVATO, codice));
      throw new NotFoundException(
          String.format(ErrorMessages.UC_CODICE_CATEGORIA_USECASE_NON_TROVATO, codice));
    }

    CategorieUseCaseResponse output = new CategorieUseCaseResponse();
    output.setCategorieUseCase(
        Arrays.asList(cosmoDCategoriaUseCaseMapper.toDTO(cosmoDCategoriaUseCase)));
    return output;
  }


}

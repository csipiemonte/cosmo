/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service.impl;

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
import it.csi.cosmo.common.entities.CosmoDCustomFormFormio;
import it.csi.cosmo.common.entities.CosmoDCustomFormFormio_;
import it.csi.cosmo.common.entities.CosmoDTipoPratica_;
import it.csi.cosmo.common.entities.proto.CosmoDEntity_;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmopratiche.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmopratiche.business.service.CustomFormsService;
import it.csi.cosmo.cosmopratiche.config.ErrorMessages;
import it.csi.cosmo.cosmopratiche.config.ParametriApplicativo;
import it.csi.cosmo.cosmopratiche.dto.rest.CustomForm;
import it.csi.cosmo.cosmopratiche.dto.rest.CustomFormResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.FiltroRicercaCustomFormDTO;
import it.csi.cosmo.cosmopratiche.dto.rest.PageInfo;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoDCustomFormFormioMapper;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDCustomFormFormioRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.specifications.CosmoDCustomFormSearchHandler;
import it.csi.cosmo.cosmopratiche.util.logger.LogCategory;
import it.csi.cosmo.cosmopratiche.util.logger.LoggerFactory;

/**
 *
 */
@Service
@Transactional
public class CustomFormsServiceImpl implements CustomFormsService {

  private CosmoLogger logger = LoggerFactory
      .getLogger(LogCategory.COSMOPRATICHE_BUSINESS_LOG_CATEGORY, "CustomFormsServiceImpl");

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoDCustomFormFormioRepository customFormFormioRepository;

  @Autowired
  private CosmoDCustomFormFormioMapper customFormFormioMapper;

  @Override
  public void deleteCustomForm(String codice) {
    CosmoDCustomFormFormio form = getForm(codice);

    if (form != null) {
      form.setDtFineVal(Timestamp.valueOf(LocalDateTime.now()));

      customFormFormioRepository.save(form);
    }
  }

  @Override
  public CustomFormResponse getCustomForms(String filter) {

    CustomFormResponse output = new CustomFormResponse();

    GenericRicercaParametricaDTO<FiltroRicercaCustomFormDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaCustomFormDTO.class);

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(ParametriApplicativo.MAX_PAGE_SIZE).asInteger());

    Page<CosmoDCustomFormFormio> pageResult =
        customFormFormioRepository.findAllActive(new CosmoDCustomFormSearchHandler()
            .findByFilters(ricercaParametrica.getFilter(), ricercaParametrica.getSort()), paging);

    List<CosmoDCustomFormFormio> entitiesSuDB = pageResult.getContent();

    List<CustomForm> dtos = new LinkedList<>();
    entitiesSuDB.forEach(e -> dtos.add(customFormFormioMapper.toDTO(e)));
    output.setCustomForms(dtos);

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      SearchUtils.filterFields(dtos,
          Arrays.asList(ricercaParametrica.getFields().split(",")));
    }

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(pageResult.getNumber());
    pageInfo.setPageSize(pageResult.getSize());
    pageInfo.setTotalElements(Math.toIntExact(pageResult.getTotalElements()));
    pageInfo.setTotalPages(pageResult.getTotalPages());
    output.setPageInfo(pageInfo);

    return output;

  }

  @Override
  public CustomForm getCustomForm(String codice) {
    CosmoDCustomFormFormio form = getForm(codice);

    return customFormFormioMapper.toDTO(form);
  }

  @Override
  public CustomForm postCustomForm(CustomForm body) {
    ValidationUtils.require(body, "Custom form");
    ValidationUtils.require(body.getCodice(), "Codice del custom form");
    ValidationUtils.require(body.getCustomForm(), "Json del custom form");

    CosmoDCustomFormFormio customForm = customFormFormioMapper.toRecord(body);
    customForm.setDtInizioVal(Timestamp.valueOf(LocalDateTime.now()));

    customForm = customFormFormioRepository.save(customForm);

    return customFormFormioMapper.toDTO(customForm);
  }

  @Override
  public CustomForm putCustomForm(String codice, CustomForm body) {

    ValidationUtils.require(body, "Custom form");
    ValidationUtils.require(body.getCodice(), "Codice del custom form");
    ValidationUtils.require(body.getCustomForm(), "Json del custom form");

    CosmoDCustomFormFormio form = getForm(codice);

    if (form != null) {
      form.setDescrizione(body.getDescrizione());
      form.setCustomForm(body.getCustomForm());

      form = customFormFormioRepository.save(form);
    }

    return customFormFormioMapper.toDTO(form);
  }

  private CosmoDCustomFormFormio getForm(String codice) {
    final String methodName = "getCustomForm";

    if (codice == null || StringUtils.isBlank(codice)) {
      logger.error(methodName, ErrorMessages.CODICE_CUSTOM_FORM_NON_VALORIZZATO_CORRETTAMENTE);
      throw new BadRequestException(ErrorMessages.CODICE_CUSTOM_FORM_NON_VALORIZZATO_CORRETTAMENTE);
    }

    return customFormFormioRepository.findOneActive(codice)
        .orElseThrow(() -> new NotFoundException(
            String.format(ErrorMessages.CUSTOM_FORM_CON_CODICE_NON_TROVATO, codice)));
  }

  @Override
  public CustomForm getCustomFormFromCodiceTipoPratica(String codiceTipoPratica) {
    final String methodName = "getCustomFormFromCodiceTipoPratica";

    if (codiceTipoPratica == null || StringUtils.isBlank(codiceTipoPratica)) {
      logger.error(methodName, ErrorMessages.CODICE_CUSTOM_FORM_CODICE_TIPO_PRATICA_NON_VALORIZZATO_CORRETTAMENTE);
      throw new BadRequestException(ErrorMessages.CODICE_CUSTOM_FORM_CODICE_TIPO_PRATICA_NON_VALORIZZATO_CORRETTAMENTE);
    }

    var customForms = customFormFormioRepository.findAllActive((root, cq, cb) -> {
      return cb.and(cb.isNull(root.get(CosmoDEntity_.dtFineVal)),
          cb.isNotNull(
              root.get(CosmoDCustomFormFormio_.cosmoDTipoPratica).get(CosmoDTipoPratica_.codice)),
          cb.equal(
              root.get(CosmoDCustomFormFormio_.cosmoDTipoPratica).get(CosmoDTipoPratica_.codice),
              codiceTipoPratica));
    });

    if (customForms == null || customForms.isEmpty()) {
      String message = String.format(ErrorMessages.CUSTOM_FORM_CON_CODICE_TIPO_PRATICA_NON_TROVATO,
          codiceTipoPratica);
      logger.error(methodName, message);
      return null;
    }

    if (customForms.size() > 1) {
      String message = String.format(
          ErrorMessages.TROPPI_CUSTOM_FORM_CON_CODICE_TIPO_PRATICA_ASSOCIATI, codiceTipoPratica);
      logger.error(methodName, message);
      throw new BadRequestException(message);
    }

    return customFormFormioMapper.toDTO(customForms.get(0));

  }
}

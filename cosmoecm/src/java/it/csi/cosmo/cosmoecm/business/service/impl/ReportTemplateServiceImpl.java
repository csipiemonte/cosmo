/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoDTipoPratica_;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTTemplateReport;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmoecm.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoecm.business.service.ReportTemplateService;
import it.csi.cosmo.cosmoecm.config.ErrorMessages;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo;
import it.csi.cosmo.cosmoecm.dto.FiltroRicercaTemplateDTO;
import it.csi.cosmo.cosmoecm.dto.rest.CreaTemplateRequest;
import it.csi.cosmo.cosmoecm.dto.rest.PageInfo;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateReport;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateReportResponse;
import it.csi.cosmo.cosmoecm.integration.mapper.CosmoTTemplateReportMapper;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDTipoPraticaRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTTemplateReportRepository;
import it.csi.cosmo.cosmoecm.integration.repository.specifications.CosmoTTemplateReportSpecifications;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;

/**
 *
 */
@Service
public class ReportTemplateServiceImpl implements ReportTemplateService {

  private static final String CLASSNAME = ReportTemplateServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASSNAME);

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoTTemplateReportRepository cosmoTTemplateReportRepository;

  @Autowired
  private CosmoTTemplateReportMapper cosmoTTemplateReportMapper;

  @Autowired
  private CosmoTEnteRepository cosmoTEnteRepository;

  @Autowired
  private CosmoDTipoPraticaRepository cosmoDTipoPraticaRepository;

  @Override
  @Transactional(readOnly = true)
  public TemplateReportResponse getReportTemplate(String filter) {
    TemplateReportResponse output = new TemplateReportResponse();

    GenericRicercaParametricaDTO<FiltroRicercaTemplateDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaTemplateDTO.class);

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(ParametriApplicativo.MAX_PAGE_SIZE).asInteger());

    Page<CosmoTTemplateReport> pageTemplateReport = cosmoTTemplateReportRepository.findAllNotDeleted(CosmoTTemplateReportSpecifications
        .findByFilters(ricercaParametrica.getFilter(), ricercaParametrica.getSort()), paging);

    List<CosmoTTemplateReport> templateReportSuDB = pageTemplateReport.getContent();

    List<TemplateReport> templateReport = new LinkedList<>();
    templateReportSuDB.forEach(templateSuDB -> templateReport.add(cosmoTTemplateReportMapper.toDTO(templateSuDB)));
    output.setTemplateReport(templateReport);

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      SearchUtils.filterFields(templateReport,
          Arrays.asList(ricercaParametrica.getFields().split(",")));
    }

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(pageTemplateReport.getNumber());
    pageInfo.setPageSize(pageTemplateReport.getSize());
    pageInfo.setTotalElements(Math.toIntExact(pageTemplateReport.getTotalElements()));
    pageInfo.setTotalPages(pageTemplateReport.getTotalPages());
    output.setPageInfo(pageInfo);

    return output;
  }

  @Override
  public TemplateReport creaReportTemplate(CreaTemplateRequest request) {
    String methodName = "creaReportTemplate";

    ValidationUtils.require(request, "request");
    ValidationUtils.validaAnnotations(request);

    // crea il template report su DB
    CosmoTTemplateReport templateDaSalvare = new CosmoTTemplateReport();

    if (request.getIdEnte() != null) {
      CosmoTEnte cosmoTEnte = cosmoTEnteRepository.findOneNotDeleted(request.getIdEnte())
          .orElseThrow(() -> new NotFoundException(
              String.format(ErrorMessages.E_ENTE_NON_TROVATO, request.getIdEnte())));
      templateDaSalvare.setEnte(cosmoTEnte);
    }

    if (request.getCodiceTipoPratica() != null) {
      CosmoDTipoPratica cosmoDTipoPratica = cosmoDTipoPraticaRepository
          .findOneActiveByField(CosmoDTipoPratica_.codice, request.getCodiceTipoPratica())
          .orElseThrow(() -> new NotFoundException(String
              .format(ErrorMessages.D_TIPO_PRATICA_NON_TROVATA, request.getCodiceTipoPratica())));
      templateDaSalvare.setTipoPratica(cosmoDTipoPratica);
    }

    if (request.getCodiceTemplatePadre() != null) {
      templateDaSalvare.setCodiceTemplatePadre(request.getCodiceTemplatePadre());
    }

    templateDaSalvare.setCodice(request.getCodice());
    byte[] result = Base64.getDecoder().decode(request.getSorgenteTemplate());
    templateDaSalvare.setSorgenteTemplate(result);

    CosmoTTemplateReport templateSalvato = cosmoTTemplateReportRepository.save(templateDaSalvare);
    logger.info(methodName, "Template report con id {} creato", templateSalvato.getId());

    return cosmoTTemplateReportMapper.toDTOsenzaTrasformazioni(templateSalvato);

  }

  @Override
  public void deleteReportTemplate(Long id) {
    String methodName = "deleteReportTemplate";
    ValidationUtils.require(id, "id report template");

    CosmoTTemplateReport templateDaEliminare = cosmoTTemplateReportRepository.findOneNotDeleted(id)
        .orElseThrow(
            () -> new NotFoundException(
                String.format(ErrorMessages.D_TEMPLATE_REPORT_NON_TROVATO, id)));

    templateDaEliminare.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));
    templateDaEliminare.setUtenteCancellazione(AuditServiceImpl.getPrincipalCode());
    templateDaEliminare = cosmoTTemplateReportRepository.save(templateDaEliminare);

    logger.info(methodName, "Template report con id {} eliminato", templateDaEliminare.getId());

  }

  @Override
  public TemplateReport getReportTemplateId(Long id) {
    ValidationUtils.require(id, "id template");

    CosmoTTemplateReport templateReport = cosmoTTemplateReportRepository
        .findOneNotDeleted(id).orElseThrow(
            () -> new NotFoundException(
            String.format(ErrorMessages.D_TEMPLATE_REPORT_NON_TROVATO, id)));

    return cosmoTTemplateReportMapper.toDTOsenzaTrasformazioni(templateReport);
  }

  @Override
  public TemplateReport updateReportTemplate(Long id, CreaTemplateRequest body) {
    String methodName = "updateReportTemplate";
    ValidationUtils.require(id, "id template report");
    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    CosmoTTemplateReport templateDaAggiornare = cosmoTTemplateReportRepository.findOneNotDeleted(id)
        .orElseThrow(() -> new NotFoundException(
            String.format(ErrorMessages.D_TEMPLATE_REPORT_NON_TROVATO, id)));

    templateDaAggiornare.setCodice(body.getCodice());
    if (!body.getSorgenteTemplate().isEmpty()) {
      byte[] result = Base64.getDecoder().decode(body.getSorgenteTemplate());
      templateDaAggiornare.setSorgenteTemplate(result);
      templateDaAggiornare.setTemplateCompilato(null);
    }
    if (body.getIdEnte() != null) {
      CosmoTEnte cosmoTEnte = cosmoTEnteRepository.findOneNotDeleted(body
          .getIdEnte())
          .orElseThrow(() -> new NotFoundException(
              String.format(ErrorMessages.E_ENTE_NON_TROVATO, body.getIdEnte())));
      templateDaAggiornare.setEnte(cosmoTEnte);
    }

    if (body.getCodiceTipoPratica() != null) {
      CosmoDTipoPratica cosmoDTipoPratica = cosmoDTipoPraticaRepository
          .findOneActiveByField(CosmoDTipoPratica_.codice, body
              .getCodiceTipoPratica())
          .orElseThrow(() -> new NotFoundException(String
              .format(ErrorMessages.D_TIPO_PRATICA_NON_TROVATA, body.getCodiceTipoPratica())));
      templateDaAggiornare.setTipoPratica(cosmoDTipoPratica);
    }

    if (body.getCodiceTemplatePadre() != null) {
      templateDaAggiornare.setCodiceTemplatePadre(body.getCodiceTemplatePadre());
    }
    templateDaAggiornare = cosmoTTemplateReportRepository.save(templateDaAggiornare);

    logger.info(methodName, "Template report con id {} aggiornato", templateDaAggiornare.getId());
    return cosmoTTemplateReportMapper.toDTOsenzaTrasformazioni(templateDaAggiornare);
  }


}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

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
import it.csi.cosmo.common.entities.CosmoDTipoDocumento;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoDTipoPratica_;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTTemplateFea;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmoecm.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoecm.business.service.TemplateFeaService;
import it.csi.cosmo.cosmoecm.config.ErrorMessages;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo;
import it.csi.cosmo.cosmoecm.dto.rest.CreaTemplateFeaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.FiltroRicercaTemplateFeaDTO;
import it.csi.cosmo.cosmoecm.dto.rest.PageInfo;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateFea;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateFeaResponse;
import it.csi.cosmo.cosmoecm.integration.mapper.CosmoTTemplateFeaMapper;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDTipoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDTipoPraticaRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTTemplateFeaRepository;
import it.csi.cosmo.cosmoecm.integration.repository.specifications.CosmoTTemplateFeaSearchHandler;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;

/**
 *
 */
@Service
public class TemplateFeaServiceImpl implements TemplateFeaService {

  private static final String CLASSNAME = TemplateFeaServiceImpl.class.getSimpleName();

  private static final String ID_TEMPLATE_FEA = "ID template fea";

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASSNAME);

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoTTemplateFeaRepository cosmoTTemplateFeaRepository;

  @Autowired
  private CosmoTEnteRepository cosmoTEnteRepository;

  @Autowired
  private CosmoDTipoPraticaRepository cosmoDTipoPraticaRepository;

  @Autowired
  private CosmoDTipoDocumentoRepository cosmoDTipoDocumentoRepository;

  @Autowired
  private CosmoTTemplateFeaMapper cosmoTTemplateFeaMapper;

  @Override
  @Transactional(readOnly = true)
  public TemplateFeaResponse getTemplateFea(String filter, Boolean tutti) {
    TemplateFeaResponse output = new TemplateFeaResponse();
    GenericRicercaParametricaDTO<FiltroRicercaTemplateFeaDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaTemplateFeaDTO.class);

    if (tutti != null && Boolean.TRUE.equals(tutti)) {
      List<TemplateFea> templateFea = new LinkedList<>();
      var templates =
          cosmoTTemplateFeaRepository.findAllNotDeleted(new CosmoTTemplateFeaSearchHandler()
              .findByFilters(ricercaParametrica.getFilter(), ricercaParametrica.getSort()));
      templates.forEach(template -> {
        var dto = cosmoTTemplateFeaMapper.toDTO(template);
        if (template.getCosmoTPratica() != null) {
          dto.setIdPratica(template.getCosmoTPratica().getId());
        }
        templateFea.add(dto);
      });
      output.setTemplateFea(templateFea);
      return output;
    }

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(ParametriApplicativo.MAX_PAGE_SIZE).asInteger());

    Page<CosmoTTemplateFea> pageResult = cosmoTTemplateFeaRepository.findAllNotDeleted(
        new CosmoTTemplateFeaSearchHandler().findByFilters(ricercaParametrica.getFilter(), ricercaParametrica.getSort()), paging);

    List<CosmoTTemplateFea> templateFeaSuDB = pageResult.getContent();

    List<TemplateFea> templateFea = new LinkedList<>();
    templateFeaSuDB
        .forEach(templateSuDB -> {
          var dto = cosmoTTemplateFeaMapper.toDTO(templateSuDB);
          if (templateSuDB.getCosmoTPratica() != null) {
            dto.setIdPratica(templateSuDB.getCosmoTPratica().getId());
          }
          templateFea.add(dto);
        });
    output.setTemplateFea(templateFea);

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      SearchUtils.filterFields(templateFea,
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
  public void deleteTemplateFea(Long id) {
    String methodName = "deleteTemplateFea";
    ValidationUtils.require(id, ID_TEMPLATE_FEA);

    CosmoTTemplateFea templateFeaDaEliminare = cosmoTTemplateFeaRepository.findOneNotDeleted(id)
        .orElseThrow(() -> new NotFoundException(
            String.format(ErrorMessages.FEA_TEMPLATE_NON_TROVATO, id)));

    templateFeaDaEliminare.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));
    templateFeaDaEliminare.setUtenteCancellazione(AuditServiceImpl.getPrincipalCode());
    templateFeaDaEliminare = cosmoTTemplateFeaRepository.save(templateFeaDaEliminare);

    logger.info(methodName, "Template fea con id {} eliminato", templateFeaDaEliminare.getId());
  }

  @Override
  public TemplateFea getTemplateFeaId(Long id) {
    ValidationUtils.require(id, ID_TEMPLATE_FEA);

    CosmoTTemplateFea tf = cosmoTTemplateFeaRepository.findOneNotDeleted(id).orElseThrow(
        () -> new NotFoundException(String.format(ErrorMessages.FEA_TEMPLATE_NON_TROVATO, id)));

    return cosmoTTemplateFeaMapper.toDTO(tf);
  }

  @Override
  public TemplateFea creaTemplateFea(CreaTemplateFeaRequest request) {
    String methodName = "creaTemplateFea";
    ValidationUtils.require(request, "request");
    ValidationUtils.validaAnnotations(request);

    // crea il template fea su DB
    CosmoTTemplateFea templateFeaDaSalvare = new CosmoTTemplateFea();
    templateFeaDaSalvare.setDescrizione(request.getDescrizione());
    templateFeaDaSalvare.setCoordinataX(request.getCoordinataX());
    templateFeaDaSalvare.setCoordinataY(request.getCoordinataY());
    templateFeaDaSalvare.setPagina(request.getPagina());

    CosmoTEnte cosmoTEnte = cosmoTEnteRepository.findOneNotDeleted(request.getIdEnte())
        .orElseThrow(() -> new NotFoundException(
            String.format(ErrorMessages.E_ENTE_NON_TROVATO, request.getIdEnte())));
    templateFeaDaSalvare.setEnte(cosmoTEnte);

    CosmoDTipoPratica cosmoDTipoPratica = cosmoDTipoPraticaRepository
        .findOneActiveByField(CosmoDTipoPratica_.codice, request.getCodiceTipoPratica())
        .orElseThrow(() -> new NotFoundException(String
            .format(ErrorMessages.D_TIPO_PRATICA_NON_TROVATA, request.getCodiceTipoPratica())));
    templateFeaDaSalvare.setTipologiaPratica(cosmoDTipoPratica);

    CosmoDTipoDocumento cosmoDTipoDocumento = cosmoDTipoDocumentoRepository
        .findOneActiveByField(CosmoDTipoDocumento_.codice, request.getCodiceTipoDocumento())
        .orElseThrow(() -> new NotFoundException(String
            .format(ErrorMessages.D_TIPO_DOCUMENTO_NON_TROVATO, request.getCodiceTipoDocumento())));
    templateFeaDaSalvare.setTipologiaDocumento(cosmoDTipoDocumento);
    templateFeaDaSalvare.setCaricatoDaTemplate(request.isCaricatoDaTemplate());

    CosmoTTemplateFea templateFeaSalvato = cosmoTTemplateFeaRepository.save(templateFeaDaSalvare);
    logger.info(methodName, "Template fea con id {} creato", templateFeaSalvato.getId());

    return cosmoTTemplateFeaMapper.toDTO(templateFeaSalvato);
  }

  @Override
  public TemplateFea updateTemplateFea(Long id, CreaTemplateFeaRequest body) {
    String methodName = "updateTemplateFea";
    ValidationUtils.require(id, ID_TEMPLATE_FEA);
    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    CosmoTTemplateFea templateFeaDaAggiornare = cosmoTTemplateFeaRepository.findOneNotDeleted(
        id)
        .orElseThrow(() -> new NotFoundException(
            String.format(ErrorMessages.FEA_TEMPLATE_NON_TROVATO, id)));

    templateFeaDaAggiornare.setDescrizione(body.getDescrizione());
    templateFeaDaAggiornare.setCoordinataX(body.getCoordinataX());
    templateFeaDaAggiornare.setCoordinataY(body.getCoordinataY());
    templateFeaDaAggiornare.setPagina(body.getPagina());

    CosmoTEnte cosmoTEnte = cosmoTEnteRepository.findOneNotDeleted(body.getIdEnte())
        .orElseThrow(() -> new NotFoundException(
            String.format(ErrorMessages.E_ENTE_NON_TROVATO, body.getIdEnte())));
    templateFeaDaAggiornare.setEnte(cosmoTEnte);

    CosmoDTipoPratica cosmoDTipoPratica = cosmoDTipoPraticaRepository
        .findOneActiveByField(CosmoDTipoPratica_.codice, body.getCodiceTipoPratica())
        .orElseThrow(() -> new NotFoundException(String
            .format(ErrorMessages.D_TIPO_PRATICA_NON_TROVATA, body.getCodiceTipoPratica())));
    templateFeaDaAggiornare.setTipologiaPratica(cosmoDTipoPratica);

    CosmoDTipoDocumento tipoDocumento = cosmoDTipoDocumentoRepository
        .findOneActiveByField(CosmoDTipoDocumento_.codice, body.getCodiceTipoDocumento())
        .orElseThrow(() -> new NotFoundException(String
            .format(ErrorMessages.D_TIPO_DOCUMENTO_NON_TROVATO, body.getCodiceTipoDocumento())));
    templateFeaDaAggiornare.setTipologiaDocumento(tipoDocumento);
    templateFeaDaAggiornare.setCaricatoDaTemplate(body.isCaricatoDaTemplate());

    templateFeaDaAggiornare = cosmoTTemplateFeaRepository.save(templateFeaDaAggiornare);

    logger.info(methodName, "Template fea con id {} aggiornato", templateFeaDaAggiornare.getId());

    return cosmoTTemplateFeaMapper.toDTO(templateFeaDaAggiornare);
  }

}

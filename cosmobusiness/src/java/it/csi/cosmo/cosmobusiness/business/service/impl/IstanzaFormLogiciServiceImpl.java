/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.CosmoDChiaveParametroFunzionalitaFormLogico;
import it.csi.cosmo.common.entities.CosmoDFunzionalitaFormLogico;
import it.csi.cosmo.common.entities.CosmoDFunzionalitaFormLogico_;
import it.csi.cosmo.common.entities.CosmoRFormLogicoIstanzaFunzionalita;
import it.csi.cosmo.common.entities.CosmoRIstanzaFormLogicoParametroValore;
import it.csi.cosmo.common.entities.CosmoRIstanzaFormLogicoParametroValorePK;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTIstanzaFunzionalitaFormLogico;
import it.csi.cosmo.common.entities.proto.CosmoREntity;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ComplexListComparator;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.common.util.ValoreParametroFormLogicoWrapper;
import it.csi.cosmo.cosmobusiness.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmobusiness.business.service.IstanzaFormLogiciService;
import it.csi.cosmo.cosmobusiness.config.ErrorMessages;
import it.csi.cosmo.cosmobusiness.config.ParametriApplicativo;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaIstanzaFunzionalitaFormLogicoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaIstanzaParametroFormLogico;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaIstanzaFunzionalitaFormLogicoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.FiltroRicercaIstanzeFormLogiciDTO;
import it.csi.cosmo.cosmobusiness.dto.rest.IstanzaFunzionalitaFormLogico;
import it.csi.cosmo.cosmobusiness.dto.rest.IstanzeFormLogiciResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.PageInfo;
import it.csi.cosmo.cosmobusiness.dto.rest.TipologiaFunzionalitaFormLogico;
import it.csi.cosmo.cosmobusiness.dto.rest.TipologieParametroFormLogicoResponse;
import it.csi.cosmo.cosmobusiness.integration.mapper.FormLogicoMapper;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoDChiaveParametroFunzionalitaFormLogicoRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoDFunzionalitaFormLogicoRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoRFormLogicoIstanzaFunzionalitaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoRIstanzaFormLogicoParametroValoreRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTIstanzaFunzionalitaFormLogicoRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.specifications.CosmoTIstanzaFunzionalitaFormLogicoSpecifications;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmobusiness.security.SecurityUtils;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;

/**
 *
 */
@Service
@Transactional
public class IstanzaFormLogiciServiceImpl implements IstanzaFormLogiciService {

  private static final String CLASS_NAME = IstanzaFormLogiciServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private CosmoTIstanzaFunzionalitaFormLogicoRepository cosmoTIstanzaFunzionalitaFormLogicoRepository;

  @Autowired
  private CosmoRFormLogicoIstanzaFunzionalitaRepository cosmoRFormLogicoIstanzaFunzionalitaRepository;

  @Autowired
  private CosmoRIstanzaFormLogicoParametroValoreRepository cosmoRIstanzaFormLogicoParametroValoreRepository;

  @Autowired
  private CosmoDFunzionalitaFormLogicoRepository cosmoDFunzionalitaFormLogicoRepository;

  @Autowired
  private CosmoDChiaveParametroFunzionalitaFormLogicoRepository cosmoDChiaveParametroFunzionalitaFormLogicoRepository;

  @Autowired
  private FormLogicoMapper istanzaFunzionalitaFormLogicoMapper;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoCmmnFeignClient cosmoCmmnFeignClient;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteFormLogiciIstanzeId(Long id) {
    String methodName = "deleteFormLogiciIstanzeId";
    ValidationUtils.require(id, "id istanza form logico");

    CosmoTIstanzaFunzionalitaFormLogico istanza =
        cosmoTIstanzaFunzionalitaFormLogicoRepository.findOneNotDeleted(id).orElseThrow(() -> {
          String error = String.format(ErrorMessages.ISTANZA_FORM_LOGICO_NON_TROVATA, id);
          logger.error(methodName, error);
          throw new NotFoundException(error);
        });

    if (null != istanza) {

      Timestamp data = Timestamp.valueOf(LocalDateTime.now());
      istanza.getCosmoRFormLogicoIstanzaFunzionalitas().forEach(formLogicoIstanza -> {
        if (null == formLogicoIstanza.getDtFineVal()
            || formLogicoIstanza.getDtFineVal().after(data)) {
          formLogicoIstanza.setDtFineVal(data);
          cosmoRFormLogicoIstanzaFunzionalitaRepository.save(formLogicoIstanza);
        }
      });

      istanza.getCosmoRIstanzaFormLogicoParametroValores().forEach(istanzaParametro -> {
        if (null == istanzaParametro.getDtFineVal()
            || istanzaParametro.getDtFineVal().after(data)) {
          istanzaParametro.setDtFineVal(data);
          cosmoRIstanzaFormLogicoParametroValoreRepository.save(istanzaParametro);
        }
      });

      istanza.setDtCancellazione(data);
      istanza.setUtenteCancellazione(SecurityUtils.getUtenteCorrente().getCodiceFiscale());

      cosmoTIstanzaFunzionalitaFormLogicoRepository.save(istanza);
    }

  }

  @Override
  @Transactional(readOnly = true)
  public IstanzeFormLogiciResponse getFormLogiciIstanze(String filter) {
    IstanzeFormLogiciResponse output = new IstanzeFormLogiciResponse();

    GenericRicercaParametricaDTO<FiltroRicercaIstanzeFormLogiciDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaIstanzeFormLogiciDTO.class);

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(ParametriApplicativo.MAX_PAGE_SIZE).asInteger());

    Page<CosmoTIstanzaFunzionalitaFormLogico> pageIstanze =
        cosmoTIstanzaFunzionalitaFormLogicoRepository
        .findAllNotDeleted(CosmoTIstanzaFunzionalitaFormLogicoSpecifications.findByFilters(
            ricercaParametrica.getFilter(), ricercaParametrica.getSort()), paging);

    List<CosmoTIstanzaFunzionalitaFormLogico> istanzeSuDB = pageIstanze.getContent();

    List<IstanzaFunzionalitaFormLogico> funzionalita = new LinkedList<>();
    istanzeSuDB.forEach(
        istanzaSuDB -> funzionalita.add(istanzaFunzionalitaFormLogicoMapper.toDTO(istanzaSuDB)));
    output.setIstanze(funzionalita);

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      SearchUtils.filterFields(funzionalita,
          Arrays.asList(ricercaParametrica.getFields().split(",")));
    }

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(pageIstanze.getNumber());
    pageInfo.setPageSize(pageIstanze.getSize());
    pageInfo.setTotalElements(Math.toIntExact(pageIstanze.getTotalElements()));
    pageInfo.setTotalPages(pageIstanze.getTotalPages());

    output.setPageInfo(pageInfo);

    return output;
  }

  @Override
  @Transactional(readOnly = true)
  public IstanzaFunzionalitaFormLogico getFormLogiciIstanzeId(Long id) {
    ValidationUtils.require(id, "id istanza");

    CosmoTIstanzaFunzionalitaFormLogico istanza = cosmoTIstanzaFunzionalitaFormLogicoRepository
        .findOneNotDeleted(id).orElseThrow(() -> new NotFoundException(
            String.format(ErrorMessages.ISTANZA_FORM_LOGICO_NON_TROVATO, id)));

    return istanzaFunzionalitaFormLogicoMapper.toDTO(istanza);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public IstanzaFunzionalitaFormLogico postFormLogiciIstanze(
      CreaIstanzaFunzionalitaFormLogicoRequest body) {

    ValidationUtils.require(body, "request");
    ValidationUtils.validaAnnotations(body);

    CosmoDFunzionalitaFormLogico funzionalitaFormLogico = cosmoDFunzionalitaFormLogicoRepository
        .findOneActive(body.getCodice()).orElseThrow(() -> new NotFoundException(String
            .format(ErrorMessages.CODICE_FUNZIONALITA_FORM_LOGICO_NON_TROVATO, body.getCodice())));

    if (StringUtils.isBlank(body.getDescrizione())) {
      body.setDescrizione(funzionalitaFormLogico.getDescrizione());
    }

    CosmoTIstanzaFunzionalitaFormLogico istanzaDaSalvare =
        new CosmoTIstanzaFunzionalitaFormLogico();
    istanzaDaSalvare.setCosmoRFormLogicoIstanzaFunzionalitas(new ArrayList<>());
    istanzaDaSalvare.setCosmoRIstanzaFormLogicoParametroValores(new ArrayList<>());
    istanzaDaSalvare.setDescrizione(body.getDescrizione());
    istanzaDaSalvare.setCosmoDFunzionalitaFormLogico(funzionalitaFormLogico);

    istanzaDaSalvare = cosmoTIstanzaFunzionalitaFormLogicoRepository.save(istanzaDaSalvare);

    aggiornaParametri(istanzaDaSalvare, body.getParametri());

    return istanzaFunzionalitaFormLogicoMapper.toDTO(istanzaDaSalvare);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public IstanzaFunzionalitaFormLogico putFormLogiciIstanzeId(Long id,
      AggiornaIstanzaFunzionalitaFormLogicoRequest body) {

    ValidationUtils.require(id, "id istanza");
    ValidationUtils.require(body, "request");
    ValidationUtils.validaAnnotations(body);

    CosmoTIstanzaFunzionalitaFormLogico istanzaDaAggiornare =
        cosmoTIstanzaFunzionalitaFormLogicoRepository.findOneNotDeleted(id)
        .orElseThrow(NotFoundException::new);

    CosmoDFunzionalitaFormLogico funzionalitaFormLogico = cosmoDFunzionalitaFormLogicoRepository
        .findOneActive(body.getCodice()).orElseThrow(() -> new NotFoundException(String
            .format(ErrorMessages.CODICE_FUNZIONALITA_FORM_LOGICO_NON_TROVATO, body.getCodice())));

    istanzaDaAggiornare.setCosmoDFunzionalitaFormLogico(funzionalitaFormLogico);
    istanzaDaAggiornare.setDescrizione(body.getDescrizione());

    istanzaDaAggiornare = cosmoTIstanzaFunzionalitaFormLogicoRepository.save(istanzaDaAggiornare);

    aggiornaParametri(istanzaDaAggiornare, body.getParametri());

    return istanzaFunzionalitaFormLogicoMapper.toDTO(istanzaDaAggiornare);
  }

  private void aggiornaParametri(CosmoTIstanzaFunzionalitaFormLogico istanzaDaAggiornare,
      List<AggiornaIstanzaParametroFormLogico> parametri) {

    Timestamp now = Timestamp.from(Instant.now());

    ComplexListComparator
    .compareLists(istanzaDaAggiornare.getCosmoRIstanzaFormLogicoParametroValores(), parametri,
        (e, i) -> e.valido() && e.getId().getCodiceChiaveParametro().equals(i.getChiave()))

    .onElementsInFirstNotInSecond(daEliminare -> {
      daEliminare.setDtFineVal(now);
      cosmoRIstanzaFormLogicoParametroValoreRepository.save(daEliminare);
      istanzaDaAggiornare.getCosmoRIstanzaFormLogicoParametroValores().remove(daEliminare);

    }).onElementsInSecondNotInFirst(daInserire -> {
      CosmoDChiaveParametroFunzionalitaFormLogico param =
          cosmoDChiaveParametroFunzionalitaFormLogicoRepository
          .findOneActive(daInserire.getChiave()).orElseThrow(NotFoundException::new);
      CosmoRIstanzaFormLogicoParametroValore newRel =
          new CosmoRIstanzaFormLogicoParametroValore();
      newRel.setCosmoDChiaveParametroFunzionalitaFormLogico(param);
      newRel.setCosmoTIstanzaFunzionalitaFormLogico(istanzaDaAggiornare);
      newRel.setDtInizioVal(now);
      newRel.setValoreParametro(daInserire.getValore());
      CosmoRIstanzaFormLogicoParametroValorePK id =
          new CosmoRIstanzaFormLogicoParametroValorePK();
      id.setCodiceChiaveParametro(param.getCodice());
      id.setIdIstanza(istanzaDaAggiornare.getId());
      newRel.setId(id);
      istanzaDaAggiornare.getCosmoRIstanzaFormLogicoParametroValores()
      .add(cosmoRIstanzaFormLogicoParametroValoreRepository.save(newRel));

    }).onElementsInBoth((e, i) -> {
      e.setValoreParametro(i.getValore());
      cosmoRIstanzaFormLogicoParametroValoreRepository.save(e);
    });
  }

  @Override
  public List<TipologiaFunzionalitaFormLogico> getAllTipologie() {

    List<CosmoDFunzionalitaFormLogico> funzionalita = cosmoDFunzionalitaFormLogicoRepository
        .findAllActive(new Sort(Direction.ASC, CosmoDFunzionalitaFormLogico_.descrizione.getName(),
            CosmoDFunzionalitaFormLogico_.codice.getName()));

    return istanzaFunzionalitaFormLogicoMapper.toDTOList(funzionalita);

  }

  @Override
  public TipologieParametroFormLogicoResponse getFormLogiciTipologieIstanzeFunzionalitaParametri(
      String codice) {
    ValidationUtils.require(codice, "codice");

    CosmoDFunzionalitaFormLogico funzionalitaFormLogico = cosmoDFunzionalitaFormLogicoRepository
        .findOneActive(codice).orElseThrow(() -> new NotFoundException(String
            .format(ErrorMessages.CODICE_FUNZIONALITA_FORM_LOGICO_NON_TROVATO, codice)));

    TipologieParametroFormLogicoResponse output = new TipologieParametroFormLogicoResponse();

    output.addAll(funzionalitaFormLogico.getAssociazioniParametri().stream()
        .filter(CosmoREntity::valido)
        .map(istanzaFunzionalitaFormLogicoMapper::toDTO)
        .collect(Collectors.toList()));

    return output;
  }

  @Override
  public Map<String, ValoreParametroFormLogicoWrapper> getValoriParametri(
      CosmoTIstanzaFunzionalitaFormLogico istanza) {

    //@formatter:off
    return istanza.getCosmoRIstanzaFormLogicoParametroValores().stream()
        .filter(CosmoREntity::valido)
        .map(r -> new ValoreParametroFormLogicoWrapper(r))
        .collect(Collectors.toMap(e -> e.getCodice(), e -> e));
    //@formatter:on
  }

  @Override
  public ValoreParametroFormLogicoWrapper requireValoreParametro(
      CosmoTIstanzaFunzionalitaFormLogico istanza,
      String codiceParametro) {

    return getValoreParametro(istanza, codiceParametro).orElseThrow(
        () -> new InternalServerException("Parametro " + codiceParametro + " non configurato"));
  }

  @Override
  public Optional<ValoreParametroFormLogicoWrapper> getValoreParametro(
      CosmoTIstanzaFunzionalitaFormLogico istanza,
      String codiceParametro) {
    //@formatter:off
    var valore = istanza.getCosmoRIstanzaFormLogicoParametroValores().stream()
        .filter(CosmoREntity::valido)
        .filter(p -> p.getId().getCodiceChiaveParametro().equals(codiceParametro))
        .findFirst()
        .orElse(null);
    //@formatter:on

    if (valore == null) {
      return Optional.empty();
    }
    return Optional.of(new ValoreParametroFormLogicoWrapper(valore));
  }

  @Override
  public CosmoTIstanzaFunzionalitaFormLogico ricercaIstanzaAttiva(CosmoTAttivita attivita,
      String codiceFunzionalita) {

    ValidationUtils.require(attivita, "attivita");
    ValidationUtils.require(codiceFunzionalita, "codiceFunzionalita");

    final var method = "ricercaIstanza";
    logger.debug(method, "ricerco istanza della funzionalita' {} a partire dall'attivita {}",
        codiceFunzionalita, attivita.getId());

    // recupero il task
    String taskId = attivita.getTaskId();
    ValidationUtils.require(taskId, "taskId");
    logger.debug(method, "attivita {} -> taskId {}", attivita.getId(), taskId);
    var task = cosmoCmmnFeignClient.getTaskId(taskId);
    if (logger.isDebugEnabled()) {
      logger.debug(method, "attivita {} -> task {}", attivita.getId(), task);
    }

    // recupero la form key
    String formKey = task.getFormKey();
    ValidationUtils.require(formKey, "formKey");
    logger.debug(method, "attivita {} -> taskId {} -> formKey {}", attivita.getId(), taskId,
        formKey);

    // ricerco form logico
    var formLogico = attivita.getFormLogico();
    if (formLogico == null || formLogico.getDtCancellazione() != null) {
      throw new NotFoundException("Form logico non trovato");
    }

    /*
     * var formLogico =
     * cosmoTFormLogicoRepository.findOneNotDeletedByField(CosmoTFormLogico_.codice, formKey)
     * .orElseThrow(() -> new NotFoundException("Form logico non trovato")); logger.debug(method,
     * "attivita {} -> taskId {} -> formKey {} -> formLogico {}", attivita.getId(), taskId, formKey,
     * formLogico.getId());
     */

    // ricerco istanze
    //@formatter:off
    var istanze = formLogico.getCosmoRFormLogicoIstanzaFunzionalitas().stream()
        .filter(CosmoREntity::valido)
        .map(CosmoRFormLogicoIstanzaFunzionalita::getCosmoTIstanzaFunzionalitaFormLogico)
        .filter(CosmoTEntity::nonCancellato)
        .filter(istanza -> istanza.getCosmoDFunzionalitaFormLogico().valido() && istanza.getCosmoDFunzionalitaFormLogico().getCodice().equals(codiceFunzionalita))
        .collect(Collectors.toList());
    //@formatter:on

    if (istanze.isEmpty()) {
      throw new NotFoundException("Nessuna istanza di funzionalita' " + codiceFunzionalita
          + " valida per il form logico " + formLogico.getId());
    } else if (istanze.size() > 1) {
      throw new InternalServerException("Troppe istanza di funzionalita' " + codiceFunzionalita
          + " valide per il form logico " + formLogico.getId() + ": " + istanze.size());
    }

    var istanza = istanze.get(0);
    logger.debug(method, "attivita {} -> taskId {} -> formKey {} -> formLogico {} -> istanza {}",
        attivita.getId(), taskId, formKey, formLogico.getId(), istanza.getId());

    return istanza;
  }
}

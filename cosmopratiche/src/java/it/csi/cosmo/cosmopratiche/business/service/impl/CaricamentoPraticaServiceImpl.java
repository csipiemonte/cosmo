/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.rest.RicercaParametricaDTO;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.CosmoTCaricamentoPratica;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.cosmopratiche.business.service.CaricamentoPraticaService;
import it.csi.cosmo.cosmopratiche.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmopratiche.config.ParametriApplicativo;
import it.csi.cosmo.cosmopratiche.dto.rest.CaricamentoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.CaricamentoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.CaricamentoPraticheResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.FiltroRicercaCaricamentoPraticheDTO;
import it.csi.cosmo.cosmopratiche.dto.rest.PageInfo;
import it.csi.cosmo.cosmopratiche.dto.rest.StatoCaricamentoPratica;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoTCaricamentoPraticaMapper;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDStatoCaricamentoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTCaricamentoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTUtenteRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.specifications.CosmoTCaricamentoPraticaSpecifications;
import it.csi.cosmo.cosmopratiche.integration.repository.specifications.CosmoTPraticaSpecifications;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoFileUploadFeignClient;
import it.csi.cosmo.cosmopratiche.security.SecurityUtils;
import it.csi.cosmo.cosmopratiche.util.logger.LogCategory;
import it.csi.cosmo.cosmopratiche.util.logger.LoggerFactory;


/**
 *
 */

@Service
@Transactional
public class CaricamentoPraticaServiceImpl implements CaricamentoPraticaService{

  private static final String CLASS_NAME = CaricamentoPraticaServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.COSMOPRATICHE_BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private CosmoTCaricamentoPraticaRepository cosmoTCaricamentoPraticaRepository;

  @Autowired
  private CosmoDStatoCaricamentoPraticaRepository cosmoDStatoCaricamentoPraticaRepository;

  @Autowired
  private CosmoTCaricamentoPraticaMapper mapper;

  @Autowired
  private CosmoTEnteRepository cosmoTEnteRepository;

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CosmoTUtenteRepository cosmoTUtenteRepository;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoFileUploadFeignClient cosmoFileUploadFeignClient;

  @Override
  @Transactional
  public CaricamentoPratica creaCaricamentoPratica(CaricamentoPraticaRequest request) {
    final String methodName = "creaCaricamentoPratica";

    var statoCaricamento =
        cosmoDStatoCaricamentoPraticaRepository.findOne(request.getStatoCaricamentoPratica());

    if (statoCaricamento == null) {
      logger.error(methodName, "stato caricamento pratica " + statoCaricamento + " non trovato");
      throw new NotFoundException(
          "creaCaricamentoPratica: stato caricamento pratica " + statoCaricamento + " non trovato");
    }

    if (SecurityUtils.getUtenteCorrente() == null
        || SecurityUtils.getUtenteCorrente().getEnte() == null) {
      logger.error(methodName, "Utente non autenticato");
      throw new BadRequestException("Utente non autenticato");
    }

    var ente = cosmoTEnteRepository.findOne(SecurityUtils.getUtenteCorrente().getEnte().getId());

    if (ente == null) {
      logger.error(methodName, "ente non trovato");
      throw new NotFoundException("creaCaricamentoPratica: ente non trovato");
    }

    var utente = cosmoTUtenteRepository.findOne(SecurityUtils.getUtenteCorrente().getId());

    if (utente == null) {
      logger.error(methodName, "utente non trovato");
      throw new NotFoundException("creaCaricamentoPratica: utente non trovato");
    }


    var caricamentoPratica = new CosmoTCaricamentoPratica();
    caricamentoPratica.setDtInserimento(Timestamp.from(Instant.now()));
    caricamentoPratica.setNomeFile(request.getNomeFile());
    caricamentoPratica.setPathFile(request.getPathFile());
    caricamentoPratica.setCosmoDStatoCaricamentoPratica(statoCaricamento);
    caricamentoPratica.setCosmoTEnte(ente);
    caricamentoPratica.setCosmoTUtente(utente);
    caricamentoPratica.setErrore(request.getErrore());
    caricamentoPratica.setIdentificativoPratica(request.getIdentificativoPratica());
    caricamentoPratica.setDescrizioneEvento(request.getDescrizioneEvento());

    var result = cosmoTCaricamentoPraticaRepository.save(caricamentoPratica);

    return mapper.toDTO(result);
  }

  @Override
  @Transactional
  public CaricamentoPratica aggiornaCaricamentoPratica(Long id,
      CaricamentoPraticaRequest request) {
    final String methodName = "aggiornaCaricamentoPratica";

    var caricamentoPratica =
        cosmoTCaricamentoPraticaRepository.findOneNotDeleted(id);

    if (!caricamentoPratica.isPresent()) {
      logger.error(methodName, "caricamentoPratica non trovato");
      throw new NotFoundException(
          "aggiornaCaricamentoPratica: caricamentoPratica non trovato");
    }

    var statoCaricamento =
        cosmoDStatoCaricamentoPraticaRepository.findOne(request.getStatoCaricamentoPratica());

    if (statoCaricamento == null) {
      logger.error(methodName, "stato caricamento pratica " + statoCaricamento + " non trovato");
      throw new NotFoundException("aggiornaCaricamentoPratica: stato caricamento pratica "
          + statoCaricamento + " non trovato");
    }

    if (request.getIdPratica() != null) {
      var pratica =
          cosmoTPraticaRepository.findOneNotDeleted(request.getIdPratica());
      if (!pratica.isPresent()) {
        logger.error(methodName, "pratica non trovato");
        throw new NotFoundException(
            "aggiornaCaricamentoPratica: Pratica non trovato non trovato");
      }
      caricamentoPratica.get().setIdentificativoPratica(request.getIdentificativoPratica());
      caricamentoPratica.get().setCosmoTPratica(pratica.get());
    }

    caricamentoPratica.get().setDtUltimaModifica(Timestamp.from(Instant.now()));
    caricamentoPratica.get().setCosmoDStatoCaricamentoPratica(statoCaricamento);
    caricamentoPratica.get().setErrore(request.getErrore());
    caricamentoPratica.get().setDescrizioneEvento(request.getDescrizioneEvento());

    var result = cosmoTCaricamentoPraticaRepository.save(caricamentoPratica.get());

    return mapper.toDTO(result);
  }

  @Override
  public List<StatoCaricamentoPratica> getStatiCaricamento() {
    var stati = cosmoDStatoCaricamentoPraticaRepository.findAllActive();

    return mapper.toListDTO(stati);
  }

  @Override
  @Transactional
  public CaricamentoPraticheResponse getCaricamentoPratiche(String filter) {

    final String methodName = "getCaricamentoPratiche";

    CaricamentoPraticheResponse output = new CaricamentoPraticheResponse();


    GenericRicercaParametricaDTO<FiltroRicercaCaricamentoPraticheDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaCaricamentoPraticheDTO.class);


    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(ParametriApplicativo.MAX_PAGE_SIZE).asInteger());

    if (SecurityUtils.getUtenteCorrente() == null
        || SecurityUtils.getUtenteCorrente().getEnte() == null) {
      logger.error(methodName, "Ente non trovato");
      throw new BadRequestException("getCaricamentoPratiche: Ente non trovato");
    }

    var idEnte = SecurityUtils.getUtenteCorrente().getEnte().getId();
    var idUtente = SecurityUtils.getUtenteCorrente().getId();


    Page<CosmoTCaricamentoPratica> page = cosmoTCaricamentoPraticaRepository.findAll(
        CosmoTCaricamentoPraticaSpecifications.findByEnteFiltersIdParentNull(idEnte, idUtente,
            ricercaParametrica.getFilter()),
        paging);

    output.setCaricamentoPratiche(page.getContent().stream().map(elem -> {

      var x = mapper.toLightDTO(elem);
      x.setNomeFile(elem.getNomeFile());
      x.setHasElaborazioni(elem.getCosmoTCaricamentoPraticas() != null
          && !elem.getCosmoTCaricamentoPraticas().isEmpty());

      return x;
    }).collect(Collectors.toCollection(LinkedList::new)));

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(page.getNumber());
    pageInfo.setPageSize(page.getSize());
    pageInfo.setTotalElements(Math.toIntExact(page.getTotalElements()));
    pageInfo.setTotalPages(page.getTotalPages());
    output.setPageInfo(pageInfo);

    return output;
  }

  @Override
  public CaricamentoPraticheResponse getCaricamentoPraticheCaricamentoInBozza(String filter) {
    final String methodName = "getCaricamentoPraticheCaricamentoInBozza";

    CaricamentoPraticheResponse output = new CaricamentoPraticheResponse();


    GenericRicercaParametricaDTO<RicercaParametricaDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, RicercaParametricaDTO.class);

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(ParametriApplicativo.MAX_PAGE_SIZE).asInteger());

    if (SecurityUtils.getUtenteCorrente() == null
        || SecurityUtils.getUtenteCorrente().getEnte() == null) {
      logger.error(methodName, "Ente non trovato");
      throw new BadRequestException("getCaricamentoPratiche: Ente non trovato");
    }

    var idEnte = SecurityUtils.getUtenteCorrente().getEnte().getId();

    var idUtente = SecurityUtils.getUtenteCorrente().getId();

    Page<CosmoTCaricamentoPratica> page = cosmoTCaricamentoPraticaRepository.findAll(
        CosmoTCaricamentoPraticaSpecifications.findByEnteAndCaricamentoInBozza(idEnte, idUtente),
        paging);

    output.setCaricamentoPratiche(page.getContent().stream().map(elem -> {
      var x = mapper.toLightDTO(elem);
      x.setNomeFile(elem.getNomeFile());
      x.setPathFile(elem.getPathFile());
      return x;
    }).collect(Collectors.toCollection(LinkedList::new)));

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(page.getNumber());
    pageInfo.setPageSize(page.getSize());
    pageInfo.setTotalElements(Math.toIntExact(page.getTotalElements()));
    pageInfo.setTotalPages(page.getTotalPages());
    output.setPageInfo(pageInfo);

    return output;
  }

  @Override
  public void deleteCaricamentoPratiche(Long id) {

    var caricamentoPratica = cosmoTCaricamentoPraticaRepository.findOneNotDeleted(id).orElse(null);

    if (caricamentoPratica == null) {
      throw new BadRequestException("Caricamento pratica con id " + id + " non trovato");
    }

    var utente = SecurityUtils.getUtenteCorrente();

    if (utente == null) {
      throw new BadRequestException("Utente non autenticato");
    }

    var path = caricamentoPratica.getPathFile();

    caricamentoPratica.setPathFile(null);

    caricamentoPratica.setUtenteCancellazione(utente.getCodiceFiscale());
    caricamentoPratica.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));

    cosmoTCaricamentoPraticaRepository.save(caricamentoPratica);

    try {

      cosmoFileUploadFeignClient.deleteFilePratiche(path);

    } catch (Exception e) {
      throw new BadRequestException("Errore cancellazione " + caricamentoPratica.getNomeFile());
    }


  }

  @Override
  public CaricamentoPraticheResponse getCaricamentoPraticheId(String id, String filter,
      Boolean export) {
    final String methodName = "getCaricamentoPraticheId";

    CaricamentoPraticheResponse output = new CaricamentoPraticheResponse();


    GenericRicercaParametricaDTO<RicercaParametricaDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, RicercaParametricaDTO.class);

    var size = Boolean.TRUE.equals(export) ? ParametriApplicativo.EXPORT_ROW_MAX_SIZE
        : ParametriApplicativo.MAX_PAGE_SIZE;


    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(size).asInteger());

    if (SecurityUtils.getUtenteCorrente() == null
        || SecurityUtils.getUtenteCorrente().getEnte() == null) {
      logger.error(methodName, "Ente non trovato");
      throw new BadRequestException("getCaricamentoPratiche: Ente non trovato");
    }


    var userInfo = SecurityUtils.getUtenteCorrente();

    Page<CosmoTCaricamentoPratica> page = cosmoTCaricamentoPraticaRepository.findAll(
        CosmoTCaricamentoPraticaSpecifications.findByEnteWithIdParentNotNullSortDesc(userInfo.getEnte().getId(),
            userInfo.getId(), Long.valueOf(id)),
        paging);

    output.setCaricamentoPratiche(page.getContent().stream().map(elem -> {
      var x = mapper.toLightDTO(elem);
      if (export != null && Boolean.TRUE.equals(export)) {
        x.setNomeFile(elem.getNomeFile());
      }
      if (elem.getCosmoTPratica() != null) {
        x.setIdPratica(elem.getCosmoTPratica().getId());
      }
      x.setVisualizzaDettaglio(canShowPracticeDetails(x.getIdPratica(), userInfo.getEnte().getId(), userInfo));
      x.setHasElaborazioni(elem.getCosmoTCaricamentoPraticas() != null
          && !elem.getCosmoTCaricamentoPraticas().isEmpty());
      elem.getCosmoTCaricamentoPraticas().stream().filter(el -> el.nonCancellato()
          && !el.getCosmoDStatoCaricamentoPratica().getCodice()
          .equals(
              it.csi.cosmo.common.entities.enums.StatoCaricamentoPratica.CARICAMENTO_IN_BOZZA
              .getCodice()))
      .forEach(element -> {
        var y = mapper.toLightDTO(element);
        x.getElaborazioni().add(y);
      });
      x.getElaborazioni().sort((e1, e2) -> e1.getId().compareTo(e2.getId()));

      return x;
    }).collect(Collectors.toCollection(LinkedList::new)));

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(page.getNumber());
    pageInfo.setPageSize(page.getSize());
    pageInfo.setTotalElements(Math.toIntExact(page.getTotalElements()));
    pageInfo.setTotalPages(page.getTotalPages());
    output.setPageInfo(pageInfo);

    return output;
  }

  @Override
  public List<String> getPathElaborazioni(String dataInserimento) {

    LocalDate date = null;
    try {
      date = LocalDate.parse(dataInserimento);
    } catch (Exception e) {
      throw new BadRequestException("Errore ottenimento dataInserimento");
    }

    var dateTime = Timestamp.valueOf(
        LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 0, 0));

    var lista = cosmoTCaricamentoPraticaRepository
        .findAll(
            CosmoTCaricamentoPraticaSpecifications
            .findAllActiveElaborazioneCompletataAndInErrore(dateTime));


    var pathDaCancellare = lista.stream().filter(elem -> elem != null && elem.getPathFile() != null)
        .map(CosmoTCaricamentoPratica::getPathFile).collect(Collectors.toList());

    lista.stream().forEach(elem -> {
      elem.setPathFile(null);
      cosmoTCaricamentoPraticaRepository.save(elem);
    });

    return pathDaCancellare;

  }

  @Override
  public void deletePathFile(String path) {

    var daCancellare = cosmoTCaricamentoPraticaRepository
        .findAll(CosmoTCaricamentoPraticaSpecifications.findAllActiveByPathFile(path));

    daCancellare.stream().forEach(elem -> {
      elem.setPathFile(null);
      cosmoTCaricamentoPraticaRepository.save(elem);
    });

  }

  private boolean canShowPracticeDetails(Long idPratica, Long idEnte, UserInfoDTO userInfo) {
    if (null == idPratica) {
      return false;
    }
    return null != cosmoTPraticaRepository
        .findOne(
        CosmoTPraticaSpecifications
            .findByCondizioniDiVisibilita(idPratica, idEnte, userInfo));
  }


}

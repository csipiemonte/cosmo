/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.CosmoDAutorizzazioneFruitore;
import it.csi.cosmo.common.entities.CosmoDOperazioneFruitore;
import it.csi.cosmo.common.entities.CosmoDOperazioneFruitore_;
import it.csi.cosmo.common.entities.CosmoDTipoSchemaAutenticazione;
import it.csi.cosmo.common.entities.CosmoRFruitoreEnte;
import it.csi.cosmo.common.entities.CosmoRFruitoreEntePK;
import it.csi.cosmo.common.entities.CosmoTCredenzialiAutenticazioneFruitore;
import it.csi.cosmo.common.entities.CosmoTCredenzialiAutenticazioneFruitore_;
import it.csi.cosmo.common.entities.CosmoTEndpointFruitore;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.entities.CosmoTFruitore_;
import it.csi.cosmo.common.entities.CosmoTSchemaAutenticazioneFruitore;
import it.csi.cosmo.common.entities.CosmoTSchemaAutenticazioneFruitore_;
import it.csi.cosmo.common.entities.enums.TipoSchemaAutenticazione;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.exception.UnauthorizedException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ComplexListComparator;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmoauthorization.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoauthorization.business.service.FruitoriService;
import it.csi.cosmo.cosmoauthorization.config.ErrorMessages;
import it.csi.cosmo.cosmoauthorization.config.ParametriApplicativo;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaEndpointFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaSchemaAutenticazioneFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaEndpointFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaSchemaAutenticazioneFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CredenzialiAutenticazioneFruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.EndpointFruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.FiltroRicercaFruitoriDTO;
import it.csi.cosmo.cosmoauthorization.dto.rest.Fruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.FruitoriResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.OperazioneFruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.PageInfo;
import it.csi.cosmo.cosmoauthorization.dto.rest.SchemaAutenticazioneFruitore;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoTFruitoreMapper;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoDAutorizzazioneFruitoreRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoDOperazioneFruitoreRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoRFruitoreEnteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTCredenzialiAutenticazioneFruitoreRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTEndpointFruitoreRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTFruitoreRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTSchemaAutenticazioneFruitoreRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.specifications.CosmoTFruitoreSpecifications;
import it.csi.cosmo.cosmoauthorization.security.SecurityUtils;
import it.csi.cosmo.cosmoauthorization.util.logger.LogCategory;
import it.csi.cosmo.cosmoauthorization.util.logger.LoggerFactory;

/**
 *
 */

@Service
@Transactional
public class FruitoreServiceImpl implements FruitoriService {

  private static final String CLASS_NAME = FruitoreServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private CosmoTFruitoreRepository cosmoTFruitoreRepository;

  @Autowired
  private CosmoTEnteRepository cosmoTEnteRepository;

  @Autowired
  private CosmoRFruitoreEnteRepository cosmoRFruitoreEnteRepository;

  @Autowired
  private CosmoTFruitoreMapper cosmoTFruitoreMapper;

  @Autowired
  private CosmoDAutorizzazioneFruitoreRepository cosmoDAutorizzazioneFruitoreRepository;

  @Autowired
  private CosmoDOperazioneFruitoreRepository cosmoDOperazioneFruitoreRepository;

  @Autowired
  private CosmoTSchemaAutenticazioneFruitoreRepository cosmoTSchemaAutenticazioneFruitoreRepository;

  @Autowired
  private CosmoTCredenzialiAutenticazioneFruitoreRepository cosmoTCredenzialiAutenticazioneFruitoreRepository;

  @Autowired
  private CosmoTEndpointFruitoreRepository cosmoTEndpointFruitoreRepository;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Fruitore deleteFruitore(Long id) {
    String methodName = "deleteFruitore";

    ValidationUtils.require(id, "id fruitore da eliminare");

    CosmoTFruitore fruitoreDaEliminare = cosmoTFruitoreRepository.findOne(id);
    if (fruitoreDaEliminare == null || fruitoreDaEliminare.cancellato()) {
      logger.error(CLASS_NAME, String.format(ErrorMessages.F_FRUITORE_NON_TROVATO, id));
      throw new NotFoundException(String.format(ErrorMessages.F_FRUITORE_NON_TROVATO, id));
    }

    fruitoreDaEliminare.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));
    fruitoreDaEliminare.setUtenteCancellazione(AuditServiceImpl.getPrincipalCode());

    fruitoreDaEliminare = cosmoTFruitoreRepository.save(fruitoreDaEliminare);

    logger.info(methodName, "Fruitore con id {} eliminato", fruitoreDaEliminare.getId());

    return cosmoTFruitoreMapper.toDTO(fruitoreDaEliminare);
  }

  @Override
  @Transactional(readOnly = true)
  public FruitoriResponse getFruitori(String filter) {
    FruitoriResponse output = new FruitoriResponse();

    GenericRicercaParametricaDTO<FiltroRicercaFruitoriDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaFruitoriDTO.class);

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(ParametriApplicativo.MAX_PAGE_SIZE).asInteger());

    var utenteCorrente = SecurityUtils.getUtenteCorrente();
    Long idEnte = utenteCorrente != null && utenteCorrente.getEnte() != null
        ? utenteCorrente.getEnte().getId()
            : null;

    Page<CosmoTFruitore> pageFruitori =
        cosmoTFruitoreRepository.findAllNotDeleted(CosmoTFruitoreSpecifications
            .findByFilters(ricercaParametrica.getFilter(), ricercaParametrica.getSort(), idEnte),
            paging);

    List<CosmoTFruitore> fruitoriSuDB = pageFruitori.getContent();

    List<Fruitore> fruitori = new LinkedList<>();
    fruitoriSuDB.forEach(fruitoreSuDB -> fruitori.add(cosmoTFruitoreMapper.toDTO(fruitoreSuDB)));
    output.setFruitori(fruitori);

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

  @Override
  @Transactional(readOnly = true)
  public Fruitore getFruitore(Long id) {
    String methodName = "getFruitore";

    ValidationUtils.require(id, "id fruitore da cercare");

    CosmoTFruitore fruitore = cosmoTFruitoreRepository.findOne(id);
    if (fruitore == null || fruitore.cancellato()) {
      logger.error(methodName, String.format(ErrorMessages.F_FRUITORE_NON_TROVATO, id));
      throw new NotFoundException(String.format(ErrorMessages.F_FRUITORE_NON_TROVATO, id));
    }

    return cosmoTFruitoreMapper.toDTO(fruitore);
  }

  @Override
  @Transactional(readOnly = true)
  public Fruitore getFruitoreApiManagerId(String apiManagerId) {
    String methodName = "getFruitoreApiManagerId";

    if (StringUtils.isBlank(apiManagerId)) {
      logger.error(methodName,
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO_CORRETTAMENTE, "apiManagerId"));
      throw new BadRequestException(
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO_CORRETTAMENTE, "apiManagerId"));
    }

    CosmoTFruitore fruitore = cosmoTFruitoreRepository.findByApiManagerId(apiManagerId);

    if (fruitore == null) {
      logger.error(methodName,
          String.format(ErrorMessages.F_FRUITORE_NON_TROVATO_API_MANAGER_ID, apiManagerId));
      throw new NotFoundException(
          String.format(ErrorMessages.F_FRUITORE_NON_TROVATO_API_MANAGER_ID, apiManagerId));
    }

    return cosmoTFruitoreMapper.toDTO(fruitore);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Fruitore createFruitore(CreaFruitoreRequest request) {
    String methodName = "createFruitore";

    ValidationUtils.require(request, "request");
    ValidationUtils.validaAnnotations(request);

    // controlla che non ci siano conflitti
    checkConflict(CosmoTFruitore_.apiManagerId, clean(request.getApiManagerId()), null);
    checkConflict(CosmoTFruitore_.nomeApp, clean(request.getNomeApp()), null);

    CosmoTFruitore fruitoreDaSalvare = new CosmoTFruitore();
    fruitoreDaSalvare.setApiManagerId(clean(request.getApiManagerId()));
    fruitoreDaSalvare.setNomeApp(clean(request.getNomeApp()));
    fruitoreDaSalvare.setUrl(clean(request.getUrl()));
    CosmoTFruitore fruitoreSalvato = cosmoTFruitoreRepository.save(fruitoreDaSalvare);

    salvaAggiornaAutorizzazioni(request.getAutorizzazioni(), fruitoreSalvato);
    salvaAggiornaEnti(request.getIdEnti(), fruitoreSalvato);

    logger.info(methodName, "Fruitore con id {} salvato", fruitoreSalvato.getId());
    return cosmoTFruitoreMapper.toDTO(fruitoreSalvato);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Fruitore putFruitore(Long id, AggiornaFruitoreRequest request) {
    String methodName = "putFruitore";

    ValidationUtils.require(id, "id");
    ValidationUtils.require(request, "request");
    ValidationUtils.validaAnnotations(request);

    // controlla che non ci siano conflitti
    checkConflict(CosmoTFruitore_.nomeApp, clean(request.getNomeApp()), id);

    CosmoTFruitore fruitoreDaAggiornare =
        cosmoTFruitoreRepository.findOneNotDeleted(id).orElseThrow(NotFoundException::new);

    fruitoreDaAggiornare.setNomeApp(clean(request.getNomeApp()));
    fruitoreDaAggiornare.setUrl(clean(request.getUrl()));

    salvaAggiornaAutorizzazioni(request.getAutorizzazioni(), fruitoreDaAggiornare);
    salvaAggiornaEnti(request.getIdEnti(), fruitoreDaAggiornare);

    fruitoreDaAggiornare = cosmoTFruitoreRepository.save(fruitoreDaAggiornare);

    logger.info(methodName, "Fruitore con id {} aggiornato", fruitoreDaAggiornare.getId());
    return cosmoTFruitoreMapper.toDTO(fruitoreDaAggiornare);
  }

  private void salvaAggiornaAutorizzazioni(List<String> list, CosmoTFruitore cosmoTFruitore) {

    String methodName = "salvaAggiornaAutorizzazioni";
    List<CosmoDAutorizzazioneFruitore> cosmoDAutorizzazioneFruitores = new LinkedList<>();

    for (String auth : list) {

      CosmoDAutorizzazioneFruitore autorizzazione =
          cosmoDAutorizzazioneFruitoreRepository.findByCodice(auth);

      if (autorizzazione == null) {
        logger.error(methodName, String.format(ErrorMessages.F_AUTORIZZAZIONE_NON_TROVATA, auth));
        throw new NotFoundException(
            String.format(ErrorMessages.F_AUTORIZZAZIONE_NON_TROVATA, auth));
      }

      cosmoDAutorizzazioneFruitores.add(autorizzazione);
    }

    cosmoTFruitore.setCosmoDAutorizzazioneFruitores(cosmoDAutorizzazioneFruitores);
    cosmoTFruitoreRepository.save(cosmoTFruitore);
  }

  private void salvaAggiornaEnti(List<Long> entiInput, CosmoTFruitore cosmoTFruitore) {
    Timestamp now = Timestamp.from(Instant.now());

    if (cosmoTFruitore.getCosmoRFruitoreEntes() == null) {
      cosmoTFruitore.setCosmoRFruitoreEntes(new ArrayList<>());
    }

    ComplexListComparator
    .compareLists(cosmoTFruitore.getCosmoRFruitoreEntes(), entiInput,
        (rel, idEnteInput) -> rel.valido() && rel.getId().getIdEnte().equals(idEnteInput))
    .onElementsInFirstNotInSecond(daEliminare -> {
      cosmoRFruitoreEnteRepository.delete(daEliminare);
      cosmoTFruitore.getCosmoRFruitoreEntes().remove(daEliminare);
    }).onElementsInSecondNotInFirst(daInserire -> {
      CosmoRFruitoreEnte cosmoRFruitoreEnte = new CosmoRFruitoreEnte();
      CosmoRFruitoreEntePK pk = new CosmoRFruitoreEntePK();
      pk.setIdEnte(daInserire);
      pk.setIdFruitore(cosmoTFruitore.getId());
      cosmoRFruitoreEnte.setId(pk);
      cosmoRFruitoreEnte.setCosmoTFruitore(cosmoTFruitore);
      cosmoRFruitoreEnte.setDtInizioVal(now);
      cosmoRFruitoreEnte
      .setCosmoTEnte(cosmoTEnteRepository.findOneNotDeleted(daInserire).orElseThrow(
          () -> new NotFoundException("Ente con id " + daInserire + " non trovato")));
      cosmoTFruitore.getCosmoRFruitoreEntes()
      .add(cosmoRFruitoreEnteRepository.save(cosmoRFruitoreEnte));
    });

    cosmoTFruitoreRepository.save(cosmoTFruitore);
  }

  @Override
  public List<OperazioneFruitore> getOperazioniFruitore() {

    List<CosmoDOperazioneFruitore> operazioni = cosmoDOperazioneFruitoreRepository
        .findAllActive(new Sort(Direction.ASC, CosmoDOperazioneFruitore_.descrizione.getName()));
    return operazioni.stream().map(cosmoTFruitoreMapper::toDTO).collect(Collectors.toList());
  }

  @Override
  public SchemaAutenticazioneFruitore postSchemiAuthFruitori(Long idFruitore,
      CreaSchemaAutenticazioneFruitoreRequest body) {

    ValidationUtils.require(idFruitore, "idFruitore");
    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    CosmoTFruitore fruitore =
        cosmoTFruitoreRepository.findOneNotDeleted(idFruitore).orElseThrow(NotFoundException::new);

    CosmoTSchemaAutenticazioneFruitore newEntity = new CosmoTSchemaAutenticazioneFruitore();
    newEntity.setCredenziali(new ArrayList<>());
    newEntity.setFruitore(fruitore);
    newEntity.setInIngresso(body.isInIngresso());
    newEntity.setTipo(cosmoTFruitoreRepository.reference(CosmoDTipoSchemaAutenticazione.class,
        body.getCodiceTipo()));
    newEntity.setTokenEndpoint(body.getTokenEndpoint());
    newEntity.setMappaturaOutputToken(body.getMappaturaOutputToken());
    newEntity.setMappaturaRichiestaToken(body.getMappaturaRichiestaToken());
    newEntity.setNomeHeader(body.getNomeHeader());
    newEntity.setFormatoHeader(body.getFormatoHeader());

    newEntity = cosmoTSchemaAutenticazioneFruitoreRepository.save(newEntity);

    CosmoTCredenzialiAutenticazioneFruitore credenziali =
        new CosmoTCredenzialiAutenticazioneFruitore();
    credenziali.setSchemaAutenticazione(newEntity);
    credenziali.setUsername(body.getCredenziali().getUsername());
    credenziali.setPassword(body.getCredenziali().getPassword());
    credenziali.setClientId(body.getCredenziali().getClientId());
    credenziali.setClientSecret(body.getCredenziali().getClientSecret());

    credenziali = cosmoTCredenzialiAutenticazioneFruitoreRepository.save(credenziali);
    newEntity.getCredenziali().add(credenziali);

    return cosmoTFruitoreMapper.toDTO(newEntity);
  }

  @Override
  public SchemaAutenticazioneFruitore putSchemaAuthFruitore(Long idFruitore,
      Long idSchemaAutenticazione, AggiornaSchemaAutenticazioneFruitoreRequest body) {

    ValidationUtils.require(idFruitore, "idFruitore");
    ValidationUtils.require(idSchemaAutenticazione, "idSchemaAutenticazione");
    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    CosmoTSchemaAutenticazioneFruitore existingEntity = cosmoTSchemaAutenticazioneFruitoreRepository
        .findOneNotDeleted(idSchemaAutenticazione).orElseThrow(NotFoundException::new);

    if (!existingEntity.getFruitore().getId().equals(idFruitore)) {
      throw new NotFoundException();
    }

    CosmoTCredenzialiAutenticazioneFruitore credenziali = existingEntity.getCredenziali().stream()
        .filter(CosmoTEntity::nonCancellato).findAny().orElse(null);

    if (credenziali == null) {
      credenziali = new CosmoTCredenzialiAutenticazioneFruitore();
      credenziali.setSchemaAutenticazione(existingEntity);
      existingEntity.getCredenziali().add(credenziali);
    }

    if (body.getCodiceTipo().equals(TipoSchemaAutenticazione.BASIC.name())) {
      credenziali.setClientId(null);
      credenziali.setClientSecret(null);

      credenziali.setUsername(body.getCredenziali().getUsername());
      if (!StringUtils.isEmpty(body.getCredenziali().getPassword())) {
        credenziali.setPassword(body.getCredenziali().getPassword());
      }

    } else {
      credenziali.setUsername(null);
      credenziali.setPassword(null);

      credenziali.setClientId(body.getCredenziali().getClientId());

      if (!StringUtils.isEmpty(body.getCredenziali().getClientSecret())) {
        credenziali.setClientSecret(body.getCredenziali().getClientSecret());
      }
    }

    existingEntity.setInIngresso(body.isInIngresso());
    existingEntity.setTipo(cosmoTFruitoreRepository.reference(CosmoDTipoSchemaAutenticazione.class,
        body.getCodiceTipo()));
    existingEntity.setTokenEndpoint(body.getTokenEndpoint());
    existingEntity.setMappaturaOutputToken(body.getMappaturaOutputToken());
    existingEntity.setMappaturaRichiestaToken(body.getMappaturaRichiestaToken());
    existingEntity.setContentTypeRichiestaToken(body.getContentTypeRichiestaToken());
    existingEntity.setMetodoRichiestaToken(body.getMetodoRichiestaToken());
    existingEntity.setNomeHeader(body.getNomeHeader());
    existingEntity.setFormatoHeader(body.getFormatoHeader());
    
    cosmoTCredenzialiAutenticazioneFruitoreRepository.save(credenziali);

    existingEntity = cosmoTSchemaAutenticazioneFruitoreRepository.save(existingEntity);
    return cosmoTFruitoreMapper.toDTO(existingEntity);
  }

  @Override
  public void deleteSchemaAuthFruitore(Long idFruitore, Long idSchemaAutenticazione) {

    ValidationUtils.require(idFruitore, "idFruitore");
    ValidationUtils.require(idSchemaAutenticazione, "idSchemaAutenticazione");

    CosmoTSchemaAutenticazioneFruitore existingEntity = cosmoTSchemaAutenticazioneFruitoreRepository
        .findOneNotDeleted(idSchemaAutenticazione).orElseThrow(NotFoundException::new);

    if (!existingEntity.getFruitore().getId().equals(idFruitore)) {
      throw new NotFoundException();
    }

    // non devono esistere endpoint attivi che utilizzino lo schema
    if (existingEntity.getFruitore().getEndpoints().stream()
        .anyMatch(e -> e.nonCancellato() && e.getSchemaAutenticazione() != null
        && e.getSchemaAutenticazione().getId().equals(idSchemaAutenticazione))) {

      throw new BadRequestException(
          "Impossibile eliminare uno schema di autenticazione in uso da degli endpoint attivi.");
    }

    existingEntity.setDtCancellazione(Timestamp.from(Instant.now()));
    existingEntity.setUtenteCancellazione(AuditServiceImpl.getPrincipalCode());

    cosmoTSchemaAutenticazioneFruitoreRepository.save(existingEntity);
  }

  @Override
  public EndpointFruitore postEndpointsFruitori(Long idFruitore, CreaEndpointFruitoreRequest body) {

    ValidationUtils.require(idFruitore, "idFruitore");
    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    CosmoTFruitore fruitore =
        cosmoTFruitoreRepository.findOneNotDeleted(idFruitore).orElseThrow(NotFoundException::new);

    // non deve gia' esistere un endpoint per quella operation
    if (fruitore.getEndpoints().stream()
        .anyMatch(e -> e.nonCancellato() && e.getOperazione() != null
            && (!it.csi.cosmo.common.entities.enums.OperazioneFruitore.CUSTOM.toString()
            .equals(e.getOperazione().getCodice())
            && e.getOperazione().getCodice().equals(body.getCodiceOperazione())
            || (it.csi.cosmo.common.entities.enums.OperazioneFruitore.CUSTOM.toString()
                .equals(e.getOperazione().getCodice())
                    && e.getCodiceDescrittivo().equals(body.getCodiceDescrittivo()))))) {
      throw new ConflictException(
          "Esiste gia' un endpoint attivo per l'operazione " + body.getCodiceOperazione());
    }

    CosmoTEndpointFruitore newEntity = new CosmoTEndpointFruitore();
    newEntity.setCodiceTipoEndpoint(body.getCodiceTipo());
    newEntity.setEndpoint(body.getEndpoint());
    newEntity.setMetodoHttp(body.getMetodoHttp());

    CosmoDOperazioneFruitore operazioneFruitore = cosmoTFruitoreRepository
        .reference(CosmoDOperazioneFruitore.class, body.getCodiceOperazione());

    newEntity.setOperazione(operazioneFruitore);

    if (operazioneFruitore.getPersonalizzabile() != null
        && Boolean.TRUE.equals(operazioneFruitore.getPersonalizzabile())) {
      if (body.getCodiceDescrittivo() == null || body.getCodiceDescrittivo().isBlank()) {
        String errorMessage = "Codice descrittivo dell'endpoint non valido";
        logger.error("postEndpointsFruitori", errorMessage);
        throw new BadRequestException(errorMessage);
      }

      newEntity.setCodiceDescrittivo(body.getCodiceDescrittivo());
    } else {
      newEntity.setCodiceDescrittivo(null);
    }

    newEntity.setSchemaAutenticazione(body.getIdSchemaAutenticazione() != null
        ? cosmoTSchemaAutenticazioneFruitoreRepository
            .findOneNotDeleted(body.getIdSchemaAutenticazione()).orElseThrow(NotFoundException::new)
            : null);
    newEntity.setFruitore(fruitore);

    newEntity = cosmoTEndpointFruitoreRepository.save(newEntity);

    return cosmoTFruitoreMapper.toDTO(newEntity);
  }

  @Override
  public EndpointFruitore putEndpointFruitore(Long idFruitore, Long idEndpoint,
      AggiornaEndpointFruitoreRequest body) {
    final String methodName = "putEndpointFruitore";

    ValidationUtils.require(idFruitore, "idFruitore");
    ValidationUtils.require(idEndpoint, "idEndpoint");
    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);
    
    // non deve gia' esistere un endpoint per quella operation, se non e' custom, o con lo stesso
    // codice descrittivo, se e' custom
    CosmoTFruitore fruitore =
        cosmoTFruitoreRepository.findOneNotDeleted(idFruitore).orElseThrow(NotFoundException::new);

    CosmoTEndpointFruitore existingEntity = cosmoTEndpointFruitoreRepository
        .findOneNotDeleted(idEndpoint).orElseThrow(NotFoundException::new);

    if (!existingEntity.getFruitore().getId().equals(idFruitore)) {
      throw new NotFoundException();
    }

    if (fruitore.getEndpoints().stream().anyMatch(
        e -> !e.getId().equals(idEndpoint) && e.nonCancellato() && e.getOperazione() != null
            && (!it.csi.cosmo.common.entities.enums.OperazioneFruitore.CUSTOM.toString()
            .equals(e.getOperazione().getCodice())
            && e.getOperazione().getCodice().equals(body.getCodiceOperazione())
            || (it.csi.cosmo.common.entities.enums.OperazioneFruitore.CUSTOM.toString()
                .equals(e.getOperazione().getCodice())
                    && e.getCodiceDescrittivo().equals(body.getCodiceDescrittivo()))))) {

      String errorMessage =
          "Esiste gia' un endpoint attivo per l'operazione " + body.getCodiceOperazione();
      logger.error(methodName, errorMessage);
      throw new ConflictException(errorMessage);
    }

    existingEntity.setCodiceTipoEndpoint(body.getCodiceTipo());
    existingEntity.setEndpoint(body.getEndpoint());
    existingEntity.setMetodoHttp(body.getMetodoHttp());

    CosmoDOperazioneFruitore operazioneFruitore =
        cosmoTFruitoreRepository.reference(CosmoDOperazioneFruitore.class,
            body.getCodiceOperazione());

    existingEntity.setOperazione(operazioneFruitore);

    if (operazioneFruitore.getPersonalizzabile() != null
        && Boolean.TRUE.equals(operazioneFruitore.getPersonalizzabile())) {

      if (body.getCodiceDescrittivo() == null || body.getCodiceDescrittivo().isBlank()) {
        String errorMessage = "Codice descrittivo dell'endpoint non valido";
        logger.error(methodName, errorMessage);
        throw new BadRequestException(errorMessage);
      }

      existingEntity.setCodiceDescrittivo(body.getCodiceDescrittivo());
    } else {
      existingEntity.setCodiceDescrittivo(null);
    }

    existingEntity.setSchemaAutenticazione(body.getIdSchemaAutenticazione() != null
        ? cosmoTSchemaAutenticazioneFruitoreRepository
            .findOneNotDeleted(body.getIdSchemaAutenticazione()).orElseThrow(NotFoundException::new)
            : null);

    cosmoTEndpointFruitoreRepository.save(existingEntity);
    return cosmoTFruitoreMapper.toDTO(existingEntity);
  }

  @Override
  public void deleteEndpointFruitore(Long idFruitore, Long idEndpoint) {

    ValidationUtils.require(idFruitore, "idFruitore");
    ValidationUtils.require(idEndpoint, "idEndpoint");

    CosmoTEndpointFruitore existingEntity = cosmoTEndpointFruitoreRepository
        .findOneNotDeleted(idEndpoint).orElseThrow(NotFoundException::new);

    if (!existingEntity.getFruitore().getId().equals(idFruitore)) {
      throw new NotFoundException();
    }

    existingEntity.setDtCancellazione(Timestamp.from(Instant.now()));
    existingEntity.setUtenteCancellazione(AuditServiceImpl.getPrincipalCode());

    cosmoTEndpointFruitoreRepository.save(existingEntity);
  }

  private void checkConflict(SingularAttribute<CosmoTFruitore, String> field, String value,
      Long excludeId) {
    findByFieldEqualsIgnoreCase(field, value, excludeId).ifPresent(other -> {
      throw new ConflictException(
          "Campo \"" + field.getName() + "\" gia' assegnato ad altro fruitore");
    });
  }

  private Optional<CosmoTFruitore> findByFieldEqualsIgnoreCase(
      SingularAttribute<CosmoTFruitore, String> field, String value, Long excludeId) {
    return cosmoTFruitoreRepository.findAllNotDeleted((root, query, cb) -> {
      var condition =
          cb.equal(cb.upper(root.get(field)), value != null ? value.toUpperCase() : null);
      if (excludeId != null) {
        condition = cb.and(condition, cb.notEqual(root.get(CosmoTFruitore_.id), excludeId));
      }
      return condition;
    }).stream().findAny();
  }

  private String clean(String raw) {
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    return raw.strip();
  }

  @Transactional(readOnly = true)
  @Override
  public Fruitore postFruitoriAutentica(CredenzialiAutenticazioneFruitore body) {
    ValidationUtils.require(body, "body");
    CosmoTFruitore fruitoreAutenticato = null;

    if (!StringUtils.isBlank(body.getUsername())) {
      List<CosmoTCredenzialiAutenticazioneFruitore> credenzialiValide =
          cosmoTCredenzialiAutenticazioneFruitoreRepository.findAllNotDeleted((root, cq, cb) -> {
            var joinSchema =
                root.join(CosmoTCredenzialiAutenticazioneFruitore_.schemaAutenticazione);
            //@formatter:off
            return cb.and(
                cb.isTrue(joinSchema.get(CosmoTSchemaAutenticazioneFruitore_.inIngresso)),
                cb.isNotNull(root.get(CosmoTCredenzialiAutenticazioneFruitore_.username)),
                cb.isNotNull(root.get(CosmoTCredenzialiAutenticazioneFruitore_.password)),
                cb.equal(root.get(CosmoTCredenzialiAutenticazioneFruitore_.username), ValidationUtils.require(body.getUsername(), "username")),
                cb.equal(root.get(CosmoTCredenzialiAutenticazioneFruitore_.password), ValidationUtils.require(body.getPassword(), "password"))
                );
            //@formatter:on
          });

      List<CosmoTFruitore> fruitori = credenzialiValide.stream()
          .filter(cred -> cred.nonCancellato() && cred.getSchemaAutenticazione().nonCancellato()
              && cred.getSchemaAutenticazione().getFruitore().nonCancellato())
          .map(cred -> cred.getSchemaAutenticazione().getFruitore()).distinct()
          .collect(Collectors.toList());

      if (fruitori.isEmpty()) {
        throw new UnauthorizedException("Nessun fruitore autenticabile con le credenziali fornite");
      } else if (fruitori.size() > 1) {
        throw new ConflictException("Fruitore non univoco individuato dalle credenziali fornite");
      } else {
        fruitoreAutenticato = fruitori.get(0);
      }

    } else if (StringUtils.isBlank(body.getClientId())) {
      throw new BadRequestException(
          "Autenticazione mediante token non supportata al momento. Utilizzare la basic authentication.");

    } else {
      throw new BadRequestException("Nessuna coppia di credenziali fornite.");
    }

    return cosmoTFruitoreMapper.toDTO(fruitoreAutenticato);
  }

}

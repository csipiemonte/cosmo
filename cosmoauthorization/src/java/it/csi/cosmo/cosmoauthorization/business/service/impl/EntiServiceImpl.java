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
import javax.persistence.metamodel.SingularAttribute;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEnte;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEntePK;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEntePK_;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEnte_;
import it.csi.cosmo.common.entities.CosmoRUtenteEnte;
import it.csi.cosmo.common.entities.CosmoRUtenteEntePK;
import it.csi.cosmo.common.entities.CosmoRUtenteProfilo;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTProfilo;
import it.csi.cosmo.common.entities.CosmoTProfilo_;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.proto.CosmoCEntity_;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmoauthorization.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoauthorization.business.service.EntiService;
import it.csi.cosmo.cosmoauthorization.config.ErrorMessages;
import it.csi.cosmo.cosmoauthorization.config.ParametriApplicativo;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaEnteRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaEnteRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.Ente;
import it.csi.cosmo.cosmoauthorization.dto.rest.EnteResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.EntiResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.FiltroRicercaEntiDTO;
import it.csi.cosmo.cosmoauthorization.dto.rest.PageInfo;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoTEnteMapper;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoCConfigurazioneEnteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoRUtenteEnteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoRUtenteProfiloRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTProfiloRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTUtenteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.specifications.CosmoTEnteSpecifications;
import it.csi.cosmo.cosmoauthorization.util.logger.LogCategory;
import it.csi.cosmo.cosmoauthorization.util.logger.LoggerFactory;


/**
 *
 */

@Service
@Transactional
public class EntiServiceImpl implements EntiService {

  /**
   *
   */
  private static final String CODICE_PROFILO_AMMINISTRATORE_ENTE = "ADMIN";

  private static final String CLASS_NAME = EntiServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  private static final String CODICE_PROFILO_UTENTE_DEFAULT = "profilo.utente.default";
  @Autowired
  private CosmoTEnteRepository cosmoTEnteRepository;

  @Autowired
  private CosmoTUtenteRepository cosmoTUtenteRepository;

  @Autowired
  private CosmoRUtenteEnteRepository cosmoRUtenteEnteRepository;

  @Autowired
  private CosmoRUtenteProfiloRepository cosmoRUtenteProfiloRepository;

  @Autowired
  private CosmoTProfiloRepository cosmoTProfiloRepository;

  @Autowired
  private CosmoCConfigurazioneEnteRepository cosmoCConfigurazioneEnteRepository;

  @Autowired
  private CosmoTEnteMapper cosmoTEnteMapper;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteEnte(Long id) {
    String methodName = "deleteEnte";
    ValidationUtils.require(id, "id ente");

    CosmoTEnte enteDaEliminare = cosmoTEnteRepository.findOneNotDeleted(id).orElseThrow(
        () -> new NotFoundException(String.format(ErrorMessages.E_ENTE_NON_TROVATO, id)));

    enteDaEliminare.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));
    enteDaEliminare.setUtenteCancellazione(AuditServiceImpl.getPrincipalCode());
    enteDaEliminare = cosmoTEnteRepository.save(enteDaEliminare);

    logger.info(methodName, "Ente con id {} eliminato", enteDaEliminare.getId());
  }

  @Override
  @Transactional(readOnly = true)
  public EntiResponse getEnti(String filter) {
    EntiResponse output = new EntiResponse();

    GenericRicercaParametricaDTO<FiltroRicercaEntiDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaEntiDTO.class);

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(ParametriApplicativo.MAX_PAGE_SIZE).asInteger());

    Page<CosmoTEnte> pageEnti = cosmoTEnteRepository.findAllNotDeleted(CosmoTEnteSpecifications
        .findByFilters(ricercaParametrica.getFilter(), ricercaParametrica.getSort()), paging);

    List<CosmoTEnte> entiSuDB = pageEnti.getContent();

    List<Ente> enti = new LinkedList<>();
    entiSuDB.forEach(enteSuDB -> enti.add(cosmoTEnteMapper.toDTOSenzaGruppiELogo(enteSuDB)));
    output.setEnti(enti);

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      SearchUtils.filterFields(enti, Arrays.asList(ricercaParametrica.getFields().split(",")));
    }

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(pageEnti.getNumber());
    pageInfo.setPageSize(pageEnti.getSize());
    pageInfo.setTotalElements(Math.toIntExact(pageEnti.getTotalElements()));
    pageInfo.setTotalPages(pageEnti.getTotalPages());
    output.setPageInfo(pageInfo);

    return output;
  }

  @Override
  @Transactional(readOnly = true)
  public Ente getEnte(Long id) {
    ValidationUtils.require(id, "id ente");
    EnteResponse enteResponse = new EnteResponse();

    CosmoTEnte enteDB = cosmoTEnteRepository.findOneNotDeleted(id).orElseThrow(
        () -> new NotFoundException(String.format(ErrorMessages.E_ENTE_NON_TROVATO, id)));

    Ente ente = cosmoTEnteMapper.toDTO(enteDB);
    enteResponse.setEnte(ente);

    CosmoCConfigurazioneEnte configurazione = cosmoCConfigurazioneEnteRepository
        .findOne((root, cq, cb) -> cb.and(cb.isNull(root.get(CosmoCEntity_.dtFineVal)),
            cb.equal(root.get(CosmoCConfigurazioneEnte_.id).get(CosmoCConfigurazioneEntePK_.idEnte),
                id),
            cb.equal(root.get(CosmoCConfigurazioneEnte_.id).get(CosmoCConfigurazioneEntePK_.chiave),
                CODICE_PROFILO_UTENTE_DEFAULT)));

    if (configurazione != null) {
      CosmoTProfilo profiloEsistente = cosmoTProfiloRepository
          .findOneNotDeletedByField(CosmoTProfilo_.codice, configurazione.getValore()).orElse(null);

      if (profiloEsistente != null) {
        ente.setCodiceProfiloDefault(profiloEsistente.getCodice());
      }

    }


    return ente;
  }


  @Override
  @Transactional(rollbackFor = Exception.class)
  public Ente createEnte(CreaEnteRequest request) {
    String methodName = "createEnte";

    ValidationUtils.require(request, "request");
    ValidationUtils.validaAnnotations(request);

    if ((request.getUtentiAmministratori() == null
        || request.getUtentiAmministratori().isEmpty())
        && (request.getNuoviUtentiAmministratori() == null
        || request.getNuoviUtentiAmministratori().isEmpty())) {
      throw new BadRequestException(
          "Occorre specificare un id utente amministratore esistente o i dati per creare un nuovo utente");
    }

    // controlla che non ci siano conflitti
    findByFieldEqualsIgnoreCase(CosmoTEnte_.codiceFiscale, request.getCodiceFiscale().trim(), null)
    .ifPresent(other -> {
      throw new ConflictException(
          String.format(ErrorMessages.E_ENTE_CF_GIA_ESISTENTE, request.getCodiceFiscale()));
    });

    findByFieldEqualsIgnoreCase(CosmoTEnte_.codiceIpa, request.getCodiceIpa().trim(), null)
    .ifPresent(other -> {
      throw new ConflictException(
          String.format(ErrorMessages.E_ENTE_IPA_GIA_ESISTENTE, request.getCodiceIpa()));
    });

    findByFieldEqualsIgnoreCase(CosmoTEnte_.nome, request.getNome().trim(), null)
    .ifPresent(other -> {
      throw new ConflictException(
          String.format(ErrorMessages.E_ENTE_NOME_GIA_ESISTENTE, request.getNome()));
    });


    // crea l'ente su DB
    CosmoTEnte enteDaSalvare = new CosmoTEnte();
    enteDaSalvare.setCodiceFiscale(request.getCodiceFiscale().trim());
    enteDaSalvare.setCodiceIpa(request.getCodiceIpa().trim());
    enteDaSalvare.setNome(request.getNome().trim());
    enteDaSalvare.setCosmoRUtenteEntes(new ArrayList<>());

    CosmoTEnte enteSalvato = cosmoTEnteRepository.save(enteDaSalvare);
    logger.info(methodName, "Ente con id {} creato", enteSalvato.getId());


    request.getUtentiAmministratori().stream().forEach(elem -> {
      var utente = cosmoTUtenteRepository.findOneNotDeleted(elem.getId())
          .orElseThrow(() -> new BadRequestException("Utente amministratore non specificato"));
      associaUtenteEnte(utente, enteSalvato, elem.getEmail(), elem.getTelefono());
      associaProfiloAmministratore(utente, enteSalvato);
    });

    request.getNuoviUtentiAmministratori().stream().forEach(elem -> {
      var utente = creaNuovoUtente(elem.getNome(), elem.getCognome(), elem.getCodiceFiscale());
      associaUtenteEnte(utente, enteSalvato, elem.getEmail(), elem.getTelefono());
      associaProfiloAmministratore(utente, enteSalvato);
    });

    Ente ente = cosmoTEnteMapper.toDTO(enteSalvato);

    if (StringUtils.isNotBlank(request.getCodiceProfiloDefault())) {
      CosmoTProfilo profiloEsistente = cosmoTProfiloRepository
          .findOneNotDeletedByField(CosmoTProfilo_.codice, request.getCodiceProfiloDefault())
          .orElse(null);

      if (profiloEsistente != null) {
        CosmoCConfigurazioneEnte configurazioneDaSalvare = new CosmoCConfigurazioneEnte();
        CosmoCConfigurazioneEntePK pk = new CosmoCConfigurazioneEntePK();
        pk.setChiave(CODICE_PROFILO_UTENTE_DEFAULT);
        pk.setIdEnte(enteSalvato.getId());
        configurazioneDaSalvare.setId(pk);
        configurazioneDaSalvare.setDescrizione("Profilo di default dell'utente");
        configurazioneDaSalvare.setValore(profiloEsistente.getCodice());
        configurazioneDaSalvare.setCosmoTEnte(enteSalvato);
        configurazioneDaSalvare.setDtInizioVal(Timestamp.valueOf(LocalDateTime.now()));

        cosmoCConfigurazioneEnteRepository.save(configurazioneDaSalvare);

        ente.setCodiceProfiloDefault(profiloEsistente.getCodice());
      }

    }

    return ente;
  }

  private Optional<CosmoTEnte> findByFieldEqualsIgnoreCase(
      SingularAttribute<CosmoTEnte, String> field, String value, Long excludeId) {
    return cosmoTEnteRepository.findAllNotDeleted((root, queru, cb) -> {
      var condition = cb.equal(cb.upper(root.get(field)), value.toUpperCase());
      if (excludeId != null) {
        condition = cb.and(condition, cb.notEqual(root.get(CosmoTEnte_.id), excludeId));
      }
      return condition;
    }).stream().findAny();
  }

  private CosmoRUtenteProfilo associaProfiloAmministratore(CosmoTUtente utente, CosmoTEnte ente) {
    Timestamp now = Timestamp.from(Instant.now());

    var profiloAdmin = cosmoTProfiloRepository
        .findOneNotDeletedByField(CosmoTProfilo_.codice, CODICE_PROFILO_AMMINISTRATORE_ENTE)
        .orElseThrow(
            () -> new InternalServerException("Profilo di amministrazione non configurato"));

    CosmoRUtenteProfilo rUtenteProfilo = new CosmoRUtenteProfilo();
    rUtenteProfilo.setCosmoTEnte(ente);
    rUtenteProfilo.setCosmoTProfilo(profiloAdmin);
    rUtenteProfilo.setCosmoTUtente(utente);
    rUtenteProfilo.setDtInizioVal(now);

    return cosmoRUtenteProfiloRepository.save(rUtenteProfilo);
  }

  private CosmoTUtente creaNuovoUtente(String nome, String cognome, String codiceFiscale) {

    var elem = cosmoTUtenteRepository.findByCodiceFiscale(codiceFiscale);
    
    if (elem != null) {
      throw new BadRequestException("Utente amministratore già esistente");
    }

    CosmoTUtente utente = new CosmoTUtente();
    utente.setCodiceFiscale(codiceFiscale);
    utente.setCognome(cognome);
    utente.setNome(nome);
    utente.setCosmoRUtenteEntes(new ArrayList<>());
    utente.setCosmoRUtenteProfilos(new ArrayList<>());

    utente = cosmoTUtenteRepository.save(utente);

    logger.info("creaNuovoUtente", "creato utente {}", utente.getId());
    return utente;
  }

  private CosmoRUtenteEnte associaUtenteEnte(CosmoTUtente utente, CosmoTEnte ente,
      String email, String telefono) {
    ValidationUtils.require(utente, "utente");
    ValidationUtils.require(ente, "ente");

    Timestamp now = Timestamp.from(Instant.now());

    CosmoRUtenteEnte relazioneUtenteEnte = new CosmoRUtenteEnte();
    relazioneUtenteEnte.setCosmoTEnte(ente);
    relazioneUtenteEnte.setCosmoTUtente(utente);
    relazioneUtenteEnte.setDtInizioVal(now);
    relazioneUtenteEnte.setEmail(email);
    relazioneUtenteEnte.setTelefono(telefono);
    CosmoRUtenteEntePK cosmoRUtenteEntePK = new CosmoRUtenteEntePK();
    cosmoRUtenteEntePK.setIdEnte(ente.getId());
    cosmoRUtenteEntePK.setIdUtente(utente.getId());
    relazioneUtenteEnte.setId(cosmoRUtenteEntePK);

    relazioneUtenteEnte = cosmoRUtenteEnteRepository.save(relazioneUtenteEnte);
    utente.getCosmoRUtenteEntes().add(relazioneUtenteEnte);
    ente.getCosmoRUtenteEntes().add(relazioneUtenteEnte);

    return relazioneUtenteEnte;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Ente updateEnte(Long id, AggiornaEnteRequest request) {
    String methodName = "updateEnte";
    ValidationUtils.require(id, "id ente");
    ValidationUtils.require(request, "request");
    ValidationUtils.validaAnnotations(request);

    CosmoTEnte enteDaAggiornare = cosmoTEnteRepository.findOne(id);
    if (enteDaAggiornare == null || enteDaAggiornare.cancellato()) {
      logger.error(methodName, String.format(ErrorMessages.E_ENTE_NON_TROVATO, id));
      throw new NotFoundException(String.format(ErrorMessages.E_ENTE_NON_TROVATO, id));
    }

    // check conflicts
    findByFieldEqualsIgnoreCase(CosmoTEnte_.nome, request.getNome().trim(), id).ifPresent(other -> {
      throw new ConflictException(
          String.format(ErrorMessages.E_ENTE_NOME_GIA_ESISTENTE, request.getNome()));
    });

    enteDaAggiornare.setNome(request.getNome().trim());
    CosmoTEnte enteAggiornato = cosmoTEnteRepository.save(enteDaAggiornare);

    request.getUtentiAmministratori().stream().forEach(elem -> {
      var utente = cosmoTUtenteRepository.findOneNotDeleted(elem.getId())
          .orElseThrow(() -> new BadRequestException("Utente amministratore non specificato"));
      associaUtenteEnte(utente, enteAggiornato, elem.getEmail(), elem.getTelefono());
      associaProfiloAmministratore(utente, enteAggiornato);
    });

    request.getNuoviUtentiAmministratori().stream().forEach(elem -> {
      var utente = creaNuovoUtente(elem.getNome(), elem.getCognome(), elem.getCodiceFiscale());
      associaUtenteEnte(utente, enteAggiornato, elem.getEmail(), elem.getTelefono());
      associaProfiloAmministratore(utente, enteAggiornato);
    });


    Ente ente = cosmoTEnteMapper.toDTO(enteAggiornato);

    if (StringUtils.isNotBlank(request.getCodiceProfiloDefault())) {
      CosmoTProfilo profiloEsistente = cosmoTProfiloRepository
          .findOneNotDeletedByField(CosmoTProfilo_.codice, request.getCodiceProfiloDefault())
          .orElse(null);

      if (profiloEsistente != null) {

        CosmoCConfigurazioneEnte configurazioneDaAggiornare =
            cosmoCConfigurazioneEnteRepository.findOne((root, cq, cb) -> cb.and(
                cb.isNull(root.get(CosmoCEntity_.dtFineVal)),
                cb.equal(
                    root.get(CosmoCConfigurazioneEnte_.id).get(CosmoCConfigurazioneEntePK_.idEnte),
                    id),
                cb.equal(
                    root.get(CosmoCConfigurazioneEnte_.id).get(CosmoCConfigurazioneEntePK_.chiave),
                    CODICE_PROFILO_UTENTE_DEFAULT)));

        if (configurazioneDaAggiornare != null) {
          configurazioneDaAggiornare.setValore(profiloEsistente.getCodice());
          cosmoCConfigurazioneEnteRepository.save(configurazioneDaAggiornare);
          ente.setCodiceProfiloDefault(profiloEsistente.getCodice());
        } else {

          CosmoCConfigurazioneEnte configurazioneDaSalvare = new CosmoCConfigurazioneEnte();
          CosmoCConfigurazioneEntePK pk = new CosmoCConfigurazioneEntePK();
          pk.setChiave(CODICE_PROFILO_UTENTE_DEFAULT);
          pk.setIdEnte(enteAggiornato.getId());
          configurazioneDaSalvare.setId(pk);
          configurazioneDaSalvare.setDescrizione("Profilo di default dell'utente");
          configurazioneDaSalvare.setValore(profiloEsistente.getCodice());
          configurazioneDaSalvare.setCosmoTEnte(enteAggiornato);
          configurazioneDaSalvare.setDtInizioVal(Timestamp.valueOf(LocalDateTime.now()));

          cosmoCConfigurazioneEnteRepository.save(configurazioneDaSalvare);
          ente.setCodiceProfiloDefault(profiloEsistente.getCodice());
        }

      }

    } else {
      CosmoCConfigurazioneEnte configurazione = cosmoCConfigurazioneEnteRepository
          .findOne((root, cq, cb) -> cb.and(cb.isNull(root.get(CosmoCEntity_.dtFineVal)),
              cb.equal(root.get(CosmoCConfigurazioneEnte_.cosmoTEnte).get(CosmoTEnte_.id), id),
              cb.equal(
                  root.get(CosmoCConfigurazioneEnte_.id).get(CosmoCConfigurazioneEntePK_.chiave),
                  CODICE_PROFILO_UTENTE_DEFAULT)));

      if (configurazione != null) {
        configurazione.setDtFineVal(Timestamp.valueOf(LocalDateTime.now()));
        cosmoCConfigurazioneEnteRepository.save(configurazione);
      }


    }


    logger.info(methodName, "Ente con id {} aggiornato", enteAggiornato.getId());
    return ente;
  }

}

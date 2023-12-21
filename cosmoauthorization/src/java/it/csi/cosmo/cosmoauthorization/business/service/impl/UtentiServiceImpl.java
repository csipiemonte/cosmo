/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.dto.search.LongFilter;
import it.csi.cosmo.common.entities.CosmoRUtenteEnte;
import it.csi.cosmo.common.entities.CosmoRUtenteEntePK;
import it.csi.cosmo.common.entities.CosmoRUtenteProfilo;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTProfilo;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.proto.CampiTecniciEntity;
import it.csi.cosmo.common.entities.proto.CosmoREntity;
import it.csi.cosmo.common.entities.proto.IntervalloValiditaEntity;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.exception.UnauthorizedException;
import it.csi.cosmo.common.exception.UnprocessableEntityException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.cosmoauthorization.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoauthorization.business.service.UtentiService;
import it.csi.cosmo.cosmoauthorization.config.ErrorMessages;
import it.csi.cosmo.cosmoauthorization.config.ParametriApplicativo;
import it.csi.cosmo.cosmoauthorization.dto.rest.AssociazioneEnteUtente;
import it.csi.cosmo.cosmoauthorization.dto.rest.AssociazioneUtenteProfilo;
import it.csi.cosmo.cosmoauthorization.dto.rest.FiltroRicercaUtentiDTO;
import it.csi.cosmo.cosmoauthorization.dto.rest.PageInfo;
import it.csi.cosmo.cosmoauthorization.dto.rest.Utente;
import it.csi.cosmo.cosmoauthorization.dto.rest.UtenteCampiTecnici;
import it.csi.cosmo.cosmoauthorization.dto.rest.UtentiResponse;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoTUtenteMapper;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoRUtenteEnteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoRUtenteProfiloRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTProfiloRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTUtenteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.specifications.CosmoTUtenteSpecifications;
import it.csi.cosmo.cosmoauthorization.security.SecurityUtils;
import it.csi.cosmo.cosmoauthorization.util.CommonUtils;
import it.csi.cosmo.cosmoauthorization.util.logger.LogCategory;
import it.csi.cosmo.cosmoauthorization.util.logger.LoggerFactory;


/**
 *
 */
@Service
@Transactional
public class UtentiServiceImpl implements UtentiService {

  private static final String UTENTE_CON_ID = "Utente con id ";

  private static final String CLASS_NAME = UtentiServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private CosmoTUtenteRepository cosmoTUtenteRepository;

  @Autowired
  private CosmoTEnteRepository cosmoTEnteRepository;

  @Autowired
  private CosmoRUtenteEnteRepository cosmoRUtenteEnteRepository;

  @Autowired
  private CosmoRUtenteProfiloRepository cosmoRUtenteProfiloRepository;

  @Autowired
  private CosmoTProfiloRepository cosmoTProfiloRepository;

  @Autowired
  private CosmoTUtenteMapper cosmoTUtenteMapper;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Utente deleteUtente(String id) {

    String methodName = "deleteUtente";

    CommonUtils.validaDatiInput(id, "id utente");

    CosmoTUtente utenteDaEliminare = cosmoTUtenteRepository.findOne(Long.valueOf(id));

    if (utenteDaEliminare == null) {
      logger.error(methodName, String.format(ErrorMessages.U_UTENTE_NON_TROVATO, id));
      throw new NotFoundException(String.format(ErrorMessages.U_UTENTE_NON_TROVATO, id));
    }

    var ente = cosmoTEnteRepository.findOne(SecurityUtils.getUtenteCorrente().getEnte().getId());
    if (ente == null) {
      throw new UnauthorizedException("Nessun ente corrente.");
    }

    utenteDaEliminare.getCosmoRUtenteEntes().stream()
    .filter(r -> r.valido() && r.getCosmoTEnte() != null && r.getCosmoTEnte().getId().equals(ente.getId()))
    .forEach(cosmoRUtenteEnteRepository::deactivate);

    utenteDaEliminare.getCosmoRUtenteProfilos().stream()
    .filter(r -> r.valido() && r.getCosmoTEnte() != null
    && r.getCosmoTEnte().getId().equals(ente.getId()))
    .forEach(cosmoRUtenteProfiloRepository::deactivate);

    logger.info(methodName, UTENTE_CON_ID + utenteDaEliminare.getId() + " eliminato");

    return cosmoTUtenteMapper.toDTO(utenteDaEliminare);
  }

  @Override
  @Transactional(readOnly = true)
  public Utente getUtenteDaId(String id) {
    String methodName = "getUtenteDaId";

    CommonUtils.validaDatiInput(id, "id utente");

    CosmoTUtente utente = cosmoTUtenteRepository.findOne(Long.valueOf(id));
    if (utente == null) {
      logger.error(methodName, String.format(ErrorMessages.U_UTENTE_NON_TROVATO, id));
      throw new NotFoundException(String.format(ErrorMessages.U_UTENTE_NON_TROVATO, id));
    }

    return cosmoTUtenteMapper.toDTO(utente);
  }

  @Override
  @Transactional(readOnly = true)
  public UtentiResponse getUtenti(String filter) {

    return getUtenti(filter, null);
  }

  @Override
  @Transactional(readOnly = true)
  public UtentiResponse getUtentiEnte(String filter) {

    return getUtenti(filter, SecurityUtils.getUtenteCorrente().getEnte().getId());
  }


  private UtentiResponse getUtenti(String filter, Long idEnte) {

    UtentiResponse output = new UtentiResponse();

    GenericRicercaParametricaDTO<FiltroRicercaUtentiDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaUtentiDTO.class);

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(ParametriApplicativo.MAX_PAGE_SIZE).asInteger());

    Page<CosmoTUtente> pageUtenti =
        cosmoTUtenteRepository.findAllNotDeleted(CosmoTUtenteSpecifications.findByFilters(idEnte,
            ricercaParametrica.getFilter(), ricercaParametrica.getSort()), paging);

    List<CosmoTUtente> utentiSuDB = pageUtenti.getContent();

    LongFilter ente = (ricercaParametrica.getFilter() != null
        && ricercaParametrica.getFilter().getIdEnte() != null)
        ? ricercaParametrica.getFilter().getIdEnte()
            : null;

    List<Utente> utenti = new LinkedList<>();
    utentiSuDB.forEach(utenteSuDB -> {
      var utente = cosmoTUtenteMapper.toLightDTO(utenteSuDB);
      utenti.add(utente);

      if (ente != null) {
        Optional<CosmoRUtenteEnte> utenteEnte = utenteSuDB.getCosmoRUtenteEntes().stream()
            .filter(ue -> ue.getCosmoTEnte().getId().equals(ente.getEquals())).findAny();
        if (utenteEnte.isPresent()) {
          var associazioneEnteUtente = new AssociazioneEnteUtente();
          associazioneEnteUtente.setEmail(utenteEnte.get().getEmail());
          associazioneEnteUtente.setTelefono(utenteEnte.get().getTelefono());
          utente.getEnti().add(associazioneEnteUtente);
        }
      }
    });
    output.setUtenti(utenti);

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      it.csi.cosmo.common.util.SearchUtils.filterFields(utenti,
          Arrays.asList(ricercaParametrica.getFields().split(",")));
    }

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(pageUtenti.getNumber());
    pageInfo.setPageSize(pageUtenti.getSize());
    pageInfo.setTotalElements(Math.toIntExact(pageUtenti.getTotalElements()));
    pageInfo.setTotalPages(pageUtenti.getTotalPages());

    output.setPageInfo(pageInfo);

    return output;
  }

  @Override
  @Transactional(readOnly = true)
  public UtenteCampiTecnici getUtenteConValidita(String idUtente, String idEnte) {
    String methodName = "getUtenteConValidita";

    CommonUtils.validaDatiInput(idUtente, "id utente");
    CommonUtils.validaDatiInput(idEnte, "id ente");
    Long idEnteLong = Long.valueOf(idEnte);
    Long idUtenteLong = Long.valueOf(idUtente);

    CosmoTUtente utente = cosmoTUtenteRepository.findOne(idUtenteLong);
    if (utente == null) {
      logger.error(methodName, String.format(ErrorMessages.U_UTENTE_NON_TROVATO, idUtente));
      throw new NotFoundException(String.format(ErrorMessages.U_UTENTE_NON_TROVATO, idUtente));
    }

    utente.setCosmoRUtenteProfilos(utente.getCosmoRUtenteProfilos().stream()
        .filter(utenteProfilo -> utenteProfilo.valido() && utenteProfilo.getCosmoTEnte() != null
        && utenteProfilo.getCosmoTEnte().getId().equals(idEnteLong))
        .collect(Collectors.toList()));

    UtenteCampiTecnici utenteCampiTecnici = new UtenteCampiTecnici();

    var mapped = cosmoTUtenteMapper.toDTO(utente);
    utenteCampiTecnici.setUtente(mapped);

    Optional<CosmoRUtenteEnte> utenteEnte = utente.getCosmoRUtenteEntes().stream()
        .filter(ue -> ue.getCosmoTEnte().getId().equals(idEnteLong) && ue.getDtFineVal() == null)
        .findAny();

    // filtra per idEnte
    mapped.setPreferenze(null);

    mapped.setEnti(mapped.getEnti().stream()
        .filter(e -> e.getEnte() != null && e.getEnte().getId().equals(idEnteLong))
        .map(e -> {
          e.getEnte().setGruppi(null);
          e.getEnte().setPreferenze(null);
          e.getEnte().setLogo(null);
          return e;
        })
        .collect(Collectors.toList()));

    mapped.setGruppi(mapped.getGruppi().stream()
        .filter(e -> e.getEnte() != null && e.getEnte().getId().equals(idEnteLong))
        .map(e -> {
          e.setEnte(null);
          e.setUtenti(null);
          return e;
        })
        .collect(Collectors.toList()));

    mapped.setProfili(mapped.getProfili().stream()
        .filter(e -> e.getEnte() != null && e.getEnte().getId().equals(idEnteLong))
        .map(e -> {
          e.getEnte().setGruppi(null);
          e.getEnte().setPreferenze(null);
          e.getEnte().setLogo(null);
          return e;
        })
        .collect(Collectors.toList()));

    if (utenteEnte.isPresent()) {
      utenteCampiTecnici.setDtInizioValidita(utenteEnte.get().getDtInizioVal() == null ? null
          : OffsetDateTime.ofInstant(
              Instant.ofEpochMilli(utenteEnte.get().getDtInizioVal().getTime()),
              ZoneId.systemDefault()));
      utenteCampiTecnici.setDtFineValidita(utenteEnte.get().getDtFineVal() == null ? null
          : OffsetDateTime.ofInstant(
              Instant.ofEpochMilli(utenteEnte.get().getDtFineVal().getTime()),
              ZoneId.systemDefault()));

      return utenteCampiTecnici;
    } else {
      return null;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Utente getUtenteDaCodiceFiscale(String codiceFiscale) {
    String methodName = "getUtenteDaCodiceFiscale";

    if (StringUtils.isBlank(codiceFiscale)) {
      logger.error(methodName,
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "codice fiscale"));
      throw new BadRequestException(
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "codice fiscale"));
    }

    CosmoTUtente utente = cosmoTUtenteRepository.findByCodiceFiscale(codiceFiscale);
    if (utente == null) {
      logger.error(methodName,
          String.format(ErrorMessages.U_UTENTE_CON_CODICE_FISCALE_NON_TROVATO, codiceFiscale));
      throw new NotFoundException(
          String.format(ErrorMessages.U_UTENTE_CON_CODICE_FISCALE_NON_TROVATO, codiceFiscale));
    }
    utente.setCosmoTGruppos(utente.getCosmoTGruppos().stream()
        .filter(CampiTecniciEntity::nonCancellato)
        .distinct()
        .filter(gruppo -> gruppo.getAssociazioniUtenti().stream()
            .anyMatch(associazioneUtente -> associazioneUtente.nonCancellato()
                && associazioneUtente.getIdUtente().equals(utente.getId())))
        .collect(Collectors.toList()));

    utente.setCosmoRUtenteEntes(
        utente.getCosmoRUtenteEntes().stream().filter(CosmoREntity::valido)
        .collect(Collectors.toList()));

    utente.setCosmoRUtenteProfilos(utente.getCosmoRUtenteProfilos().stream()
        .filter(CosmoREntity::valido).collect(Collectors.toList()));

    return cosmoTUtenteMapper.toDTO(utente);
  }

  @Override
  @Transactional(readOnly = true)
  public Utente getUtenteCorrente() {
    String methodName = "getUtenteCorrente";

    CosmoTUtente utente = cosmoTUtenteRepository
        .findByCodiceFiscale(SecurityUtils.getUtenteCorrente().getCodiceFiscale());

    if (utente == null) {
      logger.error(methodName, String.format(ErrorMessages.U_UTENTE_CON_CODICE_FISCALE_NON_TROVATO,
          SecurityUtils.getUtenteCorrente().getCodiceFiscale()));
      throw new NotFoundException(ErrorMessages.U_UTENTE_CORRENTE_NON_TROVATO);
    }

    utente.getCosmoRUtenteProfilos().removeIf(IntervalloValiditaEntity::nonValido);
    utente.getCosmoRUtenteProfilos()
    .removeIf(r -> r.getCosmoTEnte() != null && r.getCosmoTEnte().cancellato());

    utente.getCosmoRUtenteEntes().removeIf(CosmoREntity::nonValido);

    Utente mapped = cosmoTUtenteMapper.toDTO(utente);

      mapped.getEnti().forEach(e -> {
        if (e.getEnte() != null) {
          e.getEnte().setPreferenze(null);
          e.getEnte().setGruppi(null);
        }
      });

      mapped.getProfili().forEach(p -> {
        if (p.getEnte() != null) {
          p.getEnte().setPreferenze(null);
          p.getEnte().setGruppi(null);
        }
      });

    return mapped;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Utente createUtente(UtenteCampiTecnici utente) {
    String methodName = "createUtente";

    validaUtente(utente.getUtente());

    CosmoTUtente utenteDaSalvare;

    CosmoTUtente utenteSulDB =
        cosmoTUtenteRepository.findByCodiceFiscale(utente.getUtente().getCodiceFiscale());

    if (utenteSulDB != null) {
      utenteDaSalvare = utenteSulDB;
    } else {
      utenteDaSalvare = cosmoTUtenteMapper.toRecord(utente.getUtente());
      utenteDaSalvare.setCosmoRUtenteEntes(new ArrayList<>());
    }

    utenteDaSalvare = cosmoTUtenteRepository.save(utenteDaSalvare);

    Timestamp inizioValidita = Timestamp.valueOf(
        utente.getDtInizioValidita().atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());
    Timestamp fineValidita = utente.getDtFineValidita() == null ? null
        : Timestamp.valueOf(
            utente.getDtFineValidita().atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());

    salvaAggiornaUtenteEnte(utente.getUtente(), utenteDaSalvare, inizioValidita, fineValidita);

    salvaAggiornaProfili(utente.getUtente(), utenteDaSalvare, inizioValidita, fineValidita);

    logger.info(methodName, UTENTE_CON_ID + utenteDaSalvare.getId() + " creato");

    return cosmoTUtenteMapper.toDTO(utenteDaSalvare);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Utente updateUtente(UtenteCampiTecnici utente) {
    String methodName = "updateUtente";

    if (utente == null) {
      logger.error(methodName, ErrorMessages.U_UTENTE_NON_VALORIZZATO);
      throw new BadRequestException(ErrorMessages.U_UTENTE_NON_VALORIZZATO);
    }

    CosmoTUtente utenteDaAggiornare = cosmoTUtenteRepository.findOne(utente.getUtente().getId());
    if (utenteDaAggiornare == null) {
      logger.error(methodName,
          String.format(ErrorMessages.U_UTENTE_NON_TROVATO, utente.getUtente().getId()));
      throw new NotFoundException(
          String.format(ErrorMessages.U_UTENTE_NON_TROVATO, utente.getUtente().getId()));
    }

    utenteDaAggiornare.setNome(utente.getUtente().getNome());
    utenteDaAggiornare.setCognome(utente.getUtente().getCognome());

    utenteDaAggiornare = cosmoTUtenteRepository.save(utenteDaAggiornare);

    Timestamp inizioValidita = Timestamp.valueOf(
        utente.getDtInizioValidita().atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());
    Timestamp fineValidita = utente.getDtFineValidita() == null ? null
        : Timestamp.valueOf(
            utente.getDtFineValidita().atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());

    salvaAggiornaUtenteEnte(utente.getUtente(), utenteDaAggiornare, inizioValidita, fineValidita);

    salvaAggiornaProfili(utente.getUtente(), utenteDaAggiornare, inizioValidita, fineValidita);

    logger.info(methodName, UTENTE_CON_ID + utenteDaAggiornare.getId() + " aggiornato");

    return cosmoTUtenteMapper.toDTO(utenteDaAggiornare);
  }

  private void salvaAggiornaProfili(Utente utente, CosmoTUtente utenteSulDB,
      Timestamp inizioValidita, Timestamp fineValidita) {

    logger.info("salvaAggiornaProfili", "Salva/aggiorna profili associati all'utente");

    if (CollectionUtils.isEmpty(utenteSulDB.getCosmoRUtenteProfilos())) {
      utente.getProfili()
      .forEach(profilo -> salvaProfilo(profilo, utenteSulDB, inizioValidita, fineValidita));
      return;
    }

    var ente = cosmoTEnteRepository.findOne(SecurityUtils.getUtenteCorrente().getEnte().getId());
    if (ente == null) {
      throw new UnauthorizedException("Nessun ente corrente.");
    }

    List<CosmoRUtenteProfilo> profiliAssociatiAttualmente = utenteSulDB.getCosmoRUtenteProfilos()
        .stream().filter(p -> p.getCosmoTEnte() != null && p.getCosmoTEnte().getId().equals(ente.getId()))
        .collect(Collectors.toList());

    for (CosmoRUtenteProfilo profiloSuDB : profiliAssociatiAttualmente) {

      if (utente.getProfili().stream()
          .anyMatch(profilo -> profilo.getEnte().getId().equals(profiloSuDB.getCosmoTEnte().getId())
              && profilo.getProfilo().getId().equals(profiloSuDB.getCosmoTProfilo().getId())
              && utenteSulDB.getId().equals(profiloSuDB.getCosmoTUtente().getId()))) {

        profiloSuDB.setDtFineVal(fineValidita);
        cosmoRUtenteProfiloRepository.save(profiloSuDB);
      }

      if (utente.getProfili().stream().noneMatch(
          profilo -> profilo.getEnte().getId().equals(profiloSuDB.getCosmoTEnte().getId())
          && profilo.getProfilo().getId().equals(profiloSuDB.getCosmoTProfilo().getId())
          && utenteSulDB.getId().equals(profiloSuDB.getCosmoTUtente().getId()))) {

        profiloSuDB.setDtFineVal(Timestamp.valueOf(LocalDateTime.now()));
        cosmoRUtenteProfiloRepository.save(profiloSuDB);
      }
    }

    for (AssociazioneUtenteProfilo profilo : utente.getProfili()) {

      if (CollectionUtils.isEmpty(profiliAssociatiAttualmente)
          || profiliAssociatiAttualmente.stream().noneMatch(
              profiloSuDB -> profiloSuDB.getCosmoTEnte().getId().equals(profilo.getEnte().getId())
              && profiloSuDB.getCosmoTProfilo().getId().equals(profilo.getProfilo().getId())
              && profiloSuDB.getCosmoTUtente().getId().equals(utente.getId())
              && profiloSuDB.getDtFineVal() == null)) {

        salvaProfilo(profilo, utenteSulDB, inizioValidita, fineValidita);
      }
    }
  }

  private void salvaProfilo(AssociazioneUtenteProfilo profilo, CosmoTUtente utenteSuDB,
      Timestamp inizioValidita, Timestamp fineValidita) {
    CosmoTProfilo profiloSulDB = null;

    if (profilo.getProfilo().getId() != null) {
      profiloSulDB = cosmoTProfiloRepository.findOne(profilo.getProfilo().getId());
    }

    if (profiloSulDB == null) {
      throw new NotFoundException("Profilo non trovato");
    } else if (!Boolean.TRUE.equals(profiloSulDB.getAssegnabile())) {
      throw new ForbiddenException("Profilo " + profiloSulDB.getCodice() + " non assegnabile");
    }

    CosmoTEnte enteSulDB = null;

    if (profilo.getEnte() != null && profilo.getEnte().getId() != null) {
      enteSulDB = cosmoTEnteRepository.findOne(profilo.getEnte().getId());
    }

    if (enteSulDB == null) {
      throw new NotFoundException("Ente non trovato");
    }

    CosmoRUtenteProfilo utenteProfiloSuDB = cosmoRUtenteProfiloRepository
        .findByCosmoTUtenteAndCosmoTProfiloAndCosmoTEnte(utenteSuDB, profiloSulDB, enteSulDB);

    if (utenteProfiloSuDB == null) {
      utenteProfiloSuDB = new CosmoRUtenteProfilo();
      utenteProfiloSuDB.setCosmoTUtente(utenteSuDB);
      utenteProfiloSuDB.setCosmoTProfilo(profiloSulDB);
      utenteProfiloSuDB.setCosmoTEnte(enteSulDB);
    }

    utenteProfiloSuDB.setDtInizioVal(inizioValidita);
    utenteProfiloSuDB.setDtFineVal(fineValidita);

    cosmoRUtenteProfiloRepository.save(utenteProfiloSuDB);
  }

  private void salvaAggiornaUtenteEnte(
      Utente utente,
      CosmoTUtente utenteSulDB,
      Timestamp inizioValidita,
      Timestamp fineValidita
      ) {

    logger.info("salvaAggiornaUtenteEnte", "Salva/aggiorna relazione tra l'ente e l'utente");

    var ente = cosmoTEnteRepository.findOne(SecurityUtils.getUtenteCorrente().getEnte().getId());
    if (ente == null) {
      throw new UnauthorizedException("Nessun ente corrente.");
    }

    if (CollectionUtils.isEmpty(utente.getEnti())) {
      throw new UnprocessableEntityException("Dati di associazione per l'ente non presenti");
    }

    AssociazioneEnteUtente rEnteUtente = utente.getEnti().get(0);

    CosmoRUtenteEnte utenteEnteSulDb = utenteSulDB.getCosmoRUtenteEntes().stream()
        .filter(r -> r.getId().getIdEnte().equals(ente.getId()))
        .findFirst().orElse(null);

    boolean toInsert = false;

    if (utenteEnteSulDb == null) {
      utenteEnteSulDb = new CosmoRUtenteEnte();
      CosmoRUtenteEntePK pk = new CosmoRUtenteEntePK();
      pk.setIdEnte(ente.getId());
      pk.setIdUtente(utenteSulDB.getId());
      utenteEnteSulDb.setId(pk);
      utenteEnteSulDb.setCosmoTEnte(ente);
      utenteEnteSulDb.setCosmoTUtente(utenteSulDB);
      toInsert = true;
    }

    utenteEnteSulDb.setDtInizioVal(inizioValidita);
    utenteEnteSulDb.setDtFineVal(fineValidita);

    utenteEnteSulDb.setEmail(rEnteUtente.getEmail());
    utenteEnteSulDb.setTelefono(rEnteUtente.getTelefono());

    utenteEnteSulDb = cosmoRUtenteEnteRepository.save(utenteEnteSulDb);

    if (toInsert) {
      utenteSulDB.getCosmoRUtenteEntes().add(utenteEnteSulDb);
    }

    if (fineValidita != null) {
      List<CosmoRUtenteProfilo> utentiProfiliSuDB =
          cosmoRUtenteProfiloRepository.findByCosmoTUtenteAndCosmoTEnte(utenteSulDB, ente);
      utentiProfiliSuDB.stream().filter(val -> val.getDtFineVal() == null).forEach(elem -> {
        elem.setDtFineVal(fineValidita);
        cosmoRUtenteProfiloRepository.save(elem);
      });
    }
  }

  private void validaUtente(Utente utente) {
    String methodName = "validaUtente";
    if (utente == null) {
      logger.error(methodName, ErrorMessages.U_UTENTE_NON_VALORIZZATO);
      throw new BadRequestException(ErrorMessages.U_UTENTE_NON_VALORIZZATO);
    }
    if (StringUtils.isBlank(utente.getNome())) {
      logger.error(methodName, ErrorMessages.U_NOME_UTENTE_NON_VALORIZZATO);
      throw new BadRequestException(ErrorMessages.U_NOME_UTENTE_NON_VALORIZZATO);
    }
    if (StringUtils.isBlank(utente.getCognome())) {
      logger.error(methodName, ErrorMessages.U_COGNOME_UTENTE_NON_VALORIZZATO);
      throw new BadRequestException(ErrorMessages.U_COGNOME_UTENTE_NON_VALORIZZATO);
    }
    if (StringUtils.isBlank(utente.getCodiceFiscale())) {
      logger.error(methodName, ErrorMessages.U_CODICE_FISCALE_UTENTE_NON_VALORIZZATO);
      throw new BadRequestException(ErrorMessages.U_CODICE_FISCALE_UTENTE_NON_VALORIZZATO);
    }

  }



}

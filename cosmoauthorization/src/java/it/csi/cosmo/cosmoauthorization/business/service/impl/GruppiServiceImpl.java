/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoRGruppoTipoPratica;
import it.csi.cosmo.common.entities.CosmoRGruppoTipoPraticaPK;
import it.csi.cosmo.common.entities.CosmoRUtenteGruppoTag;
import it.csi.cosmo.common.entities.CosmoRUtenteGruppoTagPK;
import it.csi.cosmo.common.entities.CosmoRUtenteGruppoTag_;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTGruppo;
import it.csi.cosmo.common.entities.CosmoTGruppo_;
import it.csi.cosmo.common.entities.CosmoTTag;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.CosmoTUtenteGruppo;
import it.csi.cosmo.common.entities.CosmoTUtenteGruppo_;
import it.csi.cosmo.common.entities.CosmoTUtente_;
import it.csi.cosmo.common.entities.proto.CosmoREntity;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ComplexListComparator;
import it.csi.cosmo.common.util.ComplexListComparator.DifferentListsDifference;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmoauthorization.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoauthorization.business.service.GruppiService;
import it.csi.cosmo.cosmoauthorization.config.ErrorMessages;
import it.csi.cosmo.cosmoauthorization.config.ParametriApplicativo;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaGruppoRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaGruppoRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.FiltroRicercaGruppiDTO;
import it.csi.cosmo.cosmoauthorization.dto.rest.GruppiResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.Gruppo;
import it.csi.cosmo.cosmoauthorization.dto.rest.PageInfo;
import it.csi.cosmo.cosmoauthorization.dto.rest.RiferimentoGruppo;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoDTipoPraticaMapper;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoTGruppoMapper;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoTUtenteMapper;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoDTipoPraticaRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoRGruppoTipoPraticaRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoRUtenteGruppoTagRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTGruppoRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTTagRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTUtenteGruppoRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTUtenteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.specifications.CosmoTGruppoSpecifications;
import it.csi.cosmo.cosmoauthorization.security.SecurityUtils;
import it.csi.cosmo.cosmoauthorization.util.logger.LogCategory;
import it.csi.cosmo.cosmoauthorization.util.logger.LoggerFactory;


/**
 *
 */
@Service
@Transactional
public class GruppiServiceImpl implements GruppiService {

  private static final String CLASS_NAME = GruppiServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private CosmoTGruppoRepository cosmoTGruppoRepository;

  @Autowired
  private CosmoTEnteRepository cosmoTEnteRepository;

  @Autowired
  private CosmoTUtenteRepository cosmoTUtenteRepository;

  @Autowired
  private CosmoDTipoPraticaRepository cosmoDTipoPraticaRepository;

  @Autowired
  private CosmoTUtenteGruppoRepository cosmoTUtenteGruppoRepository;

  @Autowired
  private CosmoRGruppoTipoPraticaRepository cosmoRGruppoTipoPraticaRepository;

  @Autowired
  private CosmoTGruppoMapper cosmoTGruppoMapper;

  @Autowired
  private CosmoTUtenteMapper cosmoTUtenteMapper;

  @Autowired
  private CosmoDTipoPraticaMapper cosmoDTipoPraticaMapper;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoTTagRepository cosmoTTagRepository;

  @Autowired
  private CosmoRUtenteGruppoTagRepository cosmoRUtenteGruppoTagRepository;

  @Override
  @Transactional(readOnly = true)
  public Gruppo getGruppo(Long id) {
    ValidationUtils.require(id, "id gruppo");

    CosmoTGruppo gruppo = cosmoTGruppoRepository.findOneNotDeleted(id).orElseThrow(
        () -> new NotFoundException(String.format(ErrorMessages.G_GRUPPO_NON_TROVATO, id)));

    return map(gruppo);
  }

  @Override
  public Gruppo getGruppoPerCodice(String codice) {
    ValidationUtils.require(codice, "codice gruppo");

    CosmoTGruppo gruppo =
        cosmoTGruppoRepository.findOneNotDeletedByField(CosmoTGruppo_.codice, codice)
        .orElseThrow(() -> new NotFoundException(
            String.format(ErrorMessages.G_GRUPPO_CODICE_NON_TROVATO, codice)));

    return map(gruppo);
  }

  @Override
  @Transactional(readOnly = true)
  public GruppiResponse getGruppi(String filter) {

    GruppiResponse output = new GruppiResponse();

    GenericRicercaParametricaDTO<FiltroRicercaGruppiDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaGruppiDTO.class);

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(ParametriApplicativo.MAX_PAGE_SIZE).asInteger());

    Page<CosmoTGruppo> pageGruppi = null;

    pageGruppi = cosmoTGruppoRepository.findAllNotDeleted(CosmoTGruppoSpecifications
        .findByFilters(ricercaParametrica.getFilter(), ricercaParametrica.getSort()), paging);

    List<CosmoTGruppo> gruppiSuDB = pageGruppi.getContent();

    List<Gruppo> gruppi = new LinkedList<>();
    gruppiSuDB.forEach(gruppoSuDB -> gruppi.add(map(gruppoSuDB)));
    output.setGruppi(gruppi);

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      it.csi.cosmo.common.util.SearchUtils.filterFields(gruppi,
          Arrays.asList(ricercaParametrica.getFields().split(",")));
    }

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(pageGruppi.getNumber());
    pageInfo.setPageSize(pageGruppi.getSize());
    pageInfo.setTotalElements(Math.toIntExact(pageGruppi.getTotalElements()));
    pageInfo.setTotalPages(pageGruppi.getTotalPages());
    output.setPageInfo(pageInfo);
    return output;
  }

  @Override
  @Transactional(readOnly = true)
  public List<RiferimentoGruppo> getGruppiUtenteCorrente() {
    List<RiferimentoGruppo> output = new LinkedList<>();

    var codiceFiscale = SecurityUtils.getUtenteCorrente().getCodiceFiscale() != null
        ? SecurityUtils.getUtenteCorrente().getCodiceFiscale()
            : null;
    var idEnte = SecurityUtils.getUtenteCorrente().getEnte() != null
        ? SecurityUtils.getUtenteCorrente().getEnte().getId()
            : null;
    if (codiceFiscale == null || idEnte == null) {
      throw new BadRequestException("Utente non loggato");
    }

    List<CosmoTGruppo> gruppi = cosmoTGruppoRepository.findAllNotDeleted((root, cq, cb) -> {
      ListJoin<CosmoTGruppo, CosmoTUtenteGruppo> joinUtente =
          root.join(CosmoTGruppo_.associazioniUtenti, JoinType.LEFT);


      Predicate predicate = cb.and(cb.isNotNull(root.get(CosmoTGruppo_.ente)),
          cb.equal(root.get(CosmoTGruppo_.ente).get(CosmoTEnte_.id), idEnte),
          cb.isNotNull(joinUtente.get(CosmoTUtenteGruppo_.utente)),
          cb.equal(joinUtente.get(CosmoTUtenteGruppo_.utente).get(CosmoTUtente_.codiceFiscale),
              codiceFiscale),
          cb.isNull(joinUtente.get(CosmoTUtenteGruppo_.dtCancellazione)));

      return cq.where(predicate).distinct(true).getRestriction();

    });

    gruppi.forEach(gruppo -> output.add(cosmoTGruppoMapper.toDTOLight(gruppo)));
    return output;
  }


  @Override
  @Transactional(rollbackFor = Exception.class)
  public Gruppo createGruppo(CreaGruppoRequest body) {
    String methodName = "createGruppo";
    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    Long idEnte = SecurityUtils.getUtenteCorrente().getEnte().getId();

    body.setCodice(body.getCodice().trim());

    if (!cosmoTGruppoRepository.findAllNotDeleted((root, cq, cb) -> {
      //@formatter:off
      var joinEnte = (root.join(CosmoTGruppo_.ente));
      return cb.and(
          cb.isNotNull(joinEnte.get(CosmoTEnte_.id)),
          cb.equal(joinEnte.get(CosmoTEnte_.id), idEnte),
          cb.equal(root.get(CosmoTGruppo_.codice), body.getCodice())
          );
      //@formatter:on
    }).isEmpty()) {
      throw new ConflictException(
          String.format("Gruppo con codice '%s' gia' esistente", body.getCodice()));
    }

    CosmoTGruppo gruppoDaCreare = new CosmoTGruppo();
    gruppoDaCreare.setCodice(body.getCodice());
    gruppoDaCreare.setDescrizione(body.getDescrizione());
    gruppoDaCreare.setNome(body.getNome());
    gruppoDaCreare
    .setEnte(cosmoTEnteRepository.findOne(idEnte));
    gruppoDaCreare.setAssociazioniUtenti(new ArrayList<>());

    final CosmoTGruppo gruppoCreato = cosmoTGruppoRepository.save(gruppoDaCreare);

    // genera un codice
    if (body.getIdUtenti() != null && !body.getIdUtenti().isEmpty()) {
      gruppoCreato.setAssociazioniUtenti(body.getIdUtenti().stream().filter(Objects::nonNull)
          .map(id -> cosmoTUtenteGruppoRepository.save(buildAssociazione(gruppoCreato, id)))
          .collect(Collectors.toList()));
    }

    if (body.getCodiciTipologiePratiche() != null && !body.getCodiciTipologiePratiche().isEmpty()) {
      gruppoCreato.setCosmoRGruppoTipoPraticas(body.getCodiciTipologiePratiche().stream()
          .filter(Objects::nonNull)
          .map(codice -> cosmoRGruppoTipoPraticaRepository
              .save(buildGruppoPratica(gruppoCreato, codice)))
          .collect(Collectors.toList()));
    }
    logger.info(methodName, "Gruppo con id {} creato", gruppoCreato.getId());

    return map(gruppoCreato);
  }

  private CosmoTUtenteGruppo buildAssociazione(CosmoTGruppo gruppo, Long idUtente) {
    CosmoTUtenteGruppo assoc = new CosmoTUtenteGruppo();
    CosmoTUtente user = cosmoTUtenteRepository.findOne(idUtente);
    if (user == null) {
      throw new NotFoundException("Utente non trovato");
    }
    assoc.setUtente(user);
    assoc.setGruppo(gruppo);
    assoc.setIdGruppo(gruppo.getId());
    assoc.setIdUtente(user.getId());
    return assoc;
  }

  private CosmoRGruppoTipoPratica buildGruppoPratica(CosmoTGruppo gruppo,
      String codiceTipoPratica) {
    Timestamp now = Timestamp.from(Instant.now());
    CosmoRGruppoTipoPratica assoc = new CosmoRGruppoTipoPratica();
    CosmoDTipoPratica tipoPratica = cosmoDTipoPraticaRepository.findOne(codiceTipoPratica);
    if (tipoPratica == null) {
      throw new NotFoundException("Tipo pratica '" + codiceTipoPratica + "'non trovato");
    }
    assoc.setCosmoDTipoPratica(tipoPratica);
    assoc.setCosmoTGruppo(gruppo);
    assoc.setDtInizioVal(now);
    CosmoRGruppoTipoPraticaPK id = new CosmoRGruppoTipoPraticaPK();
    id.setIdGruppo(gruppo.getId());
    id.setCodiceTipoPratica(tipoPratica.getCodice());
    assoc.setId(id);
    return assoc;
  }

  private CosmoRUtenteGruppoTag buildUtenteGruppoTag(CosmoTGruppo gruppo, CosmoTUtente utente, Long idTag) {
    Timestamp now = Timestamp.from(Instant.now());
    CosmoRUtenteGruppoTag assoc = new CosmoRUtenteGruppoTag();
    CosmoTUtenteGruppo assUtenteGruppo =
        cosmoTUtenteGruppoRepository
        .findOneByGruppoAndUtenteAndDtCancellazioneNull(gruppo, utente);
    if (assUtenteGruppo == null) {
      throw new NotFoundException("Associazione gruppo/utente gruppo: '" + gruppo.toString() + "' e utente: '" + utente.toString() + "' non trovata");
    }

    CosmoTTag tag = cosmoTTagRepository.findOneNotDeleted(idTag)
        .orElseThrow(() -> new NotFoundException("Tag con id: '" + idTag + "' non trovato"));
    assoc.setCosmoTUtenteGruppo(assUtenteGruppo);
    assoc.setCosmoTTag(tag);
    assoc.setDtInizioVal(now);
    CosmoRUtenteGruppoTagPK id = new CosmoRUtenteGruppoTagPK();
    id.setIdTag(tag.getId());
    id.setIdUtenteGruppo(assUtenteGruppo.getId());
    return assoc;
  }

  @Override
  public void deleteGruppo(Long id) {
    String methodName = "deleteGruppo";
    ValidationUtils.require(id, "id");

    final CosmoTGruppo gruppoDaEliminare = cosmoTGruppoRepository.findOneNotDeleted(id).orElseThrow(
        () -> new NotFoundException(String.format(ErrorMessages.G_GRUPPO_NON_TROVATO, id)));



    gruppoDaEliminare.getCosmoRGruppoTipoPraticas().forEach(rGruppoTipoPratica ->
    cosmoRGruppoTipoPraticaRepository.deactivate(rGruppoTipoPratica));



    cosmoTGruppoRepository.deleteLogically(id, AuditServiceImpl.getPrincipalCode());

    logger.info(methodName, "Gruppo con id {} eliminato", gruppoDaEliminare.getId());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Gruppo updateGruppo(Long id, AggiornaGruppoRequest body) {
    String methodName = "updateGruppo";
    ValidationUtils.require(id, "id");
    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    Long idEnte = SecurityUtils.getUtenteCorrente().getEnte().getId();

    Timestamp now = Timestamp.from(Instant.now());

    body.setCodice(body.getCodice().trim());

    if (!cosmoTGruppoRepository.findAllNotDeleted((root, cq, cb) -> {
      //@formatter:off
      var joinEnte = (root.join(CosmoTGruppo_.ente));
      return cb.and(
          cb.notEqual(root.get(CosmoTGruppo_.id), id),
          cb.isNotNull(joinEnte.get(CosmoTEnte_.id)),
          cb.equal(joinEnte.get(CosmoTEnte_.id), idEnte),
          cb.equal(root.get(CosmoTGruppo_.codice), body.getCodice())
          );
      //@formatter:on
    }).isEmpty()) {
      throw new ConflictException(
          String.format("Gruppo con codice '%s' gia' esistente", body.getCodice()));
    }

    final CosmoTGruppo gruppoDaAggiornare =
        cosmoTGruppoRepository.findOneNotDeleted(id).orElseThrow(
            () -> new NotFoundException(String.format(ErrorMessages.G_GRUPPO_NON_TROVATO, id)));

    gruppoDaAggiornare.setDescrizione(body.getDescrizione());
    gruppoDaAggiornare.setNome(body.getNome());
    gruppoDaAggiornare.setCodice(body.getCodice());

    // compara associazioni utenti
    List<CosmoTUtenteGruppo> associazioniInput = body.getIdUtenti() != null ? body.getIdUtenti()
        .stream().filter(Objects::nonNull)
        .map(userId -> buildAssociazione(gruppoDaAggiornare, userId)).collect(Collectors.toList())
        : Collections.emptyList();

    DifferentListsDifference<CosmoTUtenteGruppo, CosmoTUtenteGruppo> differenzaAssociazioniUtenti =
        ComplexListComparator.compareLists(
            gruppoDaAggiornare.getAssociazioniUtenti().stream().filter(CosmoTEntity::nonCancellato)
            .collect(Collectors.toList()),
            associazioniInput, (existing, input) -> Objects.equals(existing.getIdUtente(),
                input.getIdUtente()));

    differenzaAssociazioniUtenti.onElementsInFirstNotInSecond(daEliminare -> {
      daEliminare.setDtCancellazione(now);
      cosmoTUtenteGruppoRepository.save(daEliminare);
      logger.info(methodName, "Utente {} eliminato dal gruppo {}",
          daEliminare.getIdUtente(), daEliminare.getIdGruppo());

      var listaAssociazioneUtenteGruppoTagDaEliminare = cosmoRUtenteGruppoTagRepository
          .findActiveByField(CosmoRUtenteGruppoTag_.cosmoTUtenteGruppo, daEliminare);

      listaAssociazioneUtenteGruppoTagDaEliminare.stream().forEach(ass -> {
        cosmoRUtenteGruppoTagRepository.deactivate(ass);
        logger.info(methodName, "Associazione utente/gruppo e tag con id {} eliminata",
            ass.getId());
      });

    });

    differenzaAssociazioniUtenti.onElementsInSecondNotInFirst(daInserire -> {
      daInserire.setDtInserimento(now);
      gruppoDaAggiornare.getAssociazioniUtenti().add(cosmoTUtenteGruppoRepository.save(daInserire));
      logger.info(methodName, "Utente {} aggiunto al gruppo {}", daInserire.getIdUtente(),
          daInserire.getIdGruppo());
    });

    // compara associazione tipi pratiche
    List<CosmoRGruppoTipoPratica> associazioniTipologiePratiche =
        body.getCodiciTipologiePratiche() != null ? body.getCodiciTipologiePratiche().stream()
            .filter(Objects::nonNull).map(codice -> buildGruppoPratica(gruppoDaAggiornare, codice))
            .collect(Collectors.toList()) : Collections.emptyList();

    DifferentListsDifference<CosmoRGruppoTipoPratica, CosmoRGruppoTipoPratica> differenzaAssociazioniTipologiePratiche =
        ComplexListComparator.compareLists(
            gruppoDaAggiornare.getCosmoRGruppoTipoPraticas().stream().filter(CosmoREntity::valido)
            .collect(Collectors.toList()),
            associazioniTipologiePratiche,
            (existing, input) -> Objects.equals(existing.getId().getCodiceTipoPratica(),
                input.getId().getCodiceTipoPratica()));

    differenzaAssociazioniTipologiePratiche.onElementsInFirstNotInSecond(daEliminare -> {
      daEliminare.setDtFineVal(now);
      cosmoRGruppoTipoPraticaRepository.save(daEliminare);
      logger.info(methodName, "Tipologia pratica {} eliminata dal gruppo {}",
          daEliminare.getId().getCodiceTipoPratica(), daEliminare.getId().getIdGruppo());
    });

    differenzaAssociazioniTipologiePratiche.onElementsInSecondNotInFirst(daInserire -> {
      daInserire.setDtInizioVal(now);
      gruppoDaAggiornare.getCosmoRGruppoTipoPraticas()
      .add(cosmoRGruppoTipoPraticaRepository.save(daInserire));
      logger.info(methodName, "Tipologia pratica {} aggiunta al gruppo {}",
          daInserire.getId().getCodiceTipoPratica(), daInserire.getId().getCodiceTipoPratica());
    });

    if (body.getUtenteTag() != null) {
      aggiornaAssociazioneGruppoUtentiTags(gruppoDaAggiornare, body);
    }

    CosmoTGruppo gruppoAggiornato = cosmoTGruppoRepository.save(gruppoDaAggiornare);

    logger.info(methodName, "Gruppo con id {} aggiornato", gruppoAggiornato.getId());
    return map(gruppoAggiornato);
  }

  private void aggiornaAssociazioneGruppoUtentiTags(CosmoTGruppo gruppoDaAggiornare, AggiornaGruppoRequest body) {
    String methodName = "aggiornaAssociazioneGruppoUtentiTags";
    Timestamp now = Timestamp.from(Instant.now());
    
    CosmoTUtente utente = cosmoTUtenteRepository.findOneNotDeleted(body.getUtenteTag())
        .orElseThrow(() -> new NotFoundException("utente con id: '" + body.getUtenteTag() + "' non trovato"));
    
    // compara associazione gruppo utente e tags
    CosmoTUtenteGruppo assUtenteGruppo = cosmoTUtenteGruppoRepository
        .findOneByGruppoAndUtenteAndDtCancellazioneNull(gruppoDaAggiornare,
            utente);
    if (assUtenteGruppo == null) {
      throw new NotFoundException("Associazione utente/gruppo: '" + gruppoDaAggiornare.toString()
      + "' e utente: '" + utente.toString() + "' non trovata");
    }

    List<CosmoRUtenteGruppoTag> associazioniGruppoUtenteTags =
        body.getIdTags() != null ? body.getIdTags().stream()
            .filter(Objects::nonNull).map(id -> buildUtenteGruppoTag(gruppoDaAggiornare,utente,id))
            .collect(Collectors.toList()) : Collections.emptyList();

    DifferentListsDifference<CosmoRUtenteGruppoTag, CosmoRUtenteGruppoTag> differenzaAssociazioniGruppoUtenteTags =
        ComplexListComparator.compareLists(
            assUtenteGruppo.getCosmoRUtenteGruppoTags().stream()
            .filter(
                CosmoREntity::valido)
            .collect(Collectors.toList()),
            associazioniGruppoUtenteTags, (existing, input) -> Objects
            .equals(existing.getCosmoTTag().getId(), input.getCosmoTTag().getId()));

    differenzaAssociazioniGruppoUtenteTags.onElementsInFirstNotInSecond(daEliminare -> {
      daEliminare.setDtFineVal(now);
      cosmoRUtenteGruppoTagRepository.save(daEliminare);
      logger.info(methodName, "Relazione utente/gruppo {} e tag {} eliminata",
          daEliminare.getCosmoTUtenteGruppo().getId(), daEliminare.getCosmoTTag().getId());
    });

    differenzaAssociazioniGruppoUtenteTags.onElementsInSecondNotInFirst(daInserire -> {
      CosmoRUtenteGruppoTagPK id = new CosmoRUtenteGruppoTagPK();
      id.setIdUtenteGruppo(daInserire.getCosmoTUtenteGruppo().getId());
      id.setIdTag(daInserire.getCosmoTTag().getId());
      daInserire.setId(id);
      daInserire.setDtInizioVal(now);
      assUtenteGruppo.getCosmoRUtenteGruppoTags()
      .add(cosmoRUtenteGruppoTagRepository.save(daInserire));
      logger.info(methodName, "TRelazione utente/gruppo {} e tag {} inserita correttamente",
          daInserire.getCosmoTUtenteGruppo().getId(), daInserire.getCosmoTTag().getId());
    });

  }

  private Gruppo map(CosmoTGruppo gruppo) {
    if (gruppo == null) {
      return null;
    }
    Gruppo mapped = cosmoTGruppoMapper.toDTO(gruppo);
    mapped.setUtenti(gruppo.getAssociazioniUtenti() == null ? Collections.emptyList()
        : gruppo.getAssociazioniUtenti().stream().filter(CosmoTEntity::nonCancellato)
        .map(r -> cosmoTUtenteMapper.toRiferimentoDTO(r.getUtente()))
        .collect(Collectors.toList()));

    mapped.setTipologiePratiche(gruppo.getCosmoRGruppoTipoPraticas() == null
        ? Collections.emptyList()
            : gruppo.getCosmoRGruppoTipoPraticas().stream().filter(CosmoREntity::valido)
            .map(r -> cosmoDTipoPraticaMapper.toDTO(r.getCosmoDTipoPratica()))
            .collect(Collectors.toList()));
    return mapped;
  }


}

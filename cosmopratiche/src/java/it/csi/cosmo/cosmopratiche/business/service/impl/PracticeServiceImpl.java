/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.service.impl;

import static it.csi.cosmo.cosmopratiche.config.ErrorMessages.PRATICA_NON_TROVATA;
import static it.csi.cosmo.cosmopratiche.config.ErrorMessages.PRATICA_NON_VALIDA;
import static it.csi.cosmo.cosmopratiche.config.ErrorMessages.TIPO_PRATICA_NON_CREABILE_DA_INTERFACCIA;
import static it.csi.cosmo.cosmopratiche.config.ErrorMessages.TIPO_PRATICA_NON_TROVATA;
import static it.csi.cosmo.cosmopratiche.util.Util.parseNumber;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.search.DateFilter;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.CosmoDStatoPratica;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoLStoricoPratica;
import it.csi.cosmo.common.entities.CosmoRPraticaTag;
import it.csi.cosmo.common.entities.CosmoRPraticaTagPK;
import it.csi.cosmo.common.entities.CosmoRStatoTipoPratica;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTAttivita_;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTFruitore_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.CosmoTTag;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.enums.TipoEventoStoricoPratica;
import it.csi.cosmo.common.entities.proto.CosmoREntity;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.feignclient.exception.FeignClientNotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.EnteDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.ComplexListComparator;
import it.csi.cosmo.common.util.ComplexListComparator.DifferentListsDifference;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmopratiche.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmopratiche.business.service.PracticeService;
import it.csi.cosmo.cosmopratiche.business.service.StoricoPraticaService;
import it.csi.cosmo.cosmopratiche.business.service.UserService;
import it.csi.cosmo.cosmopratiche.config.Constants;
import it.csi.cosmo.cosmopratiche.config.ErrorMessages;
import it.csi.cosmo.cosmopratiche.config.ParametriApplicativo;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.FiltroRicercaPraticheDTO;
import it.csi.cosmo.cosmopratiche.dto.rest.PageInfo;
import it.csi.cosmo.cosmopratiche.dto.rest.PaginaTask;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheNoLinkResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.RiassuntoStatoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.StatoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.Task;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.VariabileProcessoDTO;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoTAttivitaMapper;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoTPraticheMapper;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDStatoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDTipoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRAttivitaAssegnazioneRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRPraticaTagRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRPraticaUtenteGruppoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRStatoTipoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTAttivitaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTFruitoreRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTTagRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTUtenteRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.specifications.CosmoTPraticaSpecifications;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmopratiche.security.SecurityUtils;
import it.csi.cosmo.cosmopratiche.util.logger.LogCategory;
import it.csi.cosmo.cosmopratiche.util.logger.LoggerFactory;

@Service
@Transactional
public class PracticeServiceImpl implements PracticeService {

  private static final String CLASS_NAME = PracticeServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.COSMOPRATICHE_BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private CosmoTPraticaRepository cosmoPraticaRepository;

  @Autowired
  private CosmoTUtenteRepository cosmoUtenteRepository;

  @Autowired
  private CosmoDTipoPraticaRepository cosmoTipoPraticaRepository;

  @Autowired
  private CosmoTAttivitaRepository cosmoTAttivitaRepository;

  @Autowired
  private CosmoRAttivitaAssegnazioneRepository cosmoRAttivitaAssegnazioneRepository;

  @Autowired
  private CosmoRPraticaUtenteGruppoRepository cosmoRPraticaUtenteGruppoRepository;

  @Autowired
  private CosmoTPraticheMapper mapper;

  @Autowired
  private CosmoTAttivitaMapper attivitaMapper;

  @Autowired
  private CosmoRStatoTipoPraticaRepository statoTipoPraticaRepository;

  @Autowired
  private CosmoDStatoPraticaRepository statoPraticaRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private CosmoTPraticheMapper cosmoTPraticheMapper;

  @Autowired
  private CosmoTEnteRepository cosmoTEnteRepository;

  @Autowired
  private StoricoPraticaService storicoPraticaService;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoCmmnFeignClient cosmoCmmnFeignClient;

  @Autowired
  private CosmoTFruitoreRepository cosmoTFruitoreRepository;

  @Autowired
  private CosmoTTagRepository cosmoTTagRepository;

  @Autowired
  private CosmoRPraticaTagRepository cosmoRPraticaTagRepository;

  Map<String, Predicate<Pratica>> validazioneModifica = new LinkedHashMap<>();

  public PracticeServiceImpl() {
    validazioneModifica.put(PRATICA_NON_VALIDA, Objects::isNull);
  }

  @Override
  @Transactional
  public Pratica postPractices(CreaPraticaRequest body) {

    ValidationUtils.require(body, "request body");
    ValidationUtils.validaAnnotations(body);

    Timestamp adesso = new Timestamp(System.currentTimeMillis());

    // verifico il codice tipo pratica e ottengo la reference
    CosmoDTipoPratica tipo = cosmoTipoPraticaRepository.findOneActive(body.getCodiceTipologia())
        .orElseThrow(() -> new BadRequestException(
            String.format(TIPO_PRATICA_NON_TROVATA, body.getCodiceTipologia())));

    if (Boolean.FALSE.equals(tipo.getCreabileDaInterfaccia())) {
      throw new BadRequestException(
          String.format(TIPO_PRATICA_NON_CREABILE_DA_INTERFACCIA, body.getCodiceTipologia()));
    }

    CosmoTEnte ente = Optional
        .ofNullable(cosmoTEnteRepository
            .findOneNotDeletedByField(CosmoTEnte_.codiceIpa, body.getCodiceIpaEnte()).orElse(null))
        .filter(CosmoTEntity::nonCancellato)
        .orElseThrow(() -> new BadRequestException("Ente non trovato"));

    if (tipo.getCosmoTEnte() != null && !ente.getId().equals(tipo.getCosmoTEnte().getId())) {
      throw new BadRequestException("Il tipo di pratica non e' disponibile per l'ente");
    }

    CosmoTPratica pratica = new CosmoTPratica();

    pratica.setEnte(ente);
    pratica.setFruitore(null);
    pratica.setOggetto(body.getOggetto());
    pratica.setRiassunto(body.getRiassunto());
    pratica.setUtenteCreazionePratica(body.getUtenteCreazionePratica());
    pratica.setTipo(tipo);
    pratica.setDataCreazionePratica(adesso);

    if (!StringUtils.isBlank(body.getRiassunto())) {
      // tenta indicizzazione del campo riassunto come best-attempt
      try {
        pratica.setRiassuntoTestuale(Jsoup.parse(body.getRiassunto()).text());
      } catch (Exception e) {
        logger.error("postPractices",
            "errore nell'indicizzazione del campo riassunto a versione testuale", e);
      }
    }

    if (!StringUtils.isBlank(body.getCodiceFruitore())) {
      var fruitore = cosmoTFruitoreRepository
          .findOneNotDeletedByField(CosmoTFruitore_.apiManagerId, body.getCodiceFruitore()).stream()
          .filter(f -> f.getCosmoRFruitoreEntes().stream()
              .anyMatch(r -> r.valido() && r.getId().getIdEnte().equals(ente.getId())
                  && r.getCosmoTEnte().nonCancellato()))
          .findFirst().orElseThrow(
              () -> new NotFoundException("Fruitore non trovato o non associato all'ente"));

      pratica.setFruitore(fruitore);
    }

    pratica.setIdPraticaExt(UUID.randomUUID().toString());

    pratica = cosmoPraticaRepository.save(pratica);

    return mapper.toPractice(pratica, null, SecurityUtils.getUtenteCorrente());
  }

  private void validateBody(Map<String, java.util.function.Predicate<Pratica>> m, Pratica body) {
    m.keySet().stream().filter(e -> m.get(e).test(body)).findAny().ifPresent(e -> {
      throw new BadRequestException(e);
    });
  }

  @Override
  @Transactional
  public Void deletePracticesId(String id) {
    long parsedId = parseNumber(id, -1, (e -> e != -1), PRATICA_NON_VALIDA);

    CosmoTPratica pratica = cosmoPraticaRepository.findByIdAndDtCancellazioneIsNull(parsedId);
    if (pratica == null) {
      throw new NotFoundException(String.format(PRATICA_NON_TROVATA, id));
    }

    Timestamp adesso = new Timestamp(System.currentTimeMillis());
    String cf = userService.getUtenteCorrente().getCodiceFiscale();
    pratica.setDtCancellazione(adesso);
    pratica.setUtenteCancellazione(cf);

    // Recupero Attivita e aggiorno la data di cancellazione
    pratica.getAttivita().forEach(t -> {
      t.setDtCancellazione(adesso);
      t.setUtenteCancellazione(cf);
      cosmoTAttivitaRepository.save(t);

      // Recupero la relazione attivita e assegnazioni e aggiorno la data di fine validita'
      t.getCosmoRAttivitaAssegnaziones().forEach(r -> {
        r.setDtFineVal(adesso);
        cosmoRAttivitaAssegnazioneRepository.save(r);
      });
    });

    pratica.getAssociazioneUtentiGruppi().forEach(r -> {
      r.setDtFineVal(adesso);
      cosmoRPraticaUtenteGruppoRepository.save(r);
    });

    cosmoPraticaRepository.save(pratica);

    // loggo l'operazione su db
    //@formatter:off
    storicoPraticaService.logEvent(CosmoLStoricoPratica.builder()
        .withCodiceTipoEvento(TipoEventoStoricoPratica.PRATICA_ANNULLATA)
        .withDescrizioneEvento(String.format("La pratica '%s' e' stata annullata.", pratica.getOggetto()))
        .withPratica(pratica)
        .build());
    //@formatter:on

    return null;
  }

  @Override
  public Pratica getPracticesId(String id, Boolean annullata) {
    long parsedId = parseNumber(id, -1, (e -> e > 0), PRATICA_NON_VALIDA);

    CosmoTPratica pratica;

    if (annullata != null && annullata) {
      pratica = cosmoPraticaRepository.findOne(parsedId);
    } else {
      pratica = cosmoPraticaRepository.findByIdAndDtCancellazioneIsNull(parsedId);
    }

    if (pratica == null) {
      throw new NotFoundException(String.format(PRATICA_NON_TROVATA, id));
    }

    return mapper.toPractice(pratica, null, SecurityUtils.getUtenteCorrente());
  }

  @Override
  @Transactional
  public Pratica putPracticesId(String id, Pratica body, Boolean aggiornaTask) {
    long parsedId = parseNumber(id, -1, (e -> e != -1), PRATICA_NON_VALIDA);
    validateBody(validazioneModifica, body);
    Timestamp adesso = new Timestamp(System.currentTimeMillis());
    String cf = userService.getUtenteCorrente().getCodiceFiscale();

    CosmoTPratica pratica =
        cosmoPraticaRepository.findByIdAndDtCancellazioneIsNull(parsedId);
    if (pratica == null) {
      throw new NotFoundException(String.format(PRATICA_NON_TROVATA, id));
    }

    pratica.setDtUltimaModifica(adesso);
    pratica.setUtenteUltimaModifica(cf);
    pratica.setOggetto(body.getOggetto());
    pratica.setUuidNodo(body.getUuidNodo());
    pratica.setMetadati(body.getMetadati());

    List<CosmoTAttivita> att = null;

    if (aggiornaTask != null && aggiornaTask && !body.getAttivita().isEmpty()) {
      cosmoTAttivitaRepository
      .delete(cosmoTAttivitaRepository.findBycosmoTPraticaId(Long.parseLong(id)));
      body.setId(Integer.parseInt("" + pratica.getId()));

      List<CosmoTAttivita> attivita = mapper.toCosmoTPratica(body).getAttivita();
      attivita.forEach(a -> a.setCosmoTPratica(pratica));
      att = cosmoTAttivitaRepository.save(attivita);
    }

    aggiornaRelazionePraticaTags(pratica, body);

    CosmoTPratica savedCp = cosmoPraticaRepository.save(pratica);
    savedCp.setAttivita(att);



    return mapper.toPractice(savedCp, null, SecurityUtils.getUtenteCorrente());
  }

  @Override
  public RiassuntoStatoPratica getPraticheStatoIdPraticaExt(String idPraticaExt) {
    CosmoTPratica pratica =
        cosmoPraticaRepository.findByIdPraticaExtAndDtCancellazioneIsNull(idPraticaExt);
    if (pratica == null) {
      throw new NotFoundException(String.format(PRATICA_NON_TROVATA, idPraticaExt));
    }

    Pratica res = mapper.toPractice(pratica, null, SecurityUtils.getUtenteCorrente());

    // res.setPreferita(false);

    RiassuntoStatoPratica output = new RiassuntoStatoPratica();
    output.setPratica(res);
    return output;
  }

  @Override
  public PaginaTask getPraticheTaskIdTaskSubtasks(String idTask) {

    // trovare l'attivita tramite task id
    Optional<CosmoTAttivita> attivita = cosmoTAttivitaRepository
        .findOneNotDeletedByField(CosmoTAttivita_.linkAttivita, "tasks/" + idTask);

    if (!attivita.isPresent()) {
      throw new NotFoundException();
    }

    CosmoTAttivita att = attivita.get();

    List<CosmoTAttivita> subtasks =
        cosmoTAttivitaRepository.findByField(CosmoTAttivita_.parent, att);

    List<Task> subs = subtasks.stream().map(t -> {
      CosmoTUtente assegnatario = t.getCosmoRAttivitaAssegnaziones().stream()
          .filter(assegnazione -> Boolean.TRUE.equals(assegnazione.getAssegnatario())).findFirst()
          .map(assegnazione -> cosmoUtenteRepository.getOne(assegnazione.getIdUtente().longValue()))
          .orElse(null);

      return attivitaMapper.toTask(t, assegnatario);
    }).collect(Collectors.toList());

    PaginaTask res = new PaginaTask();
    res.setElementi(subs);
    res.setDimensionePagina(BigDecimal.valueOf(subs.size()));
    res.setInizio(BigDecimal.ZERO);
    res.setTotale(BigDecimal.valueOf(subs.size()));
    return res;
  }

  @Override
  public List<TipoPratica> getTipiPratica(Integer idEnte) {

    Long id;

    if (null == idEnte || idEnte == 0) {
      EnteDTO ente = SecurityUtils.getUtenteCorrente().getEnte();
      id = null != ente ? ente.getId() : null;
    } else {
      CosmoTEnte ente = cosmoTEnteRepository.findOne(idEnte.longValue());
      id = null != ente ? ente.getId().longValue() : null;
    }

    return cosmoTipoPraticaRepository.findAllActive().stream()
        .filter(tipoPratica -> id == null || tipoPratica.getCosmoTEnte().getId().equals(id))
        .map(t -> {
          TipoPratica tp = new TipoPratica();
          tp.setCodice(t.getCodice());
          tp.setDescrizione(t.getDescrizione());
          tp.setProcessDefinitionKey(t.getProcessDefinitionKey());
          return tp;
        }).collect(Collectors.toList());
  }


  @Override
  public List<StatoPratica> getStatiPerTipo(String tipoPratica) {

    List<StatoPratica> output = new LinkedList<>();

    if (!StringUtils.isBlank(tipoPratica)) {
      List<CosmoRStatoTipoPratica> cc =
          statoTipoPraticaRepository.findByCosmoDTipoPraticaCodice(tipoPratica).stream()
          .filter(CosmoREntity::valido).collect(Collectors.toList());

      for (CosmoRStatoTipoPratica record : cc) {
        if (record.getCosmoDStatoPratica() == null || !record.getCosmoDStatoPratica().valido()) {
          continue;
        }
        StatoPratica dto = new StatoPratica();
        dto.setCodice(record.getCosmoDStatoPratica().getCodice());
        dto.setDescrizione(record.getCosmoDStatoPratica().getDescrizione());
        dto.setClasse(record.getCosmoDStatoPratica().getClasse());
        output.add(dto);
      }
    } else {
      List<CosmoDStatoPratica> cc = statoPraticaRepository.findAllActive();

      for (CosmoDStatoPratica record : cc) {
        StatoPratica dto = new StatoPratica();
        dto.setCodice(record.getCodice());
        dto.setDescrizione(record.getDescrizione());
        dto.setClasse(record.getClasse());
        output.add(dto);
      }
    }

    output.sort(
        (StatoPratica o1, StatoPratica o2) -> o1.getDescrizione().compareTo(o2.getDescrizione()));

    return output;
  }

  private void searchFieldsValidation(FiltroRicercaPraticheDTO filters) { // NOSONAR
    if (filters != null) {
      /*
       * Oggetto deve essere alfanumerico e non deve superare la dimensione massima di
       * cosmo_t_pratica.oggetto
       */
      if (filters.getOggetto() != null
          && !StringUtils.isEmpty(filters.getOggetto().getContainsIgnoreCase())) {

        if (filters.getOggetto().getContainsIgnoreCase()
            .length() > Constants.PRATICHE.MAX_LENGTH_OGGETTO) {
          throw new BadRequestException(ErrorMessages.DIMENSIONE_MASSIMA_CAMPO_OGGETTO_SUPERATA);
        }
        if (!filters.getOggetto().getContainsIgnoreCase()
            .matches(Constants.PRATICHE.OGGETTO_PRINTABLE_CHARACTERS_REGEX)) {
          throw new BadRequestException(ErrorMessages.VALORE_OGGETTO_CON_CARATTERI_NON_VALIDI);
        }
      }

      /*
       * Da/A Ultima modifica, Da/A Apertura pratica e Da/A Ultimo cambio di stato devono essere
       * indicate in formato dd/mm/aaaa - controllo implicito nella deserializzazione tipizzata
       */

      /*
       * Se Da/A Apertura pratica sono entrambe valorizzate, controllare che A Apertura pratica sia
       * uguale o successiva a Da Apertura pratica, in caso contrario mostrare un messaggio di
       * errore.
       */
      if (!checkDateFilterSequence(filters.getDataAperturaPraticaDa(),
          filters.getDataAperturaPraticaA())
          || !checkDateFilterSequence(filters.getDataAperturaPratica(),
              filters.getDataAperturaPratica())) {
        throw new BadRequestException(
            ErrorMessages.DATA_APERTURA_PRATICA_DA_MINORE_DATA_APERTURA_PRATICA_A);
      }
      /*
       * Se Da/A Ultima modifica sono entrambe valorizzate, controllare che A Ultima modifica sia
       * uguale o successiva a Da Ultima modifica, in caso contrario mostrare un messaggio di
       * errore.
       */
      if (!checkDateFilterSequence(filters.getDataUltimaModificaDa(),
          filters.getDataUltimaModificaA())
          || !checkDateFilterSequence(filters.getDataUltimaModifica(),
              filters.getDataUltimaModifica())) {
        throw new BadRequestException(
            ErrorMessages.DATA_ULTIMA_MODIFICA_DA_MINORE_DATA_ULTIMA_MODIFICA_A);
      }

      /*
       * Se Da/A Ultimo cambio di stato sono entrambe valorizzate, controllare che A Ultimo cambio
       * di stato sia uguale o successiva a Da Ultimo cambio di stato, in caso contrario mostrare un
       * messaggio di errore.
       */
      if (!checkDateFilterSequence(filters.getDataUltimoCambioStatoDa(),
          filters.getDataUltimoCambioStatoA())
          || !checkDateFilterSequence(filters.getDataUltimoCambioStato(),
              filters.getDataUltimoCambioStato())) {
        throw new BadRequestException(
            ErrorMessages.DATA_ULTIMO_CAMBIO_STATO_DA_MINORE_DATA_ULTIMO_CAMBIO_STATO_A);
      }


      if (filters.getTipologia() != null && filters.getVariabiliProcesso() != null) {
        Optional<VariabileProcessoDTO> variabileProcessoEmptyName = filters.getVariabiliProcesso()
            .stream()
            .filter(temp -> temp.getNomeVariabile() == null || temp.getNomeVariabile().isEmpty())
            .findFirst();
        if (variabileProcessoEmptyName.isPresent())
          throw new BadRequestException(ErrorMessages.NOME_VARIABILE_PROCESSO_OBBLIGATORIA);

        Optional<VariabileProcessoDTO> variabileProcessoEmptyFilter =
            filters.getVariabiliProcesso().stream().filter(
                temp -> (temp.getSingolo() == null || (temp.getSingolo()
                    .getVariabileBoolean() == null
                    && temp.getSingolo().getVariabileData() == null
                    && temp.getSingolo().getVariabileNumerica() == null
                    && temp.getSingolo().getVariabileString() == null))
                && (temp.getRangeDa() == null || (temp.getRangeDa().getVariabileData() == null
                && temp.getRangeDa().getVariabileNumerica() == null))
                && (temp.getRangeA() == null || (temp.getRangeA().getVariabileData() == null
                && temp.getRangeA().getVariabileNumerica() == null)))
            .findFirst();
        if (variabileProcessoEmptyFilter.isPresent())
          throw new BadRequestException(ErrorMessages.FILTRO_VARIABILE_PROCESSO_OBBLIGATORIA);

        filters.getVariabiliProcesso().stream().filter(temp ->
        temp.getRangeDa() != null && temp.getRangeDa().getVariabileData()!=null
        && temp.getRangeA() != null && temp.getRangeA().getVariabileData() != null)
        .forEach(temp -> {
          if (!checkDateFilterSequence(temp.getRangeDa().getVariabileData(),
              temp.getRangeA().getVariabileData()))
            throw new BadRequestException(
                ErrorMessages.VARIABILE_PROCESSO_DATA_RANGE_DA_MINORE_DATA_RANGE_A);
        });



      }
    }
  }

  private boolean checkDateFilterSequence(DateFilter from, DateFilter to) {
    if (from == null || to == null) {
      return true;
    }

    if (from.getGreaterThanOrEqualTo() != null && to.getLessThanOrEqualTo() != null
        && to.getLessThanOrEqualTo().isBefore(from.getGreaterThanOrEqualTo())) {
      return false;
    }

    if (from.getGreaterThan() != null && to.getLessThanOrEqualTo() != null
        && to.getLessThanOrEqualTo().isBefore(from.getGreaterThan())) {
      return false;
    }

    if (from.getGreaterThanOrEqualTo() != null && to.getLessThan() != null
        && to.getLessThan().isBefore(from.getGreaterThanOrEqualTo())) {
      return false;
    }

    if (from.getGreaterThan() != null && to.getLessThan() != null
        && to.getLessThan().isBefore(from.getGreaterThan())) {
      return false;
    }

    return true;
  }

  @Transactional(readOnly = true)
  @Override
  public PraticheResponse getPratiche(String filter, Boolean export) {

    PraticheResponse output = new PraticheResponse();

    GenericRicercaParametricaDTO<FiltroRicercaPraticheDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaPraticheDTO.class);

    searchFieldsValidation(ricercaParametrica.getFilter());

    var size = Boolean.TRUE.equals(export) ? ParametriApplicativo.EXPORT_ROW_MAX_SIZE
        : ParametriApplicativo.MAX_PAGE_SIZE;

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(size).asInteger());

    UserInfoDTO userInfo = SecurityUtils.getUtenteCorrente();

    Page<CosmoTPratica> pagePratiche = cosmoPraticaRepository
        .findAll(CosmoTPraticaSpecifications.findByFilters(ricercaParametrica.getFilter(), userInfo,
            userInfo.getEnte() != null ? userInfo.getEnte().getId() : null,
                ricercaParametrica.getSort()), paging);
    //@formatter:off
    output.setPratiche(pagePratiche.getContent().stream()
        .map(x -> mapRisultatoRicerca(x, ricercaParametrica.getFilter(),export))
        .collect(Collectors.toCollection(LinkedList::new)));
    //@formatter:on

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      SearchUtils.filterFields(output.getPratiche(),
          Arrays.asList(ricercaParametrica.getFields().split(",")));
    }

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(pagePratiche.getNumber());
    pageInfo.setPageSize(pagePratiche.getSize());
    pageInfo.setTotalElements(Math.toIntExact(pagePratiche.getTotalElements()));
    pageInfo.setTotalPages(pagePratiche.getTotalPages());
    output.setPageInfo(pageInfo);

    return output;
  }

  private Pratica mapRisultatoRicerca(CosmoTPratica praticaDB,
      FiltroRicercaPraticheDTO filtroRicercaPraticheDTO, Boolean export) {
    if (praticaDB == null) {
      return null;
    }

    if (Boolean.TRUE.equals(export)) {

      return cosmoTPraticheMapper.toPracticeLight(praticaDB, filtroRicercaPraticheDTO);
    } else {

      return cosmoTPraticheMapper.toPractice(praticaDB, filtroRicercaPraticheDTO,
          SecurityUtils.getUtenteCorrente());
    }
  }

  @Override
  public Pratica getVisibilitaPraticaById(Long idPratica) {
    UserInfoDTO userInfo = SecurityUtils.getUtenteCorrente();
    CosmoTEnte ente = cosmoTEnteRepository.findOne(userInfo.getEnte().getId());
    CosmoTPratica pratica = cosmoPraticaRepository.findOne(idPratica);

    if (pratica == null) {
      throw new NotFoundException(String.format(PRATICA_NON_TROVATA, idPratica));
    }

    CosmoTPratica praticaByCondizioniVisibilita =
        cosmoPraticaRepository.findOne(CosmoTPraticaSpecifications
            .findByCondizioniDiVisibilita(idPratica, ente.getId(), userInfo));

    if (praticaByCondizioniVisibilita == null) {
      throw new NotFoundException(
          String.format(ErrorMessages.ACCESSO_PRATICA_NON_CONSENTITO, idPratica));
    }

    return mapper.toPractice(praticaByCondizioniVisibilita, null,
        SecurityUtils.getUtenteCorrente());
  }

  @Override
  public Pratica getVisibilitaPraticaByIdTask(String idTask) {
    UserInfoDTO userInfo = SecurityUtils.getUtenteCorrente();
    CosmoTEnte ente = cosmoTEnteRepository.findOne(userInfo.getEnte().getId());

    CosmoTAttivita attivita = cosmoTAttivitaRepository
        .findOneByField(CosmoTAttivita_.linkAttivita, "tasks/" + idTask.trim())
        .orElseThrow(NotFoundException::new);

    CosmoTPratica praticaByCondizioniVisibilitaTask =
        cosmoPraticaRepository.findOne(CosmoTPraticaSpecifications
            .findByCondizioniDiVisibilitaSuAttivita(attivita.getId(), ente.getId(), userInfo));

    if (praticaByCondizioniVisibilitaTask == null) {
      throw new NotFoundException(String.format(ErrorMessages.ACCESSO_ATTIVITA_NON_CONSENTITO,
          attivita.getId().toString()));
    }
    return mapper.toPractice(praticaByCondizioniVisibilitaTask, null,
        SecurityUtils.getUtenteCorrente());
  }

  @Override
  public byte[] getPraticheIdPraticaDiagramma(Integer idPratica) {
    ValidationUtils.require(idPratica, "idPratica");

    var pratica = cosmoPraticaRepository.findOne(Long.valueOf(idPratica));
    if (pratica == null) {
      throw new NotFoundException("Pratica non trovata");
    }

    var processInstanceId = pratica.getProcessInstanceId();
    if (StringUtils.isEmpty(processInstanceId)) {
      throw new ConflictException("Nessun processo attivo associato alla pratica");
    }

    ProcessInstanceResponse processInstance;
    boolean ended = false;

    try {
      processInstance = cosmoCmmnFeignClient.getProcessInstanceId(processInstanceId);
    } catch (FeignClientNotFoundException e) {
      processInstance = null;
    }

    if (processInstance == null) {
      try {
        processInstance = cosmoCmmnFeignClient.getHistoricProcessInstanceId(processInstanceId);
        ended = true;
      } catch (FeignClientNotFoundException e) {
        processInstance = null;
      }
    }

    if (processInstance == null) {
      throw new NotFoundException("Processo non trovato");
    }

    byte[] result;
    if (ended || processInstance.isEnded()) {
      result = cosmoCmmnFeignClient.getHistoricProcessInstanceDiagram(processInstanceId);
    } else {
      result = cosmoCmmnFeignClient.getProcessInstanceDiagram(processInstanceId);
    }

    return result;
  }

  private void aggiornaRelazionePraticaTags(CosmoTPratica praticaDaAggiornare, Pratica body) {
    String methodName = "aggiornaRelazionePraticaTags";
    Timestamp now = Timestamp.from(Instant.now());
    // compara associazione gruppo utente e tags
    List<CosmoRPraticaTag> relPraticaTags =
        body.getTags() != null ? body.getTags().stream()
            .filter(Objects::nonNull).map(id -> buildPraticaTag(praticaDaAggiornare,
                id))
            .collect(Collectors.toList()) : Collections.emptyList();

    DifferentListsDifference<CosmoRPraticaTag, CosmoRPraticaTag> differenzaAssociazioniPraticaTags =
        ComplexListComparator.compareLists(
            praticaDaAggiornare.getCosmoRPraticaTags().stream()
            .filter(
                CosmoREntity::valido)
            .collect(Collectors.toList()),
            relPraticaTags, (existing, input) -> Objects
            .equals(existing.getCosmoTTag().getId(), input.getCosmoTTag().getId()));

    differenzaAssociazioniPraticaTags.onElementsInFirstNotInSecond(daEliminare -> {
      daEliminare.setDtFineVal(now);
      cosmoRPraticaTagRepository.save(daEliminare);
      logger.info(methodName, "Relazione pratica {} e tag {} eliminata",
          daEliminare.getCosmoTPratica().getId(), daEliminare.getCosmoTTag().getId());
    });

    differenzaAssociazioniPraticaTags.onElementsInSecondNotInFirst(daInserire -> {
      CosmoRPraticaTagPK id = new CosmoRPraticaTagPK();
      id.setIdPratica(daInserire.getCosmoTPratica().getId());
      id.setIdTag(daInserire.getCosmoTTag().getId());
      daInserire.setId(id);
      daInserire.setDtInizioVal(now);
      praticaDaAggiornare.getCosmoRPraticaTags()
      .add(cosmoRPraticaTagRepository.save(daInserire));
      logger.info(methodName, "Relazione pratica {} e tag {} inserita correttamente",
          daInserire.getCosmoTPratica().getId(), daInserire.getCosmoTTag().getId());
    });

  }

  private CosmoRPraticaTag buildPraticaTag(CosmoTPratica pratica, Long idTag) {
    Timestamp now = Timestamp.from(Instant.now());
    CosmoRPraticaTag assoc = new CosmoRPraticaTag();
    CosmoTTag tag =
        cosmoTTagRepository.findOneNotDeleted(idTag)
        .orElseThrow(() -> new NotFoundException("Tag con id: '" + idTag + "' non trovato"));
    assoc.setCosmoTPratica(pratica);
    assoc.setCosmoTTag(tag);
    assoc.setDtInizioVal(now);
    CosmoRPraticaTagPK id = new CosmoRPraticaTagPK();
    id.setIdTag(tag.getId());
    id.setIdPratica(pratica.getId());
    return assoc;
  }

  @Override
  public PraticheNoLinkResponse getPraticheNoLink() {
    List<Pratica> listaPratiche = new ArrayList<>();
    PraticheNoLinkResponse output = new PraticheNoLinkResponse();

    var settimane =
        configurazioneService.requireConfig(ParametriApplicativo.NUMERO_SETTIMANE).getValue();

    if (settimane == null || !StringUtils.isNumeric(StringUtils.trim(settimane))) {
      String error = "Configurazione delle settimane errata";
      logger.error("getPraticheNoLink", error);
      throw new InternalServerException(error);
    }

    List<CosmoTPratica> result =
        cosmoPraticaRepository.findAllNotDeleted(
            (root, cq, cb) -> cb.and(root.get(CosmoTPratica_.linkPratica).isNull(),
                cb.greaterThanOrEqualTo(root.get(CosmoTEntity_.dtInserimento),
                    Timestamp.valueOf(
                        LocalDateTime.now()
                            .minusWeeks(Long.valueOf(StringUtils.trim(settimane)))))));

    result.forEach(pratica -> listaPratiche.add(mapper.toPracticeLight(pratica, null)));
    output.setPratiche(listaPratiche);
    return output;
  }
}

/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.business.service.impl;

import static it.csi.cosmo.cosmonotifications.config.ErrorMessages.LIMIT_NON_VALIDO;
import static it.csi.cosmo.cosmonotifications.config.ErrorMessages.NOTIFICA_NON_TROVATA;
import static it.csi.cosmo.cosmonotifications.config.ErrorMessages.NOTIFICA_NON_VALIDA;
import static it.csi.cosmo.cosmonotifications.config.ErrorMessages.OFFSET_NON_VALIDO;
import static it.csi.cosmo.cosmonotifications.config.ErrorMessages.UTENTE_NON_PRESENTE;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.LongPredicate;
import java.util.stream.Collectors;
import javax.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.entities.CosmoDTipoCondivisionePratica_;
import it.csi.cosmo.common.entities.CosmoLStoricoPratica;
import it.csi.cosmo.common.entities.CosmoRAttivitaAssegnazione;
import it.csi.cosmo.common.entities.CosmoRNotificaUtenteEnte;
import it.csi.cosmo.common.entities.CosmoRNotificaUtenteEntePK;
import it.csi.cosmo.common.entities.CosmoRPraticaPratica_;
import it.csi.cosmo.common.entities.CosmoRPraticaUtenteGruppo;
import it.csi.cosmo.common.entities.CosmoRPraticaUtenteGruppo_;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTAttivita_;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.entities.CosmoTGruppo_;
import it.csi.cosmo.common.entities.CosmoTNotifica;
import it.csi.cosmo.common.entities.CosmoTNotifica_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.CosmoTUtenteGruppo;
import it.csi.cosmo.common.entities.CosmoTUtente_;
import it.csi.cosmo.common.entities.enums.RelazionePraticaUtente;
import it.csi.cosmo.common.entities.enums.StatoInvioNotificaMail;
import it.csi.cosmo.common.entities.enums.TipoEventoStoricoPratica;
import it.csi.cosmo.common.entities.enums.TipoNotifica;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmo.dto.rest.ws.WebSocketEventPostRequest;
import it.csi.cosmo.cosmo.dto.rest.ws.WebSocketPostRequest;
import it.csi.cosmo.cosmo.dto.rest.ws.WebSocketTargetSelector;
import it.csi.cosmo.cosmoauthorization.dto.rest.Preferenza;
import it.csi.cosmo.cosmonotifications.business.service.NotificationsService;
import it.csi.cosmo.cosmonotifications.business.service.TransactionService;
import it.csi.cosmo.cosmonotifications.config.Constants;
import it.csi.cosmo.cosmonotifications.config.ws.WebsocketPostEventName;
import it.csi.cosmo.cosmonotifications.config.ws.WebsocketPostPayloadType;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaResponse;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificheRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificheResponse;
import it.csi.cosmo.cosmonotifications.dto.rest.Notifica;
import it.csi.cosmo.cosmonotifications.dto.rest.NotificheGlobaliRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.PageInfo;
import it.csi.cosmo.cosmonotifications.dto.rest.PaginaNotifiche;
import it.csi.cosmo.cosmonotifications.dto.rest.WebsocketPayload;
import it.csi.cosmo.cosmonotifications.integration.mapper.AbstractMapper;
import it.csi.cosmo.cosmonotifications.integration.mapper.CosmoNotificationMapper;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoEnteRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoFruitoreRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoLStoricoPraticaRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoNotificationRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoPraticaRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoRAttivitaAssegnazioneRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoRNotificaUtenteEnteRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoRPraticaPraticaRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoRPraticaUtenteGruppoRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoTAttivitaRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoTGruppoRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoTipoNotificaRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoUtenteRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.specifications.CosmoNotificheSpecifications;
import it.csi.cosmo.cosmonotifications.integration.rest.CosmoAuthorizationPreferenzeUtentiFeignClient;
import it.csi.cosmo.cosmonotifications.integration.rest.CosmoWebsocketFeignClient;
import it.csi.cosmo.cosmonotifications.security.SecurityUtils;
import it.csi.cosmo.cosmonotifications.util.logger.LogCategory;
import it.csi.cosmo.cosmonotifications.util.logger.LoggerFactory;

@Service
@Transactional
public class NotificationsServiceImpl implements NotificationsService {

  private final CosmoLogger logger = LoggerFactory.getLogger(
      LogCategory.COSMONOTIFICATIONS_BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  private CosmoNotificationMapper mapper;

  @Autowired
  private CosmoNotificationRepository notificheRepo;

  @Autowired
  private CosmoFruitoreRepository fruitoreRepo;

  @Autowired
  private CosmoPraticaRepository praticaRepo;

  @Autowired
  private CosmoUtenteRepository utenteRepo;

  @Autowired
  private CosmoTGruppoRepository gruppoRepo;

  @Autowired
  private CosmoRNotificaUtenteEnteRepository notificaUtenteRepo;

  @Autowired
  private CosmoEnteRepository enteRepo;

  @Autowired
  private CosmoTipoNotificaRepository tipoNotificaRepo;

  @Autowired
  private CosmoTAttivitaRepository attivitaRepo;

  @Autowired
  private CosmoRAttivitaAssegnazioneRepository attivitaAssegnazioneRepo;

  @Autowired
  private CosmoRPraticaUtenteGruppoRepository praticaUtenteGruppoRepo;

  @Autowired
  private CosmoLStoricoPraticaRepository storicoPraticaRepo;

  @Autowired
  private CosmoRPraticaPraticaRepository praticaPraticaRepo;

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private CosmoWebsocketFeignClient websocketFeignClient;

  @Autowired
  private CosmoAuthorizationPreferenzeUtentiFeignClient preferenzeUtenteFeignClient;

  private <T> Long parseQueryParameter(T x, LongPredicate p, int defaultValue,
      String errorMessage) {
    Long res;
    try {
      res = x == null ? defaultValue : Long.parseLong(x.toString());
    } catch (NumberFormatException e) {
      throw new BadRequestException(String.format(errorMessage, x));
    }
    if (!p.test(res)) {
      throw new BadRequestException(String.format(errorMessage, x));
    }
    return res;
  }

  private CosmoRNotificaUtenteEnte lettura(String codiceFiscale, Long idNotifica,
      String codiceIpa) {
    CosmoRNotificaUtenteEntePK pk = new CosmoRNotificaUtenteEntePK();
    Long idUtente = utenteRepo.findByCodiceFiscale(codiceFiscale).getId();

    if (codiceIpa != null && !codiceIpa.isBlank()) {
      CosmoTEnte ente = enteRepo.findByCodiceIpa(codiceIpa);
      if (ente != null) {
        pk.setIdEnte(ente.getId());
      }
    }

    pk.setIdNotifica(idNotifica);
    pk.setIdUtente(idUtente);
    return notificaUtenteRepo.findOne(pk);
  }

  @Override
  public PaginaNotifiche getNotifications(Integer offset, Integer limit) {

    String codiceFiscaleUtente = SecurityUtils.getUtenteCorrente().getCodiceFiscale();

    String codiceIpaEnte = SecurityUtils.getUtenteCorrente().getEnte() != null
        ? SecurityUtils.getUtenteCorrente().getEnte().getTenantId()
            : null;

    Long parsedOffset = parseQueryParameter(offset, x -> x >= 0, 0, OFFSET_NON_VALIDO);
    Long parsedLimit = parseQueryParameter(limit, x -> x > 1, 10, LIMIT_NON_VALIDO);

    Page<CosmoTNotifica> pp = notificheRepo.findAll(
        CosmoNotificheSpecifications.findByCodiceFiscaleAndCodiceIpa(codiceFiscaleUtente,
            codiceIpaEnte),
        new PageRequest(parsedOffset.intValue(), parsedLimit.intValue(),
            new Sort(Direction.DESC, CosmoTNotifica_.arrivo.getName())));

    Long countNonLette = notificheRepo.count(CosmoNotificheSpecifications
        .findNonLetteByCodiceFiscaleAndCodiceIpa(codiceFiscaleUtente, codiceIpaEnte));

    List<CosmoRNotificaUtenteEnte> statiLettura = pp.getContent().isEmpty()
        ? Collections.emptyList()
            : notificaUtenteRepo.findAll(CosmoNotificheSpecifications
                .findStatoLetturaByCodiceFiscaleAndCodiceIpa(codiceFiscaleUtente, codiceIpaEnte,
                    pp.getContent().stream().map(CosmoTNotifica::getId).collect(Collectors.toSet())));

    PaginaNotifiche result = new PaginaNotifiche();

    PageInfo pageinfo = new PageInfo();
    pageinfo.setPage(pp.getNumber());
    pageinfo.setPageSize(pp.getSize());
    pageinfo.setTotalElements((int) pp.getTotalElements());
    pageinfo.setTotalPages(pp.getTotalPages());

    result.setInfo(pageinfo);
    result
    .setElementi(pp.getContent().stream()
        .map(e -> mapper.toNotification(e,
            statiLettura.stream()
            .filter(stato -> stato.getId().getIdNotifica().equals(e.getId())).findFirst()
            .orElseThrow()))
        .collect(Collectors.toList()));
    result.setTotaleNonLette(countNonLette.intValue());

    return result;
  }

  @Override
  public Notifica getNotificationsId(String idNotifica) {

    String codiceFiscaleUtente = SecurityUtils.getUtenteCorrente().getCodiceFiscale();

    String codiceIpaEnte = SecurityUtils.getUtenteCorrente().getEnte() != null
        ? SecurityUtils.getUtenteCorrente().getEnte().getTenantId()
            : null;

    Long parsedIdNotifica = parseQueryParameter(idNotifica, x -> x >= 0, 0,
        String.format(NOTIFICA_NON_VALIDA, idNotifica));

    CosmoTUtente user = utenteRepo.findByCodiceFiscale(codiceFiscaleUtente);
    if (user == null) {
      throw new NotFoundException(UTENTE_NON_PRESENTE);
    }
    CosmoTNotifica notifica = user.getCosmoRNotificaUtenteEntes().stream()
        .filter(nue -> null != codiceIpaEnte
        && codiceIpaEnte.equals(nue.getCosmoTEnte().getCodiceIpa()))
        .map(CosmoRNotificaUtenteEnte::getCosmoTNotifica)
        .filter(n -> n.getId().equals(parsedIdNotifica)).findFirst()
        .orElseThrow(() -> new NotFoundException(String.format(NOTIFICA_NON_TROVATA, idNotifica)));

    return mapper.toNotification(notifica,
        lettura(codiceFiscaleUtente, parsedIdNotifica, codiceIpaEnte));
  }

  @Override
  public CreaNotificheResponse postNotifications(CreaNotificheRequest body) {
    CreaNotificheResponse response = new CreaNotificheResponse();
    List<CreaNotificaResponse> notifiche = new LinkedList<>();

    body.getNotifiche().forEach(n -> {
      var result = transactionService.inTransaction(() -> notifiche.add(postNotification(n)));
      if (result.failed()) {
        logger.error("postNotifications", "error sending notification", result.getError());
      }
    });

    response.setNotifiche(notifiche);
    return response;
  }

  @Override
  public CreaNotificaResponse postNotificationFruitore(CreaNotificaRequest body,
      CosmoTFruitore fruitore) {

    return doPostNotification(body);
  }

  private CreaNotificaResponse postNotification(CreaNotificaRequest body) {
    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    return doPostNotification(body);
  }

  private CreaNotificaResponse doPostNotification(CreaNotificaRequest body) {
    String methodName = "doPostNotification";

    Timestamp now = Timestamp.from(Instant.now());

    CosmoTEnte ente =
        body.getCodiceIpaEnte() == null ? null : enteRepo.findByCodiceIpa(body.getCodiceIpaEnte());

    // calcola i destinatari
    List<CosmoRNotificaUtenteEnte> users = new ArrayList<>();
    if (body.getUtentiDestinatari() != null && !body.getUtentiDestinatari().isEmpty()) { // NOSONAR
      body.getUtentiDestinatari().stream().map(cf -> utenteRepo.findByCodiceFiscale(cf))
      .map(utente -> getRelazioneNotificaUtenteEnte(utente, ente, now))
      .filter(Objects::nonNull).forEach(users::add);
    }

    // spacchetta i gruppi destinatari
    // il gruppo dei destinatari viene creato nel momento della creazione del task.
    // Quindi per ogni destinatario si controlla se nelle preferenze, la notifica per la creazione
    // dei task, e' abilitata o meno.
    if (body.getGruppiDestinatari() != null && !body.getGruppiDestinatari().isEmpty()) {
      body.getGruppiDestinatari().stream()
      .map(code -> gruppoRepo.findOneNotDeleted((root, cq, cb) -> {
        if (ente != null) {
          var joinEnte = root.join(CosmoTGruppo_.ente);
          return cb.and(cb.equal(joinEnte.get(CosmoTEnte_.id), ente.getId()),
              cb.equal(root.get(CosmoTGruppo_.codice), code));
        } else {
          return cb.and(cb.isNull(root.get(CosmoTGruppo_.ente)),
              cb.equal(root.get(CosmoTGruppo_.codice), code));
        }
      }).orElseThrow(() -> new NotFoundException("Gruppo non trovato")))
      .filter(gruppo -> gruppo != null && gruppo.getAssociazioniUtenti() != null
      && !gruppo.getAssociazioniUtenti().isEmpty())
      .flatMap(
          gruppo -> gruppo.getAssociazioniUtenti().stream().map(CosmoTUtenteGruppo::getUtente))
      .map(utente -> getRelazioneNotificaUtenteEnte(utente, ente, now))
      .filter(Objects::nonNull).forEach(users::add);
    }

    logger.info(methodName, "Inviare una notifica di tipo " + body.getTipoNotifica()
    + " agli utenti " + users.toString());

    // eseguo un prefetch delle preferenze utenti per non triplicare le chiamate feign a
    // cosmoauthorization
    Map<String, PreferenzeNotificheUtenteParsed> preferenzeMap = new HashMap<>();

    users.stream().forEach(u -> {
      String hash = u.getCosmoTUtente().getId().toString();
      preferenzeMap.computeIfAbsent(hash,
          h -> parsePreferenzeNotificheUtente(u.getCosmoTUtente(), body.getTipoNotifica()));
      var preferenze = preferenzeMap.get(u.getCosmoTUtente().getId().toString());
      if (Boolean.FALSE.equals(preferenze.mail)) {
        u.setInvioMail(false);
        u.setStatoInvioMail(null);
      } else {
        u.setInvioMail(true);
        u.setStatoInvioMail(StatoInvioNotificaMail.DA_INVIARE.name());
      }
    });


    List<CosmoRNotificaUtenteEnte> usersNotifichePush = users.stream()
        .filter(u -> Boolean.TRUE
            .equals(preferenzeMap.get(u.getCosmoTUtente().getId().toString()).push))
        .collect(Collectors.toList());

    CreaNotificaResponse response = new CreaNotificaResponse();

    final String titolo = ente != null ? ente.getNome() : null;
    if (!users.isEmpty()) {
      response = salvaNotifica(body, now, users);
    }

    if (!usersNotifichePush.isEmpty()) {
      inviaPush(usersNotifichePush, body.getMessaggio(), body.getClasse(), titolo, body.getUrl(),
          body.getDescrizioneUrl(), body.getEvento());
    }

    return response;
  }

  private CosmoRNotificaUtenteEnte getRelazioneNotificaUtenteEnte(CosmoTUtente utente,
      CosmoTEnte ente, Timestamp now) {
    if (utente !=null && ente != null && utente.getCosmoRUtenteEntes().stream().anyMatch(
        utenteEnte -> ente.getCodiceIpa().equals(utenteEnte.getCosmoTEnte().getCodiceIpa()))) {
      var rel = new CosmoRNotificaUtenteEnte();
      rel.setCosmoTUtente(utente);
      rel.setCosmoTEnte(ente);
      rel.setDtInizioVal(now);
      return rel;
    }
    return null;
  }

  @Override
  public Notifica putNotificationsId(String idNotifica, Notifica body) {
    long parsedIdNotifica;
    try {
      parsedIdNotifica = Long.parseLong(idNotifica);
    } catch (NumberFormatException e) {
      throw new NotFoundException(String.format(NOTIFICA_NON_TROVATA, idNotifica));
    }
    String codiceFiscaleUtente = SecurityUtils.getUtenteCorrente().getCodiceFiscale();
    String codiceIpaEnte = SecurityUtils.getUtenteCorrente().getEnte() != null
        ? SecurityUtils.getUtenteCorrente().getEnte().getTenantId()
            : null;

    Long idUtente = utenteRepo.findByCodiceFiscale(codiceFiscaleUtente).getId();
    Long idEnte = enteRepo.findByCodiceIpa(codiceIpaEnte).getId();

    CosmoRNotificaUtenteEntePK pk = new CosmoRNotificaUtenteEntePK();
    pk.setIdNotifica(parsedIdNotifica);
    pk.setIdUtente(idUtente);
    pk.setIdEnte(idEnte);
    CosmoRNotificaUtenteEnte n = notificaUtenteRepo.findOne(pk);

    if (n == null) {
      throw new NotFoundException(String.format(NOTIFICA_NON_TROVATA, idNotifica));
    }

    n.setDataLettura(new Timestamp(System.currentTimeMillis()));
    notificaUtenteRepo.save(n);
    CosmoTNotifica saved = notificheRepo.findOne(parsedIdNotifica);
    return mapper.toNotification(saved, n);
  }

  @Override
  public void sendNotification(CosmoTNotifica notifica, String titolo, String evento) {
    String methodName = "sendNotification";

    final String channel = "notifications";

    List<WebSocketTargetSelector> targets =
        notifica.getCosmoRNotificaUtenteEntes().stream().map(utente -> {
          WebSocketTargetSelector targetSelector = new WebSocketTargetSelector();
          targetSelector.setCodiceFiscale(utente.getCosmoTUtente().getCodiceFiscale());
          if (utente.getCosmoTEnte() != null) {
            targetSelector.setIdEnte(utente.getCosmoTEnte().getId());
          }
          return targetSelector;
        }).collect(Collectors.toList());

    WebsocketPayload payload = new WebsocketPayload();
    payload.setMessage(notifica.getDescrizione());

    if (notifica.getUrlDescrizione() != null) {
      payload.setUrlDescription(notifica.getUrlDescrizione());
    }
    if (notifica.getUrl() != null) {
      payload.setUrl(notifica.getUrl());
    }

    if (titolo != null && !titolo.isBlank()) {
      payload.setTitle(titolo);
    }

    payload.setType(WebsocketPostPayloadType.INFO.getType());
    if (!StringUtils.isBlank(notifica.getClasse())) {
      WebsocketPostPayloadType type = WebsocketPostPayloadType.fromCode(notifica.getClasse());
      if (type != null) {
        payload.setType(type.getType());
      }
    }

    payload.setNotificationId(notifica.getId());


    logger.info(methodName, "Invia push di tipo: " + payload.getType());

    // creo il toast
    WebSocketPostRequest webSocketPost = new WebSocketPostRequest();
    webSocketPost.setPayload(payload);
    webSocketPost.setTargets(targets);
    try {
      websocketFeignClient.post(channel, webSocketPost);
    } catch (Exception e) {
      logger.error(methodName, "error event ", e);
    }


    WebSocketEventPostRequest websocketPostRequest = new WebSocketEventPostRequest();

    websocketPostRequest.setEvent(WebsocketPostEventName.NOTIFICA_ULTIME_LAVORATE.getEventName());
    if (!StringUtils.isBlank(evento)) {
      WebsocketPostEventName event = WebsocketPostEventName.fromEventName(evento);
      if (event != null) {
        websocketPostRequest.setEvent(event.getEventName());
      }
    }

    websocketPostRequest.setPayload(payload);
    websocketPostRequest.setTargets(targets);
    try {
      websocketFeignClient.postEvent(websocketPostRequest);
    } catch (Exception e) {
      logger.error(methodName, "error event ", e);
    }
  }

  private CreaNotificaResponse salvaNotifica(CreaNotificaRequest body, Timestamp now,
      List<CosmoRNotificaUtenteEnte> users) {
    final String methodName = "salvaNotificaInviaPush";

    CosmoTNotifica notifica = new CosmoTNotifica();
    notifica.setDescrizione(body.getMessaggio());
    notifica.setClasse(body.getClasse());

    if (body.getArrivo() != null) {
      notifica.setArrivo(AbstractMapper.toTimestamp(body.getArrivo()));
    } else {
      notifica.setArrivo(now);
    }

    if (body.getScadenza() != null) {
      notifica.setScadenza(AbstractMapper.toTimestamp(body.getScadenza()));
    }

    if (body.getIdPratica() != null) {
      notifica.setCosmoTPratica(praticaRepo.findOneNotDeleted(body.getIdPratica())
          .orElseThrow(() -> new NotFoundException("Pratica non trovata")));
    }

    if (body.getIdFruitore() != null) {
      notifica.setCosmoTFruitore(fruitoreRepo.findOneNotDeleted(body.getIdFruitore())
          .orElseThrow(() -> new NotFoundException("Fruitore non trovato")));
    }

    if (body.getTipoNotifica() != null) {
      notifica.setCosmoDTipoNotifica(tipoNotificaRepo
          .findOneActive(
              TipoNotifica.fromAzione(body.getTipoNotifica()).getCodice())
          .orElseThrow(() -> new NotFoundException("Tipo notifica non trovata")));
    }

    if (body.getUrl() != null) {
      notifica.setUrl(body.getUrl());
    }

    if (body.getDescrizioneUrl() != null) {
      notifica.setUrlDescrizione(body.getDescrizioneUrl());
    }

    notifica.setCosmoRNotificaUtenteEntes(new ArrayList<>());

    // salva la notifica
    var inserita = notificheRepo.save(notifica);
    notificheRepo.flush();

    // salva i destinatari
    users.forEach(rel -> {
      CosmoRNotificaUtenteEntePK pk = new CosmoRNotificaUtenteEntePK();
      pk.setIdNotifica(inserita.getId());
      pk.setIdUtente(rel.getCosmoTUtente().getId());
      pk.setIdEnte(rel.getCosmoTEnte().getId());
      rel.setId(pk);
      rel.setCosmoTNotifica(inserita);
      notifica.addCosmoRNotificaUtenteEnte(notificaUtenteRepo.save(rel));
    });

    logger.debug(methodName, "creata notifica {}", inserita.getId());

    CreaNotificaResponse response = new CreaNotificaResponse();
    response.setId(inserita.getId());

    return response;
  }

  private void inviaPush(List<CosmoRNotificaUtenteEnte> users, String messaggio, String classe,
      String titolo, String url, String urlDescrizione, String evento) {
    final String channel = "notifications";
    final String methodName = "inviaPush";

    List<WebSocketTargetSelector> targets = users.stream().map(utente -> {
      WebSocketTargetSelector targetSelector = new WebSocketTargetSelector();
      targetSelector.setCodiceFiscale(utente.getCosmoTUtente().getCodiceFiscale());
      if (utente.getCosmoTEnte() != null) {
        targetSelector.setIdEnte(utente.getCosmoTEnte().getId());
      }
      return targetSelector;
    }).collect(Collectors.toList());

    WebsocketPayload payload = new WebsocketPayload();
    payload.setMessage(messaggio);

    if (urlDescrizione != null && !urlDescrizione.isBlank()) {
      payload.setUrlDescription(urlDescrizione);
    }
    if (url != null && !url.isBlank()) {
      payload.setUrl(url);
    }

    if (titolo != null && !titolo.isBlank()) {
      payload.setTitle(titolo);
    }

    payload.setType(WebsocketPostPayloadType.INFO.getType());
    if (!StringUtils.isBlank(classe)) {
      WebsocketPostPayloadType type = WebsocketPostPayloadType.fromCode(classe);
      if (type != null) {
        payload.setType(type.getType());
      }
    }

    // creo il toast
    WebSocketPostRequest webSocketPost = new WebSocketPostRequest();
    webSocketPost.setPayload(payload);
    webSocketPost.setTargets(targets);
    try {
      websocketFeignClient.post(channel, webSocketPost);
    } catch (Exception e) {
      logger.error(methodName, "error event ", e);
    }

    WebSocketEventPostRequest websocketPostRequest = new WebSocketEventPostRequest();

    websocketPostRequest.setEvent(WebsocketPostEventName.NOTIFICA_ULTIME_LAVORATE.getEventName());
    if (!StringUtils.isBlank(evento)) {
      WebsocketPostEventName event = WebsocketPostEventName.fromEventName(evento);
      if (event != null) {
        websocketPostRequest.setEvent(event.getEventName());
      }
    }

    websocketPostRequest.setPayload(payload);
    websocketPostRequest.setTargets(targets);
    try {
      websocketFeignClient.postEvent(websocketPostRequest);
    } catch (Exception e) {
      logger.error(methodName, "error event ", e);
    }



  }

  @Override
  public void sendNotifications(NotificheGlobaliRequest notifica) {
    final String methodName = "sendNotifications";

    ValidationUtils.validaAnnotations(notifica);

    CosmoTPratica pratica = praticaRepo.findOneNotDeleted(notifica.getIdPratica()).orElse(null);

    if (pratica == null) {
      String message = String.format("Pratica con id %s non trovata", notifica.getIdPratica());
      logger.error(methodName, message);
      return;
    }

    List<CreaNotificaRequest> notifiche = new LinkedList<>();

    List<String> utentiDestinatari = getCodiciFiscaliDestinatari(pratica, new LinkedList<>());


    if (!utentiDestinatari.isEmpty()) {
      logger.info(methodName,
          "La notifica deve essere inviata a " + utentiDestinatari.size() + " destinatari");

      CreaNotificaRequest notificaDaInviare = new CreaNotificaRequest();
      notificaDaInviare.setIdPratica(pratica.getId());
      notificaDaInviare.setUtentiDestinatari(utentiDestinatari);
      notificaDaInviare
      .setTipoNotifica(TipoNotifica.fromCodice(notifica.getTipoNotifica()).getAzione());

      if (SecurityUtils.getUtenteCorrente().getEnte() != null) {
        notificaDaInviare
        .setCodiceIpaEnte(SecurityUtils.getUtenteCorrente().getEnte().getTenantId());
      } else if (notifica.getCodiceIpaEnte() != null) {
        notificaDaInviare.setCodiceIpaEnte(notifica.getCodiceIpaEnte());
      }

      notificaDaInviare.setPush(Boolean.TRUE);
      notificaDaInviare.setClasse(notifica.getClasse());
      notificaDaInviare.setEvento(notifica.getEvento());
      notificaDaInviare.setUrl(notifica.getUrl());
      notificaDaInviare.setDescrizioneUrl(notifica.getDescrizioneUrl());

      notificaDaInviare.setMessaggio(
          String.format(Constants.MESSAGGIO, notifica.getMessaggio(), pratica.getOggetto()));
      notifiche.add(notificaDaInviare);
    }

    notifiche.addAll(creaNotificheDellaRelazione(pratica, utentiDestinatari, notifica));

    if (!notifiche.isEmpty()) {
      CreaNotificheRequest creaNotificheRequest = new CreaNotificheRequest();
      creaNotificheRequest.setNotifiche(notifiche);
      postNotifications(creaNotificheRequest);
    }

  }

  private List<String> getCodiciFiscaliDestinatari(CosmoTPratica pratica,
      List<String> utentiPadre) {

    List<String> result = new LinkedList<>();

    if (SecurityUtils.getUtenteCorrente() == null
        || SecurityUtils.getUtenteCorrente().getCodiceFiscale() == null
        || SecurityUtils.getUtenteCorrente().getCodiceFiscale().isBlank()) {
      return new LinkedList<>();
    }

    if (pratica.getUtenteCreazionePratica() != null
        && !pratica.getUtenteCreazionePratica().isBlank()) {
      Optional<CosmoTUtente> utenteCreatore = utenteRepo.findOneNotDeletedByField(
          CosmoTUtente_.codiceFiscale, pratica.getUtenteCreazionePratica());

      if (utenteCreatore.isPresent()
          && !SecurityUtils.getUtenteCorrente().getCodiceFiscale()
          .equals(utenteCreatore.get().getCodiceFiscale())
          && !utentiPadre.contains(utenteCreatore.get().getCodiceFiscale())) {
        result.add(utenteCreatore.get().getCodiceFiscale());
      }
    }

    List<Long> idUtenti = new LinkedList<>();

    List<CosmoTAttivita> attiviteValide =
        attivitaRepo.findNotDeletedByField(CosmoTAttivita_.cosmoTPratica, pratica);

    attiviteValide.forEach(attivitaValida -> {
      List<CosmoRAttivitaAssegnazione> assegnazioni = attivitaAssegnazioneRepo
          .findAllByCosmoTAttivitaIdAndDtFineValIsNullAndUtenteNotNullAndUtenteCodiceFiscaleNotAndUtenteDtCancellazioneNull(
              attivitaValida.getId(), SecurityUtils.getUtenteCorrente().getCodiceFiscale());

      assegnazioni.stream().filter(ass -> !idUtenti.contains(ass.getIdUtente().longValue()))
      .forEach(ass -> idUtenti.add(ass.getIdUtente().longValue()));
    });

    List<CosmoRPraticaUtenteGruppo> associazioniUtenti =
        praticaUtenteGruppoRepo.findAllActive((root, cq, cb) -> {
          Predicate predicate = cb.and(
              cb.equal(root.get(CosmoRPraticaUtenteGruppo_.cosmoTPratica).get(CosmoTPratica_.id),
                  pratica.getId()),
              cb.equal(
                  root.get(CosmoRPraticaUtenteGruppo_.cosmoDTipoCondivisionePratica)
                  .get(CosmoDTipoCondivisionePratica_.codice),
                  RelazionePraticaUtente.PREFERITA.getCodice()),
              root.get(CosmoRPraticaUtenteGruppo_.cosmoTUtente).get(CosmoTUtente_.codiceFiscale)
              .in(SecurityUtils.getUtenteCorrente().getCodiceFiscale()).not(),
              cb.isNull(
                  root.get(CosmoRPraticaUtenteGruppo_.cosmoTUtente)
                  .get(CosmoTEntity_.dtCancellazione)));

          return cq.where(predicate).getRestriction();
        });

    associazioniUtenti.stream().filter(ass -> !idUtenti.contains(ass.getCosmoTUtente().getId()))
    .forEach(ass -> idUtenti.add(ass.getCosmoTUtente().getId()));

    List<CosmoTUtente> utenti = utenteRepo.findByIdIn(idUtenti);

    result.addAll(utenti.stream().filter(
        u -> !result.contains(u.getCodiceFiscale()) && !utentiPadre.contains(u.getCodiceFiscale()))
        .map(CosmoTUtente::getCodiceFiscale).collect(Collectors.toList()));

    List<CosmoLStoricoPratica> storicoPratica = storicoPraticaRepo
        .findAllByPraticaIdAndCodiceTipoEventoAndUtenteCodiceFiscaleNotAndUtenteDtCancellazioneNull(
            pratica.getId(), TipoEventoStoricoPratica.ATTIVITA_LAVORATA,
            SecurityUtils.getUtenteCorrente().getCodiceFiscale());

    storicoPratica.forEach(s -> {
      if (!result.contains(s.getUtente().getCodiceFiscale())
          && !utentiPadre.contains(s.getUtente().getCodiceFiscale())) {
        result.add(s.getUtente().getCodiceFiscale());
      }
    });

    return result.stream().collect(Collectors.toList());
  }

  private List<CreaNotificaRequest> creaNotificheDellaRelazione(CosmoTPratica pratica,
      List<String> utentiPadre, NotificheGlobaliRequest notifica) {
    String methodName = "creaNotificheDellaRelazione";

    List<CreaNotificaRequest> output = new ArrayList<>();

    var pratiche =
        praticaPraticaRepo.findActiveByField(CosmoRPraticaPratica_.cosmoTPraticaDa, pratica);

    pratiche.forEach(p -> {
      CosmoTPratica praticaA = praticaRepo.findOne(p.getId().getIdPraticaA());

      if (praticaA == null) {
        String message = String.format("Pratica con id %d non trovata", p.getId().getIdPraticaA());
        logger.error(methodName, message);
      }

      List<String> utentiDestinatari = getCodiciFiscaliDestinatari(praticaA, utentiPadre);

      if (!utentiDestinatari.isEmpty()) {

        CreaNotificaRequest notificaDaInviare = new CreaNotificaRequest();
        notificaDaInviare.setIdPratica(pratica.getId());
        notificaDaInviare.setUtentiDestinatari(utentiDestinatari);
        notificaDaInviare
        .setTipoNotifica(TipoNotifica.fromCodice(notifica.getTipoNotifica()).getAzione());

        if (SecurityUtils.getUtenteCorrente().getEnte() != null) {
          notificaDaInviare
          .setCodiceIpaEnte(SecurityUtils.getUtenteCorrente().getEnte().getTenantId());
        }
        notificaDaInviare.setPush(Boolean.TRUE);
        notificaDaInviare.setClasse(notifica.getClasse());
        notificaDaInviare.setEvento(notifica.getEvento());

        CosmoTPratica praticaInRelazione = praticaRepo.findOne(p.getId().getIdPraticaA());
        if (praticaInRelazione != null) {
          notificaDaInviare.setMessaggio(String.format(Constants.MESSAGGIO_PARENT,
              notifica.getMessaggio(), pratica.getOggetto(),
              p.getCosmoDTipoRelazionePratica().getDescrizione().toLowerCase(),
              praticaInRelazione.getOggetto()));

        } else {
          notificaDaInviare.setMessaggio(
              String.format(Constants.MESSAGGIO, notifica.getMessaggio(), pratica.getOggetto()));
        }

        output.add(notificaDaInviare);
      }
    });

    return output;

  }

  private PreferenzeNotificheUtenteParsed parsePreferenzeNotificheUtente(CosmoTUtente utente,
      String value) {
    PreferenzeNotificheUtenteParsed out = new PreferenzeNotificheUtenteParsed();
    out.push = Boolean.TRUE;
    out.mail = Boolean.TRUE;


    Preferenza preferenze =
        preferenzeUtenteFeignClient.getPreferenzeUtenteId(utente.getId().toString());

    if (preferenze != null && preferenze.getValore() != null && !preferenze.getValore().isBlank()) {
      JsonNode ricezioneNotifiche =
          ObjectUtils.fromJson(preferenze.getValore(), JsonNode.class).get("ricezioneNotifiche");

      if (ricezioneNotifiche != null) {

        JsonNode ricezioneNotificheCosmo = ricezioneNotifiche.get(Constants.COSMO_NOTIFICATIONS);
        if (ricezioneNotificheCosmo != null) {
          var optInSpecific = ricezioneNotificheCosmo.get(value);

          //@formatter:off
          if (optInSpecific != null && !optInSpecific.booleanValue()){
            out.push = Boolean.FALSE;
          }
          //@formatter:on
        }

        JsonNode ricezioneNotificheMail = ricezioneNotifiche.get(Constants.EMAIL_NOTIFICATIONS);
        if (ricezioneNotificheMail != null) {
          var optInSpecific = ricezioneNotificheMail.get(value);

          //@formatter:off
          if (optInSpecific != null && !optInSpecific.booleanValue()){
            out.mail = Boolean.FALSE;
          }
          //@formatter:on
        }

      }
    }


    return out;
  }

  private static class PreferenzeNotificheUtenteParsed {
    protected Boolean push;
    protected Boolean mail;
  }

  @Override
  public void putNotificationsAll() {
    notificaUtenteRepo.markAllNotificationsRead(SecurityUtils.getUtenteCorrente().getId(),
        SecurityUtils.getUtenteCorrente().getEnte().getId());
  }

}

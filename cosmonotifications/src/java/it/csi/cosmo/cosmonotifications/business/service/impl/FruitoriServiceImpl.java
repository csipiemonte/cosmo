/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.business.service.impl;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.components.CosmoErrorHandler;
import it.csi.cosmo.common.entities.CosmoRFruitoreEnte;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.entities.CosmoTFruitore_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.enums.TipoNotifica;
import it.csi.cosmo.common.entities.proto.IntervalloValiditaEntity;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.exception.UnauthorizedException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmonotifications.business.service.FruitoriService;
import it.csi.cosmo.cosmonotifications.business.service.NotificationsService;
import it.csi.cosmo.cosmonotifications.business.service.TransactionService;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaFruitoreRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaFruitoreResponse;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.Esito;
import it.csi.cosmo.cosmonotifications.dto.rest.EsitoCreazioneNotificaFruitoreResponse;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoFruitoreRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoPraticaRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.specifications.CosmoTPraticaSpecifications;
import it.csi.cosmo.cosmonotifications.security.SecurityUtils;
import it.csi.cosmo.cosmonotifications.util.logger.LogCategory;
import it.csi.cosmo.cosmonotifications.util.logger.LoggerFactory;

@Service
public class FruitoriServiceImpl implements FruitoriService {

  private static final CosmoLogger logger = LoggerFactory
      .getLogger(LogCategory.COSMONOTIFICATIONS_BUSINESS_LOG_CATEGORY,
          FruitoriServiceImpl.class.getSimpleName());

  @Autowired
  private CosmoFruitoreRepository cosmoTFruitoreRepository;

  @Autowired
  private CosmoPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private NotificationsService notificationsService;

  @Autowired
  private TransactionService transactionService;

  private CosmoErrorHandler errorMapper =
      new CosmoErrorHandler(LogCategory.COSMONOTIFICATIONS_BUSINESS_LOG_CATEGORY.getCategory()) {

    @Override
    protected boolean exposeExceptions() {
      return false;
    }

  };

  @Override
  @Transactional(noRollbackFor = Exception.class, timeout = 60000)
  public CreaNotificaFruitoreResponse creaNotificaFruitore(CreaNotificaFruitoreRequest request) {
    final var method = "creaNotificaFruitore";

    OffsetDateTime now = OffsetDateTime.now();

    if (logger.isDebugEnabled()) {
      logger.debug(method, "ricevuta richiesta di creazione notifica da fruitore esterno: "
          + ObjectUtils.represent(request));
    }

    // valida dati di input
    ValidationUtils.require(request, "request");
    ValidationUtils.validaAnnotations(request);

    if (request.getDestinatari().isEmpty() && request.getUtentiDestinatari().isEmpty()
        && request.getGruppiDestinatari().isEmpty()) {
      throw new BadRequestException("Nessun destinatario fornito.");
    }

    // ottengo la reference al fruitore
    CosmoTFruitore fruitoreEntity = getFruitoreCorrente();

    // valido il codice ipa ente verificando contro le associazioni del fruitore
    var enteAssociato = verificaEnteAssociato(fruitoreEntity, request.getCodiceIpaEnte());

    var pratica = StringUtils.isBlank(request.getIdPratica()) ? null
        : this
        .findPraticaByChiaveEsterna(request.getIdPratica(), request.getCodiceIpaEnte(),
            fruitoreEntity.getId())
        .orElseThrow(() -> new NotFoundException("Pratica non trovata"));

    Supplier<CreaNotificaRequest> forwardRequestBuilder = () -> {
      var forwardRequest = new CreaNotificaRequest();
      forwardRequest.setCodiceIpaEnte(enteAssociato.getCodiceIpa());
      forwardRequest.setIdPratica(pratica != null ? pratica.getId() : null);
      forwardRequest.setIdFruitore(fruitoreEntity.getId());
      forwardRequest.setMessaggio(request.getDescrizione());
      forwardRequest.setArrivo(now);
      forwardRequest.setClasse(null);
      forwardRequest.setEvento(null);
      forwardRequest.setScadenza(null);
      forwardRequest.setPush(Boolean.TRUE);
      forwardRequest.setTipoNotifica(TipoNotifica.INFO.getAzione());
      forwardRequest.setGruppiDestinatari(new ArrayList<>());
      forwardRequest.setUtentiDestinatari(new ArrayList<>());
      forwardRequest.setUrl(request.getUrl() != null ? request.getUrl() : null);
      forwardRequest.setDescrizioneUrl(request.getDescrizioneUrl() != null ? request.getDescrizioneUrl() : null);
      return forwardRequest;
    };

    // tenta invio per ogni destinatario - in transazioni separate
    List<EsitoCreazioneNotificaFruitoreResponse> esiti = new ArrayList<>();

    Consumer<CreaNotificaRequest> forwarder = req -> {
      var esito = new Esito();
      String codiceDestinatario =
          !req.getUtentiDestinatari().isEmpty() ? req.getUtentiDestinatari().get(0)
              : req.getGruppiDestinatari().get(0);

      var attempt =
          transactionService.inTransaction(
              () -> notificationsService.postNotificationFruitore(req, fruitoreEntity));

      if (attempt.success()) {
        esito.setCode("CREATED");
        esito.setStatus(201);
        esito.setTitle("Notifica inviata");
      } else {
        logger.error(method, "errore nell'invio di una notifica" + codiceDestinatario,
            attempt.getError());
        var converted = errorMapper.convertException(attempt.getError());
        esito.setCode(converted.getCode());
        esito.setStatus(converted.getStatus());
        esito.setTitle(converted.getTitle());
      }

      EsitoCreazioneNotificaFruitoreResponse esitoDTO =
          new EsitoCreazioneNotificaFruitoreResponse();
      esitoDTO.setDestinatario(codiceDestinatario);
      esitoDTO.setEsito(esito);
      esiti.add(esitoDTO);
    };

    if (request.getDestinatari() != null) {
      for (String destinatarioLegacy : request.getDestinatari()) {
        var forwardRequest = forwardRequestBuilder.get();
        forwardRequest.getUtentiDestinatari().add(destinatarioLegacy);
        forwarder.accept(forwardRequest);
      }
    }
    if (request.getUtentiDestinatari() != null) {
      for (String destinatario : request.getUtentiDestinatari()) {
        var forwardRequest = forwardRequestBuilder.get();
        forwardRequest.getUtentiDestinatari().add(destinatario);
        forwarder.accept(forwardRequest);
      }
    }
    if (request.getGruppiDestinatari() != null) {
      for (String destinatario : request.getGruppiDestinatari()) {
        var forwardRequest = forwardRequestBuilder.get();
        forwardRequest.getGruppiDestinatari().add(destinatario);
        forwarder.accept(forwardRequest);
      }
    }

    // ritorna response con i singoli esiti al chiamante
    CreaNotificaFruitoreResponse output = new CreaNotificaFruitoreResponse();
    output.setEsiti(esiti);
    return output;
  }

  private CosmoTFruitore getFruitoreCorrente() {
    // ottengo il fruitore corrente
    var fruitore = SecurityUtils.getClientCorrente();

    // ottengo la reference al fruitore
    return cosmoTFruitoreRepository
        .findOneNotDeletedByField(CosmoTFruitore_.apiManagerId, fruitore.getCodice()).orElseThrow(
            () -> new UnauthorizedException("Fruitore non autenticato o non riconosciuto"));
  }

  private CosmoTEnte verificaEnteAssociato(CosmoTFruitore fruitoreEntity, String codiceIpaEnte) {
    //@formatter:off
    return fruitoreEntity.getCosmoRFruitoreEntes().stream()
        .filter(IntervalloValiditaEntity::valido)
        .map(CosmoRFruitoreEnte::getCosmoTEnte)
        .filter(ente -> !ente.cancellato())
        .filter(ente -> codiceIpaEnte.equalsIgnoreCase(ente.getCodiceIpa()))
        .findFirst()
        .orElseThrow(() -> new BadRequestException("Il Codice IPA ente non coincide con nessuno degli enti associati al fruitore"));
    //@formatter:on
  }

  private Optional<CosmoTPratica> findPraticaByChiaveEsterna(String idPraticaExt,
      String codiceIpaEnte, Long idFruitore) {
    return this.cosmoTPraticaRepository.findOneNotDeleted(CosmoTPraticaSpecifications
        .findByChiaveFruitoreEsterno(idPraticaExt, codiceIpaEnte, idFruitore));
  }

}

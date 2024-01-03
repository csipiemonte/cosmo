/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.batch.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.batch.model.BatchExecutionContext;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTLock;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.cosmo.dto.rest.ws.WebSocketTargetSelector;
import it.csi.cosmo.cosmoecm.business.batch.ProtocollazioneStardasBatch;
import it.csi.cosmo.cosmoecm.business.service.EventService;
import it.csi.cosmo.cosmoecm.business.service.LockService;
import it.csi.cosmo.cosmoecm.business.service.StardasService;
import it.csi.cosmo.cosmoecm.business.service.impl.LockServiceImpl.LockAcquisitionRequest;
import it.csi.cosmo.cosmoecm.config.Constants;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo;
import it.csi.cosmo.cosmoecm.dto.stardas.EsitoRichiestaSmistamentoDocumento;

/**
 *
 */
@Service
public class ProtocollazioneStardasBatchImpl extends ParentBatchImpl
implements ProtocollazioneStardasBatch {

  @Autowired
  private StardasService stardasService;

  @Autowired
  private EventService eventService;
  
  @Autowired
  private LockService lockService;
  
  public static final String JOB_LOCK_RESOURCE_CODE = "SMISTAMENTO_STARDAS_JOB_LOCK";

  @Override
  public boolean isEnabled() {
    return configurazioneService != null && configurazioneService
        .requireConfig(ParametriApplicativo.BATCH_SMISTAMENTO_STARDAS_ENABLE).asBool();
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public void execute(BatchExecutionContext context) {
    final String method = "execute";

    logger.info(method, "Inizio batch di smistamento stardas");
    
    this.smistaStardas();
  }

  private void smista(CosmoTDocumento documento) {
    String method = "smista";

    var resultRichiestaSmistamento = inTransaction(() -> smistaDocumento(documento));
    final EsitoRichiestaSmistamentoDocumento esitoRichiestaSmistamento;
    if (resultRichiestaSmistamento.failed()) {
      logger.error(method, String.format(
          "Errore durante la richiesta smistamento del documento con id %d", documento.getId()));
      esitoRichiestaSmistamento = new EsitoRichiestaSmistamentoDocumento(null,
          "Errore durante la richiesta di smistamento", null);
    } else {
      logger.info(method,
          String.format("Codice risposta: %s Messaggio: %s UUID: %s",
              resultRichiestaSmistamento.getResult().getCodice(),
              resultRichiestaSmistamento.getResult().getMessaggio(),
              resultRichiestaSmistamento.getResult().getMessageUUID()));
      esitoRichiestaSmistamento = resultRichiestaSmistamento.getResult();
    }

    if (!Constants.ESITO_RICHIESTA_SMISTAMENTO_TMP
        .equals(resultRichiestaSmistamento.getResult().getCodice())) {
      var resultSalvataggioEsitoRichiestaSmistamento =
          inTransaction(() -> salvaEsitoRichiestaSmistamento(esitoRichiestaSmistamento, documento));

      if (resultSalvataggioEsitoRichiestaSmistamento.failed()) {
        logger.error(method, String.format(
            "Errore durante il salvataggio dell'esito richiesta smistamento del documento con id %d",
            documento.getId()));
      } else {
        logger.beginForClass(method, String
            .format("Richiesta smistamento del documento con id %d completata", documento.getId()));
      }
    } else {
      stardasService.aggiornaDaSmistare(documento);
    }
  }

  private EsitoRichiestaSmistamentoDocumento smistaDocumento(CosmoTDocumento documento) {
    return stardasService.smistaDocumento(documento);
  }

  private void salvaEsitoRichiestaSmistamento(
      EsitoRichiestaSmistamentoDocumento esitoRichiestaSmistamento, CosmoTDocumento documento) {
    stardasService.salvaEsitoRichiestaSmistamento(esitoRichiestaSmistamento, documento);
  }

  private void smistaAllegato(CosmoTDocumento documento) {

    if (stardasService.checkEsitoSmistamentoDocPadre(documento)) {
      smista(documento);
    }

  }

  private void notificaDocumentiSmistati(Map<Long, Set<Long>> mappedDocuments) {
    final var method = "notificaDocumentiSmistati";

    WebSocketTargetSelector target = new WebSocketTargetSelector();
    try {
      mappedDocuments.keySet().forEach(key -> mappedDocuments.get(key).forEach(idPratica -> {

        Map<String, Object> payload = new HashMap<>();
        payload.put("idPratica", idPratica);
        target.setIdEnte(key);
        eventService.broadcastEvent(Constants.EVENTS.DOCUMENTI_SMISTATI, payload, target);

        logger.info(method,
            String.format("Notificato smistamento all'ente %s, pratica %d", key, idPratica));
      }));

    } catch (Exception e) {
      logger.error(method, "Errore durante la notifica dei documenti smistati", e);
    }
  }

  private Map<Long, Set<Long>> smistaStardas() {
  //@formatter:off
    return this.lockService.executeLocking(lock -> smistaInsideLock(lock),
        LockAcquisitionRequest.builder()
        .withCodiceRisorsa(JOB_LOCK_RESOURCE_CODE)
        .withRitardoRetry(500L)
        .withTimeout(2000L)
        .withDurata(5 * 60 * 1000L)
        .build()
        );
    //@formatter:on
    
  }

  private Map<Long, Set<Long>> smistaInsideLock(CosmoTLock lock) {
    
    String method = "smistaInsideLock";
    
    if (lock == null) {
      throw new ConflictException(
          "Non e' consentito avviare la procedura di smistamento senza un lock sulla risorsa "
              + JOB_LOCK_RESOURCE_CODE);
    }

    if (lock.cancellato()
        || (lock.getDtScadenza() != null && lock.getDtScadenza().before(new Date()))) {
      throw new ConflictException(
          "Non e' consentito avviare la procedura di smistamento con un lock sulla risorsa "
              + JOB_LOCK_RESOURCE_CODE + " gia' scaduto");
    }
    
    List<CosmoTDocumento> documentiPrincipaliDaSmistare =
        stardasService.recuperaDocumentiDaSmistare(null, Constants.SOLO_PRINCIPALI);

    logger.info(method, String.format("Sono stati recuperati %d documenti principali da smistare",
        documentiPrincipaliDaSmistare.size()));

    List<CosmoTDocumento> documentiAllegatiDaSmistare =
        stardasService.recuperaDocumentiDaSmistare(null, Constants.SOLO_ALLEGATI);

    logger.info(method, String.format("Sono stati recuperati %d documenti allegati da smistare",
        documentiAllegatiDaSmistare.size()));

    logger.info(method,
        String.format("Inizio elaborazione di %d documenti", documentiPrincipaliDaSmistare.size()));

    documentiPrincipaliDaSmistare.forEach(this::smista);

    logger.info(method,
        String.format("Inizio elaborazione di %d allegati", documentiAllegatiDaSmistare.size()));

    documentiAllegatiDaSmistare.forEach(this::smistaAllegato);

    var documentiTotali = new LinkedList<CosmoTDocumento>();

    documentiTotali.addAll(documentiPrincipaliDaSmistare);
    documentiTotali.addAll(documentiAllegatiDaSmistare);

    Map<Long, Set<Long>> mappedDocuments =
        documentiTotali.stream()
        .collect(Collectors.toMap(stardasService::recuperaIdEnteDaDocumento,
            stardasService::recuperaIdPraticaDaDocumento, (existing, replacement) -> {
              existing.addAll(replacement);
              return existing;
            }));

    this.notificaDocumentiSmistati(mappedDocuments);
    return mappedDocuments;
  }
}

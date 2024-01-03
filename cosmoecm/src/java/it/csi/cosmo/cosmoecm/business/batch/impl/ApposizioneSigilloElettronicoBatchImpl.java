/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.batch.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.batch.model.BatchExecutionContext;
import it.csi.cosmo.common.entities.CosmoRSigilloDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTLock;
import it.csi.cosmo.common.entities.CosmoTSigilloElettronico;
import it.csi.cosmo.common.entities.CosmoTSigilloElettronico_;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.cosmo.dto.rest.ws.WebSocketTargetSelector;
import it.csi.cosmo.cosmobusiness.dto.rest.AvanzamentoProcessoRequest;
import it.csi.cosmo.cosmoecm.business.batch.ApposizioneSigilloElettronicoBatch;
import it.csi.cosmo.cosmoecm.business.service.EventService;
import it.csi.cosmo.cosmoecm.business.service.FirmaService;
import it.csi.cosmo.cosmoecm.business.service.LockService;
import it.csi.cosmo.cosmoecm.business.service.SigilloElettronicoService;
import it.csi.cosmo.cosmoecm.business.service.impl.LockServiceImpl.LockAcquisitionRequest;
import it.csi.cosmo.cosmoecm.config.Constants;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo;
import it.csi.cosmo.cosmoecm.dto.EsitoRichiestaSigilloElettronicoDocumento;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTSigilloElettronicoRepository;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoBusinessProcessoFeignClient;

/**
 *
 */
@Service
public class ApposizioneSigilloElettronicoBatchImpl extends ParentBatchImpl
    implements ApposizioneSigilloElettronicoBatch {

  @Autowired
  private SigilloElettronicoService sigilloElettronicoService;

  @Autowired
  private EventService eventService;

  @Autowired
  private LockService lockService;

  @Autowired
  private FirmaService firmaService;

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CosmoTSigilloElettronicoRepository cosmoTSigilloElettronicoRepository;

  @Autowired
  private CosmoBusinessProcessoFeignClient businessProcessoClient;

  public static final String JOB_LOCK_RESOURCE_CODE = "SIGILLO_ELETTRONICO_JOB_LOCK";
  public static final String SIGILLATO_KO_EVENT = "SIGILLATO_KO";

  @Override
  public boolean isEnabled() {
    return configurazioneService != null && configurazioneService
        .requireConfig(ParametriApplicativo.BATCH_SIGILLO_ELETTRONICO_ENABLE).asBool();
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public void execute(BatchExecutionContext context) {
    final String method = "execute";

    logger.info(method, "Inizio batch di apposizione sigillo elettronico");

    this.apponiSigilloElettronico();

  }

  private void sigilla(List<CosmoTDocumento> documenti, String alias, Long idSigillo) {
    String method = "sigilla";


    documenti.stream().forEach(documento -> {

      var docDaAggiornare = documento.getCosmoRSigilloDocumentos().stream().filter(rsd -> rsd.getCosmoTSigilloElettronico().getId().equals(idSigillo)).findFirst().orElseThrow();

      var result = inTransaction(() -> aggiornaStatoSigilloWorkInProgress(docDaAggiornare));

      var resultRichiestaSigillo = inTransaction(() -> sigillaDocumento(documento, alias));
      final EsitoRichiestaSigilloElettronicoDocumento esitoRichiestaApposizioneSigillo;
      if (resultRichiestaSigillo.failed()) {
        logger.error(method, String.format(
            "Errore durante la richiesta di apposizione sigillo elettronico del documento con id %d",
            documento.getId()));
        esitoRichiestaApposizioneSigillo = new EsitoRichiestaSigilloElettronicoDocumento(resultRichiestaSigillo.getResult().getCodice(),
            "Errore durante la richiesta di apposizione sigillo elettronico");
      } else {
        logger.info(method,
            String.format("Codice risposta: %s Messaggio: %s",
                resultRichiestaSigillo.getResult().getCodice(),
                resultRichiestaSigillo.getResult().getMessaggio()));
        esitoRichiestaApposizioneSigillo = resultRichiestaSigillo.getResult();
      }

      var resultSalvataggioEsitoRichiestaApposizioneSigillo = inTransaction(
          () -> salvaEsitoRichiestaApposizioneSigillo(esitoRichiestaApposizioneSigillo, documento, idSigillo));

      if (resultSalvataggioEsitoRichiestaApposizioneSigillo.failed()) {
        logger.error(method, String.format(
            "Errore durante il salvataggio dell'esito richiesta apposizione sigillo del documento con id %d",
            documento.getId()));
      } else {
        logger.beginForClass(method, String.format(
            "Richiesta apposizione sigillo del documento con id %d completata", documento.getId()));
      }
    });


  }

  private EsitoRichiestaSigilloElettronicoDocumento sigillaDocumento(CosmoTDocumento documento, String alias) {
    return firmaService.apponiSigilloElettronico(documento, alias);
  }

  private void salvaEsitoRichiestaApposizioneSigillo(
      EsitoRichiestaSigilloElettronicoDocumento esitoRichiestaApposizioneSigillo,
      CosmoTDocumento documento, Long idSigillo) {
    sigilloElettronicoService.salvaEsitoRichiestaApposizioneSigillo(esitoRichiestaApposizioneSigillo, documento, idSigillo);
  }


  private void notificaDocumentiSigillati(Map<Long, Set<Long>> mappedDocuments) {
    final var method = "notificaDocumentiSigillati";

    WebSocketTargetSelector target = new WebSocketTargetSelector();
    try {
      mappedDocuments.keySet().forEach(key -> mappedDocuments.get(key).forEach(idPratica -> {

        Map<String, Object> payload = new HashMap<>();
        payload.put("idPratica", idPratica);
        target.setIdEnte(key);
        eventService.broadcastEvent(Constants.EVENTS.DOCUMENTI_SIGILLATI, payload, target);

        logger.info(method,
            String.format("Notificato apposizione sigillo elettronico all'ente %s, pratica %d", key, idPratica));
      }));

    } catch (Exception e) {
      logger.error(method, "Errore durante la notifica dei documenti sigillati", e);
    }
  }

  private Map<Long, Set<Long>> apponiSigilloElettronico() {
  //@formatter:off
    return this.lockService.executeLocking(this::apponiSigilloElettronicoInsideLock,
        LockAcquisitionRequest.builder()
        .withCodiceRisorsa(JOB_LOCK_RESOURCE_CODE)
        .withRitardoRetry(500L)
        .withTimeout(2000L)
        .withDurata(5 * 60 * 1000L)
        .build()
        );
    //@formatter:on

  }

  private Map<Long, Set<Long>> apponiSigilloElettronicoInsideLock(CosmoTLock lock) {

    String method = "apponiSigilloElettronicoInsideLock";

    if (lock == null) {
      throw new ConflictException(
          "Non e' consentito avviare la procedura di apposizione sigillo senza un lock sulla risorsa "
              + JOB_LOCK_RESOURCE_CODE);
    }

    if (lock.cancellato()
        || (lock.getDtScadenza() != null && lock.getDtScadenza().before(new Date()))) {
      throw new ConflictException(
          "Non e' consentito avviare la procedura di apposizione sigillo con un lock sulla risorsa "
              + JOB_LOCK_RESOURCE_CODE + " gia' scaduto");
    }

    List<CosmoTDocumento> documentiDaSigillare =
        sigilloElettronicoService.recuperaDocumentiDaSigillare();

    Map<Long, List<CosmoTDocumento>> docsGroupedById =
        documentiDaSigillare.stream().collect(Collectors.groupingBy(doc -> doc.getPratica().getId()));

    logger.info(method,
        String.format("Sono presenti %d pratiche e un totale di %d documenti da sigillare",
            docsGroupedById.size(), documentiDaSigillare.size()));

    docsGroupedById.entrySet().forEach(es -> {
      logger.info(method, String.format("pratica con id %d: Inizio eleborazione di %d documenti",
          es.getKey(), es.getValue().size()));

      var pratica = cosmoTPraticaRepository.findOne(es.getKey());

      var sigilloDaApporre = cosmoTSigilloElettronicoRepository.findOneNotDeleted((Root<CosmoTSigilloElettronico> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
        return cb.and(
            cb.equal(root.get(CosmoTSigilloElettronico_.cosmoTPratica), pratica),
            cb.equal(root.get(CosmoTSigilloElettronico_.utilizzato), Boolean.FALSE));
      });


      this.sigilla(es.getValue(), sigilloDaApporre.get().getIdentificativoAlias(), sigilloDaApporre.get().getId());

      checkApposizioneSigilli(es.getKey(), es.getValue(), sigilloDaApporre.get().getIdentificativoEvento(), sigilloDaApporre.get().getId());

    });


    Map<Long, Set<Long>> mappedDocuments =
        documentiDaSigillare.stream()
        .collect(Collectors.toMap(sigilloElettronicoService::recuperaIdEnteDaDocumento,
            sigilloElettronicoService::recuperaIdPraticaDaDocumento, (existing, replacement) -> {
              existing.addAll(replacement);
              return existing;
            }));



    this.notificaDocumentiSigillati(mappedDocuments);
    return mappedDocuments;
  }

  private void checkApposizioneSigilli(Long idPratica, List<CosmoTDocumento> documenti, String identificativoOk, Long idSigillo) {
    var res =
        inTransaction(() -> sigilloElettronicoService.checkApposizioneSigilloDocumenti(documenti, idSigillo));

    var identificativoEvento = identificativoOk;
    if (!res.getResult().booleanValue()) {
      identificativoEvento = SIGILLATO_KO_EVENT;
    }
    avanzaProcesso(idPratica, identificativoEvento);

  }


  private void aggiornaStatoSigilloWorkInProgress(CosmoRSigilloDocumento sigilloDocumento) {

    sigilloElettronicoService.aggiornaStatoSigilloWip(sigilloDocumento);

  }

  private void avanzaProcesso(Long idPratica, String identificativoEvento) {

    AvanzamentoProcessoRequest apr = new AvanzamentoProcessoRequest();
    apr.setIdentificativoEvento(identificativoEvento);
    apr.setIdPratica(idPratica);

    inTransaction(() -> businessProcessoClient.postAvanzaProcesso(apr));


  }


}

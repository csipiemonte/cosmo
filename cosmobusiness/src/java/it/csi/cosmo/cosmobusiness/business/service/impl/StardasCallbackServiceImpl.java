/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.util.ArrayList;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.flowable.rest.service.api.runtime.process.ExecutionActionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.CosmoTSmistamento_;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.feignclient.exception.FeignClientServerErrorException;
import it.csi.cosmo.common.feignclient.exception.FeignClientStatusCodeException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmobusiness.business.service.StardasCallbackService;
import it.csi.cosmo.cosmobusiness.dto.rest.EsitoSmistaDocumentoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.EsitoSmistaDocumentoResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.InformazioneType;
import it.csi.cosmo.cosmobusiness.dto.rest.ResultType;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTSmistamentoRepository;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoEcmDocumentiFeignClient;
import it.csi.cosmo.cosmobusiness.util.CommonUtils;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;
import it.csi.cosmo.cosmoecm.dto.rest.Esito;
import it.csi.cosmo.cosmoecm.dto.rest.InformazioneAggiuntivaSmistamento;

/**
 *
 */
@Service
public class StardasCallbackServiceImpl implements StardasCallbackService {

  private static final String ESITO_OK = "000";
  private static final String REGEX_CODICE_ESITO_WARNING = "([0][0][2-9]|[0][1-9][0-9])";
  private static final String MESSAGGIO_OK = "Smistamento completato con successo";
  private static final String REQUEST = "request esito";
  private static final String MESSAGE_EVENT_RECEIVED_ACTION = "messageEventReceived";

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.MESSAGING_LOG_CATEGORY, "CallbackServiceImpl");

  @Autowired
  private CosmoEcmDocumentiFeignClient ecmClient;

  @Autowired
  private CosmoCmmnFeignClient cmmnClient;

  @Autowired
  private CosmoTSmistamentoRepository cosmoTSmistamentoRepository;

  /*
   * Inserimento dell'esito di smistamento (prima callback)
   *
   * @see
   * it.csi.cosmo.cosmobusiness.business.service.CallbackService#inserisciEsitoSmistaDocumento(it.
   * csi.cosmo.cosmobusiness.dto.rest.EsitoSmistaDocumentoRequest)
   */
  @Override
  public EsitoSmistaDocumentoResponse inserisciEsitoSmistaDocumento(
      EsitoSmistaDocumentoRequest request) {
    String methodName = "inserisciEsitoSmistaDocumento";
    // validazione dell'input
    var ecmRequest =
        checkInputAndBuildEcmRequest(request);

    var response = new EsitoSmistaDocumentoResponse();

    try {
      var resultType = new ResultType();
      // resultType.setCodice(ESITO_KO_GENERICO);
      // resultType.setMessaggio("Errore durante l'applicazione dell'esito dello smistamento");
      resultType.setCodice(ESITO_OK);
      resultType.setMessaggio(MESSAGGIO_OK);
      response.setEsito(resultType);

      // contatto ecm per la scrittura su db
      var ecmResponse = ecmClient.postDocumentiIdDocumentoSmistamento(
          request.getEsitoSmistaDocumento().getEsito().getIdDocumentoFruitore(), ecmRequest);

      if (null != ecmResponse && ecmResponse.getEsito() != null) {
        logger.debug(methodName, "codice: " + ecmResponse.getEsito().getCode());
        logger.debug(methodName,
            "getNumDocumentiDaSmistare: " + ecmResponse.getNumDocumentiDaSmistare());
        logger.debug(methodName, "getNumDocumentiSmistatiCorrettamente: "
            + ecmResponse.getNumDocumentiSmistatiCorrettamente());
        logger.debug(methodName,
            "getNumDocumentiSmistatiInErrore: " + ecmResponse.getNumDocumentiSmistatiInErrore());
        logger.debug(methodName,
            "getIdentificativoEvento: " + ecmResponse.getIdentificativoEvento());

        if ((ESITO_OK.equals(ecmResponse.getEsito().getCode()) || ecmResponse.getEsito().getCode().matches(REGEX_CODICE_ESITO_WARNING))
            && null != ecmResponse.getNumDocumentiDaSmistare()
            && ecmResponse.getNumDocumentiDaSmistare() > 0
            && ecmResponse.getNumDocumentiDaSmistare()
            .equals(ecmResponse.getNumDocumentiSmistatiCorrettamente())
            && 0 == ecmResponse.getNumDocumentiSmistatiInErrore()) {

          // se tutti i documenti sono stati smistati correttamente, invio il segnale di avanzamento
          // al processo
          avanzaProcesso(ecmResponse.getIdPratica(), ecmResponse.getIdentificativoEvento());
          var smistamenti = cosmoTSmistamentoRepository.findAll((root, cq,
              cb) -> cq.where(cb.and(cb.equal(root.get(CosmoTSmistamento_.utilizzato), false),
                  cb.equal(root.get(CosmoTSmistamento_.cosmoTPratica).get(CosmoTPratica_.id),
                      ecmResponse.getIdPratica()),
                  cb.equal(root.get(CosmoTSmistamento_.identificativoEvento),
                      ecmResponse.getIdentificativoEvento())))
              .getRestriction());
          if (smistamenti != null && !smistamenti.isEmpty()) {
            var smistamento = smistamenti.get(0);
            smistamento.setUtilizzato(true);
            cosmoTSmistamentoRepository.save(smistamento);
          }
        }
        response.getEsito().setCodice(ecmResponse.getEsito().getCode());
        response.getEsito().setMessaggio(ecmResponse.getEsito().getTitle());
      }
    } catch (FeignClientStatusCodeException | FeignClientServerErrorException e) {
      logger.error("inserisciEsitoSmistaDocumento - post ", e.getMessage());
      throw e;
    }


    return response;
  }

  /*
   * Aggiornamento dell'esito di smistamento (dalla seconda callback in poi)
   *
   * @see
   * it.csi.cosmo.cosmobusiness.business.service.CallbackService#inserisciEsitoSmistaDocumento(it.
   * csi.cosmo.cosmobusiness.dto.rest.EsitoSmistaDocumentoRequest)
   */
  @Override
  public EsitoSmistaDocumentoResponse aggiornaEsitoSmistaDocumento(
      EsitoSmistaDocumentoRequest request) {

    // validazione dell'input
    var ecmRequest = checkInputAndBuildEcmRequest(request);

    var response = new EsitoSmistaDocumentoResponse();

    try {
      var resultType = new ResultType();
      // resultType.setCodice(ESITO_KO_GENERICO);
      // resultType.setMessaggio("Errore durante l'applicazione dell'esito dello smistamento");
      resultType.setCodice(ESITO_OK);
      resultType.setMessaggio(MESSAGGIO_OK);
      response.setEsito(resultType);

      // contatto ecm per la scrittura su db
      var ecmResponse = ecmClient.putDocumentiIdDocumentoSmistamento(
          request.getEsitoSmistaDocumento().getEsito().getIdDocumentoFruitore(), ecmRequest);

      if (null != ecmResponse && ecmResponse.getEsito() != null) {
        if ((ESITO_OK.equals(ecmResponse.getEsito().getCode()) || ecmResponse.getEsito().getCode().matches(REGEX_CODICE_ESITO_WARNING))
            && null != ecmResponse.getNumDocumentiDaSmistare()
            && ecmResponse.getNumDocumentiDaSmistare() > 0
            && ecmResponse.getNumDocumentiDaSmistare()
            .equals(ecmResponse.getNumDocumentiSmistatiCorrettamente())
            && 0 == ecmResponse.getNumDocumentiSmistatiInErrore()) {
          // se tutti i documenti sono stati smistati correttamente, invio il segnale di avanzamento
          // al processo
          avanzaProcesso(ecmResponse.getIdPratica(), ecmResponse.getIdentificativoEvento());
          var smistamenti =
              cosmoTSmistamentoRepository
                  .findAll(
                      (root, cq,
                          cb) -> cq
                              .where(
                                  cb.and(cb.equal(root.get(CosmoTSmistamento_.utilizzato), false),
                                      cb.equal(root.get(CosmoTSmistamento_.cosmoTPratica)
                                          .get(CosmoTPratica_.id), ecmResponse.getIdPratica()),
                                      cb.equal(root.get(CosmoTSmistamento_.identificativoEvento),
                                          ecmResponse.getIdentificativoEvento())))
                              .getRestriction());
          if (smistamenti != null && !smistamenti.isEmpty()) {
            var smistamento = smistamenti.get(0);
            smistamento.setUtilizzato(true);
            cosmoTSmistamentoRepository.save(smistamento);
          }

        }
        response.getEsito().setCodice(ecmResponse.getEsito().getCode());
        response.getEsito().setMessaggio(ecmResponse.getEsito().getTitle());
      }
    } catch (FeignClientStatusCodeException | FeignClientServerErrorException e) {
      logger.error("inserisciEsitoSmistaDocumento - put ", e.getMessage());
      throw e;
    }
    return response;
  }

  private it.csi.cosmo.cosmoecm.dto.rest.EsitoSmistaDocumentoRequest checkInputAndBuildEcmRequest(
      EsitoSmistaDocumentoRequest request) {
    String methodName = "checkInputAndBuildEcmRequest";
    logger.info(methodName, ObjectUtils.represent(request));
    var ecmRequest = new it.csi.cosmo.cosmoecm.dto.rest.EsitoSmistaDocumentoRequest();
    ecmRequest.setEsito(new ArrayList<>());
    CommonUtils.require(request, REQUEST);
    CommonUtils.require(request.getEsitoSmistaDocumento(), REQUEST);
    CommonUtils.require(request.getEsitoSmistaDocumento().getEsito(), REQUEST);
    CommonUtils.require(request.getEsitoSmistaDocumento().getEsito().getIdDocumentoFruitore(),
        "id documento fruitore");
    // CommonUtils.require(request.getEsitoSmistaDocumento().getEsito().getEsitiStep(), "esiti
    // step");
    // CommonUtils.require(request.getEsitoSmistaDocumento().getEsito().getEsitiStep().getEsitoStep(),
    // "esiti step");
    CommonUtils.require(request.getEsitoSmistaDocumento().getEsito().getEsitoTrattamento(),
        "esito trattamento");

    // var esitiStep = request.getEsitoSmistaDocumento().getEsito().getEsitiStep().getEsitoStep();
    // if (esitiStep.isEmpty()) {
    // String errore = String.format(
    // "Esiti step non valorizzati all'interno della callback relativa al documento con id %s",
    // request.getEsitoSmistaDocumento().getEsito().getIdDocumentoFruitore());
    // logger.error(methodName, errore);
    // throw new InternalServerException(errore);
    // }

    var esiti = new ArrayList<Esito>();
    var esito = new Esito();
    esito.setCode(ESITO_OK);
    esito.setTitle(MESSAGGIO_OK);
    if (null != request.getEsitoSmistaDocumento().getEsito().getEsitoTrattamento().getCodice()) {
      esito.setCode(request.getEsitoSmistaDocumento().getEsito().getEsitoTrattamento().getCodice());
      esito.setTitle(
          request.getEsitoSmistaDocumento().getEsito().getEsitoTrattamento().getMessaggio());
    }
    esiti.add(esito);
    //    List<EsitoStep> esitiKO = esitiStep.stream().filter(esitoStep -> esitoStep.getEsito() != null
    //        && !ESITO_OK.equals(esitoStep.getEsito().getCodice())).collect(Collectors.toList());
    //
    //    if (esitiKO == null || esitiKO.isEmpty()) {
    //      Esito esito = new Esito();
    //      esito.setCode(ESITO_OK);
    //      esito.setTitle(MESSAGGIO_OK);
    //      esiti.add(esito);
    //    } else {
    //      Esito esito = new Esito();
    //      esito.setCode(esitiKO.stream().findFirst().orElseThrow().getEsito().getCodice());
    //      esito.setTitle(esitiKO.stream().findFirst().orElseThrow().getEsito().getMessaggio());
    //      esiti.add(esito);
    //    }


    ecmRequest.setEsito(esiti);
    ecmRequest.setMessageUUID(request.getEsitoSmistaDocumento().getEsito().getMessageUUID());
    ecmRequest
    .setTipoTrattamento(request.getEsitoSmistaDocumento().getEsito().getTipoTrattamento());

    var infoAggiuntive = request.getEsitoSmistaDocumento().getEsito().getInformazioniAggiuntive();
    if (null != infoAggiuntive) {
      ecmRequest.setInformazioniAggiuntive(infoAggiuntive.getInformazione().stream()
          .map(this::getInfoAggiuntive).collect(Collectors.toList()));
    }
    return ecmRequest;
  }

  private InformazioneAggiuntivaSmistamento getInfoAggiuntive(InformazioneType informazioni) {
    InformazioneAggiuntivaSmistamento infoAggiuntive = new InformazioneAggiuntivaSmistamento();

    if (null != informazioni) {
      infoAggiuntive.setCodInformazione(informazioni.getNome());
      infoAggiuntive.setValore(informazioni.getValore());
    } else {
      infoAggiuntive.setCodInformazione("Codice informazione assente");
      infoAggiuntive.setValore("Valore informazione assente");
    }

    return infoAggiuntive;
  }

  private void avanzaProcesso(String idPratica, String identificativoEvento) {
    String methodName = "avanzaProcesso";
    CommonUtils.require(idPratica, "idPratica");
    if (null == identificativoEvento || StringUtils.isBlank(identificativoEvento)) {
      String errore = "identificativo evento non trovato";
      logger.error(methodName, errore);
      throw new InternalServerException(errore);
    }

    // ricerco il processo con businessKey=idPratica
    var processInstanceWrapper = cmmnClient.getProcessInstancesByBusinessKey(idPratica);

    if (null != processInstanceWrapper && null != processInstanceWrapper.getData()) {
      if (processInstanceWrapper.getTotal() != 1) {
        String errore = String.format(
            "Sono stati trovati %d processi per l'id pratica %s. Deve esisterne soltanto 1 attivo",
            processInstanceWrapper.getTotal(), idPratica);
        logger.error(methodName, errore);
        throw new InternalServerException(errore);
      } else {
        // deve esistere solo un processo attivo per ogni pratica
        String processInstanceId =
            processInstanceWrapper.getData().stream().findFirst().orElseThrow().getId();

        // ricerco la execution afferente al processo
        var executionWrapper = cmmnClient.getExecutions(identificativoEvento, processInstanceId);

        if (null == executionWrapper || executionWrapper.getData() == null) {
          String errore = String.format(
              "Nessuna execution esistente per il processo con id %s",
              processInstanceId);
          logger.error(methodName, errore);
          throw new InternalServerException(errore);
        }
        // if (executionWrapper.getTotal() != 1) {
        // String errore = String.format(
        // "Sono state trovate %d execution per il process instance %s. Deve esisterne soltanto 1
        // attiva",
        // executionWrapper.getTotal(), processInstanceId);
        // logger.error(methodName, errore);
        // throw new InternalServerException(errore);
        // }

        // invio il messaggio al processo con EVENT = identificativoEvento
        var executionActionRequest = new ExecutionActionRequest();
        executionActionRequest.setAction(MESSAGE_EVENT_RECEIVED_ACTION);
        executionActionRequest.setMessageName(identificativoEvento);

        executionWrapper.getData().stream().forEach(execution -> {
          cmmnClient.putExecution(execution.getId(), executionActionRequest);
        });

        logger.info(
            methodName,
            String.format("Segnale %s correttamente inviato al processo relativo alla pratica %s",
                identificativoEvento, idPratica));
      }
    } else {
      String errore =
          String.format("Nessun processo esistente per la pratica con id %s", idPratica);
      logger.error(methodName, errore);
      throw new InternalServerException(errore);
    }
  }

}

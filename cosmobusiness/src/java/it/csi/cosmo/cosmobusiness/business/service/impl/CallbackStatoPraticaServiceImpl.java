/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.enums.OperazioneFruitore;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.business.service.CallbackService;
import it.csi.cosmo.cosmobusiness.business.service.CallbackStatoPraticaService;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaStatoPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.GetPraticaFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaCallbackStatoPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InvioCallbackResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.SchedulaCallbackStatoPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.SchedulazioneCallbackResponse;
import it.csi.cosmo.cosmobusiness.integration.mapper.CallbackFruitoriMapper;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;


@Service
@Transactional
public class CallbackStatoPraticaServiceImpl extends StatoPraticaWrapper implements CallbackStatoPraticaService {

  /**
   *
   */
  public static final String FIELD_ID_PRATICA = "idPratica";
  public static final String FIELD_ID_PRATICA_EXT = "idPraticaExt";

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CallbackFruitoriMapper callbackFruitoriMapper;

  @Autowired
  private CallbackService callbackService;

  @Override
  public GetPraticaFruitoreResponse getStatoPratica(Long idPratica) {
    final var method = "getStatoPratica";
    logger.debug(method, "richiesto stato pratica {}", idPratica);

    // verifico i dati di input
    ValidationUtils.require(idPratica, FIELD_ID_PRATICA);

    // costruisco il payload
    AggiornaStatoPraticaRequest stato = getStatoPraticaPerCallback(idPratica);

    return callbackFruitoriMapper.toGetPraticaFruitoreResponse(stato);
  }

  @Override
  public AggiornaStatoPraticaRequest getStatoPraticaPerCallback(Long idPratica) {
    final var method = "getStatoPraticaPerCallback";
    logger.debug(method, "richiesto stato pratica {}", idPratica);

    // verifico i dati di input
    ValidationUtils.require(idPratica, FIELD_ID_PRATICA);

    // ottengo il contesto
    ContestoInvioCallback contesto = getContestoInvio(idPratica, false);

    // costruisco il payload
    return toAggiornaStatoPraticaRequest(contesto.pratica);
  }

  @Override
  public InvioCallbackResponse postCallbackStatoPraticaInvia(
      InviaCallbackStatoPraticaRequest body) {
    final var method = "postCallbackStatoPraticaInvia";

    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    logger.debug(method, "richiesto invio stato pratica {}", body.getIdPratica());

    // ottengo il contesto
    ContestoInvioCallback contesto = getContestoInvio(body.getIdPratica(), true);

    // costruisco il payload
    AggiornaStatoPraticaRequest payload =
        toAggiornaStatoPraticaRequest(contesto.pratica);

    if (logger.isDebugEnabled()) {
      logger.info(method, "invio stato pratica {} al fruitore {} con payload {}", contesto.pratica,
          contesto.fruitore, ObjectUtils.represent(payload));
    }

    // invio il callback
    Map<String, Object> parametri = new HashMap<>();
    parametri.put(FIELD_ID_PRATICA, contesto.pratica.getId());
    parametri.put(FIELD_ID_PRATICA_EXT, contesto.pratica.getIdPraticaExt());

    return callbackService.inviaSincrono(OperazioneFruitore.RICEZIONE_STATO_PRATICA,
        contesto.fruitore.getId(), payload, parametri, body.getCodiceSegnale());
  }

  @Override
  public SchedulazioneCallbackResponse postCallbackStatoPraticaSchedula(
      SchedulaCallbackStatoPraticaRequest body) {
    final var method = "postCallbackStatoPraticaSchedula";

    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    logger.debug(method, "richiesta schedulazione invio stato pratica {}", body.getIdPratica());

    // ottengo il contesto
    ContestoInvioCallback contesto = getContestoInvio(body.getIdPratica(), true);

    // costruisco il payload
    AggiornaStatoPraticaRequest payload =
        toAggiornaStatoPraticaRequest(contesto.pratica);

    if (logger.isDebugEnabled()) {
      logger.info(method, "schedulo invio stato pratica {} al fruitore {} con payload {}",
          contesto.pratica, contesto.fruitore, ObjectUtils.represent(payload));
    }

    Map<String, Object> parametri = new HashMap<>();
    parametri.put(FIELD_ID_PRATICA, contesto.pratica.getId());
    parametri.put(FIELD_ID_PRATICA_EXT, contesto.pratica.getIdPraticaExt());

    return callbackService.schedulaInvioAsincrono(OperazioneFruitore.RICEZIONE_STATO_PRATICA,
        contesto.fruitore.getId(), payload, parametri, body.getCodiceSegnale());
  }

  protected ContestoInvioCallback getContestoInvio(Long idPratica, boolean requireFruitore) {

    // verifico i dati di input
    ValidationUtils.require(idPratica, FIELD_ID_PRATICA);

    // recupero pratica e fruitore
    CosmoTPratica pratica = cosmoTPraticaRepository.findOneByField(CosmoTPratica_.id, idPratica)
        .orElseThrow(() -> new NotFoundException("Pratica non trovata"));

    CosmoTFruitore fruitore = Optional.ofNullable(pratica.getFruitore()).orElse(null);
    if (requireFruitore && fruitore == null) {
      throw new NotFoundException("Pratica non associata ad un fruitore");
    }

    ContestoInvioCallback output = new ContestoInvioCallback();
    output.pratica = pratica;
    output.fruitore = fruitore;
    return output;
  }

  protected AggiornaStatoPraticaRequest toAggiornaStatoPraticaRequest(CosmoTPratica pratica) {
    ValidationUtils.assertNotNull(pratica.getIdPraticaExt(), "IdPraticaExt");

    AggiornaStatoPraticaRequest payload = fetchPratica(pratica);

    // fetch attivita nell ordine desiderato
    payload.setAttivita(fetchAttivita(pratica));

    // fetch documenti nell ordine desiderato
    payload.setDocumenti(fetchDocumenti(pratica));

    // fetch dei commenti sulla pratica
    payload.setCommenti(fetchMessaggiPratica(pratica.getProcessInstanceId()));

    // arrichisco utenti che al momento hanno solo il codice fiscale con una singola query massiva
    enrichUtenti(payload);

    return payload;
  }

  private static class ContestoInvioCallback {
    CosmoTPratica pratica;
    CosmoTFruitore fruitore;
  }
}
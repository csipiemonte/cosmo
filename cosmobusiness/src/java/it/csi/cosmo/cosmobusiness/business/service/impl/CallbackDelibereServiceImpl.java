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
import it.csi.cosmo.cosmobusiness.business.service.CallbackDelibereService;
import it.csi.cosmo.cosmobusiness.business.service.CallbackService;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaCallbackAggiornamentoDeliberaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaCallbackCreazioneDeliberaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InvioCallbackResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RegaxDelibere;
import it.csi.cosmo.cosmobusiness.dto.rest.SchedulazioneCallbackResponse;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;


@Service
@Transactional
public class CallbackDelibereServiceImpl implements CallbackDelibereService {

  /**
   *
   */
  private static final String FIELD_ID_PRATICA = "idPratica";

  private static final String FIELD_KEY = "key";

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CallbackService callbackService;

  @Override
  public InvioCallbackResponse postCallbackCreazioneDeliberaInvia(
      InviaCallbackCreazioneDeliberaRequest body) {

    final var method = "postCallbackCreazioneDeliberaInvia";

    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    logger.debug(method, "richiesto invio CreazioneDelibera {}", body.getIdPratica());

    // ottengo il contesto
    ContestoInvioCallback contesto = getContestoInvio(body.getIdPratica(), true);

    // costruisco il payload
    RegaxDelibere payload = buildPayloadCreazione(contesto.pratica);

    if (logger.isDebugEnabled()) {
      logger.info(method, "invio CreazioneDelibera {} al fruitore {} con payload {}",
          contesto.pratica,
          contesto.fruitore, ObjectUtils.represent(payload));
    }

    // invio il callback
    Map<String, Object> parametri = new HashMap<>();
    parametri.put(FIELD_ID_PRATICA, body.getIdPratica());

    return callbackService.inviaSincrono(OperazioneFruitore.CREAZIONE_DELIBERA,
        contesto.fruitore.getId(), payload, parametri,
        OperazioneFruitore.CREAZIONE_DELIBERA.name());
  }

  @Override
  public SchedulazioneCallbackResponse postCallbackCreazioneDeliberaSchedula(
      InviaCallbackCreazioneDeliberaRequest body) {

    final var method = "postCallbackCreazioneDeliberaSchedula";

    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    logger.debug(method, "richiesta schedulazione invio CreazioneDelibera {}", body.getIdPratica());

    // ottengo il contesto
    ContestoInvioCallback contesto = getContestoInvio(body.getIdPratica(), true);

    // costruisco il payload
    RegaxDelibere payload = buildPayloadCreazione(contesto.pratica);

    if (logger.isDebugEnabled()) {
      logger.info(method, "schedulo invio CreazioneDelibera {} al fruitore {} con payload {}",
          contesto.pratica, contesto.fruitore, ObjectUtils.represent(payload));
    }

    Map<String, Object> parametri = new HashMap<>();
    parametri.put(FIELD_ID_PRATICA, body.getIdPratica());

    return callbackService.schedulaInvioAsincrono(OperazioneFruitore.CREAZIONE_DELIBERA,
        contesto.fruitore.getId(), payload, parametri,
        OperazioneFruitore.CREAZIONE_DELIBERA.name());
  }

  @Override
  public InvioCallbackResponse postCallbackAggiornamentoDeliberaInvia(
      InviaCallbackAggiornamentoDeliberaRequest body) {
    final var method = "postCallbackAggiornamentoDeliberaInvia";

    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    logger.debug(method, "richiesto invio AggiornamentoDelibera {}", body.getIdPratica());

    // ottengo il contesto
    ContestoInvioCallback contesto = getContestoInvio(body.getIdPratica(), true);

    // costruisco il payload
    Map<String, Object> payload = new HashMap<>();
    payload.put("id", contesto.pratica.getId());
    payload.put("oggetto", contesto.pratica.getOggetto());
    payload.put(FIELD_KEY, body.getKey());

    if (logger.isDebugEnabled()) {
      logger.info(method, "invio CreazioneDelibera {} al fruitore {} con payload {}",
          contesto.pratica, contesto.fruitore, ObjectUtils.represent(payload));
    }

    // invio il callback
    Map<String, Object> parametri = new HashMap<>();
    parametri.put(FIELD_ID_PRATICA, body.getIdPratica());
    parametri.put(FIELD_KEY, body.getKey());

    return callbackService.inviaSincrono(OperazioneFruitore.AGGIORNAMENTO_DELIBERA,
        contesto.fruitore.getId(), payload, parametri,
        OperazioneFruitore.AGGIORNAMENTO_DELIBERA.name());
  }

  @Override
  public SchedulazioneCallbackResponse postCallbackAggiornamentoDeliberaSchedula(
      InviaCallbackAggiornamentoDeliberaRequest body) {

    final var method = "postCallbackAggiornamentoDeliberaSchedula";

    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    logger.debug(method, "richiesta schedulazione invio AggiornamentoDelibera {}",
        body.getIdPratica());

    // ottengo il contesto
    ContestoInvioCallback contesto = getContestoInvio(body.getIdPratica(), true);

    // costruisco il payload
    Map<String, Object> payload = new HashMap<>();
    payload.put("id", contesto.pratica.getId());
    payload.put("oggetto", contesto.pratica.getOggetto());
    payload.put(FIELD_KEY, body.getKey());

    if (logger.isDebugEnabled()) {
      logger.info(method, "schedulo invio AggiornamentoDelibera {} al fruitore {} con payload {}",
          contesto.pratica, contesto.fruitore, ObjectUtils.represent(payload));
    }

    Map<String, Object> parametri = new HashMap<>();
    parametri.put(FIELD_ID_PRATICA, body.getIdPratica());
    parametri.put(FIELD_KEY, body.getKey());

    return callbackService.schedulaInvioAsincrono(OperazioneFruitore.AGGIORNAMENTO_DELIBERA,
        contesto.fruitore.getId(), payload, parametri,
        OperazioneFruitore.AGGIORNAMENTO_DELIBERA.name());
  }

  private RegaxDelibere buildPayloadCreazione(CosmoTPratica pratica) {
    RegaxDelibere output = new RegaxDelibere();

    output.setDelOggetto(pratica.getOggetto());
    output.setContdelContesto(1L);

    return output;
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

  private static class ContestoInvioCallback {
    CosmoTPratica pratica;
    CosmoTFruitore fruitore;
  }

}


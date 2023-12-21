/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.async.model.LongTask.LongTaskStatus;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTIstanzaFunzionalitaFormLogico;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.business.service.AsyncTaskService;
import it.csi.cosmo.cosmobusiness.business.service.AttivazioneSistemaEsternoService;
import it.csi.cosmo.cosmobusiness.business.service.ChiamataEsternaService;
import it.csi.cosmo.cosmobusiness.business.service.IstanzaFormLogiciService;
import it.csi.cosmo.cosmobusiness.business.service.MotoreJsonDinamicoService;
import it.csi.cosmo.cosmobusiness.business.service.StatoPraticaService;
import it.csi.cosmo.cosmobusiness.dto.ChiamataEsternaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.GetPayloadAttivazioneSistemaEsternoResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.SandboxFactory;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmobusiness.security.SecurityUtils;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;

@Service
@Transactional
public class AttivazioneSistemaEsternoServiceImpl implements AttivazioneSistemaEsternoService {

  private final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  private static final String CODICE_FUNZIONALITA = "ATTIVA-SISTEMA-ESTERNO";
  private static final String CODICE_PARAMETRO_LINK_BUTTON = "LINK_BUTTON";
  private static final String CODICE_PARAMETRO_PAYLOAD_BUTTON = "PAYLOAD_BUTTON";
  private static final String CODICE_PARAMETRO_METODO_BUTTON = "METODO_BUTTON";

  @Autowired
  private MotoreJsonDinamicoService motoreJsonDinamicoService;

  @Autowired
  private AsyncTaskService asyncTaskService;

  @Autowired
  private StatoPraticaService statoPraticaService;

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private ChiamataEsternaService chiamataEsternaService;

  @Autowired
  private IstanzaFormLogiciService istanzaFormLogiciService;

  @Override
  public RiferimentoOperazioneAsincrona inviaAttivazioneSistemaEsterno(Long idPratica,
      Long idAttivita) {

    final var method = "inviaAttivazioneSistemaEsterno";
    logger.debug(method,
        "richiesto invio dati per attivazione sistema esterno per pratica {} e attivita {}",
        idPratica, idAttivita);

    final String taskName = "Invio dei dati al sistema esterno";

    var contestoAttivazione = getContestoAttivazioneSistemaEsterno(idPratica, idAttivita);
    var contestoInvio = getContestoInvio(contestoAttivazione);

    var utente = SecurityUtils.getUtenteCorrente();

    var operazioneAsincrona = asyncTaskService.start(taskName, task -> {
      logger.debug(method, "avvio step 1 task di invio dei dati");

      logger.debug(method, "avvio step 2 task di invio dei dati");

      // invia i dati
      var responseBodyString = task.step("Invio dei dati", step -> {

        var response = eseguiInvio(contestoAttivazione, contestoInvio, utente);
        var responseBodyBytes = response.getBody();
        if (responseBodyBytes.length > 0) {
          return new String(responseBodyBytes, StandardCharsets.UTF_8);
        }
        return "";
      });

      logger.debug(method, "fine task di invio dei dati");


      return responseBodyString;
    });

    var output = new RiferimentoOperazioneAsincrona();
    output.setUuid(operazioneAsincrona.getTaskId());
    output.setNome(taskName);
    output.setStato(LongTaskStatus.STARTED.name());
    return output;
  }

  private ContestoInvioDatiAttivazioneSistemaEsterno getContestoInvio(
      ContestoAttivazioneSistemaEsterno contestoAttivazione) {

    var contestoInvio = new ContestoInvioDatiAttivazioneSistemaEsterno();

    contestoInvio.metodo =
        istanzaFormLogiciService.requireValoreParametro(contestoAttivazione.istanzaFunzionalita,
            CODICE_PARAMETRO_METODO_BUTTON).asString();

    contestoInvio.url = istanzaFormLogiciService.requireValoreParametro(
        contestoAttivazione.istanzaFunzionalita,
        CODICE_PARAMETRO_LINK_BUTTON).asString();
    if (!contestoInvio.url.startsWith("http")) {
      contestoInvio.url = "http://" + contestoInvio.url;
    }

    contestoInvio.payloadCompilato =
        buildPayloadAttivazioneSistemaEsterno(contestoAttivazione).toString();

    return contestoInvio;
  }

  private ResponseEntity<byte[]> eseguiInvio(ContestoAttivazioneSistemaEsterno contestoAttivazione,
      ContestoInvioDatiAttivazioneSistemaEsterno contestoInvio, UserInfoDTO utente) {

    final var method = "eseguiInvio";
    logger.debug(method, "eseguo invio dei dati in {} all'url {}", contestoInvio.metodo,
        contestoInvio.url);

    Map<String, Object> headers = new HashMap<>();

    if (Boolean.FALSE.equals(utente.getAnonimo())) {
      headers.put("X-User", utente.getCodiceFiscale());
    }
    if (utente.getEnte() != null && !StringUtils.isBlank(utente.getEnte().getTenantId())) {
      headers.put("X-Organization", utente.getEnte().getTenantId());
    }
    if (!StringUtils.isBlank(contestoAttivazione.pratica.getIdPraticaExt())) {
      headers.put("X-Pratica", contestoAttivazione.pratica.getIdPraticaExt());
    }

    //@formatter:off
    var requestChiamataEsterna = ChiamataEsternaRequest.<byte[]>builder(byte[].class)
        .withUrl(contestoInvio.url)
        .withMethod(contestoInvio.metodo)
        .withHeaders(headers)
        .withPayload(contestoInvio.payloadCompilato)
        .build();
    //@formatter:on

    try {
      return chiamataEsternaService.inviaChiamataEsterna(requestChiamataEsterna);
    } catch (Exception e) {
      logger.error(method, "errore nell'invio dei dati all'url " + contestoInvio.url, e);
      throw e;
    }
  }

  @Override
  public GetPayloadAttivazioneSistemaEsternoResponse getPayloadAttivazioneSistemaEsterno(
      Long idPratica, Long idAttivita) {

    final var method = "getPayloadAttivazioneSistemaEsterno";
    logger.debug(method,
        "richiesta costruzione payload per attivazione sistema esterno per pratica {} e attivita {}",
        idPratica, idAttivita);

    var contesto = getContestoAttivazioneSistemaEsterno(idPratica, idAttivita);

    JsonNode compiled = buildPayloadAttivazioneSistemaEsterno(contesto);

    GetPayloadAttivazioneSistemaEsternoResponse output =
        new GetPayloadAttivazioneSistemaEsternoResponse();

    output.setPayload(compiled);

    return output;
  }

  private JsonNode buildPayloadAttivazioneSistemaEsterno(
      ContestoAttivazioneSistemaEsterno contesto) {
    ValidationUtils.require(contesto, "contesto");

    final var method = "buildPayloadAttivazioneSistemaEsterno";
    logger.debug(method,
        "costruisco payload per attivazione sistema esterno per pratica {} e attivita {}",
        contesto.pratica.getId(), contesto.attivita.getId());

    // ottengo istanza
    CosmoTIstanzaFunzionalitaFormLogico istanza = contesto.istanzaFunzionalita;

    // leggo e verifico i parametri del form logico
    var specificaPayload =
        istanzaFormLogiciService.requireValoreParametro(istanza, CODICE_PARAMETRO_PAYLOAD_BUTTON)
        .asString();

    if (logger.isDebugEnabled()) {
      logger.debug(method, "costruisco specifica di trasformazione da parametro: {}",
          specificaPayload);
    }

    // creo sandbox
    var sandboxedVirtualNode =
        SandboxFactory.buildStatoPraticaSandbox(contesto.pratica, statoPraticaService);

    // applica la trasformazione
    JsonNode compiled =
        motoreJsonDinamicoService.eseguiMappatura(specificaPayload, sandboxedVirtualNode);

    if (logger.isDebugEnabled()) {
      logger.debug(method, "risultato trasformazione: {}", compiled.toString());
    }

    return compiled;
  }

  private ContestoAttivazioneSistemaEsterno getContestoAttivazioneSistemaEsterno(Long idPratica,
      Long idAttivita) {
    ValidationUtils.require(idPratica, "idPratica");
    ValidationUtils.require(idAttivita, "idAttivita");

    ContestoAttivazioneSistemaEsterno output = new ContestoAttivazioneSistemaEsterno();

    output.pratica = cosmoTPraticaRepository.findOneNotDeleted(idPratica)
        .orElseThrow(() -> new NotFoundException("Pratica non trovata"));

    output.attivita = output.pratica.getAttivita().stream().filter(CosmoTEntity::nonCancellato)
        .filter(a -> a.getId().equals(idAttivita)).findFirst()
        .orElseThrow(() -> new NotFoundException("Attivita' non trovata"));

    output.istanzaFunzionalita =
        istanzaFormLogiciService.ricercaIstanzaAttiva(output.attivita, CODICE_FUNZIONALITA);

    return output;
  }

  private class ContestoAttivazioneSistemaEsterno {
    CosmoTPratica pratica;
    CosmoTAttivita attivita;
    CosmoTIstanzaFunzionalitaFormLogico istanzaFunzionalita;
  }

  private class ContestoInvioDatiAttivazioneSistemaEsterno {
    String url;
    String metodo;
    String payloadCompilato;
  }

}

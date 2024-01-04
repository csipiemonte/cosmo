/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.business.service.impl;

import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.common.ServiceStatusDTO;
import it.csi.cosmo.common.dto.common.ServiceStatusEnum;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.monitoring.Monitorable;
import it.csi.cosmo.cosmosoap.business.service.Api2IndexService;
import it.csi.cosmo.cosmosoap.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmosoap.business.service.MailService;
import it.csi.cosmo.cosmosoap.business.service.StardasService;
import it.csi.cosmo.cosmosoap.config.ErrorMessages;
import it.csi.cosmo.cosmosoap.config.ParametriApplicativo;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.cosmo.cosmosoap.dto.rest.GetStatoRichiestaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.GetStatoRichiestaResponse;
import it.csi.cosmo.cosmosoap.dto.rest.SmistaDocumentoRequest;
import it.csi.cosmo.cosmosoap.dto.rest.SmistaDocumentoResponse;
import it.csi.cosmo.cosmosoap.dto.rest.StatoRichiesta;
import it.csi.cosmo.cosmosoap.facade.dto.common.EmbeddedBinaryType;
import it.csi.cosmo.cosmosoap.integration.mapper.StardasMapper;
import it.csi.cosmo.cosmosoap.integration.stardasService.service.StardasServiceProxyPortType;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;

/**
 *
 */
@Service
@Transactional
public class StardasServiceImpl implements StardasService, InitializingBean, Monitorable {

  StardasServiceProxyPortType stardasService;

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "StardasServiceImpl");

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  protected MailService mailService;

  @Autowired
  private Api2IndexService api2IndexService;

  private static final String NAMESPACE = "http://www.csi.it/stardas/wso2/StardasService";

  private static final QName QNAME_WS_SERVICE = new QName(NAMESPACE, "StardasServiceProxy");
  private static final QName QNAME_WS_PORT = new QName(NAMESPACE, "StardasServiceSOAP");
  private static final QName QNAME_WS_PORT_11 =
      new QName(NAMESPACE, "StardasServiceProxyHttpSoap11Endpoint");

  @Autowired
  private StardasMapper stardasMapper;

  @Override
  public void afterPropertiesSet() throws Exception {
    // NOP
  }

  /*
   * Istanziazione del service soap a partire dal WSDL (interno all'ear)
   */
  private StardasServiceProxyPortType getStardasService() {
    if (stardasService == null) {
      String endpointUrl =
          configurazioneService.requireConfig(ParametriApplicativo.STARDAS_URL).asString();
      logger.info("getStardasService", String
          .format("Verra' creato un nuovo Service corrispondente all'indirizzo %s", endpointUrl));
      javax.xml.ws.Service service;
      try {
        service = javax.xml.ws.Service
            .create(new ClassPathResource("/wsdl/StardasService.wsdl").getURL(), QNAME_WS_SERVICE);
        stardasService = service.getPort(QNAME_WS_PORT_11, StardasServiceProxyPortType.class);
        BindingProvider bp = (BindingProvider) stardasService;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);
      } catch (IOException e1) {
        logger.error("getStardasService", e1.getMessage());
        throw new InternalServerException(e1.getMessage());
      }
    }

    return stardasService;
  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  @Override
  public ServiceStatusDTO checkStatus() {

    return ServiceStatusDTO.of(() -> this.getStardasService() != null, ServiceStatusEnum.DOWN)
        .withName("Stardas client").withDetail("endpointUrl",
            configurazioneService.getConfig(ParametriApplicativo.STARDAS_URL).asString())
        .build();
  }

  @Override
  public SmistaDocumentoResponse smistaDocumento(SmistaDocumentoRequest requestSmistamento) {
    final var methodName = "smistaDocumento";

    var request = stardasMapper.toStardasDTO(requestSmistamento);

    // recupero del contenuto (contenuto fisico)
    Entity documentoIndex =
        api2IndexService.find(requestSmistamento.getDatiSmistaDocumento().getUuidNodo(), true);

    if (null == documentoIndex) {
      var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "documentoIndex");
      logger.error(methodName, parametroNonValorizzato);
      throw new InternalServerException(parametroNonValorizzato);
    }

    var contenutoBinario = new EmbeddedBinaryType();
    contenutoBinario.setContent(documentoIndex.getContent());
    request.getDatiSmistaDocumento().getDocumentoElettronico()
    .setContenutoBinario(contenutoBinario);
    
    try {
      logger.info(methodName, "Effettuo chiamata a stardas");
      var result = this.getStardasService().smistaDocumento(request);
      logger.info(methodName, String.format(
          "Richiesta smistamento del documento %s inviata a Stardas. UUID di risposta: %s",
          requestSmistamento.getDatiSmistaDocumento().getDocumentoElettronico().getNomeFile(),
          result.getMessageUUID()));
      logger.info(methodName, String.format("Codice risposta: %s%nMessaggio risposta: %s",
          result.getResult().getCodice(), result.getResult().getMessaggio()));

      return stardasMapper.toCosmoDTO(result);
    } catch (Exception e) {
      logger.error(methodName, e.getMessage(), e);
      throw new InternalServerException("Stardas error", e);
    }
  }

  @Override
  public GetStatoRichiestaResponse getStatoRichiesta(
      GetStatoRichiestaRequest statoRichiestaRequest) {
    final var methodName = "getStatoRichiesta";

    var request = stardasMapper.toStardasDTO(statoRichiestaRequest);

    try {
      var result = this.getStardasService().getStatoRichiesta(request);
      logger.info(methodName,
          String.format(
              "Esito dello stato della richiesta con UUID %s: %s",
              statoRichiestaRequest.getMessageUUID(), result.getDettaglioEsitoRichiesta()));

      logger.info(methodName, String.format("Codice risposta: %s%nMessaggio risposta: %s",
          result.getResult().getCodice(), result.getResult().getMessaggio()));

      StatoRichiesta statoRichiesta = new StatoRichiesta();
      statoRichiesta.setName(result.getStatoRichiesta().value());

      var response = stardasMapper.toCosmoDTO(result);
      response.setStatoRichiesta(statoRichiesta);

      return response;
    } catch (Exception e) {
      logger.error(methodName, e.getMessage());
      throw new InternalServerException("Stardas error", e);
    }
  }

}

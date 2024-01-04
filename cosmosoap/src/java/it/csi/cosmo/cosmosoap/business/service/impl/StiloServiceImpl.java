/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.business.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ws.rs.InternalServerErrorException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.MTOMFeature;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;
import it.csi.cosmo.common.dto.common.ServiceStatusDTO;
import it.csi.cosmo.common.dto.common.ServiceStatusEnum;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.enums.TipoContenutoDocumento;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.monitoring.Monitorable;
import it.csi.cosmo.cosmobe.dto.rest.AddUdRequest;
import it.csi.cosmo.cosmobe.dto.rest.AddUdResponse;
import it.csi.cosmo.cosmobe.dto.rest.StiloAllegatoRequest;
import it.csi.cosmo.cosmobe.dto.rest.UpUdRequest;
import it.csi.cosmo.cosmobe.dto.rest.UpUdResponse;
import it.csi.cosmo.cosmosoap.business.service.Api2IndexService;
import it.csi.cosmo.cosmosoap.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmosoap.business.service.FileShareService;
import it.csi.cosmo.cosmosoap.business.service.StiloService;
import it.csi.cosmo.cosmosoap.config.ErrorMessages;
import it.csi.cosmo.cosmosoap.config.ParametriApplicativo;
import it.csi.cosmo.cosmosoap.dto.rest.AggiornaUnitaDocumentariaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.cosmo.cosmosoap.integration.mapper.StiloMapper;
import it.csi.cosmo.cosmosoap.integration.repository.CosmoTDocumentoRepository;
import it.csi.cosmo.cosmosoap.integration.rest.CosmoBeStiloFeignClient;
import it.csi.cosmo.cosmosoap.integration.soap.stilo.handler.StiloHandler;
import it.csi.cosmo.cosmosoap.integration.soap.stilo.model.AddUdOutput;
import it.csi.cosmo.cosmosoap.integration.soap.stilo.model.StiloAllegato;
import it.csi.cosmo.cosmosoap.integration.soap.stilo.model.UpdUdOutput;
import it.csi.cosmo.cosmosoap.util.ContenutoException;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;
import it.eng.auriga.repository2.webservices.addunitadoc.WSAddUdService;
import it.eng.auriga.repository2.webservices.addunitadoc.WSIAddUd;
import it.eng.auriga.repository2.webservices.addunitadoc.dto.BaseOutputWS;
import it.eng.auriga.repository2.webservices.addunitadoc.dto.NewUD;
import it.eng.auriga.repository2.webservices.addunitadoc.dto.OutputUD;
import it.eng.auriga.repository2.webservices.updunitadoc.WSIUpdUd;
import it.eng.auriga.repository2.webservices.updunitadoc.WSUpdUdService;
import it.eng.auriga.repository2.webservices.updunitadoc.dto.UDDaAgg;

/**
 *
 */


@Service
@Transactional
public class StiloServiceImpl implements StiloService, InitializingBean, Monitorable {

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "StiloServiceImpl");


  private static final String ADD_NAMESPACE =
      "http://addunitadoc.webservices.repository2.auriga.eng.it";
  private static final String UPD_NAMESPACE =
      "http://updunitadoc.webservices.repository2.auriga.eng.it";

  private static final QName QNAME_WS_ADDSERVICE = new QName(ADD_NAMESPACE, "WSAddUdService");
  private static final QName QNAME_WS_UPDSERVICE = new QName(UPD_NAMESPACE, "WSUpdUdService");

  private static final String CONVERSIONE_ALLEGATI = "Problemi nella conversione degli allegati";
  private static final String MESSAGGIO_SOAP = "Problemi con il messaggio SOAP";
  private static final String CONVERSIONE_MESSAGGIO_SOAP =
      "Problemi con la conversione del messaggio SOAP";

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private FileShareService fileShareService;

  @Autowired
  private Api2IndexService api2IndexService;

  @Autowired
  private CosmoBeStiloFeignClient cosmoBeStiloFeignClient;

  @Autowired
  private CosmoTDocumentoRepository cosmoTDocumentoRepository;

  @Autowired
  StiloMapper stiloMapper;

  private WSIAddUd addUdService;

  private WSIUpdUd updUService;

  private WSIAddUd getAddUdService() {
    if (addUdService == null) {
      try {
        String endpointUrl =
            configurazioneService.requireConfig(ParametriApplicativo.STILO_ADDUD_URL).asString();

        if (!StringUtils.hasText(endpointUrl)) {
          logger.error("getAddUdService",
              String.format(ErrorMessages.FIRMA_NESSUNA_CONFIGURAZIONE_PER_LA_CHIAVE,
                  ParametriApplicativo.STILO_ADDUD_URL));
          throw new BadRequestException(
              String.format(ErrorMessages.FIRMA_NESSUNA_CONFIGURAZIONE_PER_LA_CHIAVE,
                  ParametriApplicativo.STILO_ADDUD_URL));
        }

        WSAddUdService locator =
            new WSAddUdService(new URL(endpointUrl), QNAME_WS_ADDSERVICE);
        addUdService = locator.getWSAddUdPort(new MTOMFeature());

      } catch (MalformedURLException e) {
        logger.error("getAddUdService", e.getMessage(), e);
        throw new BadRequestException(e.getMessage());
      }
    }
    return addUdService;
  }

  private WSIUpdUd getUpdUdService() {
    if (updUService == null) {
      try {
        String endpointUrl =
            configurazioneService.requireConfig(ParametriApplicativo.STILO_UPDUD_URL).asString();
        if (!StringUtils.hasText(endpointUrl)) {
          logger.error("getUpdUdService",
              String.format(ErrorMessages.FIRMA_NESSUNA_CONFIGURAZIONE_PER_LA_CHIAVE,
                  ParametriApplicativo.STILO_UPDUD_URL));
          throw new BadRequestException(
              String.format(ErrorMessages.FIRMA_NESSUNA_CONFIGURAZIONE_PER_LA_CHIAVE,
                  ParametriApplicativo.STILO_UPDUD_URL));
        }
        WSUpdUdService locator = new WSUpdUdService(new URL(endpointUrl), QNAME_WS_UPDSERVICE);

        updUService = locator.getWSUpdUdPort(new MTOMFeature());
      } catch (MalformedURLException e) {
        logger.error("getUpdUdService", e.getMessage(), e);
        throw new BadRequestException(e.getMessage());
      }
    }
    return updUService;
  }

  @Override
  public AddUdOutput addUd(NewUD newUD, List<StiloAllegato> allegati) {
    String methodName = "addUd";

    AddUdOutput addUdOutput = new AddUdOutput();
    boolean useCosmoBeProxy = configurazioneService
        .requireConfig(ParametriApplicativo.STILO_COSMOBEPROXY_ENABLED).asBoolean();

    String codApplicazione =
        configurazioneService.requireConfig(ParametriApplicativo.STILO_COD_APPLICAZIONE).asString();

    String istanzaApplicazione = configurazioneService
        .requireConfig(ParametriApplicativo.STILO_ISTANZA_APPLICAZIONE).asString();

    String userName =
        configurazioneService.requireConfig(ParametriApplicativo.STILO_USERNAME).asString();

    String password =
        configurazioneService.requireConfig(ParametriApplicativo.STILO_PASSWORD).asString();

    logger.debug(methodName, "codApplicazione = " + codApplicazione);
    logger.debug(methodName, "istanzaApplicazione = " + istanzaApplicazione);
    logger.debug(methodName, "userName = " + userName);
    logger.debug(methodName, "password = " + password);

    String xml;
    String hash;
    try {
      JAXBContext context = JAXBContext.newInstance(NewUD.class);
      Marshaller marshaller = context.createMarshaller();
      StringWriter out = new StringWriter();
      marshaller.marshal(newUD, out);
      xml = out.toString();
      hash = getHashFromXML(xml);
      logger.info(methodName, "xml = " + xml);
      logger.info(methodName, "hash = " + hash);

    } catch (JAXBException | NoSuchAlgorithmException e) {
      logger.error(methodName, CONVERSIONE_MESSAGGIO_SOAP, e);
      throw new InternalServerErrorException(CONVERSIONE_MESSAGGIO_SOAP, e);
    }

    if (useCosmoBeProxy) {

      AddUdResponse addUdResponse = addUdWithCosmoBeProxy(codApplicazione, istanzaApplicazione,
          userName, password, xml, hash, allegati);

      String result = addUdResponse.getServiceResponse();
      BaseOutputWS baseOutputWS = getBaseOutputWSFromString(result);
      addUdOutput.setBaseOutputWS(baseOutputWS);

      if (!baseOutputWS.getWSResult().equalsIgnoreCase("1")) {
        logger.error(methodName, MESSAGGIO_SOAP,
            baseOutputWS.getWSError().getErrorMessage());
      }
      else {
        StiloAllegato allegato = new StiloAllegato();
        BeanUtils.copyProperties(addUdResponse.getAllegato(), allegato);
        allegato.setContent(new SequenceInputStream(IteratorUtils.asEnumeration(addUdResponse
            .getAllegato().getContent().stream().map(ByteArrayInputStream::new).iterator())));
        OutputUD outputUD = getOutputUDFromResponseAllegato(allegato);
        addUdOutput.setOutputUD(outputUD);

      }

      return addUdOutput;

    }

    it.eng.auriga.repository2.webservices.addunitadoc.Service service =
        new it.eng.auriga.repository2.webservices.addunitadoc.Service();

    service.setCodApplicazione(codApplicazione);
    service.setIstanzaApplicazione(istanzaApplicazione);
    service.setUserName(userName);
    service.setPassword(password);
    service.setXml(xml);
    service.setHash(hash);

    Binding binding = ((BindingProvider) this.getAddUdService()).getBinding();
    var handlerList = binding.getHandlerChain();
    StiloHandler stiloHandler = new StiloHandler();
    stiloHandler.setAllegati(allegati);
    handlerList.add(stiloHandler);
    binding.setHandlerChain(handlerList);

    logger.info(methodName, "Avvio la chiamata a stilo");

    String result = this.getAddUdService().serviceOperation(service).getServiceReturn();
    BaseOutputWS baseOutputWS = getBaseOutputWSFromString(result);
    addUdOutput.setBaseOutputWS(baseOutputWS);

    logger.info(methodName, "Termina la chiamata a stilo");
    logger.info(methodName, "Result: " + baseOutputWS.getWSResult());
    logger.info(methodName, "Warning message " + baseOutputWS.getWarningMessage());
    logger.info(methodName, "Error " + baseOutputWS.getWSError());

    if (!baseOutputWS.getWSResult().equalsIgnoreCase("1")) {
      logger.error(methodName, MESSAGGIO_SOAP,
          baseOutputWS.getWSError().getErrorMessage());
    }

    else {
      OutputUD outputUD = getOutputUDFromResponseAllegato(stiloHandler.getAllegatoResponse());
      addUdOutput.setOutputUD(outputUD);
    }

    return addUdOutput;
  }

  private AddUdResponse addUdWithCosmoBeProxy(String codApplicazione, String istanzaApplicazione,
      String userName, String password, String xml, String hash, List<StiloAllegato> allegati) {

    AddUdRequest addUdRequest = new AddUdRequest();
    addUdRequest.setCodApplicazione(codApplicazione);
    addUdRequest.setHash(hash);
    addUdRequest.setIstanzaApplicazione(istanzaApplicazione);
    addUdRequest.setPassword(password);
    addUdRequest.setUserName(userName);
    addUdRequest.setXml(xml);

    addUdRequest.setAllegati(allegati.stream().map(temp -> {

      StiloAllegatoRequest allegato = new StiloAllegatoRequest();
      allegato.setContentId(temp.getContentId());
      allegato.setContentType(temp.getContentType());
      allegato.setFileName(temp.getFileName());
      try {
        allegato.setContent(Arrays.asList(temp.getContent().readAllBytes()));
        return allegato;
      } catch (IOException e) {
        logger.error("addUdWithCosmoBeProxy", CONVERSIONE_ALLEGATI, e);
        throw new InternalServerErrorException(CONVERSIONE_ALLEGATI, e);
      }

    }).collect(Collectors.toList()));

    return this.cosmoBeStiloFeignClient.postStiloInserimentoCosmo(addUdRequest);

  }

  @Override
  public UpdUdOutput updUd(UDDaAgg udDaAgg, List<StiloAllegato> allegati) {
    String methodName = "updUd";

    boolean useCosmoBeProxy = configurazioneService
        .requireConfig(ParametriApplicativo.STILO_COSMOBEPROXY_ENABLED).asBoolean();

    String codApplicazione =
        configurazioneService.requireConfig(ParametriApplicativo.STILO_COD_APPLICAZIONE).asString();

    String istanzaApplicazione = configurazioneService
        .requireConfig(ParametriApplicativo.STILO_ISTANZA_APPLICAZIONE).asString();

    String userName =
        configurazioneService.requireConfig(ParametriApplicativo.STILO_USERNAME).asString();

    String password =
        configurazioneService.requireConfig(ParametriApplicativo.STILO_PASSWORD).asString();


    logger.debug(methodName, "codApplicazione = " + codApplicazione);
    logger.debug(methodName, "istanzaApplicazione = " + istanzaApplicazione);
    logger.debug(methodName, "userName = " + userName);
    logger.debug(methodName, "password = " + password);

    UpdUdOutput updUdOutput = new UpdUdOutput();

    String xml;
    String hash;
    try {
      JAXBContext context = JAXBContext.newInstance(UDDaAgg.class);
      Marshaller marshaller = context.createMarshaller();
      StringWriter out = new StringWriter();
      marshaller.marshal(udDaAgg, out);
      xml = out.toString();
      hash = getHashFromXML(xml);
      logger.info(methodName, "xml = " + xml);
      logger.info(methodName, "hash = " + hash);
    } catch (JAXBException | NoSuchAlgorithmException e) {
      logger.error(methodName, CONVERSIONE_MESSAGGIO_SOAP, e);
      throw new InternalServerErrorException(CONVERSIONE_MESSAGGIO_SOAP, e);
    }

    if (useCosmoBeProxy) {
      UpUdResponse upUdResponse = updUdWithCosmoBeProxy(codApplicazione, istanzaApplicazione,
          userName, password, xml, hash, allegati);


      String result = upUdResponse.getServiceResponse();
      BaseOutputWS baseOutputWS = getBaseOutputWSFromString(result);
      updUdOutput.setBaseOutputWS(baseOutputWS);

      if (!baseOutputWS.getWSResult().equalsIgnoreCase("1")) {
        logger.error(methodName, MESSAGGIO_SOAP,
            baseOutputWS.getWSError().getErrorMessage());
      }
      else {
        StiloAllegato allegato = new StiloAllegato();
        BeanUtils.copyProperties(upUdResponse.getAllegato(), allegato);
        allegato.setContent(new SequenceInputStream(IteratorUtils.asEnumeration(upUdResponse
            .getAllegato().getContent().stream().map(ByteArrayInputStream::new).iterator())));
        OutputUD outputUD = getOutputUDFromResponseAllegato(allegato);
        updUdOutput.setOutputUD(outputUD);

      }

      return updUdOutput;

    }

    it.eng.auriga.repository2.webservices.updunitadoc.Service service =
        new it.eng.auriga.repository2.webservices.updunitadoc.Service();

    service.setCodApplicazione(codApplicazione);
    service.setIstanzaApplicazione(istanzaApplicazione);
    service.setUserName(userName);
    service.setPassword(password);
    service.setXml(xml);
    service.setHash(hash);

    Binding binding = ((BindingProvider) this.getUpdUdService()).getBinding();
    var handlerList = binding.getHandlerChain();
    StiloHandler stiloHandler = new StiloHandler();
    stiloHandler.setAllegati(allegati);
    handlerList.add(stiloHandler);
    binding.setHandlerChain(handlerList);

    logger.info(methodName, "Avvio chiamata a stilo");

    String result = this.getUpdUdService().serviceOperation(service).getServiceReturn();
    BaseOutputWS baseOutputWS = getBaseOutputWSFromString(result);
    updUdOutput.setBaseOutputWS(baseOutputWS);

    logger.info(methodName, "Termina la chiamata a stilo");
    logger.info(methodName, "Result: " + baseOutputWS.getWSResult());
    logger.info(methodName, "Warning message " + baseOutputWS.getWarningMessage());
    logger.info(methodName, "Error " + baseOutputWS.getWSError());

    if (!baseOutputWS.getWSResult().equalsIgnoreCase("1")) {
      logger.error(methodName, MESSAGGIO_SOAP,
          baseOutputWS.getWSError().getErrorMessage());
    }

    else {
      OutputUD outputUD = getOutputUDFromResponseAllegato(stiloHandler.getAllegatoResponse());
      updUdOutput.setOutputUD(outputUD);
    }

    return updUdOutput;

  }


  private UpUdResponse updUdWithCosmoBeProxy(String codApplicazione, String istanzaApplicazione,
      String userName, String password, String xml, String hash, List<StiloAllegato> allegati) {

    UpUdRequest upUdRequest = new UpUdRequest();
    upUdRequest.setCodApplicazione(codApplicazione);
    upUdRequest.setHash(hash);
    upUdRequest.setIstanzaApplicazione(istanzaApplicazione);
    upUdRequest.setPassword(password);
    upUdRequest.setUserName(userName);
    upUdRequest.setXml(xml);
    upUdRequest.setAllegati(allegati.stream().map(temp -> {

      StiloAllegatoRequest allegato = new StiloAllegatoRequest();
      try {
        allegato.setContent(Arrays.asList(temp.getContent().readAllBytes()));
      } catch (IOException e) {
        logger.error("addUdWithCosmoBeProxy", CONVERSIONE_ALLEGATI, e);
        throw new InternalServerErrorException(CONVERSIONE_ALLEGATI, e);
      }
      allegato.setContentId(temp.getContentId());
      allegato.setContentType(temp.getContentType());
      allegato.setFileName(temp.getFileName());

      return allegato;


    }).collect(Collectors.toList()));



    return this.cosmoBeStiloFeignClient.putStiloAggiornamentoCosmo(upUdRequest);
  }


  private String getHashFromXML(String xml) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    return Base64.encodeBase64String(md.digest(xml.getBytes()));
  }

  private BaseOutputWS getBaseOutputWSFromString(String result) {
    String decodedResult = new String(Base64.decodeBase64(result));
    StringReader reader = new StringReader(decodedResult);

    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(BaseOutputWS.class);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      return (BaseOutputWS) jaxbUnmarshaller.unmarshal(reader);

    } catch (JAXBException e) {
      logger.error("getHashFromXML", "Problemi nella conversione del messaggio", e);
      throw new InternalServerErrorException("Problemi nella conversione del messaggio", e);
    }

  }

  private OutputUD getOutputUDFromResponseAllegato(StiloAllegato allegatoResponse) {

    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(OutputUD.class);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      String allegatoResponseString =
          new String(allegatoResponse.getContent().readAllBytes());

      // senza questo replace jaxb va in errore, lo spazio alla prima riga prima di version non e'
      // uno space ma un non breaking space
      allegatoResponseString = allegatoResponseString.replaceAll("&#160;", " ");
      allegatoResponseString = allegatoResponseString.replaceAll("&nbsp;", " ");

      String allegatoResponseStringDecoded =
          HtmlUtils.htmlUnescape(allegatoResponseString);

      return (OutputUD) jaxbUnmarshaller.unmarshal(new StringReader(allegatoResponseStringDecoded));
    } catch (JAXBException | IOException e) {
      logger.error("getHashFromXML", "Problemi nella conversione del messaggio outputUD", e);
      throw new InternalServerErrorException("Problemi nella conversione del messaggio outputUD",
          e);
    }

  }

  @Override
  public ServiceStatusDTO checkStatus() {

    String endpointAddUrl =
        configurazioneService.requireConfig(ParametriApplicativo.STILO_ADDUD_URL).asString();

    String endpointUpdUrl =
        configurazioneService.requireConfig(ParametriApplicativo.STILO_ADDUD_URL).asString();

    String message = endpointAddUrl + " e " + endpointUpdUrl;
    return ServiceStatusDTO
        .of(() -> this.getAddUdService() != null || this.getUpdUdService() != null,
        ServiceStatusEnum.DOWN)
        .withName("Stilo client").withDetail("endpointUrl", message)
        .build();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    // NOP
  }

  @Override
  public UpdUdOutput aggiornaUnitaDocumentaria(
      AggiornaUnitaDocumentariaRequest aggiornaUnitaDocumentariaRequest) {

    String methodName = "aggiornaUnitaDocumentaria";
    CosmoTDocumento documento = null;
    CosmoTContenutoDocumento contenutoDocumento = null;
    RetrievedContent retrievedFile=null;
    Entity documentoIndex = null;

    if (!StringUtils.isEmpty(aggiornaUnitaDocumentariaRequest.getIdDocumento())) {
      documento =
          cosmoTDocumentoRepository
          .findOne(Long.valueOf(aggiornaUnitaDocumentariaRequest.getIdDocumento()));

      contenutoDocumento = getContenutoDocumento(documento);

      if (contenutoDocumento.getTipo().getCodice().equals(TipoContenutoDocumento.FIRMATO.name())
          || contenutoDocumento.getTipo().getCodice()
          .equals(TipoContenutoDocumento.ORIGINALE.name())) {
        logger.info(methodName, "Recupero documento da index");
        // recupero del contenuto (contenuto fisico)
        documentoIndex = api2IndexService.find(contenutoDocumento.getUuidNodo(), true);
        if (null == documentoIndex) {
          var parametroNonValorizzato =
              String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "documentoIndex");
          logger.error(methodName, parametroNonValorizzato);
          throw new InternalServerException(parametroNonValorizzato);
        }

      }
      else {

        logger.info(methodName, "Recupero documento da file system");
        retrievedFile = fileShareService.get(contenutoDocumento.getUuidNodo(),
            contenutoDocumento.getDtInserimento().toLocalDateTime().toLocalDate());
        if (null == retrievedFile) {
          var parametroNonValorizzato =
              String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "documentoFileSystem");
          logger.error(methodName, parametroNonValorizzato);
          throw new InternalServerException(parametroNonValorizzato);

        }
      }
    }

    List<StiloAllegato> allegati = (contenutoDocumento != null)
        ? Arrays.asList(
            this.stiloMapper.mapStiloAllegato(contenutoDocumento, documentoIndex, retrievedFile))
            : new ArrayList<>();

    return this.updUd(
        this.stiloMapper.mapAggiornaUnitaDocumentariaRequestToUDDaAgg(
            aggiornaUnitaDocumentariaRequest, contenutoDocumento, documentoIndex, retrievedFile),
        allegati);



  }



  /*
   * Controlli formali e recupero del contenuto del documento
   */
  private CosmoTContenutoDocumento getContenutoDocumento(CosmoTDocumento documento) {
    final var methodName = "checkInputSmistamento";

    if (null == documento) {
      var parametroNonValorizzato = "Documento non trovato";
      logger.error(methodName, parametroNonValorizzato);
      throw new InternalServerException(parametroNonValorizzato);
    }
    if (null == documento.getPratica().getEnte()) {
      var parametroNonValorizzato = String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "ente");
      logger.error(methodName, parametroNonValorizzato);
      throw new InternalServerException(parametroNonValorizzato);
    }
    if (null == documento.getPratica().getTipo()) {
      var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "tipo pratica");
      logger.error(methodName, parametroNonValorizzato);
      throw new InternalServerException(parametroNonValorizzato);
    }
    if (null == documento.getTipo()) {
      var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "tipo documento");
      logger.error(methodName, parametroNonValorizzato);
      throw new InternalServerException(parametroNonValorizzato);
    }
    if (null == documento.getContenuti()) {
      var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "contenuto documento");
      logger.error(methodName, parametroNonValorizzato);
      throw new InternalServerException(parametroNonValorizzato);
    }
    var contenutoDocumento =
        Optional.ofNullable(documento.findContenuto(TipoContenutoDocumento.FIRMATO))
        .orElse(Optional.ofNullable(documento.findContenuto(TipoContenutoDocumento.ORIGINALE))
            .orElseGet(() -> documento.findContenuto(TipoContenutoDocumento.TEMPORANEO)));

    if (null == contenutoDocumento || null == contenutoDocumento.getUuidNodo()) {
      var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "contenuto documento");
      logger.error(methodName, parametroNonValorizzato);
      throw new ContenutoException(parametroNonValorizzato);
    }

    if (null == contenutoDocumento.getFormatoFile()) {
      var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "formato file");
      logger.error(methodName, parametroNonValorizzato);
      throw new InternalServerException(parametroNonValorizzato);
    }

    return contenutoDocumento;

  }


}

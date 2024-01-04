/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmosoap.business.service.impl;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.activation.DataHandler;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import org.apache.commons.io.IOUtils;
import org.apache.soap.util.mime.ByteArrayDataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.csi.cosmo.common.async.internal.ContextAwareCallable;
import it.csi.cosmo.common.async.model.LongTask;
import it.csi.cosmo.common.dto.common.ServiceStatusDTO;
import it.csi.cosmo.common.dto.common.ServiceStatusEnum;
import it.csi.cosmo.common.entities.CosmoDEnteCertificatore;
import it.csi.cosmo.common.entities.CosmoDEnteCertificatore_;
import it.csi.cosmo.common.entities.CosmoDFormatoFile;
import it.csi.cosmo.common.entities.CosmoDFormatoFile_;
import it.csi.cosmo.common.entities.CosmoDTipoFirma;
import it.csi.cosmo.common.entities.CosmoDTipoFirma_;
import it.csi.cosmo.common.entities.CosmoREnteCertificatoreEnte;
import it.csi.cosmo.common.entities.CosmoREnteCertificatoreEntePK;
import it.csi.cosmo.common.entities.CosmoRFormatoFileProfiloFeqTipoFirma;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTIstanzaFunzionalitaFormLogico;
import it.csi.cosmo.common.entities.CosmoTLock;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.enums.EnteCertificatore;
import it.csi.cosmo.common.entities.enums.ProfiloFEQ;
import it.csi.cosmo.common.entities.enums.TipoContenutoDocumento;
import it.csi.cosmo.common.entities.proto.CosmoREntity;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.monitoring.Monitorable;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.ValoreParametroFormLogicoWrapper;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmosoap.business.service.Api2IndexService;
import it.csi.cosmo.cosmosoap.business.service.AsyncTaskService;
import it.csi.cosmo.cosmosoap.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmosoap.business.service.ContenutoDocumentoService;
import it.csi.cosmo.cosmosoap.business.service.DoSignService;
import it.csi.cosmo.cosmosoap.business.service.EventService;
import it.csi.cosmo.cosmosoap.business.service.LockService;
import it.csi.cosmo.cosmosoap.business.service.TransactionService;
import it.csi.cosmo.cosmosoap.business.service.impl.LockServiceImpl.LockAcquisitionRequest;
import it.csi.cosmo.cosmosoap.config.Constants;
import it.csi.cosmo.cosmosoap.config.ErrorMessages;
import it.csi.cosmo.cosmosoap.config.ParametriApplicativo;
import it.csi.cosmo.cosmosoap.dto.Task;
import it.csi.cosmo.cosmosoap.dto.TransactionExecutionResult;
import it.csi.cosmo.cosmosoap.dto.rest.AttivitaEseguibileMassivamente;
import it.csi.cosmo.cosmosoap.dto.rest.CommonRemoteData;
import it.csi.cosmo.cosmosoap.dto.rest.ContenutoDocumento;
import it.csi.cosmo.cosmosoap.dto.rest.ContenutoEntity;
import it.csi.cosmo.cosmosoap.dto.rest.ContinueTransaction;
import it.csi.cosmo.cosmosoap.dto.rest.DoSignFirmaMassivaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.DoSignFirmaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.DoSignSigilloRequest;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiPayload;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiPraticaPayload;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiTask;
import it.csi.cosmo.cosmosoap.dto.rest.Documento;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.cosmo.cosmosoap.dto.rest.Esito;
import it.csi.cosmo.cosmosoap.dto.rest.FormatoFile;
import it.csi.cosmo.cosmosoap.dto.rest.RichiediOTPRequest;
import it.csi.cosmo.cosmosoap.dto.rest.SigillaDocumentoResponse;
import it.csi.cosmo.cosmosoap.dto.rest.StartTransaction;
import it.csi.cosmo.cosmosoap.integration.dto.DoSignMassivePayloadDTO;
import it.csi.cosmo.cosmosoap.integration.dto.DosignOutcomeDTO;
import it.csi.cosmo.cosmosoap.integration.dto.DosignPayloadDTO;
import it.csi.cosmo.cosmosoap.integration.dto.dosign.VerifyInfo;
import it.csi.cosmo.cosmosoap.integration.dto.dosign.VerifyInput;
import it.csi.cosmo.cosmosoap.integration.mapper.DosignMapper;
import it.csi.cosmo.cosmosoap.integration.mapper.VerifyInfoMapper;
import it.csi.cosmo.cosmosoap.integration.repository.CosmoDEnteCertificatoreRepository;
import it.csi.cosmo.cosmosoap.integration.repository.CosmoDFormatoFileRepository;
import it.csi.cosmo.cosmosoap.integration.repository.CosmoDTipoFirmaRepository;
import it.csi.cosmo.cosmosoap.integration.repository.CosmoREnteCertificatoreEnteRepository;
import it.csi.cosmo.cosmosoap.integration.repository.CosmoRFormatoFileProfiloFeqTipoFirmaRepository;
import it.csi.cosmo.cosmosoap.integration.repository.CosmoTAttivitaRepository;
import it.csi.cosmo.cosmosoap.integration.repository.CosmoTContenutoDocumentoRepository;
import it.csi.cosmo.cosmosoap.integration.repository.CosmoTDocumentoRepository;
import it.csi.cosmo.cosmosoap.integration.repository.CosmoTIstanzaFunzionalitaFormLogicoRepository;
import it.csi.cosmo.cosmosoap.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmosoap.integration.rest.CosmoBusinessPraticheFeignClient;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.BaseRemoteSignatureDto;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.CommonRemoteDataDto;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.ContinueTransactionDto;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.DosignCertificateChainNotFoundException_Exception;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.DosignClientHttpErrorException_Exception;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.DosignException_Exception;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.DosignInvalidAuthenticationException_Exception;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.DosignInvalidDataException_Exception;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.DosignInvalidEncodingException_Exception;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.DosignInvalidEnvelopeException_Exception;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.DosignInvalidModeException_Exception;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.DosignInvalidOtpException_Exception;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.DosignInvalidPinException_Exception;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.DosignInvalidSignerException_Exception;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.DosignPinLockedException_Exception;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.DosignRemoteSignature;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.DosignRemoteSignature_Service;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.SingleRemotePdfSignatureDto;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.SingleRemoteSignatureDto;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.StartTransactionDto;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.exception.DosignClientException;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.exception.DosignTestModeEnabledException;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.DosignSignatureValidation;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.DosignSignatureValidation_Service;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.VerifyDto;
import it.csi.cosmo.cosmosoap.integration.soap.dosign.validation.VerifyReportDto;
import it.csi.cosmo.cosmosoap.security.SecurityUtils;
import it.csi.cosmo.cosmosoap.util.CommonUtils;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;
import it.doqui.dosign.dosign.business.session.dosign.Dosign;
import it.doqui.dosign.dosign.business.session.dosign.Dosign_Service;
import it.doqui.dosign.dosign.business.session.dosign.SigilloSignatureDto;
import it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignRemoteService;
import it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignRemoteService_Service;
import it.doqui.dosign.dosign.business.session.dosign.remotev2.Format;
import it.doqui.dosign.dosign.business.session.dosign.remotev2.RemoteEndTransactionDto;
import it.doqui.dosign.dosign.business.session.dosign.remotev2.RemoteOtpDto;
import it.doqui.dosign.dosign.business.session.dosign.remotev2.RemotePdfSignatureDto;
import it.doqui.dosign.dosign.business.session.dosign.remotev2.RemoteSignatureDto;
import it.doqui.dosign.dosign.business.session.dosign.remotev2.RemoteStartTransactionDto;


/**
 *
 */
@Transactional(readOnly = true)
@Service
public class DoSignServiceImpl implements DoSignService, InitializingBean, Monitorable {

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "DoSignServiceImpl");

  private volatile DosignRemoteSignature portDosignRemoteSignature;

  private volatile DosignSignatureValidation portDosignSignatureValidation;

  private volatile DosignRemoteService portDosignUanatacaRemoteSignature;

  private volatile Dosign portDosign;

  private static final QName SERVICE_SIGNATURE =
      new QName("http://remotesignature.dosign.session.business.dosign.dosign.doqui.it/",
          "DosignRemoteSignature");

  private static final QName SERVICE_VALIDATION =
      new QName("http://signaturevalidation.dosign.session.business.dosign.dosign.doqui.it/",
          "DosignRemoteValidation");

  private static final QName SERVICE_UANATACA_SIGNATURE =
      new QName("http://remotev2.dosign.session.business.dosign.dosign.doqui.it/",
          "DosignRemoteService");

  private static final QName SERVICE_DOSIGN =
      new QName("http://dosign.session.business.dosign.dosign.doqui.it/", "Dosign");

  private static final String FIRMASINGOLODOCUMENTO = "firmaSingoloDocumento";

  private static final String UANATACAFIRMASINGOLODOCUMENTO = "uanatacaFirmaSingoloDocumento";

  private static final String FIRMASINGOLODOCUMENTOPDF = "firmaSingoloDocumentoPDF";

  private static final String UANATACAFIRMASINGOLODOCUMENTOPDF = "uanatacaFirmaSingoloDocumentoPDF";

  private static final int MAX_PARALLEL_EXECUTIONS = 3;

  private static final String TRACE_INPUT_FOR_END_TRANSACTION = "Tracing input for operation 'endTransaction'";

  private static final String TRACE_OUTPUT_FOR_END_TRANSACTION = "Tracing output for operation 'endTransaction'";

  private static final String GENERIC_ERROR = "Generic error";

  private static final String NO_RETURN_TYPE_VOID = "no return type (void)";

  private static final String TEST = ".TEST";

  private static final String FIRMA_DOCUMENTI = "Firma documenti";

  private static final String NOME_DOCUMENTO_DA_FIRMARE = "Nome documento da firmare: %s.%n";

  private static final String ERRORE = "errore";

  private static final String COMPLETED = "completed";

  private static final String STATO = "stato";

  private static final String OGGETTO_PRATICA = "oggettoPratica";

  private static final String GET_PORT_UANATACA = "getPortDosignUanatacaRemoteSignature";

  private static final String CONTENUTO_DOCUMENTO_NON_PRESENTE = "Contenuto documento non presente";

  private static final String PRIMA_FIRMA = "prima firma";

  private static final String TRACING_INPUT_FOR_OPERATION = "Tracing input for operation '%s'";

  private static final String TRACING_OUTPUT_FOR_OPERATION = "Tracing output for operation '%s'";

  private static final String ADD_SIGN = "addSign";

  private Integer mode;

  private Integer encoding;

  private String otpSendingType;

  private String signedContentFileExtension;

  private Boolean testMode;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private DosignMapper dosignMapper;

  @Autowired
  private AsyncTaskService asyncTaskService;

  @Autowired
  private CosmoRFormatoFileProfiloFeqTipoFirmaRepository cosmoRFormatoFileProfiloFeqTipoFirmaRepository;

  @Autowired
  private CosmoDTipoFirmaRepository cosmoDTipoFirmaRepository;

  @Autowired
  private CosmoDFormatoFileRepository cosmoDFormatoFileRepository;

  @Autowired
  private Api2IndexService api2IndexService;

  @Autowired
  private ContenutoDocumentoService contenutoDocumentoService;

  @Autowired
  private CosmoTContenutoDocumentoRepository cosmoTContenutoDocumentoRepository;

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private EventService eventService;

  @Autowired
  private CosmoTAttivitaRepository cosmoTAttivitaRepository;

  @Autowired
  private LockService lockService;

  @Autowired
  private CosmoBusinessPraticheFeignClient cosmoBusinessPraticheFeignClient;

  @Autowired
  private CosmoTIstanzaFunzionalitaFormLogicoRepository cosmoTIstanzaFunzionalitaFormLogicoRepository;

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CosmoTDocumentoRepository cosmoTDocumentoRepository;

  @Autowired
  private CosmoREnteCertificatoreEnteRepository cosmoREnteCertificatoreEnteRepository;

  @Autowired
  private CosmoDEnteCertificatoreRepository cosmoDEnteCertificatoreRepository;

  private static final String CONFIG_FIRMA_NOTE_FIRMA_KEY = "variabileNoteFirma";

  private static final String CONFIG_FIRMA_RISULTATO_FIRMA_KEY = "variabileRisultatoFirma";

  private static final String CONFIG_FIRMA_KEY = "O_FIRMA";

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  @Override
  public ServiceStatusDTO checkStatus() {
    return ServiceStatusDTO.of(() -> {
      try {
        this.getPortDosignRemoteSignature().testResources();
      } catch (DosignException_Exception e) {
        throw new InternalServerException(e.getMessage(), e);
      }
      try {
        this.getPortDosignUanatacaRemoteSignature().testResources();
      } catch (it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignException_Exception e) {
        throw new InternalServerException(e.getMessage(), e);
      }
      try {
        this.getPortDosign().testResources();
      } catch (it.doqui.dosign.dosign.business.session.dosign.DosignException_Exception e) {
        throw new InternalServerException(e.getMessage(), e);
      }
      return true;
    }, ServiceStatusEnum.DOWN).withName("DOSIGN (Remote sign service)")
        .withDetail("endpointUrl",
            configurazioneService.getConfig(ParametriApplicativo.REMOTESIGN_DOSIGN_URL).asString())
        .withDetail("endpointUrlUanataca",
            configurazioneService.getConfig(ParametriApplicativo.REMOTESIGN_UANATACA_URL).asString())
        .withDetail("endpointUrlDosign",
            configurazioneService.getConfig(ParametriApplicativo.DOSIGN_URL).asString())
        .withDetail("testMode",
            configurazioneService.getConfig(ParametriApplicativo.REMOTESIGN_DOSIGN_TESTMODE_ENABLED)
            .asBoolean())
        .build();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    mode = configurazioneService.requireConfig(ParametriApplicativo.REMOTESIGN_DOSIGN_MODE)
        .asInteger();
    CommonUtils.require(mode, "mode");
    encoding = configurazioneService.requireConfig(ParametriApplicativo.REMOTESIGN_DOSIGN_ENCODING)
        .asInteger();
    CommonUtils.require(encoding, "encoding");
    otpSendingType = configurazioneService
        .requireConfig(ParametriApplicativo.REMOTESIGN_DOSIGN_OTP_SENDING_TYPE).asString();
    CommonUtils.requireString(otpSendingType, "otpSendingType");
    signedContentFileExtension = configurazioneService
        .requireConfig(ParametriApplicativo.REMOTESIGN_DOSIGN_SIGNED_CONTENT_FILE_EXTENSION)
        .asString();
    CommonUtils.requireString(signedContentFileExtension, "signedContentFileExtension");
    testMode = configurazioneService
        .requireConfig(ParametriApplicativo.REMOTESIGN_DOSIGN_TESTMODE_ENABLED).asBoolean();
    CommonUtils.require(testMode, "testMode");
    logger.info("afterPropertiesSet", "DoSignService Bean initialized and set");
  }

  @Override
  public DosignOutcomeDTO richiediOTP(RichiediOTPRequest body) {

    CommonUtils.requireString(body.getAlias(), "alias");
    CommonUtils.requireString(body.getPin(), "PIN");
    CommonUtils.requireString(body.getCodiceEnteCertificatore(), "codiceEnteCertificatore");

    return body.getCodiceEnteCertificatore().equalsIgnoreCase(EnteCertificatore.UANATACA.getCodice())
        ? richiediOTPUanataca(body) : richiediOTPInternal(body);
  }

  @Override
  public void firma(DoSignFirmaRequest body) {
    String methodName = "Firma";

    CommonUtils.requireString(body.getAlias(), "alias");
    CommonUtils.requireString(body.getPin(), "PIN");
    CommonUtils.require(body.getDocumentiDaFirmare(), "documentiDaFirmare");
    if (CollectionUtils.isEmpty(body.getDocumentiDaFirmare())) {
      throw new BadRequestException("Nessun documento da firmare", new InvalidParameterException());
    }

    int numeroDocumentiDaFirmare = body.getDocumentiDaFirmare().size();
    UUID identificativoTransazione = java.util.UUID.randomUUID();

    logger.info(methodName, String.format(
        "Tentativo di firma digitale avviato.%nIdentificativo transazione: %s.",
        identificativoTransazione.toString()));
    logger.info(methodName, String.format("Numero documenti da firmare: %d.", numeroDocumentiDaFirmare));
    logger.info(methodName, String.format("Profilo FEQ: %s.", body.getProfiloFEQ()));
    logger.info(methodName, String.format("Scelta marca temporale: %s", body.isMarcaTemporale()));
    logger.info(methodName, String.format("Scelta notifica firma documenti: %s", body.isNotificaFirma()));

    List<DosignPayloadDTO> firmaResponse = new LinkedList<>();

    asyncTaskService.start(FIRMA_DOCUMENTI, task -> {

      List<CosmoTContenutoDocumento> listaContenuti = getListaContenutiDaFirmare(body.getDocumentiDaFirmare(), body.getProfiloFEQ());
      List<DosignPayloadDTO> listaDocDaFirmare = getDosignPayloadFromIndex(listaContenuti);

      String sessionId = body.getCodiceEnteCertificatore().equalsIgnoreCase(EnteCertificatore.UANATACA.getCodice())
          ? getFirmaUanatacaSessionId(body.getAlias(), body.getPin(),body.getPassword(), body.getOtp(),identificativoTransazione.toString(), body.getProvider())
              : infoDoSignTransaction(body.getAlias(), body.getPin(), body.getCodiceCa(), body.getOtp(),
                  identificativoTransazione.toString(), body.getDocumentiDaFirmare().size());

      task.step(FIRMA_DOCUMENTI, step -> {
        List<DosignPayloadDTO> res = new LinkedList<>();
        res = firmaDocumenti(listaDocDaFirmare, body, null, sessionId);
        firmaResponse.addAll(res);
        return null;
      });

      try {
        if (body.getCodiceEnteCertificatore().equalsIgnoreCase(EnteCertificatore.UANATACA.getCodice())) {
          uanatacaEndSession(body.getAlias(), body.getPin(), body.getPassword(), identificativoTransazione.toString(), sessionId, body.getProvider());
        } else {
          remoteEndSession(body.getAlias(), body.getPin(), Integer.valueOf(body.getCodiceCa()),identificativoTransazione.toString() , sessionId);
        }
      } catch (Exception e) {
        notificaErroreFirmaDocumenti(parseError(e.getMessage()));
        logger.error(methodName, ErrorMessages.FIRMA_ERRORE);
        throw new InternalServerException(e.getMessage());
      }
      int count = 0;
      for (DosignPayloadDTO docFirmato : firmaResponse) {

        salvaDocumentoFirmato(null, docFirmato,
            body.getProfiloFEQ(), ++count);
      }
      if (null != body.isMarcaTemporale() && body.isMarcaTemporale().equals(Boolean.TRUE)) {
        aggiornamentoContatoreMarcaTemporale(body.getCodiceTsa(), count);
      }

      notificaFineFirmaDocumenti();
      return null;
    });
  }


  @Override
  public List<VerifyInfo> verify(VerifyInput verifyInput) {
    String methodName = "verify";

    List<VerifyInfo> result = null;
    try {
      VerifyDto verifyDto = new VerifyDto();
      verifyDto.setEnvelopeArray(verifyInput.getContent());
      verifyDto.setExtractData(verifyInput.isExtractData());
      verifyDto.setProfileType(
          verifyInput.isVerifyStrongSignature() ? STRONG_SIGNATURE : WEAK_SIGNATURE);
      verifyDto.setVerificationScope(verifyInput.isVerifyCrl() ? VERIFY_CRL : NO_VERIFY_CRL);
      VerifyReportDto verifyReportDto = getPortDosignSignatureValidation().verify(verifyDto);

      if (verifyReportDto != null && verifyReportDto.getError() == ERROR_CODE_NOT_PRESENT
          && !CollectionUtils.isEmpty(verifyReportDto.getVerifyInfo())) {
        result = VerifyInfoMapper.getInstance().toListDTO(verifyReportDto.getVerifyInfo());
      } else {
        if (verifyReportDto != null) {
          logger.error(methodName, "verifyReportDto.getError(): " + verifyReportDto.getError());
          logger.error(methodName, "verifyReportDto.getErrorMsg(): " + verifyReportDto.getErrorMsg());
          logger.error(methodName,
              "verifyReportDto.getHexErrorCode(): " + verifyReportDto.getHexErrorCode());
          throw new InternalServerException(verifyReportDto.getErrorMsg(),
              new DosignClientException(verifyReportDto.getErrorMsg()));
        } else {
          throw new InternalServerException(GENERIC_ERROR, new DosignClientException(GENERIC_ERROR));
        }
      }
    } catch (Exception e) {
      logger.error(methodName, e.getMessage(), e);
      throw new InternalServerException(e.getMessage(), new DosignClientException(e.getMessage()));
    }
    return result;
  }

  @Override
  public List<VerifyInfo> verifyPdf(VerifyInput verifyInput) {
    String methodName = "verifyPdf";

    List<VerifyInfo> result = null;
    try {
      VerifyDto verifyDto = new VerifyDto();
      verifyDto.setEnvelopeArray(verifyInput.getContent());
      verifyDto.setExtractData(verifyInput.isExtractData());
      verifyDto.setProfileType(
          verifyInput.isVerifyStrongSignature() ? STRONG_SIGNATURE : WEAK_SIGNATURE);
      verifyDto.setVerificationType(verifyInput.isVerifyCrl() ? VERIFY_CRL : NO_VERIFY_CRL);
      verifyDto.setVerificationScope(SCOPE_DIGITAL_SIGNATURE);
      VerifyReportDto verifyReportDto = getPortDosignSignatureValidation().pdfVerify(verifyDto);

      if (verifyReportDto != null && verifyReportDto.getError() == ERROR_CODE_NOT_PRESENT
          && !CollectionUtils.isEmpty(verifyReportDto.getVerifyInfo())) {
        result = VerifyInfoMapper.getInstance().toListDTO(verifyReportDto.getVerifyInfo());
      } else {
        if (verifyReportDto != null) {
          logger.error(methodName, "verifyReportDto.getError(): " + verifyReportDto.getError());
          logger.error(methodName, "verifyReportDto.getErrorMsg(): " + verifyReportDto.getErrorMsg());
          logger.error(methodName,
              "verifyReportDto.getHexErrorCode(): " + verifyReportDto.getHexErrorCode());
          throw new InternalServerException(verifyReportDto.getErrorMsg(),
              new DosignClientException(verifyReportDto.getErrorMsg()));
        } else {
          throw new InternalServerException(GENERIC_ERROR, new DosignClientException(GENERIC_ERROR));
        }
      }
    } catch (Exception e) {
      logger.error(methodName, e.getMessage(), e);
      throw new InternalServerException(e.getMessage(), new DosignClientException(e.getMessage()));
    }
    return result;
  }

  @Override
  public void uanatacaExecuteEndSession(RemoteEndTransactionDto endTransaction,
      String idTransazione) {
    String methodName = "uanatacaExecuteEndSession";

    try {
      executeUanatacaEndTransaction(endTransaction);

    } catch (it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignInvalidPinException_Exception e) {
      logger.error(methodName, ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_PIN_LOCKED, e);
      throw new InternalServerException(methodName, new DosignClientException(
          ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_PIN, parseError(e.getMessage())));

    } catch (it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignInvalidOtpException_Exception e) {
      logger.error(methodName, ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_OTP, e);
      throw new InternalServerException(methodName,
          new DosignClientException(e.getMessage(), parseError(e.getMessage())));

    } catch (it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignException_Exception e) {
      logger.error(methodName, ErrorMessages.FIRMA_ERRORE, e);
      throw new InternalServerException(methodName,
          new DosignClientException(e.getMessage(), parseError(e.getMessage())));

    } catch (DosignTestModeEnabledException eTest) {
      logger.error(methodName,
          ErrorMessages.FIRMA_STARTTRANSACTION_DOSIGN_IN_MODALITA_TEST_NESSUNA_OPERAZIONE_EFFETTUATA,
          eTest);
    }

  }

  @Override
  public CommonRemoteData executeStartSession(StartTransaction startTransaction,
      String idTransazione) {
    String methodName = "executeStartSession";

    CommonRemoteDataDto ret = new CommonRemoteDataDto();
    try {
      ret = executeStartTransaction(dosignMapper.toStartTransactionDTO(startTransaction));

    } catch (DosignClientHttpErrorException_Exception
        | DosignCertificateChainNotFoundException_Exception e) {
      logger.error(methodName,
          String.format(ErrorMessages.FIRMA_ERRORE_IN_FASE_DI_APERTURA_SESSIONE, idTransazione),
          parseError(e.getMessage()));
      throw new InternalServerException(methodName,
          new DosignClientException(ErrorMessages.FIRMA_ERRORE, parseError(e.getMessage())));

    } catch (DosignInvalidSignerException_Exception e) {
      logger.error(methodName, ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_SIGNER,
          parseError(e.getMessage()));
      throw new InternalServerException(methodName, new DosignClientException(
          ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_SIGNER, parseError(e.getMessage())));

    } catch (DosignInvalidPinException_Exception e) {
      logger.error(methodName, ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_PIN,
          parseError(e.getMessage()));
      throw new InternalServerException(methodName, new DosignClientException(
          ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_PIN, parseError(e.getMessage())));

    } catch (DosignInvalidOtpException_Exception e) {
      logger.error(methodName, ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_OTP,
          parseError(e.getMessage()));
      throw new InternalServerException(methodName, new DosignClientException(
          ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_OTP, parseError(e.getMessage())));

    } catch (DosignPinLockedException_Exception e) {
      logger.error(methodName, ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_PIN_LOCKED,
          parseError(e.getMessage()));
      throw new InternalServerException(methodName, new DosignClientException(
          ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_PIN_LOCKED, parseError(e.getMessage())));

    } catch (DosignException_Exception e) {
      logger.error(methodName,
          String.format(ErrorMessages.FIRMA_ERRORE_IN_FASE_DI_APERTURA_SESSIONE, idTransazione),
          parseError(e.getMessage()));
      throw new InternalServerException(methodName,
          new DosignClientException(e.getMessage(), parseError(e.getMessage())));

    } catch (DosignTestModeEnabledException eTest) {
      logger.error(methodName,
          ErrorMessages.FIRMA_STARTTRANSACTION_DOSIGN_IN_MODALITA_TEST_NESSUNA_OPERAZIONE_EFFETTUATA,
          eTest);
      ret.setAuthData("TEST MODE ENABLED");
    }

    return dosignMapper.toCommonRemoteData(ret);
  }

  @Override
  public String uanatacaExecuteStartSession(RemoteStartTransactionDto startTransaction,
      String idTransazione) {
    String methodName = "executeUanatacaStartSession";
    String ret = "";

    try {
      ret = executeUanatacaStartTransaction(startTransaction);

    } catch (it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignInvalidPinException_Exception e) {
      logger.error(methodName, ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_PIN_LOCKED,
          parseError(e.getMessage()));
      throw new InternalServerException(methodName, new DosignClientException(
          ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_PIN, parseError(e.getMessage())));

    } catch (it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignInvalidOtpException_Exception e) {
      logger.error(methodName, ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_OTP,
          parseError(e.getMessage()));
      throw new InternalServerException(methodName,
          new DosignClientException(e.getMessage(), parseError(e.getMessage())));

    } catch (it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignException_Exception e) {
      logger.error(methodName,
          String.format(ErrorMessages.FIRMA_ERRORE_IN_FASE_DI_APERTURA_SESSIONE, idTransazione),
          parseError(e.getMessage()));
      throw new InternalServerException(methodName,
          new DosignClientException(e.getMessage(), parseError(e.getMessage())));

    } catch (DosignTestModeEnabledException eTest) {
      logger.error(methodName,
          ErrorMessages.FIRMA_STARTTRANSACTION_DOSIGN_IN_MODALITA_TEST_NESSUNA_OPERAZIONE_EFFETTUATA,
          eTest);
      ret = ("TEST MODE ENABLED");
    }

    return ret;
  }

  @Override
  public void executeEndSession(ContinueTransaction continueTransaction, String idTransazione) {
    String methodName = "executeEndSession";

    ContinueTransactionDto continueTransactionDto =
        dosignMapper.toContinueTransactionDTO(continueTransaction);
    continueTransactionDto.setSendingType(otpSendingType);
    try {
      executeEndTransaction(continueTransactionDto);

    } catch (DosignInvalidSignerException_Exception e) {
      logger.error(methodName, ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_SIGNER,
          parseError(e.getMessage()));
      throw new InternalServerException(methodName, new DosignClientException(
          ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_SIGNER, parseError(e.getMessage())));

    } catch (DosignClientHttpErrorException_Exception e) {
      logger.error(methodName,
          String.format(ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA, idTransazione),
          parseError(e.getMessage()));
      throw new InternalServerException(methodName,
          new DosignClientException(ErrorMessages.FIRMA_ERRORE, parseError(e.getMessage())));

    } catch (DosignException_Exception e) {
      logger.error(methodName,
          String.format(ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA, idTransazione),
          parseError(e.getMessage()));
      throw new InternalServerException(methodName,
          new DosignClientException(e.getMessage(), parseError(e.getMessage())));

    } catch (DosignInvalidPinException_Exception e) {
      logger.error(methodName, ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_PIN,
          parseError(e.getMessage()));
      throw new InternalServerException(methodName, new DosignClientException(
          ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_PIN, parseError(e.getMessage())));

    } catch (DosignInvalidOtpException_Exception e) {
      logger.error(methodName, ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_OTP,
          parseError(e.getMessage()));
      throw new InternalServerException(methodName, new DosignClientException(
          ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_OTP, parseError(e.getMessage())));

    } catch (DosignPinLockedException_Exception e) {
      logger.error(methodName, ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_PIN_LOCKED,
          parseError(e.getMessage()));
      throw new InternalServerException(methodName, new DosignClientException(
          ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_PIN_LOCKED, parseError(e.getMessage())));

    } catch (DosignTestModeEnabledException eTest) {
      logger.error(methodName,
          ErrorMessages.FIRMA_STARTTRANSACTION_DOSIGN_IN_MODALITA_TEST_NESSUNA_OPERAZIONE_EFFETTUATA,
          eTest);
    }
  }

  @Override
  public void firmaMassiva(DoSignFirmaMassivaRequest request) {
    String methodName = "firmaMassiva";
    String taskName = "Firma multipla pratiche/documenti";

    CommonUtils.require(request.getTasks(), "task di firma");
    CommonUtils.require(request.getDocumentiDaFirmare(), "documenti da firmare");

    if (CollectionUtils.isEmpty(request.getTasks())) {
      throw new BadRequestException("Nessun task da consumare", new InvalidParameterException());
    }
    UUID identificativoTransazioneFirma = java.util.UUID.randomUUID();

    logger.info(methodName,
        String.format(
            "Tentativo di firma digitale avviato.%nIdentificativo transazione massivo: %s.%n",
            request.getUuidTransaction()));
    logger.info(methodName,
        String.format(
            "Tentativo di firma digitale avviato.%nIdentificativo transazione firma: %s.%n",
            identificativoTransazioneFirma.toString()));
    logger.info(methodName, String.format("Profilo FEQ: %s.%n", request.getProfiloFEQ()));
    logger.info(methodName, String.format("Scelta marca temporale: %s",
        Boolean.TRUE.equals(request.isMarcaTemporale()) ? "SI" : "NO"));
    logger.info(methodName, String.format("Scelta notifica firma documenti: %s",
        Boolean.TRUE.equals(request.isNotificaFirma()) ? "SI" : "NO"));

    var resultCollector = new ConcurrentHashMap<Long, EsitoAttivitaEseguibileMassivamente>();

    asyncTaskService.start(taskName, task -> {

      try {
        List<DoSignMassivePayloadDTO> listaDocTaskDaFirmare = new LinkedList<>();
        for (AttivitaEseguibileMassivamente atm : request.getTasks()) {
          List<DosignPayloadDTO> listaDocDaFirmare = new LinkedList<>();
          var docs = request.getDocumentiDaFirmare().stream()
              .filter(docT -> docT.getAttivita().getId() == atm.getAttivita().getId().intValue())
              .map(DocumentiTask::getDocumenti).findFirst().orElseThrow();
          List<CosmoTContenutoDocumento> listaContenuti =
              getListaContenutiDaFirmare(docs, request.getProfiloFEQ());
          for (CosmoTContenutoDocumento contenuto : listaContenuti) {
            boolean documentoFirmato = contenuto.getTipoFirma() != null;
            // recupero del contenuto (contenuto fisico)
            Entity documentoIndex = api2IndexService.find(contenuto.getUuidNodo(), true);
            if (null == documentoIndex) {
              var parametroNonValorizzato =
                  String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "documentoIndex");
              logger.error(methodName, parametroNonValorizzato);
              throw new InternalServerException(parametroNonValorizzato);
            }
            // verifica documento gia' firmato
            DosignPayloadDTO documentoDaFirmare =
                DosignPayloadDTO.builder().withOriginalContent(documentoIndex.getContent())
                    .withOriginalFilename(contenuto.getNomeFile()).withPrimaFirma(!documentoFirmato)
                    .withContenuto(contenuto).withId(contenuto.getId().intValue()).build();
            listaDocDaFirmare.add(documentoDaFirmare);
          }
          var taskDaFirmare = DoSignMassivePayloadDTO.builder()
              .withDocumentiDaFirmare(listaDocDaFirmare).withIdPratica(atm.getPratica().getId())
              .withIdAttivita(atm.getAttivita().getId()).build();
          listaDocTaskDaFirmare.add(taskDaFirmare);
        }

        var documenti = dosignMapper.toDocumentiPraticaPayloads(listaDocTaskDaFirmare);
        documenti.stream()
            .forEach(d -> d.setIdFunzionalita(request.getTasks().get(0).getFunzionalita().getId()));
        Integer numMax = configurazioneService
            .requireConfig(ParametriApplicativo.NUM_MAX_FIRME_PARALLELE).asInteger();

        int numMaxDocuments = request.getDocumentiDaFirmare().stream()
            .collect(Collectors.summingInt(x -> x.getDocumenti().size()));

        if (numMax != null && numMaxDocuments > numMax) {
          Throwable bre =
              new BadRequestException("Troppe elaborazioni richieste. Il massimo consentito e' di "
                  + numMax + " firme alla volta.");
          notificaErroriPreliminari(bre);
          throw new BadRequestException(
              "Troppe elaborazioni richieste. Il massimo consentito e' di " + numMax
                  + " firme alla volta.");
        }

        String identificativoTransazione = java.util.UUID.randomUUID().toString();
        String sessionId = request.getCodiceEnteCertificatore()
            .equalsIgnoreCase(EnteCertificatore.UANATACA.getCodice())
                ? getFirmaUanatacaSessionId(request.getAlias(), request.getPin(),
                    request.getPassword(), request.getOtp(), identificativoTransazione,
                    request.getProvider())
                : infoDoSignTransaction(request.getAlias(), request.getPin(), request.getCodiceCa(),
                    request.getOtp(), identificativoTransazione,
                    request.getDocumentiDaFirmare().size());

        var executor = Executors.newFixedThreadPool(
            Math.min(request.getDocumentiDaFirmare().size(), MAX_PARALLEL_EXECUTIONS));
        // submit di subtask all'executor per l'esecuzione parallela
        for (DocumentiPraticaPayload dpp : documenti) {

          var callable = new ContextAwareCallable<Object>(() -> task.step(FIRMA_DOCUMENTI, step -> {
            EsitoAttivitaEseguibileMassivamente esito = new EsitoAttivitaEseguibileMassivamente();
            esito.task = dpp;
            try {
              notificaInzioFirmaDocumentiPratica(dpp.getIdPratica(), dpp.getIdAttivita());
              lockAndThanSign(request, dpp, step, sessionId);
              esito.successo = true;
            } catch (Throwable terr) { // NOSONAR
              resultCollector.put(dpp.getIdPratica(), esito);
              notificaPraticaDocumentoErrore(dpp.getIdPratica(), dpp.getIdAttivita(), terr);
              esito.successo = false;
              esito.errore = terr;
            } finally {
              if (!resultCollector.containsKey(dpp.getIdPratica())) {
                resultCollector.put(dpp.getIdPratica(), esito);
                notificaFineFirmaDocumentiPratica(dpp.getIdPratica(), dpp.getIdAttivita());
              }

            }
            return null;
          }), getCurrentRequestAttributes());
          executor.submit(callable);
        }

        // attendi che l'esecuzione termini per tutti i task in esecuzione parallela
        executor.shutdown();
        try {
          if (!executor.awaitTermination(Math.max(300, request.getDocumentiDaFirmare().size() * 60),
              TimeUnit.SECONDS)) {
            executor.shutdownNow();
          }
        } catch (InterruptedException e) {
          task.warn(
              "executor did not complete in MAX_EXECUTION_TIME, following executions might be delayed");
          Thread.currentThread().interrupt();
        }
        try {
          if (request.getCodiceEnteCertificatore()
              .equalsIgnoreCase(EnteCertificatore.UANATACA.getCodice())) {
            uanatacaEndSession(request.getAlias(), request.getPin(), request.getPassword(),
                identificativoTransazione, sessionId, request.getProvider());
          } else {
            remoteEndSession(request.getAlias(), request.getPin(),
                Integer.valueOf(request.getCodiceCa()), identificativoTransazione, sessionId);
          }
        } catch (Exception e) {
          notificaErroreFirmaDocumenti(parseError(e.getMessage()));
          logger.error(methodName, ErrorMessages.FIRMA_ERRORE);
          throw new InternalServerException(e.getMessage());
        }

        logger.trace(methodName, TRACE_OUTPUT_FOR_END_TRANSACTION);
        logger.trace(methodName, NO_RETURN_TYPE_VOID);

      } catch (Throwable e) {
        notificaErroriPreliminari(e);
      }

      return null;
    });
  }

  @Override
  public SigillaDocumentoResponse apponiSigillo(DoSignSigilloRequest request) {

    String methodName = "apponiSigillo";

    var contenuti = getContenutiDocumenti(List.of(request.getDocumento()));

    var retrived = getDosignPayloadFromIndex(contenuti);

    if (retrived.isEmpty()) {
      throw new BadRequestException("Nessun documento su cui apporre il sigillo elettronico");
    }

    SigillaDocumentoResponse response = new SigillaDocumentoResponse();
    response.setCodice("000");
    response.setMessaggio("OK");

    try {
      logger.info(methodName, "Effettuo chiamata a dosign");
      DosignPayloadDTO sealedDoc = apponiSigilloSingoloDocumento(retrived.get(0), request);
      transactionService.inTransaction(() -> salvaDocumentoSigillato(sealedDoc));
    } catch (Exception e) {
      logger.error(methodName, e.getMessage(), e);
      response.setCodice("001");
      response.setMessaggio(e.getMessage());
    }

    return response;

  }

  public static class EsitoFirmaDocumenti {
    DocumentiPayload documento;
    Esito esito;

    public DocumentiPayload getDocumento() {
      return this.documento;
    }

    public Esito getEsito() {
      return this.esito;
    }

    public void setEsito(Esito e) {
      this.esito = e;
    }

  }

  public static class EsitoAttivitaEseguibileMassivamente {
    DocumentiPraticaPayload task;
    Boolean successo;
    Throwable errore;

    public DocumentiPraticaPayload getTask() {
      return task;
    }

    public Boolean getSuccesso() {
      return successo;
    }

    public Throwable getErrore() {
      return errore;
    }

  }

  private void uanatacaEndSession(String alias,String pin,String password,String uuidTransaction, String sessionId, String provider) {
    String methodName = "uanatacaEndSession";
    RemoteEndTransactionDto remoteEndTransactionDto = new RemoteEndTransactionDto();
    remoteEndTransactionDto.setSessionId(sessionId);
    remoteEndTransactionDto.setPassword(password);
    remoteEndTransactionDto.setUsername(alias);
    remoteEndTransactionDto.setPin(pin);
    remoteEndTransactionDto.setProvider(provider);

    logger.trace(methodName, TRACE_INPUT_FOR_END_TRANSACTION);
    logger.trace(methodName, CommonUtils.represent(remoteEndTransactionDto));
    try {
      uanatacaExecuteEndSession(remoteEndTransactionDto, uuidTransaction);

    } catch (Exception e) {
      logger.error(methodName, ErrorMessages.FIRMA_ERRORE);
      throw new InternalServerException(e.getMessage());
    }

    logger.trace(methodName, TRACE_OUTPUT_FOR_END_TRANSACTION);
    logger.trace(methodName, NO_RETURN_TYPE_VOID);


  }

  private void remoteEndSession(String alias, String pin, Integer codiceCa, String uuidTransaction, String sessionId) {
    String methodName = "remoteEndSession";
    ContinueTransaction continueTransaction = new ContinueTransaction();

    continueTransaction.setAlias(alias);
    continueTransaction.setAuthData(sessionId);
    continueTransaction.setPin(pin);
    continueTransaction.setCustomerCa(codiceCa);

    logger.trace(methodName, TRACE_INPUT_FOR_END_TRANSACTION);
    logger.trace(methodName, CommonUtils.represent(continueTransaction));
    try {
      executeEndSession(continueTransaction, uuidTransaction);

    } catch (Exception e) {
      logger.error(methodName, ErrorMessages.FIRMA_ERRORE);
      throw new InternalServerException(e.getMessage());
    }

    logger.trace(methodName, TRACE_OUTPUT_FOR_END_TRANSACTION);
    logger.trace(methodName, NO_RETURN_TYPE_VOID);

  }

  private DosignRemoteSignature getPortDosignRemoteSignature() {
    if (portDosignRemoteSignature == null) {
      synchronized (this) {
        if (portDosignRemoteSignature == null) {
          try {
            String endpointUrl = configurazioneService
                .requireConfig(ParametriApplicativo.REMOTESIGN_DOSIGN_URL).asString();
            if (!StringUtils.hasText(endpointUrl)) {
              logger.error("getPortDosignRemoteSignature",
                  String.format(ErrorMessages.FIRMA_NESSUNA_CONFIGURAZIONE_PER_LA_CHIAVE,
                      ParametriApplicativo.REMOTESIGN_DOSIGN_URL));
              throw new BadRequestException(
                  String.format(ErrorMessages.FIRMA_NESSUNA_CONFIGURAZIONE_PER_LA_CHIAVE,
                      ParametriApplicativo.REMOTESIGN_DOSIGN_URL));
            }
            DosignRemoteSignature_Service locator;

            locator = new DosignRemoteSignature_Service(new URL(endpointUrl), SERVICE_SIGNATURE);

            portDosignRemoteSignature = locator.getDosignRemoteSignatureBeanPort();
          } catch (MalformedURLException e) {
            logger.error("getPortDosignRemoteSignature", e.getMessage(), e);
            throw new BadRequestException(e.getMessage());
          }
        }
      }
    }

    return portDosignRemoteSignature;
  }

  private DosignSignatureValidation getPortDosignSignatureValidation() {
    if (portDosignSignatureValidation == null) {
      synchronized (this) {
        if (portDosignSignatureValidation == null) {
          try {
            String endpointUrl = configurazioneService
                .requireConfig(ParametriApplicativo.SIGNATUREVALIDATION_DOSIGN_URL).asString();
            if (!StringUtils.hasText(endpointUrl)) {
              logger.error("getPortDosignSignatureValidation",
                  String.format(ErrorMessages.FIRMA_NESSUNA_CONFIGURAZIONE_PER_LA_CHIAVE,
                      ParametriApplicativo.SIGNATUREVALIDATION_DOSIGN_URL));
              throw new BadRequestException(
                  String.format(ErrorMessages.FIRMA_NESSUNA_CONFIGURAZIONE_PER_LA_CHIAVE,
                      ParametriApplicativo.SIGNATUREVALIDATION_DOSIGN_URL));
            }
            DosignSignatureValidation_Service locator =
                new DosignSignatureValidation_Service(new URL(endpointUrl), SERVICE_VALIDATION);

            portDosignSignatureValidation = locator.getDosignSignatureValidationBeanPort();
          } catch (MalformedURLException e) {
            logger.error("getPortDosignSignatureValidation", e.getMessage(), e);
            throw new BadRequestException(e.getMessage());
          }
        }
      }
    }

    return portDosignSignatureValidation;
  }

  private DosignRemoteService getPortDosignUanatacaRemoteSignature() {
    if (portDosignUanatacaRemoteSignature == null) {
      synchronized (this) {
        if (portDosignUanatacaRemoteSignature == null) {
          try {
            String endpointUrl = configurazioneService
                .requireConfig(ParametriApplicativo.REMOTESIGN_UANATACA_URL).asString();
            if (!StringUtils.hasText(endpointUrl)) {
              logger.error(GET_PORT_UANATACA,
                  String.format(ErrorMessages.FIRMA_NESSUNA_CONFIGURAZIONE_PER_LA_CHIAVE,
                      ParametriApplicativo.REMOTESIGN_UANATACA_URL));
              throw new BadRequestException(
                  String.format(ErrorMessages.FIRMA_NESSUNA_CONFIGURAZIONE_PER_LA_CHIAVE,
                      ParametriApplicativo.REMOTESIGN_UANATACA_URL));
            }
            DosignRemoteService_Service locator;

            locator =
                new DosignRemoteService_Service(new URL(endpointUrl), SERVICE_UANATACA_SIGNATURE);

            portDosignUanatacaRemoteSignature = locator.getDosignRemoteServiceBeanPort();
          } catch (MalformedURLException e) {
            logger.error(GET_PORT_UANATACA, e.getMessage(), e);
            throw new BadRequestException(e.getMessage());
          }
        }
      }
    }

    return portDosignUanatacaRemoteSignature;
  }

  private Dosign getPortDosign() {
    if (portDosign == null) {
      synchronized (this) {
        if (portDosign == null) {
          try {
            String endpointUrl = configurazioneService
                .requireConfig(ParametriApplicativo.DOSIGN_URL).asString();
            if (!StringUtils.hasText(endpointUrl)) {
              logger.error("getPortDosign",
                  String.format(ErrorMessages.FIRMA_NESSUNA_CONFIGURAZIONE_PER_LA_CHIAVE,
                      ParametriApplicativo.DOSIGN_URL));
              throw new BadRequestException(
                  String.format(ErrorMessages.FIRMA_NESSUNA_CONFIGURAZIONE_PER_LA_CHIAVE,
                      ParametriApplicativo.DOSIGN_URL));
            }
            Dosign_Service locator;

            locator = new Dosign_Service(new URL(endpointUrl), SERVICE_DOSIGN);

            portDosign = locator.getDosignBeanPort();
          } catch (MalformedURLException e) {
            logger.error(GET_PORT_UANATACA, e.getMessage(), e);
            throw new BadRequestException(e.getMessage());
          }
        }
      }
    }

    return portDosign;
  }

  private String firmaSingoloDocumento(
      DoSignFirmaRequest request, String sessionId, boolean isLastContent,
      DosignPayloadDTO documentoDaFirmare, UUID identificativoTransazione,
      Long idPratica)
          throws DosignClientException {

    if (idPratica == null) {
      notificaInizioFirmaDocumento(documentoDaFirmare.getId().longValue());
    }
    if (Boolean.TRUE.equals(testMode)) {
      throw new DosignTestModeEnabledException();
    }

    return request.getCodiceEnteCertificatore().equalsIgnoreCase(EnteCertificatore.UANATACA
        .getCodice())
        ? uanatacaFirmaSingoloDocumento(request,sessionId,
            documentoDaFirmare,identificativoTransazione)
            : remoteFirmaSingoloDocumento(request, sessionId, isLastContent, documentoDaFirmare,
                identificativoTransazione);
  }

  private String uanatacaFirmaSingoloDocumento(DoSignFirmaRequest request, String sessionId,
      DosignPayloadDTO documentoDaFirmare, UUID identificativoTransazione)
          throws DosignClientException {
    RemotePdfSignatureDto singleUanatacaPdfSignatureDto = new RemotePdfSignatureDto();
    ByteArrayDataSource dataDs = null;
    var contenutoDocumento = cosmoTContenutoDocumentoRepository
        .findOneNotDeleted(documentoDaFirmare.getContenuto().getId());
    if (contenutoDocumento.isPresent()) {
      dataDs = new ByteArrayDataSource(documentoDaFirmare.getOriginalContent(),
          contenutoDocumento.get().getFormatoFile().getMimeType());
    } else {
      // questa eccezione non dovrebbe mai verificarsi, ma utilizzando un Optional e' corretto verificare questa casistica
      throw new BadRequestException(CONTENUTO_DOCUMENTO_NON_PRESENTE);
    }

    DataHandler dataHandler = new DataHandler(dataDs);

    singleUanatacaPdfSignatureDto.setData(dataHandler);
    singleUanatacaPdfSignatureDto.setPassword(request.getPassword());
    singleUanatacaPdfSignatureDto.setPin(request.getPin());
    singleUanatacaPdfSignatureDto.setSessionId(sessionId);
    singleUanatacaPdfSignatureDto.setUsername(request.getAlias());
    singleUanatacaPdfSignatureDto.setTimestamped(request.isMarcaTemporale().booleanValue());
    singleUanatacaPdfSignatureDto.setFormat(
        request.getProfiloFEQ().equals(ProfiloFEQ.CADES.toString()) ? Format.CADES : Format.XADES_ENVELOPED);
    singleUanatacaPdfSignatureDto.setProvider(request.getProvider());

    String action = documentoDaFirmare.isPrimaFirma() ? PRIMA_FIRMA : "aggiunta firma";
    RemoteSignatureDto signOutput = null;
    try {
      logger.info(UANATACAFIRMASINGOLODOCUMENTO, String.format("Identificativo transazione: %s. %n",
          identificativoTransazione.toString()));
      logger.info(UANATACAFIRMASINGOLODOCUMENTO, String.format("Azione: %s. %n", action));
      logger.info(UANATACAFIRMASINGOLODOCUMENTO, String.format(NOME_DOCUMENTO_DA_FIRMARE,
          documentoDaFirmare.getOriginalFilename()));

      logger.trace(UANATACAFIRMASINGOLODOCUMENTO, String.format(TRACING_INPUT_FOR_OPERATION,
          documentoDaFirmare.isPrimaFirma() ? "sign" : ADD_SIGN));
      logger.trace(UANATACAFIRMASINGOLODOCUMENTO, CommonUtils.represent(singleUanatacaPdfSignatureDto));

      signOutput = documentoDaFirmare.isPrimaFirma()
          ? getPortDosignUanatacaRemoteSignature().sign(singleUanatacaPdfSignatureDto)
              : getPortDosignUanatacaRemoteSignature().addsign(singleUanatacaPdfSignatureDto);

      logger.trace(UANATACAFIRMASINGOLODOCUMENTO, String.format(TRACING_INPUT_FOR_OPERATION,
          documentoDaFirmare.isPrimaFirma() ? "sign" : ADD_SIGN));
      logger.trace(UANATACAFIRMASINGOLODOCUMENTO, CommonUtils.represent(signOutput));

      if (request.getProfiloFEQ().equals(ProfiloFEQ.CADES.toString())
          && documentoDaFirmare.getOriginalFilename() != null
          && !documentoDaFirmare.getOriginalFilename().endsWith("." + signedContentFileExtension)) {
        documentoDaFirmare.setSignedFilename(
            documentoDaFirmare.getOriginalFilename() + "." + signedContentFileExtension);
      } else {
        documentoDaFirmare.setSignedFilename(documentoDaFirmare.getOriginalFilename());
      }

      documentoDaFirmare.setSignedContent(setSignedContentFromInputStream(signOutput));

      logger.info(UANATACAFIRMASINGOLODOCUMENTO, String.format(
          "Documento firmato con successo.%nIdentificativo transazione: %s.%nTentativo di %s del documento %s.",
          identificativoTransazione.toString(), action, documentoDaFirmare.getOriginalFilename()));

    } catch (it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignException_Exception |
        it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignInvalidDataException_Exception |
        it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignInvalidModeException_Exception e) {
      logger.error(UANATACAFIRMASINGOLODOCUMENTOPDF,
          String.format(ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA,
              identificativoTransazione.toString(), PRIMA_FIRMA,
              documentoDaFirmare.getOriginalFilename()));
      logger.error(UANATACAFIRMASINGOLODOCUMENTOPDF, singleUanatacaPdfSignatureDto
          .toString(),
          parseError(e.getMessage()));
      throw new DosignClientException(ErrorMessages.FIRMA_ERRORE, parseError(e.getMessage()));

    } catch (it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignInvalidPinException_Exception e) {
      logger.error(UANATACAFIRMASINGOLODOCUMENTOPDF,
          ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_PIN, parseError(e.getMessage()));
      logger.error(UANATACAFIRMASINGOLODOCUMENTOPDF, singleUanatacaPdfSignatureDto.toString());
      throw new DosignClientException(ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_PIN,
          parseError(e.getMessage()));

    } catch (it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignInvalidOtpException_Exception e) {
      logger.error(UANATACAFIRMASINGOLODOCUMENTOPDF,
          ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_OTP,
          parseError(e.getMessage()));
      logger.error(UANATACAFIRMASINGOLODOCUMENTOPDF, singleUanatacaPdfSignatureDto.toString());
      throw new DosignClientException(ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_OTP,
          parseError(e.getMessage()));
    }

    return signOutput.getSessionId();
  }

  private byte[] setSignedContentFromInputStream(RemoteSignatureDto signOutput) {
    try {
      return IOUtils.toByteArray(signOutput.getData().getInputStream());
    } catch (IOException e) {
      throw new InternalServerException("Impossibile reperire il contenuto del documento");
    }
  }

  private String remoteFirmaSingoloDocumento(DoSignFirmaRequest request, String sessionId,
      boolean isLastContent, DosignPayloadDTO documentoDaFirmare, UUID identificativoTransazione)
          throws DosignClientException {

    SingleRemoteSignatureDto singleRemoteSignatureDto =
        setSignPayload(request, sessionId, isLastContent, documentoDaFirmare);

    CommonRemoteDataDto signOutput = null;
    String action = documentoDaFirmare.isPrimaFirma() ? PRIMA_FIRMA : "aggiunta firma";

    try {

      logger.info(FIRMASINGOLODOCUMENTO, String.format("Identificativo transazione: %s. %n",
          identificativoTransazione.toString()));
      logger.info(FIRMASINGOLODOCUMENTO, String.format("Azione: %s. %n", action));
      logger.info(FIRMASINGOLODOCUMENTO, String.format(NOME_DOCUMENTO_DA_FIRMARE,
          documentoDaFirmare.getOriginalFilename()));

      logger.trace(FIRMASINGOLODOCUMENTO,
          String.format(TRACING_INPUT_FOR_OPERATION,
              documentoDaFirmare.isPrimaFirma() ? "sign" : "addsign"));
      logger.trace(FIRMASINGOLODOCUMENTO, CommonUtils.represent(singleRemoteSignatureDto));



      signOutput = documentoDaFirmare.isPrimaFirma() ? getPortDosignRemoteSignature().sign(singleRemoteSignatureDto)
          : getPortDosignRemoteSignature().addsign(singleRemoteSignatureDto);

      logger.trace(FIRMASINGOLODOCUMENTO,
          String.format(TRACING_OUTPUT_FOR_OPERATION, documentoDaFirmare.isPrimaFirma() ? "sign" : "addsign"));
      logger.trace(FIRMASINGOLODOCUMENTO, CommonUtils.represent(signOutput));

      if (request.getProfiloFEQ().equals(ProfiloFEQ.CADES.toString())
          && documentoDaFirmare.getOriginalFilename() != null
          && !documentoDaFirmare.getOriginalFilename().endsWith("." + signedContentFileExtension)) {
        documentoDaFirmare.setSignedFilename(
            documentoDaFirmare.getOriginalFilename() + "." + signedContentFileExtension);
      } else {
        documentoDaFirmare.setSignedFilename(documentoDaFirmare.getOriginalFilename());
      }

      documentoDaFirmare.setSignedContent(signOutput.getData());

      logger.info(FIRMASINGOLODOCUMENTO, String.format(
          "Documento firmato con successo.%nIdentificativo transazione: %s.%nTentativo di %s del documento %s.",
          identificativoTransazione.toString(), action, documentoDaFirmare.getOriginalFilename()));

    } catch (DosignClientHttpErrorException_Exception
        | DosignCertificateChainNotFoundException_Exception | DosignInvalidDataException_Exception
        | DosignInvalidModeException_Exception | DosignInvalidEncodingException_Exception
        | DosignInvalidEnvelopeException_Exception | DosignException_Exception e) {
      logger.error(FIRMASINGOLODOCUMENTO, String.format(ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA,
          identificativoTransazione.toString(), action, documentoDaFirmare.getOriginalFilename()));
      logger.error(FIRMASINGOLODOCUMENTO, singleRemoteSignatureDto.toString(),
          parseError(e.getMessage()));
      throw new DosignClientException(ErrorMessages.FIRMA_ERRORE, parseError(e.getMessage()));

    } catch (DosignInvalidSignerException_Exception e) {
      logger.error(FIRMASINGOLODOCUMENTO, ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_SIGNER);
      logger.error(FIRMASINGOLODOCUMENTO, singleRemoteSignatureDto.toString(),
          parseError(e.getMessage()));
      throw new DosignClientException(ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_SIGNER,
          parseError(e.getMessage()));

    } catch (DosignInvalidPinException_Exception e) {
      logger.error(FIRMASINGOLODOCUMENTO, ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_PIN);
      logger.error(FIRMASINGOLODOCUMENTO, singleRemoteSignatureDto.toString(),
          parseError(e.getMessage()));
      throw new DosignClientException(ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_PIN,
          parseError(e.getMessage()));

    } catch (DosignInvalidOtpException_Exception e) {
      logger.error(FIRMASINGOLODOCUMENTO, ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_OTP);
      logger.error(FIRMASINGOLODOCUMENTO, singleRemoteSignatureDto.toString(),
          parseError(e.getMessage()));
      throw new DosignClientException(ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_OTP,
          parseError(e.getMessage()));

    } catch (DosignPinLockedException_Exception e) {
      logger.error(FIRMASINGOLODOCUMENTO, ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_PIN_LOCKED);
      logger.error(FIRMASINGOLODOCUMENTO, singleRemoteSignatureDto.toString(),
          parseError(e.getMessage()));
      throw new DosignClientException(ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_PIN_LOCKED,
          parseError(e.getMessage()));

    } catch (Throwable e) {
      logger.error(FIRMASINGOLODOCUMENTO, ErrorMessages.FIRMA_ERRORE, parseError(e.getMessage()));
      throw new DosignClientException(ErrorMessages.FIRMA_ERRORE, parseError(e.getMessage()));
    }

    return signOutput.getAuthData();

  }

  private String firmaSingoloDocumentoPdf(
      DoSignFirmaRequest request, String sessionId, boolean isLastContent, DosignPayloadDTO documentoDaFirmare,
      UUID identificativoTransazione, Long idPratica)
          throws DosignClientException {


    if (idPratica == null) {
      notificaInizioFirmaDocumento(documentoDaFirmare.getId().longValue());
    }
    if (Boolean.TRUE.equals(testMode)) {
      throw new DosignTestModeEnabledException();
    }

    return request.getCodiceEnteCertificatore().equalsIgnoreCase(EnteCertificatore.UANATACA.getCodice())
        ? uanatacaFirmaSingoloDocumentoPdf(request, sessionId, documentoDaFirmare, identificativoTransazione)
            : remoteFirmaSingoloDocumentoPdf(request, sessionId, isLastContent,
                documentoDaFirmare,
                identificativoTransazione);
  }


  private String uanatacaFirmaSingoloDocumentoPdf(DoSignFirmaRequest request, String sessionId,
      DosignPayloadDTO documentoDaFirmare, UUID identificativoTransazione)
          throws DosignClientException {
    RemotePdfSignatureDto singleUanatacaPdfSignatureDto = new RemotePdfSignatureDto();

    ByteArrayDataSource dataDs = null;
    var contenutoDocumento = cosmoTContenutoDocumentoRepository
        .findOneNotDeleted(documentoDaFirmare.getContenuto().getId());
    if (contenutoDocumento.isPresent()) {
      dataDs = new ByteArrayDataSource(documentoDaFirmare.getOriginalContent(),
          contenutoDocumento.get().getFormatoFile().getMimeType());
    } else {
      // questa eccezione non dovrebbe mai verificarsi, ma utilizzando un Optional e' corretto verificare questa casistica
      throw new BadRequestException(CONTENUTO_DOCUMENTO_NON_PRESENTE);
    }
    DataHandler dataHandler = new DataHandler(dataDs);

    singleUanatacaPdfSignatureDto.setData(dataHandler);
    singleUanatacaPdfSignatureDto.setFormat(Format.PADES);
    singleUanatacaPdfSignatureDto.setPassword(request.getPassword());
    singleUanatacaPdfSignatureDto.setPin(request.getPin());
    singleUanatacaPdfSignatureDto.setSessionId(sessionId);
    singleUanatacaPdfSignatureDto.setUsername(request.getAlias());
    singleUanatacaPdfSignatureDto.setTimestamped(request.isMarcaTemporale().booleanValue());
    singleUanatacaPdfSignatureDto.setProvider(request.getProvider());

    RemoteSignatureDto signOutput = null;
    try {
      logger.info(UANATACAFIRMASINGOLODOCUMENTOPDF,
          String.format("Identificativo transazione: %s", identificativoTransazione.toString()));
      logger.info(UANATACAFIRMASINGOLODOCUMENTOPDF, "Azione: prima firma");
      logger.info(UANATACAFIRMASINGOLODOCUMENTOPDF, String.format(NOME_DOCUMENTO_DA_FIRMARE,
          documentoDaFirmare.getOriginalFilename()));
      logger.trace(UANATACAFIRMASINGOLODOCUMENTOPDF,
          String.format(TRACING_INPUT_FOR_OPERATION, "sign"));
      logger.trace(UANATACAFIRMASINGOLODOCUMENTOPDF, CommonUtils.represent(singleUanatacaPdfSignatureDto));

      signOutput = getPortDosignUanatacaRemoteSignature().pdfsign(singleUanatacaPdfSignatureDto);

      logger.trace(
          UANATACAFIRMASINGOLODOCUMENTOPDF,
          String.format(TRACING_OUTPUT_FOR_OPERATION, "sign"));
      logger.trace(UANATACAFIRMASINGOLODOCUMENTOPDF, CommonUtils.represent(signOutput));

      documentoDaFirmare.setSignedFilename(documentoDaFirmare.getOriginalFilename());

      documentoDaFirmare.setSignedContent(setSignedContentFromInputStream(signOutput));

      logger.info(UANATACAFIRMASINGOLODOCUMENTOPDF, String
          .format(
              "Documento firmato con successo.%nIdentificativo transazione: %s.%nTentativo di prima firma del documento %s.",
              identificativoTransazione.toString(), documentoDaFirmare.getOriginalFilename()));

    } catch (it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignException_Exception
        | it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignInvalidDataException_Exception e) {
      logger.error(UANATACAFIRMASINGOLODOCUMENTOPDF,
          String.format(ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA,
              identificativoTransazione.toString(), PRIMA_FIRMA,
              documentoDaFirmare.getOriginalFilename()));
      logger.error(UANATACAFIRMASINGOLODOCUMENTOPDF, singleUanatacaPdfSignatureDto.toString(),
          parseError(e.getMessage()));
      throw new DosignClientException(ErrorMessages.FIRMA_ERRORE, parseError(e.getMessage()));
    } catch (it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignInvalidPinException_Exception e) {
      logger.error(UANATACAFIRMASINGOLODOCUMENTOPDF,
          ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_PIN, parseError(e.getMessage()));
      logger.error(UANATACAFIRMASINGOLODOCUMENTOPDF, singleUanatacaPdfSignatureDto.toString());
      throw new DosignClientException(ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_PIN,
          parseError(e.getMessage()));

    } catch (it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignInvalidOtpException_Exception e) {
      logger.error(UANATACAFIRMASINGOLODOCUMENTOPDF,
          ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_OTP,
          parseError(e.getMessage()));
      logger.error(UANATACAFIRMASINGOLODOCUMENTOPDF, singleUanatacaPdfSignatureDto.toString());
      throw new DosignClientException(ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_OTP,
          parseError(e.getMessage()));
    }

    return signOutput.getSessionId();

  }

  private String remoteFirmaSingoloDocumentoPdf(DoSignFirmaRequest request, String sessionId,
      boolean isLastContent, DosignPayloadDTO documentoDaFirmare, UUID identificativoTransazione)  throws DosignClientException{

    SingleRemotePdfSignatureDto singleRemotePdfSignatureDto = new SingleRemotePdfSignatureDto();
    singleRemotePdfSignatureDto.setAlias(request.getAlias());
    singleRemotePdfSignatureDto.setAuthData(sessionId);
    singleRemotePdfSignatureDto.setLastContent(isLastContent);
    singleRemotePdfSignatureDto.setOtp(request.getOtp());
    singleRemotePdfSignatureDto.setPin(request.getPin());
    singleRemotePdfSignatureDto.setCustomerCa(Integer.valueOf(request.getCodiceCa()));
    singleRemotePdfSignatureDto.setData(documentoDaFirmare.getOriginalContent());
    singleRemotePdfSignatureDto.setTimestamped(request.isMarcaTemporale().booleanValue());
    singleRemotePdfSignatureDto.setMode(mode);
    singleRemotePdfSignatureDto.setCustomerTsa(request.getCodiceTsa());
    singleRemotePdfSignatureDto.setSendingType(otpSendingType);
    singleRemotePdfSignatureDto.setEncoding(encoding);

    XMLGregorianCalendar calendar;
    try {
      calendar = DatatypeFactory.newInstance()
          .newXMLGregorianCalendar((GregorianCalendar) Calendar.getInstance());

      singleRemotePdfSignatureDto.setSigningDate(calendar);
    } catch (DatatypeConfigurationException e1) {
      logger.error(FIRMASINGOLODOCUMENTOPDF, ErrorMessages.FIRMA_ERRORE_CONFIGURAZIONE_DATA, e1);
      throw new DosignClientException(ErrorMessages.FIRMA_ERRORE_CONFIGURAZIONE_DATA,
          parseError(e1.getMessage()));
    }

    CommonRemoteDataDto signOutput = null;
    try {
      logger.info(FIRMASINGOLODOCUMENTOPDF,
          String.format("Identificativo transazione: %s", identificativoTransazione.toString()));
      logger.info(FIRMASINGOLODOCUMENTOPDF, "Azione: prima firma");
      logger.info(FIRMASINGOLODOCUMENTOPDF, String.format(NOME_DOCUMENTO_DA_FIRMARE,
          documentoDaFirmare.getOriginalFilename()));

      logger.trace(FIRMASINGOLODOCUMENTOPDF,
          String.format(TRACING_INPUT_FOR_OPERATION, "sign"));
      logger.trace(FIRMASINGOLODOCUMENTOPDF, CommonUtils.represent(singleRemotePdfSignatureDto));

      signOutput = getPortDosignRemoteSignature().pdfsign(singleRemotePdfSignatureDto);

      logger.trace(FIRMASINGOLODOCUMENTOPDF,
          String.format(TRACING_OUTPUT_FOR_OPERATION, "sign"));
      logger.trace(FIRMASINGOLODOCUMENTOPDF, CommonUtils.represent(signOutput));

      documentoDaFirmare.setSignedFilename(documentoDaFirmare.getOriginalFilename());
      documentoDaFirmare.setSignedContent(signOutput.getData());

      logger.info(FIRMASINGOLODOCUMENTOPDF, String.format(
          "Documento firmato con successo.%nIdentificativo transazione: %s.%nTentativo di prima firma del documento %s.",
          identificativoTransazione.toString(), documentoDaFirmare.getOriginalFilename()));

    } catch (DosignClientHttpErrorException_Exception
        | DosignCertificateChainNotFoundException_Exception | DosignInvalidDataException_Exception
        | DosignInvalidEncodingException_Exception | DosignException_Exception e) {
      logger.error(FIRMASINGOLODOCUMENTOPDF,
          String.format(ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA,
              identificativoTransazione.toString(), PRIMA_FIRMA,
              documentoDaFirmare.getOriginalFilename()));
      logger.error(FIRMASINGOLODOCUMENTOPDF, singleRemotePdfSignatureDto.toString(),
          parseError(e.getMessage()));
      throw new DosignClientException(ErrorMessages.FIRMA_ERRORE, parseError(e.getMessage()));

    } catch (DosignInvalidSignerException_Exception e) {
      logger.error(FIRMASINGOLODOCUMENTOPDF,
          ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_SIGNER, parseError(e.getMessage()));
      logger.error(FIRMASINGOLODOCUMENTOPDF, singleRemotePdfSignatureDto.toString());
      throw new DosignClientException(ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_SIGNER,
          parseError(e.getMessage()));

    } catch (DosignInvalidPinException_Exception e) {
      logger.error(FIRMASINGOLODOCUMENTOPDF, ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_PIN,
          parseError(e.getMessage()));
      logger.error(FIRMASINGOLODOCUMENTOPDF, singleRemotePdfSignatureDto.toString());
      throw new DosignClientException(ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_PIN,
          parseError(e.getMessage()));

    } catch (DosignInvalidOtpException_Exception e) {
      logger.error(FIRMASINGOLODOCUMENTOPDF, ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_OTP,
          parseError(e.getMessage()));
      logger.error(FIRMASINGOLODOCUMENTOPDF, singleRemotePdfSignatureDto.toString());
      throw new DosignClientException(ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_INVALID_OTP,
          parseError(e.getMessage()));

    } catch (DosignPinLockedException_Exception e) {
      logger.error(FIRMASINGOLODOCUMENTOPDF, ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_PIN_LOCKED,
          parseError(e.getMessage()));
      logger.error(FIRMASINGOLODOCUMENTOPDF, singleRemotePdfSignatureDto.toString());
      throw new DosignClientException(ErrorMessages.FIRMA_ERRORE_DURANTE_FIRMA_PIN_LOCKED,
          parseError(e.getMessage()));

    } catch (Throwable e) {
      logger.error(FIRMASINGOLODOCUMENTOPDF, singleRemotePdfSignatureDto.toString(),
          parseError(e.getMessage()));
      throw new DosignClientException(ErrorMessages.FIRMA_ERRORE, parseError(e.getMessage()));
    }

    return signOutput.getAuthData();
  }

  private DosignOutcomeDTO parseError(String errorMessage) {
    String methodName = "parseError";

    DosignOutcomeDTO errorInfo = null;
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      if (null == errorMessage) {
        logger.error(methodName, ErrorMessages.FIRMA_NESSUN_ERRORE_RESTITUITO);
        throw new BadRequestException(ErrorMessages.FIRMA_NESSUN_ERRORE_RESTITUITO);
      }
      if (errorMessage.startsWith("{")) {
        errorInfo = objectMapper.readValue(errorMessage, DosignOutcomeDTO.class);
      } else {
        errorInfo = new DosignOutcomeDTO();
        errorInfo.setDescription(errorMessage);
      }
    } catch (IOException e) {
      logger.error(methodName, ErrorMessages.FIRMA_ERRORE_DURANTE_PARSING_MESSAGGIO_ERRORE, e);
      logger.error(methodName,
          String.format(ErrorMessages.FIRMA_MESSAGGIO_ORIGINALE_ERRORE, errorMessage));
    }
    return errorInfo;
  }

  private CommonRemoteDataDto executeStartTransaction(StartTransactionDto startTransactionDto)
      throws DosignInvalidSignerException_Exception, DosignClientHttpErrorException_Exception,
      DosignException_Exception, DosignCertificateChainNotFoundException_Exception,
      DosignInvalidPinException_Exception, DosignInvalidOtpException_Exception,
      DosignPinLockedException_Exception {

    if (Boolean.TRUE.equals(testMode)) {
      throw new DosignTestModeEnabledException();
    }

    return getPortDosignRemoteSignature().startTransaction(startTransactionDto);

  }

  private String executeUanatacaStartTransaction(RemoteStartTransactionDto startTransactionDto)
      throws it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignInvalidPinException_Exception,
      it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignInvalidOtpException_Exception,
      it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignException_Exception {

    if (Boolean.TRUE.equals(testMode)) {
      throw new DosignTestModeEnabledException();
    }

    return getPortDosignUanatacaRemoteSignature().startTransaction(startTransactionDto);

  }

  private void executeUanatacaEndTransaction(RemoteEndTransactionDto endTransactionDto)
      throws it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignInvalidPinException_Exception,
      it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignInvalidOtpException_Exception,
      it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignException_Exception {

    if (Boolean.TRUE.equals(testMode)) {
      throw new DosignTestModeEnabledException();
    }

    getPortDosignUanatacaRemoteSignature().endTransaction(endTransactionDto);

  }

  private void executeEndTransaction(ContinueTransactionDto continueTransactionDto)
      throws DosignInvalidSignerException_Exception, DosignClientHttpErrorException_Exception,
      DosignException_Exception, DosignInvalidPinException_Exception,
      DosignInvalidOtpException_Exception, DosignPinLockedException_Exception {

    if (Boolean.TRUE.equals(testMode)) {
      throw new DosignTestModeEnabledException();
    }
    getPortDosignRemoteSignature().endTransaction(continueTransactionDto);
  }


  private RequestAttributes getCurrentRequestAttributes() {
    try {
      return RequestContextHolder.currentRequestAttributes();
    } catch (IllegalStateException e) {
      return null;
    }
  }

  private  List<DosignPayloadDTO> firmaDocumenti(List<DosignPayloadDTO> documentiDaFirmare,
      DoSignFirmaRequest request, Long idPratica, String sessionId) {
    String methodName = "Firma Documenti";
    UUID identificativoTransazioneFirma = java.util.UUID.randomUUID();
    boolean isProfiloPades = request.getProfiloFEQ().equals(ProfiloFEQ.PADES.toString());
    String authDataTransaction = sessionId;

    try {
      if (documentiDaFirmare.size() > 1) {

        int i = 1;
        for (DosignPayloadDTO documentoDaFirmare : documentiDaFirmare) {

          if (isProfiloPades) {

            try {
              authDataTransaction = firmaSingoloDocumentoPdf(
                  request, authDataTransaction, i == documentiDaFirmare.size(), documentoDaFirmare,
                  identificativoTransazioneFirma, idPratica);

            } catch (DosignTestModeEnabledException eTest) {
              logger.error(methodName,
                  ErrorMessages.FIRMA_FIRMASINGOLODOCUMENTO_DOSIGN_IN_MODALITA_TEST_NESSUNA_OPERAZIONE_EFFETTUATA,
                  eTest);
              documentoDaFirmare.setSignedContent(documentoDaFirmare.getOriginalContent());
              documentoDaFirmare.setSignedFilename(documentoDaFirmare.getOriginalFilename() + TEST);
            }
          } else {


            try {
              String result = firmaSingoloDocumento(request,
                  authDataTransaction, i == documentiDaFirmare.size(), documentoDaFirmare,
                  identificativoTransazioneFirma, idPratica);
              authDataTransaction = result;

            } catch (DosignTestModeEnabledException eTest) {
              logger.error(methodName,
                  ErrorMessages.FIRMA_FIRMASINGOLODOCUMENTO_DOSIGN_IN_MODALITA_TEST_NESSUNA_OPERAZIONE_EFFETTUATA,
                  eTest);
              documentoDaFirmare.setSignedContent(documentoDaFirmare.getOriginalContent());
              documentoDaFirmare.setSignedFilename(documentoDaFirmare.getOriginalFilename() + TEST);
            }
          }

          i++;
        }

        logger.info(methodName, String.format(
            "Identificativo transazione: %s.%nTransazione di firma terminata con successo.%nDocumenti firmati: %d",
            identificativoTransazioneFirma.toString(), documentiDaFirmare.size()));

      } else {
        if (isProfiloPades) {

          DosignPayloadDTO documentoDaFirmare = documentiDaFirmare.get(0);

          try {
            firmaSingoloDocumentoPdf(request, authDataTransaction, true, documentiDaFirmare.get(0),
                identificativoTransazioneFirma, idPratica);

          } catch (DosignTestModeEnabledException eTest) {
            logger.error(methodName,
                ErrorMessages.FIRMA_FIRMASINGOLODOCUMENTO_DOSIGN_IN_MODALITA_TEST_NESSUNA_OPERAZIONE_EFFETTUATA,
                eTest);
            documentoDaFirmare.setSignedContent(documentoDaFirmare.getOriginalContent());
            documentoDaFirmare.setSignedFilename(documentoDaFirmare.getOriginalFilename() + TEST);
          }
        } else {

          DosignPayloadDTO documentoDaFirmare = documentiDaFirmare.get(0);

          try {

            firmaSingoloDocumento(request, authDataTransaction, true, documentoDaFirmare,
                identificativoTransazioneFirma, idPratica);

          } catch (DosignTestModeEnabledException eTest) {
            logger.error(methodName,
                ErrorMessages.FIRMA_FIRMASINGOLODOCUMENTO_DOSIGN_IN_MODALITA_TEST_NESSUNA_OPERAZIONE_EFFETTUATA,
                eTest);
            documentoDaFirmare.setSignedContent(documentoDaFirmare.getOriginalContent());
            documentoDaFirmare
            .setSignedFilename(documentoDaFirmare.getOriginalFilename() + TEST);
          }
        }
      }
    } catch (Exception e) {
      logger.error(methodName, ErrorMessages.FIRMA_ERRORE, parseError(e.getMessage()));
      notificaErroreFirmaDocumenti(parseError(e.getMessage()));
      throw new InternalServerException(e.getMessage(),
          new DosignClientException(e.getMessage(), parseError(e.getMessage())));

    }

    logger.info(methodName, String.format(
        "Tentativo di firma digitale completato con successo.%nIdentificativo transazione: %s",
        identificativoTransazioneFirma.toString()));
    return documentiDaFirmare;
  }

  private void salvaDocumentoFirmato(Long idPratica,
      DosignPayloadDTO dosignPayload, String profiloFEQ, int numDocumento) {

    var attempt =
        transactionService.inTransaction(() -> tentaSalvataggio(dosignPayload, profiloFEQ));
    if (idPratica == null) {
      if (attempt.success()) {
        notificaDocumentoFirmato(dosignPayload.getContenuto().getId(), numDocumento);
      } else {
        notificaErroreFirmaDocumenti(parseError(attempt.getError().getMessage()));
      }
    }

  }

  private void tentaSalvataggio( DosignPayloadDTO dosignPayload, String profiloFEQ) {
    final String methodName = "tentaSalvataggio";
    CosmoTContenutoDocumento contenutoOriginale = cosmoTContenutoDocumentoRepository
        .findOneNotDeleted(dosignPayload.getId().longValue()).orElseThrow();

    CosmoRFormatoFileProfiloFeqTipoFirma rFormatoFileProfiloFeqTipoFirma =
        cosmoRFormatoFileProfiloFeqTipoFirmaRepository
        .findOneByCosmoDFormatoFileCodiceAndCosmoDProfiloFeqCodice(
            contenutoOriginale.getFormatoFile().getCodice(), profiloFEQ);

    if (rFormatoFileProfiloFeqTipoFirma == null) {
      String message =
          String.format(ErrorMessages.TIPO_FIRMA_PER_FORMATO_FILE_E_PROFILO_FEQ_NON_TROVATA,
              contenutoOriginale.getFormatoFile().getCodice(), profiloFEQ);
      logger.error(methodName, message);
      throw new InternalServerException(message);

    }
    CosmoDFormatoFile formatoFile = getFormatoFile(rFormatoFileProfiloFeqTipoFirma);

    Entity documentoIndex = null;

    if (TipoContenutoDocumento.FIRMATO.toString()
        .equals(contenutoOriginale.getTipo().getCodice())) {
      documentoIndex = api2IndexService.find(contenutoOriginale.getUuidNodo(), false);

      if (documentoIndex == null) {
        logger.error(methodName, ErrorMessages.REPERIMENTO_DOCUMENTO_INDEX);
        throw new InternalServerException(ErrorMessages.REPERIMENTO_DOCUMENTO_INDEX);
      }
      documentoIndex.setContent(dosignPayload.getSignedContent());
      api2IndexService.aggiorna(documentoIndex);

    } else {

      CosmoTPratica pratica = contenutoOriginale.getDocumentoPadre().getPratica();

      Entity documentoDaCreareSuIndex = buildDocumentoIndex(contenutoOriginale
          .getDocumentoPadre(),
          dosignPayload.getSignedFilename(), formatoFile.getCodice());

      ContenutoEntity contenutoEntity = new ContenutoEntity();
      contenutoEntity.setEntity(documentoDaCreareSuIndex);
      contenutoEntity.setContent(dosignPayload.getSignedContent());
      documentoIndex = api2IndexService.create(pratica.getUuidNodo(), contenutoEntity.getEntity(), contenutoEntity.getContent());
    }

    var creato = contenutoDocumentoService.creaContenutoFirmato(contenutoOriginale,
        documentoIndex,
        rFormatoFileProfiloFeqTipoFirma.getCosmoDTipoFirma(), dosignPayload.getSignedFilename(),
        formatoFile);

    if (TipoContenutoDocumento.FIRMATO.toString()
        .equals(contenutoOriginale.getTipo().getCodice())) {
      contenutoOriginale.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));
      contenutoOriginale
      .setUtenteCancellazione(SecurityUtils.getUtenteCorrente().getCodiceFiscale());
      cosmoTContenutoDocumentoRepository.save(contenutoOriginale);
    }
    creato.setShaFile(contenutoDocumentoService.generaSha256PerFile(documentoIndex.getContent()));
    creato = cosmoTContenutoDocumentoRepository.save(creato);

    contenutoDocumentoService.verificaFirma(creato);
  }

  private CosmoDFormatoFile getFormatoFile(CosmoRFormatoFileProfiloFeqTipoFirma formatoProfiloTipoFirma) {
    String methodName = "getFormatoFile";
    CosmoDTipoFirma tipoFirma =
        cosmoDTipoFirmaRepository
        .findOneActiveByField(CosmoDTipoFirma_.codice, formatoProfiloTipoFirma.getCosmoDTipoFirma().getCodice()).orElseThrow(() -> {
          logger.error(methodName, ErrorMessages.FIRMA_TIPO_FIRMA_NON_TROVATA);
          throw new InternalServerException(ErrorMessages.FIRMA_TIPO_FIRMA_NON_TROVATA);
        });

    String codiceFormato = tipoFirma.getMimeType() != null ? tipoFirma.getMimeType()
        : formatoProfiloTipoFirma.getCosmoDFormatoFile().getCodice();

    return cosmoDFormatoFileRepository
        .findOneActiveByField(CosmoDFormatoFile_.codice, codiceFormato).orElseThrow(() -> {
          logger.error(methodName, ErrorMessages.FIRMA_FORMATO_DOCUMENTO_FIRMATO_NON_TROVATO);
          throw new InternalServerException(
              ErrorMessages.FIRMA_FORMATO_DOCUMENTO_FIRMATO_NON_TROVATO);
        });

  }

  private Entity buildDocumentoIndex(CosmoTDocumento documentoDB, String nomeOriginale,
      String contentType) {
    Entity documentoIndex = new Entity();
    documentoIndex.setDescrizione(documentoDB.getDescrizione());
    documentoIndex.setFilename(addNamePostfix(nomeOriginale, "-firmato"));
    documentoIndex.setIdDocumento(documentoDB.getId());
    documentoIndex.setMimeType(contentType);
    documentoIndex
    .setTipoDocumento(documentoDB.getTipo() != null ? documentoDB.getTipo().getCodice() : null);

    return documentoIndex;
  }

  private String addNamePostfix(String name, String postfix) {
    if (!name.contains(".")) {
      return name + postfix + "";
    }
    int position = name.lastIndexOf(".");
    String onlyName = name.substring(0, position);
    String onlyExt = name.substring(position + 1);
    return onlyName + postfix + "." + onlyExt;
  }

  protected TransactionExecutionResult<Void> inTransaction(Runnable task) {
    return transactionService.inTransaction(task);
  }

  private void lockAndThanSign(DoSignFirmaMassivaRequest request,
      DocumentiPraticaPayload task, LongTask<Serializable> step, String authData) {

    var attivita = cosmoTAttivitaRepository.findOne(task.getIdAttivita());

    String lockResourceCode = "@task(" + attivita.getTaskId() + ")";

    CosmoTLock[] locks = {null};


    String lockOwner = UUID.randomUUID().toString();

    step.step("Acquisizione del lock", substep -> {

      var lockRequest = new LockAcquisitionRequest();
      lockRequest.codiceOwner = lockOwner;
      lockRequest.codiceRisorsa = lockResourceCode;
      lockRequest.durata = 300000L;
      lockRequest.ritardoRetry = 250L;
      lockRequest.timeout = 5000L;

      var lockAcquisitionResult = lockService.acquire(lockRequest);

      if (lockAcquisitionResult.acquired) {
        locks[0] = lockAcquisitionResult.lock;
      } else {
        throw new ConflictException(lockAcquisitionResult.reason);
      }

      substep.sleep(500);
      return null;
    });

    try {
      step.step("Elaborazione del task", substep -> {
        doFirmaMassiva(request, task, attivita, locks[0].getCodiceOwner(), authData, step);
      });
    } finally {
      logger.trace("firmaMassiva", "Firma con lock - Rilascio del lock");

      step.step("Rilascio del lock", substep -> {
        lockService.release(locks[0]);
      });
    }
  }

  private void doFirmaMassiva(
      DoSignFirmaMassivaRequest request,
      DocumentiPraticaPayload task, CosmoTAttivita attivita, String codiceOwner,
      String authData, LongTask<Serializable> step) {

    step.step(FIRMA_DOCUMENTI, substep -> {

      DoSignFirmaRequest commonInfoRequest = new DoSignFirmaRequest();
      commonInfoRequest.setAlias(request.getAlias());
      commonInfoRequest.setCodiceCa(request.getCodiceCa());
      commonInfoRequest.setCodiceTsa(request.getCodiceTsa());
      commonInfoRequest.setMarcaTemporale(request.isMarcaTemporale());
      commonInfoRequest.setNotificaFirma(request.isNotificaFirma());
      commonInfoRequest.setOtp(request.getOtp());
      commonInfoRequest.setPin(request.getPin());
      commonInfoRequest.setPassword(request.getPassword());
      commonInfoRequest.setProfiloFEQ(request.getProfiloFEQ());
      commonInfoRequest.setUuidTransaction(request.getUuidTransaction());
      commonInfoRequest.setCodiceEnteCertificatore(request.getCodiceEnteCertificatore());
      commonInfoRequest.setProvider(request.getProvider());

      List<DosignPayloadDTO> documentiDTO =
          dosignMapper.toDosignPayloads(task.getDocumentiDaFirmare());
      var documentiFirmati =
          firmaDocumenti(documentiDTO, commonInfoRequest, task.getIdPratica(), authData);
      int count = 0;
      for (DosignPayloadDTO docFirmato : documentiFirmati) {

        salvaDocumentoFirmato(task.getIdPratica(), docFirmato,
            request.getProfiloFEQ(), ++count);
      }

      if (null != request.isMarcaTemporale() && request.isMarcaTemporale().equals(Boolean.TRUE)) {
        aggiornamentoContatoreMarcaTemporale(request.getCodiceTsa(), count);
      }

    });

    try {
      step.step("Esecuzione azione post firma", substep -> {
        azioniPostFirma(task, request, attivita, codiceOwner);
      });
    } finally {
      logger.trace("doFirmaMassiva", "Azione post firma - fine");
    }

  }

  private void azioniPostFirma( DocumentiPraticaPayload task,
      DoSignFirmaMassivaRequest request, CosmoTAttivita attivita, String codiceOwner) {

    Long idIstanza = task.getIdFunzionalita();

    // leggo la configurazione per sapere in che variabili salvare il risultato

    var istanza = cosmoTIstanzaFunzionalitaFormLogicoRepository.findOne(idIstanza);

    var parametri = getValoriParametri(istanza);

    var outputFirma = parametri.get(CONFIG_FIRMA_KEY).asObject();

    var lavorazioneRequest = new Task();
    List<Object> variables = new ArrayList<>();
    lavorazioneRequest.setVariables(variables);

    variables.add(
        variable(outputFirma.get(CONFIG_FIRMA_RISULTATO_FIRMA_KEY).asText(), String.valueOf(true)));
    variables.add(
        variable(outputFirma.get(CONFIG_FIRMA_NOTE_FIRMA_KEY).asText(), request.getNote().strip()));

    if (Boolean.FALSE.equals(request.isProcessCarryOn())) {
      var riferimento = cosmoBusinessPraticheFeignClient
          .postPraticaAttivitaSalvaConLock(task.getIdPratica(),
              attivita.getId(),
              lavorazioneRequest, codiceOwner);
      asyncTaskService.wait(riferimento.getUuid(), 600L);
    } else {
      //@formatter:off
      RiferimentoOperazioneAsincrona riferimento = null;
      riferimento = cosmoBusinessPraticheFeignClient.postPraticaAttivitaConfermaConLock(
          task.getIdPratica(),
          attivita.getId(),
          lavorazioneRequest,
          codiceOwner
          );
      asyncTaskService.wait(riferimento.getUuid(), 600L);
      //@formatter:on


    }

  }

  private Map<String, ValoreParametroFormLogicoWrapper> getValoriParametri(
      CosmoTIstanzaFunzionalitaFormLogico istanza) {

    //@formatter:off
    return istanza.getCosmoRIstanzaFormLogicoParametroValores().stream()
        .filter(CosmoREntity::valido)
        .map(ValoreParametroFormLogicoWrapper::new)
        .collect(Collectors.toMap(ValoreParametroFormLogicoWrapper::getCodice, e -> e));
    //@formatter:on
  }

  protected Object variable(String name, String value) {
    var output = new HashMap<String, Object>();
    output.put("name", name);
    output.put("value", value);
    return output;
  }

  private void notificaInzioFirmaDocumentiPratica(Long idPratica, Long idAttivita) {
    final var method = "notificaInzioFirmaDocumentiPratica";

    var pratica = cosmoTPraticaRepository.findOneNotDeleted(idPratica).orElseThrow();

    var attivita = pratica.getAttivita().stream().filter(a -> a.getId().equals(idAttivita))
        .findFirst().orElseThrow();

    var result = this.inTransaction(() -> {
      Map<String, Object> payload = new HashMap<>();
      payload.put(OGGETTO_PRATICA, pratica.getOggetto());
      payload.put("descrizioneAttivita", attivita.getDescrizione());
      payload.put(STATO, "STARTED");

      eventService.broadcastEvent(Constants.EVENTS.PRATICA_INIZIO_FIRMA, payload);
    });

    if (result.failed()) {
      logger.error(method, "Errore durante la notifica di inizio firma pratica", result.getError());
    }
  }

  private void notificaFineFirmaDocumentiPratica(Long idPratica, Long idAttivita) {
    final var method = "notificaInzioFirmaDocumentiPratica";

    var pratica = cosmoTPraticaRepository.findOneNotDeleted(idPratica).orElseThrow();

    var attivita = pratica.getAttivita().stream().filter(a -> a.getId().equals(idAttivita))
        .findFirst().orElseThrow();

    var result = this.inTransaction(() -> {
      Map<String, Object> payload = new HashMap<>();
      payload.put(OGGETTO_PRATICA, pratica.getOggetto());
      payload.put("descrizioneAttivita", attivita.getDescrizione());
      payload.put(STATO, COMPLETED);

      eventService.broadcastEvent(Constants.EVENTS.PRATICA_FINE_FIRMA, payload);
    });

    if (result.failed()) {
      logger.error(method, "Errore durante la notifica di fine firma pratica", result.getError());
    }
  }

  private void notificaPraticaDocumentoErrore(Long idPratica, Long idAttivita, Throwable errore) {
    final var method = "notificaPraticaDocumentoErrore";

    var pratica = cosmoTPraticaRepository.findOneNotDeleted(idPratica).orElseThrow();

    var attivita = pratica.getAttivita().stream().filter(a -> a.getId().equals(idAttivita))
        .findFirst().orElseThrow();

    var result = this.inTransaction(() -> {
      Map<String, Object> payload = new HashMap<>();
      payload.put(OGGETTO_PRATICA, pratica.getOggetto());
      payload.put("descrizioneAttivitaAttivita", attivita.getDescrizione());
      payload.put(STATO, "FAILED");
      payload.put(ERRORE, errore.getCause() + " " + errore.getMessage());

      eventService.broadcastEvent(Constants.EVENTS.PRATICA_ERRORI_FIRMA, payload);
    });

    if (result.failed()) {
      logger.error(method, "Errore durante la notifica dei documenti firmati", result.getError());
    }
  }

  private void notificaErroriPreliminari(Throwable errore) {
    final var method = "notificaErroriPreliminari";

    var result = this.inTransaction(() -> {
      Map<String, Object> payload = new HashMap<>();
      payload.put(ERRORE, errore.getCause() + " " + errore.getMessage());

      eventService.broadcastEvent(Constants.EVENTS.FIRMA_ERRORI_PRELIMINARI, payload);
    });

    if (result.failed()) {
      logger.error(method, "Errore durante la notifica degli errori preliminari", result.getError());
    }
  }

  private List<CosmoTContenutoDocumento> getListaContenutiDaFirmare(List<Documento> documenti, String profiloFEQ) {
    String methodName = "getListaContenutiDaFirmare";
    List<CosmoTContenutoDocumento> listaContenuti = new LinkedList<>();
    documenti.forEach(documento -> {

      List<ContenutoDocumento> contenuti = documento.getContenuti().stream()
          .filter( contenuto -> contenuto.getDtCancellazione() == null &&
          (TipoContenutoDocumento.ORIGINALE.toString().equals(contenuto.getTipo().getCodice())||
              TipoContenutoDocumento.FIRMATO.toString().equals(contenuto.getTipo().getCodice())))
          .sorted(Comparator.comparing(ContenutoDocumento::getDtInserimento).reversed())
          .limit(1)
          .filter(contenuto -> (TipoContenutoDocumento.ORIGINALE.toString().equals(contenuto.getTipo().getCodice()) &&
              (contenuto.getInfoVerificaFirme().isEmpty() || contenuto.getInfoVerificaFirme().stream()
                  .noneMatch(firma -> SecurityUtils.getUtenteCorrente().getCodiceFiscale()
                      .equals(firma.getCodiceFiscaleFirmatario())))) ||
              (TipoContenutoDocumento.FIRMATO.toString().equals(contenuto.getTipo().getCodice())
                  && contenuto.getInfoVerificaFirme().stream()
                  .noneMatch(firma -> SecurityUtils.getUtenteCorrente().getCodiceFiscale().equals(firma.getCodiceFiscaleFirmatario()))))
          .collect(Collectors.toList());

      // verifica esistenza ultimo contenuto
      if (contenuti == null || contenuti.isEmpty()) {
        logger.error(methodName, ErrorMessages.FIRMA_ERRORE);
        Throwable e = new BadRequestException(CONTENUTO_DOCUMENTO_NON_PRESENTE);
        notificaErroriPreliminari(e);
        throw new BadRequestException("Contenuto documenti errato");
      }

      //verifica coerenza tra il formato del documento (ultimo contenuto salvato) e il profilo FEQ selezionato
      CosmoRFormatoFileProfiloFeqTipoFirma rFormatoFileProfiloFeqTipoFirma = cosmoRFormatoFileProfiloFeqTipoFirmaRepository.findOneByCosmoDFormatoFileCodiceAndCosmoDProfiloFeqCodice(
          contenuti.get(0).getFormatoFile().getCodice(), profiloFEQ);
      if (rFormatoFileProfiloFeqTipoFirma == null) {
        String message = ErrorMessages.FORMATO_DOCUMENTI_NON_COMPATIBILE_FORMATO_FEQ_SELEZIONATO;
        logger.error(methodName, message);
        Throwable e = new InternalServerException(message);
        notificaErroriPreliminari(e);
        throw new InternalServerException(message);
      }
      listaContenuti.add(cosmoTContenutoDocumentoRepository.findOne(contenuti.get(0).getId()));
    });

    return listaContenuti;
  }

  private void notificaInizioFirmaDocumento(long idContenuto) {
    String method = "notificaInizioFirmaDocumento";
    var documento = cosmoTDocumentoRepository
        .findOneByContenutiIdAndDtCancellazioneNull(idContenuto);

    var result = this.inTransaction(() -> {
      Map<String, Object> payload = new HashMap<>();
      payload.put("descrizioneDocumento", !StringUtils.isEmpty(documento.getTitolo()) ? documento.getTitolo() : getContenutoOriginale(documento.getContenuti()));
      payload.put(STATO, "STARTED");

      eventService.broadcastEvent(Constants.EVENTS.DOCUMENTI_INIZIO_FIRMA, payload);
    });

    if (result.failed()) {
      logger.error(method, "Errore durante inizio firma documento con id: " + documento.getId(), result.getError());
    }

  }

  private String getContenutoOriginale(List<CosmoTContenutoDocumento> contenuti) {
    var contenutoOriginale = contenuti.stream()
        .filter(
            c -> c.getTipo().getCodice().equalsIgnoreCase(TipoContenutoDocumento.ORIGINALE.name()))
        .findFirst().orElseThrow();
    return contenutoOriginale.getNomeFile();
  }

  private void notificaErroreFirmaDocumenti(DosignOutcomeDTO parsedError) {
    final var method = "notificaErroreFirmaDocumenti";


    var result = this.inTransaction(() -> {
      Map<String, Object> payload = new HashMap<>();
      payload.put(STATO, "FAILED");
      payload.put(ERRORE, parsedError.getDescription());

      eventService.broadcastEvent(Constants.EVENTS.FIRMA_ERRORE_DOCUMENTO, payload);
    });

    if (result.failed()) {
      logger.error(method, "Errore durante la firma documenti", result.getError());
    }
  }

  private void notificaDocumentoFirmato(Long idContenuto, int numDoc) {
    String method = "notificaDocumentoFirmato";
    var documento =
        cosmoTDocumentoRepository.findOneByContenutiIdAndDtCancellazioneNull(idContenuto);

    var result = this.inTransaction(() -> {
      Map<String, Object> payload = new HashMap<>();
      payload.put("descrizioneDocumento",
          !StringUtils.isEmpty(documento.getTitolo()) ? documento.getTitolo() : "");
      payload.put("numeroDocumento", numDoc);
      payload.put(STATO, "COMPLETED");

      eventService.broadcastEvent(Constants.EVENTS.DOCUMENTI_FIRMATI, payload);
    });

    if (result.failed()) {
      logger.error(method,
          "Errore durante salvataggio documento firmato con id: "
              + documento.getId(),
              result.getError());
    }

  }

  private void notificaFineFirmaDocumenti() {
    final var method = "notificaFineFirmaDocumenti";


    var result = this.inTransaction(() -> {
      Map<String, Object> payload = new HashMap<>();
      payload.put(STATO, "COMPLETED");

      eventService.broadcastEvent(Constants.EVENTS.DOCUMENTI_FINE_FIRMA, payload);
    });

    if (result.failed()) {
      logger.error(method, "Errore durante la notifica di fine firma documenti", result.getError());
    }
  }

  private String infoDoSignTransaction(String alias, String pin, String codiceCa, String otp, String idTransazione, int numeroDocumenti) {
    String methodName = "infoDoSignTransaction";
    CommonRemoteData startTransactionOutput = null;

    StartTransaction startTransaction = new StartTransaction();
    startTransaction.setAlias(alias);
    startTransaction.setPin(pin);
    startTransaction.setOtp(otp);
    startTransaction.setCustomerCa(Integer.valueOf(codiceCa));
    startTransaction.setMaxTranSize(numeroDocumenti);
    try {
      startTransactionOutput = executeStartSession(startTransaction, idTransazione);
    } catch (Exception e) {
      notificaErroreFirmaDocumenti(parseError(e.getMessage()));
      logger.error(methodName, ErrorMessages.FIRMA_ERRORE);
      throw new InternalServerException(e.getMessage());
    }

    logger.trace(methodName, "Output metodo 'startTransaction'");
    logger.trace(methodName, CommonUtils.represent(startTransactionOutput));
    logger.info(methodName, String
        .format("Identificativo transazione: %s.%nTransazione inizializzata.", idTransazione));

    return startTransactionOutput.getAuthData();
  }


  private void aggiornamentoContatoreMarcaTemporale(String codiceTsa,
      int numeroDocumentiFirmati) {
    UserInfoDTO userInfo = SecurityUtils.getUtenteCorrente();
    long year = Calendar.getInstance().get(Calendar.YEAR);
    CosmoDEnteCertificatore enteCertificatore = cosmoDEnteCertificatoreRepository
        .findOneActiveByField(CosmoDEnteCertificatore_.codiceTsa, codiceTsa).orElseThrow();
    CosmoREnteCertificatoreEnte enteCertificatoreEnteAnnoCorrente =
        cosmoREnteCertificatoreEnteRepository
        .findOneByCosmoTEnteIdAndCosmoDEnteCertificatoreCodiceAndAnno(
            userInfo.getEnte().getId(), enteCertificatore.getCodice(), year);
    if (enteCertificatoreEnteAnnoCorrente == null) {
      CosmoREnteCertificatoreEnte enteCertificatoreEnte = new CosmoREnteCertificatoreEnte();
      CosmoREnteCertificatoreEntePK enteCertificatoreEntePk = new CosmoREnteCertificatoreEntePK();
      enteCertificatoreEntePk.setAnno(year);
      enteCertificatoreEntePk.setCodiceEnteCertificatore(enteCertificatore.getCodice());
      enteCertificatoreEntePk.setIdEnte(userInfo.getEnte().getId());
      enteCertificatoreEnte.setId(enteCertificatoreEntePk);
      enteCertificatoreEnte.setNumeroMarcheTemporali((long) numeroDocumentiFirmati);
      enteCertificatoreEnte.setDtInizioVal(Timestamp.valueOf(LocalDateTime.now()));
      cosmoREnteCertificatoreEnteRepository.save(enteCertificatoreEnte);
      CosmoREnteCertificatoreEnte enteCertificatoreEnteAnnoPassato =
          cosmoREnteCertificatoreEnteRepository
          .findOneByCosmoTEnteIdAndCosmoDEnteCertificatoreCodiceAndAnno(
              userInfo.getEnte().getId(), enteCertificatore.getCodice(), year - 1);
      if (enteCertificatoreEnteAnnoPassato != null) {
        enteCertificatoreEnteAnnoPassato
        .setDtFineVal(Timestamp.valueOf(LocalDateTime.now()));
        cosmoREnteCertificatoreEnteRepository.save(enteCertificatoreEnteAnnoPassato);
      }
    } else {
      enteCertificatoreEnteAnnoCorrente.setNumeroMarcheTemporali(
          enteCertificatoreEnteAnnoCorrente.getNumeroMarcheTemporali() + numeroDocumentiFirmati);
      cosmoREnteCertificatoreEnteRepository.save(enteCertificatoreEnteAnnoCorrente);
    }
  }

  private DosignOutcomeDTO richiediOTPUanataca(RichiediOTPRequest body) {
    String methodName = "richiediOTPUanataca";
    CommonUtils.requireString(body.getPassword(), "password");

    RemoteOtpDto uanatacaDto = new RemoteOtpDto();
    uanatacaDto.setUsername(body.getAlias());
    uanatacaDto.setPin(body.getPin());
    uanatacaDto.setPassword(body.getPassword());
    uanatacaDto.setProvider(body.getProvider());

    DosignOutcomeDTO outcome = new DosignOutcomeDTO();

    try {

      logger.trace(methodName, "Tracing input for operation 'pushOtp'");
      logger.trace(methodName, CommonUtils.represent(uanatacaDto));

      if (Boolean.TRUE.equals(testMode)) {
        throw new DosignTestModeEnabledException();
      }
      getPortDosignUanatacaRemoteSignature().pushOtp(uanatacaDto);

      logger.trace(methodName, "Tracing output for operation 'pushOtp'");
      logger.trace(methodName, NO_RETURN_TYPE_VOID);

      outcome.setDescription(DoSignService.OTP_REQUESTED);
      outcome.setReturnCode(DoSignService.DEFAULT_INFO_RETURN_CODE);
      outcome.setStatus(DoSignService.DEFAULT_INFO_STATUS);

    } catch (it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignException_Exception
        | it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignInvalidDataException_Exception
        | it.doqui.dosign.dosign.business.session.dosign.remotev2.DosignInvalidAuthenticationException_Exception e) {

      logger.error(methodName, ErrorMessages.FIRMA_ERRORE_RICHIESTA_OTP, e);
      throw new InternalServerException(e.getMessage(), new DosignClientException(
          ErrorMessages.FIRMA_ERRORE_RICHIESTA_OTP, parseError(e.getMessage())));

    } catch (DosignTestModeEnabledException eTest) {

      logger.error(methodName,
          ErrorMessages.FIRMA_PUSHOTP_DOSIGN_IN_MODALITA_TEST_NESSUNA_OPERAZIONE_EFFETTUATA, eTest);
      throw new InternalServerException(
          ErrorMessages.FIRMA_PUSHOTP_DOSIGN_IN_MODALITA_TEST_NESSUNA_OPERAZIONE_EFFETTUATA, eTest);
    }
    return outcome;
  }

  private DosignOutcomeDTO richiediOTPInternal(RichiediOTPRequest body) {
    String methodName = "richiediOTPInternal";
    CommonUtils.requireString(body.getCodiceCa(), "codiceCa");

    BaseRemoteSignatureDto inputDto = new BaseRemoteSignatureDto();
    inputDto.setAlias(body.getAlias());
    inputDto.setCustomerCa(Integer.valueOf(body.getCodiceCa()));
    inputDto.setPin(body.getPin());
    inputDto.setSendingType(otpSendingType);

    DosignOutcomeDTO outcome = new DosignOutcomeDTO();

    try {

      logger.trace(methodName, "Tracing input for operation 'pushOtp'");
      logger.trace(methodName, CommonUtils.represent(inputDto));

      if (Boolean.TRUE.equals(testMode)) {
        throw new DosignTestModeEnabledException();
      }
      getPortDosignRemoteSignature().pushOtp(inputDto);

      logger.trace(methodName, "Tracing output for operation 'pushOtp'");
      logger.trace(methodName, NO_RETURN_TYPE_VOID);

      outcome.setDescription(DoSignService.OTP_REQUESTED);
      outcome.setReturnCode(DoSignService.DEFAULT_INFO_RETURN_CODE);
      outcome.setStatus(DoSignService.DEFAULT_INFO_STATUS);

    } catch (DosignException_Exception | DosignInvalidDataException_Exception
        | DosignInvalidAuthenticationException_Exception e) {

      logger.error(methodName, ErrorMessages.FIRMA_ERRORE_RICHIESTA_OTP, e);
      throw new InternalServerException(e.getMessage(), new DosignClientException(
          ErrorMessages.FIRMA_ERRORE_RICHIESTA_OTP, parseError(e.getMessage())));

    } catch (DosignTestModeEnabledException eTest) {

      logger.error(methodName,
          ErrorMessages.FIRMA_PUSHOTP_DOSIGN_IN_MODALITA_TEST_NESSUNA_OPERAZIONE_EFFETTUATA, eTest);
      throw new InternalServerException(
          ErrorMessages.FIRMA_PUSHOTP_DOSIGN_IN_MODALITA_TEST_NESSUNA_OPERAZIONE_EFFETTUATA, eTest);
    }
    return outcome;
  }

  private String getFirmaUanatacaSessionId(String alias, String pin, String password, String otp, String idTransazione, String provider) {
    String methodName = "getFirmaUanatacaSessionId";
    RemoteStartTransactionDto startTransactionDto = new RemoteStartTransactionDto();
    String ret = "";

    startTransactionDto.setOtp(otp);
    startTransactionDto.setPassword(password);
    startTransactionDto.setUsername(alias);
    startTransactionDto.setPin(pin);
    startTransactionDto.setProvider(provider);
    try {
      ret = uanatacaExecuteStartSession(startTransactionDto, idTransazione);
    } catch (Exception e) {
      notificaErroriPreliminari(e);
      logger.error(methodName, ErrorMessages.FIRMA_ERRORE);
      throw new InternalServerException(e.getMessage());
    }

    logger.trace(methodName, "Output metodo 'startTransaction'");
    logger.trace(methodName, ret);
    logger.info(methodName, String
        .format("Identificativo transazione: %s.%nTransazione inizializzata.", idTransazione));

    return ret;
  }

  private List<CosmoTContenutoDocumento> getContenutiDocumenti(List<Documento> documenti) {

    String methodName = "getContenutiDocumenti";
    List<CosmoTContenutoDocumento> listaContenuti = new LinkedList<>();
    documenti.forEach(documento -> {

      List<ContenutoDocumento> contenuti = documento.getContenuti().stream().filter(
          contenuto -> contenuto.getDtCancellazione() == null
          && (contenuto.getFormatoFile().getCodice().equals("application/pdf")
              || isXml(contenuto.getFormatoFile()))
          && (TipoContenutoDocumento.ORIGINALE
              .toString().equals(contenuto.getTipo().getCodice())
              || TipoContenutoDocumento.FIRMATO.toString().equals(contenuto.getTipo().getCodice())))
          .sorted(Comparator.comparing(ContenutoDocumento::getDtInserimento).reversed()).limit(1)
          .filter(contenuto -> (TipoContenutoDocumento.ORIGINALE.toString()
              .equals(contenuto.getTipo().getCodice())
              && (contenuto.getInfoVerificaFirme().isEmpty()
                  || contenuto.getInfoVerificaFirme().stream()
                  .noneMatch(firma -> SecurityUtils.getUtenteCorrente().getCodiceFiscale()
                      .equals(firma.getCodiceFiscaleFirmatario()))))
              || (TipoContenutoDocumento.FIRMATO.toString().equals(contenuto.getTipo().getCodice())
                  && contenuto.getInfoVerificaFirme().stream()
                  .noneMatch(firma -> SecurityUtils.getUtenteCorrente().getCodiceFiscale()
                      .equals(firma.getCodiceFiscaleFirmatario()))))
          .collect(Collectors.toList());

      if (contenuti == null || contenuti.isEmpty()) {
        logger.error(methodName, ErrorMessages.FIRMA_ERRORE);
        Throwable e = new BadRequestException(CONTENUTO_DOCUMENTO_NON_PRESENTE);
        notificaErroriPreliminari(e);
        throw new BadRequestException("Contenuto documenti errato");
      }

      listaContenuti.add(cosmoTContenutoDocumentoRepository.findOne(contenuti.get(0).getId()));
    });

    return listaContenuti;

  }

  private DosignPayloadDTO apponiSigilloSingoloDocumento(DosignPayloadDTO body, DoSignSigilloRequest dati)
      throws DosignClientException {

    final var methodName = "apponiSigilloSingoloDocumento";
    SigilloSignatureDto request = new SigilloSignatureDto();
    request.setData(body.getOriginalContent());
    request.setDelegatedDomain(dati.getDelegatedDomain());
    request.setDelegatedPassword(dati.getDelegatedPassword());
    request.setDelegatedUser(dati.getDelegatedUser());
    request
    .setType(body.getContenuto().getFormatoFile().getCodice().equals("application/pdf")
        ? Constants.TYPEDOSIGN.PDF
            : Constants.TYPEDOSIGN.XML);
    request.setOtpPwd(dati.getOtpPwd());
    request.setTypeHSM(dati.getTipoHsm());
    request.setTypeOtpAuth(dati.getTipoOtpAuth());
    request.setUser(dati.getUser());

    logger.info(methodName,
        "Inizio apposizione sigillo elettronico per documento con id: " + body.getId());

    byte[] sealedDoc = null;

    try {
      if (Boolean.TRUE.equals(testMode)) {
        throw new DosignTestModeEnabledException();
      }
      sealedDoc = getPortDosign().sigillo(request);
      body.setSignedFilename(body.getOriginalFilename());
      body.setSignedContent(sealedDoc);
      logger.info(methodName,
          "Apposto sigillo su documento " + body.getContenuto().getDocumentoPadre().getId());
    } catch (it.doqui.dosign.dosign.business.session.dosign.DosignException_Exception e) {
      logger.error(methodName, ErrorMessages.SIGILLO_ERRORE_COMUNICAZIONE_DOSIGN);
      throw new DosignClientException(ErrorMessages.SIGILLO_ERRORE_COMUNICAZIONE_DOSIGN, parseError(e.getMessage()));
    } catch (DosignTestModeEnabledException eTest) {
      logger.error(methodName,
          ErrorMessages.SIGILLO_APPONI_SIGILLO_DOSIGN_IN_MODALITA_TEST_NESSUNA_OPERAZIONE_EFFETTUATA,
          eTest);
    }

    logger.info(methodName,
        "Fine apposizione sigillo elettronico per documento con id: " + body.getId());

    return body;

  }

  private boolean isXml(FormatoFile formato) {
    return formato.getDescrizione().contains("XML");
  }

  private void salvaDocumentoSigillato(DosignPayloadDTO dosignPayload) {
    final String methodName = "salvaDocumentoSigillato";

    Entity documentoIndex = null;

    CosmoTContenutoDocumento contenutoOriginale = cosmoTContenutoDocumentoRepository
        .findOneNotDeleted(dosignPayload.getId().longValue()).orElseThrow();

    if (TipoContenutoDocumento.FIRMATO.toString()
        .equals(contenutoOriginale.getTipo().getCodice())) {
      documentoIndex = api2IndexService.find(contenutoOriginale.getUuidNodo(), false);

      if (documentoIndex == null) {
        logger.error(methodName, ErrorMessages.REPERIMENTO_DOCUMENTO_INDEX);
        throw new InternalServerException(ErrorMessages.REPERIMENTO_DOCUMENTO_INDEX);
      }
      documentoIndex.setContent(dosignPayload.getSignedContent());
      api2IndexService.aggiorna(documentoIndex);

    } else {

      CosmoTPratica pratica = contenutoOriginale.getDocumentoPadre().getPratica();

      Entity documentoDaCreareSuIndex = buildDocumentoIndex(contenutoOriginale.getDocumentoPadre(),
          dosignPayload.getSignedFilename(), contenutoOriginale.getFormatoFile().getCodice());

      ContenutoEntity contenutoEntity = new ContenutoEntity();
      contenutoEntity.setEntity(documentoDaCreareSuIndex);
      contenutoEntity.setContent(dosignPayload.getSignedContent());
      documentoIndex = api2IndexService.create(pratica.getUuidNodo(), contenutoEntity.getEntity(),
          contenutoEntity.getContent());
    }

    var creato = contenutoDocumentoService.creaContenutoSigilloElettronico(contenutoOriginale, documentoIndex);

    if (TipoContenutoDocumento.FIRMATO.toString()
        .equals(contenutoOriginale.getTipo().getCodice())) {
      contenutoOriginale.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));
      contenutoOriginale
      .setUtenteCancellazione(SecurityUtils.getUtenteCorrente().getCodiceFiscale());
      cosmoTContenutoDocumentoRepository.save(contenutoOriginale);
    }
    creato.setShaFile(contenutoDocumentoService.generaSha256PerFile(documentoIndex.getContent()));
    creato = cosmoTContenutoDocumentoRepository.save(creato);

    contenutoDocumentoService.verificaFirma(creato);

  }

  private SingleRemoteSignatureDto setSignPayload(DoSignFirmaRequest request, String sessionId,
      boolean isLastContent, DosignPayloadDTO documentoDaFirmare) throws DosignClientException {
    var payload = new SingleRemoteSignatureDto();
    payload.setAlias(request.getAlias());
    payload.setAuthData(sessionId);
    payload.setLastContent(isLastContent);
    payload.setOtp(request.getOtp());
    payload.setPin(request.getPin());

    payload.setCustomerCa(Integer.valueOf(request.getCodiceCa()));
    if (documentoDaFirmare.isPrimaFirma()) {
      payload.setData(documentoDaFirmare.getOriginalContent());
    } else {
      payload.setEnvelope(documentoDaFirmare.getOriginalContent());
    }
    payload.setTimestamped(request.isMarcaTemporale().booleanValue());
    payload.setMode(mode);
    payload.setCustomerTsa(request.getCodiceTsa());
    payload.setSendingType(otpSendingType);
    payload.setEncoding(encoding);

    XMLGregorianCalendar calendar;
    try {
      calendar = DatatypeFactory.newInstance()
          .newXMLGregorianCalendar((GregorianCalendar) Calendar.getInstance());

      payload.setSigningDate(calendar);
    } catch (DatatypeConfigurationException e1) {
      logger.error(FIRMASINGOLODOCUMENTO, ErrorMessages.FIRMA_ERRORE_CONFIGURAZIONE_DATA, e1);
      throw new DosignClientException(ErrorMessages.FIRMA_ERRORE);
    }

    return payload;
  }

  private List<DosignPayloadDTO> getDosignPayloadFromIndex(List<CosmoTContenutoDocumento> contenuti) {
    String methodName = "getDosignPayloadFromIndex";
    List<DosignPayloadDTO> ret = new LinkedList<>();
    for (CosmoTContenutoDocumento contenuto : contenuti) {
      boolean documentoFirmato = contenuto.getTipoFirma() != null;
      // recupero del contenuto (contenuto fisico)
      Entity documentoIndex = api2IndexService.find(contenuto.getUuidNodo(), true);
      if (null == documentoIndex) {
        var parametroNonValorizzato =
            String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "documentoIndex");
        logger.error(methodName, parametroNonValorizzato);
        throw new InternalServerException(parametroNonValorizzato);
      }

      DosignPayloadDTO documentoDaFirmare = DosignPayloadDTO.builder()
          .withOriginalContent(documentoIndex.getContent())
          .withOriginalFilename(contenuto.getNomeFile())
          .withPrimaFirma(!documentoFirmato)
          .withContenuto(contenuto)
          .withId(contenuto.getId().intValue()).build();
      ret.add(documentoDaFirmare);
    }
    return ret;
  }

}

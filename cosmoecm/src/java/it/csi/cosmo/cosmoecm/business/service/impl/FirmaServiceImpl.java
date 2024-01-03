/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoDEnteCertificatore;
import it.csi.cosmo.common.entities.CosmoDEnteCertificatore_;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTCredenzialiSigilloElettronico_;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.enums.SceltaMarcaTemporale;
import it.csi.cosmo.common.entities.enums.TipoContenutoDocumento;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmoecm.business.service.FirmaService;
import it.csi.cosmo.cosmoecm.config.Constants;
import it.csi.cosmo.cosmoecm.config.ErrorMessages;
import it.csi.cosmo.cosmoecm.dto.EsitoRichiestaSigilloElettronicoDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.FirmaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RichiestaOTPRequest;
import it.csi.cosmo.cosmoecm.integration.mapper.CosmoTDocumentoMapper;
import it.csi.cosmo.cosmoecm.integration.mapper.CycleAvoidingMappingContext;
import it.csi.cosmo.cosmoecm.integration.mapper.DosignMapper;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDEnteCertificatoreRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTCredenzialiSigilloElettronicoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapDosignFeignClient;
import it.csi.cosmo.cosmoecm.util.ContenutoTemporaneoException;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;
import it.csi.cosmo.cosmosoap.dto.rest.DoSignFirmaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.DoSignSigilloRequest;
import it.csi.cosmo.cosmosoap.dto.rest.RichiediOTPRequest;
import it.csi.cosmo.cosmosoap.dto.rest.SigillaDocumentoResponse;


@Service
@Transactional
public class FirmaServiceImpl implements FirmaService {

  @Autowired
  private CosmoSoapDosignFeignClient doSignFeignClient;

  @Autowired
  private CosmoDEnteCertificatoreRepository cosmoDEnteCertificatoreRepository;

  @Autowired
  private DosignMapper dosignMapper;

  @Autowired
  CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CosmoTCredenzialiSigilloElettronicoRepository cosmoTCredenzialiSigilloElettronicoRepository;

  @Autowired
  private CosmoTDocumentoMapper cosmoTDocumentoMapper;

  @Autowired
  private CosmoTDocumentoRepository cosmoTDocumentoRepository;

  private static final String CLASS_NAME = FirmaServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Override
  public void richiediOTP(RichiestaOTPRequest request) {
    String methodName = "richiediOTP";

    CosmoDEnteCertificatore cosmoDEnteCertificatore =
        cosmoDEnteCertificatoreRepository.findOneActiveByField(CosmoDEnteCertificatore_.codice,
            request.getCodiceEnteCertificatore())
            .orElseThrow(() -> {
              logger.error(methodName, ErrorMessages.FIRMA_ERRORE_ENTE_CERTIFICATORE_NON_PRESENTE);
              throw new BadRequestException(ErrorMessages.FIRMA_ERRORE_ENTE_CERTIFICATORE_NON_PRESENTE);});

    try {
      RichiediOTPRequest otpRequest = new RichiediOTPRequest();
      otpRequest.setAlias(request.getAlias());
      otpRequest.setPin(request.getPin());
      otpRequest.setCodiceCa(cosmoDEnteCertificatore.getCodiceCa());
      otpRequest.setPassword(request.getPassword());
      otpRequest.setCodiceEnteCertificatore(cosmoDEnteCertificatore.getCodice());
      otpRequest.setProvider(cosmoDEnteCertificatore.getProvider());
      doSignFeignClient.richiediOTP(otpRequest);
    } catch (Exception e) {
      logger.error(methodName, ErrorMessages.FIRMA_ERRORE_RICHIESTA_OTP, e);
      throw new InternalServerException(ErrorMessages.FIRMA_ERRORE_RICHIESTA_OTP);
    }

  }

  @Override
  public void firma(FirmaRequest firmaRequest) {
    String methodName = "firma";
    // Verificare che la data di scadenza del certificato, se valorizzata, non sia superata
    if (firmaRequest.getCertificato() != null
        && firmaRequest.getCertificato().getDataScadenza() != null) {
      if (firmaRequest.getCertificato().getDataScadenza().isBefore(OffsetDateTime.now())) {
        logger.error(methodName, ErrorMessages.FIRMA_ERRORE_CERTIFICATO_SCADUTO);
        throw new BadRequestException(ErrorMessages.FIRMA_ERRORE_CERTIFICATO_SCADUTO);
      }
    }
    //@formatter:off
    String profiloFEQ = firmaRequest.getCertificato().getProfiloFEQ().getCodice();

    CosmoDEnteCertificatore enteCertificatore = cosmoDEnteCertificatoreRepository
        .findOneActive(firmaRequest.getCertificato().getEnteCertificatore().getCodice()).orElseThrow(() -> {
          String notFound = String.format(ErrorMessages.FIRMA_ENTE_CERTIFICATOR_NON_PRESENTE_O_NON_ATTIVO,
              firmaRequest.getCertificato().getEnteCertificatore().getCodice());
          logger.error(methodName, notFound);
          throw new NotFoundException(notFound);
        });

    boolean marcaTemporale = false;

    marcaTemporale = firmaRequest.getCertificato().getSceltaMarcaTemporale().getCodice().equals(SceltaMarcaTemporale.SI.toString());
    try {


      List<it.csi.cosmo.cosmosoap.dto.rest.Documento> documenti =
          dosignMapper.toDocumentiSoap(firmaRequest.getDocumenti());

     String identificativoTransazione = java.util.UUID.randomUUID().toString();

      DoSignFirmaRequest doSignFirmaRequest = new DoSignFirmaRequest();
      doSignFirmaRequest.setAlias(firmaRequest.getCertificato().getUsername());
      doSignFirmaRequest.setPin(firmaRequest.getCertificato().getPin());
      doSignFirmaRequest.setOtp(firmaRequest.getOtp());
      doSignFirmaRequest.setUuidTransaction(identificativoTransazione);
      doSignFirmaRequest.setDocumentiDaFirmare(documenti);
      doSignFirmaRequest.setCodiceCa(enteCertificatore.getCodiceCa());
      doSignFirmaRequest.setCodiceTsa(enteCertificatore.getCodiceTsa());
      doSignFirmaRequest.setProfiloFEQ(profiloFEQ);
      doSignFirmaRequest.setMarcaTemporale(marcaTemporale);
      doSignFirmaRequest.setNotificaFirma(true);
      doSignFirmaRequest.setPassword(firmaRequest.getCertificato().getPassword());
      doSignFirmaRequest.setCodiceEnteCertificatore(enteCertificatore.getCodice());
      doSignFirmaRequest.setProvider(enteCertificatore.getProvider());
      doSignFeignClient.firma(doSignFirmaRequest);

    } catch (Exception e) {
      String error = e.getMessage() != null ? e.getCause() + " " + e.getMessage() : ErrorMessages.FIRMA_ERRORE;
      logger.error(methodName, error);
      throw new InternalServerException(error);
    }
    //@formatter:on
  }

  @Override
  public EsitoRichiestaSigilloElettronicoDocumento apponiSigilloElettronico(CosmoTDocumento documento, String alias) {
    final var methodName = "apponiSigilloElettronico";

    documento = cosmoTDocumentoRepository.findOne(documento.getId());

    var optCredenzialiSigillo =
        cosmoTCredenzialiSigilloElettronicoRepository.findOneNotDeletedByField(CosmoTCredenzialiSigilloElettronico_.alias,
            alias);

    if (optCredenzialiSigillo.isEmpty()) {
      logger.error(methodName,
          String.format("Le credenziali con alias %s non sono presenti a sistema", alias));
      return new EsitoRichiestaSigilloElettronicoDocumento(Constants.ERR_CREDENZIALI_SIGILLO,
          "Le credenziali per l'apposizione del sigillo elettronico sono errate. verificare alias");
    }

    var credenzialiSigillo = optCredenzialiSigillo.get();


    // recupero del contenuto (metadati cosmo)
    CosmoTContenutoDocumento contenutoDocumento = null;
    try {
      contenutoDocumento = getContenutoDocumento(documento);
    } catch (ContenutoTemporaneoException e) {
      logger.error(methodName,
          String.format(
              "Il documento con id %d non e' ancora stato archiviato su Index, verra' ignorato",
              documento.getId()));
      return new EsitoRichiestaSigilloElettronicoDocumento(Constants.ESITO_RICHIESTA_APPOSIZIONE_SIGILLO_TMP, null);
    }

    logger.info(methodName, String.format("Avvio richiesta apposizione sigillo del documento %s",
        contenutoDocumento.getNomeFile()));

    var result = new SigillaDocumentoResponse();

    try {
      // richiesta di smistamento
      DoSignSigilloRequest soapRequest = new DoSignSigilloRequest();
      soapRequest.setDelegatedDomain(credenzialiSigillo.getDelegatedDomain());
      soapRequest.setDelegatedPassword(credenzialiSigillo.getDelegatedPassword());
      soapRequest.setDelegatedUser(credenzialiSigillo.getDelegatedUser());
      soapRequest.setOtpPwd(credenzialiSigillo.getOtpPwd());
      soapRequest.setTipoHsm(credenzialiSigillo.getTipoHsm());
      soapRequest.setTipoOtpAuth(credenzialiSigillo.getTipoOtpAuth());
      soapRequest.setDocumento(dosignMapper.toDocumentoSoap(cosmoTDocumentoMapper.toDTOLight(documento, new CycleAvoidingMappingContext())));
      result = doSignFeignClient.postDosignSigillo(soapRequest);
      logger.info(methodName,String.format(
          "Richiesta apposizione sigillo del documento %s inviata a doSign. Nome file : %s",
          contenutoDocumento.getId(), contenutoDocumento.getNomeFile()));
      logger.info(methodName, String.format("Codice risposta: %s%nMessaggio risposta: %s",
          result.getCodice(), result.getMessaggio()));
    } catch (Exception e) {
      logger.error(methodName, "Errore nella chiamata a doSign");
      logger.error(methodName, e.getMessage(), e);
      return new EsitoRichiestaSigilloElettronicoDocumento("800", e.getMessage());
    }

    return new EsitoRichiestaSigilloElettronicoDocumento(result.getCodice(), result.getMessaggio());

  }

  /*
   * Controlli formali e recupero del contenuto del documento
   */
  private CosmoTContenutoDocumento getContenutoDocumento(CosmoTDocumento documento) {
    final var methodName = "checkInputApposizioneSigilloDocumento";

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
            .orElseGet(() -> documento.findContenuto(TipoContenutoDocumento.ORIGINALE));

    if (null == contenutoDocumento || null == contenutoDocumento.getUuidNodo()) {
      var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "contenuto documento");
      logger.error(methodName, parametroNonValorizzato);
      throw new ContenutoTemporaneoException(parametroNonValorizzato);
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

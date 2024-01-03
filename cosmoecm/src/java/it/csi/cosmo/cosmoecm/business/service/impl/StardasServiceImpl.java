/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.emory.mathcs.backport.java.util.Arrays;
import it.csi.cosmo.common.dto.common.ConfigurazioneDTO;
import it.csi.cosmo.common.dto.rest.FilterCriteria;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.CosmoDStatoSmistamento;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoRSmistamentoDocumento;
import it.csi.cosmo.common.entities.CosmoRTipoDocumentoTipoDocumento;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento_;
import it.csi.cosmo.common.entities.CosmoTInfoAggiuntiveSmistamento;
import it.csi.cosmo.common.entities.CosmoTSmistamento;
import it.csi.cosmo.common.entities.enums.TipoContenutoDocumento;
import it.csi.cosmo.common.entities.enums.TipoNotifica;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmoecm.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoecm.business.service.MailService;
import it.csi.cosmo.cosmoecm.business.service.StardasService;
import it.csi.cosmo.cosmoecm.config.Constants;
import it.csi.cosmo.cosmoecm.config.ErrorMessages;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo;
import it.csi.cosmo.cosmoecm.dto.rest.CodiceTipologiaDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.Esito;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoSmistaDocumentoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoSmistamentoResponse;
import it.csi.cosmo.cosmoecm.dto.rest.FiltroRicercaDocumentiDTO;
import it.csi.cosmo.cosmoecm.dto.rest.InformazioneAggiuntivaSmistamento;
import it.csi.cosmo.cosmoecm.dto.rest.RichiediSmistamentoRequest;
import it.csi.cosmo.cosmoecm.dto.stardas.EsitoRichiestaSmistamentoDocumento;
import it.csi.cosmo.cosmoecm.dto.stardas.StatoSmistamento;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDStatoSmistamentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoRSmistamentoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTInfoAggiuntiveSmistamentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTSmistamentoRespository;
import it.csi.cosmo.cosmoecm.integration.repository.specifications.CosmoTDocumentoSpecifications;
import it.csi.cosmo.cosmoecm.integration.repository.specifications.CosmoTSmistamentoSpecifications;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoBusinessPraticheFeignClient;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoNotificationsNotificheGlobaliFeignClient;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapStardasFeignClient;
import it.csi.cosmo.cosmoecm.security.SecurityUtils;
import it.csi.cosmo.cosmoecm.util.ContenutoTemporaneoException;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;
import it.csi.cosmo.cosmonotifications.dto.rest.NotificheGlobaliRequest;
import it.csi.cosmo.cosmosoap.dto.rest.ConfigurazioneChiamante;
import it.csi.cosmo.cosmosoap.dto.rest.DatiSmistaDocumento;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoElettronico;
import it.csi.cosmo.cosmosoap.dto.rest.EmbeddedJSON;
import it.csi.cosmo.cosmosoap.dto.rest.Result;
import it.csi.cosmo.cosmosoap.dto.rest.SmistaDocumentoRequest;
import it.csi.cosmo.cosmosoap.dto.rest.SmistaDocumentoResponse;

/**
 *
 */
@Service
@Transactional
public class StardasServiceImpl implements StardasService {

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "StardasServiceImpl");

  private static final String LOG_STATO_PRESENTE_ATTIVO_SU_DB = "Lo stato %s e' presente e attivo su db?";

  private static final String ERRORE_DURANTE_BROADCAST_NOTIFICHE = "Si e' verificato un errore durante il broadcast delle notifiche";

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoTDocumentoRepository cosmoTDocumentoRepository;

  @Autowired
  private CosmoDStatoSmistamentoRepository cosmoDStatoSmistamentoRepository;

  @Autowired
  private CosmoTSmistamentoRespository cosmoTSmistamentoRespository;

  @Autowired
  private CosmoRSmistamentoDocumentoRepository cosmoRSmistamentoDocumentoRepository;

  @Autowired
  private CosmoTInfoAggiuntiveSmistamentoRepository cosmoTInfoAggiuntiveSmistamentoRepository;

  @Autowired
  private CosmoBusinessPraticheFeignClient businessPraticheFeignClient;

  @Autowired
  private CosmoNotificationsNotificheGlobaliFeignClient notificheFeignClient;

  @Autowired
  private CosmoSoapStardasFeignClient stardasFeignClient;

  @Autowired
  protected MailService mailService;

  /*
   * Smistamento del documento
   *
   * @see it.csi.cosmo.cosmoecm.business.service.StardasService#smistaDocumento(it.csi.cosmo.common.
   * entities.CosmoTDocumento)
   */
  @Override
  public EsitoRichiestaSmistamentoDocumento smistaDocumento(CosmoTDocumento documento) {
    final var methodName = "smistaDocumento";
    documento = cosmoTDocumentoRepository.findOne(documento.getId());

    // recupero del contenuto (metadati cosmo)
    CosmoTContenutoDocumento contenutoDocumento = null;
    try {
      contenutoDocumento = getContenutoDocumento(documento);
    } catch (ContenutoTemporaneoException e) {
      logger.error(methodName,
          String.format(
              "Il documento con id %d non e' ancora stato archiviato su Index, verra' ignorato",
              null != documento && null != documento.getId() ? documento.getId() : 0));
      return new EsitoRichiestaSmistamentoDocumento(Constants.ESITO_RICHIESTA_SMISTAMENTO_TMP, null,
          null);
    }

    logger.info(methodName, String.format("Avvio richiesta smistamento del documento %s",
        contenutoDocumento.getNomeFile()));


    // il codice fruitore deve essere configurato su db (in alternativa dentro al file di
    // properties)
    String codiceFruitore = getCodiceFruitore(documento.getPratica().getTipo());

    if (null == codiceFruitore) {
      var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "codiceFruitore");
      logger.error(methodName, parametroNonValorizzato);
      throw new InternalServerException(parametroNonValorizzato);
    }

    var result = new SmistaDocumentoResponse();
    result.setResult(new Result());

    try {
      // creazione dell'oggetto request per Stardas
      SmistaDocumentoRequest requestSmistamento =
          buildRequestSmistamento(documento, contenutoDocumento, codiceFruitore);

      // richiesta di smistamento
      result = stardasFeignClient.smistamentoDocumento(requestSmistamento);
      logger.info(methodName,
          String.format(
              "Richiesta smistamento del documento %s inviata a Stardas. UUID di risposta: %s",
              contenutoDocumento.getNomeFile(), result.getMessageUUID()));
      logger.info(methodName, String.format("Codice risposta: %s%nMessaggio risposta: %s",
          result.getResult().getCodice(), result.getResult().getMessaggio()));
    } catch (Exception e) {
      logger.error(methodName, "Errore nella chiamata a stardas");
      logger.error(methodName, e.getMessage(), e);
      return new EsitoRichiestaSmistamentoDocumento(null, e.getMessage(), null);
    }

    return new EsitoRichiestaSmistamentoDocumento(result.getResult().getCodice(),
        result.getResult().getMessaggio(), result.getMessageUUID());

  }

  /*
   * Recupero dei documenti da smistare
   *
   * @see
   * it.csi.cosmo.cosmoecm.business.service.StardasService#recuperaDocumentiDaSmistare(java.lang.
   * Long, int)
   */
  @Override
  public List<CosmoTDocumento> recuperaDocumentiDaSmistare(Long idPratica,
      int principaliOAllegati) {
    Integer maxNumRetry = configurazioneService != null
        ? configurazioneService
            .requireConfig(ParametriApplicativo.BATCH_SMISTAMENTO_STARDAS_MAXNUMRETRY).asInteger()
            : null;
    return cosmoTDocumentoRepository.findAll(
        CosmoTDocumentoSpecifications.findDaSmistare(idPratica, principaliOAllegati, maxNumRetry));
  }

  /*
   * Scrittura su cosmo_t_smistamento e cosmo_r_smistamento_documento
   *
   * @see
   * it.csi.cosmo.cosmoecm.business.service.StardasService#richiediSmistamento(java.lang.String,
   * java.lang.String)
   */
  @Override
  public void richiediSmistamento(String idPratica, RichiediSmistamentoRequest body) {
    final var methodName = "richiediSmistamento";

    this.checkPreliminariRichiestaSmistamento(idPratica, body);

    logger.info(methodName,
        String.format("Ricevuta richiesta di smistamento per la pratica %s, relativa all'evento %s",
            idPratica, body.getIdentificativoMessaggio()));

    // recupero dei documenti
    GenericRicercaParametricaDTO<FiltroRicercaDocumentiDTO> ricercaParametrica =
        new GenericRicercaParametricaDTO<>();

    FiltroRicercaDocumentiDTO filter = new FiltroRicercaDocumentiDTO();
    var filterIdPratica = new HashMap<String, String>();
    filterIdPratica.put(FilterCriteria.EQUALS.getCodice(), idPratica);
    filter.setIdPratica(filterIdPratica);

    if (null != body.getTipiDocumento() && !body.getTipiDocumento().isEmpty()) {
      var filterTipiDocumento = new HashMap<String, Object>();
      filterTipiDocumento
      .put(body.getTipiDocumento().size() == 1 ? FilterCriteria.EQUALS.getCodice()
          : FilterCriteria.IN.getCodice(),
          body.getTipiDocumento().stream().map(CodiceTipologiaDocumento::getCodice)
          .collect(Collectors.toList()));
      filter.setTipo(filterTipiDocumento);
    }

    ricercaParametrica.setFilter(filter);

    var documentiDaSmistare = cosmoTDocumentoRepository.findAll(CosmoTDocumentoSpecifications
        .findByFiltri(ricercaParametrica.getFilter(), ricercaParametrica.getSort()));

    documentiDaSmistare = getDocumentiDaSmistare(documentiDaSmistare, body);

    if (!documentiDaSmistare.isEmpty()) {
      var pratica = documentiDaSmistare.stream().findFirst().orElseThrow().getPratica();
      CosmoDStatoSmistamento daSmistare = this.cosmoDStatoSmistamentoRepository
          .findOneActive(StatoSmistamento.DA_SMISTARE.getCodice()).orElseThrow(() -> {
            String notFoundStato =
                String.format(ErrorMessages.STATO_SMISTAMENTO_NON_PRESENTE_O_NON_ATTIVO,
                    StatoSmistamento.DA_SMISTARE.getCodice());
            logger.error(methodName, notFoundStato);
            throw new NotFoundException(notFoundStato);
          });

      if (null != this.cosmoTSmistamentoRespository.findOne(CosmoTSmistamentoSpecifications
          .findSmistamentoPratica(Long.parseLong(idPratica), body.getIdentificativoMessaggio()))) {
        String smistamentoEsistente = String.format(
            "Esiste gia' uno smistamento attivo per l'id pratica %s e l'identificativo evento %s",
            idPratica, body.getIdentificativoMessaggio());
        throw new ConflictException(smistamentoEsistente);
      }

      // scrittura su cosmo_t_smistamento_documento per il singolo evento di smistamento
      var smistamento = new CosmoTSmistamento();
      Timestamp now = Timestamp.valueOf(LocalDateTime.now());
      smistamento.setDtInserimento(now);
      smistamento.setIdentificativoEvento(body.getIdentificativoMessaggio());
      smistamento.setUtenteInserimento(SecurityUtils.getUtenteCorrente().getCodiceFiscale());
      smistamento.setCosmoTPratica(pratica);
      smistamento.setUtilizzato(false);
      smistamento = this.cosmoTSmistamentoRespository.saveAndFlush(smistamento);
      // scrittura di cosmo_r_smistamento_documento per ogni documento da smistare
      for (CosmoTDocumento documento : documentiDaSmistare) {
        var relazioneSmistamento = new CosmoRSmistamentoDocumento();
        relazioneSmistamento.setCosmoDStatoSmistamento(daSmistare);
        relazioneSmistamento.setCosmoTDocumento(documento);
        relazioneSmistamento.setCosmoTSmistamento(smistamento);
        relazioneSmistamento.setDtInizioVal(now);
        cosmoRSmistamentoDocumentoRepository.saveAndFlush(relazioneSmistamento);
      }
      logger.info(methodName,
          String.format("Creato smistamento per la pratica %s, evento %s relativo a %d documenti",
              idPratica, body.getIdentificativoMessaggio(), documentiDaSmistare.size()));
    } else {
      logger.warn(methodName, String.format(
          "Non sono presenti documenti per la pratica %s: non verra' salvato alcuno smistamento",
          idPratica));
    }
  }

  /*
   * Impostazione dell'esito smistamento post-callback
   *
   * @see
   * it.csi.cosmo.cosmoecm.business.service.StardasService#impostaEsitoSmistamento(java.lang.Long,
   * it.csi.cosmo.cosmoecm.dto.rest.EsitoSmistaDocumentoRequest, boolean)
   */
  @Override
  public void impostaEsitoSmistamento(Long idDocumento, EsitoSmistaDocumentoRequest request,
      boolean primoSmistamento) {
    final var methodName = "impostaEsitoSmistamento";
    Esito esito = checkRequestEsitoSmistamento(request);
    CosmoTDocumento documento = cosmoTDocumentoRepository.findOne(idDocumento);
    if (null == documento) {
      final var documentoNonTrovato =
          String.format("Il documento con id %d non esiste", idDocumento);
      logger.error(methodName, documentoNonTrovato);

    }
    // recupero del record di smistamento afferente al documento
    var smistamenti = (null != documento && null != documento.getCosmoRSmistamentoDocumentos())
        ? documento.getCosmoRSmistamentoDocumentos()
        : null;
    if (null != smistamenti) {
      logger.info(methodName,
          String.format("Recuperati %d smistamenti.", smistamenti.size()));
    } else {
      logger.error(methodName, ErrorMessages.STARDAS_NESSUN_SMISTAMENTO_PRESENTE);
      throw new InternalServerException(ErrorMessages.STARDAS_NESSUN_SMISTAMENTO_PRESENTE);
    }

    smistamenti.forEach(smistamento -> {

      var statoSmistamento = smistamento.getCosmoDStatoSmistamento();
      if (null == statoSmistamento) {
        final var smistamentoNonTrovato =
            String.format("Smistamento non trovato per il documento con id %d", idDocumento);
        logger.error(methodName, smistamentoNonTrovato);
        throw new InternalServerException(smistamentoNonTrovato);
      }

      switch (StatoSmistamento.valueOf(statoSmistamento.getCodice())) {
        case IN_SMISTAMENTO:
          if (!primoSmistamento) {
            /*
             * se il documento e' 'IN_SMISTAMENTO', significa che il documento non ha ancora
             * ricevuto una callback di risposta pertanto 'primoSmistamento' (impostato a false per
             * la put e a true per la post) non puo' essere false
             */
            final var statoIncongruente = String.format(
                "Documento con id %d ancora da smistare. E' stato chiamato l'endpoint in PUT? Deve essere chiamato in POST.",
                idDocumento);
            logger.error(methodName, statoIncongruente);
            throw new InternalServerException(statoIncongruente);
          }

          aggiornaSmistamento(smistamento, esito, request);
          smistamento = salvaInfoAggiuntive(smistamento, request);

          try {
            inviaNotificaStatoSmistamento(documento,
                smistamento.getCosmoDStatoSmistamento().getDescrizione());
          } catch (Exception e) {
            logger.error(methodName, ERRORE_DURANTE_BROADCAST_NOTIFICHE, e);
          }
          break;
        case ERR_CALLBACK:
          if (primoSmistamento) {
            /*
             * se il documento e' 'ERR_SMISTAMENTO', significa che e' gia' stata ricevuta una
             * callback (ed e' andata in errore) pertanto 'primoSmistamento' (impostato a false per
             * la put e a true per la post) non puo' essere true
             */
            final var statoIncongruente = String.format(
                "Documento con id %d gia' smistato precedentemente. E' stato chiamato l'endpoint in POST? Deve essere chiamato in PUT",
                idDocumento);
            logger.error(methodName, statoIncongruente);
            throw new InternalServerException(statoIncongruente);
          }

          aggiornaSmistamento(smistamento, esito, request);
          smistamento = salvaInfoAggiuntive(smistamento, request);
          try {
            inviaNotificaStatoSmistamento(documento,
                smistamento.getCosmoDStatoSmistamento().getDescrizione());
          } catch (Exception e) {
            logger.error(methodName, ERRORE_DURANTE_BROADCAST_NOTIFICHE, e);
          }
          break;

        case SMISTATO_PARZIALMENTE:
          aggiornaSmistamento(smistamento, esito, request);
          smistamento = salvaInfoAggiuntive(smistamento, request);
          try {
            inviaNotificaStatoSmistamento(documento,
                smistamento.getCosmoDStatoSmistamento().getDescrizione());
          } catch (Exception e) {
            logger.error(methodName, ERRORE_DURANTE_BROADCAST_NOTIFICHE, e);
          }
          break;

        default:
          var statoIncongruente = String.format(
              "Lo stato di smistamento del documento con id %d non puo' essere %s. Gli unici stati accettabili in questo momento sono IN_SMISTAMENTO, SMISTATO_PARZIALMENTE e ERR_CALLBACK",
              idDocumento, statoSmistamento.getCodice());
          logger.error(methodName, statoIncongruente);
          throw new ConflictException(statoIncongruente);
      }
    });
    logger.info(methodName, "Salvataggio del documento");
    cosmoTDocumentoRepository.save(documento);
    logger.info(methodName, "Documento salvato");
  }

  @Override
  public EsitoSmistamentoResponse creaEsitoSmistamento(Long idDocumento, String messageUUID) {
    var methodName = "creaEsitoSmistamento";

    var documento = cosmoTDocumentoRepository.findOne(idDocumento);
    if (null == documento) {
      final var documentoNonTrovato =
          String.format("Il documento con id %d non esiste", idDocumento);
      logger.error(methodName, documentoNonTrovato);
      throw new InternalServerException(documentoNonTrovato);
    }
    logger.info(methodName, "Trovato documento");

    GenericRicercaParametricaDTO<FiltroRicercaDocumentiDTO> ricercaParametrica =
        new GenericRicercaParametricaDTO<>();
    FiltroRicercaDocumentiDTO filter = new FiltroRicercaDocumentiDTO();
    var filterIdPratica = new HashMap<String, String>();
    filterIdPratica.put(FilterCriteria.EQUALS.getCodice(),
        documento.getPratica().getId().toString());
    filter.setIdPratica(filterIdPratica);
    ricercaParametrica.setFilter(filter);

    var documenti = cosmoTDocumentoRepository.findAll(CosmoTDocumentoSpecifications
        .findByFiltri(ricercaParametrica.getFilter(), ricercaParametrica.getSort()));

    logger.info(methodName, "Trovati documenti pratica");

    var response = new EsitoSmistamentoResponse();
    response.setNumDocumentiDaSmistare(0);
    response.setNumDocumentiSmistatiCorrettamente(0);
    response.setNumDocumentiSmistatiInErrore(0);
    response.setIdPratica(documento.getPratica().getId().toString());
    response.setEsito(new Esito());

    CosmoRSmistamentoDocumento smistamentoDocumento =
        documento.getCosmoRSmistamentoDocumentos().stream().findFirst().orElseThrow();

    logger.info(methodName, "Trovato smistamento");

    documenti = documenti.stream().filter(doc -> {

      var filtrati = doc.getCosmoRSmistamentoDocumentos().stream().filter(r -> {
        return documento.getCosmoRSmistamentoDocumentos().stream()
            .anyMatch(rDoc -> rDoc.getCosmoTSmistamento().getIdentificativoEvento()
                .equals(r.getCosmoTSmistamento().getIdentificativoEvento()));
      }).collect(Collectors.toList());

      return (filtrati != null && !filtrati.isEmpty());

    }).collect(Collectors.toList());

    if (null != documenti) {
      logger.info(methodName, String.format("Filtrati %d documenti", documenti.size()));
    }

    if (messageUUID != null && !messageUUID.isBlank()
        && !messageUUID.equals(smistamentoDocumento.getMessageUuid())) {
      response.getEsito().setCode("199");
      response.getEsito().setTitle("MessageUUID non corrispondente");
      logger.error(methodName, "MessageUUID non corrispondente");
      return response;
    }

    response.getEsito().setCode("000");
    response.getEsito().setTitle("Operazione completata");

    if (null != documenti) {
      response.setNumDocumentiDaSmistare(documenti.size());
      documenti.stream().forEach(doc -> {

        if (doc.getCosmoRSmistamentoDocumentos() != null
            || !doc.getCosmoRSmistamentoDocumentos().isEmpty()) {
          CosmoRSmistamentoDocumento smistamento =
              doc.getCosmoRSmistamentoDocumentos().stream().findFirst().orElse(null);

          if (null != smistamento) {

            if (null != smistamento.getCosmoTSmistamento()) {
              response.setIdentificativoEvento(
                  smistamento.getCosmoTSmistamento().getIdentificativoEvento());
            }

            if (smistamento.getCosmoDStatoSmistamento() != null && StatoSmistamento.SMISTATO
                .getCodice().equals(smistamento.getCosmoDStatoSmistamento().getCodice())) {
              logger.info(methodName, "Documento smistato correttamente");
              response.setNumDocumentiSmistatiCorrettamente(
                  response.getNumDocumentiSmistatiCorrettamente() + 1);
            } else {
              logger.info(methodName, "Documento non smistato per errore");
              response
              .setNumDocumentiSmistatiInErrore(response.getNumDocumentiSmistatiInErrore() + 1);
            }
          }
        }
      });
    }
    return response;
  }

  @Override
  public void salvaEsitoRichiestaSmistamento(
      EsitoRichiestaSmistamentoDocumento esitoRichiestaSmistamento, CosmoTDocumento documento) {
    final var methodName = "salvaEsitoRichiestaSmistamento";

    if (null == esitoRichiestaSmistamento) {
      var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "esitoRichiestaSmistamento");
      logger.error(methodName, parametroNonValorizzato);
      throw new InternalServerException(parametroNonValorizzato);
    }

    if (null == documento) {
      var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "documento");
      logger.error(methodName, parametroNonValorizzato);
      throw new InternalServerException(parametroNonValorizzato);
    }

    documento = cosmoTDocumentoRepository.findOne(documento.getId());

    CosmoRSmistamentoDocumento smistamento =
        documento.getCosmoRSmistamentoDocumentos().stream().findFirst().orElseThrow();

    smistamento.setCodiceEsitoSmistamento(esitoRichiestaSmistamento.getCodice());
    smistamento.setMessaggioEsitoSmistamento(esitoRichiestaSmistamento.getMessaggio());
    smistamento.setMessageUuid(esitoRichiestaSmistamento.getMessageUUID());

    CosmoDStatoSmistamento statoSmistamento = null;

    if (Constants.ESITO_SMISTAMENTO_OK.equals(esitoRichiestaSmistamento.getCodice())
        || (esitoRichiestaSmistamento.getCodice() != null && esitoRichiestaSmistamento.getCodice()
        .matches(Constants.REGEX_CODICE_ESITO_WARNING))) {

      statoSmistamento = this.cosmoDStatoSmistamentoRepository
          .findOneActive(StatoSmistamento.IN_SMISTAMENTO.getCodice()).orElseThrow(() -> {
            String notFoundStato =
                String.format(ErrorMessages.STATO_SMISTAMENTO_NON_PRESENTE_O_NON_ATTIVO,
                    StatoSmistamento.IN_SMISTAMENTO.getCodice());
            logger.error(methodName, notFoundStato);
            throw new NotFoundException(notFoundStato);
          });
    } else {
      smistamento.setNumeroRetry(
          (smistamento.getNumeroRetry() != null ? smistamento.getNumeroRetry() : 0) + 1);
      statoSmistamento = this.cosmoDStatoSmistamentoRepository
          .findOneActive(StatoSmistamento.ERR_SMISTAMENTO.getCodice()).orElseThrow(() -> {
            String notFoundStato =
                String.format(ErrorMessages.STATO_SMISTAMENTO_NON_PRESENTE_O_NON_ATTIVO,
                    StatoSmistamento.ERR_SMISTAMENTO.getCodice());
            logger.error(methodName, notFoundStato);
            throw new NotFoundException(notFoundStato);
          });
    }
    smistamento.setCosmoDStatoSmistamento(statoSmistamento);

    cosmoRSmistamentoDocumentoRepository.save(smistamento);

    try {
      inviaNotificaStatoSmistamento(documento, statoSmistamento.getDescrizione());
    } catch (Exception e) {
      logger.error(methodName, "Errore durante invio notifica stato smistamento: " + e.getMessage());
    }

  }

  @Override
  public Long recuperaIdEnteDaDocumento(CosmoTDocumento documento) {
    documento = cosmoTDocumentoRepository.findOne(documento.getId());
    return documento.getPratica().getEnte().getId();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Set<Long> recuperaIdPraticaDaDocumento(CosmoTDocumento documento) {
    documento = cosmoTDocumentoRepository.findOne(documento.getId());
    return new HashSet<>(Arrays.asList(new Long[] {documento.getPratica().getId()}));
  }

  @Override
  public void aggiornaDaSmistare(CosmoTDocumento documento) {

    final var methodName = "aggiornaNumeroRetryDaSmistare";
    if (null == documento) {
      var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "documento");
      logger.error(methodName, parametroNonValorizzato);
      throw new InternalServerException(parametroNonValorizzato);
    }

    documento = cosmoTDocumentoRepository.findOne(documento.getId());

    CosmoRSmistamentoDocumento smistamento = null;
    if (null != documento.getCosmoRSmistamentoDocumentos()) {
      smistamento =
          documento.getCosmoRSmistamentoDocumentos().stream().findFirst().orElseThrow();
    } else {
      logger.error(methodName,
          ErrorMessages.STARDAS_NESSUN_DOCUMENTO_IN_SMISTAMENTO_DA_AGGIORNARE);
      throw new InternalServerException(
          ErrorMessages.STARDAS_NESSUN_DOCUMENTO_IN_SMISTAMENTO_DA_AGGIORNARE);
    }
    Integer maxNumRetry = configurazioneService != null
        ? configurazioneService
            .requireConfig(ParametriApplicativo.BATCH_SMISTAMENTO_STARDAS_MAXNUMRETRY).asInteger()
        : null;

    if (maxNumRetry != null && smistamento.getNumeroRetry() != null
        && maxNumRetry <= smistamento.getNumeroRetry()) {

      CosmoDStatoSmistamento statoSmistamento = this.cosmoDStatoSmistamentoRepository
          .findOneActive(StatoSmistamento.ERR_SMISTAMENTO.getCodice()).orElseThrow(() -> {
            String notFoundStato =
                String.format(ErrorMessages.STATO_SMISTAMENTO_NON_PRESENTE_O_NON_ATTIVO,
                    StatoSmistamento.ERR_SMISTAMENTO.getCodice());
            logger.error(methodName, notFoundStato);
            throw new NotFoundException(notFoundStato);
          });
      smistamento.setCosmoDStatoSmistamento(statoSmistamento);
      smistamento.setMessaggioEsitoSmistamento(
          ("Documento inviato piu' volte senza riuscirlo a smistare"));
      smistamento.setNumeroRetry(null);
    } else {
      smistamento.setNumeroRetry(
          (smistamento.getNumeroRetry() != null ? smistamento.getNumeroRetry() : 0) + 1);
    }

    cosmoRSmistamentoDocumentoRepository.save(smistamento);
  }

  @Override
  public boolean checkEsitoSmistamentoDocPadre(CosmoTDocumento documento) {

    var documentoDB = cosmoTDocumentoRepository.findOne(documento.getId());
    if (documentoDB != null && documentoDB.getDocumentoPadre() != null
        && documentoDB.getCosmoRSmistamentoDocumentos() != null) {
      var smistamentoNonValido =
          documentoDB.getDocumentoPadre().getCosmoRSmistamentoDocumentos().stream()
              .filter(elem -> elem.getCosmoDStatoSmistamento().getCodice()
                  .equals(StatoSmistamento.ERR_SMISTAMENTO.getCodice())
                  || elem.getCosmoDStatoSmistamento().getCodice()
                      .equals(StatoSmistamento.ERR_CALLBACK.getCodice())
                  || elem.getCosmoDStatoSmistamento().getCodice()
                      .equals(StatoSmistamento.DA_SMISTARE.getCodice()))
              .findAny().orElse(null);

      return smistamentoNonValido == null;
    }

    return false;
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


  private SmistaDocumentoRequest buildRequestSmistamento(CosmoTDocumento documento,
      CosmoTContenutoDocumento contenutoDocumento, String codiceFruitore) {
    SmistaDocumentoRequest input = new SmistaDocumentoRequest();

    ConfigurazioneChiamante configurazioneChiamante = new ConfigurazioneChiamante();
    configurazioneChiamante
    .setCodiceApplicazione(documento.getPratica().getTipo().getCodiceApplicazioneStardas());
    configurazioneChiamante
    .setCodiceFiscaleEnte(documento.getPratica().getEnte().getCodiceFiscale());
    configurazioneChiamante.setCodiceFruitore(codiceFruitore);

    // valorizzare il codice stardas su cosmo_d_tipo_documento

    configurazioneChiamante.setCodiceTipoDocumento(getCodiceStardas(documento));

    DatiSmistaDocumento datiSmista = new DatiSmistaDocumento();

    String responsabile = documento.getPratica().getUtenteCreazionePratica();
    if (Boolean.TRUE
        .equals(documento.getPratica().getTipo().getOverrideResponsabileTrattamento())) {
      responsabile = documento.getPratica().getTipo().getResponsabileTrattamentoStardas();
    }
    datiSmista.setResponsabileTrattamento(responsabile);

    EmbeddedJSON embeddedJSON = new EmbeddedJSON();
    embeddedJSON
    .setContent(getInformazioniPratica(documento.getPratica().getId()));
    datiSmista.setDatiDocumentoJSON(embeddedJSON);

    DocumentoElettronico documentoElettronicoType = new DocumentoElettronico();
    documentoElettronicoType.setDocumentoFirmato(contenutoDocumento.getTipoFirma() != null);
    documentoElettronicoType.setMimeType(contenutoDocumento.getFormatoFile().getMimeType());
    documentoElettronicoType.setNomeFile(contenutoDocumento.getNomeFile());

    datiSmista.setDocumentoElettronico(documentoElettronicoType);
    datiSmista.setIdDocumentoFruitore(documento.getId().toString());
    datiSmista.setUuidNodo(contenutoDocumento.getUuidNodo());

    var processInstanceId = getIdFromLink(documento.getPratica().getLinkPratica());

    if (processInstanceId != null) {
      String codiceStardas = getCodiceStardas(documento);

      var indice = businessPraticheFeignClient
          .getPraticheVariabiliProcessInstanceId(processInstanceId).getVariabili().stream()
          .filter(variabile -> codiceStardas.equals(variabile.getName())).findFirst().orElse(null);

      if (indice != null && indice.getValue() != null) {
        datiSmista.setIndiceClassificazioneEsteso(String.valueOf(indice.getValue()));
      }

    }

    // recupero degli eventuali allegati
    var allegati =
        cosmoTDocumentoRepository.findNotDeletedByField(CosmoTDocumento_.documentoPadre, documento);

    if (null != allegati && !allegati.isEmpty()) {
      datiSmista.setNumAllegati(allegati.size());
    }
    input.setConfigurazioneChiamante(configurazioneChiamante);
    input.setDatiSmistaDocumento(datiSmista);

    /*
     * se stiamo richiedendo lo smistamento per un allegato, bisogna valorizzare l'uuid del
     * documento principale. Smistare prima il principale (ProtocollazioneStardasBatchImpl lo fa
     * automaticamente)
     */
    if (documento.getDocumentoPadre() != null
        && documento.getDocumentoPadre().getCosmoRSmistamentoDocumentos() != null) {
      datiSmista.setMessageUUIDPrincipale(
          documento.getDocumentoPadre().getCosmoRSmistamentoDocumentos().stream().findFirst()
          .orElseThrow(() -> new InternalServerException("Documento principale non presente"))
          .getMessageUuid());
    }

    return input;
  }

  private Esito checkRequestEsitoSmistamento(EsitoSmistaDocumentoRequest request) {
    final var methodName = "checkRequestEsitoSmistamento";
    if (null == request) {
      final var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "esitoSmistaDocumentoRequest");
      logger.error(methodName, parametroNonValorizzato);
      throw new BadRequestException(parametroNonValorizzato);
    }
    if (null == request.getEsito() || request.getEsito().isEmpty()) {
      final var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "esito");
      logger.error(methodName, parametroNonValorizzato);
      throw new BadRequestException(parametroNonValorizzato);
    }
    if (null == request.getMessageUUID()) {
      final var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "messageUUID");
      logger.error(methodName, parametroNonValorizzato);
      throw new BadRequestException(parametroNonValorizzato);
    }
    if (request.getEsito().size() != 1) {
      final var esitiNonCongruenti =
          "Sono presenti piu' esiti (deve esserne presente esattamente 1)";
      logger.error(methodName, esitiNonCongruenti);
      throw new ConflictException(esitiNonCongruenti);
    }
    Esito esito = request.getEsito().stream().findFirst().orElseThrow();
    if (StringUtils.isBlank(esito.getCode())) {
      final var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "codiceEsito");
      logger.error(methodName, parametroNonValorizzato);
      throw new BadRequestException(parametroNonValorizzato);
    }
    return esito;
  }

  /*
   * Aggiornamento dello smistamento post-callback
   */
  private void aggiornaSmistamento(CosmoRSmistamentoDocumento smistamento, Esito esito,
      EsitoSmistaDocumentoRequest request) {
    var methodName = "aggiornaSmistamento";
    smistamento.setCodiceEsitoSmistamento(esito.getCode());
    smistamento.setMessageUuid(request.getMessageUUID());
    smistamento.setMessaggioEsitoSmistamento(esito.getTitle());

    logger.info(methodName, String.format("Aggiornamento smistamento con esito %s", esito));

    // verifica che il codice esito smistamento sia ok o compreso comunque in un range di valori di
    // warning, ma validi
    if (Constants.ESITO_SMISTAMENTO_OK.equals(esito.getCode())
        || esito.getCode().matches(Constants.REGEX_CODICE_ESITO_WARNING)) {
      CosmoDStatoSmistamento smistato = this.cosmoDStatoSmistamentoRepository
          .findOneActive(StatoSmistamento.SMISTATO.getCodice()).orElseThrow(() -> {
            String notFoundStato = String.format(LOG_STATO_PRESENTE_ATTIVO_SU_DB,
                StatoSmistamento.SMISTATO.getCodice());
            logger.error(methodName, notFoundStato);
            throw new NotFoundException(notFoundStato);
          });
      smistamento.setCosmoDStatoSmistamento(smistato);
      logger.info(methodName, "Impostazione esito OK");

    } else if (Constants.ESITO_SMISTAMENTO_OK_PARZIALE.equals(esito.getCode())) {
      CosmoDStatoSmistamento smistato = this.cosmoDStatoSmistamentoRepository
          .findOneActive(StatoSmistamento.SMISTATO_PARZIALMENTE.getCodice()).orElseThrow(() -> {
            String notFoundStato = String.format(LOG_STATO_PRESENTE_ATTIVO_SU_DB,
                StatoSmistamento.SMISTATO_PARZIALMENTE.getCodice());
            logger.error(methodName, notFoundStato);
            throw new NotFoundException(notFoundStato);
          });
      smistamento.setCosmoDStatoSmistamento(smistato);
      logger.info(methodName, "Impostazione esito OK parziale");
    } else {
      CosmoDStatoSmistamento errCallback = this.cosmoDStatoSmistamentoRepository
          .findOneActive(StatoSmistamento.ERR_CALLBACK.getCodice()).orElseThrow(() -> {
            String notFoundStato = String.format(LOG_STATO_PRESENTE_ATTIVO_SU_DB,
                StatoSmistamento.ERR_CALLBACK.getCodice());
            logger.error(methodName, notFoundStato);
            throw new NotFoundException(notFoundStato);
          });
      StringBuilder builder = new StringBuilder();
      builder.append(
          "Si sono verificati degli errori durante lo smistamento documenti in fase di callback da stardas.<br/>")
      .append("L'errore di callback riguarda i documenti seguenti: <br/><br/>");

      builder.append(" - #" + smistamento.getCosmoTDocumento().getId() + "<br/>");

      builder.append("<br/>Dettaglio esito: <br/>");

      builder.append(
          "Codice: " + esito.getCode() + ":<br/>" + "Titolo: " + esito.getTitle() + "<br />");
      logger.info(methodName, "Errore callback, invio mail ad assistenza");
      mailService.inviaMailAssistenza(
          "Errori nel ricevimento callback da stardas per smistamento documenti",
          builder.toString());
      smistamento.setCosmoDStatoSmistamento(errCallback);
      logger.info(methodName, "Impostazione esito ERR");
    }
  }

  /*
   * Aggiornamento delle info aggiuntive post-callback
   */
  private CosmoRSmistamentoDocumento salvaInfoAggiuntive(CosmoRSmistamentoDocumento smistamento,
      EsitoSmistaDocumentoRequest request) {
    var methodName = "salvaInfoAggiuntive";
    logger.info(methodName, "Salvataggio info aggiuntive");
    if (request.getInformazioniAggiuntive() != null
        && !request.getInformazioniAggiuntive().isEmpty()) {
      List<CosmoTInfoAggiuntiveSmistamento> infoAggiuntiveDbList = new ArrayList<>();
      request.getInformazioniAggiuntive().forEach(infoAggiuntiveRequest -> {
        // se esiste gia' una info aggiuntiva con lo stesso nome di quella ricevuta su db, la
        // aggiorno
        var infoAggiuntive = findSmistamento(smistamento, infoAggiuntiveRequest);
        if (infoAggiuntive == null) {
          infoAggiuntive = new CosmoTInfoAggiuntiveSmistamento();
          infoAggiuntive.setDtInserimento(Timestamp.valueOf(LocalDateTime.now()));
          infoAggiuntive.setUtenteInserimento(SecurityUtils.getUtenteCorrente().getCodiceFiscale());
          infoAggiuntive.setCodInformazione(infoAggiuntiveRequest.getCodInformazione());
          infoAggiuntive.setValore(infoAggiuntiveRequest.getValore());
          infoAggiuntive.setCosmoRSmistamentoDocumento(smistamento);
        }
        infoAggiuntiveDbList.add(infoAggiuntive);
        cosmoTInfoAggiuntiveSmistamentoRepository.save(infoAggiuntive);
        logger.info(methodName, "Info aggiuntive salvate");
      });
      smistamento.setCosmoTInfoAggiuntiveSmistamentos(infoAggiuntiveDbList);
    }
    return smistamento;
  }

  private void inviaNotificaStatoSmistamento(CosmoTDocumento documento, String statoSmistamento) {

    var methodName = "inviaNotificaStatoSmistamento";

    logger.info(methodName, "Invio notifica stato smistamento");
    String messaggio = String.format(Constants.STATO_SMISTAMENTO, statoSmistamento,
        documento.getTitolo() != null ? documento.getTitolo()
            : getContenutoDocumento(documento).getNomeFile());

    NotificheGlobaliRequest request = new NotificheGlobaliRequest();
    request.setIdPratica(documento.getPratica().getId());
    request.setTipoNotifica(TipoNotifica.SMISTAMENTO_DOCUMENTI.getCodice());
    request.setMessaggio(messaggio);
    request.setClasse("SUCCESS");
    request.setEvento(Constants.EVENTS.DOCUMENTI_SMISTATI);
    request.setCodiceIpaEnte(documento.getPratica().getEnte().getCodiceIpa());

    try {
      notificheFeignClient.postNotificheGlobali(request);
    } catch (Exception e) {
      logger.error(methodName, "Errore nell'inviare la notifica", e);
    }
    logger.info(methodName, "Notifiche inviate");
  }

  // ricerca e aggiornamento di una info aggiuntiva per codInformazione
  private CosmoTInfoAggiuntiveSmistamento findSmistamento(CosmoRSmistamentoDocumento smistamento,
      InformazioneAggiuntivaSmistamento infoAggiuntiveRequest) {
    CosmoTInfoAggiuntiveSmistamento infoAggiuntive = null;
    if (null != smistamento.getCosmoTInfoAggiuntiveSmistamentos()
        && !smistamento.getCosmoTInfoAggiuntiveSmistamentos().isEmpty()) {
      for (var infoAggiuntiveDb : smistamento.getCosmoTInfoAggiuntiveSmistamentos()) {
        if (infoAggiuntiveDb.getCodInformazione() != null && infoAggiuntiveDb.getCodInformazione()
            .equals(infoAggiuntiveRequest.getCodInformazione())) {
          infoAggiuntiveDb.setValore(infoAggiuntiveRequest.getValore());
          infoAggiuntiveDb
          .setUtenteUltimaModifica(SecurityUtils.getUtenteCorrente().getCodiceFiscale());
          infoAggiuntiveDb.setDtUltimaModifica(Timestamp.valueOf(LocalDateTime.now()));
          infoAggiuntive = infoAggiuntiveDb;
        }
      }
    }
    return infoAggiuntive;
  }

  private String getInformazioniPratica(Long idPratica) {
    var methodName = "getInformazioniPratica";
    String metadatiAsString = null;

    Object infoPratica = null;

    logger.info(methodName, "Recupero contesto pratica per smistamento verso STARDAS");
    try {
      infoPratica = businessPraticheFeignClient.getPraticheIdElaborazioneContesto(idPratica);
    } catch (Exception e) {
      logger.error(methodName, e.getMessage(), e);
      throw new InternalServerException("Errore nel recupero contesto pratica ", e);
    }

    if (null != infoPratica) {
      byte[] bytes = ObjectUtils.json2Byte(infoPratica);

      if (bytes.length > 0) {
        metadatiAsString = new String(bytes, StandardCharsets.UTF_8);
      }
      logger.info(methodName, "Dump Contesto:");
      logger.info(methodName, metadatiAsString);

    }

    return metadatiAsString;
  }

  private String getCodiceFruitore(CosmoDTipoPratica tipoPratica) {

    String codiceFruitore = "";
    /*
     * Se il codice fruitore e' valorizzato sul tipo pratica e se il flag di override e' a true,
     * utilizzo il codice fruitore stardas, da configurazione altrimenti
     */
    if (tipoPratica != null && Boolean.TRUE.equals(tipoPratica.getOverrideFruitoreDefault())
        && tipoPratica.getCodiceFruitoreStardas() != null) {
      codiceFruitore = tipoPratica.getCodiceFruitoreStardas();

    } else {
      ConfigurazioneDTO codiceFruitoreCfg =
          configurazioneService.getConfig(ParametriApplicativo.STARDAS_CODICE_FRUITORE);

      codiceFruitore = codiceFruitoreCfg.getValue();
    }

    return codiceFruitore;
  }

  private void checkPreliminariRichiestaSmistamento(String idPratica,
      RichiediSmistamentoRequest body) {
    final var methodName = "checkPreliminariRichiestaSmistamento";
    if (null == idPratica) {
      final var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "idPratica");
      logger.error(methodName, parametroNonValorizzato);
      throw new BadRequestException(parametroNonValorizzato);
    }
    try {
      Long.parseLong(idPratica);
    } catch (NumberFormatException nfe) {
      logger.error(methodName, nfe.getMessage());
      throw nfe;
    }

    if (null == body) {
      final var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "request");
      logger.error(methodName, parametroNonValorizzato);
      throw new BadRequestException(parametroNonValorizzato);
    }

    if (null == body.getIdentificativoMessaggio()) {
      final var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "identificativoEvento");
      logger.error(methodName, parametroNonValorizzato);
      throw new BadRequestException(parametroNonValorizzato);
    }

  }

  private Predicate<CosmoTDocumento> filtraDocumentiDaSmistareTramiteTipoDocumento(
      List<CosmoTDocumento> docs, RichiediSmistamentoRequest body) {
    return documentoDaSmistare -> {
      var tipoDoc =
          !CollectionUtils.isEmpty(body.getTipiDocumento()) ? body.getTipiDocumento().stream()
              .filter(tipo -> tipo.getCodice().equals(documentoDaSmistare.getTipo().getCodice()))
              .findFirst().orElse(null) : null;

      if (tipoDoc == null) {
        return false;
      }

      if (tipoDoc.getCodicePadre() != null && !tipoDoc.getCodicePadre().isBlank()) {

        if (documentoDaSmistare.getDocumentoPadre() == null) {
          return false;
        } else {
          return tipoDoc.getCodicePadre()
              .equals(documentoDaSmistare.getDocumentoPadre().getTipo().getCodice());
        }
      } else {

        return (documentoDaSmistare.getDocumentoPadre() == null || docs.stream()
            .anyMatch(doc -> doc.getId().equals(documentoDaSmistare.getDocumentoPadre().getId())));
      }
    };
  }

  private List<CosmoTDocumento> getDocumentiDaSmistare(List<CosmoTDocumento> documentiDaSmistare,
      RichiediSmistamentoRequest body) {
    var docs = documentiDaSmistare;

    return documentiDaSmistare.stream()
        .filter(filtraDocumentiDaSmistareTramiteTipoDocumento(docs, body))
        .filter(documentoDaSmistare -> {

          if (documentoDaSmistare.getCosmoRSmistamentoDocumentos() == null) {
            documentoDaSmistare.setCosmoRSmistamentoDocumentos(new LinkedList<>());
          }

          return documentoDaSmistare.getCosmoRSmistamentoDocumentos().stream()
              .noneMatch(uno -> StatoSmistamento.SMISTATO.getCodice()
                  .equals(uno.getCosmoDStatoSmistamento().getCodice())
                  || StatoSmistamento.IN_SMISTAMENTO.getCodice()
                      .equals(uno.getCosmoDStatoSmistamento().getCodice())
                  || StatoSmistamento.SMISTATO_PARZIALMENTE.getCodice()
                      .equals(uno.getCosmoDStatoSmistamento().getCodice()));
        }).collect(Collectors.toList());

  }

  private String getIdFromLink(String raw) {
    if (raw == null || StringUtils.isBlank(raw)) {
      return null;
    }
    raw = raw.strip();

    while (raw.startsWith("/")) {
      raw = raw.substring(1).strip();
    }

    if (raw.contains("/")) {
      return raw.substring(raw.indexOf('/') + 1);
    }

    return null;
  }

  private String getCodiceStardas(CosmoTDocumento documento) {
    if (documento.getDocumentoPadre() != null) {

      var codiceStardas =
          documento.getTipo().getCosmoRTipoDocumentoTipoDocumentosAllegato().stream()
          .filter(d -> d.getCosmoDTipoDocumentoAllegato().getCodice()
              .equals(documento.getTipo().getCodice())
              && d.getCosmoDTipoDocumentoPadre().getCodice()
              .equals(documento.getDocumentoPadre().getTipo().getCodice()))
          .map(CosmoRTipoDocumentoTipoDocumento::getCodiceStardasAllegato)
          .filter(Objects::nonNull).findFirst();


      if (codiceStardas.isPresent()) {
        return codiceStardas.get();
      }

    } else {
      return documento.getTipo().getCodiceStardas();
    }

    return null;
  }

}

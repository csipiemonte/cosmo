/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.business.service.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmosoap.business.service.ActaService;
import it.csi.cosmo.cosmosoap.business.service.ConfigurazioneEnteService;
import it.csi.cosmo.cosmosoap.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmosoap.config.ParametriApplicativo;
import it.csi.cosmo.cosmosoap.dto.FiltroRicercaDocumentiActaDTO;
import it.csi.cosmo.cosmosoap.dto.TransactionExecutionResult;
import it.csi.cosmo.cosmosoap.dto.acta.ContestoOperativoUtenteActa;
import it.csi.cosmo.cosmosoap.dto.acta.FiltroRicercaVociTitolarioActaDTO;
import it.csi.cosmo.cosmosoap.dto.acta.InformazioniActaEnte;
import it.csi.cosmo.cosmosoap.dto.rest.Classificazione;
import it.csi.cosmo.cosmosoap.dto.rest.ContenutoDocumentoFisico;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiSemplici;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoFisico;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoFisicoMap;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoSemplice;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoSempliceMap;
import it.csi.cosmo.cosmosoap.dto.rest.IdentitaActa;
import it.csi.cosmo.cosmosoap.dto.rest.IdentitaUtente;
import it.csi.cosmo.cosmosoap.dto.rest.ImportaDocumentiRequest;
import it.csi.cosmo.cosmosoap.dto.rest.PageInfo;
import it.csi.cosmo.cosmosoap.dto.rest.Protocollo;
import it.csi.cosmo.cosmosoap.dto.rest.RegistrazioniClassificazioni;
import it.csi.cosmo.cosmosoap.dto.rest.Titolario;
import it.csi.cosmo.cosmosoap.dto.rest.VociTitolario;
import it.csi.cosmo.cosmosoap.integration.mapper.ActaMapper;
import it.csi.cosmo.cosmosoap.integration.soap.acta.ActaClient;
import it.csi.cosmo.cosmosoap.integration.soap.acta.ActaClientImpl;
import it.csi.cosmo.cosmosoap.integration.soap.acta.ActaProvider;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.DocumentoFisicoActaDefaultImpl;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.DocumentoSempliceActaDefaultImpl;
import it.csi.cosmo.cosmosoap.security.SecurityUtils;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;

/**
 *
 */


@Service
public class ActaServiceImpl implements InitializingBean, DisposableBean, ActaService {

  private final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  private static final String ID_IDENTITA = "idIdentita";
  private static final String FILTER = "filter";
  private static final String CONTESTO = "contesto";

  Map<String, ActaClient> istanzeClient = new HashMap<>();

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  public ConfigurazioneEnteService configurazioneEnteService;

  @Autowired
  private ActaMapper actaMapper;

  @Override
  public void afterPropertiesSet() throws Exception {
    // NOP
  }

  @Override
  public void destroy() throws Exception {
    // NOP
  }

  @Override
  public ActaClient getClient(String idIdentita) {
    ValidationUtils.require(idIdentita, ID_IDENTITA);
    return getClientByContestoOperativo(idIdentita);
  }

  @Override
  public List<DocumentoFisicoMap> findDocumentiFisici(String idIdentita,
      ImportaDocumentiRequest body) {

    ValidationUtils.require(idIdentita, ID_IDENTITA);
    ValidationUtils.require(body, "body");

    var client = getClientByContestoOperativo(idIdentita);

    List<DocumentoFisicoMap> documentiFisiciMap = new LinkedList<>();
    DocumentoFisicoMap documentoFisico;

    for (var documentoSempliceInput : body.getDocumenti()) {
      for (var documentoFisicoInput : documentoSempliceInput.getDocumentiFisici()) {
        var documentoFisicoActa =
            client.find(documentoFisicoInput.getId(), DocumentoFisicoActaDefaultImpl.class);
        logger.info("findDocumentiFisici",
            "Recuperato il documento fisico " + documentoFisicoActa.getDescrizione());

        documentoFisico = new DocumentoFisicoMap();
        documentoFisico.setId(documentoFisicoInput.getId());
        documentoFisico.setDocumento(actaMapper.toDocumentoFisico(documentoFisicoActa));
        documentiFisiciMap.add(documentoFisico);
      }
    }
    return documentiFisiciMap;
  }

  @Override
  public List<DocumentoSempliceMap> findDocumentiSemplici(String idIdentita,
      ImportaDocumentiRequest body) {

    ValidationUtils.require(idIdentita, ID_IDENTITA);
    ValidationUtils.require(body, "body");

    var client = getClientByContestoOperativo(idIdentita);

    List<DocumentoSempliceMap> documentiSemplici = new LinkedList<>();

    DocumentoSempliceMap documentoSemplice;

    for (var documentoSempliceInput : body.getDocumenti()) {
      var documentoSempliceActa =
          client.find(documentoSempliceInput.getId(), DocumentoSempliceActaDefaultImpl.class);
      logger.info("findDocumentiSemplici",
          "Recuperato il documento " + documentoSempliceActa.getOggetto());

      documentoSemplice = new DocumentoSempliceMap();
      documentoSemplice.setId(documentoSempliceInput.getId());
      documentoSemplice.setDocumentoSemplice(actaMapper.toDocumentoSemplice(documentoSempliceActa));

      documentiSemplici.add(documentoSemplice);

    }

    return documentiSemplici;
  }

  @Override
  public RegistrazioniClassificazioni findRegistrazioni(String idIdentita, String filter) {
    ValidationUtils.require(idIdentita, ID_IDENTITA);
    ValidationUtils.require(filter, FILTER);

    var client = getClientByContestoOperativo(idIdentita);

    GenericRicercaParametricaDTO<FiltroRicercaDocumentiActaDTO> parametri =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaDocumentiActaDTO.class);

    Pageable paging = SearchUtils.getPageRequest(parametri, 999);

    var result = client.findRegistrazioni(parametri.getFilter(), paging);

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(result.getNumber());
    pageInfo.setPageSize(result.getSize());
    pageInfo.setTotalElements(Math.toIntExact(result.getTotalElements()));
    pageInfo.setTotalPages(result.getTotalPages());

    RegistrazioniClassificazioni output = new RegistrazioniClassificazioni();
    output.setRegistrazioni(actaMapper.toRegistrazioniClassificazioni(result.getContent()));
    output.setPageInfo(pageInfo);
    return output;
  }

  @Override
  public DocumentiSemplici findDocumentiSempliciPerProtocollo(String idIdentita, String filter) {
    ValidationUtils.require(idIdentita, ID_IDENTITA);
    ValidationUtils.require(filter, FILTER);

    var client = getClientByContestoOperativo(idIdentita);

    GenericRicercaParametricaDTO<FiltroRicercaDocumentiActaDTO> parametri =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaDocumentiActaDTO.class);

    Pageable paging = SearchUtils.getPageRequest(parametri, 999);

    var registrazioni = client.findRegistrazioni(parametri.getFilter(), paging);
    var documenti = registrazioni.getContent().stream()
        .flatMap(reg -> client
            .findDocumentiSempliciByIdClassificazione(reg.getObjectIdClassificazione()).stream())
        .collect(Collectors.toList());

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(paging.getPageNumber());
    pageInfo.setPageSize(paging.getPageSize());
    pageInfo.setTotalElements(Math.toIntExact(registrazioni.getTotalElements()));
    pageInfo.setTotalPages(registrazioni.getTotalPages());

    DocumentiSemplici output = new DocumentiSemplici();
    output.setDocumenti(actaMapper.toDocumentiSemplici(documenti));
    output.setPageInfo(pageInfo);
    return output;
  }

  @Override
  public List<DocumentoSemplice> findDocumentiSempliciByIdClassificazione(String idIdentita,
      String idClassificazione) {
    ValidationUtils.require(idIdentita, ID_IDENTITA);
    ValidationUtils.require(idClassificazione, "idClassificazione");

    var client = getClientByContestoOperativo(idIdentita);

    var result = client.findDocumentiSempliciByIdClassificazione(idClassificazione);
    return actaMapper.toDocumentiSemplici(result);
  }

  @Override
  public DocumentiSemplici findDocumentiSemplici(String idIdentita, String filter) {
    ValidationUtils.require(idIdentita, ID_IDENTITA);
    ValidationUtils.require(filter, FILTER);

    var client = getClientByContestoOperativo(idIdentita);

    GenericRicercaParametricaDTO<FiltroRicercaDocumentiActaDTO> parametri =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaDocumentiActaDTO.class);

    Pageable paging = SearchUtils.getPageRequest(parametri, 999);

    var result = client.findDocumentiSemplici(parametri.getFilter(), paging);

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(result.getNumber());
    pageInfo.setPageSize(result.getSize());
    pageInfo.setTotalElements(Math.toIntExact(result.getTotalElements()));
    pageInfo.setTotalPages(result.getTotalPages());

    DocumentiSemplici output = new DocumentiSemplici();
    output.setDocumenti(actaMapper.toDocumentiSemplici(result.getContent()));
    output.setPageInfo(pageInfo);
    return output;
  }

  @Override
  public List<DocumentoSemplice> findDocumentiSempliciByParolaChiave(String idIdentita,
      String parolaChiave) {
    ValidationUtils.require(idIdentita, ID_IDENTITA);
    ValidationUtils.require(parolaChiave, "parolaChiave");

    var client = getClientByContestoOperativo(idIdentita);

    var result = client.findDocumentiSempliciByParolaChiave(parolaChiave, 10, 0);
    return actaMapper.toDocumentiSemplici(result);
  }

  @Override
  public List<DocumentoFisico> findDocumentiFisiciByIdDocumentoSemplice(String idIdentita,
      String id) {
    ValidationUtils.require(idIdentita, ID_IDENTITA);
    ValidationUtils.require(id, "id");

    var client = getClientByContestoOperativo(idIdentita);

    var result = client.findDocumentiFisiciByIdDocumentoSemplice(id);
    return actaMapper.toDocumentiFisici(result);
  }

  @Override
  public ContenutoDocumentoFisico findContenutoPrimarioByIdDocumentoFisico(String idIdentita,
      String id) {
    ValidationUtils.require(idIdentita, ID_IDENTITA);
    ValidationUtils.require(id, "id");

    var client = getClientByContestoOperativo(idIdentita);

    return actaMapper
        .toContenutoDocumentoFisico(client.findContenutoPrimarioByIdDocumentoFisico(id));
  }

  @Override
  public List<Classificazione> findClassificazioniByIdDocumentoSemplice(String idIdentita,
      String id) {
    ValidationUtils.require(idIdentita, ID_IDENTITA);
    ValidationUtils.require(id, "id");

    var client = getClientByContestoOperativo(idIdentita);

    var result = client.findClassificazioniByIdDocumentoSemplice(id);

    return actaMapper.toClassificazioni(result);
  }

  @Override
  public Protocollo findProtocolloById(String idIdentita, String id) {
    ValidationUtils.require(idIdentita, ID_IDENTITA);
    ValidationUtils.require(id, "id");

    var client = getClientByContestoOperativo(idIdentita);

    var result = client.findProtocolloById(id);
    return actaMapper.toProtocollo(result);
  }

  @Override
  public IdentitaActa findIdentitaDisponibili(ContestoOperativoUtenteActa contesto,
      IdentitaUtente identitaUtente) {
    ValidationUtils.require(contesto, CONTESTO);

    var client = getClientByUtenteEnte(contesto.getCodiceFiscaleUtente(), contesto.getEnte());

    var identita = client.findIdentitaByCodiceFiscaleUtente(contesto.getCodiceFiscaleUtente());

    // prendo sempre la prima identita'
    return identita.stream()
        .map(i -> actaMapper.toIdentitaActa(i))
        .filter(i -> i.getIdentificativoAOO().equals(identitaUtente.getIdentificativoAOO())
            && i.getIdentificativoNodo().equals(identitaUtente.getIdentificativoNodo())
            && i.getIdentificativoStruttura().equals(identitaUtente.getIdentificativoStruttura()))
        .findFirst().orElseThrow(() -> new BadRequestException("identita' non valida"));
  }

  @Override
  public List<IdentitaActa> findIdentitaDisponibili() {

    var contesto = ContestoOperativoUtenteActa.builder()
        .withCodiceFiscaleUtente(getCodiceFiscaleEffettivoUtenteCorrente())
        .withEnte(getConfigurazioneActaEnte()).build();

    var client = getClientByUtenteEnte(contesto.getCodiceFiscaleUtente(), contesto.getEnte());

    return actaMapper.toIdentitaActas(
        client.findIdentitaByCodiceFiscaleUtente(contesto.getCodiceFiscaleUtente()));
  }


  @Override
  public ContenutoDocumentoFisico findContenutoPrimario(String idIdentita, String idFisico) {
    return this.findContenutoPrimarioByIdDocumentoFisico(idIdentita, idFisico);
  }

  private String hashContestoOperativo(ContestoOperativoUtenteActa contesto) {
    ValidationUtils.require(contesto, CONTESTO);

    if (contesto.getIdentitaSelezionata() == null) {
      throw new InternalServerException("Nessuna identita' acta selezionata");
    }
    if (contesto.getEnte() == null) {
      throw new InternalServerException("Configurazione acta dell'ente mancante");
    }
    if (StringUtils.isBlank(contesto.getEnte().getAppKey())) {
      throw new InternalServerException("AppKey non valorizzata");
    }
    if (StringUtils.isBlank(contesto.getEnte().getRepositoryName())) {
      throw new InternalServerException("RepositoryName non valorizzato");
    }
    if (StringUtils.isBlank(contesto.getIdentitaSelezionata().getIdentificativoAOO())) {
      throw new InternalServerException("IdentificativoAOO non valorizzato");
    }
    if (StringUtils.isBlank(contesto.getIdentitaSelezionata().getIdentificativoNodo())) {
      throw new InternalServerException("IdentificativoNodo non valorizzato");
    }
    if (StringUtils.isBlank(contesto.getIdentitaSelezionata().getIdentificativoStruttura())) {
      throw new InternalServerException("IdentificativoStruttura");
    }

    StringBuilder hash = new StringBuilder("");
    //@formatter:off
    hash
    .append(hashContestoOperativoUtenteEnte(contesto.getCodiceFiscaleUtente(), contesto.getEnte()))
    .append(",aoo=")
    .append(contesto.getIdentitaSelezionata().getIdentificativoAOO())
    .append(",nodo=")
    .append(contesto.getIdentitaSelezionata().getIdentificativoNodo())
    .append(",struttura=")
    .append(contesto.getIdentitaSelezionata().getIdentificativoStruttura());
    //@formatter:on
    return hash.toString();
  }

  private String hashContestoOperativoUtenteEnte(String codiceFiscale, InformazioniActaEnte ente) {
    ValidationUtils.require(ente, "ente");
    ValidationUtils.require(codiceFiscale, "codiceFiscale");

    StringBuilder hash = new StringBuilder("");
    //@formatter:off
    hash
    .append("cf=")
    .append(codiceFiscale)
    .append(",ente=")
    .append(hashContestoOperativoEnte(ente));
    //@formatter:on
    return hash.toString();
  }

  private String hashContestoOperativoEnte(InformazioniActaEnte ente) {
    ValidationUtils.require(ente, "ente");

    if (StringUtils.isBlank(ente.getAppKey())) {
      throw new InternalServerException("AppKey non valorizzata");
    }
    if (StringUtils.isBlank(ente.getRepositoryName())) {
      throw new InternalServerException("RepositoryName non valorizzato");
    }

    StringBuilder hash = new StringBuilder("");
    //@formatter:off
    hash
    .append("appkey=")
    .append(ente.getAppKey())
    .append("repo=")
    .append(ente.getRepositoryName());
    //@formatter:on
    return hash.toString();
  }

  private ActaClient getClientByUtenteEnte(String codiceFiscale, InformazioniActaEnte ente) {
    ValidationUtils.require(ente, "ente");
    ValidationUtils.require(codiceFiscale, "codiceFiscale");

    return this.istanzeClient.computeIfAbsent(hashContestoOperativoUtenteEnte(codiceFiscale, ente),
        hash ->
          //@formatter:off
           ActaClientImpl.builder()
              .withCodiceProtocollista(codiceFiscale)
              .withCodiceApplicazione(ente.getAppKey())
              .withRepositoryName(ente.getRepositoryName())
              .withFacadeProvider(getActaProvider())
              .build()
          //@formatter:on
        );
  }


  private ActaClient getClientByContestoOperativo(String idIdentita) {
    ValidationUtils.require(idIdentita, ID_IDENTITA);

    ContestoOperativoUtenteActa contesto = getContestoOperativoUtente(idIdentita);
    ValidationUtils.require(contesto, CONTESTO);

    return this.istanzeClient.computeIfAbsent(hashContestoOperativo(contesto), hash ->
      //@formatter:off
       ActaClientImpl.builder()
          .withCodiceProtocollista(contesto.getCodiceFiscaleUtente())
          .withCodiceApplicazione(contesto.getEnte().getAppKey())
          .withRepositoryName(contesto.getEnte().getRepositoryName())
          .withCodiceAreaOrganizzativaOmogenea(contesto.getIdentitaSelezionata().getIdentificativoAOO())
          .withCodiceNodo(contesto.getIdentitaSelezionata().getIdentificativoNodo())
          .withCodiceStruttura(contesto.getIdentitaSelezionata().getIdentificativoStruttura())
          .withFacadeProvider(getActaProvider())
          .build()
      //@formatter:on
    );
  }

  private ActaProvider getActaProvider() {
    //@formatter:off
    return ActaProvider.builder()
        .withUseApiManager(configurazioneService.requireConfig(ParametriApplicativo.ACTA_USE_API_MANAGER).asBool())
        .withApiManagerConsumerKey(configurazioneService.requireConfig(ParametriApplicativo.ACTA_API_MANAGER_CONSUMER_KEY).asString())
        .withApiManagerConsumerSecret(configurazioneService.requireConfig(ParametriApplicativo.ACTA_API_MANAGER_CONSUMER_SECRET).asString())
        .withTokenEndpoint(configurazioneService.requireConfig(ParametriApplicativo.ACTA_API_MANAGER_TOKEN_ENDPOINT).asString())
        .withRootEndpoint(configurazioneService.requireConfig(ParametriApplicativo.ACTA_ROOT_ENDPOINT).asString())
        .build();
    //@formatter:of
  }

  protected <T> TransactionExecutionResult<T> attempt(Callable<T> task) {
    try {
      T result = task.call();
      return TransactionExecutionResult.<T>builder().withResult(result).withSuccess(true).build();
    } catch (Exception e) {
      return TransactionExecutionResult.<T>builder().withError(e).withSuccess(false).build();
    }
  }

  private ContestoOperativoUtenteActa getContestoOperativoUtente(String idIdentita) {
    ValidationUtils.require(idIdentita, "identita");

    var parsed = parseIdentitaSelezionata(idIdentita);
    if (parsed == null) {
      throw new BadRequestException("identita' non fornita");
    }

    var infoEnte = getConfigurazioneActaEnte();

    String cf = getCodiceFiscaleEffettivoUtenteCorrente();

    var identita = findIdentitaDisponibili(ContestoOperativoUtenteActa.builder()
        .withCodiceFiscaleUtente(cf).withEnte(infoEnte).build(), parsed);

    return ContestoOperativoUtenteActa.builder()
        .withCodiceFiscaleUtente(cf)
        .withEnte(infoEnte)
        .withIdentitaSelezionata(actaMapper.toIdentitaActa(identita))
        .build();
  }

  private IdentitaUtente parseIdentitaSelezionata(String raw) {
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    return ObjectUtils.fromJson(raw, IdentitaUtente.class);
  }

  private InformazioniActaEnte getConfigurazioneActaEnte() {

    return configurazioneService.requireConfig(ParametriApplicativo.ACTA_MOCK_UTENTE_ENABLE)
        .asBool()
        ? InformazioniActaEnte.builder()
            .withAppKey(configurazioneService
                .requireConfig(ParametriApplicativo.ACTA_MOCK_ENTE_APP_KEY).asString())
            .withRepositoryName(configurazioneService
                .requireConfig(ParametriApplicativo.ACTA_MOCK_ENTE_REPOSITORY_NAME).asString())
            .build()
            : configurazioneEnteService.getConfigurazioneActaEnte();

  }

  private String getCodiceFiscaleEffettivoUtenteCorrente() {

    return configurazioneService.requireConfig(ParametriApplicativo.ACTA_MOCK_UTENTE_ENABLE)
        .asBool()
        ? configurazioneService
            .requireConfig(ParametriApplicativo.ACTA_MOCK_UTENTE_CODICE_FISCALE).asString()
            : SecurityUtils.getUtenteCorrente().getCodiceFiscale();
  }

  @Override
  public String findObjectIdStrutturaAggregativa(String identita, String indiceClassificazioneEsteso) {
    final var methodName = "findObjectIdStrutturaAggregativa";
    logger.info(methodName, "Inizio ricerca object ID della struttura aggregativa ricercata, a partire dall' indice di classificazione esteso");

    ValidationUtils.require(identita, "identita");
    ValidationUtils.require(indiceClassificazioneEsteso, "indiceClassificazioneEsteso");
    var client = getClientByContestoOperativo(identita);

    var res = client.findObjectIDStrutturaAggregativa(indiceClassificazioneEsteso);


    logger.info(methodName, "Fine ricerca object ID della struttura aggregativa ricercata, a partire dall' indice di classificazione esteso");


    return res;
  }

  @Override
  public Titolario getTitolario(String idIdentita, String codice) {
    final var methodName = "getTitolario";
    ValidationUtils.require(idIdentita, ID_IDENTITA);
    ValidationUtils.require("codice", codice);
    logger.info(methodName, "Inizio ricerca titolario per codice %s", codice);
    var client = getClientByContestoOperativo(idIdentita);
    var result = client.findTitolario(codice);
    logger.info(methodName, "Fine ricerca titolario con codice %s", codice);
    return actaMapper.toTitolario(result);
  }

  @Override
  public VociTitolario ricercaAlberaturaVociTitolario(String idIdentita, String chiaveTitolario, String chiavePadre, String filter) {
    final var methodName = "ricercaAlberaturaVociTitolario";
    ValidationUtils.require(idIdentita, ID_IDENTITA);
    ValidationUtils.require(chiaveTitolario, "chiaveTitolario");
    ValidationUtils.require(filter, FILTER);

    GenericRicercaParametricaDTO<FiltroRicercaVociTitolarioActaDTO> parametri =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaVociTitolarioActaDTO.class);

    Pageable paging = SearchUtils.getPageRequest(parametri, 999);


    logger.info(methodName, "Inizio ricerca nell'alberatura del titolario con chiave %s" + chiaveTitolario + " e chiave padre: %s" + chiavePadre);
    var client = getClientByContestoOperativo(idIdentita);
    var result = client.findVociTitolarioInAlberatura(chiaveTitolario, chiavePadre, paging);
    logger.info(methodName, "Fine ricerca nell'alberatura del titolario con chiave %s" +  chiaveTitolario + " e chiave padre: %s" + chiavePadre);

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(result.getNumber());
    pageInfo.setPageSize(result.getSize());
    pageInfo.setTotalElements(Math.toIntExact(result.getTotalElements()));
    pageInfo.setTotalPages(result.getTotalPages());

    VociTitolario output = new VociTitolario();
    output.setVociTitolario(actaMapper.toVociTitolario(result.getContent()));
    output.setPageInfo(pageInfo);
    return output;
  }

}

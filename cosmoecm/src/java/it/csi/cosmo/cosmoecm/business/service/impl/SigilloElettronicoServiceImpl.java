/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.CosmoDStatoSigilloElettronico;
import it.csi.cosmo.common.entities.CosmoRSigilloDocumento;
import it.csi.cosmo.common.entities.CosmoRSigilloDocumento_;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTCredenzialiSigilloElettronico;
import it.csi.cosmo.common.entities.CosmoTCredenzialiSigilloElettronico_;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.CosmoTSigilloElettronico;
import it.csi.cosmo.common.entities.CosmoTSigilloElettronico_;
import it.csi.cosmo.common.entities.enums.TipoContenutoDocumento;
import it.csi.cosmo.common.entities.enums.TipoNotifica;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmoecm.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoecm.business.service.SigilloElettronicoService;
import it.csi.cosmo.cosmoecm.config.Constants;
import it.csi.cosmo.cosmoecm.config.ErrorMessages;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo;
import it.csi.cosmo.cosmoecm.dto.EsitoRichiestaSigilloElettronicoDocumento;
import it.csi.cosmo.cosmoecm.dto.FiltroSigilloElettronicoDTO;
import it.csi.cosmo.cosmoecm.dto.StatoSigilloElettronico;
import it.csi.cosmo.cosmoecm.dto.rest.CodiceTipologiaDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.CreaCredenzialiSigilloElettronicoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CredenzialiSigilloElettronico;
import it.csi.cosmo.cosmoecm.dto.rest.CredenzialiSigilloElettronicoResponse;
import it.csi.cosmo.cosmoecm.dto.rest.PageInfo;
import it.csi.cosmo.cosmoecm.dto.rest.RichiediApposizioneSigilloRequest;
import it.csi.cosmo.cosmoecm.integration.mapper.CosmoTCredenzialiSigilloElettronicoMapper;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDStatoSigilloElettronicoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoRSigilloDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTCredenzialiSigilloElettronicoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTSigilloElettronicoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.specifications.CosmoTCredenzialiSigilloElettronicoSpecifications;
import it.csi.cosmo.cosmoecm.integration.repository.specifications.CosmoTDocumentoSpecifications;
import it.csi.cosmo.cosmoecm.integration.repository.specifications.CosmoTSigilloElettronicoSpecifications;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoNotificationsNotificheGlobaliFeignClient;
import it.csi.cosmo.cosmoecm.security.SecurityUtils;
import it.csi.cosmo.cosmoecm.util.ContenutoTemporaneoException;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;
import it.csi.cosmo.cosmonotifications.dto.rest.NotificheGlobaliRequest;

@Service
@Transactional
public class SigilloElettronicoServiceImpl implements SigilloElettronicoService {

  private final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  private static final String REQUEST = "request";

  @Autowired
  CosmoTCredenzialiSigilloElettronicoMapper cosmoTCredenzialiSigilloElettronicoMapper;

  @Autowired
  CosmoTCredenzialiSigilloElettronicoRepository cosmoTCredenzialiSigilloElettronicoRepository;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CosmoDStatoSigilloElettronicoRepository cosmoDStatoSigilloElettronicoRepository;

  @Autowired
  private CosmoTSigilloElettronicoRepository cosmoTSigilloElettronicoRepository;

  @Autowired
  private CosmoRSigilloDocumentoRepository cosmoRSigilloDocumentoRepository;

  @Autowired
  private CosmoTDocumentoRepository cosmoTDocumentoRepository;

  @Autowired
  private CosmoNotificationsNotificheGlobaliFeignClient notificheFeignClient;

  @Override
  public void deleteSigilloElettronico(Integer id) {

    String methodName = "deleteSigilloElettronico";

    ValidationUtils.require(id, "id sigillo elettronico");

    CosmoTCredenzialiSigilloElettronico sigilloElettronicoDaEliminare =
        cosmoTCredenzialiSigilloElettronicoRepository.findOneNotDeleted(id.longValue())
        .orElseThrow(
            () -> new NotFoundException(String.format(ErrorMessages.SIGILLO_ELETTRONICO_NON_TROVATO, id)));

    sigilloElettronicoDaEliminare.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));
    sigilloElettronicoDaEliminare.setUtenteCancellazione(AuditServiceImpl.getPrincipalCode());
    sigilloElettronicoDaEliminare =
        cosmoTCredenzialiSigilloElettronicoRepository.save(sigilloElettronicoDaEliminare);

    logger.info(methodName, "Sigillo elettronico con id {} eliminato", sigilloElettronicoDaEliminare.getId());
  }

  @Override
  public CredenzialiSigilloElettronico getSigilloElettronicoId(Integer id) {
    ValidationUtils.require(id, "id sigillo elettronico");

    CosmoTCredenzialiSigilloElettronico sigilloElettronico =
        cosmoTCredenzialiSigilloElettronicoRepository
        .findOneNotDeleted(id.longValue()).orElseThrow(
            () -> new NotFoundException(String.format(ErrorMessages.SIGILLO_ELETTRONICO_NON_TROVATO, id)));

    return cosmoTCredenzialiSigilloElettronicoMapper.toDto(sigilloElettronico);
  }

  @Override
  public CredenzialiSigilloElettronico updateSigilloElettronico(Integer id,
      CreaCredenzialiSigilloElettronicoRequest request) {
    String methodName = "updateSigilloElettronico";

    ValidationUtils.require(request, REQUEST);
    ValidationUtils.require(id, "id");
    ValidationUtils.require(request.getAlias(), "alias");
    ValidationUtils.require(request.getDelegatedDomain(), "delegatedDomain");
    ValidationUtils.require(request.getDelegatedPassword(), "delegatedPassword");
    ValidationUtils.require(request.getDelegatedUser(), "delegatedUser");
    ValidationUtils.require(request.getOtpPwd(), "otpPwd");
    ValidationUtils.require(request.getTipoHsm(), "tipoHsm");
    ValidationUtils.require(request.getTipoOtpAuth(), "tipoOtpAuth");
    ValidationUtils.require(request.getUtente(), "utente");
    ValidationUtils.validaAnnotations(request);

    CosmoTCredenzialiSigilloElettronico cosmoTCredenzialiSigilloElettronico =
        cosmoTCredenzialiSigilloElettronicoRepository
        .findOneNotDeleted(id.longValue()).orElseThrow(
            () -> new NotFoundException(String.format(ErrorMessages.SIGILLO_ELETTRONICO_NON_TROVATO, id)));

    cosmoTCredenzialiSigilloElettronico.setDelegatedDomain(request.getDelegatedDomain());
    cosmoTCredenzialiSigilloElettronico.setDelegatedPassword(request.getDelegatedPassword());
    cosmoTCredenzialiSigilloElettronico.setDelegatedUser(request.getDelegatedUser());
    cosmoTCredenzialiSigilloElettronico.setOtpPwd(request.getOtpPwd());
    cosmoTCredenzialiSigilloElettronico.setTipoHsm(request.getTipoHsm());
    cosmoTCredenzialiSigilloElettronico.setTipoOtpAuth(request.getTipoOtpAuth());
    cosmoTCredenzialiSigilloElettronico.setUtente(request.getUtente());
    cosmoTCredenzialiSigilloElettronico.setDtUltimaModifica(Timestamp.valueOf(LocalDateTime.now()));
    cosmoTCredenzialiSigilloElettronico
    .setUtenteUltimaModifica(AuditServiceImpl.getPrincipalCode());

    cosmoTCredenzialiSigilloElettronico =
        cosmoTCredenzialiSigilloElettronicoRepository.save(cosmoTCredenzialiSigilloElettronico);
    logger.info(methodName, "Sigillo Elettronico con id {} aggiornato",
        cosmoTCredenzialiSigilloElettronico.getId());

    return cosmoTCredenzialiSigilloElettronicoMapper.toDto(cosmoTCredenzialiSigilloElettronico);
  }

  @Override
  public CredenzialiSigilloElettronico creaSigilloElettronico(
      CreaCredenzialiSigilloElettronicoRequest request) {
    String methodName = "creaSigilloElettronico";

    ValidationUtils.require(request, REQUEST);
    ValidationUtils.require(request.getAlias(), "alias");
    ValidationUtils.require(request.getDelegatedDomain(), "delegatedDomain");
    ValidationUtils.require(request.getDelegatedPassword(), "delegatedPassword");
    ValidationUtils.require(request.getDelegatedUser(), "delegatedUser");
    ValidationUtils.require(request.getOtpPwd(), "otpPwd");
    ValidationUtils.require(request.getTipoHsm(), "tipoHsm");
    ValidationUtils.require(request.getTipoOtpAuth(), "tipoOtpAuth");
    ValidationUtils.require(request.getUtente(), "utente");
    ValidationUtils.validaAnnotations(request);

    Optional<CosmoTCredenzialiSigilloElettronico> cosmoTSigilloElettronicoEsistente =
        cosmoTCredenzialiSigilloElettronicoRepository.
        findOneNotDeletedByField(CosmoTCredenzialiSigilloElettronico_.alias,
            request.getAlias());

    if(cosmoTSigilloElettronicoEsistente.isPresent()) {
      throw new BadRequestException(String.format(ErrorMessages.SIGILLO_ELETTRONICO_CON_ALIAS_ESISTENTE, request.getAlias()));
    }
    CosmoTCredenzialiSigilloElettronico cosmoTSigilloElettronico =
        cosmoTCredenzialiSigilloElettronicoMapper.toRecord(request);
    cosmoTSigilloElettronico.setDtInserimento(Timestamp.valueOf(LocalDateTime.now()));
    cosmoTSigilloElettronico.setUtenteInserimento(AuditServiceImpl.getPrincipalCode());

    cosmoTSigilloElettronico =
        cosmoTCredenzialiSigilloElettronicoRepository.save(cosmoTSigilloElettronico);
    logger.info(methodName, "Sigillo Elettronico con id {} creato", cosmoTSigilloElettronico.getId());

    return cosmoTCredenzialiSigilloElettronicoMapper.toDto(cosmoTSigilloElettronico);
  }

  @Override
  public CredenzialiSigilloElettronicoResponse getSigilloElettronico(String filter) {
    CredenzialiSigilloElettronicoResponse output = new CredenzialiSigilloElettronicoResponse();

    GenericRicercaParametricaDTO<FiltroSigilloElettronicoDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroSigilloElettronicoDTO.class);

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(ParametriApplicativo.MAX_PAGE_SIZE).asInteger());

    Page<CosmoTCredenzialiSigilloElettronico> pageSigilloElettronico =
        cosmoTCredenzialiSigilloElettronicoRepository
        .findAllNotDeleted(CosmoTCredenzialiSigilloElettronicoSpecifications
            .findByFilters(ricercaParametrica.getFilter(), ricercaParametrica.getSort()), paging);

    List<CosmoTCredenzialiSigilloElettronico> sigilloElettronicoSuDB =
        pageSigilloElettronico.getContent();

    List<CredenzialiSigilloElettronico> sigilliElettronici = new LinkedList<>();
    sigilloElettronicoSuDB.forEach(sigilloSuDb -> sigilliElettronici
        .add(cosmoTCredenzialiSigilloElettronicoMapper.toDto(sigilloSuDb)));
    output.setSigilliElettronici(sigilliElettronici);

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      SearchUtils.filterFields(sigilliElettronici,
          Arrays.asList(ricercaParametrica.getFields().split(",")));
    }

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(pageSigilloElettronico.getNumber());
    pageInfo.setPageSize(pageSigilloElettronico.getSize());
    pageInfo.setTotalElements(Math.toIntExact(pageSigilloElettronico.getTotalElements()));
    pageInfo.setTotalPages(pageSigilloElettronico.getTotalPages());
    output.setPageInfo(pageInfo);

    return output;
  }

  @Override
  public void richiediApposizioneSigillo(String idPratica, RichiediApposizioneSigilloRequest body) {
    final var methodName = "richiediApposizioneSigillo";
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
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, REQUEST);
      logger.error(methodName, parametroNonValorizzato);
      throw new BadRequestException(parametroNonValorizzato);

    }

    if (null == body.getIdentificativoMessaggio()) {
      final var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "identificativoEvento");
      logger.error(methodName, parametroNonValorizzato);
      throw new BadRequestException(parametroNonValorizzato);
    }

    logger.info(methodName,
        String.format("Ricevuta richiesta di apposizione sigillo per la pratica %s, relativa all'evento %s",
            idPratica, body.getIdentificativoMessaggio()));

    var pratica =
        cosmoTPraticaRepository.findOneNotDeleted(Long.valueOf(idPratica))
            .orElseThrow(() -> new BadRequestException(ErrorMessages.P_PRATICA_NON_TROVATA));

    List<CosmoTDocumento> documentiDaSigillare = recuperaDocumentiDaSigillare(pratica, body);

     if (!documentiDaSigillare.isEmpty()) {

       CosmoDStatoSigilloElettronico daSigillare =
           this.cosmoDStatoSigilloElettronicoRepository
           .findOneActive(StatoSigilloElettronico.DA_SIGILLARE.getCodice()).orElseThrow(() -> {
             String notFoundStato =
                 String.format(ErrorMessages.STATO_SIGILLO_NON_PRESENTE_O_NON_ATTIVO,
                             StatoSigilloElettronico.DA_SIGILLARE.getCodice());
             logger.error(methodName, notFoundStato);
             throw new NotFoundException(notFoundStato);
           });


           if (null != this.cosmoTSigilloElettronicoRepository
               .findOne(
                   CosmoTSigilloElettronicoSpecifications.findSigilloElettronicoPratica(
                       Long.parseLong(idPratica), body.getIdentificativoMessaggio()))) {
         String sigilloEsistente = String.format(
             "Esiste gia' uno sigillo attivo per l'id pratica %s e l'identificativo evento %s",
             idPratica, body.getIdentificativoMessaggio());
         throw new ConflictException(sigilloEsistente);
       }

       // scrittura su cosmo_t_sigillo_elettronico per il singolo evento di sigillo
       var sigillo = new CosmoTSigilloElettronico();
       Timestamp now = Timestamp.valueOf(LocalDateTime.now());
       sigillo.setDtInserimento(now);
       sigillo.setIdentificativoEvento(body.getIdentificativoMessaggio());
       sigillo.setIdentificativoAlias(body.getIdentificativoAlias());
       sigillo.setUtenteInserimento(SecurityUtils.getUtenteCorrente().getCodiceFiscale());
       sigillo.setCosmoTPratica(pratica);
       sigillo.setUtilizzato(false);
       sigillo = this.cosmoTSigilloElettronicoRepository.saveAndFlush(sigillo);
       // scrittura di cosmo_r_sigillo_documento per ogni documento da sigillare
       for (CosmoTDocumento documento : documentiDaSigillare) {
         var relazioneSigillo = new CosmoRSigilloDocumento();
         relazioneSigillo.setCosmoDStatoSigilloElettronico(daSigillare);
         relazioneSigillo.setCosmoTDocumento(documento);
         relazioneSigillo.setCosmoTSigilloElettronico(sigillo);
         relazioneSigillo.setDtInizioVal(now);
         cosmoRSigilloDocumentoRepository.saveAndFlush(relazioneSigillo);
       }
       logger.info(methodName,
           String.format("Creato sigillo elettronico per la pratica %s, evento %s relativo a %d documenti",
               idPratica, body.getIdentificativoMessaggio(), documentiDaSigillare.size()));
     } else {
       logger.warn(methodName, String.format(
           "Non sono presenti documenti per la pratica %s: non verra' salvato alcuno sigillo elettronico",
           idPratica));
     }
   }

   /*
    * Recupero dei documenti da sigillare
    *
    * @see
    * it.csi.cosmo.cosmoecm.business.service.SigilloElettronicoService#recuperaDocumentiDaSigillare(
    * java.lang. Long)
    */
   @Override
   public List<CosmoTDocumento> recuperaDocumentiDaSigillare() {
     return cosmoTDocumentoRepository.findAll(
         CosmoTDocumentoSpecifications.findDaSigillare());
  }

  @Override
  public void salvaEsitoRichiestaApposizioneSigillo(
      EsitoRichiestaSigilloElettronicoDocumento esitoRichiestaSigillo, CosmoTDocumento documento,
      Long idSigillo) {
    final var methodName = "salvaEsitoRichiestaApposizioneSigillo";

    if (null == esitoRichiestaSigillo) {
      var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "esitoRichiestaSigillo");
      logger.error(methodName, parametroNonValorizzato);
      throw new InternalServerException(parametroNonValorizzato);
    }

    if (null == documento) {
      var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "documento");
      logger.error(methodName, parametroNonValorizzato);
      throw new InternalServerException(parametroNonValorizzato);
    }

    if (null == idSigillo) {
      var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "idSigillo");
      logger.error(methodName, parametroNonValorizzato);
      throw new InternalServerException(parametroNonValorizzato);
    }

    documento = cosmoTDocumentoRepository.findOne(documento.getId());

    CosmoRSigilloDocumento sigilloDocumento =
        documento.getCosmoRSigilloDocumentos().stream()
            .filter(rsd -> rsd.valido() && rsd.getCosmoTSigilloElettronico().getId().equals(idSigillo)).findFirst()
            .orElseThrow(() -> {
              var formatError = String.format(
                  ErrorMessages.DOCUMENTO_NON_PRESENTE_ID_SIGILLO_INSERIMENTO_ESITO_SIGILLO, idSigillo);
                      logger.error(methodName, formatError);
                      throw new NotFoundException(formatError);
            });

    sigilloDocumento.setCodiceEsitoSigillo(esitoRichiestaSigillo.getCodice());
    sigilloDocumento.setMessaggioEsitoSigillo(esitoRichiestaSigillo.getMessaggio());

    CosmoDStatoSigilloElettronico statoSigillo = null;

    if (Constants.ESITO_SIGILLO_OK.equals(esitoRichiestaSigillo.getCodice())) {

      statoSigillo = this.cosmoDStatoSigilloElettronicoRepository
          .findOneActive(StatoSigilloElettronico.SIGILLATO.getCodice()).orElseThrow(() -> {
            String notFoundStato =
                String.format(ErrorMessages.STATO_SIGILLO_NON_PRESENTE_O_NON_ATTIVO,
                        StatoSigilloElettronico.SIGILLATO.getCodice());
            logger.error(methodName, notFoundStato);
            throw new NotFoundException(notFoundStato);
          });
    } else {
      statoSigillo = this.cosmoDStatoSigilloElettronicoRepository
          .findOneActive(StatoSigilloElettronico.ERR_SIGILLO.getCodice()).orElseThrow(() -> {
            String notFoundStato =
                String.format(ErrorMessages.STATO_SIGILLO_NON_PRESENTE_O_NON_ATTIVO,
                    StatoSigilloElettronico.ERR_SIGILLO.getCodice());
            logger.error(methodName, notFoundStato);
            throw new NotFoundException(notFoundStato);
          });
    }
    sigilloDocumento.setCosmoDStatoSigilloElettronico(statoSigillo);

    cosmoRSigilloDocumentoRepository.save(sigilloDocumento);

    try {
      inviaNotificaStatoSigilloElettronico(documento, statoSigillo.getDescrizione());
    } catch (Exception e) {
      logger.error(methodName,
          "Errore durante invio notifica stato smistamento: " + e.getMessage());
    }

  }

  private void inviaNotificaStatoSigilloElettronico(CosmoTDocumento documento, String statoSigillo) {

    var methodName = "inviaNotificaStatoSigilloElettronico";

    logger.info(methodName, "Invio notifica stato sigillo elettronico");
    String messaggio = String.format(Constants.STATO_SIGILLO, statoSigillo,
        documento.getTitolo() != null ? documento.getTitolo()
            : getContenutoDocumento(documento).getNomeFile());

    NotificheGlobaliRequest request = new NotificheGlobaliRequest();
    request.setIdPratica(documento.getPratica().getId());
    request.setTipoNotifica(TipoNotifica.APP_SIGILLO_DOCUMENTI.getCodice());
    request.setMessaggio(messaggio);
    request.setClasse("SUCCESS");
    request.setEvento(Constants.EVENTS.DOCUMENTI_SIGILLATI);
    request.setCodiceIpaEnte(documento.getPratica().getEnte().getCodiceIpa());

    notificheFeignClient.postNotificheGlobali(request);
    logger.info(methodName, "Notifiche inviate");
  }

  /*
   * Controlli formali e recupero del contenuto del documento
   */
  private CosmoTContenutoDocumento getContenutoDocumento(CosmoTDocumento documento) {
    final var methodName = "checkInputSigilloElettronico";

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

  @Override
  public Long recuperaIdEnteDaDocumento(CosmoTDocumento documento) {
    documento = cosmoTDocumentoRepository.findOne(documento.getId());
    return documento.getPratica().getEnte().getId();
  }

  @Override
  public Set<Long> recuperaIdPraticaDaDocumento(CosmoTDocumento documento) {
    documento = cosmoTDocumentoRepository.findOne(documento.getId());
    return new HashSet<>(Arrays.asList(documento.getPratica().getId()));
  }

  @Override
  public void aggiornaStatoSigilloWip(CosmoRSigilloDocumento sigilloDocumento) {
    final var methodName = "aggiornaStatoSigilloWip";
    CosmoDStatoSigilloElettronico statoSigilloWip =
        this.cosmoDStatoSigilloElettronicoRepository
        .findOneActive(StatoSigilloElettronico.SIGILLO_WIP.getCodice()).orElseThrow(() -> {
          String notFoundStato =
              String.format(ErrorMessages.STATO_SIGILLO_NON_PRESENTE_O_NON_ATTIVO,
                      StatoSigilloElettronico.SIGILLO_WIP.getCodice());
          logger.error(methodName, notFoundStato);
          throw new NotFoundException(notFoundStato);
        });

        sigilloDocumento =
            cosmoRSigilloDocumentoRepository.findOneActive(sigilloDocumento.getId()).orElseThrow();

        sigilloDocumento.setCosmoDStatoSigilloElettronico(statoSigilloWip);
        cosmoRSigilloDocumentoRepository.saveAndFlush(sigilloDocumento);
  }

  @Override
  public boolean checkApposizioneSigilloDocumenti(List<CosmoTDocumento> documenti, Long idSigillo) {
    final var methodName = "impostaEsitoSigillo";
    int errori = 0;
    for (var documento : documenti) {
      documento = cosmoTDocumentoRepository.findOne(documento.getId());
      // recupero del record di smistamento afferente al documento
      var sigilloDocumento = documento.getCosmoRSigilloDocumentos().stream()
          .filter(rsd -> rsd.getCosmoTSigilloElettronico().getId().equals(idSigillo)).findFirst();
      if (sigilloDocumento.isEmpty()) {
        throw new InternalServerException("Nessun sigillo presente");
      }
      if (!StatoSigilloElettronico.SIGILLATO.getCodice()
          .equals(sigilloDocumento.get().getCosmoDStatoSigilloElettronico().getCodice())) {
        ++errori;
        logger.info(methodName, String.format(
            "Il documento con id %d non e' stato sigillato correttamente",
            documento.getId()));
      }
      logger.info(methodName, "Salvataggio del documento");
      cosmoTDocumentoRepository.save(documento);
      logger.info(methodName, "Documento salvato");
    }

    if (errori != 0) {

      var sigillo = cosmoTSigilloElettronicoRepository.findOne(idSigillo);
      sigillo.setUtilizzato(true);
      cosmoTSigilloElettronicoRepository.save(sigillo);
    }
    return errori == 0;


  }

  @Override
  public void aggiornaSigilliInErrore(Long idPratica, String identificativoEvento,
      String identificativoAlias) {
    final var methodName = "aggiornaSigilliInErrore";

    CosmoDStatoSigilloElettronico statoSigilloInErrore =
        this.cosmoDStatoSigilloElettronicoRepository
            .findOneActive(StatoSigilloElettronico.ERR_SIGILLO.getCodice()).orElseThrow(() -> {
              String notFoundStato =
                  String.format(ErrorMessages.STATO_SIGILLO_NON_PRESENTE_O_NON_ATTIVO,
                      StatoSigilloElettronico.ERR_SIGILLO.getCodice());
              logger.error(methodName, notFoundStato);
              throw new NotFoundException(notFoundStato);
            });

    CosmoDStatoSigilloElettronico statoSigilloDaSigillare =
        this.cosmoDStatoSigilloElettronicoRepository
            .findOneActive(StatoSigilloElettronico.DA_SIGILLARE.getCodice()).orElseThrow(() -> {
              String notFoundStato =
                  String.format(ErrorMessages.STATO_SIGILLO_NON_PRESENTE_O_NON_ATTIVO,
                      StatoSigilloElettronico.DA_SIGILLARE.getCodice());
              logger.error(methodName, notFoundStato);
              throw new NotFoundException(notFoundStato);
            });


    var findSigilliInErrore = cosmoRSigilloDocumentoRepository
        .findAllActive(
            (Root<CosmoRSigilloDocumento> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
              var joinSigillo = root.join(CosmoRSigilloDocumento_.cosmoTSigilloElettronico, JoinType.INNER);
                      return cb.and(
                          cb.equal(joinSigillo.get(CosmoTSigilloElettronico_.cosmoTPratica).get(CosmoTPratica_.id), idPratica),
                          cb.equal(joinSigillo.get(CosmoTSigilloElettronico_.identificativoEvento),identificativoEvento),
                          cb.equal(joinSigillo.get(CosmoTSigilloElettronico_.identificativoAlias), identificativoAlias),
                          cb.equal(root.get(CosmoRSigilloDocumento_.cosmoDStatoSigilloElettronico), statoSigilloInErrore),
                          cb.equal(joinSigillo.get(CosmoTSigilloElettronico_.utilizzato), Boolean.FALSE));
            });

    findSigilliInErrore.stream().forEach(sigilloInErrore -> {
      sigilloInErrore.setCosmoDStatoSigilloElettronico(statoSigilloDaSigillare);
      sigilloInErrore.setMessaggioEsitoSigillo("");
      sigilloInErrore.setCodiceEsitoSigillo("");
      cosmoRSigilloDocumentoRepository.save(sigilloInErrore);
      logger.info(methodName, String.format(
          "Aggiornamento dallo stato di errore allo stato di attesa apposizione sigillo per il documento con id %d",
          sigilloInErrore.getCosmoTDocumento().getId()));
    });

  }

  private List<CosmoTDocumento> recuperaDocumentiDaSigillare(CosmoTPratica pratica, RichiediApposizioneSigilloRequest body) {
    final var methodName = "recuperaDocumentiDaSigillare";
    logger.info(methodName, "Inizio reperimento documenti da sigillare");
    List<CosmoTDocumento> documentiDaSigillare = new ArrayList<>();

    Consumer<CodiceTipologiaDocumento> consumerCodiceTipologiaDocumento = tipoDocumento -> {
      if (null != tipoDocumento.getCodicePadre()) {
        pratica.getDocumenti().stream()
          .filter(CosmoTEntity::nonCancellato)
          .forEach(x ->
            x.getDocumentiFigli().forEach(figlio -> {
            if (figlio.nonCancellato()
                  && figlio.getDocumentoPadre().getTipo().getCodice().equals(tipoDocumento.getCodicePadre())
                  && figlio.getTipo().getCodice().equals(tipoDocumento.getCodice())) {
              documentiDaSigillare.add(figlio);
            }
          })
        );
      } else {
        documentiDaSigillare.addAll(pratica.getDocumenti().stream()
           .filter(x -> x.nonCancellato() && x.getTipo().getCodice().equals(tipoDocumento.getCodice()))
           .collect(Collectors.toList()));
      }
    };

    body.getTipiDocumento().stream().forEach(consumerCodiceTipologiaDocumento::accept);
    logger.info(methodName, "Fine reperimento documenti da sigillare");
    return documentiDaSigillare;
  }

}

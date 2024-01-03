/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.CosmoDStatoPratica;
import it.csi.cosmo.common.entities.CosmoDTabDettaglio;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoDTipoPratica_;
import it.csi.cosmo.common.entities.CosmoDTrasformazioneDatiPratica;
import it.csi.cosmo.common.entities.CosmoRFormatoFileTipoDocumento;
import it.csi.cosmo.common.entities.CosmoRFormatoFileTipoDocumentoPK;
import it.csi.cosmo.common.entities.CosmoRGruppoTipoPratica;
import it.csi.cosmo.common.entities.CosmoRGruppoTipoPraticaPK;
import it.csi.cosmo.common.entities.CosmoRGruppoTipoPratica_;
import it.csi.cosmo.common.entities.CosmoRStatoTipoPratica;
import it.csi.cosmo.common.entities.CosmoRTabDettaglioTipoPratica;
import it.csi.cosmo.common.entities.CosmoRTabDettaglioTipoPraticaPK;
import it.csi.cosmo.common.entities.CosmoRTipoDocumentoTipoDocumento;
import it.csi.cosmo.common.entities.CosmoRTipoDocumentoTipoDocumentoPK;
import it.csi.cosmo.common.entities.CosmoRTipoDocumentoTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoRTipodocTipopratica;
import it.csi.cosmo.common.entities.CosmoRTipodocTipopraticaPK;
import it.csi.cosmo.common.entities.CosmoRTipodocTipopratica_;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;
import it.csi.cosmo.common.entities.proto.CosmoREntity;
import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import it.csi.cosmo.common.entities.proto.IntervalloValiditaEntity;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.exception.UnprocessableEntityException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.EnteDTO;
import it.csi.cosmo.common.util.ComplexListComparator;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmopratiche.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmopratiche.business.service.TipiPraticheService;
import it.csi.cosmo.cosmopratiche.config.ErrorMessages;
import it.csi.cosmo.cosmopratiche.config.ParametriApplicativo;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaTipoPraticaDocumentoRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaTipoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaTipoPraticaStatoRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaTipoPraticaTrasformazioneDatiRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaTipoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.FiltroRicercaTipiPraticheDTO;
import it.csi.cosmo.cosmopratiche.dto.rest.FormatoFile;
import it.csi.cosmo.cosmopratiche.dto.rest.PageInfo;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoGruppo;
import it.csi.cosmo.cosmopratiche.dto.rest.TabsDettaglio;
import it.csi.cosmo.cosmopratiche.dto.rest.TipiPraticheResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoPratica;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoDCustomFormFormioMapper;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoDFormatoFileMapper;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoDTipoDocumentoMapper;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoDTipoPraticaMapper;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDCustomFormFormioRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDEnteCertificatoreRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDFormatoFileRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDProfiloFeqRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDSceltaMarcaTemporaleRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDStatoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDTabsDettaglioRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDTipoCredenzialiFirmaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDTipoOtpRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDTipoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDTrasformazioneDatiPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRFormatoFileTipoDocumentoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRGruppoTipoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRStatoTipoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRTabDettaglioTipoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRTipoDocTipoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRTipoDocumentoTipoDocumentoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTGruppoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTipoDocumentoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.specifications.CosmoDTipiPraticheSearchHandler;
import it.csi.cosmo.cosmopratiche.security.SecurityUtils;
import it.csi.cosmo.cosmopratiche.security.UseCase;
import it.csi.cosmo.cosmopratiche.util.logger.LogCategory;
import it.csi.cosmo.cosmopratiche.util.logger.LoggerFactory;

@Service
@Transactional
public class TipiPraticheServiceImpl implements TipiPraticheService {

  private static final String CLASS_NAME = TipiPraticheServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger = LoggerFactory.getLogger(LogCategory.COSMOPRATICHE_BUSINESS_LOG_CATEGORY,
      CLASS_NAME);

  @Autowired
  private CosmoDTipoPraticaRepository cosmoTipoPraticaRepository;

  @Autowired
  private CosmoDStatoPraticaRepository cosmoStatoPraticaRepository;

  @Autowired
  private CosmoRStatoTipoPraticaRepository cosmoRStatoTipoPraticaRepository;

  @Autowired
  private CosmoRTipoDocTipoPraticaRepository cosmoRTipoDocTipoPraticaRepository;

  @Autowired
  private CosmoTipoDocumentoRepository cosmoTipoDocumentoRepository;

  @Autowired
  private CosmoDTrasformazioneDatiPraticaRepository cosmoDTrasformazioneDatiPraticaRepository;

  @Autowired
  private CosmoDCustomFormFormioRepository cosmoDCustomFormFormioRepository;

  @Autowired
  private CosmoDTipoPraticaMapper mapper;

  @Autowired
  private CosmoDCustomFormFormioMapper customFormFormioMapper;

  @Autowired
  private CosmoDTabsDettaglioRepository cosmoDTabsDettaglioRepository;

  @Autowired
  private CosmoRTabDettaglioTipoPraticaRepository cosmoRTabDettaglioTipoPraticaRepository;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoRGruppoTipoPraticaRepository cosmoRGruppoTipoPraticaRepository;

  @Autowired
  private CosmoTGruppoRepository cosmoTGruppoRepository;

  @Autowired
  private CosmoDFormatoFileRepository cosmoDFormatoFileRepository;

  @Autowired
  private CosmoRFormatoFileTipoDocumentoRepository cosmoRFormatoFileTipoDocumentoRepository;

  @Autowired
  private CosmoRTipoDocumentoTipoDocumentoRepository cosmoRTipoDocumentoTipoDocumentoRepository;

  @Autowired
  private CosmoDEnteCertificatoreRepository cosmoDEnteCertificatoreRepository;

  @Autowired
  private CosmoDTipoCredenzialiFirmaRepository cosmoDTipoCredenzialiFirmaRepository;

  @Autowired
  private CosmoDTipoOtpRepository cosmoDTipoOtpRepository;

  @Autowired
  private CosmoDProfiloFeqRepository cosmoDProfiloFeqRepository;

  @Autowired
  private CosmoDSceltaMarcaTemporaleRepository cosmoDSceltaMarcaTemporaleRepository;

  @Autowired
  private CosmoDFormatoFileMapper cosmoDFormatoFileMapper;

  @Autowired
  private CosmoDTipoDocumentoMapper cosmoDTipoDocumentoMapper;

  @Override
  @Transactional(readOnly = true)
  public List<TipoPratica> getTipiPraticaPerEnte(Integer idEnte, Boolean creazionePratica,
      Boolean conEnte) {
    Long id;

    if (null == idEnte || idEnte == 0) {
      EnteDTO ente = SecurityUtils.getUtenteCorrente().getEnte();
      id = null != ente ? ente.getId() : null;
    } else {
      id = idEnte.longValue();
    }

    var idUtente = SecurityUtils.getUtenteCorrente().getId() != null ? SecurityUtils.getUtenteCorrente().getId()
        : null;
    if (idUtente == null) {
      throw new BadRequestException("Utente non loggato");
    }
    boolean isAdmin = SecurityUtils.getUtenteCorrente().getProfilo().getUseCases().stream()
        .anyMatch(x -> x != null && x.getCodice().equals(UseCase.ADMIN_COSMO.name()));
    if (id == null && !isAdmin) {
      throw new BadRequestException("Ente non indicato");
    }


    if (creazionePratica != null && Boolean.TRUE.equals(creazionePratica)) {

      Timestamp now = Timestamp.from(Instant.now());

      List<TipoPratica> output = new LinkedList<>();

      List<CosmoDTipoPratica> tipopratiche = cosmoTipoPraticaRepository.findAllActive((root, cq, cb) -> {

        ListJoin<CosmoDTipoPratica, CosmoRGruppoTipoPratica> joinCodTipPrat = root
            .join(CosmoDTipoPratica_.cosmoRGruppoTipoPraticas, JoinType.LEFT);

        Predicate predicate = cb.and();
        if (id != null) {
          predicate =
              cb.and(cb.equal(root.get(CosmoDTipoPratica_.cosmoTEnte).get(CosmoTEnte_.id), id));
        }
        predicate = cb.and(predicate,
            cb.or(cb.isNull(joinCodTipPrat.get(CosmoRGruppoTipoPratica_.cosmoTGruppo)), cb.and(
                cb.or(cb.isNull(joinCodTipPrat.get(CosmoREntity_.dtFineVal)),
                    cb.greaterThanOrEqualTo(joinCodTipPrat.get(CosmoREntity_.dtFineVal), now)))));

        return cq.where(predicate).distinct(true).getRestriction();

      });

      tipopratiche.stream()
      .filter(tipoPratica -> tipoPratica.getCosmoRGruppoTipoPraticas().isEmpty()
          || tipoPratica.getCosmoRGruppoTipoPraticas().stream()
          .filter(IntervalloValiditaEntity::valido)
          .allMatch(r -> r.getCreatore() == null || Boolean.FALSE.equals(r.getCreatore()))
          || tipoPratica.getCosmoRGruppoTipoPraticas().stream()
          .anyMatch(r -> r.valido() && Boolean.TRUE.equals(r.getCreatore())
              && r.getCosmoTGruppo().getAssociazioniUtenti().stream()
              .anyMatch(
                  u -> u.nonCancellato() && idUtente.equals(u.getUtente().getId()))))
      .forEach(tipopratica -> output.add(mapper.toLightDTO(tipopratica)));
      return output;

    } else {

      return cosmoTipoPraticaRepository.findAllActive().stream()
          .filter(tipoPratica -> (id == null || tipoPratica.getCosmoTEnte().getId().equals(id)))
          .map(conEnte != null && Boolean.TRUE.equals(conEnte) ? mapper::toLightDTOEnte
              : mapper::toLightDTO)
          .collect(Collectors.toList());
    }
  }

  @Override
  public TipiPraticheResponse getTipiPratica(String filter) {
    TipiPraticheResponse output = new TipiPraticheResponse();

    GenericRicercaParametricaDTO<FiltroRicercaTipiPraticheDTO> ricercaParametrica = SearchUtils
        .getRicercaParametrica(filter, FiltroRicercaTipiPraticheDTO.class);

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(ParametriApplicativo.MAX_PAGE_SIZE).asInteger());

    Page<CosmoDTipoPratica> pageTipiPratiche = cosmoTipoPraticaRepository
        .findAllActive(new CosmoDTipiPraticheSearchHandler().findByFilters(ricercaParametrica.getFilter(),
            ricercaParametrica.getSort()), paging);

    List<CosmoDTipoPratica> tipiPraticheSuDB = pageTipiPratiche.getContent();

    List<TipoPratica> tipiPratiche = new LinkedList<>();

    tipiPraticheSuDB.forEach(tipiPraticaSuDB -> tipiPratiche.add(mapper.toLightDTOEnte(tipiPraticaSuDB)));
    output.setTipiPratiche(tipiPratiche);

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      it.csi.cosmo.common.util.SearchUtils.filterFields(tipiPratiche,
          Arrays.asList(ricercaParametrica.getFields().split(",")));
    }

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(pageTipiPratiche.getNumber());
    pageInfo.setPageSize(pageTipiPratiche.getSize());
    pageInfo.setTotalElements(Math.toIntExact(pageTipiPratiche.getTotalElements()));
    pageInfo.setTotalPages(pageTipiPratiche.getTotalPages());

    output.setPageInfo(pageInfo);

    return output;
  }


  @Override
  @Transactional(readOnly = true)
  public TipoPratica getTipoPratica(String codice) {
    ValidationUtils.require(codice, "codice");

    CosmoDTipoPratica entity = cosmoTipoPraticaRepository.findOneActive(codice).orElseThrow(NotFoundException::new);

    entity.setTrasformazioni(
        entity.getTrasformazioni().stream().filter(CosmoDEntity::valido).collect(Collectors.toList()));

    entity.setCosmoRStatoTipoPraticas(entity.getCosmoRStatoTipoPraticas().stream().filter(CosmoREntity::valido)
        .filter(r -> r.getCosmoDStatoPratica().valido()).collect(Collectors.toList()));

    entity.setCosmoRTipodocTipopraticas(entity.getCosmoRTipodocTipopraticas().stream()
        .filter(CosmoREntity::valido).filter(r -> r.getCosmoDTipoDocumento().valido())
        .filter(r -> r.getCosmoDTipoDocumento().getCosmoRTipoDocumentoTipoDocumentosAllegato()
            .isEmpty())
        .map(r -> {
          r.getCosmoDTipoDocumento().setCosmoRTipoDocumentoTipoDocumentosAllegato(
              r.getCosmoDTipoDocumento().getCosmoRTipoDocumentoTipoDocumentosPadre().stream()
              .filter(IntervalloValiditaEntity::valido)
              .filter(x -> x.getCosmoDTipoDocumentoAllegato().valido()
                  && x.getCosmoDTipoDocumentoAllegato().getCosmoRTipodocTipopraticas().stream()
                  .anyMatch(t -> t.valido()
                      && t.getCosmoDTipoPratica().getCodice().equals(entity.getCodice())))
              .collect(Collectors.toList()));

          r.getCosmoDTipoDocumento().setCosmoRFormatoFileTipoDocumentos(
              r.getCosmoDTipoDocumento().getCosmoRFormatoFileTipoDocumentos().stream()
              .filter(CosmoREntity::valido).collect(Collectors.toList()));
          return r;
        }).collect(Collectors.toList()));

    entity.setCosmoRTabDettaglioTipoPraticas(
        entity.getCosmoRTabDettaglioTipoPraticas().stream().filter(CosmoREntity::valido)
        .filter(r -> r.getCosmoDTabDettaglio().valido()).collect(Collectors.toList()));

    var output = mapper.toDTO(entity);
    output.setImmagine(entity.getIcona() == null ? null : new String(entity.getIcona()));

    output.getTipiDocumento().forEach(tipoDocumento ->
    entity.getCosmoRTipodocTipopraticas().forEach(r -> {

      if(r.getCosmoDTipoDocumento().getCodice().equals(tipoDocumento.getCodice())) {
        tipoDocumento
        .setAllegati(r.getCosmoDTipoDocumento().getCosmoRTipoDocumentoTipoDocumentosAllegato()
            .stream()
            .filter(a -> a.valido() && a.getCosmoDTipoDocumentoAllegato().valido()
                && a.getCosmoDTipoPratica().getCodice().equals(entity.getCodice()))
            .map(a -> {
                  var allegato =
                      cosmoDTipoDocumentoMapper.toDTO(a.getCosmoDTipoDocumentoAllegato());
                  allegato.setCodiceStardas(a.getCodiceStardasAllegato());
                  return allegato;
            })
            .collect(Collectors.toList()));
      }
    })
        );

    var customFormios = entity.getCosmoDCustomFormFormios().stream().filter(CosmoDEntity::valido)
        .collect(Collectors.toList());

    if (customFormios != null && customFormios.size() > 1) {
      String message = String.format(ErrorMessages.TROPPI_CUSTOM_FORM_CON_CODICE_TIPO_PRATICA_ASSOCIATI,
          entity.getCodice());
      logger.error("getTipoPratica", message);
      throw new ConflictException(message);
    }

    if (customFormios != null && customFormios.size() == 1) {
      output.setCustomForm(customFormFormioMapper.toDTO(customFormios.get(0)));
    }


    var gruppoTipoPratica = entity.getCosmoRGruppoTipoPraticas().stream()
        .filter(r -> r.valido() && Boolean.TRUE.equals(r.getCreatore())).findFirst();

    if (gruppoTipoPratica.isPresent()) {
      RiferimentoGruppo rg = new RiferimentoGruppo();
      rg.setCodice(gruppoTipoPratica.get().getCosmoTGruppo().getCodice());
      rg.setDescrizione(gruppoTipoPratica.get().getCosmoTGruppo().getDescrizione());
      rg.setId(gruppoTipoPratica.get().getCosmoTGruppo().getId());
      rg.setNome(gruppoTipoPratica.get().getCosmoTGruppo().getNome());
      output.setGruppoCreatore(rg);
    }

    var gruppoSupTipoPratica = entity.getCosmoRGruppoTipoPraticas().stream()
        .filter(r -> r.valido() && Boolean.TRUE.equals(r.getSupervisore())).findFirst();

    if (gruppoSupTipoPratica.isPresent()) {
      RiferimentoGruppo rgs = new RiferimentoGruppo();
      rgs.setCodice(gruppoSupTipoPratica.get().getCosmoTGruppo().getCodice());
      rgs.setDescrizione(gruppoSupTipoPratica.get().getCosmoTGruppo().getDescrizione());
      rgs.setId(gruppoSupTipoPratica.get().getCosmoTGruppo().getId());
      rgs.setNome(gruppoSupTipoPratica.get().getCosmoTGruppo().getNome());
      output.setGruppoSupervisore(rgs);
    }


    return output;
  }

  @Override
  public TipoPratica postTipoPratica(CreaTipoPraticaRequest body) {
    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    Timestamp now = Timestamp.from(Instant.now());

    body.setCodice(clean(body.getCodice()));
    body.setCaseDefinitionKey(clean(body.getCaseDefinitionKey()));
    body.setCodiceApplicazioneStardas(clean(body.getCodiceApplicazioneStardas()));
    body.setDescrizione(clean(body.getDescrizione()));
    body.setProcessDefinitionKey(clean(body.getProcessDefinitionKey()));

    body.setResponsabileTrattamentoStardas(clean(body.getResponsabileTrattamentoStardas()));

    // check conflict on field 'codice'
    checkConflict(CosmoDTipoPratica_.codice, body.getCodice(), null);

    CosmoDTipoPratica entity = new CosmoDTipoPratica();

    entity.setCosmoTEnte(cosmoTipoPraticaRepository.reference(CosmoTEnte.class, body.getIdEnte()));

    entity.setDtInizioVal(now);
    entity.setCaseDefinitionKey(body.getCaseDefinitionKey());
    entity.setCodice(body.getCodice());
    entity.setCodiceApplicazioneStardas(body.getCodiceApplicazioneStardas());
    entity.setDescrizione(body.getDescrizione());
    entity.setProcessDefinitionKey(body.getProcessDefinitionKey());
    entity.setCosmoRStatoTipoPraticas(new ArrayList<>());
    entity.setCosmoRTipodocTipopraticas(new ArrayList<>());
    entity.setCreabileDaInterfaccia(body.isCreabileDaInterfaccia());
    entity.setCreabileDaServizio(body.isCreabileDaServizio());
    entity.setAnnullabile(body.isAnnullabile());
    entity.setCondivisibile(body.isCondivisibile());
    entity.setAssegnabile(body.isAssegnabile());
    entity.setTrasformazioni(new ArrayList<>());
    entity.setCodiceFruitoreStardas(body.getCodiceFruitoreStardas());
    entity.setOverrideFruitoreDefault(body.isOverrideFruitoreDefault());
    entity.setRegistrazioneStilo(body.getRegistrazioneStilo());
    entity.setTipoUnitaDocumentariaStilo(body.getTipoUnitaDocumentariaStilo());
    entity.setIcona(StringUtils.isBlank(body.getImmagine()) ? null : body.getImmagine().getBytes());

    entity.setResponsabileTrattamentoStardas(body.getResponsabileTrattamentoStardas());
    entity.setOverrideResponsabileTrattamento(body.isOverrideResponsabileTrattamento());

    if (!StringUtils.isBlank(body.getCodiceEnteCertificatore())) {
      var enteCertificatore =
          cosmoDEnteCertificatoreRepository.findOneActive(body.getCodiceEnteCertificatore())
          .orElseThrow(
              () -> new BadRequestException(ErrorMessages.ENTE_CERITFICATORE_NON_TROVATO));
      entity.setCosmoDEnteCertificatore(enteCertificatore);
    }

    if (!StringUtils.isBlank(body.getCodiceTipoCredenziale())) {
      var tipoCredenzialiFirma =
          cosmoDTipoCredenzialiFirmaRepository.findOneActive(body.getCodiceTipoCredenziale())
          .orElseThrow(
              () -> new BadRequestException(ErrorMessages.TIPO_CREDENZIALI_FIRMA_NON_TROVATO));
      entity.setCosmoDTipoCredenzialiFirma(tipoCredenzialiFirma);
    }

    if (!StringUtils.isBlank(body.getCodiceProfiloFEQ())) {
      var profiloFeq = cosmoDProfiloFeqRepository.findOneActive(body.getCodiceProfiloFEQ())
          .orElseThrow(() -> new BadRequestException(ErrorMessages.PROFILO_FEQ_NON_TROVATO));
      entity.setCosmoDProfiloFeq(profiloFeq);
    }

    if (!StringUtils.isBlank(body.getCodiceTipoOtp())) {
      var tipoOtp = cosmoDTipoOtpRepository.findOneActive(body.getCodiceTipoOtp())
          .orElseThrow(() -> new BadRequestException(ErrorMessages.TIPO_OTP_NON_TROVATO));
      entity.setCosmoDTipoOtp(tipoOtp);
    }

    if (!StringUtils.isBlank(body.getCodiceSceltaMarcaTemporale())) {
      var sceltaMarcaTemporale =
          cosmoDSceltaMarcaTemporaleRepository.findOneActive(body.getCodiceSceltaMarcaTemporale())
          .orElseThrow(
              () -> new BadRequestException(ErrorMessages.SCELTA_MARCA_TEMPORALE_NON_TROVATA));
      entity.setCosmoDSceltaMarcaTemporale(sceltaMarcaTemporale);
    }

    controlliIntegrita(entity);
    entity = cosmoTipoPraticaRepository.save(entity);

    aggiornaStatiPratica(entity, body.getStati());
    aggiornaTipiDocumentoPratica(entity, body.getTipiDocumento(), null);
    aggiornaTrasformazioniPratica(entity, body.getTrasformazioni());
    aggiornaCustomForm(body.getCodiceCustomForm(), entity);
    aggiornaTabsDettaglio(entity, body.getTabsDettaglio());
    if (body.getIdGruppoCreatore() != null && body.getIdGruppoCreatore() > 0) {
      aggiornaGruppoCreatorePratica(entity, body.getIdGruppoCreatore());
    }

    if (body.getIdGruppoSupervisore() != null && body.getIdGruppoSupervisore() > 0) {
      aggiornaGruppoSupervisorePratica(entity, body.getIdGruppoSupervisore());
    }

    var output = mapper.toDTO(entity);
    output.setImmagine(entity.getIcona() == null ? null : new String(entity.getIcona()));
    return output;
  }

  @Override
  public TipoPratica putTipoPratica(String codice, AggiornaTipoPraticaRequest body) {
    ValidationUtils.require(codice, "codice");
    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    body.setCaseDefinitionKey(clean(body.getCaseDefinitionKey()));
    body.setCodiceApplicazioneStardas(clean(body.getCodiceApplicazioneStardas()));
    body.setDescrizione(clean(body.getDescrizione()));
    body.setProcessDefinitionKey(clean(body.getProcessDefinitionKey()));

    body.setResponsabileTrattamentoStardas(clean(body.getResponsabileTrattamentoStardas()));

    CosmoDTipoPratica entity = cosmoTipoPraticaRepository.findOneActive(codice).orElseThrow(NotFoundException::new);

    entity.setCosmoTEnte(cosmoTipoPraticaRepository.reference(CosmoTEnte.class, body.getIdEnte()));

    entity.setCaseDefinitionKey(body.getCaseDefinitionKey());
    entity.setCodiceApplicazioneStardas(body.getCodiceApplicazioneStardas());
    entity.setDescrizione(body.getDescrizione());
    entity.setProcessDefinitionKey(body.getProcessDefinitionKey());
    entity.setCreabileDaInterfaccia(body.isCreabileDaInterfaccia());
    entity.setCreabileDaServizio(body.isCreabileDaServizio());

    entity.setResponsabileTrattamentoStardas(body.getResponsabileTrattamentoStardas());
    entity.setOverrideResponsabileTrattamento(body.isOverrideResponsabileTrattamento());
    entity.setAnnullabile(body.isAnnullabile());
    entity.setCondivisibile(body.isCondivisibile());
    entity.setAssegnabile(body.isAssegnabile());
    entity.setCodiceFruitoreStardas(body.getCodiceFruitoreStardas());
    entity.setOverrideFruitoreDefault(body.isOverrideFruitoreDefault());
    entity.setRegistrazioneStilo(body.getRegistrazioneStilo());
    entity.setTipoUnitaDocumentariaStilo(body.getTipoUnitaDocumentariaStilo());
    entity.setIcona(StringUtils.isBlank(body.getImmagine()) ? null : body.getImmagine().getBytes());

    if (!StringUtils.isBlank(body.getCodiceEnteCertificatore())) {
      var enteCertificatore =
          cosmoDEnteCertificatoreRepository.findOneActive(body.getCodiceEnteCertificatore())
          .orElseThrow(
              () -> new BadRequestException(ErrorMessages.ENTE_CERITFICATORE_NON_TROVATO));
      entity.setCosmoDEnteCertificatore(enteCertificatore);
    } else {
      entity.setCosmoDEnteCertificatore(null);
    }

    if (!StringUtils.isBlank(body.getCodiceTipoCredenziale())) {
      var tipoCredenzialiFirma =
          cosmoDTipoCredenzialiFirmaRepository.findOneActive(body.getCodiceTipoCredenziale())
          .orElseThrow(
              () -> new BadRequestException(ErrorMessages.TIPO_CREDENZIALI_FIRMA_NON_TROVATO));
      entity.setCosmoDTipoCredenzialiFirma(tipoCredenzialiFirma);
    } else {
      entity.setCosmoDTipoCredenzialiFirma(null);
    }

    if (!StringUtils.isBlank(body.getCodiceProfiloFEQ())) {
      var profiloFeq = cosmoDProfiloFeqRepository.findOneActive(body.getCodiceProfiloFEQ())
          .orElseThrow(() -> new BadRequestException(ErrorMessages.PROFILO_FEQ_NON_TROVATO));
      entity.setCosmoDProfiloFeq(profiloFeq);
    } else {
      entity.setCosmoDProfiloFeq(null);
    }

    if (!StringUtils.isBlank(body.getCodiceTipoOtp())) {
      var tipoOtp = cosmoDTipoOtpRepository.findOneActive(body.getCodiceTipoOtp())
          .orElseThrow(() -> new BadRequestException(ErrorMessages.TIPO_OTP_NON_TROVATO));
      entity.setCosmoDTipoOtp(tipoOtp);
    } else {
      entity.setCosmoDTipoOtp(null);
    }

    if (!StringUtils.isBlank(body.getCodiceSceltaMarcaTemporale())) {
      var sceltaMarcaTemporale =
          cosmoDSceltaMarcaTemporaleRepository.findOneActive(body.getCodiceSceltaMarcaTemporale())
          .orElseThrow(
              () -> new BadRequestException(ErrorMessages.SCELTA_MARCA_TEMPORALE_NON_TROVATA));
      entity.setCosmoDSceltaMarcaTemporale(sceltaMarcaTemporale);
    } else {
      entity.setCosmoDSceltaMarcaTemporale(null);
    }

    controlliIntegrita(entity);

    cosmoTipoPraticaRepository.save(entity);

    aggiornaStatiPratica(entity, body.getStati());
    aggiornaTipiDocumentoPratica(entity, body.getTipiDocumento(), null);
    aggiornaTrasformazioniPratica(entity, body.getTrasformazioni());
    aggiornaCustomForm(body.getCodiceCustomForm(), entity);
    aggiornaTabsDettaglio(entity, body.getTabsDettaglio());
    if(body.getIdGruppoCreatore() != null) {
      aggiornaGruppoCreatorePratica(entity, body.getIdGruppoCreatore());
    }else {
      deleteGruppoCreatorePratica(entity, body.getIdGruppoCreatore());
    }

    if(body.getIdGruppoSupervisore() != null) {
      aggiornaGruppoSupervisorePratica(entity, body.getIdGruppoSupervisore());
    }else {
      deleteGruppoSupervisorePratica(entity, body.getIdGruppoSupervisore());
    }

    var output = mapper.toDTO(entity);
    output.setImmagine(entity.getIcona() == null ? null : new String(entity.getIcona()));
    return output;
  }

  private void controlliIntegrita(CosmoDTipoPratica entity) {
    boolean hasPDK = !StringUtils.isBlank(entity.getProcessDefinitionKey());
    boolean hasCDK = !StringUtils.isBlank(entity.getCaseDefinitionKey());

    if (hasPDK == hasCDK) {
      throw new UnprocessableEntityException(
          "Uno fra processDefinitionKey e caseDefinitionKey deve essere valorizzato");
    }


    boolean hasCAS = !StringUtils.isBlank(entity.getCodiceApplicazioneStardas());
    boolean hasRTS = !StringUtils.isBlank(entity.getResponsabileTrattamentoStardas());
    if (!hasCAS && (entity.getOverrideResponsabileTrattamento() || hasRTS)) {
      throw new UnprocessableEntityException(
          "Responsabile trattamento Stardas e Override responsabile trattamento possono essere valorizzati solo se Codice Applicazione Stardas e' valorizzato");
    }
  }

  @Override
  public void deleteTipoPratica(String codice) {

    ValidationUtils.require(codice, "codice");
    CosmoDTipoPratica entity =
        cosmoTipoPraticaRepository.findOneActive(codice).orElseThrow(NotFoundException::new);
    entity.getCosmoRStatoTipoPraticas().forEach(stato ->
    cosmoRStatoTipoPraticaRepository.deactivate(stato));

    entity.getCosmoRTipodocTipopraticas()
    .forEach(tipo -> cosmoRTipoDocTipoPraticaRepository.deactivate(tipo));

    entity.getTrasformazioni() .forEach(trasformazione ->
    cosmoDTrasformazioneDatiPraticaRepository.deactivate(trasformazione));

    entity.getCosmoDCustomFormFormios() .forEach(customForm ->
    cosmoDCustomFormFormioRepository.deactivate(customForm));

    entity.getCosmoRTabDettaglioTipoPraticas() .forEach(tabDettaglio ->
    cosmoRTabDettaglioTipoPraticaRepository.deactivate(tabDettaglio));

    entity.getCosmoRGruppoTipoPraticas() .forEach(gruppo ->
    cosmoRGruppoTipoPraticaRepository.deactivate(gruppo));

    cosmoTipoPraticaRepository.deactivate(entity);

  }


  private void aggiornaStatiPratica(CosmoDTipoPratica entity, List<AggiornaTipoPraticaStatoRequest> inputList) {

    var now = Timestamp.from(Instant.now());

    List<CosmoRStatoTipoPratica> esistentiAttivi = entity.getCosmoRStatoTipoPraticas().stream()
        .filter(CosmoREntity::valido).collect(Collectors.toList());

    ComplexListComparator
    .compareLists(esistentiAttivi, inputList,
        (existing, input) -> existing.getCosmoDStatoPratica() != null
        && existing.getCosmoDStatoPratica().getCodice().equals(input.getCodice()))
    .onElementsInFirstNotInSecond(daEliminare -> {
      cosmoRStatoTipoPraticaRepository.deactivate(daEliminare);
      entity.getCosmoRStatoTipoPraticas().remove(daEliminare);

    }).onElementsInSecondNotInFirst(daInserire -> {

      var statoDaAssociare = getOrCreate(daInserire);
      var newRel = new CosmoRStatoTipoPratica();
      newRel.setCosmoDStatoPratica(statoDaAssociare);
      newRel.setCosmoDTipoPratica(entity);
      newRel.setDtInizioVal(now);

      entity.getCosmoRStatoTipoPraticas().add(cosmoRStatoTipoPraticaRepository.save(newRel));

    }).onElementsInBoth((existing, input) -> {

      var stato = existing.getCosmoDStatoPratica();
      stato.setClasse(input.getClasse());
      stato.setDescrizione(input.getDescrizione());
      cosmoStatoPraticaRepository.save(stato);
    });
  }

  private CosmoDStatoPratica getOrCreate(AggiornaTipoPraticaStatoRequest daInserire) {
    ValidationUtils.require(daInserire, "request");
    ValidationUtils.validaAnnotations(daInserire);

    Optional<CosmoDStatoPratica> found = cosmoStatoPraticaRepository.findOneActive(daInserire.getCodice());
    if (found.isPresent()) {
      // already exists, return existing
      return found.get();
    }

    // missing, create and return
    CosmoDStatoPratica created = new CosmoDStatoPratica();
    created.setClasse(daInserire.getClasse());
    created.setCodice(daInserire.getCodice());
    created.setCosmoRStatoTipoPraticas(new ArrayList<>());
    created.setDescrizione(daInserire.getDescrizione());
    created.setDtInizioVal(Timestamp.from(Instant.now()));

    logger.info("getOrCreate", "registro nuovo stato pratica {}", created.getCodice());

    return cosmoStatoPraticaRepository.save(created);
  }

  private void aggiornaTabsDettaglio(CosmoDTipoPratica entity, List<TabsDettaglio> inputList) {

    var now = Timestamp.from(Instant.now());

    List<CosmoRTabDettaglioTipoPratica> esistentiAttivi = safe(entity.getCosmoRTabDettaglioTipoPraticas()).stream()
        .filter(CosmoREntity::valido).collect(Collectors.toList());

    ComplexListComparator
    .compareLists(esistentiAttivi, inputList,
        (existing, input) -> existing.getCosmoDTabDettaglio() != null
        && existing.getCosmoDTabDettaglio().getCodice().equals(input.getCodice()))

    .onElementsInFirstNotInSecond(daEliminare -> {
      cosmoRTabDettaglioTipoPraticaRepository.deactivate(daEliminare);
      entity.getCosmoRTabDettaglioTipoPraticas().remove(daEliminare);

    }).onElementsInSecondNotInFirst(daInserire -> {

      Optional<CosmoDTabDettaglio> found = cosmoDTabsDettaglioRepository
          .findOneActive(daInserire.getCodice());

      if (found.isPresent()) {
        var newRel = new CosmoRTabDettaglioTipoPratica();
        CosmoRTabDettaglioTipoPraticaPK id = new CosmoRTabDettaglioTipoPraticaPK();
        id.setCodiceTabDettaglio(found.get().getCodice());
        id.setCodiceTipoPratica(entity.getCodice());
        newRel.setId(id);
        newRel.setCosmoDTipoPratica(entity);
        newRel.setCosmoDTabDettaglio(found.get());
        newRel.setOrdine(daInserire.getOrdine());
        newRel.setDtInizioVal(now);

        var created = cosmoRTabDettaglioTipoPraticaRepository.save(newRel);

        if (entity.getCosmoRTabDettaglioTipoPraticas() == null) {
          entity.setCosmoRTabDettaglioTipoPraticas(new ArrayList<>());
        }
        entity.getCosmoRTabDettaglioTipoPraticas().add(created);

      }
    }).onElementsInBoth((existing, input) -> {

      existing.setOrdine(input.getOrdine());
      cosmoRTabDettaglioTipoPraticaRepository.save(existing);
    });
  }

  private void aggiornaTipiDocumentoPratica(CosmoDTipoPratica entity,
      List<AggiornaTipoPraticaDocumentoRequest> inputList, CosmoDTipoDocumento padre) {

    var now = Timestamp.from(Instant.now());


    List<CosmoRTipodocTipopratica> esistentiAttivi = entity.getCosmoRTipodocTipopraticas().stream()
        .filter(CosmoREntity::valido)
        .filter(c -> padre == null
        ? c.getCosmoDTipoDocumento().getCosmoRTipoDocumentoTipoDocumentosAllegato() == null || c
        .getCosmoDTipoDocumento().getCosmoRTipoDocumentoTipoDocumentosAllegato().isEmpty()
        : c.getCosmoDTipoDocumento().getCosmoRTipoDocumentoTipoDocumentosAllegato() != null
        && !c.getCosmoDTipoDocumento().getCosmoRTipoDocumentoTipoDocumentosAllegato()
        .isEmpty()
        && c.getCosmoDTipoDocumento().getCosmoRTipoDocumentoTipoDocumentosAllegato()
        .stream().anyMatch(
            t -> t.valido() && t.getId().getCodicePadre().equals(padre.getCodice())
            && t.getId().getCodiceTipoPratica().equals(entity.getCodice())))
        .collect(Collectors.toList());


    ComplexListComparator
    .compareLists(esistentiAttivi, inputList,
        (existing, input) -> existing.getCosmoDTipoDocumento() != null
        && existing.getCosmoDTipoDocumento().getCodice().equals(input.getCodice()))

    .onElementsInFirstNotInSecond(daEliminare -> {

      if(padre == null) {


        var allegatiDaEliminare =
            cosmoRTipoDocumentoTipoDocumentoRepository.findAll((root, cq, cb) ->
            cb.and(
                cb.isNull(root.get(CosmoREntity_.dtFineVal)),
                cb.equal(root.get(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoDocumentoPadre), daEliminare.getCosmoDTipoDocumento()),
                cb.equal(root.get(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoPratica),
                    daEliminare.getCosmoDTipoPratica())));
        allegatiDaEliminare.stream()
        .forEach(r -> {
          cosmoRTipoDocumentoTipoDocumentoRepository.deactivate(r);

        });

        if (allegatiDaEliminare != null && !allegatiDaEliminare.isEmpty()) {


          cosmoRTipoDocTipoPraticaRepository
          .findAll((root, cq, cb) -> cb.and(cb.isNull(root.get(CosmoREntity_.dtFineVal)),
              root.get(CosmoRTipodocTipopratica_.cosmoDTipoDocumento)
              .in(allegatiDaEliminare.stream()
                  .map(CosmoRTipoDocumentoTipoDocumento::getCosmoDTipoDocumentoAllegato)
                  .collect(Collectors.toList())),
              cb.equal(root.get(CosmoRTipodocTipopratica_.cosmoDTipoPratica),
                  daEliminare.getCosmoDTipoPratica())))
          .forEach(r -> {
                    if (cosmoRTipoDocumentoTipoDocumentoRepository.findAll((root, cq, cb) -> cb.and(
                        cb.isNull(root.get(CosmoREntity_.dtFineVal)),
                        cb.equal(
                            root.get(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoDocumentoAllegato),
                            r.getCosmoDTipoDocumento()),
                        cb.equal(root.get(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoPratica),
                            r.getCosmoDTipoPratica())))
                        .isEmpty()) {
                      cosmoRTipoDocTipoPraticaRepository.deactivate(r);
                    }
          });
        }



      } else {

        cosmoRTipoDocumentoTipoDocumentoRepository.findAll((root,  cq, cb) ->
        cb.and(
            cb.isNull(root.get(CosmoREntity_.dtFineVal)),
            cb.equal(root.get(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoDocumentoPadre), padre),
            cb.equal(
                root.get(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoDocumentoAllegato)
                .get(CosmoDTipoDocumento_.codice),
                daEliminare.getId().getCodiceTipoDocumento()),
            cb.equal(root.get(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoPratica),
                daEliminare.getCosmoDTipoPratica())))
                .forEach(r -> cosmoRTipoDocumentoTipoDocumentoRepository.deactivate(r));

      }

      if (padre == null || cosmoRTipoDocumentoTipoDocumentoRepository
          .findAll((root, cq, cb) -> cb.and(cb.isNull(root.get(CosmoREntity_.dtFineVal)),
              cb.equal(root.get(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoDocumentoAllegato),
                  daEliminare.getCosmoDTipoDocumento()),
              cb.equal(root.get(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoPratica),
                  daEliminare.getCosmoDTipoPratica())))
          .isEmpty()) {
        cosmoRTipoDocTipoPraticaRepository.deactivate(daEliminare);
        eliminaRelazioniTipoDocFormatoFile(daEliminare.getCosmoDTipoDocumento());

        if (cosmoRTipoDocTipoPraticaRepository
            .findActiveByField(CosmoRTipodocTipopratica_.cosmoDTipoDocumento,
                daEliminare.getCosmoDTipoDocumento())
            .isEmpty()) {
          cosmoTipoDocumentoRepository.deactivate(daEliminare.getCosmoDTipoDocumento());
        }
        entity.getCosmoRTipodocTipopraticas().remove(daEliminare);
      }

    }).onElementsInSecondNotInFirst(daInserire -> {

      var docDaAssociare = getOrCreate(daInserire, padre, entity.getCodice());
      var newRel = new CosmoRTipodocTipopratica();
      newRel.setCosmoDTipoDocumento(docDaAssociare);
      newRel.setCosmoDTipoPratica(entity);
      newRel.setDtInizioVal(now);
      CosmoRTipodocTipopraticaPK id = new CosmoRTipodocTipopraticaPK();
      id.setCodiceTipoDocumento(docDaAssociare.getCodice());
      id.setCodiceTipoPratica(entity.getCodice());
      newRel.setId(id);

      var created = cosmoRTipoDocTipoPraticaRepository.save(newRel);
      entity.getCosmoRTipodocTipopraticas().add(created);
      aggiornaTipiDocumentoFormatoFile(docDaAssociare, daInserire.getFormatiFile());

      // update children collections
      aggiornaTipiDocumentoPratica(entity, daInserire.getAllegati(), docDaAssociare);

    }).onElementsInBoth((existing, input) -> {

      var esistente = existing.getCosmoDTipoDocumento();
      esistente.setDescrizione(input.getDescrizione());

      if (padre == null) {
        esistente.setCodiceStardas(input.getCodiceStardas());
      } else {
        cosmoRTipoDocumentoTipoDocumentoRepository.findAllActive((root, cq, cb) ->
        cb.and(
            cb.equal(root.get(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoDocumentoPadre),padre),
            cb.equal(root.get(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoDocumentoAllegato),esistente),
            cb.equal(root.get(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoPratica), entity)))
        .forEach(r -> {
          r.setCodiceStardasAllegato(input.getCodiceStardas());
          r = cosmoRTipoDocumentoTipoDocumentoRepository.save(r);
        });
      }

      esistente.setFirmabile(input.isFirmabile());

      if (input.getDimensioneMassima() != null && input.getDimensioneMassima() > 0) {
        esistente.setDimensioneMassima(input.getDimensioneMassima());
      }
      cosmoTipoDocumentoRepository.save(esistente);
      aggiornaTipiDocumentoFormatoFile(esistente, input.getFormatiFile());
      // update children collections
      aggiornaTipiDocumentoPratica(entity, input.getAllegati(), esistente);

    });
  }

  private CosmoDTipoDocumento getOrCreate(AggiornaTipoPraticaDocumentoRequest daInserire,
      CosmoDTipoDocumento padre, String codiceTipoPratica) {
    ValidationUtils.require(daInserire, "request");
    ValidationUtils.validaAnnotations(daInserire);

    Optional<CosmoDTipoDocumento> found =
        cosmoTipoDocumentoRepository.findOneActive(daInserire.getCodice());

    var saved = new CosmoDTipoDocumento();

    if (!found.isPresent()) {
      // missing, create and return
      CosmoDTipoDocumento created = new CosmoDTipoDocumento();
      created.setCodice(daInserire.getCodice());
      created.setDescrizione(daInserire.getDescrizione());
      if (padre == null) {
        created.setCodiceStardas(daInserire.getCodiceStardas());
      }
      created.setFirmabile(daInserire.isFirmabile());
      if (daInserire.getDimensioneMassima() != null && daInserire.getDimensioneMassima() > 0) {
        created.setDimensioneMassima(daInserire.getDimensioneMassima());
      }
      created.setDtInizioVal(Timestamp.from(Instant.now()));
      created.setCosmoRTipodocTipopraticas(new ArrayList<>());
      created.setCosmoTDocumentos(new ArrayList<>());
      created.setCosmoRFormatoFileTipoDocumentos(new ArrayList<>());

      logger.info("getOrCreate", "registro nuovo tipo documento {}", created.getCodice());

      saved = cosmoTipoDocumentoRepository.save(created);
    } else {
      saved = found.get();
    }

    if (padre != null) {

      CosmoRTipoDocumentoTipoDocumentoPK pkExisting = new CosmoRTipoDocumentoTipoDocumentoPK();
      pkExisting.setCodicePadre(padre.getCodice());
      pkExisting.setCodiceAllegato(saved.getCodice());
      pkExisting.setCodiceTipoPratica(codiceTipoPratica);

      var esiste = cosmoRTipoDocumentoTipoDocumentoRepository.findOne(pkExisting);

      if (esiste == null) {
        esiste = new CosmoRTipoDocumentoTipoDocumento();
        CosmoRTipoDocumentoTipoDocumentoPK pk = new CosmoRTipoDocumentoTipoDocumentoPK();
        pk.setCodicePadre(padre.getCodice());
        pk.setCodiceAllegato(daInserire.getCodice());
        pk.setCodiceTipoPratica(codiceTipoPratica);

        esiste.setId(pk);
        esiste.setCosmoDTipoDocumentoPadre(padre);
        esiste.setCosmoDTipoDocumentoAllegato(saved);
        esiste.setCodiceStardasAllegato(daInserire.getCodiceStardas());
        esiste.setDtInizioVal(Timestamp.from(Instant.now()));

        esiste = cosmoRTipoDocumentoTipoDocumentoRepository.save(esiste);

        if (saved.getCosmoRTipoDocumentoTipoDocumentosAllegato() != null
            && !saved.getCosmoRTipoDocumentoTipoDocumentosAllegato().isEmpty()) {
          saved.getCosmoRTipoDocumentoTipoDocumentosAllegato().add(esiste);
        } else {
          saved
          .setCosmoRTipoDocumentoTipoDocumentosAllegato(new ArrayList<>(Arrays.asList(esiste)));
        }


      } else {
        if (esiste.nonValido()) {
          esiste.setDtFineVal(null);
        }
        esiste.setCodiceStardasAllegato(daInserire.getCodiceStardas());

        var temp = cosmoRTipoDocumentoTipoDocumentoRepository.save(esiste);

        if (saved.getCosmoRTipoDocumentoTipoDocumentosAllegato() == null) {
        } else if (saved.getCosmoRTipoDocumentoTipoDocumentosAllegato().isEmpty()) {
          saved.getCosmoRTipoDocumentoTipoDocumentosAllegato().add(esiste);
        } else {
          saved.getCosmoRTipoDocumentoTipoDocumentosAllegato()
          .forEach(allegato -> {

            if (allegato.getCosmoDTipoDocumentoAllegato().getCodice()
                .equals(temp.getCosmoDTipoDocumentoAllegato().getCodice())
                && allegato.getId().getCodiceTipoPratica()
                .equals(temp.getId().getCodiceTipoPratica())
                && allegato.getCosmoDTipoDocumentoPadre().getCodice()
                .equals(temp.getCosmoDTipoDocumentoPadre().getCodice())) {

              allegato.setDtFineVal(temp.getDtFineVal());
              allegato.setCodiceStardasAllegato(temp.getCodiceStardasAllegato());

            }
          });
        }
      }
    }
    return saved;
  }

  private void aggiornaTrasformazioniPratica(CosmoDTipoPratica entity,
      List<AggiornaTipoPraticaTrasformazioneDatiRequest> inputList) {

    var now = Timestamp.from(Instant.now());

    List<CosmoDTrasformazioneDatiPratica> esistentiAttivi = entity.getTrasformazioni().stream()
        .filter(CosmoDEntity::valido).collect(Collectors.toList());

    ComplexListComparator
    .compareLists(esistentiAttivi, inputList, (existing, input) -> existing.getId().equals(input.getId()))

    .onElementsInFirstNotInSecond(
        daEliminare -> cosmoDTrasformazioneDatiPraticaRepository.deactivate(daEliminare)

        ).onElementsInSecondNotInFirst(daInserire -> {

          var entityDaInserire = new CosmoDTrasformazioneDatiPratica();
          entityDaInserire.setDtInizioVal(now);
          entityDaInserire.setCodiceFase(daInserire.getCodiceFase());
          entityDaInserire.setDefinizione(daInserire.getDefinizione());
          entityDaInserire.setDescrizione(daInserire.getDescrizione());
          entityDaInserire.setObbligatoria(daInserire.isObbligatoria());
          entityDaInserire.setOrdine(daInserire.getOrdine());
          entityDaInserire.setCodiceTipoPratica(entity.getCodice());
          entityDaInserire.setTipoPratica(entity);
          entity.getTrasformazioni().add(cosmoDTrasformazioneDatiPraticaRepository.save(entityDaInserire));

        }).onElementsInBoth((existing, input) -> {

          existing.setCodiceFase(input.getCodiceFase());
          existing.setDefinizione(input.getDefinizione());
          existing.setDescrizione(input.getDescrizione());
          existing.setObbligatoria(input.isObbligatoria());
          existing.setOrdine(input.getOrdine());
          cosmoDTrasformazioneDatiPraticaRepository.save(existing);
        });
  }

  private void checkConflict(SingularAttribute<CosmoDTipoPratica, String> field, String value, Long excludeId) {
    findByFieldEqualsIgnoreCase(field, value, excludeId).ifPresent(other -> {
      throw new ConflictException("Campo \"" + field.getName() + "\" gia' assegnato ad altro fruitore");
    });
  }

  private Optional<CosmoDTipoPratica> findByFieldEqualsIgnoreCase(SingularAttribute<CosmoDTipoPratica, String> field,
      String value, Long excludeId) {
    return cosmoTipoPraticaRepository.findAllActive((root, query, cb) -> {
      var condition = cb.equal(cb.upper(root.get(field)), value != null ? value.toUpperCase() : null);
      if (excludeId != null) {
        condition = cb.and(condition, cb.notEqual(root.get(CosmoDTipoPratica_.codice), excludeId));
      }
      return condition;
    }).stream().findAny();
  }

  private String clean(String raw) {
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    return raw.strip();
  }

  private void aggiornaCustomForm(String codice, CosmoDTipoPratica tipoPratica) {

    if (tipoPratica.getCosmoDCustomFormFormios() != null) {
      tipoPratica.getCosmoDCustomFormFormios().forEach(customForm -> {

        if (codice == null || !codice.equals(customForm.getCodice())) {
          var customFormDB = cosmoDCustomFormFormioRepository.findOne(customForm.getCodice());
          if (customFormDB != null) {
            customFormDB.setCosmoDTipoPratica(null);
            cosmoDCustomFormFormioRepository.save(customFormDB);
          }
        }
      });
    }

    if (codice != null && !codice.isBlank()) {
      var customForm = cosmoDCustomFormFormioRepository.findOneActive(codice).orElse(null);
      if (customForm != null) {
        customForm.setCosmoDTipoPratica(tipoPratica);
        cosmoDCustomFormFormioRepository.save(customForm);
      }
    }
  }

  private void aggiornaGruppoCreatorePratica(CosmoDTipoPratica tipoPratica, Long idGruppoCreatore) {

    CosmoRGruppoTipoPratica aggiornamento;

    if (!CollectionUtils.isEmpty(tipoPratica.getCosmoRGruppoTipoPraticas())) {

      Optional<CosmoRGruppoTipoPratica> esisteGruppoCreatore =
          tipoPratica.getCosmoRGruppoTipoPraticas().stream()
          .filter(r -> r.valido() && Boolean.TRUE.equals(r.getCreatore())).findFirst();

      if (esisteGruppoCreatore.isPresent() && !esisteGruppoCreatore.get().getCosmoTGruppo().getId().equals(idGruppoCreatore)) {
        aggiornamento = esisteGruppoCreatore.get();
        aggiornamento.setCreatore(false);
        cosmoRGruppoTipoPraticaRepository.save(aggiornamento);
      }

      // se idGruppoCreatore non e' valorizzato ed esiste gia' un gruppo creatore
      // imposto il flag del gruppo creatore esistente a false
      if (idGruppoCreatore == null) {

        if (esisteGruppoCreatore.isPresent()) {
          aggiornamento = esisteGruppoCreatore.get();
          aggiornamento.setCreatore(false);
          cosmoRGruppoTipoPraticaRepository.save(aggiornamento);
        }

      } else {

        Optional<CosmoRGruppoTipoPratica> esisteIdGruppoCreatore =
            tipoPratica.getCosmoRGruppoTipoPraticas().stream()
            .filter(rel -> rel.getCosmoTGruppo().getId().equals(idGruppoCreatore) && rel.valido())
            .findFirst();

        if (esisteIdGruppoCreatore.isPresent()) {

          aggiornamento = esisteIdGruppoCreatore.get();
          aggiornamento.setCreatore(tipoPratica.getCreabileDaInterfaccia());
          cosmoRGruppoTipoPraticaRepository.save(aggiornamento);

        } else {

          creaRelazioneGruppoTipoPratica(tipoPratica, idGruppoCreatore);
        }
      }
    } else {

      creaRelazioneGruppoTipoPratica(tipoPratica, idGruppoCreatore);

    }
  }

  private void creaRelazioneGruppoTipoPratica(CosmoDTipoPratica tipoPratica, Long idGruppoCreatore) {
    var now = Timestamp.from(Instant.now());
    var gruppo = cosmoTGruppoRepository.findOneNotDeleted(idGruppoCreatore).orElseThrow(NotFoundException::new);
    CosmoRGruppoTipoPratica salvataggio = new CosmoRGruppoTipoPratica();
    CosmoRGruppoTipoPraticaPK salvataggioPK = new CosmoRGruppoTipoPraticaPK();
    salvataggioPK.setCodiceTipoPratica(tipoPratica.getCodice());
    salvataggioPK.setIdGruppo(idGruppoCreatore);
    salvataggio.setCosmoDTipoPratica(tipoPratica);
    salvataggio.setCosmoTGruppo(gruppo);
    // la creazione da interfaccia pilota il valore del flag creatore
    salvataggio.setCreatore((tipoPratica.getCreabileDaInterfaccia()));
    salvataggio.setId(salvataggioPK);
    salvataggio.setDtInizioVal(now);
    salvataggio.setSupervisore(false);
    cosmoRGruppoTipoPraticaRepository.save(salvataggio);
  }

  private <T> Collection<T> safe(Collection<T> source) {
    return source == null ? Collections.emptyList() : source;
  }


  private void aggiornaGruppoSupervisorePratica(CosmoDTipoPratica tipoPratica, Long idGruppoSupervisore) {

    CosmoRGruppoTipoPratica aggiornamento;

    if (!CollectionUtils.isEmpty(tipoPratica.getCosmoRGruppoTipoPraticas())) {

      Optional<CosmoRGruppoTipoPratica> esisteGruppoSupervisore =
          tipoPratica.getCosmoRGruppoTipoPraticas().stream()
          .filter(r -> r.valido() && Boolean.TRUE.equals(r.getSupervisore())).findFirst();

      if (esisteGruppoSupervisore.isPresent() && !esisteGruppoSupervisore.get().getCosmoTGruppo().getId().equals(idGruppoSupervisore)) {
        aggiornamento = esisteGruppoSupervisore.get();
        aggiornamento.setSupervisore(false);
        cosmoRGruppoTipoPraticaRepository.save(aggiornamento);
      }

      // se idGruppoSuperviosre non e' valorizzato ed esiste gia' un gruppo supervisore
      // imposto il flag del gruppo supervisore esistente a false
      if (idGruppoSupervisore == null) {

        if (esisteGruppoSupervisore.isPresent()) {
          aggiornamento = esisteGruppoSupervisore.get();
          aggiornamento.setSupervisore(false);
          cosmoRGruppoTipoPraticaRepository.save(aggiornamento);
        }

      } else {

        Optional<CosmoRGruppoTipoPratica> esisteIdGruppoSupervisore =
            tipoPratica.getCosmoRGruppoTipoPraticas().stream()
            .filter(rel -> rel.getCosmoTGruppo().getId().equals(idGruppoSupervisore) && rel.valido())
            .findFirst();

        if (esisteIdGruppoSupervisore.isPresent()) {

          aggiornamento = esisteIdGruppoSupervisore.get();
          aggiornamento.setSupervisore(true);
          cosmoRGruppoTipoPraticaRepository.save(aggiornamento);

        } else {

          creaRelSupGruppoTipoPratica(tipoPratica, idGruppoSupervisore);
        }
      }
    } else {

      creaRelSupGruppoTipoPratica(tipoPratica, idGruppoSupervisore);

    }
  }

  private void deleteGruppoSupervisorePratica(CosmoDTipoPratica tipoPratica, Long idGruppoSupervisore) {

    CosmoRGruppoTipoPratica aggiornamento;

    Optional<CosmoRGruppoTipoPratica> esisteGruppoSupervisore =
        tipoPratica.getCosmoRGruppoTipoPraticas().stream()
        .filter(r -> r.valido() && Boolean.TRUE.equals(r.getSupervisore())).findFirst();

    if (esisteGruppoSupervisore.isPresent() && !esisteGruppoSupervisore.get().getCosmoTGruppo().getId().equals(idGruppoSupervisore)) {
      aggiornamento = esisteGruppoSupervisore.get();
      aggiornamento.setSupervisore(false);
      cosmoRGruppoTipoPraticaRepository.save(aggiornamento);
    }

  }

  private void deleteGruppoCreatorePratica(CosmoDTipoPratica tipoPratica, Long idGruppoCreatore) {

    CosmoRGruppoTipoPratica aggiornamento;

    Optional<CosmoRGruppoTipoPratica> esisteGruppoCreatore =
        tipoPratica.getCosmoRGruppoTipoPraticas().stream()
        .filter(r -> r.valido() && Boolean.TRUE.equals(r.getCreatore())).findFirst();

    if (esisteGruppoCreatore.isPresent() && !esisteGruppoCreatore.get().getCosmoTGruppo().getId().equals(idGruppoCreatore)) {
      aggiornamento = esisteGruppoCreatore.get();
      aggiornamento.setCreatore(false);
      cosmoRGruppoTipoPraticaRepository.save(aggiornamento);
    }

  }

  private void creaRelSupGruppoTipoPratica(CosmoDTipoPratica tipoPratica, Long idGruppoSupervisore) {
    var now = Timestamp.from(Instant.now());
    var gruppo = cosmoTGruppoRepository.findOneNotDeleted(idGruppoSupervisore).orElseThrow(NotFoundException::new);
    CosmoRGruppoTipoPratica salvatag = new CosmoRGruppoTipoPratica();
    CosmoRGruppoTipoPraticaPK salvatagPK = new CosmoRGruppoTipoPraticaPK();
    salvatagPK.setCodiceTipoPratica(tipoPratica.getCodice());
    salvatagPK.setIdGruppo(idGruppoSupervisore);
    salvatag.setCosmoDTipoPratica(tipoPratica);
    salvatag.setCosmoTGruppo(gruppo);
    salvatag.setCreatore(salvatag.getCreatore());
    salvatag.setId(salvatagPK);
    salvatag.setDtInizioVal(now);
    salvatag.setSupervisore(true);
    cosmoRGruppoTipoPraticaRepository.save(salvatag);
  }



  private void eliminaRelazioniTipoDocFormatoFile(CosmoDTipoDocumento tipoDoc) {
    tipoDoc.getCosmoRFormatoFileTipoDocumentos().stream()
    .forEach(relff -> {
      if (relff.getCosmoDFormatoFile().getMimeType().equalsIgnoreCase("raggruppato")) {
        var formatiFileGropedByDesc =
            cosmoDFormatoFileRepository.findAllByDescrizioneContainingAndDtFineValIsNull(
                relff.getCosmoDFormatoFile().getDescrizione());
        formatiFileGropedByDesc.stream().forEach(ff -> {
          CosmoRFormatoFileTipoDocumentoPK pkRel = new CosmoRFormatoFileTipoDocumentoPK();
          pkRel.setCodiceFormatoFile(ff.getCodice());
          pkRel.setCodiceTipoDocumento(tipoDoc.getCodice());
          cosmoRFormatoFileTipoDocumentoRepository.deactivate(pkRel);
        });
      } else {
        CosmoRFormatoFileTipoDocumentoPK pkRel = new CosmoRFormatoFileTipoDocumentoPK();
        pkRel.setCodiceFormatoFile(relff.getCosmoDFormatoFile().getCodice());
        pkRel.setCodiceTipoDocumento(tipoDoc.getCodice());
        cosmoRFormatoFileTipoDocumentoRepository.deactivate(pkRel);
      }
    });
  }

  private void aggiornaTipiDocumentoFormatoFile(CosmoDTipoDocumento tipoDoc, List<FormatoFile> inputList) {

    var now = Timestamp.from(Instant.now());

    List<CosmoRFormatoFileTipoDocumento> esistentiAttivi = tipoDoc.getCosmoRFormatoFileTipoDocumentos().stream()
        .filter(CosmoREntity::valido).collect(Collectors.toList());

    List<FormatoFile> inputListExplose = new LinkedList<>();

    inputList.stream().forEach(input -> {
      if (input.getMimeType().equalsIgnoreCase("raggruppato")) {
        inputListExplose.addAll(cosmoDFormatoFileMapper.toDTOs(cosmoDFormatoFileRepository.findAllByDescrizioneContainingAndDtFineValIsNull(input.getDescrizione())));
      } else {
        inputListExplose.add(input);
      }
    });


    ComplexListComparator
    .compareLists(esistentiAttivi, inputListExplose,
        (existing, input) -> existing.getCosmoDFormatoFile() != null
        && existing.getCosmoDFormatoFile().getCodice().equals(input.getCodice()))

    .onElementsInFirstNotInSecond(daEliminare -> {
      cosmoRFormatoFileTipoDocumentoRepository.deactivate(daEliminare);
      tipoDoc.getCosmoRFormatoFileTipoDocumentos().remove(daEliminare);
    })
    .onElementsInSecondNotInFirst(daInserire -> {

      CosmoRFormatoFileTipoDocumento relazioneDaInserire = new CosmoRFormatoFileTipoDocumento();
      CosmoRFormatoFileTipoDocumentoPK pkRel = new CosmoRFormatoFileTipoDocumentoPK();
      pkRel.setCodiceFormatoFile(daInserire.getCodice());
      pkRel.setCodiceTipoDocumento(tipoDoc.getCodice());
      relazioneDaInserire.setId(pkRel);
      relazioneDaInserire.setCosmoDFormatoFile(cosmoDFormatoFileMapper.toRecord(daInserire));
      relazioneDaInserire.setCosmoDTipoDocumento(tipoDoc);
      relazioneDaInserire.setDtInizioVal(now);
      cosmoRFormatoFileTipoDocumentoRepository.save(relazioneDaInserire);

    });

  }


}

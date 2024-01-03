/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.service.impl;

import static it.csi.cosmo.cosmopratiche.config.ErrorMessages.TIPO_PRATICA_NON_CREABILE_DA_SERVIZIO;
import static it.csi.cosmo.cosmopratiche.config.ErrorMessages.TIPO_PRATICA_NON_TROVATA;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoDTipoPratica_;
import it.csi.cosmo.common.entities.CosmoDTipoRelazionePratica;
import it.csi.cosmo.common.entities.CosmoDTipoTag_;
import it.csi.cosmo.common.entities.CosmoLStoricoPratica;
import it.csi.cosmo.common.entities.CosmoRFruitoreEnte;
import it.csi.cosmo.common.entities.CosmoRPraticaPratica;
import it.csi.cosmo.common.entities.CosmoRPraticaPraticaPK;
import it.csi.cosmo.common.entities.CosmoRPraticaTag;
import it.csi.cosmo.common.entities.CosmoRPraticaTagPK;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.entities.CosmoTFruitore_;
import it.csi.cosmo.common.entities.CosmoTGruppo;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTTag;
import it.csi.cosmo.common.entities.CosmoTTag_;
import it.csi.cosmo.common.entities.CosmoTTemplateFea;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.CosmoTUtente_;
import it.csi.cosmo.common.entities.enums.TipoEventoStoricoPratica;
import it.csi.cosmo.common.entities.proto.IntervalloValiditaEntity;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.UnauthorizedException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.EnteDTO;
import it.csi.cosmo.common.security.model.GruppoDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmopratiche.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmopratiche.business.service.FruitoriService;
import it.csi.cosmo.cosmopratiche.business.service.StoricoPraticaService;
import it.csi.cosmo.cosmopratiche.config.ErrorMessages;
import it.csi.cosmo.cosmopratiche.config.ParametriApplicativo;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaRelazionePraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaRelazionePraticaResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaRelazionePraticaResponseRelazioniPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaPraticaFruitoreRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaPraticaFruitoreResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.Esito;
import it.csi.cosmo.cosmopratiche.dto.rest.FiltroRicercaPraticheFruitoriDTO;
import it.csi.cosmo.cosmopratiche.dto.rest.PageInfo;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheFruitore;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheFruitoreResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.RelazionePratica;
import it.csi.cosmo.cosmopratiche.dto.rest.TagRidotto;
import it.csi.cosmo.cosmopratiche.dto.rest.TagRidottoResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.TemplateFirmaFea;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoRPraticaTagsMapper;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoTPraticheMapper;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDTipoDocumentoRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoDTipoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRPraticaPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRPraticaTagRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTFruitoreRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTTagRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTTemplateFeaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTUtenteRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTipoRelazionePraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.specifications.CosmoTPraticaSpecifications;
import it.csi.cosmo.cosmopratiche.security.SecurityUtils;
import it.csi.cosmo.cosmopratiche.util.logger.LogCategory;
import it.csi.cosmo.cosmopratiche.util.logger.LoggerFactory;

@Service
@Transactional
public class FruitoriServiceImpl implements FruitoriService {

  private static final CosmoLogger logger = LoggerFactory
      .getLogger(LogCategory.COSMOPRATICHE_BUSINESS_LOG_CATEGORY, "FruitoriServiceImpl");

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CosmoDTipoPraticaRepository cosmoTipoPraticaRepository;

  @Autowired
  private CosmoDTipoDocumentoRepository cosmoTipoDocumentoRepository;

  @Autowired
  private CosmoTFruitoreRepository cosmoTFruitoreRepository;

  @Autowired
  private CosmoTTagRepository cosmoTTagRepository;

  @Autowired
  private CosmoTTemplateFeaRepository cosmoTTemplateFeaRepository;

  @Autowired
  private CosmoRPraticaTagRepository cosmoRPraticaTagRepository;

  @Autowired
  private StoricoPraticaService storicoPraticaService;

  @Autowired
  private CosmoTipoRelazionePraticaRepository tipoRelazionePraticaRepository;

  @Autowired
  private CosmoRPraticaPraticaRepository relazionePraticaPraticaRepository;

  @Autowired
  private CosmoRPraticaTagsMapper cosmoRPraticaTagsMapper;

  @Autowired
  private CosmoTPraticheMapper praticaMapper;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoTUtenteRepository cosmoTUtenteRepository;

  @Autowired
  private CosmoTEnteRepository cosmoTEnteRepository;

  @Override
  public CreaPraticaFruitoreResponse postPratiche(CreaPraticaFruitoreRequest body) {

    Timestamp adesso = new Timestamp(System.currentTimeMillis());

    // Validazione campi obbligatori
    ValidationUtils.require(body, "request body");
    ValidationUtils.validaAnnotations(body);

    // ottengo la reference al fruitore
    CosmoTFruitore fruitoreEntity = getFruitoreCorrente();

    // valido il codice ipa ente verificando contro le associazioni del fruitore
    CosmoTEnte enteAssociato = verificaEnteAssociato(fruitoreEntity, body.getCodiceIpaEnte());

    // verifico che non collida la chiave composta idPratica - codiceIpaEnte
    if (this
        .findPraticaByChiaveEsterna(body.getIdPratica(), body.getCodiceIpaEnte(), fruitoreEntity.getId())
        .isPresent()) {
      throw new ConflictException("Esiste gia' una pratica con ID " + body.getIdPratica()
      + " e codice ente " + body.getCodiceIpaEnte());
    }

    // verifico il codice tipo pratica e ottengo la reference
    CosmoDTipoPratica tipo = cosmoTipoPraticaRepository.findOneActive(body.getCodiceTipologia())
        .orElseThrow(() -> new BadRequestException(
            String.format(TIPO_PRATICA_NON_TROVATA, body.getCodiceTipologia())));

    // verifico che il tipo pratica sia creabile da servizio
    if (Boolean.FALSE.equals(tipo.getCreabileDaServizio())) {
      throw new BadRequestException(
          String.format(TIPO_PRATICA_NON_CREABILE_DA_SERVIZIO, body.getCodiceTipologia()));
    }

    // verifico che il tipo pratica sia associato all'ente
    if (tipo.getCosmoTEnte() != null
        && !enteAssociato.getId().equals(tipo.getCosmoTEnte().getId())) {
      throw new BadRequestException("Il tipo di pratica non e' disponibile per l'ente");
    }

    List<CosmoTTag> tagsOnDB = new LinkedList<>();
    if(body.getTags() != null && !body.getTags().isEmpty()) {

      List<String> codiciTag = body.getTags().stream().map(TagRidotto::getCodice)
          .filter(Objects::nonNull).collect(Collectors.toList());

      List<String> codiciTipiTag = body.getTags().stream().map(tag -> tag.getTipoTag().getCodice())
          .filter(Objects::nonNull).collect(Collectors.toList());

      if(codiciTag.isEmpty()) {
        throw new BadRequestException("I codici dei tag non sono valorizzati");
      }

      if(codiciTipiTag.isEmpty()) {
        throw new BadRequestException("Le tipologie dei tag non sono valorizzate");
      }

      tagsOnDB = cosmoTTagRepository.findAllNotDeleted((root, q, cb) -> cb.and(
          cb.equal(root.get(CosmoTTag_.cosmoTEnte), enteAssociato),
          root.get(CosmoTTag_.codice).in(codiciTag),
          root.get(CosmoTTag_.cosmoDTipoTag).get(CosmoDTipoTag_.codice).in(codiciTipiTag)));

      if (tagsOnDB.isEmpty()) {
        throw new BadRequestException("Tags non trovati");
      }
    }

    CosmoTPratica pratica = new CosmoTPratica();

    pratica.setIdPraticaExt(body.getIdPratica());
    pratica
    .setEnte(cosmoTPraticaRepository.reference(CosmoTEnte.class, enteAssociato.getId()));
    pratica.setFruitore(
        cosmoTPraticaRepository.reference(CosmoTFruitore.class, fruitoreEntity.getId()));
    pratica.setOggetto(body.getOggetto());
    pratica.setRiassunto(body.getRiassunto());
    pratica.setUtenteCreazionePratica(body.getUtenteCreazionePratica());
    pratica.setTipo(tipo);
    pratica.setDataCreazionePratica(adesso);

    if (body.getMetadati() != null && !body.getMetadati().isBlank()) {
      pratica.setMetadati(body.getMetadati());
    }

    if (!StringUtils.isBlank(body.getRiassunto())) {
      // tenta indicizzazione del campo riassunto come best-attempt
      try {
        pratica.setRiassuntoTestuale(Jsoup.parse(body.getRiassunto()).text());
      } catch (Exception e) {
        logger.error("postPractices",
            "errore nell'indicizzazione del campo riassunto a versione testuale", e);
      }
    }

    pratica = cosmoTPraticaRepository.save(pratica);

    if (!tagsOnDB.isEmpty()) {

      List<CosmoRPraticaTag> praticaTags = new LinkedList<>();
      for (var tag : tagsOnDB) {
        CosmoRPraticaTag praticaTag = new CosmoRPraticaTag();
        CosmoRPraticaTagPK id = new CosmoRPraticaTagPK();
        id.setIdPratica(pratica.getId());
        id.setIdTag(tag.getId());
        praticaTag.setId(id);
        praticaTag.setCosmoTPratica(pratica);
        praticaTag.setCosmoTTag(tag);
        praticaTag.setDtInizioVal(Timestamp.valueOf(LocalDateTime.now()));
        praticaTags.add(praticaTag);

      }
      praticaTags = cosmoRPraticaTagRepository.save(praticaTags);

      pratica.setCosmoRPraticaTags(praticaTags);
    }
    List<CosmoTTemplateFea> templates = new LinkedList<>();

    if (body.getTemplateFirmaFea() != null && !body.getTemplateFirmaFea().isEmpty()) {
      var tipiDoc = cosmoTipoDocumentoRepository.findAllActive((root, q,
          cb) -> cb.and(root.get(CosmoDTipoDocumento_.codice)
              .in(body.getTemplateFirmaFea().stream().map(elem -> elem.getCodiceTipoDocumento())
                  .filter(Objects::nonNull).collect(Collectors.toList()))));


      for (var tipoDoc : tipiDoc) {

        var doc = body.getTemplateFirmaFea().stream()
            .filter(elem -> elem.getCodiceTipoDocumento().equals(tipoDoc.getCodice())).findFirst()
            .orElse(null);

        if (doc != null && doc.getCoordinataX() != null && doc.getCoordinataX() >= 0
            && doc.getCoordinataY() != null && doc.getCoordinataY() >= 0 && doc.getPagina() != null
            && doc.getPagina() > 0) {
          var template = new CosmoTTemplateFea();
          template.setCoordinataX(doc.getCoordinataX());
          template.setCoordinataY(doc.getCoordinataY());
          template.setDescrizione(doc.getDescrizione());
          template.setPagina(doc.getPagina());
          template.setCosmoTPratica(pratica);
          template.setEnte(enteAssociato);
          template.setTipologiaPratica(tipo);
          template.setTipologiaDocumento(tipoDoc);
          template.setCaricatoDaTemplate(Boolean.FALSE);
          templates.add(template);
        }
      }
      templates = this.cosmoTTemplateFeaRepository.save(templates);
    }

    // inserisco log dell'operazione su db
    //@formatter:off
    storicoPraticaService.logEvent(CosmoLStoricoPratica.builder()
        .withCodiceTipoEvento(TipoEventoStoricoPratica.PRATICA_CREATA)
        .withDescrizioneEvento(String.format("La pratica '%s' e' stata creata.", pratica.getOggetto()))
        .withPratica(pratica)
        .build());
    //@formatter:on

    CreaPraticaFruitoreResponse output = new CreaPraticaFruitoreResponse();
    output.setCodiceIpaEnte(pratica.getEnte().getCodiceIpa());
    output.setCodiceTipologia(pratica.getTipo().getCodice());
    output.setIdPratica(pratica.getIdPraticaExt());
    output.setOggetto(pratica.getOggetto());
    output.setRiassunto(pratica.getRiassunto());
    output.setUtenteCreazionePratica(pratica.getUtenteCreazionePratica());

    output.setTags(setTagsResponse(tagsOnDB, body.getTags(), body.getCodiceIpaEnte()));
    if (templates != null && !templates.isEmpty()) {
      output.setTemplateFirmaFea(templates.stream().map(elem -> {
        var templateDTO = new TemplateFirmaFea();
        templateDTO.setDescrizione(elem.getDescrizione());
        templateDTO.setCoordinataX(elem.getCoordinataX());
        templateDTO.setCoordinataY(elem.getCoordinataY());
        templateDTO.setPagina(elem.getPagina());
        templateDTO.setCodiceTipoDocumento(elem.getTipologiaDocumento().getCodice());

        return templateDTO;
      }).collect(Collectors.toList()));

    }

    return output;
  }

  @Override
  public AggiornaRelazionePraticaResponse creaAggiornaPraticheInRelazione(String idPraticaExt,
      AggiornaRelazionePraticaRequest body) {
    String methodName = "creaAggiornaPraticheInRelazione";

    ValidationUtils.require(idPraticaExt, "idPraticaExt");
    ValidationUtils.require(body, "body");

    // ottengo la reference al fruitore
    CosmoTFruitore fruitoreEntity = getFruitoreCorrente();

    // valido il codice ipa ente verificando contro le associazioni del fruitore
    verificaEnteAssociato(fruitoreEntity, body.getCodiceIpaEnte());

    CosmoTPratica praticaDa = findPraticaByChiaveEsterna(idPraticaExt,
        body.getCodiceIpaEnte(), fruitoreEntity.getId()).orElse(null);

    if (praticaDa == null) {
      final String message = String.format(ErrorMessages.PRATICA_CON_ID_EXT_NON_TROVATA, idPraticaExt);
      logger.error(methodName, message);
      throw new BadRequestException(message);
    }

    AggiornaRelazionePraticaResponse response = new AggiornaRelazionePraticaResponse();

    response.setIdPraticaExtDa(idPraticaExt);
    response.setRelazioniPratica(new ArrayList<>());

    List<RelazionePratica> relazioni = body.getRelazioniPratica();

    if (relazioni == null || relazioni.isEmpty()) {
      final String message = "Nessuna pratica da associare a " + idPraticaExt;
      logger.error(methodName, message);
      throw new BadRequestException(message);
    }

    for (RelazionePratica relazione : relazioni) {

      try {

        CosmoTPratica praticaA = findPraticaByChiaveEsterna(relazione.getIdPraticaExtA(),
            body.getCodiceIpaEnte(), fruitoreEntity.getId()).orElse(null);

        if (praticaA == null) {
          final String message = String.format(ErrorMessages.PRATICA_CON_ID_EXT_NON_TROVATA,
              relazione.getIdPraticaExtA());
          logger.error(methodName, message);
          throw new BadRequestException(message);
        }

        CosmoDTipoRelazionePratica tipoRelazionePratica =
            tipoRelazionePraticaRepository.findOneActive(relazione.getTipoRelazione())
            .orElseThrow(() -> {
              final String message = String.format(
                  ErrorMessages.TIPO_RELAZIONE_PRATICA_CON_ID_NON_TROVATA,
                  relazione.getTipoRelazione());
              logger.error(methodName, message);
              throw new BadRequestException(message);
            });

        CosmoRPraticaPratica relazionePraticaPratica = relazionePraticaPraticaRepository
            .findOneByCosmoTPraticaDaIdAndCosmoTPraticaAIdAndCosmoDTipoRelazionePraticaCodice(
                praticaDa.getId(), praticaA.getId(), tipoRelazionePratica.getCodice());

        if (relazionePraticaPratica == null) {

          CosmoRPraticaPraticaPK relazionePraticaPraticaPK = new CosmoRPraticaPraticaPK();
          relazionePraticaPraticaPK.setIdPraticaDa(praticaDa.getId());
          relazionePraticaPraticaPK.setIdPraticaA(praticaA.getId());
          relazionePraticaPraticaPK.setCodiceTipoRelazione(tipoRelazionePratica.getCodice());

          relazionePraticaPratica = new CosmoRPraticaPratica();
          relazionePraticaPratica.setId(relazionePraticaPraticaPK);
          relazionePraticaPratica.setCosmoTPraticaDa(praticaDa);
          relazionePraticaPratica.setCosmoTPraticaA(praticaA);
          relazionePraticaPratica.setCosmoDTipoRelazionePratica(tipoRelazionePratica);
          relazionePraticaPratica.setDtInizioVal(
              relazione.getDtInizioValidita() == null ? Timestamp.valueOf(LocalDateTime.now())
                  : Timestamp.valueOf(relazione.getDtInizioValidita()
                      .atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()));
          relazionePraticaPratica.setDtFineVal(relazione.getDtFineValidita() == null ? null
              : Timestamp.valueOf(relazione.getDtFineValidita().atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()));
        } else {

          if (relazionePraticaPratica.getDtFineVal() == null) {
            if (relazione.getDtFineValidita() != null) {
              relazionePraticaPratica.setDtFineVal(relazione.getDtFineValidita() == null ? null
                  : Timestamp.valueOf(relazione.getDtFineValidita()
                      .atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()));
              if (relazione.getDtInizioValidita() != null) {
                relazionePraticaPratica
                .setDtInizioVal(Timestamp.valueOf(relazione.getDtInizioValidita()
                    .atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()));
              }
            } else {
              final String message =
                  "Pratica " + relazione.getIdPraticaExtA() + " gia' associata a "
                      + idPraticaExt + " con relazione " + relazione.getTipoRelazione();
              logger.error(methodName, message);
              throw new BadRequestException(message);
            }
          } else {
            relazionePraticaPratica.setDtInizioVal(
                relazione.getDtInizioValidita() == null ? Timestamp.valueOf(LocalDateTime.now())
                    : Timestamp.valueOf(relazione.getDtInizioValidita()
                        .atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()));
            relazionePraticaPratica.setDtFineVal(relazione.getDtFineValidita() == null ? null
                : Timestamp.valueOf(relazione.getDtFineValidita()
                    .atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()));
          }
        }

        relazionePraticaPraticaRepository.save(relazionePraticaPratica);

        AggiornaRelazionePraticaResponseRelazioniPratica output =
            new AggiornaRelazionePraticaResponseRelazioniPratica();
        output.setRelazionePratica(relazione);
        var esitoCorretto = new Esito();
        esitoCorretto.setCode("OK");
        esitoCorretto.setStatus(200);
        esitoCorretto.setTitle("Aggiornamento avvenuto correttamente");
        output.setEsito(esitoCorretto);
        response.getRelazioniPratica().add(output);

      } catch (Exception e) {

        AggiornaRelazionePraticaResponseRelazioniPratica output =
            new AggiornaRelazionePraticaResponseRelazioniPratica();
        output.setRelazionePratica(relazione);
        var esitoErrato = new Esito();
        esitoErrato.setCode("ERRORE");
        esitoErrato.setStatus(400);
        esitoErrato.setTitle(e.getMessage());
        output.setEsito(esitoErrato);
        response.getRelazioniPratica().add(output);

      }
    }

    return response;
  }

  // HELPERS

  private CosmoTFruitore getFruitoreCorrente() {
    // ottengo il fruitore corrente
    var fruitore = SecurityUtils.getClientCorrente();

    // ottengo la reference al fruitore
    return cosmoTFruitoreRepository
        .findOneNotDeletedByField(CosmoTFruitore_.apiManagerId, fruitore.getCodice()).orElseThrow(
            () -> new UnauthorizedException("Fruitore non autenticato o non riconosciuto"));
  }

  private CosmoTEnte verificaEnteAssociato(CosmoTFruitore fruitoreEntity, String codiceIpaEnte) {
    //@formatter:off
    return fruitoreEntity.getCosmoRFruitoreEntes().stream()
        .filter(IntervalloValiditaEntity::valido)
        .map(CosmoRFruitoreEnte::getCosmoTEnte)
        .filter(ente -> !ente.cancellato())
        .filter(ente -> codiceIpaEnte.equalsIgnoreCase(ente.getCodiceIpa()))
        .findFirst()
        .orElseThrow(() -> new BadRequestException("Il Codice IPA ente non coincide con nessuno degli enti associati al fruitore"));
    //@formatter:on
  }

  private Optional<CosmoTPratica> findPraticaByChiaveEsterna(String idPraticaExt, String codiceIpaEnte,
      Long idFruitore) {
    return this.cosmoTPraticaRepository.findOneNotDeleted(CosmoTPraticaSpecifications
        .findByChiaveFruitoreEsterno(idPraticaExt, codiceIpaEnte, idFruitore));
  }


  private List<TagRidottoResponse> setTagsResponse(List<CosmoTTag> tagsOnDB,
      List<TagRidotto> request, String codiceEnte) {
    List<TagRidottoResponse> response = new LinkedList<>();

    request.forEach(tagRequest -> {
      var tag = tagsOnDB.stream()
          .filter(tagOnDB -> tagRequest.getCodice().equals(tagOnDB.getCodice())).findFirst();

      if (tag.isPresent()) {
        response.add(cosmoRPraticaTagsMapper.toDTO(tag.get()));
      } else {
        TagRidottoResponse tagResponse = new TagRidottoResponse();
        tagResponse.setCodice(tagRequest.getCodice());
        tagResponse.setDescrizione(tagRequest.getDescrizione());
        tagResponse.setTipoTag(tagRequest.getTipoTag());
        tagResponse.setWarning(
            "Tag con codice '" + tagRequest.getCodice() + "' non associato all'ente con codice '"
                + codiceEnte + "' e al tipo tag con codice '" + tagRequest.getTipoTag().getCodice()
                + "' pertanto questo tag non e' stato associato alla pratica in questione");
        response.add(tagResponse);
      }

    });
    return response;
  }

  @Override
  public PraticheFruitoreResponse getPratiche(String filter) {

    PraticheFruitoreResponse output = new PraticheFruitoreResponse();

    if(filter == null || filter.isBlank()) {
      throw new BadRequestException("filter non valorizzato");
    }

    FiltroRicercaPraticheFruitoriDTO filtriRicercaPraticheFruitoriDto = ObjectUtils.fromJson(filter, FiltroRicercaPraticheFruitoriDTO.class);

    GenericRicercaParametricaDTO<FiltroRicercaPraticheFruitoriDTO> ricercaParametrica = createRicercaParametrica(filtriRicercaPraticheFruitoriDto);
    UserInfoDTO userInfo = validationCodiceFiscaleFruitori(filtriRicercaPraticheFruitoriDto.getCodiceFiscaleUtente());
    EnteDTO enteDto = validationEnteFruitori(filtriRicercaPraticheFruitoriDto.getCodiceIpaEnte());
    searchFieldsValidationFruitori(filtriRicercaPraticheFruitoriDto);

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(ParametriApplicativo.MAX_PAGE_SIZE).asInteger());

    Page<CosmoTPratica> pagePratiche = cosmoTPraticaRepository
        .findAll(CosmoTPraticaSpecifications.findByFiltersFruitori(filtriRicercaPraticheFruitoriDto, userInfo,
            enteDto.getId(),filtriRicercaPraticheFruitoriDto.getSort()), paging);
   // @formatter:off
    output.setPraticheFruitore(pagePratiche.getContent().stream()
        .map(t -> mapRisultatoRicercaFruitori(t, userInfo))
        .collect(Collectors.toCollection(LinkedList::new)));
  //  @formatter:on

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      SearchUtils.filterFields(output.getPraticheFruitore(),
          Arrays.asList(ricercaParametrica.getFields().split(",")));
    }

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(pagePratiche.getNumber());
    pageInfo.setPageSize(pagePratiche.getSize());
    pageInfo.setTotalElements(Math.toIntExact(pagePratiche.getTotalElements()));
    pageInfo.setTotalPages(pagePratiche.getTotalPages());
    output.setPageInfo(pageInfo);

    return output;
  }

  private EnteDTO validationEnteFruitori(String codiceIpaEnte) {
    return checkCodiceIpaEnte(codiceIpaEnte);
  }

  private UserInfoDTO validationCodiceFiscaleFruitori(String codiceFiscaleUtente) {
    return checkCodiceFiscale(codiceFiscaleUtente);
  }

  private GenericRicercaParametricaDTO<FiltroRicercaPraticheFruitoriDTO> createRicercaParametrica(FiltroRicercaPraticheFruitoriDTO filtriRicercaPraticheFruitoriDto) {
    GenericRicercaParametricaDTO<FiltroRicercaPraticheFruitoriDTO> ricercaParametrica = new GenericRicercaParametricaDTO<>();
    ricercaParametrica.setFilter(filtriRicercaPraticheFruitoriDto);
    ricercaParametrica.setLimit(filtriRicercaPraticheFruitoriDto.getLimit());
    ricercaParametrica.setOffset(filtriRicercaPraticheFruitoriDto.getOffset());
    ricercaParametrica.setPage(filtriRicercaPraticheFruitoriDto.getPage());
    ricercaParametrica.setSize(filtriRicercaPraticheFruitoriDto.getSize());
    ricercaParametrica.setSort(filtriRicercaPraticheFruitoriDto.getSort());
    return ricercaParametrica;
  }

  private void searchFieldsValidationFruitori(FiltroRicercaPraticheFruitoriDTO filter) {
      checkCodiceTipoPratica(filter.getCodiceTipoPratica());
      checkCodiceTag(filter.getCodiceTag());
      checkApiManagerIdFruitore(filter.getApiManagerIdFruitore());
  }

  private void checkApiManagerIdFruitore(String apiManagerIdFruitore) {
    if(apiManagerIdFruitore != null) {
    Optional<CosmoTFruitore> cosmoTFruitore = cosmoTFruitoreRepository.findOneNotDeletedByField(CosmoTFruitore_.apiManagerId, apiManagerIdFruitore);
    if(!cosmoTFruitore.isPresent()) {
      throw new BadRequestException("apiManagerIdFruitore non trovato");
    }
    }

  }

  private void checkCodiceTag(String codiceTag) {
    if(codiceTag != null) {
      Optional<CosmoTTag> cosmoTTag = cosmoTTagRepository.findOneNotDeletedByField(CosmoTTag_.codice, codiceTag);
    if(!cosmoTTag.isPresent()) {
      throw new BadRequestException("codiceTag non trovato");
    }
    }
  }

  private void checkCodiceTipoPratica(String codiceTipoPratica) {
    if(codiceTipoPratica != null) {
      Optional<CosmoDTipoPratica> cosmoDTipoPratica = cosmoTipoPraticaRepository.findOneActiveByField(CosmoDTipoPratica_.codice, codiceTipoPratica);
    if(!cosmoDTipoPratica.isPresent()) {
      throw new BadRequestException("codiceTipoPratica non trovato");
    }
    }
  }

  private UserInfoDTO checkCodiceFiscale(String codiceFiscaleUtente) {
    if(codiceFiscaleUtente == null || StringUtils.isEmpty(codiceFiscaleUtente)) {
      throw new BadRequestException("Campo obbligatorio codiceFiscaleUtente non valorizzato");
    }else {
      Optional<CosmoTUtente> cosmoTUtente = cosmoTUtenteRepository.findOneNotDeletedByField(CosmoTUtente_.codiceFiscale, codiceFiscaleUtente);
      if(!cosmoTUtente.isPresent()) {
        throw new BadRequestException("codiceFiscaleUtente non trovato");
      }
      UserInfoDTO userInfoDto = new UserInfoDTO();
      CosmoTUtente utente = cosmoTUtente.get();
      List<GruppoDTO> gruppi = new ArrayList<>();
      userInfoDto.setId(utente.getId());
      userInfoDto.setCodiceFiscale(utente.getCodiceFiscale());
      for(CosmoTGruppo cosmoTGruppo : utente.getCosmoTGruppos()) {
        GruppoDTO gruppo = new GruppoDTO();
        gruppo.setCodice(cosmoTGruppo.getCodice());
        gruppo.setDescrizione(cosmoTGruppo.getDescrizione());
        gruppo.setId(cosmoTGruppo.getId());
        gruppo.setNome(cosmoTGruppo.getNome());
        gruppi.add(gruppo);
      }
      userInfoDto.setGruppi(gruppi);
      return userInfoDto;
    }
  }

  private EnteDTO checkCodiceIpaEnte(String codiceIpaEnte) {
    if(codiceIpaEnte == null || StringUtils.isEmpty(codiceIpaEnte)) {
      throw new BadRequestException("Campo obbligatorio codiceIpaEnte non valorizzato");
    }else {
      Optional<CosmoTEnte> cosmoTEnte = cosmoTEnteRepository.findOneNotDeletedByField(CosmoTEnte_.codiceIpa, codiceIpaEnte);
      if(!cosmoTEnte.isPresent()) {
        throw new BadRequestException("codiceIpaEnte non trovato");
      }
      EnteDTO enteDTO = new EnteDTO();
      enteDTO.setId(cosmoTEnte.get().getId());
      enteDTO.setNome(cosmoTEnte.get().getNome());
      enteDTO.setTenantId(cosmoTEnte.get().getCodiceIpa());
      return enteDTO;
    }
  }

  private PraticheFruitore mapRisultatoRicercaFruitori(CosmoTPratica praticaDB, UserInfoDTO userInfoDto) {
    var idGruppi = userInfoDto.getGruppi() != null
        ? userInfoDto.getGruppi().stream().map(GruppoDTO::getId).collect(Collectors.toSet())
        : Collections.emptySet();
    return praticaMapper.toPracticeFruitori(praticaDB,userInfoDto, idGruppi);
    }
}

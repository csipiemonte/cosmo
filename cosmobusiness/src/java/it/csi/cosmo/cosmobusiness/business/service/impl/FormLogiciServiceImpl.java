/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.CosmoDChiaveParametroFunzionalitaFormLogico;
import it.csi.cosmo.common.entities.CosmoDFunzionalitaFormLogico;
import it.csi.cosmo.common.entities.CosmoRFormLogicoIstanzaFunzionalita;
import it.csi.cosmo.common.entities.CosmoRFormLogicoIstanzaFunzionalitaPK;
import it.csi.cosmo.common.entities.CosmoRFormLogicoIstanzaFunzionalita_;
import it.csi.cosmo.common.entities.CosmoRIstanzaFormLogicoParametroValore;
import it.csi.cosmo.common.entities.CosmoRIstanzaFormLogicoParametroValorePK;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTFormLogico;
import it.csi.cosmo.common.entities.CosmoTFormLogico_;
import it.csi.cosmo.common.entities.CosmoTIstanzaFunzionalitaFormLogico;
import it.csi.cosmo.common.entities.CosmoTIstanzaFunzionalitaFormLogico_;
import it.csi.cosmo.common.entities.proto.CosmoREntity;
import it.csi.cosmo.common.entities.proto.CosmoREntity_;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.util.ComplexListComparator;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmobusiness.business.service.FormLogiciService;
import it.csi.cosmo.cosmobusiness.config.ParametriApplicativo;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaFormLogicoIstanzaFunzionalitaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaFormLogicoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaFormLogicoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.FiltroRicercaFormLogiciDTO;
import it.csi.cosmo.cosmobusiness.dto.rest.FormLogiciResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.FormLogico;
import it.csi.cosmo.cosmobusiness.dto.rest.PageInfo;
import it.csi.cosmo.cosmobusiness.integration.mapper.FormLogicoMapper;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoDChiaveParametroFunzionalitaFormLogicoRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoDFunzionalitaFormLogicoRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoRFormLogicoIstanzaFunzionalitaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoRIstanzaFormLogicoParametroValoreRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTFormLogicoRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTIstanzaFunzionalitaFormLogicoRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.specifications.CosmoTFormLogicoSearchHandler;

@Service
@Transactional
public class FormLogiciServiceImpl implements FormLogiciService {

  @Autowired
  private CosmoTFormLogicoRepository cosmoTFormLogicoRepository;

  @Autowired
  private CosmoTIstanzaFunzionalitaFormLogicoRepository cosmoTIstanzaFunzionalitaFormLogicoRepository;

  @Autowired
  private CosmoRFormLogicoIstanzaFunzionalitaRepository cosmoRFormLogicoIstanzaFunzionalitaRepository;

  @Autowired
  private CosmoDFunzionalitaFormLogicoRepository cosmoDFunzionalitaFormLogicoRepository;

  @Autowired
  private CosmoRIstanzaFormLogicoParametroValoreRepository cosmoRIstanzaFormLogicoParametroValoreRepository;

  @Autowired
  private CosmoDChiaveParametroFunzionalitaFormLogicoRepository cosmoDChiaveParametroFunzionalitaFormLogicoRepository;

  @Autowired
  private FormLogicoMapper cosmoTFormLogicoMapper;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoTEnteRepository cosmoTEnteRepository;

  @Override
  @Transactional(readOnly = true)
  public FormLogiciResponse getFormLogici(String filter) {
    FormLogiciResponse output = new FormLogiciResponse();

    GenericRicercaParametricaDTO<FiltroRicercaFormLogiciDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaFormLogiciDTO.class);

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(ParametriApplicativo.MAX_PAGE_SIZE).asInteger());

    Page<CosmoTFormLogico> pageResult =
        cosmoTFormLogicoRepository.findAllNotDeleted(new CosmoTFormLogicoSearchHandler()
            .findByFilters(ricercaParametrica.getFilter(), ricercaParametrica.getSort()), paging);

    List<CosmoTFormLogico> entitiesSuDB = pageResult.getContent();

    List<FormLogico> dtos = new LinkedList<>();
    entitiesSuDB.forEach(e -> dtos.add(cosmoTFormLogicoMapper.toLightDTO(e)));
    output.setFormLogici(dtos);

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      it.csi.cosmo.common.util.SearchUtils.filterFields(dtos,
          Arrays.asList(ricercaParametrica.getFields().split(",")));
    }

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(pageResult.getNumber());
    pageInfo.setPageSize(pageResult.getSize());
    pageInfo.setTotalElements(Math.toIntExact(pageResult.getTotalElements()));
    pageInfo.setTotalPages(pageResult.getTotalPages());
    output.setPageInfo(pageInfo);

    return output;
  }

  @Override
  @Transactional(readOnly = true)
  public FormLogico getFormLogico(Long id) {
    CosmoTFormLogico entity =
        cosmoTFormLogicoRepository.findOneNotDeleted(id).orElseThrow(NotFoundException::new);

    return cosmoTFormLogicoMapper.toDTO(entity);
  }

  @Override
  public FormLogico postFormLogici(CreaFormLogicoRequest body) {
    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    CosmoTFormLogico newInstance = new CosmoTFormLogico();
    newInstance.setEseguibileMassivamente(body.isEseguibileMassivamente());
    newInstance.setCodice(clean(body.getCodice()));
    newInstance.setDescrizione(clean(body.getDescrizione()));
    newInstance.setCosmoRFormLogicoIstanzaFunzionalitas(new ArrayList<>());
    newInstance.setWizard(body.isWizard() == null ? false : body.isWizard());
    if (body.getRiferimentoEnte() != null) {
      CosmoTEnte ente = cosmoTEnteRepository.findOneNotDeleted(body.getRiferimentoEnte().getId())
          .orElseThrow(NotFoundException::new);
      checkConflictsWithEnte(CosmoTFormLogico_.codice, clean(body.getCodice()),
          CosmoTFormLogico_.cosmoTEnte, ente, null);
      newInstance.setCosmoTEnte(ente);
    } else {
      checkConflictsWithEnte(CosmoTFormLogico_.codice, clean(body.getCodice()),
          CosmoTFormLogico_.cosmoTEnte, null, null);
    }

    newInstance = cosmoTFormLogicoRepository.save(newInstance);

    aggiornaIstanze(newInstance, body.getIstanzeFunzionalita());

    return cosmoTFormLogicoMapper.toDTO(newInstance);
  }

  @Override
  public FormLogico putFormLogici(Long id, AggiornaFormLogicoRequest body) {
    ValidationUtils.require(id, "id");
    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    CosmoTFormLogico entity =
        cosmoTFormLogicoRepository.findOneNotDeleted(id).orElseThrow(NotFoundException::new);

    entity.setEseguibileMassivamente(body.isEseguibileMassivamente());
    entity.setCodice(clean(body.getCodice()));
    entity.setDescrizione(clean(body.getDescrizione()));
    entity.setWizard(body.isWizard() == null ? false : body.isWizard());
    if (body.getRiferimentoEnte() != null) {
      CosmoTEnte ente = cosmoTEnteRepository.findOneNotDeleted(body.getRiferimentoEnte().getId())
          .orElseThrow(NotFoundException::new);
      checkConflictsWithEnte(CosmoTFormLogico_.codice, clean(body.getCodice()),
          CosmoTFormLogico_.cosmoTEnte, ente, entity.getId());
      entity.setCosmoTEnte(ente);
    } else {
      checkConflictsWithEnte(CosmoTFormLogico_.codice, clean(body.getCodice()),
          CosmoTFormLogico_.cosmoTEnte, null, entity.getId());
      entity.setCosmoTEnte(null);
    }

    aggiornaIstanze(entity, body.getIstanzeFunzionalita());
    cosmoTFormLogicoRepository.save(entity);

    return cosmoTFormLogicoMapper.toDTO(entity);
  }

  @Override
  public void deleteFormLogici(Long id) {
    ValidationUtils.require(id, "id");
    Timestamp data = Timestamp.valueOf(LocalDateTime.now());

    CosmoTFormLogico entity =
        cosmoTFormLogicoRepository.findOneNotDeleted(id).orElseThrow(NotFoundException::new);

    entity.setDtCancellazione(data);
    entity.setUtenteCancellazione(AuditServiceImpl.getPrincipalCode());

    cosmoTFormLogicoRepository.save(entity);
  }

  private void aggiornaIstanze(CosmoTFormLogico entity,
      List<AggiornaFormLogicoIstanzaFunzionalitaRequest> input) {

    checkFunzionalitaEseguiMassivamente(input, entity);

    List<AggiornaFormLogicoIstanzaFunzionalitaRequest> esecuzioneMassivaFunzionalita =
        new LinkedList<>();

    ComplexListComparator
    .compareLists(
        entity.getCosmoRFormLogicoIstanzaFunzionalitas().stream().filter(CosmoREntity::valido)
        .collect(Collectors.toList()),
        input,
        (e, i) -> i.getId() != null && e.valido()
        && e.getId().getIdIstanzaFunzionalita().equals(i.getId()))
    .onElementsInFirstNotInSecond(toDelete -> eliminaFunzionalita(entity, toDelete))
    .onElementsInSecondNotInFirst(i -> {
      esecuzioneMassivaFunzionalita.add(i);
      inserisciFunzionalita(entity, i);
    }).onElementsInBoth((e, a) -> {
      esecuzioneMassivaFunzionalita.add(a);
      aggiornaFunzionalitaEsistente(e, a);
    });

  }

  private void checkFunzionalitaEseguiMassivamente(
      List<AggiornaFormLogicoIstanzaFunzionalitaRequest> esecuzioneMassivaFunzionalita,
      CosmoTFormLogico entity) {

    Map<Boolean, List<AggiornaFormLogicoIstanzaFunzionalitaRequest>> groups =
        esecuzioneMassivaFunzionalita.stream().collect(Collectors
            .partitioningBy(single -> Boolean.TRUE.equals(single.isEseguibileMassivamente())));

    if (Boolean.TRUE.equals(entity.getEseguibileMassivamente()) && groups.get(true).isEmpty()) {
      throw new BadRequestException(
          "Non e' stata indicata nessuna funzionalita' eseguibile massivamente.");
    }

    if (Boolean.TRUE.equals(entity.getEseguibileMassivamente()) && groups.get(true).size() > 1) {
      throw new BadRequestException(
          "E' stata indicata piu' di una funzionalita' eseguibile massivamente.");
    }
  }

  @Override
  public void setFunzionalitaMultipla(Long idFormLogico,
      List<AggiornaFormLogicoIstanzaFunzionalitaRequest> input,
      boolean esecuzioneMultipla) {

    CosmoTFormLogico entity = cosmoTFormLogicoRepository.findOneNotDeleted(idFormLogico)
        .orElseThrow(NotFoundException::new);

    List<AggiornaFormLogicoIstanzaFunzionalitaRequest> esecuzioneMassivaFunzionalita =
        new LinkedList<>();

    ComplexListComparator.compareLists(
        entity.getCosmoRFormLogicoIstanzaFunzionalitas().stream().filter(CosmoREntity::valido)
        .collect(Collectors.toList()),input, (e, i) -> i.getId() != null && e.valido()
        && e.getId().getIdIstanzaFunzionalita().equals(i.getId()))
    .onElementsInBoth((e, a) -> esecuzioneMassivaFunzionalita.add(a));

    Map<Boolean, List<AggiornaFormLogicoIstanzaFunzionalitaRequest>> groups =
        esecuzioneMassivaFunzionalita.stream().collect(Collectors
            .partitioningBy(single -> Boolean.TRUE.equals(single.isEseguibileMassivamente())));

    groups.get(esecuzioneMultipla).forEach(single -> {
      var onDB = cosmoRFormLogicoIstanzaFunzionalitaRepository
          .findOne((root, cq, cb) -> cb.and(cb.isNull(root.get(CosmoREntity_.dtFineVal)),
              cb.equal(root.get(CosmoRFormLogicoIstanzaFunzionalita_.cosmoTFormLogico)
                  .get(CosmoTFormLogico_.id), idFormLogico),
              cb.equal(
                  root.get(CosmoRFormLogicoIstanzaFunzionalita_.cosmoTIstanzaFunzionalitaFormLogico)
                  .get(CosmoTIstanzaFunzionalitaFormLogico_.id),
                  single.getId())));

      if (onDB != null) {
        onDB.setEseguibileMassivamente(
            single.isEseguibileMassivamente() != null ? single.isEseguibileMassivamente()
                : Boolean.FALSE);
        onDB = cosmoRFormLogicoIstanzaFunzionalitaRepository.save(onDB);
      }
    });
  }

  private void inserisciFunzionalita(CosmoTFormLogico entity,
      AggiornaFormLogicoIstanzaFunzionalitaRequest toInsert) {
    Timestamp data = Timestamp.valueOf(LocalDateTime.now());

    ValidationUtils.require(toInsert.getCodice(), "istanza.codice");

    // CosmoTIstanzaFunzionalitaFormLogico istanza = cosmoTIstanzaFunzionalitaFormLogicoRepository
    // .findOneNotDeleted(toInsert.getId()).orElseThrow(NotFoundException::new);
    // si crea una nuova istanza
    CosmoTIstanzaFunzionalitaFormLogico nuovaIstanza = new CosmoTIstanzaFunzionalitaFormLogico();
    CosmoDFunzionalitaFormLogico cosmoDFunzionalitaFormLogico =
        cosmoDFunzionalitaFormLogicoRepository.findOneActive(toInsert.getCodice())
        .orElseThrow(NotFoundException::new);

    nuovaIstanza.setCosmoDFunzionalitaFormLogico(cosmoDFunzionalitaFormLogico);
    nuovaIstanza.setCosmoRFormLogicoIstanzaFunzionalitas(new ArrayList<>());
    nuovaIstanza.setCosmoRIstanzaFormLogicoParametroValores(new ArrayList<>());
    nuovaIstanza.setDescrizione(
        cosmoDFunzionalitaFormLogico.getDescrizione() + " per " + entity.getDescrizione());
    CosmoTIstanzaFunzionalitaFormLogico istanza =
        cosmoTIstanzaFunzionalitaFormLogicoRepository.save(nuovaIstanza);

    // inserisco associazione
    CosmoRFormLogicoIstanzaFunzionalita newRel = new CosmoRFormLogicoIstanzaFunzionalita();
    newRel.setDtInizioVal(data);
    newRel.setCosmoTFormLogico(entity);
    newRel.setCosmoTIstanzaFunzionalitaFormLogico(istanza);
    newRel.setOrdine(toInsert.getOrdine() != null ? toInsert.getOrdine().longValue() : null);
    newRel.setEseguibileMassivamente(false);
    CosmoRFormLogicoIstanzaFunzionalitaPK id = new CosmoRFormLogicoIstanzaFunzionalitaPK();
    id.setIdFormLogico(entity.getId());
    id.setIdIstanzaFunzionalita(istanza.getId());
    newRel.setId(id);
    newRel = cosmoRFormLogicoIstanzaFunzionalitaRepository.save(newRel);
    toInsert.setId(newRel.getId().getIdIstanzaFunzionalita());

    entity.getCosmoRFormLogicoIstanzaFunzionalitas().add(newRel);
    aggiornaParametri(newRel, toInsert);
  }

  private void aggiornaFunzionalitaEsistente(CosmoRFormLogicoIstanzaFunzionalita esistente,
      AggiornaFormLogicoIstanzaFunzionalitaRequest input) {
    esistente.setOrdine(input.getOrdine() != null ? input.getOrdine().longValue() : null);
    cosmoRFormLogicoIstanzaFunzionalitaRepository.save(esistente);

    aggiornaParametri(esistente, input);
  }

  private void aggiornaParametri(CosmoRFormLogicoIstanzaFunzionalita esistente,
      AggiornaFormLogicoIstanzaFunzionalitaRequest input) {
    Timestamp now = Timestamp.valueOf(LocalDateTime.now());

    var parametriNonNulli = input.getParametri().stream()
        .filter(p -> p != null && p.getValore() != null && !StringUtils.isBlank(p.getValore()))
        .collect(Collectors.toList());

    CosmoTIstanzaFunzionalitaFormLogico istanzaDaAggiornare =
        esistente.getCosmoTIstanzaFunzionalitaFormLogico();
    ComplexListComparator
    .compareLists(istanzaDaAggiornare.getCosmoRIstanzaFormLogicoParametroValores(),
        parametriNonNulli,
        (e, i) -> e.valido() && e.getId().getCodiceChiaveParametro().equals(i.getChiave()))
    .onElementsInFirstNotInSecond(daEliminare -> {
      daEliminare.setDtFineVal(now);
      cosmoRIstanzaFormLogicoParametroValoreRepository.save(daEliminare);
      istanzaDaAggiornare.getCosmoRIstanzaFormLogicoParametroValores().remove(daEliminare);

    }).onElementsInSecondNotInFirst(daInserire -> {
      CosmoDChiaveParametroFunzionalitaFormLogico param =
          cosmoDChiaveParametroFunzionalitaFormLogicoRepository
          .findOneActive(daInserire.getChiave()).orElseThrow(NotFoundException::new);
      CosmoRIstanzaFormLogicoParametroValore newRel =
          new CosmoRIstanzaFormLogicoParametroValore();
      newRel.setCosmoDChiaveParametroFunzionalitaFormLogico(param);
      newRel.setCosmoTIstanzaFunzionalitaFormLogico(istanzaDaAggiornare);
      newRel.setDtInizioVal(now);
      newRel.setValoreParametro(daInserire.getValore());
      CosmoRIstanzaFormLogicoParametroValorePK id =
          new CosmoRIstanzaFormLogicoParametroValorePK();
      id.setCodiceChiaveParametro(param.getCodice());
      id.setIdIstanza(istanzaDaAggiornare.getId());
      newRel.setId(id);
      istanzaDaAggiornare.getCosmoRIstanzaFormLogicoParametroValores()
      .add(cosmoRIstanzaFormLogicoParametroValoreRepository.save(newRel));

    }).onElementsInBoth((e, i) -> {
      e.setValoreParametro(i.getValore());
      cosmoRIstanzaFormLogicoParametroValoreRepository.save(e);
    });
  }

  private void eliminaFunzionalita(CosmoTFormLogico entity,
      CosmoRFormLogicoIstanzaFunzionalita toDelete) {
    Timestamp data = Timestamp.valueOf(LocalDateTime.now());

    // elimino l'associazione ma non l'istanza in se (potrebbe essere usata da altri form logici)
    entity.getCosmoRFormLogicoIstanzaFunzionalitas().remove(toDelete);
    toDelete.setDtFineVal(data);
    cosmoRFormLogicoIstanzaFunzionalitaRepository.save(toDelete);
  }

  private void checkConflictsWithEnte(SingularAttribute<CosmoTFormLogico, String> field1,
      String value1, SingularAttribute<CosmoTFormLogico, CosmoTEnte> field2, CosmoTEnte value2,
      Long excludeId) {
    findByTwoFieldsEqualsIgnoreCase(field1, value1, field2, value2, excludeId).ifPresent(other -> {
      if (value2 == null) {
        throw new ConflictException("Codice " + value1 + " gia' associato con ente nullo");
      }
      throw new ConflictException(
          "Codice " + value1 + " gia' associato all' ente " + value2.getNome());
    });
  }

  private Optional<CosmoTFormLogico> findByTwoFieldsEqualsIgnoreCase(
      SingularAttribute<CosmoTFormLogico, String> field1, String value1,
      SingularAttribute<CosmoTFormLogico, CosmoTEnte> field2, CosmoTEnte value2, Long excludeId) {
    return cosmoTFormLogicoRepository.findAllNotDeleted((root, query, cb) -> {
      var condition = cb.and();
      if (value1 != null) {
        condition = cb.equal(cb.upper(root.get(field1)), value1.toUpperCase());
      } else {
        condition = cb.isNull(root.get(field1));
      }
      if (value2 != null) {
        condition = cb.and(condition, cb.equal(root.get(field2), value2));
      } else {
        condition = cb.and(condition, cb.isNull(root.get(field2)));
      }
      if (excludeId != null) {
        condition = cb.and(condition, cb.notEqual(root.get(CosmoTFormLogico_.id), excludeId));
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

}
